package com.lxy.admin.controller.core;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.lxy.common.domain.R;
import com.lxy.system.po.Catalog;
import com.lxy.system.po.Classify;
import com.lxy.system.service.CatalogService;
import com.lxy.system.service.ClassifyService;
import com.lxy.common.util.JsonUtil;
import com.lxy.system.vo.ResultVO;
import com.lxy.system.vo.ZtreeVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Comparator;
import java.util.List;

/**
 * @Description: TODO
 * @author: jiacheng yang.
 * @Date: 2022/11/09 17:12
 * @Version: 1.0
 */

@RequestMapping("/classifyManage")
@Controller
@PreAuthorize("hasAuthority('/classifyManage/toClassifyTree')")
public class ClassifyManageController {

    private final CatalogService catalogService;

    private final ClassifyService classifyService;

    @Autowired
    public ClassifyManageController(CatalogService catalogService, ClassifyService classifyService) {
        this.catalogService = catalogService;
        this.classifyService = classifyService;
    }

    @GetMapping("/toClassifyTree")
    public String toClassifyTree(){
        return "classifyManage/classifyTree";
    }

    @PostMapping(value = "/getClassifyTree", produces = "application/json")
    @ResponseBody
    public R<Object> getClassifyTree(){
        List<ZtreeVO> tree = catalogService.getCatalogTree();
        //按时间倒序
        //details.sort((t1,t2) -> t2.getUpdateTime().compareTo(t1.getUpdateTime()));
        tree.sort(Comparator.comparing(ZtreeVO::getSort));
        return R.ok(tree);
    }

    @PostMapping(value = "/getClassifyById", produces = "application/json")
    @ResponseBody
    public String getClassifyById(@RequestParam("id") Integer id){
        Classify classify = classifyService.getClassifyById(id);
        return JsonUtil.toJson(new ResultVO(classify));
    }

    @PostMapping(value = "/updateClassify", produces = "application/json")
    @ResponseBody
    public String updateClassify(@RequestParam("mainJson")String mainJson){
        ResultVO resultVO = classifyService.updateClassify(mainJson);
        classifyService.removeClassifyCache();
        return JsonUtil.toJson(resultVO);
    }

    @PostMapping(value = "/removeClassify", produces = "application/json")
    @ResponseBody
    public String removeClassify(@RequestParam("id")Integer id){
        classifyService.removeById(id);
        catalogService.remove(new LambdaUpdateWrapper<Catalog>().eq(Catalog::getClassifyId,id));
        classifyService.removeClassifyCache();
        return JsonUtil.toJson(new ResultVO());
    }

    @PostMapping(value = "/removeCatalog", produces = "application/json")
    @ResponseBody
    public String removeCatalog(@RequestParam("id")Integer id){
        catalogService.removeById(id);
        catalogService.remove(new LambdaUpdateWrapper<Catalog>().eq(Catalog::getParentId,id));
        return JsonUtil.toJson(new ResultVO());
    }

    @PostMapping(value = "/getCatalogById", produces = "application/json")
    @ResponseBody
    public String getCatalogById(@RequestParam("id")Integer id){
        Catalog catalog = catalogService.getById(id);
        return JsonUtil.toJson(new ResultVO(catalog));
    }

    @PostMapping(value = "/saveCatalog", produces = "application/json")
    @ResponseBody
    public String saveCatalog(Catalog catalog){
        catalogService.saveCatalog(catalog);
        return JsonUtil.toJson(new ResultVO());
    }

}
