package com.lxy.admin.controller;

import com.lxy.common.annotation.Log;
import com.lxy.common.domain.R;
import com.lxy.common.enums.LogBusinessType;
import com.lxy.common.enums.LogUserType;
import com.lxy.system.po.Catalog;
import com.lxy.system.po.Classify;
import com.lxy.system.service.CatalogService;
import com.lxy.system.service.ClassifyService;
import com.lxy.common.vo.CatalogTreeVO;
import jakarta.annotation.Resource;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 图书馆管理
 *
 * @author jiacheng yang.
 * @version 1.0
 * @since 2022/11/09 17:12
 */

@RequestMapping("/classifyManage")
@RestController
@PreAuthorize("hasAuthority('classifyManage')")
public class ClassifyManageController {

    @Resource
    private CatalogService catalogService;
    @Resource
    private ClassifyService classifyService;

    @PostMapping(value = "/getClassifyTree", produces = "application/json")
    public R<List<CatalogTreeVO>> getClassifyTree() {
        List<CatalogTreeVO> tree = catalogService.getCatalogTree();
        return R.ok(tree);
    }

    @PostMapping(value = "/getClassifyById", produces = "application/json")
    public R<Classify> getClassifyById(@RequestParam("id") Long id) {
        Classify classify = classifyService.getClassifyById(id);
        return R.ok(classify);
    }

    @PostMapping(value = "/updateClassify", produces = "application/json")
    @Log(title = "修改图书馆", businessType = LogBusinessType.UPDATE, userType = LogUserType.ADMIN)
    public R<Long> updateClassify(@RequestBody Classify classify) {
        return R.ok(classifyService.updateClassify(classify));
    }

    @PostMapping(value = "/removeClassify", produces = "application/json")
    @Log(title = "删除图书馆", businessType = LogBusinessType.DELETE, userType = LogUserType.ADMIN)
    public R<Object> removeClassify(@RequestParam("id") Long id) {
        classifyService.removeClassify(id);
        return R.ok();
    }

    @PostMapping(value = "/removeCatalog", produces = "application/json")
    @Log(title = "删除节点", businessType = LogBusinessType.DELETE, userType = LogUserType.ADMIN)
    public R<Object> removeCatalog(@RequestParam("id") Long id) {
        catalogService.removeCatalog(id);
        return R.ok();
    }

    @PostMapping(value = "/getCatalogById", produces = "application/json")
    public R<Catalog> getCatalogById(@RequestParam("id") Long id) {
        Catalog catalog = catalogService.getById(id);
        return R.ok(catalog);
    }

    @PostMapping(value = "/saveCatalog", produces = "application/json")
    @Log(title = "保存节点", businessType = LogBusinessType.UPDATE, userType = LogUserType.ADMIN)
    public R<Long> saveCatalog(@RequestBody Catalog catalog) {
        return R.ok(catalogService.saveCatalog(catalog));
    }

}
