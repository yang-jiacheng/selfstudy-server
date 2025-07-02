package com.lxy.admin.controller;

import com.github.pagehelper.PageInfo;
import com.lxy.common.domain.CollResult;
import com.lxy.common.domain.PageResult;
import com.lxy.common.domain.R;
import com.lxy.common.vo.EnumVO;
import com.lxy.system.dto.StudyRecordPageDTO;
import com.lxy.system.po.Classify;
import com.lxy.system.service.ClassifyService;
import com.lxy.system.service.StudyRecordService;
import com.lxy.system.vo.LayUiResultVO;

import com.lxy.system.vo.StudyRecordVO;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * TODO
 * @author jiacheng yang.
 * @since 2022/12/19 10:17
 * @version 1.0
 */

@RequestMapping("/studyRecord")
@RestController
@PreAuthorize("hasAuthority('studyRecord')")
public class StudyRecordController {

    @Resource
    private ClassifyService classifyService;
    @Resource
    private StudyRecordService studyRecordService;

    @PostMapping(value = "/getAllLibrary" , produces = "application/json")

    public R<CollResult<EnumVO>> getAllLibrary(){
        List<Classify> list = classifyService.list();
        List<EnumVO> vos = new ArrayList<>();
        for (Classify classify : list) {
            vos.add(new EnumVO(classify.getId(),classify.getName()));
        }
        return R.ok(new CollResult<>(vos));
    }

    @PostMapping(value = "/getStudyRecord" , produces = "application/json")
    public R<PageResult<StudyRecordVO>> getStudyRecord(@RequestBody StudyRecordPageDTO dto){
        PageResult<StudyRecordVO> pg = studyRecordService.getStudyRecordByAdmin(dto);
        return R.ok(pg);
    }

}
