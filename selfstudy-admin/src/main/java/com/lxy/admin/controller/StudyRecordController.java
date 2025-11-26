package com.lxy.admin.controller;

import com.lxy.common.domain.CollResult;
import com.lxy.common.domain.PageResult;
import com.lxy.common.domain.R;
import com.lxy.common.vo.LabelValueVO;
import com.lxy.common.dto.StudyRecordPageDTO;
import com.lxy.system.po.Classify;
import com.lxy.system.service.ClassifyService;
import com.lxy.system.service.StudyRecordService;
import com.lxy.common.vo.StudyRecordVO;
import jakarta.annotation.Resource;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * 学习记录
 *
 * @author jiacheng yang.
 * @version 1.0
 * @since 2022/12/19 10:17
 */

@RequestMapping("/studyRecord")
@RestController
@PreAuthorize("hasAuthority('studyRecord')")
public class StudyRecordController {

    @Resource
    private ClassifyService classifyService;
    @Resource
    private StudyRecordService studyRecordService;

    @PostMapping(value = "/getAllLibrary", produces = "application/json")

    public R<CollResult<LabelValueVO>> getAllLibrary() {
        List<Classify> list = classifyService.list();
        List<LabelValueVO> vos = new ArrayList<>();
        for (Classify classify : list) {
            vos.add(new LabelValueVO(classify.getId(), classify.getName()));
        }
        return R.ok(new CollResult<>(vos));
    }

    @PostMapping(value = "/getStudyRecord", produces = "application/json")
    public R<PageResult<StudyRecordVO>> getStudyRecord(@RequestBody StudyRecordPageDTO dto) {
        PageResult<StudyRecordVO> pg = studyRecordService.getStudyRecordByAdmin(dto);
        return R.ok(pg);
    }

}
