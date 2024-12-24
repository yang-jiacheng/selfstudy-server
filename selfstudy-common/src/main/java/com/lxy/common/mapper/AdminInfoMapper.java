package com.lxy.common.mapper;

import com.lxy.common.po.AdminInfo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author jiacheng yang.
 * @since 2022-10-08
 */
@Mapper
public interface AdminInfoMapper extends BaseMapper<AdminInfo> {

    /**
     *
     * @param userId 用户id
     * @return 权限集合
     */
    List<String> getPermissionsById(@Param("userId") Integer userId);

}
