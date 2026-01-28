package com.lxy.admin.controller;

import com.lxy.common.annotation.Log;
import com.lxy.common.dto.PageDTO;
import com.lxy.common.enums.dict.LogBusinessType;
import com.lxy.common.enums.dict.LogUserType;
import com.lxy.common.model.PageResult;
import com.lxy.common.model.R;
import com.lxy.framework.security.util.UserIdUtil;
import com.lxy.system.dto.ObjectStorageDTO;
import com.lxy.system.service.ObjectStorageService;
import com.lxy.system.vo.ObjectStorageVO;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * 文件管理
 *
 * @author jiacheng yang.
 * @version 1.0
 * @since 2024/12/14 15:44
 */

@RequestMapping("/objectStorageManage")
@RestController
@PreAuthorize("hasAuthority('objectStorageManage')")
public class ObjectStorageManageController {

    @Resource
    private ObjectStorageService objectStorageService;

    /**
     * 分页查询
     */
    @PreAuthorize("hasAuthority('objectStorageManage:list')")
    @PostMapping(value = "/getObjectStoragePageList", produces = "application/json")
    public R<PageResult<ObjectStorageVO>> getObjectStoragePageList(@RequestBody PageDTO pageDTO) {
        PageResult<ObjectStorageVO> pg = objectStorageService.getObjectStoragePageList(pageDTO);
        return R.ok(pg);
    }

    /**
     * 保存
     */
    @PreAuthorize("hasAuthority('objectStorageManage:save')")
    @PostMapping(value = "/saveObjectStorage", produces = "application/json")
    @Log(title = "保存文件", businessType = LogBusinessType.UPDATE, userType = LogUserType.ADMIN)
    public R<Object> saveObjectStorage(@RequestBody @Valid ObjectStorageDTO objectStorageDTO) {
        long adminId = UserIdUtil.getUserId();
        objectStorageService.saveObjectStorage(objectStorageDTO, adminId);
        return R.ok();
    }

    /**
     * 删除
     */
    @PreAuthorize("hasAuthority('objectStorageManage:delete')")
    @PostMapping(value = "/deleteObjectStorage", produces = "application/json")
    @Log(title = "保存文件", businessType = LogBusinessType.DELETE, userType = LogUserType.ADMIN)
    public R<Object> deleteObjectStorage(@RequestParam("id") Long id) {
        objectStorageService.deleteObjectStorage(id);
        return R.ok();
    }

}
