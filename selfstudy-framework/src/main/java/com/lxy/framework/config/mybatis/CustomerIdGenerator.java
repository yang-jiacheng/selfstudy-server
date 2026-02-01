package com.lxy.framework.config.mybatis;

import com.baomidou.mybatisplus.core.incrementer.IdentifierGenerator;
import com.lxy.common.util.IdGeneratorUtil;

/**
 * 自定义id生成器
 *
 * @author jiacheng yang.
 * @version 1.0
 * @since 2022/12/20 10:49
 */

public class CustomerIdGenerator implements IdentifierGenerator {

    @Override
    public Long nextId(Object entity) {
        return IdGeneratorUtil.generateId();
    }

}
