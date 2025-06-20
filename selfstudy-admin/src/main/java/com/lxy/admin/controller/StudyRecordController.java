package com.lxy.admin.controller;

import com.github.pagehelper.PageInfo;
import com.lxy.common.domain.R;
import com.lxy.system.po.Classify;
import com.lxy.system.service.ClassifyService;
import com.lxy.system.service.StudyRecordService;
import com.lxy.system.vo.LayUiResultVO;

import com.lxy.system.vo.StudyRecordVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * TODO
 * @author jiacheng yang.
 * @since 2022/12/19 10:17
 * @version 1.0
 */

@RequestMapping("/studyRecord")
@Controller
@PreAuthorize("hasAuthority('studyRecord')")
public class StudyRecordController {

    private final ClassifyService classifyService;

    private final StudyRecordService studyRecordService;

    @Autowired
    public StudyRecordController(ClassifyService classifyService, StudyRecordService studyRecordService) {
        this.classifyService = classifyService;
        this.studyRecordService = studyRecordService;
    }

    @GetMapping("/toStudyRecord")
    public String toStudyRecord(){
        return "studyRecord/studyRecordList";
    }

    @PostMapping(value = "/getAllLibrary" , produces = "application/json")
    @ResponseBody
    public R<List<Classify>> getAllLibrary(){
        List<Classify> list = classifyService.list();
        return R.ok(list);
    }

    @PostMapping(value = "/getStudyRecord" , produces = "application/json")
    @ResponseBody
    public LayUiResultVO getStudyRecord(@RequestParam(value = "page",required = false,defaultValue = "1") Integer page,
                                  @RequestParam(value = "limit",required = false,defaultValue = "10") Integer limit,
                                 @RequestParam(value = "classifyId",required = false) Integer classifyId,
                                  @RequestParam(value = "status",required = false) Integer status,
                                  @RequestParam(value = "phone",required = false) String phone){
        PageInfo<StudyRecordVO> pg = studyRecordService.getStudyRecordByAdmin(phone, classifyId, status, page, limit);
        return new LayUiResultVO((int) pg.getTotal(),pg.getList());
    }

}
