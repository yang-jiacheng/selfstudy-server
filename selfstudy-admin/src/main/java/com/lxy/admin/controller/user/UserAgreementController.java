package com.lxy.admin.controller.user;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.lxy.system.po.UserAgreement;
import com.lxy.system.service.UserAgreementService;
import com.lxy.common.util.JsonUtil;
import com.lxy.system.vo.ResultVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Description: TODO
 * @author: jiacheng yang.
 * @Date: 2022/12/19 10:25
 * @Version: 1.0
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
    public String getAgreement(){
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
        return JsonUtil.toJson(new ResultVO(map));
    }

    @PostMapping(value = "/saveAgreement", produces = "application/json")
    @ResponseBody
    public String saveAgreement(@RequestParam(value = "type") Integer type,
                                @RequestParam(value = "content") String content){
        LambdaUpdateWrapper<UserAgreement> wrapper = new LambdaUpdateWrapper<>();
        wrapper.eq(UserAgreement::getType,type).set(UserAgreement::getContent,content);
        agreementService.update(wrapper);
        return JsonUtil.toJson(new ResultVO());
    }

}
