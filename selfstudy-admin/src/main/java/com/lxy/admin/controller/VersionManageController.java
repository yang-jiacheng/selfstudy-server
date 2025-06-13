package com.lxy.admin.controller;

import com.alibaba.fastjson2.JSON;
import com.lxy.common.annotation.Log;
import com.lxy.common.domain.R;
import com.lxy.common.enums.LogBusinessType;
import com.lxy.common.enums.LogUserType;
import com.lxy.system.po.Version;
import com.lxy.system.service.VersionService;
import com.lxy.system.vo.LayUiResultVO;
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
@Controller
@PreAuthorize("hasAuthority('/versionManage/toVersionManage')")
public class VersionManageController {

    private final VersionService versionService;

    @Autowired
    public VersionManageController(VersionService versionService) {
        this.versionService = versionService;
    }

    @GetMapping("/toVersionManage")
    public String toVersionManage(){
        return "version/versionManage";
    }

    @GetMapping("/toUpdateVersion")
    public String toUpdateVersion(){
        return "version/updateVersion";
    }

    @PostMapping("/getVersionList")
    @ResponseBody
    public LayUiResultVO getVersionList(){
        List<Version> list = versionService.list();
        return new LayUiResultVO(list.size(), list);
    }

    @PostMapping("/getVersionById")
    @ResponseBody
    public R<Version> getVersionById(@RequestParam(value = "id") Integer id){
        Version version = versionService.getById(id);
        return R.ok(version);
    }

    @Log(title = "修改APP版本", businessType = LogBusinessType.UPDATE, userType = LogUserType.ADMIN)
    @PostMapping("/saveVersion")
    @ResponseBody
    public R<Object> saveVersion(@RequestParam("versionJson") String versionJson){
        Version version = JSON.parseObject(versionJson, Version.class);
        if (version == null){
            return R.fail("数据有误！");
        }
        version.setUpdateTime(new Date());
        versionService.updateById(version);
        return R.ok();
    }
}
