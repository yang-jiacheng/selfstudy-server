package com.lxy.admin.controller;

import com.lxy.common.util.ImgConfigUtil;
import com.lxy.framework.security.util.UserIdUtil;
import com.lxy.system.po.AdminInfo;
import com.lxy.system.service.AdminInfoService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 主页
 *
 * @author jiacheng yang.
 * @version 1.0
 * @since 2022/10/08 20:00
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
    public String index(HttpServletRequest request) {
        long adminId = UserIdUtil.getUserId();
        AdminInfo adminInfo = adminInfoService.getById(adminId);
        adminInfo.setProfilePath(ImgConfigUtil.joinUploadUrl(adminInfo.getProfilePath()));
        request.setAttribute("username", adminInfo.getUsername());
        request.setAttribute("profilePath", adminInfo.getProfilePath());
        request.setAttribute("name", adminInfo.getName());
        return "index";
    }

    @GetMapping("/main")
    public String main() {
        return "main";
    }

}
