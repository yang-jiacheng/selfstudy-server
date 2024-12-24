package com.lxy.app.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lxy.common.constant.ConfigConstants;
import com.lxy.common.domain.R;
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
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Description: TODO
 * @author: jiacheng yang.
 * @Date: 2023/03/10 16:19
 * @Version: 1.0
 */

@RequestMapping("/version")
@RestController
@Api(tags = "版本控制")
public class VersionController {

    private final VersionService versionService;

    @Autowired
    public VersionController(VersionService versionService) {
        this.versionService = versionService;
    }

    @ApiOperation(value = "检查版本",  produces = "application/json", notes = "jiacheng yang.")
    @PostMapping("/checkVersion")
    public R<Object> checkVersion(){
        List<Version> list = versionService.list();
        return R.ok(list.get(0));
    }

}
