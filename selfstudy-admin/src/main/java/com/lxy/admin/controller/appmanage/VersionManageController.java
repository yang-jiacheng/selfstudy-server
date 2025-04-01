package com.lxy.admin.controller.appmanage;

import com.alibaba.fastjson2.JSON;
import com.lxy.system.po.Version;
import com.lxy.system.service.VersionService;
import com.lxy.common.util.JsonUtil;
import com.lxy.system.vo.LayUiResultVO;
import com.lxy.system.vo.ResultVO;
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
    public String getVersionList(){
        List<Version> list = versionService.list();
        return JsonUtil.toJson(new LayUiResultVO(list.size(), list));
    }

    @PostMapping("/getVersionById")
    @ResponseBody
    public String getVersionById(@RequestParam(value = "id") Integer id){
        Version version = versionService.getById(id);
        return JsonUtil.toJson(new ResultVO(version));
    }

    @PostMapping("/saveVersion")
    @ResponseBody
    public String saveVersion(@RequestParam("versionJson") String versionJson){
        Version version = JSON.parseObject(versionJson, Version.class);
        if (version == null){
            return JsonUtil.toJson(new ResultVO(-1,"数据有误！"));
        }
        version.setUpdateTime(new Date());
        versionService.updateById(version);
        return JsonUtil.toJson(new ResultVO());
    }
}
