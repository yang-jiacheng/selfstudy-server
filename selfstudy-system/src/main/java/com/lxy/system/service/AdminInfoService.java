package com.lxy.system.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.lxy.common.domain.R;
import com.lxy.system.dto.AdminInfoPageDTO;
import com.lxy.system.dto.PersonalEditDTO;
import com.lxy.system.po.AdminInfo;
import com.lxy.system.vo.AdminInfoVO;

import java.util.List;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author jiacheng yang.
 * @since 2022-10-08
 */
public interface AdminInfoService extends IService<AdminInfo> {

    /**
     * 根据用户id查找权限
     *
     * @param userId 用户id
     * @return 权限列表
     */
    List<String> getPermissionsById(Long userId);

    AdminInfoVO getAdminInfoById(Long id);

    void updateAdmin(AdminInfo adminInfo);

    AdminInfoVO updateAdminInfoCache(Long id);

    /**
     * 根据用户名和密码查询
     *
     * @param username 用户名
     * @param password 密码
     * @return AdminInfo
     */
    AdminInfo getAdminInfoByUsernameAndPassword(String username, String password);

    /**
     * 获取系统用户
     *
     * @author jiacheng yang.
     * @since 2025/6/17 19:13
     */
    Page<AdminInfo> getAdminInfoPageList(AdminInfoPageDTO pageDTO);

    void removeCachePermissionInAdminIds(List<Long> adminIds);

    R<Object> updatePersonal(PersonalEditDTO dto);

}
