package com.lxy.system.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUnit;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.lxy.common.domain.PageResult;
import com.lxy.system.dto.StudyRecordDTO;
import com.lxy.common.enums.StudyStatus;
import com.lxy.system.dto.StudyRecordPageDTO;
import com.lxy.system.po.Catalog;
import com.lxy.system.po.StudyRecord;
import com.lxy.system.mapper.StudyRecordMapper;
import com.lxy.system.service.StudyRecordService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lxy.common.util.ImgConfigUtil;
import com.lxy.system.vo.StudyRecordVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.Date;
import java.util.List;

/**
 * <p>
 * 学习记录 服务实现类
 * </p>
 *
 * @author jiacheng yang.
 * @since 2022-10-22
 */
@Service
public class StudyRecordServiceImpl extends ServiceImpl<StudyRecordMapper, StudyRecord> implements StudyRecordService {

    private final StudyRecordMapper studyRecordMapper;

    @Autowired
    public StudyRecordServiceImpl(StudyRecordMapper studyRecordMapper) {
        this.studyRecordMapper = studyRecordMapper;

    }

    @Override
    public List<StudyRecord> getRecordByStatus(Integer status) {
        LambdaQueryWrapper<StudyRecord> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(StudyRecord::getStatus, status);
        return this.list(wrapper);
    }

    @Override
    public List<StudyRecord> getRecordsByStatusAndClassIfy( Integer classifyId) {
        LambdaQueryWrapper<StudyRecord> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(StudyRecord::getClassifyId,classifyId)
                .ne(StudyRecord::getStatus, StudyStatus.FINISH.type);

        return this.list(wrapper);
    }

    @Override
    public PageInfo<StudyRecord> getStudyNotePageList(Integer userId, Integer page, Integer limit) {
        PageHelper.startPage(page,limit,"start_time desc");
        com.github.pagehelper.Page<StudyRecord> pg = (com.github.pagehelper.Page<StudyRecord>) getStudyNoteList(userId);
        return new PageInfo<>(pg);
    }

    @Override
    public PageInfo<StudyRecordVO> getStudyRecord(Integer userId, Integer classifyId, Integer status,Integer page, Integer limit) {
//        Page<StudyRecord> pg = new Page<>(page,limit);
//        LambdaQueryWrapper<StudyRecord> wrapper = new LambdaQueryWrapper<>();
//        //开始时间倒序
//        wrapper.orderByDesc(StudyRecord::getStartTime);
//        if (userId != null){
//            wrapper.eq(StudyRecord::getUserId,userId);
//        }
//        if (classifyId != null){
//            wrapper.eq(StudyRecord::getClassifyId,classifyId);
//        }
//        if (status != null){
//            wrapper.eq(StudyRecord::getStatus,status);
//        }
//        pg = this.page(pg);
        PageHelper.startPage(page,limit,"start_time desc");
        com.github.pagehelper.Page<StudyRecordVO> pg = (com.github.pagehelper.Page<StudyRecordVO>) getStudyRecordList(userId, classifyId, status);
        return new PageInfo<>(pg);
    }

    @Override
    public boolean saveStudyNote(Integer recordId, String content, String pic) {
        boolean flag = false;
        StudyRecord record = this.getById(recordId);
        if (record != null){
            LambdaUpdateWrapper<StudyRecord> wrapper = new LambdaUpdateWrapper<>();
            wrapper.eq(StudyRecord::getId,recordId)
                    .set(StudyRecord::getNoteStatus,1)
                    .set(StudyRecord::getNoteContent,content);
            if (StrUtil.isNotEmpty(pic)){
                wrapper.set(StudyRecord::getNotePath,pic);
            }else {
                //删除自习笔记图片
                wrapper.set(StudyRecord::getNotePath,"");
            }
            flag = this.update(wrapper);
        }
        return flag;
    }

    @Override
    public boolean removeStudyNote(Integer recordId) {
        boolean flag = false;
        StudyRecord record = this.getById(recordId);
        if (record != null) {
            LambdaUpdateWrapper<StudyRecord> wrapper = new LambdaUpdateWrapper<>();
            wrapper.eq(StudyRecord::getId,recordId)
                    .set(StudyRecord::getNoteStatus,0)
                    .set(StudyRecord::getNoteContent,"")
                    .set(StudyRecord::getNotePath,"");
            flag = this.update(wrapper);
        }
        return flag;
    }

    @Override
    public List<StudyRecordVO> getLearningRecords(Integer catalogId) {
        List<StudyRecordVO> records = studyRecordMapper.getLearningRecords(catalogId);
        if (CollUtil.isNotEmpty(records)){
            //座位号排序
            records.sort(Comparator.comparing(StudyRecordVO::getSeat));
            //设置用户头像
            records.forEach(record -> record.setProfilePath(ImgConfigUtil.joinUploadUrl(record.getProfilePath())));
        }
        return records;
    }

    @Override
    public StudyRecordVO getLearningRecordDetail(Integer recordId) {
        return studyRecordMapper.getLearningRecordDetail(recordId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Integer startStudy(StudyRecordDTO studyRecordDTO, Catalog catalog,Integer userId) {
        //把正在自习的或者离开的记录变为已完成
        this.updateRecordToFinish(userId);
        Integer timingMode = studyRecordDTO.getTimingMode();
        //正计时把设置时长去掉
        if (timingMode == 1){
            studyRecordDTO.setSettingDuration(null);
        }
        StudyRecord studyRecord = new StudyRecord();
        BeanUtil.copyProperties(studyRecordDTO,studyRecord);
        studyRecord.setUserId(userId);
        studyRecord.setClassifyId(catalog.getClassifyId());
        studyRecord.setStatus(1);
        studyRecord.setNoteStatus(0);
        studyRecord.setStartTime(new Date());
        this.save(studyRecord);
        return studyRecord.getId();
    }

    @Override
    public StudyRecord stopStudy(Integer recordId, Integer userId) {
        StudyRecord record = this.getById(recordId);
        if (!userId.equals(record.getUserId())){
            return null;
        }
        this.makeRecordToFinish(record);
        this.updateById(record);

        return record;
    }

    /**
     * 把正在自习的或者离开的记录变为已完成
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateRecordToFinish(Integer userId){
        LambdaQueryWrapper<StudyRecord> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(StudyRecord::getUserId,userId).ne(StudyRecord::getStatus,StudyStatus.FINISH.type);
        List<StudyRecord> records = this.list(wrapper);
        if (CollUtil.isNotEmpty(records)){
            for (StudyRecord record : records) {
                this.makeRecordToFinish(record);
            }
            this.updateBatchById(records);
        }
    }

    @Override
    public PageResult<StudyRecordVO> getStudyRecordByAdmin(StudyRecordPageDTO dto) {
        PageHelper.startPage(dto.getPage(),dto.getLimit(),"start_time desc");
        com.github.pagehelper.Page<StudyRecordVO> pg = (com.github.pagehelper.Page<StudyRecordVO>)
                studyRecordMapper.getStudyRecordByAdmin(dto.getPhone(), dto.getClassifyId(), dto.getStatus());
        return  PageResult.convert(new PageInfo<>(pg));
    }

    /**
     * 设置自习记录为已完成
     */
    private void makeRecordToFinish(StudyRecord record){
        Date now = new Date();
        //设置为已完成
        record.setStatus(StudyStatus.FINISH.type);
        record.setUpdateTime(now);
        Integer timingMode = record.getTimingMode();
        //根据计时方式设置实际学习时长
        int actualDuration = 0;
//        if (timingMode == 2){
//            //倒计时
//            actualDuration = record.getSettingDuration();
//        }else {
//            //正计时，当前时间 - 开始时间就是实际时长
//            actualDuration = (int) DateUtil.between(record.getStartTime(), now, DateUnit.MINUTE);
//        }
        actualDuration = (int) DateUtil.between(record.getStartTime(), now, DateUnit.MINUTE);
        actualDuration = actualDuration == 0 ? 1 : actualDuration;
        record.setActualDuration(actualDuration);
    }

    /**
     * 获取自习笔记
     */
    private List<StudyRecord> getStudyNoteList(Integer userId){
        List<StudyRecord> records = studyRecordMapper.getStudyNoteList(userId);
        records.forEach(record -> record.setNotePath(ImgConfigUtil.joinUploadUrl(record.getNotePath())));
        return records;
    }

    /**
     * 获取自习记录
     */
    private List<StudyRecordVO> getStudyRecordList(Integer userId,Integer classifyId,Integer status){
        return studyRecordMapper.getStudyRecordList(userId, classifyId, status);
    }

}
