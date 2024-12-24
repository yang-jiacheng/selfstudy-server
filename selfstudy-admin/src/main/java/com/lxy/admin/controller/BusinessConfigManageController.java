package com.lxy.admin.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.lxy.common.domain.R;
import com.lxy.common.po.BusinessConfig;
import com.lxy.common.service.BusinessConfigService;
import com.lxy.common.util.JsonUtil;
import com.lxy.common.vo.LayUiResultVO;
import com.lxy.common.vo.ResultVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Description: TODO
 * @author: jiacheng yang.
 * @Date: 2022/12/19 10:20
 * @Version: 1.0
 */

@RequestMapping("/businessConfigManage")
@Controller
@Api(tags = "通用配置管理")
@PreAuthorize("hasAuthority('/businessConfigManage/toConfigManage')")
public class BusinessConfigManageController {

    private final BusinessConfigService businessConfigService;

    @Autowired
    public BusinessConfigManageController(BusinessConfigService businessConfigService) {
        this.businessConfigService = businessConfigService;
    }

    @GetMapping("/toConfigManage")
    public String toConfigManage(){
        return "businessConfigManage/configList";
    }

    @ApiOperation(value = "获取业务配置列表",  notes = "jiacheng yang.")
    @PostMapping(value = "/getBusinessList" , produces = "application/json")
    @ResponseBody
    public String  getBusinessList(){
        List<BusinessConfig> list = businessConfigService.list(new LambdaQueryWrapper<BusinessConfig>().eq(BusinessConfig::getShowStatus, 1));
        return JsonUtil.toJson(new LayUiResultVO(list.size(),list));
    }

    @ApiOperation(value = "修改配置",  notes = "jiacheng yang.")
    @PostMapping(value = "/updateBusiness" , produces = "application/json")
    @ResponseBody
    public R<Object> updateBusiness(@RequestParam Integer id, @RequestParam String value){
        businessConfigService.updateBusinessConfigById(id,value);
        return R.ok();
    }



}
