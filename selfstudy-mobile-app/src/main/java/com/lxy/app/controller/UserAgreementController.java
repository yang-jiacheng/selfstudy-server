package com.lxy.app.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.lxy.common.constant.ConfigConstant;
import com.lxy.system.po.UserAgreement;
import com.lxy.system.service.BusinessConfigService;
import com.lxy.system.service.UserAgreementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.HashMap;
import java.util.Map;

/**
 * 用户协议与隐私政策
 *
 * @author jiacheng yang.
 * @since 2022/12/21 11:30
 */

@RequestMapping("/userAgreement")
@Controller
public class UserAgreementController {

    private final UserAgreementService userAgreementService;

    private final BusinessConfigService businessConfigService;

    @Autowired
    public UserAgreementController(UserAgreementService userAgreementService, BusinessConfigService businessConfigService) {
        this.userAgreementService = userAgreementService;
        this.businessConfigService = businessConfigService;
    }

    /**
     * 用户协议或隐私政策
     *
     * @author jiacheng yang.
     * @since 2025/02/20 10:18
     * @param type 类型
     * @param model 模型
     */
    @GetMapping("/agreementPolicyInfoPage{type}")
    public String agreementPolicyInfoPage(@PathVariable("type") Integer type, Model model) {
        LambdaQueryWrapper<UserAgreement> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(UserAgreement::getType, type);
        UserAgreement one = userAgreementService.getOne(wrapper);
        model.addAttribute("data", one);
        return "agreementPolicyInfo/agreementPolicyInfoPage";
    }

    /**
     * 关于软件
     *
     * @author jiacheng yang.
     * @since 2025/02/20 10:18
     * @param model 模型
     */
    @GetMapping("/aboutSoftware")
    public String callMe(Model model) {
        String content = businessConfigService.getBusinessConfigValue(ConfigConstant.ABOUT_US);
        String title = "关于软件";
        Map<String, Object> map = new HashMap<>(2);
        map.put("title", title);
        map.put("content", content);
        model.addAttribute("data", map);
        return "agreementPolicyInfo/agreementPolicyInfoPage";
    }

}
