package com.lxy.admin.controller;

import com.github.pagehelper.PageInfo;
import com.lxy.common.po.Classify;
import com.lxy.common.service.ClassifyService;
import com.lxy.common.service.StudyRecordService;
import com.lxy.common.util.JsonUtil;
import com.lxy.common.vo.LayUiResultVO;
import com.lxy.common.vo.ResultVO;
import com.lxy.common.vo.StudyRecordVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Description: TODO
 * @author: jiacheng yang.
 * @Date: 2022/12/19 10:17
 * @Version: 1.0
 */

@RequestMapping("/studyRecord")
@Controller
@Api(tags = "自习记录")
@PreAuthorize("hasAuthority('/studyRecord/toStudyRecord')")
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

    @ApiOperation(value = "获取图书馆",  notes = "jiacheng yang.")
    @PostMapping(value = "/getAllLibrary" , produces = "application/json")
    @ResponseBody
    public String getAllLibrary(){
        List<Classify> list = classifyService.list();
        return JsonUtil.toJson(new ResultVO(list));
    }

    @ApiOperation(value = "获取自习记录",  notes = "jiacheng yang.")
    @PostMapping(value = "/getStudyRecord" , produces = "application/json")
    @ResponseBody
    public String getStudyRecord(@ApiParam(value = "当前页")@RequestParam(value = "page",required = false,defaultValue = "1") Integer page,
                                  @ApiParam(value = "每页数量")@RequestParam(value = "limit",required = false,defaultValue = "10") Integer limit,
                                  @ApiParam(value = "图书馆id")@RequestParam(value = "classifyId",required = false) Integer classifyId,
                                  @ApiParam(value = "自习状态")@RequestParam(value = "status",required = false) Integer status,
                                  @ApiParam(value = "手机号")@RequestParam(value = "phone",required = false) String phone){
        PageInfo<StudyRecordVO> pg = studyRecordService.getStudyRecordByAdmin(phone, classifyId, status, page, limit);
        return JsonUtil.toJson(new LayUiResultVO((int) pg.getTotal(),pg.getList()));
    }

}
