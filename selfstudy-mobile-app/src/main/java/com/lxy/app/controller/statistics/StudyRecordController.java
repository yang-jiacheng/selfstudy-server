package com.lxy.app.controller.statistics;

import com.github.pagehelper.PageInfo;
import com.lxy.common.domain.R;
import com.lxy.common.enums.StudyStatus;
import com.lxy.system.po.StudyRecord;
import com.lxy.system.service.StudyRecordService;
import com.lxy.framework.security.util.UserIdUtil;
import com.lxy.common.util.ImgConfigUtil;
import com.lxy.system.vo.StudyRecordVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Description: 自习记录
 * author: jiacheng yang.
 * Date: 2022/12/23 11:19
 * Version: 1.0
 */

@RequestMapping("/studyRecord")
@RestController
public class StudyRecordController {

    private final StudyRecordService studyRecordService;

    @Autowired
    public StudyRecordController(StudyRecordService studyRecordService) {
        this.studyRecordService = studyRecordService;
    }

    /**
     * Description: 获取自习笔记列表
     * Author: jiacheng yang.
     * Date: 2025/02/20 10:25
     * Param: [page, limit]
     */
    @PostMapping(value = "/getStudyNotes" , produces = "application/json")
    public R<List<StudyRecord>> getStudyNotes(@RequestParam(value = "page",required = false,defaultValue = "1") Integer page,
                                 @RequestParam(value = "limit",required = false,defaultValue = "10") Integer limit){
        int userId = UserIdUtil.getUserId();
        PageInfo<StudyRecord> pg = studyRecordService.getStudyNotePageList(userId, page, limit);
        return R.ok(pg.getList());
    }

    /**
     * Description: 获取自习笔记详情
     * Author: jiacheng yang.
     * Date: 2025/02/20 10:26
     * Param: [recordId]
     */
    @PostMapping(value = "/getStudyNoteDetail" , produces = "application/json")
    public R<StudyRecord> getStudyNoteDetail(@RequestParam(value = "recordId") Integer recordId){
        StudyRecord record = studyRecordService.getById(recordId);
        record.setNotePath(ImgConfigUtil.joinUploadUrl(record.getNotePath()));
        return R.ok(record);
    }

    /**
     * Description: 编辑自习笔记
     * Author: jiacheng yang.
     * Date: 2025/02/20 10:26
     * Param: [recordId, content, pic]
     */
    @PostMapping(value = "/saveStudyNote" , produces = "application/json")
    public R<Object> saveStudyNote(@RequestParam(value = "recordId") Integer recordId,
                                @RequestParam(value = "content") String content,
                                @RequestParam(value = "pic",required = false) String pic){
        studyRecordService.saveStudyNote(recordId,content,pic);
        return R.ok();
    }

    /**
     * Description: 删除自习笔记
     * Author: jiacheng yang.
     * Date: 2025/02/20 10:26
     * Param: [recordId]
     */
    @PostMapping(value = "/removeStudyNote" , produces = "application/json")
    public R<Object> removeStudyNote(@RequestParam(value = "recordId") Integer recordId){
        studyRecordService.removeStudyNote(recordId);
        return R.ok();
    }

    /**
     * Description: 获取自习记录
     * Author: jiacheng yang.
     * Date: 2025/02/20 10:26
     * Param: [page, limit]
     */
    @PostMapping(value = "/getStudyRecord" , produces = "application/json")
    public R<List<StudyRecordVO>> getStudyRecord(@RequestParam(value = "page",required = false,defaultValue = "1") Integer page,
                                 @RequestParam(value = "limit",required = false,defaultValue = "10") Integer limit){
        int userId = UserIdUtil.getUserId();
        //已完成的记录
        PageInfo<StudyRecordVO> pg = studyRecordService.getStudyRecord(userId, null, StudyStatus.FINISH.type, page, limit);
        return R.ok(pg.getList());
    }

}
