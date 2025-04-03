package com.lxy.admin.controller.user;

import cn.hutool.core.collection.CollUtil;
import com.alibaba.fastjson2.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lxy.admin.service.PoiService;
import com.lxy.common.util.ExcelUtil;
import com.lxy.admin.vo.ExcelErrorInfoVO;
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
import com.lxy.system.vo.ResultVO;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
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
@PreAuthorize("hasAuthority('/userManage/toUserList')")
public class UserManageController {

    private final UserService userService;

    private final StudyStatisticsService statisticsService;

    private final StudyRecordService studyRecordService;

    private final PoiService poiService;

    @Autowired
    public UserManageController(UserService userService, StudyStatisticsService statisticsService,
                                StudyRecordService studyRecordService,PoiService poiService) {
        this.userService = userService;
        this.statisticsService = statisticsService;
        this.studyRecordService = studyRecordService;
        this.poiService = poiService;
    }

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
    public String getUserPageList(@RequestParam(value = "page",required = false,defaultValue = "1") Integer page,
                              @RequestParam(value = "limit",required = false,defaultValue = "10") Integer limit,
                              @RequestParam(value = "name",required = false) String name,
                              @RequestParam(value = "phone",required = false) String phone,
                              @RequestParam(value = "startTime",required = false) String startTime,
                              @RequestParam(value = "endTime",required = false) String endTime){
        Page<User> pg = userService.getUserPageList(name, phone, startTime, endTime, page, limit);
        return JsonUtil.toJson(new LayUiResultVO((int) pg.getTotal(), pg.getRecords()));
    }

    @PostMapping(value = "/getUserById", produces = "application/json")
    @ResponseBody
    public String getUserById(@RequestParam(value = "userId") Integer userId){
        User user = userService.getById(userId);
        user.setProfilePath(ImgConfigUtil.joinUploadUrl(user.getProfilePath()));
        return JsonUtil.toJson(new ResultVO(user));
    }

    @PostMapping(value = "/saveUser", produces = "application/json")
    @ResponseBody
    public String saveUser(@RequestParam(value = "userJson")String userJson){
        User user = JSON.parseObject(userJson, User.class);
        if (user == null){
            return JsonUtil.toJson(new ResultVO(-1,"数据有误！"));
        }
        String phone = user.getPhone();
        if (! cn.hutool.core.util.PhoneUtil.isMobile(phone)) {
            return JsonUtil.toJson(new ResultVO(-1,"手机号格式不正确！"));
        }

        boolean flag = userService.saveUser(user);
        if (!flag){
            return JsonUtil.toJson(new ResultVO(-1,"手机号已被使用！"));
        }
        return JsonUtil.toJson(new ResultVO());
    }

    @PostMapping(value = "/removeUserByIds", produces = "application/json")
    @ResponseBody
    public String removeUserByIds(@RequestParam(value = "jsonIds")String jsonIds){
        List<Integer> ids = JsonUtil.getListType(jsonIds, Integer.class);
        if (CollUtil.isEmpty(ids)){
            return JsonUtil.toJson(new ResultVO(-1,"数据有误！"));
        }
        userService.removeByIds(ids);
        userService.removeUserInfoCacheByIds(ids);
        //删用户其他关联数据...
        statisticsService.remove(new LambdaQueryWrapper<StudyStatistics>().in(StudyStatistics::getUserId,ids));
        studyRecordService.remove(new LambdaQueryWrapper<StudyRecord>().in(StudyRecord::getUserId,ids));
        return JsonUtil.toJson(new ResultVO());
    }


    @GetMapping(value = "/exportUserInExcel",name = "导出用户信息")
    public void exportUserInExcel(HttpServletResponse response){
        String titleName= "团团云自习用户信息";
        String fileName="团团云自习用户信息表.xlsx";
        List<User> users = userService.list();
        Workbook wb = poiService.exportUserInExcel(titleName,users);
        ExcelUtil.exportExcel(response,wb,fileName);
    }

    @GetMapping(value = "/downloadMaterial" ,name = "下载用户导入模板")
    public void downloadMaterial(HttpServletResponse response,@RequestParam(value = "fileName") String fileName){
        OssUtil.downloadOssFile(response,fileName);
    }

    @PostMapping(value = "/importUsersInExcel",name = "导入用户")
    @ResponseBody
    public String importUsersInExcel(@RequestParam("file") MultipartFile file, HttpServletRequest request){
        if (file.isEmpty()){
            return JsonUtil.toJson(new ResultVO(-1, "上传失败"));
        }
        List<ExcelErrorInfoVO> errorList = poiService.importUsersInExcel(file);
        return JsonUtil.toJson(new ResultVO(errorList));
    }


}
