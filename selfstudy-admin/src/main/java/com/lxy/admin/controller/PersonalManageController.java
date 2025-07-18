package com.lxy.admin.controller;

import cn.hutool.core.util.StrUtil;
import com.lxy.system.dto.PersonalEditDTO;
import com.lxy.system.po.AdminInfo;
import com.lxy.common.domain.R;
import com.lxy.common.util.ImgConfigUtil;
import com.lxy.framework.security.util.UserIdUtil;
import com.lxy.system.service.AdminInfoService;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import static org.bouncycastle.asn1.x500.style.RFC4519Style.name;

/**
 * 个人中心
 * @author jiacheng yang.
 * @since 2022/10/08 20:13
 * @version 1.0
 */

@RequestMapping("/personalManage")
@RestController
@PreAuthorize("hasAuthority('personalManage')")
public class PersonalManageController {

    @Resource
    private AdminInfoService adminInfoService;

    @PostMapping(value = "/updatePersonal", produces = "application/json")
    public R<Object> updatePersonal(@RequestBody @Valid PersonalEditDTO dto) {
        return adminInfoService.updatePersonal(dto);
    }

}
