package com.lxy.app.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lxy.common.constant.ConfigConstants;
import com.lxy.common.domain.R;
import com.lxy.common.po.User;
import com.lxy.common.po.Version;
import com.lxy.common.service.VersionService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Description: 版本控制
 * author: jiacheng yang.
 * Date: 2023/03/10 16:19
 * Version: 1.0
 */

@RequestMapping("/version")
@RestController
public class VersionController {

    private final VersionService versionService;

    @Autowired
    public VersionController(VersionService versionService) {
        this.versionService = versionService;
    }

    /**
     * Description: 检查版本
     * author: jiacheng yang.
     * Date: 2025/02/20 10:19
     * Param: []
     */
    @PostMapping("/checkVersion")
    public R<Object> checkVersion(){
        List<Version> list = versionService.list();
        return R.ok(list.get(0));
    }

}
