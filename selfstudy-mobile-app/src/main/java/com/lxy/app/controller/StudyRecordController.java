package com.lxy.app.controller;

import com.github.pagehelper.PageInfo;
import com.lxy.common.domain.R;
import com.lxy.common.enums.StudyStatus;
import com.lxy.common.po.StudyRecord;
import com.lxy.common.service.StudyRecordService;
import com.lxy.app.security.util.UserIdUtil;
import com.lxy.common.util.ImgConfigUtil;
import com.lxy.common.util.JsonUtil;
import com.lxy.common.vo.ResultVO;
import com.lxy.common.vo.StudyRecordVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Description: TODO
 * @author: jiacheng yang.
 * @Date: 2022/12/23 11:19
 * @Version: 1.0
 */

@RequestMapping("/studyRecord")
@RestController
@Api(tags = "自习记录")
public class StudyRecordController {

    private final StudyRecordService studyRecordService;

    @Autowired
    public StudyRecordController(StudyRecordService studyRecordService) {
        this.studyRecordService = studyRecordService;
    }

    @ApiOperation(value = "获取自习笔记列表",  notes = "jiacheng yang.")
    @PostMapping(value = "/getStudyNotes" , produces = "application/json")
    public R<Object> getStudyNotes(@ApiParam(value = "当前页")@RequestParam(value = "page",required = false,defaultValue = "1") Integer page,
                                   @ApiParam(value = "每页数量")@RequestParam(value = "limit",required = false,defaultValue = "10") Integer limit){
        int userId = UserIdUtil.getUserId();
        PageInfo<StudyRecord> pg = studyRecordService.getStudyNotePageList(userId, page, limit);
        return R.ok(pg.getList());
    }

    @ApiOperation(value = "获取自习笔记详情",  notes = "jiacheng yang.")
    @PostMapping(value = "/getStudyNoteDetail" , produces = "application/json")
    public R<Object> getStudyNoteDetail(@ApiParam(value = "记录id")@RequestParam(value = "recordId") Integer recordId){
        StudyRecord record = studyRecordService.getById(recordId);
        record.setNotePath(ImgConfigUtil.joinUploadUrl(record.getNotePath()));
        return R.ok(record);
    }

    @ApiOperation(value = "编辑自习笔记",  notes = "jiacheng yang.")
    @PostMapping(value = "/saveStudyNote" , produces = "application/json")
    public R<Object> saveStudyNote(@ApiParam(value = "记录id")@RequestParam(value = "recordId") Integer recordId,
                                @ApiParam(value = "笔记内容")@RequestParam(value = "content") String content,
                                @ApiParam(value = "笔记图片")@RequestParam(value = "pic",required = false) String pic){
        studyRecordService.saveStudyNote(recordId,content,pic);
        return R.ok();
    }

    @ApiOperation(value = "删除自习笔记",  notes = "jiacheng yang.")
    @PostMapping(value = "/removeStudyNote" , produces = "application/json")
    public R<Object> removeStudyNote(@ApiParam(value = "记录id")@RequestParam(value = "recordId") Integer recordId){
        studyRecordService.removeStudyNote(recordId);
        return R.ok();
    }

    @ApiOperation(value = "获取自习记录",  notes = "jiacheng yang.")
    @PostMapping(value = "/getStudyRecord" , produces = "application/json")
    public R<Object> getStudyRecord(@ApiParam(value = "当前页")@RequestParam(value = "page",required = false,defaultValue = "1") Integer page,
                                 @ApiParam(value = "每页数量")@RequestParam(value = "limit",required = false,defaultValue = "10") Integer limit){
        int userId = UserIdUtil.getUserId();
        //已完成的记录
        PageInfo<StudyRecordVO> pg = studyRecordService.getStudyRecord(userId, null, StudyStatus.FINISH.type, page, limit);
        return R.ok(pg.getList());
    }

}
