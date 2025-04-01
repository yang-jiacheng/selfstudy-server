package com.lxy.system.mapper;

import com.lxy.system.po.User;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lxy.system.vo.UserImportVO;
import com.lxy.system.vo.UserRankVO;
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

    UserRankVO getUserRankingById(@Param("userId") Integer userId,@Param("studyDay") String studyDay);

    void insertBatchUser(List<UserImportVO> userList);
}
