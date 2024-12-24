package com.lxy.common.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.alibaba.fastjson.JSON;
import com.lxy.common.enums.StudyStatus;
import com.lxy.common.po.Classify;
import com.lxy.common.mapper.ClassifyMapper;
import com.lxy.common.po.StudyRecord;
import com.lxy.common.redis.service.CommonRedisService;
import com.lxy.common.service.ClassifyService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lxy.common.service.StudyRecordService;
import com.lxy.common.util.ImgConfigUtil;
import com.lxy.common.util.JsonUtil;
import com.lxy.common.redis.util.RedisKeyUtil;
import com.lxy.common.vo.ClassifyVO;
import com.lxy.common.vo.ResultVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

    private final StudyRecordService studyRecordService;

    private final ClassifyMapper classifyMapper;

    private final CommonRedisService commonRedisService;

    @Autowired
    public ClassifyServiceImpl(StudyRecordService studyRecordService,ClassifyMapper classifyMapper,CommonRedisService commonRedisService) {
        this.studyRecordService = studyRecordService;
        this.classifyMapper = classifyMapper;
        this.commonRedisService = commonRedisService;
    }

    @Override
    public Classify getClassifyById(Integer id) {
        Classify classify = this.getById(id);
        classify.setIconPath(ImgConfigUtil.joinUploadUrl(classify.getIconPath()));
        classify.setCoverPath(ImgConfigUtil.joinUploadUrl(classify.getCoverPath()));
        return classify;
    }

    @Override
    public ResultVO updateClassify(String mainJson) {
        Classify classify = JSON.parseObject(mainJson, Classify.class);
        if (classify == null){
            return new ResultVO(-1,"数据有误！");
        }
        Integer id = classify.getId();
        classify.setUpdateTime(new Date());
        if (id == null){
            classify.setCreateTime(new Date());
        }
        this.saveOrUpdate(classify);
        return new ResultVO();
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
        commonRedisService.insertString(RedisKeyUtil.getClassify(),JsonUtil.toJson(list),60 * 5, TimeUnit.SECONDS);
    }

    @Override
    public void removeClassifyCache() {
        commonRedisService.deleteKey(RedisKeyUtil.getClassify());
    }

    @Override
    public List<ClassifyVO> getClassifyListCache(){
        List<ClassifyVO> list = null;
        String value = commonRedisService.getString(RedisKeyUtil.getClassify());
        if (value != null){
            list = JsonUtil.getListType(value,ClassifyVO.class);
        }
        return list;
    }

}
