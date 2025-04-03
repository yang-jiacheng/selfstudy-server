package com.lxy.admin.controller.oss;

import com.github.pagehelper.PageInfo;
import com.lxy.common.domain.R;
import com.lxy.system.dto.ObjectStorageDTO;
import com.lxy.system.dto.PageDTO;
import com.lxy.framework.security.util.UserIdUtil;
import com.lxy.system.service.ObjectStorageService;
import com.lxy.common.util.JsonUtil;
import com.lxy.system.vo.LayUiResultVO;
import com.lxy.system.vo.ObjectStorageVO;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import jakarta.annotation.Resource;
import jakarta.validation.Valid;

/**
 * TODO
 * @author jiacheng yang.
 * @since 2024/12/14 15:44
 * @version 1.0
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
        int adminId = UserIdUtil.getUserId();
        objectStorageService.saveObjectStorage(objectStorageDTO,adminId);
        return R.ok();
    }

    @PostMapping(value = "/deleteObjectStorage" , produces = "application/json")
    @ResponseBody
    public R<Object> deleteObjectStorage(@RequestParam("id") Integer id){
        objectStorageService.deleteObjectStorage(id);
        return R.ok();
    }


}
