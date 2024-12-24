package com.lxy.admin.controller;

import cn.hutool.core.util.StrUtil;
import com.lxy.admin.security.util.AdminIdUtil;
import com.lxy.common.po.AdminInfo;
import com.lxy.common.service.AdminInfoService;
import com.lxy.common.util.*;
import com.lxy.common.vo.ResultVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

/**
 * @Description: TODO
 * @author: jiacheng yang.
 * @Date: 2022/10/08 20:13
 * @Version: 1.0
 */

@RequestMapping("/personalManage")
@Controller
@Api(tags = "个人信息管理")
@PreAuthorize("hasAuthority('/personalManage/personalInfo')")
public class PersonalManageController {

    private final AdminInfoService adminInfoService;

    @Autowired
    public PersonalManageController(AdminInfoService adminInfoService) {
        this.adminInfoService = adminInfoService;
    }

    @ApiOperation(value = "个人设置页面", produces = "application/json", notes = "jiacheng yang.")
    @GetMapping("/personalInfo")
    public String personalInfo(){
        return "adminManage/personalInfo";
    }

    @ApiOperation(value = "获取", notes = "jiacheng yang.")
    @PostMapping(value = "/getPersonalById", produces = "application/json")
    @ResponseBody
    public String getPersonalById(){
        int adminId = AdminIdUtil.getAdminId();
        AdminInfo adminInfo = adminInfoService.getById(adminId);
        if (adminInfo!=null){
            adminInfo.setProfilePath(ImgConfigUtil.joinUploadUrl(adminInfo.getProfilePath()));
            adminInfo.setPassword(null);
        }
        return JsonUtil.toJson(new ResultVO(adminInfo));
    }

    @ApiOperation(value = "更新", notes = "jiacheng yang.")
    @PostMapping(value = "/updatePersonal", produces = "application/json")
    @ResponseBody
    public String updatePersonal(@ApiParam(value = "id")@RequestParam Integer id, @ApiParam(value = "昵称")@RequestParam String name,
                                 @ApiParam(value = "旧密码")@RequestParam(required = false) String oldPassword,
                                 @ApiParam(value = "新密码")@RequestParam(required = false) String newPassword,
                                 @ApiParam(value = "头像地址")@RequestParam String profilePath) {
        AdminInfo adminInfo = adminInfoService.getById(id);

        String password = adminInfo.getPassword();

        if (StrUtil.isNotBlank(oldPassword) && StrUtil.isNotBlank(newPassword) ){
            if (!password.equals(oldPassword)) {
                return JsonUtil.toJson(new ResultVO(-1, "旧密码错误！"));
            }
            if (password.equals(newPassword)) {
                return JsonUtil.toJson(new ResultVO(-1, "新密码与旧密码不能相同！"));
            }
            adminInfo.setPassword(newPassword);
        }
        adminInfo.setName(name);
        adminInfo.setProfilePath(profilePath);
        adminInfoService.updateById(adminInfo);
        return JsonUtil.toJson(new ResultVO());
    }

}
