package com.lxy.admin.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.lxy.common.domain.R;
import com.lxy.system.po.BusinessConfig;
import com.lxy.system.service.BusinessConfigService;
import com.lxy.system.vo.LayUiResultVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * TODO
 * @author jiacheng yang.
 * @since 2022/12/19 10:20
 * @version 1.0
 */

@RequestMapping("/businessConfigManage")
@Controller
@PreAuthorize("hasAuthority('businessConfigManage')")
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
    public LayUiResultVO getBusinessList(){
        List<BusinessConfig> list = businessConfigService.list(new LambdaQueryWrapper<BusinessConfig>().eq(BusinessConfig::getShowStatus, 1));
        return new LayUiResultVO(list.size(),list);
    }

    @PostMapping(value = "/updateBusiness" , produces = "application/json")
    @ResponseBody
    public R<Object> updateBusiness(@RequestParam("id") Integer id, @RequestParam("value") String value){
        businessConfigService.updateBusinessConfigById(id,value);
        return R.ok();
    }



}
