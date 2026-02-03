package com.lxy.admin.controller;

import com.lxy.common.annotation.Log;
import com.lxy.common.enums.dict.LogBusinessType;
import com.lxy.common.enums.dict.UserType;
import com.lxy.common.model.R;
import com.lxy.system.dto.PersonalEditDTO;
import com.lxy.system.service.AdminInfoService;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 个人中心
 *
 * @author jiacheng yang.
 * @version 1.0
 * @since 2022/10/08 20:13
 */

@RequestMapping("/personalManage")
@RestController
@PreAuthorize("hasAuthority('personalManage')")
public class PersonalManageController {

    @Resource
    private AdminInfoService adminInfoService;

    @PreAuthorize("hasAuthority('personalManage:save')")
    @PostMapping(value = "/updatePersonal", produces = "application/json")
    @Log(title = "修改个人信息", businessType = LogBusinessType.UPDATE, userType = UserType.ADMIN)
    public R<Object> updatePersonal(@RequestBody @Valid PersonalEditDTO dto) {
        return adminInfoService.updatePersonal(dto);
    }

}
