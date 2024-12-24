package com.lxy.app.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.lxy.common.constant.ConfigConstants;
import com.lxy.common.po.UserAgreement;
import com.lxy.common.service.BusinessConfigService;
import com.lxy.common.service.UserAgreementService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.HashMap;
import java.util.Map;

/**
 * @Description: TODO
 * @author: jiacheng yang.
 * @Date: 2022/12/21 11:30
 * @Version: 1.0
 */

@RequestMapping("/userAgreement")
@Controller
@Api(tags = "用户协议与隐私政策")
public class UserAgreementController {

    private final UserAgreementService userAgreementService;

    private final BusinessConfigService businessConfigService;

    @Autowired
    public UserAgreementController(UserAgreementService userAgreementService,BusinessConfigService businessConfigService) {
        this.userAgreementService = userAgreementService;
        this.businessConfigService = businessConfigService;
    }

    @ApiOperation(value = "用户协议或隐私政策",  produces = "application/json", notes = "jiacheng yang.")
    @GetMapping("/agreementPolicyInfoPage{type}")
    public String agreementPolicyInfoPage(@PathVariable Integer type, Model model){
        LambdaQueryWrapper<UserAgreement> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(UserAgreement::getType,type);
        UserAgreement one = userAgreementService.getOne(wrapper);
        model.addAttribute("data", one);
        return "agreementPolicyInfo/agreementPolicyInfoPage";
    }

    @ApiOperation(value = "关于软件",  produces = "application/json", notes = "jiacheng yang.")
    @GetMapping("/aboutSoftware")
    public String callMe( Model model){
        String content = businessConfigService.getBusinessConfigValue(ConfigConstants.ABOUT_US);
        String title = "关于软件";
        Map<String ,Object> map = new HashMap<>(2);
        map.put("title",title);
        map.put("content",content);
        model.addAttribute("data", map);
        return "agreementPolicyInfo/agreementPolicyInfoPage";
    }

}
