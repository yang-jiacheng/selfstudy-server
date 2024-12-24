package com.lxy.admin.controller;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lxy.common.po.User;
import com.lxy.common.po.Version;
import com.lxy.common.service.VersionService;
import com.lxy.common.util.JsonUtil;
import com.lxy.common.vo.LayUiResultVO;
import com.lxy.common.vo.ResultVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

/**
 * @Description: TODO
 * @author: jiacheng yang.
 * @Date: 2023/03/13 15:20
 * @Version: 1.0
 */

@RequestMapping("/versionManage")
@Controller
@Api(tags = "版本控制")
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

    @ApiOperation(value = "获取版本列表", produces = "application/json", notes = "jiacheng yang.")
    @PostMapping("/getVersionList")
    @ResponseBody
    public String getVersionList(){
        List<Version> list = versionService.list();
        return JsonUtil.toJson(new LayUiResultVO(list.size(), list));
    }

    @ApiOperation(value = "获取版本", produces = "application/json", notes = "jiacheng yang.")
    @PostMapping("/getVersionById")
    @ResponseBody
    public String getVersionById(@ApiParam(value = "id",required = true)@RequestParam(value = "id") Integer id){
        Version version = versionService.getById(id);
        return JsonUtil.toJson(new ResultVO(version));
    }

    @ApiOperation(value = "修改版本", produces = "application/json", notes = "jiacheng yang.")
    @PostMapping("/saveVersion")
    @ResponseBody
    public String saveVersion(String versionJson){
        Version version = JSON.parseObject(versionJson, Version.class);
        if (version == null){
            return JsonUtil.toJson(new ResultVO(-1,"数据有误！"));
        }
        version.setUpdateTime(new Date());
        versionService.updateById(version);
        return JsonUtil.toJson(new ResultVO());
    }
}
