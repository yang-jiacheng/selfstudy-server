package com.lxy.app.controller;

import com.lxy.common.constant.ConfigConstant;
import com.lxy.common.model.R;
import com.lxy.framework.security.util.UserIdUtil;
import com.lxy.system.po.StudyRecord;
import com.lxy.system.service.BusinessConfigService;
import com.lxy.system.service.StudyRecordService;
import com.lxy.system.service.StudyStatisticsService;
import com.lxy.system.service.UserService;
import com.lxy.common.vo.user.UserRankVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.List;

/**
 * 学情统计
 *
 * @author jiacheng yang.
 * @since 2022/12/23 11:37
 */

@RequestMapping("/studyStatistics")
@RestController
public class StudyStatisticsController {

    private final UserService userService;

    private final StudyStatisticsService statisticsService;

    private final StudyRecordService studyRecordService;

    private final BusinessConfigService businessConfigService;

    @Autowired
    public StudyStatisticsController(UserService userService, StudyStatisticsService statisticsService,
        StudyRecordService studyRecordService, BusinessConfigService businessConfigService) {
        this.userService = userService;
        this.statisticsService = statisticsService;
        this.studyRecordService = studyRecordService;
        this.businessConfigService = businessConfigService;
    }

    /**
     * 提交学习时长
     *
     * @author jiacheng yang.
     * @since 2025/02/20 10:21
     * @param duration 学习时长 分钟
     * @param recordId 学习记录id
     */
    @PostMapping(value = "/submitStudyDuration", produces = "application/json")
    public R<Object> submitStudyDuration(@RequestParam(value = "duration") Integer duration,
        @RequestParam(value = "recordId") Integer recordId) {
        long userId = UserIdUtil.getUserId();
        StudyRecord record = studyRecordService.getById(recordId);
        if (record != null) {
            Date startTime = record.getStartTime();
            statisticsService.saveStatistics(userId, startTime, duration);
            userService.submitStudyDuration(userId, duration);
        }
        return R.ok();
    }

    /**
     * 获取总排行榜
     *
     * @author jiacheng yang.
     * @since 2025/02/20 10:23
     */
    @PostMapping(value = "/getRankings", produces = "application/json")
    public R<List<UserRankVO>> getRankings() {
        List<UserRankVO> records = userService.getRankingsTotalDuration();
        return R.ok(records);
    }

    /**
     * 获取总排行榜更新规则
     *
     * @author jiacheng yang.
     * @since 2025/02/20 10:24
     */
    @PostMapping(value = "/getRankingsRules", produces = "application/json")
    public R<String> getRankingsRules() {
        String value = businessConfigService.getBusinessConfigValue(ConfigConstant.GENERAL_RULES);
        return R.ok(value);
    }

    /**
     * 获取自己的排名和信息
     *
     * @author jiacheng yang.
     * @since 2025/02/20 10:24
     */
    @GetMapping(value = "/getSelfRankings", produces = "application/json")
    public R<UserRankVO> getSelfRankings() {
        long userId = UserIdUtil.getUserId();
        UserRankVO user = userService.getUserRankingById(userId);
        return R.ok(user);
    }

}
