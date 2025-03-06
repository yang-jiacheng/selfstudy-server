package com.lxy.admin.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.lxy.common.bo.R;
import com.lxy.common.po.BusinessConfig;
import com.lxy.common.service.BusinessConfigService;
import com.lxy.common.util.JsonUtil;
import com.lxy.common.vo.LayUiResultVO;
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

    @PostMapping(value = "/getBusinessList" , produces = "application/json")
    @ResponseBody
    public String  getBusinessList(){
        List<BusinessConfig> list = businessConfigService.list(new LambdaQueryWrapper<BusinessConfig>().eq(BusinessConfig::getShowStatus, 1));
        return JsonUtil.toJson(new LayUiResultVO(list.size(),list));
    }

    @PostMapping(value = "/updateBusiness" , produces = "application/json")
    @ResponseBody
    public R<Object> updateBusiness(@RequestParam Integer id, @RequestParam String value){
        businessConfigService.updateBusinessConfigById(id,value);
        return R.ok();
    }



}
