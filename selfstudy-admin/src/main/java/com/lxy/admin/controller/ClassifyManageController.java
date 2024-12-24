package com.lxy.admin.controller;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.lxy.common.domain.R;
import com.lxy.common.po.Catalog;
import com.lxy.common.po.Classify;
import com.lxy.common.service.CatalogService;
import com.lxy.common.service.ClassifyService;
import com.lxy.common.util.JsonUtil;
import com.lxy.common.vo.ResultVO;
import com.lxy.common.vo.ZtreeVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

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
@Api(tags = "自习室管理")
@PreAuthorize("hasAuthority('/classifyManage/toClassifyTree')")
public class ClassifyManageController {

    private final CatalogService catalogService;

    private final ClassifyService classifyService;

    @Autowired
    public ClassifyManageController(CatalogService catalogService, ClassifyService classifyService) {
        this.catalogService = catalogService;
        this.classifyService = classifyService;
    }

    @ApiOperation(value = "自习室管理", produces = "application/json", notes = "jiacheng yang.")
    @GetMapping("/toClassifyTree")
    public String toClassifyTree(){
        return "classifyManage/classifyTree";
    }

    @ApiOperation(value = "获取自习室数据", notes = "jiacheng yang.")
    @PostMapping(value = "/getClassifyTree", produces = "application/json")
    @ResponseBody
    public R<Object> getClassifyTree(){
        List<ZtreeVO> tree = catalogService.getCatalogTree();
        //按时间倒序
        //details.sort((t1,t2) -> t2.getUpdateTime().compareTo(t1.getUpdateTime()));
        tree.sort(Comparator.comparing(ZtreeVO::getSort));
        return R.ok(tree);
    }

    @ApiOperation(value = "获取图书馆", notes = "jiacheng yang.")
    @PostMapping(value = "/getClassifyById", produces = "application/json")
    @ResponseBody
    public String getClassifyById(Integer id){
        Classify classify = classifyService.getClassifyById(id);
        return JsonUtil.toJson(new ResultVO(classify));
    }

    @ApiOperation(value = "更新图书馆", notes = "jiacheng yang.")
    @PostMapping(value = "/updateClassify", produces = "application/json")
    @ResponseBody
    public String updateClassify(String mainJson){
        ResultVO resultVO = classifyService.updateClassify(mainJson);
        classifyService.removeClassifyCache();
        return JsonUtil.toJson(resultVO);
    }

    @ApiOperation(value = "删除图书馆", notes = "jiacheng yang.")
    @PostMapping(value = "/removeClassify", produces = "application/json")
    @ResponseBody
    public String removeClassify(Integer id){
        classifyService.removeById(id);
        catalogService.remove(new LambdaUpdateWrapper<Catalog>().eq(Catalog::getClassifyId,id));
        classifyService.removeClassifyCache();
        return JsonUtil.toJson(new ResultVO());
    }

    @ApiOperation(value = "删除自习室", notes = "jiacheng yang.")
    @PostMapping(value = "/removeCatalog", produces = "application/json")
    @ResponseBody
    public String removeCatalog(Integer id){
        catalogService.removeById(id);
        catalogService.remove(new LambdaUpdateWrapper<Catalog>().eq(Catalog::getParentId,id));
        return JsonUtil.toJson(new ResultVO());
    }

    @ApiOperation(value = "获取自习室", notes = "jiacheng yang.")
    @PostMapping(value = "/getCatalogById", produces = "application/json")
    @ResponseBody
    public String getCatalogById(Integer id){
        Catalog catalog = catalogService.getById(id);
        return JsonUtil.toJson(new ResultVO(catalog));
    }

    @ApiOperation(value = "保存自习室", notes = "jiacheng yang.")
    @PostMapping(value = "/saveCatalog", produces = "application/json")
    @ResponseBody
    public String saveCatalog(Catalog catalog){
        catalogService.saveCatalog(catalog);
        return JsonUtil.toJson(new ResultVO());
    }

}
