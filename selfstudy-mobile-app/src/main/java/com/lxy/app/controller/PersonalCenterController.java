package com.lxy.app.controller;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.lxy.common.domain.R;
import com.lxy.common.po.User;
import com.lxy.common.service.PhoneCodeService;
import com.lxy.common.service.UserService;
import com.lxy.app.security.util.UserIdUtil;
import com.lxy.common.util.ImgConfigUtil;
import com.lxy.common.util.JsonUtil;
import com.lxy.common.vo.ResultVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

/**
 * @Description: TODO
 * @author: jiacheng yang.
 * @Date: 2022/12/19 17:27
 * @Version: 1.0
 */

@RequestMapping("/personalCenter")
@RestController
@Api(tags = "个人中心")
public class PersonalCenterController {

    private final UserService userService;

    private final PhoneCodeService phoneCodeService;

    @Autowired
    public PersonalCenterController(UserService userService,PhoneCodeService phoneCodeService) {
        this.userService = userService;
        this.phoneCodeService = phoneCodeService;
    }

    @ApiOperation(value = "获取个人信息", notes = "jiacheng yang.")
    @PostMapping(value = "/getUserInfo" , produces = "application/json")
    public R<Object> getUserInfo(){
        int userId = UserIdUtil.getUserId();
        User user = userService.getUserInfo(userId);
        return R.ok(user);
    }

    @ApiOperation(value = "修改个人信息", notes = "jiacheng yang.")
    @PostMapping(value = "/updateUserInfo" , produces = "application/json")
    public R<Object> updateUserInfo(@ApiParam(value = "昵称|即姓名")@RequestParam(value = "name",required = false) String name,
                                    @ApiParam(value = "性别")@RequestParam(value = "gender",required = false) String gender,
                                    @ApiParam(value = "地址")@RequestParam(value = "address",required = false) String address,
                                    @ApiParam(value = "头像,传相对路径")@RequestParam(value = "profilePath",required = false) String profilePath,
                                    @ApiParam(value = "个人资料背景")@RequestParam(value = "coverPath",required = false) String coverPath){
        int userId = UserIdUtil.getUserId();
        User user = new User(userId,name,profilePath,coverPath,gender,address);
        if(StrUtil.isNotEmpty(gender) || StrUtil.isNotEmpty(address) ||StrUtil.isNotEmpty(profilePath) ||StrUtil.isNotEmpty(coverPath) ||StrUtil.isNotEmpty(name) ){
            user.setUpdateTime(new Date());
            userService.updateById(user);
            userService.removeUserInfoCache(userId);
        }
        return R.ok();
    }

    @ApiOperation(value = "修改密码", notes = "jiacheng yang.")
    @PostMapping(value = "/updatePassword" , produces = "application/json")
    public R<Object> updatePassword(@ApiParam(value = "手机号")@RequestParam(value = "phone") String phone,
                                 @ApiParam(value = "验证码")@RequestParam(value = "verificationCode") String verificationCode,
                                 @ApiParam(value = "新密码 sha256加密")@RequestParam(value = "password") String password){
        User one = userService.getOne(new LambdaQueryWrapper<User>().eq(User::getPhone, phone));
        if (one == null){
            return R.fail(-1,"请先注册！");
        }
        boolean flag = phoneCodeService.checkVerificationCode(phone, verificationCode);
        if (! flag){
            return R.fail(-1,"验证码错误或已失效，请重新获取！");
        }
        User user = userService.getOne(new LambdaQueryWrapper<User>().eq(User::getPhone,phone));
        if (password.equals(user.getPassword())){
            return R.fail(-1,"新密码与旧密码不能相同！");
        }
        //修改验证码为已使用
        phoneCodeService.updateVerificationCodeStatus(phone,verificationCode);
        user.setPassword(password);
        user.setUpdateTime(new Date());
        userService.updateById(user);
        return R.ok();
    }

    @ApiOperation(value = "获取用户信息", notes = "jiacheng yang.")
    @PostMapping(value = "/getUserInfoById" , produces = "application/json")
    public R<Object> getUserInfoById(Integer userId){
        User user = userService.getById(userId);
        user.setPassword("");
        user.setProfilePath(ImgConfigUtil.joinUploadUrl(user.getProfilePath()));
        user.setCoverPath(ImgConfigUtil.joinUploadUrl(user.getCoverPath()));
        return R.ok(user);
    }

}
