package com.lxy.common.service.impl;

import com.lxy.common.po.Version;
import com.lxy.common.mapper.VersionMapper;
import com.lxy.common.service.VersionService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author jiacheng yang.
 * @since 2023-03-10
 */
@Service
public class VersionServiceImpl extends ServiceImpl<VersionMapper, Version> implements VersionService {

}
