package com.lxy.admin.controller;

import cn.hutool.core.collection.CollUtil;
import com.alibaba.fastjson2.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lxy.common.domain.R;
import com.lxy.common.util.ExcelUtil;
import com.lxy.system.vo.ExcelErrorInfoVO;
import com.lxy.system.dto.UserPageDTO;
import com.lxy.system.po.StudyRecord;
import com.lxy.system.po.StudyStatistics;
import com.lxy.system.po.User;
import com.lxy.system.service.StudyRecordService;
import com.lxy.system.service.StudyStatisticsService;
import com.lxy.system.service.UserService;
import com.lxy.common.util.ImgConfigUtil;
import com.lxy.common.util.JsonUtil;
import com.lxy.common.util.OssUtil;
import com.lxy.system.vo.LayUiResultVO;

import com.lxy.system.vo.user.UserExportVO;
import jakarta.annotation.Resource;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.util.List;

/**
 * TODO
 * @author jiacheng yang.
 * @since 2022/12/05 18:02
 * @version 1.0
 */

@RequestMapping("/userManage")
@Controller
@PreAuthorize("hasAuthority('userManage')")
public class UserManageController {

    @Resource
    private UserService userService;
    @Resource
    private StudyStatisticsService statisticsService;
    @Resource
    private StudyRecordService studyRecordService;

    @GetMapping("/toUserList")
    public String toUserList(){
        return "userManage/userList";
    }

    @GetMapping("/toSaveUser")
    public String toSaveUser(){
        return "userManage/saveUser";
    }

    @GetMapping("/toErrInfo")
    public String toErrInfo(){
        return "errInfo";
    }

    @PostMapping(value = "/getUserPageList", produces = "application/json")
    @ResponseBody
    public LayUiResultVO getUserPageList(@RequestParam(value = "page",required = false,defaultValue = "1") Integer page,
                              @RequestParam(value = "limit",required = false,defaultValue = "10") Integer limit,
                              @RequestParam(value = "name",required = false) String name,
                              @RequestParam(value = "phone",required = false) String phone,
                              @RequestParam(value = "startTime",required = false) String startTime,
                              @RequestParam(value = "endTime",required = false) String endTime){
        Page<User> pg = userService.getUserPageList(name, phone, startTime, endTime, page, limit);
        return new LayUiResultVO((int) pg.getTotal(), pg.getRecords());
    }

    @PostMapping(value = "/getUserById", produces = "application/json")
    @ResponseBody
    public R<Object> getUserById(@RequestParam(value = "userId") Integer userId){
        User user = userService.getById(userId);
        user.setProfilePath(ImgConfigUtil.joinUploadUrl(user.getProfilePath()));
        return R.ok(user);
    }

    @PostMapping(value = "/saveUser", produces = "application/json")
    @ResponseBody
    public R<Object> saveUser(@RequestParam(value = "userJson")String userJson){
        User user = JSON.parseObject(userJson, User.class);
        if (user == null){
            return R.fail("数据有误！");
        }
        String phone = user.getPhone();
        if (! cn.hutool.core.util.PhoneUtil.isMobile(phone)) {
            return R.fail("手机号格式有误！");
        }

        boolean flag = userService.saveUser(user);
        if (!flag){
            return R.fail("手机号已被使用！");
        }
        return R.ok();
    }

    @PostMapping(value = "/removeUserByIds", produces = "application/json")
    @ResponseBody
    public R<Object> removeUserByIds(@RequestParam(value = "jsonIds")String jsonIds){
        List<Integer> ids = JsonUtil.getListType(jsonIds, Integer.class);
        if (CollUtil.isEmpty(ids)){
            return R.fail("请至少选择一条数据！");
        }
        userService.removeByIds(ids);
        userService.removeUserInfoCacheByIds(ids);
        //删用户其他关联数据...
        statisticsService.remove(new LambdaQueryWrapper<StudyStatistics>().in(StudyStatistics::getUserId,ids));
        studyRecordService.remove(new LambdaQueryWrapper<StudyRecord>().in(StudyRecord::getUserId,ids));
        return R.ok();
    }


    @PostMapping(value = "/exportUserInExcel",name = "导出用户信息")
    @ResponseBody
    public void exportUserInExcel(@RequestBody UserPageDTO dto, HttpServletResponse response){
        List<UserExportVO> list = userService.exportUserInExcel(dto);
        ExcelUtil.exportExcelByRecords("用户信息", list, UserExportVO.class, response);
    }

    @GetMapping(value = "/downloadMaterial" ,name = "下载用户导入模板")
    @ResponseBody
    public void downloadMaterial(HttpServletResponse response){
        OssUtil.downloadOssFile(response,"用户导入模板.xlsx");
    }

    @PostMapping(value = "/importUsersInExcel",name = "导入用户")
    @ResponseBody
    public R<Object> importUsersInExcel(@RequestParam("file") MultipartFile file, HttpServletRequest request){
        if (file.isEmpty()){
            return R.fail("上传文件为空！");
        }
        List<ExcelErrorInfoVO> errorList = userService.importUsersInExcel(file);
        return R.ok(errorList);
    }


}
