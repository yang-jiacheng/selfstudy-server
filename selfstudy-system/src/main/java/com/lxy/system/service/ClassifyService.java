package com.lxy.system.service;

import com.lxy.common.domain.R;
import com.lxy.system.po.Classify;
import com.baomidou.mybatisplus.extension.service.IService;
import com.lxy.system.vo.ClassifyVO;


import java.util.List;

/**
 * <p>
 * 图书馆 服务类
 * </p>
 *
 * @author jiacheng yang.
 * @since 2022-10-22
 */
public interface ClassifyService extends IService<Classify> {

    /**
     * 根据id获取图书馆信息
     */
    Classify getClassifyById(Integer id);

    /**
     * 更新图书馆信息
     */
    void updateClassify(Classify classify);

    /**
     * 获取图书馆
     */
    List<ClassifyVO> getClassifyList();

    /**
     * 添加图书馆缓存
     */
    void insertClassifyCache(List<ClassifyVO> list);

    /**
     * 删除图书馆缓存
     */
    void removeClassifyCache();

    /**
     * 获取图书馆缓存
     */
    List<ClassifyVO> getClassifyListCache();

    /**
     * 删除图书馆
     * @author jiacheng yang.
     * @since 2025/6/30 16:48
     */
    void removeClassify(Integer id);
}
