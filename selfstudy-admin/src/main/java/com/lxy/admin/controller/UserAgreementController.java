package com.lxy.admin.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.lxy.common.annotation.Log;
import com.lxy.common.domain.R;
import com.lxy.common.enums.LogBusinessType;
import com.lxy.common.enums.LogUserType;
import com.lxy.system.dto.AgreementEditDTO;
import com.lxy.system.po.UserAgreement;
import com.lxy.system.service.UserAgreementService;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 用户协议与隐私政策
 * @author jiacheng yang.
 * @since 2022/12/19 10:25
 * @version 1.0
 */

@RequestMapping("/userAgreementManage")
@RestController
@PreAuthorize("hasAuthority('userAgreementManage')")
public class UserAgreementController {

    @Resource
    private UserAgreementService agreementService;

    @PostMapping(value = "/getAgreement-{type}", produces = "application/json")
    public R<String> getAgreement(@PathVariable(value = "type") Integer type){
        LambdaQueryWrapper<UserAgreement> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(UserAgreement::getType,type);
        UserAgreement agreement = agreementService.getOne(wrapper);
        String content = "";
        if(agreement != null){
            content = agreement.getContent();
        }
        return R.ok(content);
    }

    @Log(title = "修改隐私政策与用户协议", businessType = LogBusinessType.UPDATE, userType = LogUserType.ADMIN)
    @PostMapping(value = "/saveAgreement", produces = "application/json")
    public R<Object> saveAgreement(@RequestBody @Valid AgreementEditDTO dto){
        LambdaUpdateWrapper<UserAgreement> wrapper = new LambdaUpdateWrapper<>();
        wrapper.eq(UserAgreement::getType,dto.getType()).set(UserAgreement::getContent,dto.getContent());
        agreementService.update(wrapper);
        return R.ok();
    }

}
