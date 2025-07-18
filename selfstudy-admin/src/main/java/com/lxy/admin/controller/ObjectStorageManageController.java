package com.lxy.admin.controller;

import com.github.pagehelper.PageInfo;
import com.lxy.common.domain.PageResult;
import com.lxy.common.domain.R;
import com.lxy.system.dto.ObjectStorageDTO;
import com.lxy.common.dto.PageDTO;
import com.lxy.framework.security.util.UserIdUtil;
import com.lxy.system.service.ObjectStorageService;
import com.lxy.system.vo.LayUiResultVO;
import com.lxy.system.vo.ObjectStorageVO;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import jakarta.annotation.Resource;
import jakarta.validation.Valid;

/**
 * 文件管理
 * @author jiacheng yang.
 * @since 2024/12/14 15:44
 * @version 1.0
 */

@RequestMapping("/objectStorageManage")
@RestController
@PreAuthorize("hasAuthority('objectStorageManage')")
public class ObjectStorageManageController {

    @Resource
    private ObjectStorageService objectStorageService;

    @PostMapping(value = "/getObjectStoragePageList" , produces = "application/json")
    public R<PageResult<ObjectStorageVO>> getObjectStoragePageList(@RequestBody PageDTO pageDTO){
        PageResult<ObjectStorageVO> pg = objectStorageService.getObjectStoragePageList(pageDTO);
        return R.ok(pg);
    }

    @PostMapping(value = "/saveObjectStorage" , produces = "application/json")
    public R<Object> saveObjectStorage(@RequestBody @Valid ObjectStorageDTO objectStorageDTO){
        int adminId = UserIdUtil.getUserId();
        objectStorageService.saveObjectStorage(objectStorageDTO,adminId);
        return R.ok();
    }

    @PostMapping(value = "/deleteObjectStorage" , produces = "application/json")
    public R<Object> deleteObjectStorage(@RequestParam("id") Integer id){
        objectStorageService.deleteObjectStorage(id);
        return R.ok();
    }


}
