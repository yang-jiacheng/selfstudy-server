package com.lxy.system.service;

import com.lxy.system.po.Catalog;
import com.baomidou.mybatisplus.extension.service.IService;
import com.lxy.system.vo.CatalogTreeVO;
import com.lxy.system.vo.ClassifyDetailVO;
import com.lxy.system.vo.RoomVO;
import com.lxy.system.vo.ZtreeVO;

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
     */
    List<CatalogTreeVO> getCatalogTree();

    void saveCatalog(Catalog catalog);

    /**
     * 获取某图书馆下的自习室
     */
    ClassifyDetailVO getCatalogByClassify(Integer classifyId);

    /**
     * 自习室详情
     */
    RoomVO getRoomDetail(Integer roomId);

    /**
     * 删除节点
     * @author jiacheng yang.
     * @since 2025/6/30 16:49
     */
    void removeCatalog(Integer id);
}
