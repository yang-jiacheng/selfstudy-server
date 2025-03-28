package com.lxy.admin.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lxy.admin.po.AdminInfo;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author jiacheng yang.
 * @since 2022-10-08
 */
public interface AdminInfoService extends IService<AdminInfo> {

    /**
     * 根据用户id查找权限
     * @param userId 用户id
     * @return 权限列表
     */
    List<String> getPermissionsById(Integer userId);

    /**
     * 根据用户名和密码查询
     * @param username 用户名
     * @param password 密码
     * @return AdminInfo
     */
    AdminInfo getAdminInfoByUsernameAndPassword(String username ,String password);

    /**
     * 条件查询系统用户
     * @param username 用户名
     * @param page 当前页
     * @param limit 每页数量
     * @return Page<AdminInfo>
     */
    Page<AdminInfo> getAdminInfoList(String username, Integer page, Integer limit, int userId);

}
