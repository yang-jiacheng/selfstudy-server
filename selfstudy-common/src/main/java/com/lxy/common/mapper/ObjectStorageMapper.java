package com.lxy.common.mapper;

import com.lxy.common.dto.PageDTO;
import com.lxy.common.po.ObjectStorage;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lxy.common.vo.ObjectStorageVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 对象存储 Mapper 接口
 * </p>
 *
 * @author jiacheng yang.
 * @since 2024-12-14
 */
@Mapper
public interface ObjectStorageMapper extends BaseMapper<ObjectStorage> {

    List<ObjectStorageVO> getObjectStorageList(@Param("entity") PageDTO pageDTO);

    Integer getObjectStorageList_COUNT(@Param("entity") PageDTO pageDTO);

}
