package com.lxy.admin.controller;

import com.lxy.common.annotation.Log;
import com.lxy.common.enums.dict.LogBusinessType;
import com.lxy.common.enums.dict.LogUserType;
import com.lxy.common.model.CollResult;
import com.lxy.common.model.R;
import com.lxy.system.po.Version;
import com.lxy.system.service.VersionService;
import jakarta.annotation.Resource;
import jakarta.validation.constraints.NotNull;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

/**
 * 版本控制
 *
 * @author jiacheng yang.
 * @version 1.0
 * @since 2023/03/13 15:20
 */

@RequestMapping("/versionManage")
@RestController
@PreAuthorize("hasAuthority('versionManage')")
public class VersionManageController {

    @Resource
    private VersionService versionService;

    @PreAuthorize("hasAuthority('versionManage:list')")
    @PostMapping(value = "/getVersionList", produces = "application/json")
    public R<CollResult<Version>> getVersionList() {
        List<Version> list = versionService.list();
        return R.ok(new CollResult<>(list));
    }

    @PreAuthorize("hasAuthority('versionManage:list')")
    @PostMapping(value = "/getVersionById", produces = "application/json")
    public R<Version> getVersionById(@RequestParam(value = "id") Long id) {
        Version version = versionService.getById(id);
        return R.ok(version);
    }

    @PreAuthorize("hasAuthority('versionManage:save')")
    @Log(title = "修改APP版本", businessType = LogBusinessType.UPDATE, userType = LogUserType.ADMIN)
    @PostMapping(value = "/saveVersion", produces = "application/json")
    public R<Object> saveVersion(@RequestBody @NotNull Version version) {
        version.setUpdateTime(new Date());
        versionService.updateById(version);
        return R.ok();
    }
}
