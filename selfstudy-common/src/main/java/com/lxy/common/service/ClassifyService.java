package com.lxy.common.service;

import com.lxy.common.po.Classify;
import com.baomidou.mybatisplus.extension.service.IService;
import com.lxy.common.vo.ClassifyVO;
import com.lxy.common.vo.ResultVO;

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
    ResultVO updateClassify(String mainJson);

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

}
