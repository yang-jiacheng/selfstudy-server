package com.lxy.admin.controller;

import com.lxy.admin.security.util.AdminIdUtil;
import com.lxy.common.po.AdminInfo;
import com.lxy.common.service.AdminInfoService;
import com.lxy.common.util.ImgConfigUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;

/**
 * @Description: TODO
 * @author: jiacheng yang.
 * @Date: 2022/10/08 20:00
 * @Version: 1.0
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
        int adminId = AdminIdUtil.getAdminId();
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

    @GetMapping("/testError")
    public String testError(){
        int i =1;

        int j = i/0;

        return "ok";
    }

}
