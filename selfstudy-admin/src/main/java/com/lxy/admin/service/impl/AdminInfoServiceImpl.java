package com.lxy.admin.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lxy.admin.mapper.AdminInfoMapper;
import com.lxy.admin.po.AdminInfo;
import com.lxy.admin.service.AdminInfoService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author jiacheng yang.
 * @since 2022-10-08
 */
@Service
public class AdminInfoServiceImpl extends ServiceImpl<AdminInfoMapper, AdminInfo> implements AdminInfoService {
    private final AdminInfoMapper adminInfoMapper;

//    private final FileUtil fileUtil;

    @Autowired
    public AdminInfoServiceImpl(AdminInfoMapper adminInfoMapper) {
        this.adminInfoMapper = adminInfoMapper;
    }

    @Override
    public List<String> getPermissionsById(Integer userId) {
        return adminInfoMapper.getPermissionsById(userId);
    }

    @Override
    public AdminInfo getAdminInfoByUsernameAndPassword(String username, String password) {
        LambdaQueryWrapper<AdminInfo> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(AdminInfo::getStatus,1).eq(AdminInfo::getUsername,username).eq(AdminInfo::getPassword, password);
        return this.getOne(wrapper);
    }

    @Override
    public Page<AdminInfo> getAdminInfoList(String username, Integer page, Integer limit, int userId) {
        Page<AdminInfo> pg = new Page<AdminInfo>(page,limit);
        LambdaQueryWrapper<AdminInfo> wrapper = new LambdaQueryWrapper<AdminInfo>();
        wrapper.orderByDesc(AdminInfo::getId);
        wrapper.ne(AdminInfo::getId,userId);
        if (StrUtil.isNotEmpty(username)){
            wrapper.like(AdminInfo::getUsername,username);
        }
        pg = this.page(pg,wrapper);
        List<AdminInfo> records = pg.getRecords();
        //拼接图片路径
        //records.forEach(record -> record.setProfilePath(fileUtil.joinUploadUrl(record.getProfilePath())));
        pg.setRecords(records);
        return pg;
    }
}
