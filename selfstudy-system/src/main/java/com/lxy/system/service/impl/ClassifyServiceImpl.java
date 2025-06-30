package com.lxy.system.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.alibaba.fastjson2.JSON;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.lxy.common.domain.R;
import com.lxy.common.enums.StudyStatus;
import com.lxy.system.po.Catalog;
import com.lxy.system.po.Classify;
import com.lxy.system.mapper.ClassifyMapper;
import com.lxy.system.po.StudyRecord;
import com.lxy.system.service.CatalogService;
import com.lxy.system.service.ClassifyService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lxy.system.service.RedisService;
import com.lxy.system.service.StudyRecordService;
import com.lxy.common.util.ImgConfigUtil;
import com.lxy.common.constant.RedisKeyConstant;
import com.lxy.system.vo.ClassifyVO;

import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * <p>
 * 图书馆 服务实现类
 * </p>
 *
 * @author jiacheng yang.
 * @since 2022-10-22
 */
@Service
public class ClassifyServiceImpl extends ServiceImpl<ClassifyMapper, Classify> implements ClassifyService {

    @Resource
    private StudyRecordService studyRecordService;
    @Resource
    private ClassifyMapper classifyMapper;
    @Resource
    private RedisService redisService;
    @Resource
    private CatalogService catalogService;

    @Override
    public Classify getClassifyById(Integer id) {
        Classify classify = this.getById(id);
        classify.setIconPath(ImgConfigUtil.joinUploadUrl(classify.getIconPath()));
        classify.setCoverPath(ImgConfigUtil.joinUploadUrl(classify.getCoverPath()));
        return classify;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateClassify(Classify classify) {
        Integer id = classify.getId();
        classify.setUpdateTime(new Date());
        if (id == null){
            classify.setCreateTime(new Date());
        }
        this.saveOrUpdate(classify);
        this.removeClassifyCache();
    }

    @Override
    public List<ClassifyVO> getClassifyList() {
        //查缓存
        List<ClassifyVO> list = this.getClassifyListCache();
        if (list != null){
            return list;
        }
        //自习中的记录
        List<StudyRecord> records = studyRecordService.getRecordByStatus(StudyStatus.LEARNING.type);
        //图书馆
        list = classifyMapper.queryList();
        for (ClassifyVO vo : list) {
            Integer id = vo.getId();
            int studyCount = 0 ;
            for (StudyRecord record : records) {
                Integer classifyId = record.getClassifyId();
                if (id.equals(classifyId)){
                    studyCount ++;
                }
            }
            vo.setStudyCount(studyCount);
            vo.setIconPath(ImgConfigUtil.joinUploadUrl(vo.getIconPath()));
        }

        if (CollUtil.isNotEmpty(list)){
            this.insertClassifyCache(list);
        }

        return list;
    }

    @Override
    public void insertClassifyCache(List<ClassifyVO> list) {
        redisService.setObject(RedisKeyConstant.getClassify(),list,60L * 5L, TimeUnit.SECONDS);
    }

    @Override
    public void removeClassifyCache() {
        redisService.deleteKey(RedisKeyConstant.getClassify());
    }

    @Override
    public List<ClassifyVO> getClassifyListCache(){
        List<ClassifyVO> list = redisService.getObject(RedisKeyConstant.getClassify(), ArrayList.class);
        return list;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void removeClassify(Integer id) {
        this.removeById(id);
        catalogService.remove(new LambdaUpdateWrapper<Catalog>().eq(Catalog::getClassifyId,id));
        this.removeClassifyCache();
    }

}
