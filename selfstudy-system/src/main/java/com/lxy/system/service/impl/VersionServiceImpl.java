package com.lxy.system.service.impl;

import com.lxy.system.po.Version;
import com.lxy.system.mapper.VersionMapper;
import com.lxy.system.service.VersionService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 版本控制 服务实现类
 * </p>
 *
 * @author jiacheng yang.
 * @since 2025-02-25
 */
@Service
public class VersionServiceImpl extends ServiceImpl<VersionMapper, Version> implements VersionService {

}
