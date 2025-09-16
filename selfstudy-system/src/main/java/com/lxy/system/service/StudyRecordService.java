package com.lxy.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.github.pagehelper.PageInfo;
import com.lxy.common.domain.PageResult;
import com.lxy.system.dto.StudyRecordDTO;
import com.lxy.system.dto.StudyRecordPageDTO;
import com.lxy.system.po.Catalog;
import com.lxy.system.po.StudyRecord;
import com.lxy.system.vo.StudyRecordVO;

import java.util.List;

/**
 * <p>
 * 学习记录 服务类
 * </p>
 *
 * @author jiacheng yang.
 * @since 2022-10-22
 */
public interface StudyRecordService extends IService<StudyRecord> {

    /**
     * 根据学习状态获取记录
     */
    List<StudyRecord> getRecordByStatus(Integer status);

    /**
     * 获取某图书馆正在自习的记录
     */
    List<StudyRecord> getRecordsByStatusAndClassIfy(Integer classifyId);

    /**
     * 获取自习笔记
     */
    PageInfo<StudyRecord> getStudyNotePageList(Integer userId, Integer page, Integer limit);

    /**
     * 获取自习记录
     */
    PageInfo<StudyRecordVO> getStudyRecord(Integer userId, Integer classifyId, Integer status, Integer page, Integer limit);

    /**
     * 编辑自习笔记
     */
    boolean saveStudyNote(Integer recordId, String content, String pic);

    /**
     * 删除自习笔记
     */
    boolean removeStudyNote(Integer recordId);

    /**
     * 根据自习室id获取自习中的记录，连表查用户信息
     */
    List<StudyRecordVO> getLearningRecords(Integer catalogId);

    /**
     * 获取记录根据id
     */
    StudyRecordVO getLearningRecordDetail(Integer recordId);

    /**
     * 开始自习
     */
    Integer startStudy(StudyRecordDTO studyRecordDTO, Catalog catalog, Integer userId);

    /**
     * 结束自习
     */
    StudyRecord stopStudy(Integer recordId, Integer userId);

    /**
     * 把正在自习的或者离开的记录变为已完成
     */
    void updateRecordToFinish(Integer userId);

    /**
     * 获取自习记录,后台查询用
     */
    PageResult<StudyRecordVO> getStudyRecordByAdmin(StudyRecordPageDTO dto);

}
