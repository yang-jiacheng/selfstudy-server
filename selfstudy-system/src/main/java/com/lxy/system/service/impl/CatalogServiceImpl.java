package com.lxy.system.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lxy.common.util.ImgConfigUtil;
import com.lxy.system.mapper.CatalogMapper;
import com.lxy.system.po.Catalog;
import com.lxy.system.po.StudyRecord;
import com.lxy.system.service.CatalogService;
import com.lxy.system.service.StudyRecordService;
import com.lxy.system.vo.CatalogTreeVO;
import com.lxy.system.vo.CatalogVO;
import com.lxy.system.vo.ClassifyDetailVO;
import com.lxy.system.vo.RoomVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.Date;
import java.util.List;

/**
 * <p>
 * 节点 服务实现类
 * </p>
 *
 * @author jiacheng yang.
 * @since 2022-10-22
 */
@Service
public class CatalogServiceImpl extends ServiceImpl<CatalogMapper, Catalog> implements CatalogService {

    private final CatalogMapper catalogMapper;

    private final StudyRecordService studyRecordService;

    @Autowired
    public CatalogServiceImpl(CatalogMapper catalogMapper, StudyRecordService studyRecordService) {
        this.catalogMapper = catalogMapper;
        this.studyRecordService = studyRecordService;
    }

    @Override
    public List<CatalogTreeVO> getCatalogTree() {
        List<CatalogTreeVO> tree = catalogMapper.getTree();
        tree = CatalogTreeVO.buildTree(tree);// 按时间倒序
        // details.sort((t1,t2) -> t2.getUpdateTime().compareTo(t1.getUpdateTime()));
        // tree.sort(Comparator.comparing(CatalogTreeVO::getSort));
        tree.sort((a, b) -> {
            Integer sortA = a.getSort() == null ? 0 : a.getSort();
            Integer sortB = b.getSort() == null ? 0 : b.getSort();
            return sortA.compareTo(sortB);
        });
        return tree;
    }

    @Override
    public Long saveCatalog(Catalog catalog) {
        Integer level = catalog.getLevel();
        if (level == 1) {
            catalog.setParentId(-1L);
            catalog.setPersonCount(0);
        } else {
            Long parentId = catalog.getParentId();
            Catalog parent = this.getById(parentId);
            catalog.setClassifyId(parent.getClassifyId());
        }
        Long id = catalog.getId();
        if (id == null) {
            catalog.setCreateTime(new Date());
        }
        catalog.setUpdateTime(new Date());
        this.saveOrUpdate(catalog);
        return catalog.getId();
    }

    @Override
    public ClassifyDetailVO getCatalogByClassify(Long classifyId) {
        ClassifyDetailVO classify = catalogMapper.getCatalogByClassify(classifyId);
        // 设置头像和封面
        classify.setIconPath(ImgConfigUtil.joinUploadUrl(classify.getIconPath()));
        classify.setCoverPath(ImgConfigUtil.joinUploadUrl(classify.getCoverPath()));
        // 设置正在自习的人数
        List<CatalogVO> rooms = classify.getRooms();
        if (CollUtil.isNotEmpty(rooms)) {
            // 排序
            rooms.sort(Comparator.comparing(CatalogVO::getSort));
            // questions.sort((a, b) -> {
            // Integer sortA = a.getpId() == null ? 0 : a.getpId();
            // Integer sortB = b.getpId() == null ? 0 : b.getpId();
            // return sortA.compareTo(sortB);
            // });
            this.updateCurrCount(classifyId, rooms);
            classify.setRooms(rooms);
        }

        return classify;
    }

    @Override
    public RoomVO getRoomDetail(Long roomId) {
        return catalogMapper.getRoomDetail(roomId);
    }

    @Override
    public void removeCatalog(Long id) {
        this.removeById(id);
        this.remove(new LambdaUpdateWrapper<Catalog>().eq(Catalog::getParentId, id));
    }

    /**
     * 设置自习室当前人数
     */
    private void updateCurrCount(Long classifyId, List<CatalogVO> rooms) {
        // 正在自习的记录
        List<StudyRecord> records = studyRecordService.getRecordsByStatusAndClassIfy(classifyId);
        for (CatalogVO room : rooms) {
            // 层级为2的是自习室
            if (room.getLevel() == 2) {
                Long catalogId = room.getCatalogId();
                // 正在自习的人数
                int count = 0;
                for (StudyRecord record : records) {
                    if (catalogId.equals(record.getCatalogId())) {
                        count++;
                    }
                }
                room.setCurrCount(count);
            }
        }
    }
}
