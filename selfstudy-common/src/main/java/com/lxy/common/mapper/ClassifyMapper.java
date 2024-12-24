package com.lxy.common.mapper;

import com.lxy.common.po.Classify;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lxy.common.vo.ClassifyVO;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * <p>
 * 图书馆 Mapper 接口
 * </p>
 *
 * @author jiacheng yang.
 * @since 2022-10-22
 */
@Mapper
public interface ClassifyMapper extends BaseMapper<Classify> {

    List<ClassifyVO> queryList();

}
