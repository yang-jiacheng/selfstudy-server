package com.lxy.system.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lxy.common.constant.RedisKeyConstant;
import com.lxy.common.model.R;
import com.lxy.system.dto.AdminInfoPageDTO;
import com.lxy.system.dto.PersonalEditDTO;
import com.lxy.system.mapper.AdminInfoMapper;
import com.lxy.system.po.AdminInfo;
import com.lxy.system.service.AdminInfoService;
import com.lxy.system.service.redis.RedisService;
import com.lxy.system.vo.AdminInfoVO;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author jiacheng yang.
 * @since 2022-10-08
 */
@Service
public class AdminInfoServiceImpl extends ServiceImpl<AdminInfoMapper, AdminInfo> implements AdminInfoService {

    @Resource
    private AdminInfoMapper adminInfoMapper;
    @Resource
    private RedisService redisService;

    @Override
    public List<String> getPermissionsById(Long userId) {
        return adminInfoMapper.getPermissionsById(userId);
    }

    @Override
    public AdminInfoVO getAdminInfoById(Long id) {
        String key = RedisKeyConstant.getAdminInfo(id);
        AdminInfoVO adminInfoVO = redisService.getObject(key, AdminInfoVO.class);
        if (adminInfoVO == null) {
            adminInfoVO = this.updateAdminInfoCache(id);
        }
        return adminInfoVO;
    }

    @Override
    public void updateAdmin(AdminInfo adminInfo) {
        this.saveOrUpdate(adminInfo);
        Long id = adminInfo.getId();
        this.updateAdminInfoCache(id);
    }

    @Override
    public AdminInfoVO updateAdminInfoCache(Long id) {
        String key = RedisKeyConstant.getAdminInfo(id);
        AdminInfo adminInfo = this.getById(id);
        if (adminInfo == null) {
            return null;
        }
        List<String> permissions = adminInfoMapper.getPermissionsById(id);
        AdminInfoVO adminInfoVO = new AdminInfoVO();
        BeanUtil.copyProperties(adminInfo, adminInfoVO);
        adminInfoVO.setPermissions(permissions);
        // 热点数据永不过期
        redisService.setObject(key, adminInfoVO, -1L, TimeUnit.DAYS);
        return adminInfoVO;
    }

    @Override
    public AdminInfo getAdminInfoByUsernameAndPassword(String username, String password) {
        LambdaQueryWrapper<AdminInfo> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(AdminInfo::getStatus, 1).eq(AdminInfo::getUsername, username).eq(AdminInfo::getPassword, password);
        return this.getOne(wrapper);
    }

    @Override
    public Page<AdminInfo> getAdminInfoPageList(AdminInfoPageDTO pageDTO) {
        Page<AdminInfo> pg = new Page<AdminInfo>(pageDTO.getPage(), pageDTO.getLimit());
        LambdaQueryWrapper<AdminInfo> wrapper = new LambdaQueryWrapper<AdminInfo>();
        wrapper.orderByDesc(AdminInfo::getId);
        wrapper.ne(AdminInfo::getId, pageDTO.getUserId());
        String name = pageDTO.getName();
        if (StrUtil.isNotEmpty(name)) {
            wrapper.like(AdminInfo::getName, name);
        }
        pg = this.page(pg, wrapper);
        List<AdminInfo> records = pg.getRecords();
        if (CollUtil.isNotEmpty(records)) {
            records.forEach(adminInfo -> {
                adminInfo.setPassword(null);
            });
        }
        return pg;
    }

    @Override
    public void removeCachePermissionInAdminIds(List<Long> adminIds) {
        if (CollUtil.isEmpty(adminIds)) {
            return;
        }
        List<String> keys = adminIds.stream().map(RedisKeyConstant::getAdminInfo).collect(Collectors.toList());

        redisService.deleteKeys(keys);
    }

    @Override
    public R<Object> updatePersonal(PersonalEditDTO dto) {
        AdminInfo adminInfo = this.getById(dto.getId());
        String password = adminInfo.getPassword();
        String oldPassword = dto.getOldPassword();
        String newPassword = dto.getNewPassword();
        String name = dto.getName();
        String profilePath = dto.getProfilePath();

        if (StrUtil.isNotBlank(oldPassword) && StrUtil.isNotBlank(newPassword)) {
            if (!password.equals(oldPassword)) {
                return R.fail(-1, "旧密码错误！");
            }
            if (password.equals(newPassword)) {
                return R.fail(-1, "新密码与旧密码不能相同！");
            }
            adminInfo.setPassword(newPassword);
        }
        adminInfo.setName(name);
        adminInfo.setProfilePath(profilePath);
        this.updateById(adminInfo);
        return R.ok();
    }
}
