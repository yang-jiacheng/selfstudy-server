package com.lxy.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lxy.system.dto.UserPageDTO;
import com.lxy.system.po.User;
import com.lxy.system.vo.user.UserExportVO;
import com.lxy.system.vo.user.UserRankVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 用户表 Mapper 接口
 * </p>
 *
 * @author jiacheng yang.
 * @since 2022-10-22
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {

    List<UserRankVO> getRankingsTotalDuration();

    UserRankVO getUserRankingById(@Param("userId") Integer userId, @Param("studyDay") String studyDay);

    void insertBatchUser(List<User> userList);

    List<UserExportVO> exportUserInExcel(@Param("dto") UserPageDTO dto);
}
