package com.lxy.admin.controller;

import com.alibaba.fastjson2.JSON;
import com.lxy.common.annotation.Log;
import com.lxy.common.domain.CollResult;
import com.lxy.common.domain.R;
import com.lxy.common.enums.LogBusinessType;
import com.lxy.common.enums.LogUserType;
import com.lxy.system.po.Version;
import com.lxy.system.service.VersionService;
import com.lxy.system.vo.LayUiResultVO;
import jakarta.annotation.Resource;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

/**
 * TODO
 * @author jiacheng yang.
 * @since 2023/03/13 15:20
 * @version 1.0
 */

@RequestMapping("/versionManage")
@RestController
@PreAuthorize("hasAuthority('versionManage')")
public class VersionManageController {

    @Resource
    private VersionService versionService;

    @PostMapping("/getVersionList")
    public R<CollResult<Version>> getVersionList(){
        List<Version> list = versionService.list();
        return R.ok(new CollResult<>(list));
    }

    @PostMapping("/getVersionById")
    public R<Version> getVersionById(@RequestParam(value = "id") Integer id){
        Version version = versionService.getById(id);
        return R.ok(version);
    }

    @Log(title = "修改APP版本", businessType = LogBusinessType.UPDATE, userType = LogUserType.ADMIN)
    @PostMapping("/saveVersion")
    public R<Object> saveVersion(@RequestBody @NotNull Version version){
        version.setUpdateTime(new Date());
        versionService.updateById(version);
        return R.ok();
    }
}
