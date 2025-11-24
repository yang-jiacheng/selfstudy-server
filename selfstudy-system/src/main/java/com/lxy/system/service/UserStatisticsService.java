package com.lxy.system.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.lxy.system.po.StudyRecord;
import com.lxy.system.po.StudyStatistics;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * TODO
 *
 * @author jiacheng yang.
 * @version 1.0
 * @since 2022/12/20 10:49
 */

@Service
public class UserStatisticsService {

    @Resource
    private UserService userService;
    @Resource
    private StudyStatisticsService statisticsService;
    @Resource
    private StudyRecordService studyRecordService;

    @Transactional(rollbackFor = Exception.class)
    public void removeUserByIds(List<Long> ids) {
        userService.removeByIds(ids);
        userService.removeUserInfoCacheByIds(ids);
        // 删用户其他关联数据...
        statisticsService.remove(new LambdaQueryWrapper<StudyStatistics>().in(StudyStatistics::getUserId, ids));
        studyRecordService.remove(new LambdaQueryWrapper<StudyRecord>().in(StudyRecord::getUserId, ids));
    }

}
