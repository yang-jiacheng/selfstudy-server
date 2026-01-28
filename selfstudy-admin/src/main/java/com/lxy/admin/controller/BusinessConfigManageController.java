package com.lxy.admin.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.lxy.common.annotation.Log;
import com.lxy.common.enums.dict.LogBusinessType;
import com.lxy.common.enums.dict.LogUserType;
import com.lxy.common.model.CollResult;
import com.lxy.common.model.R;
import com.lxy.system.dto.BusinessEditDTO;
import com.lxy.system.po.BusinessConfig;
import com.lxy.system.service.BusinessConfigService;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 参数配置
 *
 * @author jiacheng yang.
 * @version 1.0
 * @since 2022/12/19 10:20
 */

@RequestMapping("/businessConfigManage")
@RestController
@PreAuthorize("hasAuthority('businessConfigManage')")
public class BusinessConfigManageController {

    @Resource
    private BusinessConfigService businessConfigService;

    @PreAuthorize("hasAuthority('businessConfigManage:list')")
    @PostMapping(value = "/getBusinessList", produces = "application/json")
    public R<CollResult<BusinessConfig>> getBusinessList() {
        List<BusinessConfig> list = businessConfigService.list(new LambdaQueryWrapper<BusinessConfig>().eq(BusinessConfig::getShowStatus, 1));
        return R.ok(new CollResult<>(list));
    }

    @PreAuthorize("hasAuthority('businessConfigManage:update')")
    @PostMapping(value = "/updateBusiness", produces = "application/json")
    @Log(title = "修改业务配置", businessType = LogBusinessType.UPDATE, userType = LogUserType.ADMIN)
    public R<Object> updateBusiness(@RequestBody @Valid BusinessEditDTO dto) {
        businessConfigService.updateBusinessConfigById(dto.getId(), dto.getValue());
        return R.ok();
    }


}
