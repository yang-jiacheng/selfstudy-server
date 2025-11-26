package com.lxy.system.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.lxy.common.dto.UserPageDTO;
import com.lxy.system.po.User;
import com.lxy.common.vo.ExcelErrorInfoVO;
import com.lxy.common.vo.user.UserExportVO;
import com.lxy.common.vo.user.UserRankVO;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * <p>
 * 用户表 服务类
 * </p>
 *
 * @author jiacheng yang.
 * @since 2022-10-22
 */
public interface UserService extends IService<User> {

    /**
     * 分页条件查询用户
     */
    Page<User> getUserPageList(UserPageDTO dto);

    /**
     * 保存用户
     */
    boolean saveUser(User user);

    User getUserByPhoneAndPassword(String phone, String password);

    /**
     * 提交学习时长
     */
    boolean submitStudyDuration(Long userId, Integer duration);

    /**
     * 获取用户信息
     */
    User getUserInfo(Long userId);

    /**
     * 获取用户信息缓存
     */
    User getUserInfoCache(Long userId);

    /**
     * 删除用户信息缓存和登录状态
     */
    void removeUserInfoCache(Long userId);

    /**
     * 批量删除用户信息缓存和登录状态
     */
    void removeUserInfoCacheByIds(List<Long> ids);

    /**
     * 新增用户信息缓存
     */
    void insertUserInfoCache(User user);

    /**
     * 获取学习时长总排行榜
     */
    List<UserRankVO> getRankingsTotalDuration();

    /**
     * 获取学习时长总排行榜从数据库
     */
    List<UserRankVO> getRankingsTotalDurationInDb();

    /**
     * 新增总排行缓存
     */
    void insertRankingsCache(List<UserRankVO> users);

    UserRankVO getUserRankingById(Long userId);

    /**
     * 批量新增用户
     *
     * @author jiacheng yang.
     * @since 2025/11/19 17:17
     */
    void insertBatchUser(List<User> userList);

    List<UserExportVO> exportUserInExcel(UserPageDTO dto);

    List<ExcelErrorInfoVO> importUsersInExcel(MultipartFile file);
}
