package com.lxy.admin.controller;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.lxy.common.annotation.Log;
import com.lxy.common.domain.R;
import com.lxy.common.enums.LogBusinessType;
import com.lxy.common.enums.LogUserType;
import com.lxy.system.po.UserAgreement;
import com.lxy.system.service.UserAgreementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * TODO
 * @author jiacheng yang.
 * @since 2022/12/19 10:25
 * @version 1.0
 */

@RequestMapping("/userAgreementManage")
@Controller
@PreAuthorize("hasAuthority('/userAgreementManage/toPrivacyPolicy')")
public class UserAgreementController {

    private final UserAgreementService agreementService;

    @Autowired
    public UserAgreementController(UserAgreementService agreementService) {
        this.agreementService = agreementService;
    }

    @GetMapping("/toPrivacyPolicy")
    public String toPrivacyPolicy(){
        return "privacyPolicy";
    }

    @PostMapping(value = "/getAgreement", produces = "application/json")
    @ResponseBody
    public R<Map<String,Object>> getAgreement(){
        List<UserAgreement> list = agreementService.list();
        Map<String ,Object> map = new HashMap<>(2);
        for (UserAgreement agreement : list) {
            Integer type = agreement.getType();
            if (type==1){
                map.put("policy",agreement.getContent());
            }else {
                map.put("agreement",agreement.getContent());
            }
        }
        return R.ok(map);
    }

    @Log(title = "修改隐私政策与用户协议", businessType = LogBusinessType.UPDATE, userType = LogUserType.ADMIN)
    @PostMapping(value = "/saveAgreement", produces = "application/json")
    @ResponseBody
    public R<Object> saveAgreement(@RequestParam(value = "type") Integer type,
                                @RequestParam(value = "content") String content){
        LambdaUpdateWrapper<UserAgreement> wrapper = new LambdaUpdateWrapper<>();
        wrapper.eq(UserAgreement::getType,type).set(UserAgreement::getContent,content);
        agreementService.update(wrapper);
        return R.ok();
    }

}
