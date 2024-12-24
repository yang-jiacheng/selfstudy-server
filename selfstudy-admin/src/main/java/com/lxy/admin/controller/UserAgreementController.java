package com.lxy.admin.controller;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.lxy.common.po.UserAgreement;
import com.lxy.common.service.UserAgreementService;
import com.lxy.common.util.JsonUtil;
import com.lxy.common.vo.ResultVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
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
@Api(tags = "用户协议与隐私政策")
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

    @ApiOperation(value = "获取隐私政策与用户协议", notes = "jiacheng yang.")
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

    @ApiOperation(value = "修改隐私政策与用户协议", notes = "jiacheng yang.")
    @PostMapping(value = "/saveAgreement", produces = "application/json")
    @ResponseBody
    public String saveAgreement( @ApiParam(value = "类型 1 隐私政策 2用户协议")@RequestParam(value = "type") Integer type,
                                 @ApiParam(value = "内容 ")@RequestParam(value = "content") String content){
        LambdaUpdateWrapper<UserAgreement> wrapper = new LambdaUpdateWrapper<>();
        wrapper.eq(UserAgreement::getType,type).set(UserAgreement::getContent,content);
        agreementService.update(wrapper);
        return JsonUtil.toJson(new ResultVO());
    }

}
