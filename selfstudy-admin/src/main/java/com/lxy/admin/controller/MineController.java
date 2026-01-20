package com.lxy.admin.controller;

import com.lxy.common.model.R;
import com.lxy.common.util.ImgConfigUtil;
import com.lxy.framework.security.util.UserIdUtil;
import com.lxy.system.po.AdminInfo;
import com.lxy.system.service.AdminInfoService;
import com.lxy.system.service.PermissionService;
import com.lxy.common.vo.PermissionTreeVO;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 我的
 *
 * @author jiacheng yang.
 * @version 1.0
 * @since 2022/12/20 10:49
 */

@RestController
@RequestMapping("/mine")
public class MineController {

    @Resource
    private PermissionService permissionService;
    @Resource
    private AdminInfoService adminInfoService;

    /**
     * 根据登录用户获取菜单
     *
     * @author jiacheng yang.
     * @since 2025/6/21 18:33
     */
    @PostMapping(value = "/getMinePermissionTree", produces = "application/json")
    public R<List<PermissionTreeVO>> getMinePermissionTree() {
        long userId = UserIdUtil.getUserId();
        List<PermissionTreeVO> tree = permissionService.getMinePermissionTree(userId);
        return R.ok(tree);
    }

    /**
     * 获取当前登录用户信息
     *
     * @author jiacheng yang.
     * @since 2025/6/21 19:16
     */
    @PostMapping(value = "/getMineInfo", produces = "application/json")
    public R<AdminInfo> getMineInfo() {
        long userId = UserIdUtil.getUserId();
        AdminInfo info = adminInfoService.getById(userId);
        if (info == null) {
            return R.fail("获取当前登录用户信息失败");
        }
        info.setPassword(null);
        info.setProfilePath(ImgConfigUtil.joinUploadUrl(info.getProfilePath()));
        return R.ok(info);
    }

}
