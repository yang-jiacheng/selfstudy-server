package com.lxy.system.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lxy.common.dto.PageDTO;
import com.lxy.system.dto.RolePageDTO;
import com.lxy.system.po.Role;
import com.lxy.system.mapper.RoleMapper;
import com.lxy.system.service.RoleService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 角色表 服务实现类
 * </p>
 *
 * @author jiacheng yang.
 * @since 2022-10-08
 */
@Service
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role> implements RoleService {

    @Override
    public Page<Role> getRolePageList(RolePageDTO pageDTO) {
        Integer page = pageDTO.getPage();
        Integer limit = pageDTO.getLimit();
        String name = pageDTO.getName();
        LambdaQueryWrapper<Role> wrapper = new LambdaQueryWrapper<>();
        wrapper.orderByDesc(Role::getCreateTime,Role::getId);
        if (StrUtil.isNotEmpty(name)) {
            wrapper.like(Role::getName,name);
        }
        if (StrUtil.isNotEmpty(pageDTO.getStartTime())){
            wrapper.ge(Role::getCreateTime,pageDTO.getStartTime());
        }
        if (StrUtil.isNotEmpty(pageDTO.getEndTime())){
            wrapper.le(Role::getCreateTime,pageDTO.getEndTime());
        }
        if (page == null && limit == null) {
            List<Role> list = this.list(wrapper);
            Page<Role> pg = new Page<>();
            pg.setRecords(list);
            return pg;
        }
        Page<Role> pg = new Page<>(pageDTO.getPage(), pageDTO.getLimit());
        pg = this.page(pg, wrapper);
        return pg;
    }

}
