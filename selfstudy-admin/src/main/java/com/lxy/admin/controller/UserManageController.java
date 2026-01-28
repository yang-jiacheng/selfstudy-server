package com.lxy.admin.controller;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lxy.common.annotation.Log;
import com.lxy.common.enums.dict.LogBusinessType;
import com.lxy.common.enums.dict.LogUserType;
import com.lxy.common.model.R;
import com.lxy.common.util.OssUtil;
import com.lxy.common.util.excel.ExcelUtil;
import com.lxy.common.vo.ExcelErrorInfoVO;
import com.lxy.system.dto.UserPageDTO;
import com.lxy.system.po.User;
import com.lxy.system.service.UserService;
import com.lxy.system.service.UserStatisticsService;
import com.lxy.system.vo.user.UserExportVO;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * 用户管理
 *
 * @author jiacheng yang.
 * @version 1.0
 * @since 2022/12/05 18:02
 */

@RequestMapping("/userManage")
@RestController
@PreAuthorize("hasAuthority('userManage')")
public class UserManageController {

    @Resource
    private UserService userService;
    @Resource
    private UserStatisticsService userStatisticsService;

    /**
     * 获取用户列表
     *
     * @author jiacheng yang.
     * @since 2025/6/25 22:38
     */
    @PostMapping(value = "/getUserPageList", produces = "application/json")
    @PreAuthorize("hasAuthority('userManage:list')")
    public R<Page<User>> getUserPageList(@RequestBody UserPageDTO dto) {
        Page<User> pg = userService.getUserPageList(dto);
        return R.ok(pg);
    }

    @PostMapping(value = "/saveUser", produces = "application/json")
    @Log(title = "保存用户", businessType = LogBusinessType.UPDATE, userType = LogUserType.ADMIN)
    @PreAuthorize("hasAuthority('userManage:save')")
    public R<Object> saveUser(@RequestBody @NotNull User user) {
        String phone = user.getPhone();
        if (!cn.hutool.core.util.PhoneUtil.isMobile(phone)) {
            return R.fail("手机号格式有误！");
        }
        boolean flag = userService.saveUser(user);
        if (!flag) {
            return R.fail("手机号已被使用！");
        }
        return R.ok();
    }

    @PostMapping(value = "/removeUserByIds", produces = "application/json")
    @Log(title = "批量删除用户", businessType = LogBusinessType.DELETE, userType = LogUserType.ADMIN)
    @PreAuthorize("hasAuthority('userManage:deleteBatch')")
    public R<Object> removeUserByIds(@RequestBody @NotEmpty List<Long> ids) {
        userStatisticsService.removeUserByIds(ids);
        return R.ok();
    }

    @PostMapping(value = "/exportUserInExcel")
    @Log(title = "导出用户信息", businessType = LogBusinessType.EXPORT, userType = LogUserType.ADMIN)
    @PreAuthorize("hasAuthority('userManage:export')")
    public void exportUserInExcel(@RequestBody UserPageDTO dto, HttpServletResponse response) {
        List<UserExportVO> list = userService.exportUserInExcel(dto);
        ExcelUtil.exportExcelByRecords("用户信息", list, UserExportVO.class, response);
    }

    @GetMapping(value = "/downloadMaterial", name = "下载用户导入模板")
    @PreAuthorize("hasAuthority('userManage:import')")
    public void downloadMaterial(HttpServletResponse response) {
        OssUtil.downloadOssFile(response, "用户导入模板.xlsx");
    }

    @PostMapping(value = "/importUsersInExcel")
    @Log(title = "导入用户", businessType = LogBusinessType.IMPORT, userType = LogUserType.ADMIN)
    @PreAuthorize("hasAuthority('userManage:import')")
    public R<Object> importUsersInExcel(@RequestParam("file") MultipartFile file, HttpServletRequest request) {
        if (file.isEmpty()) {
            return R.fail("上传文件为空！");
        }
        List<ExcelErrorInfoVO> errorList = userService.importUsersInExcel(file);
        if (CollUtil.isNotEmpty(errorList)) {
            return R.fail(R.FAIL, "导入失败！数据有误", errorList);
        }
        return R.ok();
    }

}
