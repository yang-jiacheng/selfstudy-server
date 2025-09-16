package com.lxy.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lxy.system.po.Catalog;
import com.lxy.system.vo.CatalogTreeVO;
import com.lxy.system.vo.ClassifyDetailVO;
import com.lxy.system.vo.RoomVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 节点 Mapper 接口
 * </p>
 *
 * @author jiacheng yang.
 * @since 2022-10-22
 */
@Mapper
public interface CatalogMapper extends BaseMapper<Catalog> {

    List<CatalogTreeVO> getTree();

    ClassifyDetailVO getCatalogByClassify(@Param("classifyId") Integer classifyId);

    RoomVO getRoomDetail(@Param("roomId") Integer roomId);
}
