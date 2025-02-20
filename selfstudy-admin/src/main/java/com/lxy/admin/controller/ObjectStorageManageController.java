package com.lxy.admin.controller;

import com.github.pagehelper.PageInfo;
import com.lxy.admin.security.util.AdminIdUtil;
import com.lxy.common.domain.R;
import com.lxy.common.dto.ObjectStorageDTO;
import com.lxy.common.dto.PageDTO;
import com.lxy.common.po.ObjectStorage;
import com.lxy.common.service.FeedbackService;
import com.lxy.common.service.ObjectStorageService;
import com.lxy.common.util.JsonUtil;
import com.lxy.common.vo.LayUiResultVO;
import com.lxy.common.vo.ObjectStorageVO;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;

/**
 * @Description: TODO
 * @author: jiacheng yang.
 * @Date: 2024/12/14 15:44
 * @Version: 1.0
 */

@RequestMapping("/objectStorageManage")
@Controller
@PreAuthorize("hasAuthority('/objectStorageManage/toObjectStorageManage')")
public class ObjectStorageManageController {

    @Resource
    private ObjectStorageService objectStorageService;

    @GetMapping("/toObjectStorageManage")
    public String toObjectStorageManage(){
        return "objectStorageManage/objectStorageList";
    }

    @GetMapping("/toSaveObjectStorage")
    public String toSaveObjectStorage(){
        return "objectStorageManage/saveObjectStorage";
    }

    @PostMapping(value = "/getObjectStoragePageList" , produces = "application/json")
    @ResponseBody
    public String  getObjectStoragePageList(PageDTO pageDTO){
        PageInfo<ObjectStorageVO> pg = objectStorageService.getObjectStoragePageList(pageDTO);
        return JsonUtil.toJson(new LayUiResultVO((int) pg.getTotal(),pg.getList()));
    }

    @PostMapping(value = "/saveObjectStorage" , produces = "application/json")
    @ResponseBody
    public R<Object> saveObjectStorage(@RequestBody @Valid ObjectStorageDTO objectStorageDTO){
        int adminId = AdminIdUtil.getAdminId();
        objectStorageService.saveObjectStorage(objectStorageDTO,adminId);
        return R.ok();
    }

    @PostMapping(value = "/deleteObjectStorage" , produces = "application/json")
    @ResponseBody
    public R<Object> deleteObjectStorage(Integer id){
        objectStorageService.deleteObjectStorage(id);
        return R.ok();
    }


}
