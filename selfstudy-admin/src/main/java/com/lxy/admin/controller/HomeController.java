package com.lxy.admin.controller;

import com.lxy.system.po.AdminInfo;
import com.lxy.framework.security.util.UserIdUtil;
import com.lxy.system.service.AdminInfoService;
import com.lxy.common.util.ImgConfigUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import jakarta.servlet.http.HttpServletRequest;

/**
 * TODO
 * @author jiacheng yang.
 * @since 2022/10/08 20:00
 * @version 1.0
 */

@RequestMapping("/home")
@Controller
public class HomeController {

    private final AdminInfoService adminInfoService;

    @Autowired
    public HomeController(AdminInfoService adminInfoService) {
        this.adminInfoService = adminInfoService;
    }

    @GetMapping("/index")
    public String index(HttpServletRequest request){
        int adminId = UserIdUtil.getUserId();
        AdminInfo adminInfo = adminInfoService.getById(adminId);
        adminInfo.setProfilePath(ImgConfigUtil.joinUploadUrl(adminInfo.getProfilePath()));
        request.setAttribute("username", adminInfo.getUsername());
        request.setAttribute("profilePath", adminInfo.getProfilePath());
        request.setAttribute("name", adminInfo.getName());
        return "index";
    }

    @GetMapping("/main")
    public String main(){
        return "main";
    }


}
