package com.lxy.system.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lxy.common.util.StringUtil;
import com.lxy.common.vo.DictVO;
import com.lxy.system.mapper.SysDictMapper;
import com.lxy.system.po.SysDict;
import com.lxy.system.service.SysDictService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 系统字典表 服务实现类
 * </p>
 *
 * @author jiacheng yang.
 * @since 2026-02-01
 */
@Service
public class SysDictServiceImpl extends ServiceImpl<SysDictMapper, SysDict> implements SysDictService {

    @Override
    public List<DictVO> getDictVoListByType(String dictType) {
        List<SysDict> dictList = this.getDictListByType(dictType);
        if (CollUtil.isEmpty(dictList)) {
            return List.of();
        }
        List<DictVO> dictVOList = new ArrayList<>(dictList.size());
        DictVO dictVO;
        for (SysDict dict : dictList) {
            dictVO = new DictVO();
            BeanUtil.copyProperties(dict, dictVO);
            dictVO.setValue(dict.getDictValue());
            dictVO.setLabel(dict.getDictName());
            dictVOList.add(dictVO);
        }
        return dictVOList;
    }

    @Override
    public List<SysDict> getDictListByType(String dictType) {
        LambdaQueryWrapper<SysDict> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SysDict::getDictType, dictType);
        return this.list(wrapper);
    }

    @Override
    public SysDict getDictByTypeAndValue(String dictType, String dictValue) {
        LambdaQueryWrapper<SysDict> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SysDict::getDictType, dictType).eq(SysDict::getDictValue, dictValue);
        return this.getOne(wrapper);
    }

    @Override
    public String getDictNameByTypeAndValue(String dictType, String dictValue) {
        SysDict sysDict = this.getDictByTypeAndValue(dictType, dictValue);
        if (sysDict != null) {
            return sysDict.getDictName();
        }
        return StringUtil.EMPTY;
    }

    @Override
    public String getDictByTypeAndName(String dictType, String dictName) {
        List<SysDict> dictList = this.getDictListByType(dictType);
        if (CollUtil.isEmpty(dictList)) {
            return StringUtil.EMPTY;
        }
        return dictList.stream().filter(item -> item.getDictName().equals(dictName)).findFirst()
            .map(SysDict::getDictValue).orElse(StringUtil.EMPTY);
    }
}
