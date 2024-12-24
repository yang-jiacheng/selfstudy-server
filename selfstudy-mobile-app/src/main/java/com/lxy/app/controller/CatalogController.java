package com.lxy.app.controller;

import com.lxy.common.domain.R;
import com.lxy.common.dto.StudyRecordDTO;
import com.lxy.common.po.Catalog;
import com.lxy.common.po.StudyRecord;
import com.lxy.common.service.CatalogService;
import com.lxy.common.service.StudyRecordService;
import com.lxy.app.security.util.UserIdUtil;
import com.lxy.common.util.JsonUtil;
import com.lxy.common.vo.ClassifyDetailVO;
import com.lxy.common.vo.ResultVO;
import com.lxy.common.vo.RoomVO;
import com.lxy.common.vo.StudyRecordVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Description: TODO
 * @author: jiacheng yang.
 * @Date: 2022/12/24 11:06
 * @Version: 1.0
 */

@RequestMapping("/catalog")
@RestController
@Api(tags = "自习室")
public class CatalogController {

    private final CatalogService catalogService;

    private final StudyRecordService studyRecordService;

    @Autowired
    public CatalogController(CatalogService catalogService,StudyRecordService studyRecordService) {
        this.catalogService = catalogService;
        this.studyRecordService = studyRecordService;
    }

    @ApiOperation(value = "获取图书馆详情及自习室",  notes = "jiacheng yang.")
    @PostMapping(value = "/getClassifyDetail" , produces = "application/json")
    public R<Object> getClassifyDetail(@ApiParam(value = "图书馆id")@RequestParam(value = "classifyId") Integer classifyId){
        ClassifyDetailVO detail = catalogService.getCatalogByClassify(classifyId);
        return R.ok(detail);
    }

    @ApiOperation(value = "获取自习室详情",  notes = "jiacheng yang.")
    @PostMapping(value = "/getRoomDetail" , produces = "application/json")
    public R<Object> getRoomDetail(@ApiParam(value = "自习室id")@RequestParam(value = "roomId") Integer roomId){
        RoomVO detail = catalogService.getRoomDetail(roomId);
        return R.ok(detail);
    }

    @ApiOperation(value = "获取自习中的用户记录",  notes = "jiacheng yang.")
    @PostMapping(value = "/getLearningRecords" , produces = "application/json")
    public R<Object> getLearningRecords(@ApiParam(value = "自习室id")@RequestParam(value = "catalogId") Integer catalogId){
        List<StudyRecordVO> records = studyRecordService.getLearningRecords(catalogId);
        return R.ok(records);
    }

    @ApiOperation(value = "获取自习中记录详情",  notes = "jiacheng yang.")
    @PostMapping(value = "/getLearningRecordDetail" , produces = "application/json")
    public R<Object> getLearningRecordDetail(@ApiParam(value = "记录id")@RequestParam(value = "recordId") Integer recordId){
        StudyRecordVO detail = studyRecordService.getLearningRecordDetail(recordId);
        return R.ok(detail);
    }

    @ApiOperation(value = "开始自习",  notes = "jiacheng yang.")
    @PostMapping(value = "/startStudy" , produces = "application/json")
    public R<Object> startStudy(@RequestBody StudyRecordDTO studyRecordDTO){
        int userId = UserIdUtil.getUserId();
        Catalog catalog = catalogService.getById(studyRecordDTO.getCatalogId());
        Integer recordId = null;
        if (catalog != null){
            recordId = studyRecordService.startStudy(studyRecordDTO, catalog, userId);
        }
        return R.ok(recordId);
    }

    @ApiOperation(value = "结束自习",  notes = "jiacheng yang.")
    @PostMapping(value = "/stopStudy" , produces = "application/json")
    public R<Object> stopStudy(@ApiParam(value = "记录id")@RequestParam(value = "recordId") Integer recordId){
        int userId = UserIdUtil.getUserId();
        StudyRecord studyRecord = studyRecordService.stopStudy(recordId, userId);
        return R.ok(studyRecord.getActualDuration());
    }

    @ApiOperation(value = "把正在自习的或者离开的记录变为已完成",  notes = "jiacheng yang.")
    @PostMapping(value = "/updateRecordToFinish" , produces = "application/json")
    public R<Object> updateRecordToFinish(){
        int userId = UserIdUtil.getUserId();
        studyRecordService.updateRecordToFinish(userId);
        return R.ok();
    }


}
