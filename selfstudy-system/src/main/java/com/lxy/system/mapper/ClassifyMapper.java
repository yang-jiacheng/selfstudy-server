package com.lxy.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lxy.system.po.Classify;
import com.lxy.system.vo.ClassifyVO;
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
