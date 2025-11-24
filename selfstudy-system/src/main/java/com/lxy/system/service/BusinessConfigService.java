package com.lxy.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.lxy.system.po.BusinessConfig;

/**
 * <p>
 * 业务配置表 服务类
 * </p>
 *
 * @author jiacheng yang.
 * @since 2022-10-08
 */
public interface BusinessConfigService extends IService<BusinessConfig> {

    /**
     * 根据bkey获取bvalue
     *
     * @param key bkey
     * @return bvalue
     */
    String getBusinessConfigValue(String key);

    /**
     * 根据bkey查询
     *
     * @return BusinessConfig
     */
    BusinessConfig getBusinessConfigByKey(String key);

    /**
     * 更新或修改
     *
     * @param key 键
     * @param value 值
     * @param description 描述
     * @return BusinessConfig
     */
    BusinessConfig updateBusinessConfigByKey(String key, String value, String description);

    void updateBusinessConfigById(Long id, String value);

}
