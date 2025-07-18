package com.lxy.system.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lxy.system.dto.UserPageDTO;
import com.lxy.system.po.User;
import com.baomidou.mybatisplus.extension.service.IService;
import com.lxy.system.vo.ExcelErrorInfoVO;
import com.lxy.system.vo.user.UserExportVO;
import com.lxy.system.vo.user.UserImportVO;
import com.lxy.system.vo.user.UserRankVO;
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

    User getUserByPhoneAndPassword(String phone,String password);

    /**
     * 提交学习时长
     */
    boolean submitStudyDuration(Integer userId,Integer duration);

    /**
     * 获取用户信息
     */
    User getUserInfo(Integer userId);

    /**
     * 获取用户信息缓存
     */
    User getUserInfoCache(Integer userId);

    /**
     * 删除用户信息缓存和登录状态
     */
    void removeUserInfoCache(Integer userId);

    /**
     * 批量删除用户信息缓存和登录状态
     */
    void removeUserInfoCacheByIds(List<Integer> ids);

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

    UserRankVO getUserRankingById(Integer userId);

    void insertBatchUser(List<UserImportVO> userList);

    void test();

    List<UserExportVO> exportUserInExcel(UserPageDTO dto);

    List<ExcelErrorInfoVO> importUsersInExcel(MultipartFile file);
}
