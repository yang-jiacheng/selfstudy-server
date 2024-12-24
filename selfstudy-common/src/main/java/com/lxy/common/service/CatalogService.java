package com.lxy.common.service;

import com.lxy.common.po.Catalog;
import com.baomidou.mybatisplus.extension.service.IService;
import com.lxy.common.vo.ClassifyDetailVO;
import com.lxy.common.vo.RoomVO;
import com.lxy.common.vo.ZtreeVO;

import java.util.List;

/**
 * <p>
 * 节点 服务类
 * </p>
 *
 * @author jiacheng yang.
 * @since 2022-10-22
 */
public interface CatalogService extends IService<Catalog> {

    /**
     * 获取图书馆及其子节点树状结构
     * @return ZtreeVO集合
     */
    List<ZtreeVO> getCatalogTree();

    void saveCatalog(Catalog catalog);

    /**
     * 获取某图书馆下的自习室
     */
    ClassifyDetailVO getCatalogByClassify(Integer classifyId);

    /**
     * 自习室详情
     */
    RoomVO getRoomDetail(Integer roomId);
}
