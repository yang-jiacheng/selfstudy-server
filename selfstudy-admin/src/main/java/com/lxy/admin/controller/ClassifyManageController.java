package com.lxy.admin.controller;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.lxy.common.annotation.Log;
import com.lxy.common.domain.R;
import com.lxy.common.enums.LogBusinessType;
import com.lxy.common.enums.LogUserType;
import com.lxy.system.po.Catalog;
import com.lxy.system.po.Classify;
import com.lxy.system.service.CatalogService;
import com.lxy.system.service.ClassifyService;

import com.lxy.system.vo.CatalogTreeVO;
import com.lxy.system.vo.ZtreeVO;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.Comparator;
import java.util.List;

/**
 * TODO
 * @author jiacheng yang.
 * @since 2022/11/09 17:12
 * @version 1.0
 */

@RequestMapping("/classifyManage")
@RestController
@PreAuthorize("hasAuthority('classifyManage')")
public class ClassifyManageController {

    @Resource
    private CatalogService catalogService;
    @Resource
    private ClassifyService classifyService;


    @GetMapping("/toClassifyTree")
    public String toClassifyTree(){
        return "classifyManage/classifyTree";
    }

    @PostMapping(value = "/getClassifyTree", produces = "application/json")
    public R<List<CatalogTreeVO>> getClassifyTree(){
        List<CatalogTreeVO> tree = catalogService.getCatalogTree();
        return R.ok(tree);
    }

    @PostMapping(value = "/getClassifyById", produces = "application/json")
    public R<Classify> getClassifyById(@RequestParam("id") Integer id){
        Classify classify = classifyService.getClassifyById(id);
        return R.ok(classify);
    }

    @PostMapping(value = "/updateClassify", produces = "application/json")
    @Log(title = "修改图书馆", businessType = LogBusinessType.UPDATE, userType = LogUserType.ADMIN)
    public R<Object> updateClassify(@RequestBody Classify classify){
        classifyService.updateClassify(classify);
        return R.ok();
    }

    @PostMapping(value = "/removeClassify", produces = "application/json")
    @Log(title = "删除图书馆", businessType = LogBusinessType.DELETE, userType = LogUserType.ADMIN)
    public R<Object> removeClassify(@RequestParam("id")Integer id){
        classifyService.removeClassify(id);
        return R.ok();
    }

    @PostMapping(value = "/removeCatalog", produces = "application/json")
    @Log(title = "删除节点", businessType = LogBusinessType.DELETE, userType = LogUserType.ADMIN)
    public R<Object> removeCatalog(@RequestParam("id")Integer id){
        catalogService.removeCatalog(id);
        return R.ok();
    }

    @PostMapping(value = "/getCatalogById", produces = "application/json")
    public R<Catalog> getCatalogById(@RequestParam("id")Integer id){
        Catalog catalog = catalogService.getById(id);
        return R.ok(catalog);
    }

    @PostMapping(value = "/saveCatalog", produces = "application/json")
    @Log(title = "保存节点", businessType = LogBusinessType.UPDATE, userType = LogUserType.ADMIN)
    public R<Object> saveCatalog(Catalog catalog){
        catalogService.saveCatalog(catalog);
        return R.ok();
    }

}
