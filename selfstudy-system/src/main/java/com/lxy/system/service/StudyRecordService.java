package com.lxy.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.github.pagehelper.PageInfo;
import com.lxy.common.model.PageResult;
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
    List<StudyRecord> getRecordsByStatusAndClassIfy(Long classifyId);

    /**
     * 获取自习笔记
     */
    PageInfo<StudyRecord> getStudyNotePageList(Long userId, Integer page, Integer limit);

    /**
     * 获取自习记录
     */
    PageInfo<StudyRecordVO> getStudyRecord(Long userId, Long classifyId, Integer status, Integer page, Integer limit);

    /**
     * 编辑自习笔记
     */
    boolean saveStudyNote(Long recordId, String content, String pic);

    /**
     * 删除自习笔记
     */
    boolean removeStudyNote(Long recordId);

    /**
     * 根据自习室id获取自习中的记录，连表查用户信息
     */
    List<StudyRecordVO> getLearningRecords(Long catalogId);

    /**
     * 获取记录根据id
     */
    StudyRecordVO getLearningRecordDetail(Long recordId);

    /**
     * 开始自习
     */
    Long startStudy(StudyRecordDTO studyRecordDTO, Catalog catalog, Long userId);

    /**
     * 结束自习
     */
    StudyRecord stopStudy(Long recordId, Long userId);

    /**
     * 把正在自习的或者离开的记录变为已完成
     */
    void updateRecordToFinish(Long userId);

    /**
     * 获取自习记录,后台查询用
     */
    PageResult<StudyRecordVO> getStudyRecordByAdmin(StudyRecordPageDTO dto);

}
