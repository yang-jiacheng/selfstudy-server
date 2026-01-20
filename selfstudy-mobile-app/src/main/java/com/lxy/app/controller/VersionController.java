package com.lxy.app.controller;

import com.lxy.common.model.R;
import com.lxy.system.po.Version;
import com.lxy.system.service.VersionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 版本控制
 *
 * @author jiacheng yang.
 * @since 2023/03/10 16:19
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
     * 检查版本
     *
     * @author jiacheng yang.
     * @since 2025/02/20 10:19
     */
    @PostMapping("/checkVersion")
    public R<Version> checkVersion() {
        List<Version> list = versionService.list();
        return R.ok(list.get(0));
    }

}
