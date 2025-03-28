package com.lxy.system.mapper;

import com.lxy.system.po.StudyRecord;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lxy.common.vo.StudyRecordVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 学习记录 Mapper 接口
 * </p>
 *
 * @author jiacheng yang.
 * @since 2022-12-23
 */
@Mapper
public interface StudyRecordMapper extends BaseMapper<StudyRecord> {

    List<StudyRecord> getStudyNoteList(@Param("userId") Integer userId);

    Integer getStudyNoteList_COUNT(@Param("userId") Integer userId);

    List<StudyRecordVO> getLearningRecords(@Param("catalogId") Integer catalogId);

    StudyRecordVO getLearningRecordDetail(@Param("recordId") Integer recordId);

    List<StudyRecordVO> getStudyRecordList(@Param("userId") Integer userId,@Param("classifyId") Integer classifyId,@Param("status") Integer status);

    Integer getStudyRecordList_COUNT(@Param("userId") Integer userId,@Param("classifyId") Integer classifyId,@Param("status") Integer status);

    List<StudyRecordVO> getStudyRecordByAdmin(@Param("phone") String phone,@Param("classifyId") Integer classifyId,@Param("status") Integer status);

    Integer getStudyRecordByAdmin_COUNT(@Param("phone") String phone,@Param("classifyId") Integer classifyId,@Param("status") Integer status);
}
