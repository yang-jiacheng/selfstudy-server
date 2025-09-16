package com.lxy.admin.controller;

import com.lxy.common.annotation.Log;
import com.lxy.common.domain.CollResult;
import com.lxy.common.domain.R;
import com.lxy.common.enums.LogBusinessType;
import com.lxy.common.enums.LogUserType;
import com.lxy.system.po.Version;
import com.lxy.system.service.VersionService;
import jakarta.annotation.Resource;
import jakarta.validation.constraints.NotNull;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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

    @PostMapping("/getVersionList")
    public R<CollResult<Version>> getVersionList() {
        List<Version> list = versionService.list();
        return R.ok(new CollResult<>(list));
    }

    @PostMapping("/getVersionById")
    public R<Version> getVersionById(@RequestParam(value = "id") Integer id) {
        Version version = versionService.getById(id);
        return R.ok(version);
    }

    @Log(title = "修改APP版本", businessType = LogBusinessType.UPDATE, userType = LogUserType.ADMIN)
    @PostMapping("/saveVersion")
    public R<Object> saveVersion(@RequestBody @NotNull Version version) {
        version.setUpdateTime(new Date());
        versionService.updateById(version);
        return R.ok();
    }
}
