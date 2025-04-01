package com.lxy.admin.controller.auth;

import cn.hutool.core.util.StrUtil;
import com.lxy.admin.po.AdminInfo;
import com.lxy.common.util.ImgConfigUtil;
import com.lxy.framework.security.util.UserIdUtil;
import com.lxy.admin.service.AdminInfoService;
import com.lxy.common.util.*;
import com.lxy.system.vo.ResultVO;
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
@PreAuthorize("hasAuthority('/personalManage/personalInfo')")
public class PersonalManageController {

    private final AdminInfoService adminInfoService;

    @Autowired
    public PersonalManageController(AdminInfoService adminInfoService) {
        this.adminInfoService = adminInfoService;
    }

    @GetMapping("/personalInfo")
    public String personalInfo(){
        return "adminManage/personalInfo";
    }

    @PostMapping(value = "/getPersonalById", produces = "application/json")
    @ResponseBody
    public String getPersonalById(){
        int adminId = UserIdUtil.getUserId();
        AdminInfo adminInfo = adminInfoService.getById(adminId);
        if (adminInfo!=null){
            adminInfo.setProfilePath(ImgConfigUtil.joinUploadUrl(adminInfo.getProfilePath()));
            adminInfo.setPassword(null);
        }
        return JsonUtil.toJson(new ResultVO(adminInfo));
    }

    @PostMapping(value = "/updatePersonal", produces = "application/json")
    @ResponseBody
    public String updatePersonal(@RequestParam("id") Integer id, @RequestParam("name") String name,
                                 @RequestParam(value = "oldPassword", required = false) String oldPassword,
                                 @RequestParam(value = "newPassword", required = false) String newPassword,
                                 @RequestParam("profilePath") String profilePath) {
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
