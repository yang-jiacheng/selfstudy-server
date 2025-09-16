package com.lxy.system.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lxy.system.mapper.VersionMapper;
import com.lxy.system.po.Version;
import com.lxy.system.service.VersionService;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 版本控制 服务实现类
 * </p>
 *
 * @author jiacheng yang.
 * @since 2025-04-03
 */
@Service
public class VersionServiceImpl extends ServiceImpl<VersionMapper, Version> implements VersionService {

}
