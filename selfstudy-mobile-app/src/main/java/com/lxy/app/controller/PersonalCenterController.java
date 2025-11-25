package com.lxy.app.controller;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.lxy.common.domain.R;
import com.lxy.common.util.ImgConfigUtil;
import com.lxy.framework.security.util.UserIdUtil;
import com.lxy.system.po.User;
import com.lxy.system.service.PhoneCodeService;
import com.lxy.system.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

/**
 * 个人中心
 *
 * @author jiacheng yang.
 * @since 2022/12/19 17:27
 * @version 1.0
 */

@RequestMapping("/personalCenter")
@RestController
public class PersonalCenterController {

    private final UserService userService;

    private final PhoneCodeService phoneCodeService;

    @Autowired
    public PersonalCenterController(UserService userService, PhoneCodeService phoneCodeService) {
        this.userService = userService;
        this.phoneCodeService = phoneCodeService;
    }

    /**
     * 获取个人信息
     *
     * @author jiacheng yang.
     * @since 2025/02/20 10:26
     */
    @PostMapping(value = "/getUserInfo", produces = "application/json")
    public R<User> getUserInfo() {
        long userId = UserIdUtil.getUserId();
        User user = userService.getUserInfo(userId);
        return R.ok(user);
    }

    /**
     * 修改个人信息
     *
     * @author jiacheng yang.
     * @since 2025/02/20 10:27
     */
    @PostMapping(value = "/updateUserInfo", produces = "application/json")
    public R<Object> updateUserInfo(@RequestParam(value = "name", required = false) String name,
        @RequestParam(value = "gender", required = false) String gender,
        @RequestParam(value = "address", required = false) String address,
        @RequestParam(value = "profilePath", required = false) String profilePath,
        @RequestParam(value = "coverPath", required = false) String coverPath) {
        long userId = UserIdUtil.getUserId();
        User user = new User(userId, name, profilePath, coverPath, gender, address);
        if (StrUtil.isNotEmpty(gender) || StrUtil.isNotEmpty(address) || StrUtil.isNotEmpty(profilePath)
            || StrUtil.isNotEmpty(coverPath) || StrUtil.isNotEmpty(name)) {
            user.setUpdateTime(new Date());
            userService.updateById(user);
            userService.removeUserInfoCache(userId);
        }
        return R.ok();
    }

    /**
     * 修改密码
     *
     * @author jiacheng yang.
     * @since  2025/02/20 10:27
     */
    @PostMapping(value = "/updatePassword", produces = "application/json")
    public R<Object> updatePassword(@RequestParam(value = "phone") String phone,
        @RequestParam(value = "verificationCode") String verificationCode,
        @RequestParam(value = "password") String password) {
        User one = userService.getOne(new LambdaQueryWrapper<User>().eq(User::getPhone, phone));
        if (one == null) {
            return R.fail(-1, "请先注册！");
        }
        boolean flag = phoneCodeService.checkVerificationCode(phone, verificationCode);
        if (!flag) {
            return R.fail(-1, "验证码错误或已失效，请重新获取！");
        }
        User user = userService.getOne(new LambdaQueryWrapper<User>().eq(User::getPhone, phone));
        if (password.equals(user.getPassword())) {
            return R.fail(-1, "新密码与旧密码不能相同！");
        }
        // 修改验证码为已使用
        phoneCodeService.updateVerificationCodeStatus(phone, verificationCode);
        user.setPassword(password);
        user.setUpdateTime(new Date());
        userService.updateById(user);
        return R.ok();
    }

    /**
     * 获取用户信息
     *
     * @author jiacheng yang.
     * @since 2025/02/20 10:28
     */
    @PostMapping(value = "/getUserInfoById", produces = "application/json")
    public R<User> getUserInfoById(@RequestParam(value = "userId") Long userId) {
        User user = userService.getById(userId);
        user.setPassword("");
        user.setProfilePath(ImgConfigUtil.joinUploadUrl(user.getProfilePath()));
        user.setCoverPath(ImgConfigUtil.joinUploadUrl(user.getCoverPath()));
        return R.ok(user);
    }

}
