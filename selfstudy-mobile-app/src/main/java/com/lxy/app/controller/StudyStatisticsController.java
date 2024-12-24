package com.lxy.app.controller;

import com.lxy.common.constant.ConfigConstants;
import com.lxy.common.domain.R;
import com.lxy.common.po.StudyRecord;
import com.lxy.common.service.BusinessConfigService;
import com.lxy.common.service.StudyRecordService;
import com.lxy.common.service.StudyStatisticsService;
import com.lxy.common.service.UserService;
import com.lxy.app.security.util.UserIdUtil;
import com.lxy.common.util.JsonUtil;
import com.lxy.common.vo.ResultVO;
import com.lxy.common.vo.UserRankVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

/**
 * @Description: TODO
 * @author: jiacheng yang.
 * @Date: 2022/12/23 11:37
 * @Version: 1.0
 */

@RequestMapping("/studyStatistics")
@RestController
@Api(tags = "学情统计")
public class StudyStatisticsController {

    private final UserService userService;

    private final StudyStatisticsService statisticsService;

    private final StudyRecordService studyRecordService;

    private final BusinessConfigService businessConfigService;

    @Autowired
    public StudyStatisticsController(UserService userService, StudyStatisticsService statisticsService
            ,StudyRecordService studyRecordService,BusinessConfigService businessConfigService) {
        this.userService = userService;
        this.statisticsService = statisticsService;
        this.studyRecordService = studyRecordService;
        this.businessConfigService = businessConfigService;
    }

    @ApiOperation(value = "提交学习时长",  notes = "jiacheng yang.")
    @PostMapping(value = "/submitStudyDuration" , produces = "application/json")
    public R<Object> submitStudyDuration(@ApiParam(value = "学习时长")@RequestParam(value = "duration") Integer duration,
                                         @ApiParam(value = "学习记录id")@RequestParam(value = "recordId") Integer recordId){
        int userId = UserIdUtil.getUserId();
        StudyRecord record = studyRecordService.getById(recordId);
        if (record != null){
            Date startTime = record.getStartTime();
            statisticsService.saveStatistics(userId,startTime,duration);
            userService.submitStudyDuration(userId,duration);
        }
        return R.ok();
    }

    @ApiOperation(value = "获取总排行榜",  notes = "jiacheng yang.")
    @PostMapping(value = "/getRankings" , produces = "application/json")
    public R<Object> getRankings(){
        List<UserRankVO> records = userService.getRankingsTotalDuration();
        return R.ok(records);
    }

    @ApiOperation(value = "获取总排行榜更新规则",  notes = "jiacheng yang.")
    @PostMapping(value = "/getRankingsRules" , produces = "application/json")
    public R<Object> getRankingsRules(){
        String value = businessConfigService.getBusinessConfigValue(ConfigConstants.GENERAL_RULES);
        return R.ok(value);
    }

    @ApiOperation(value = "获取自己的排名和信息",  notes = "jiacheng yang.")
    @GetMapping(value = "/getSelfRankings" , produces = "application/json")
    public R<Object> getSelfRankings(){
        int userId = UserIdUtil.getUserId();
        UserRankVO user = userService.getUserRankingById(userId);
        return R.ok(user);
    }

}
