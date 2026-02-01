package com.lxy.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.lxy.common.vo.DictVO;
import com.lxy.system.po.SysDict;

import java.util.List;

/**
 * <p>
 * 系统字典表 服务类
 * </p>
 *
 * @author jiacheng yang.
 * @since 2026-02-01
 */
public interface SysDictService extends IService<SysDict> {

    /**
     * 根据字典类型查询字典信息VO
     *
     * @param dictType 字典类型
     * @return DictVO列表
     */
    List<DictVO> getDictVoListByType(String dictType);

    /**
     * 根据字典类型查询字典信息
     *
     * @param dictType 字典类型
     * @return DictVO列表
     */
    List<SysDict> getDictListByType(String dictType);

    /**
     * 根据字典类型和字典值查询字典信息
     *
     * @param dictType 字典类型
     * @param dictValue 字典值
     * @return 字典信息
     */
    SysDict getDictByTypeAndValue(String dictType, String dictValue);

    /**
     * 根据字典类型和字典值查询字典名称
     *
     * @param dictType 字典类型
     * @param dictValue 字典值
     * @return 字典名称
     */
    String getDictNameByTypeAndValue(String dictType, String dictValue);

    /**
     * 根据字典类型和字典名称查询字典值
     *
     * @param dictType 字典类型
     * @param dictName 字典名称
     * @return 字典信息
     */
    String getDictByTypeAndName(String dictType, String dictName);

}
