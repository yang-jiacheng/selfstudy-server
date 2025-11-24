package com.lxy.system.service.impl;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lxy.system.mapper.StudyStatisticsMapper;
import com.lxy.system.po.StudyStatistics;
import com.lxy.system.service.StudyStatisticsService;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * <p>
 * 学习统计 服务实现类
 * </p>
 *
 * @author jiacheng yang.
 * @since 2022-12-23
 */
@Service
public class StudyStatisticsServiceImpl extends ServiceImpl<StudyStatisticsMapper, StudyStatistics>
    implements StudyStatisticsService {

    @Override
    public boolean saveStatistics(Long userId, Date day, Integer duration) {
        StudyStatistics statistics = this.getStatisticsByDay(userId, day);
        if (statistics == null) {
            statistics = new StudyStatistics(userId, day, duration);
            return this.save(statistics);
        }
        Integer durationActual = statistics.getDuration() == null ? 0 : statistics.getDuration();
        durationActual += duration;
        LambdaUpdateWrapper<StudyStatistics> wrapper = new LambdaUpdateWrapper<>();
        wrapper.eq(StudyStatistics::getId, statistics.getId()).set(StudyStatistics::getDuration, durationActual);
        return this.update(wrapper);
    }

    @Override
    public StudyStatistics getStatisticsByDay(Long userId, Date day) {
        LambdaQueryWrapper<StudyStatistics> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(StudyStatistics::getUserId, userId).eq(StudyStatistics::getStudyDay,
            DateUtil.format(day, DatePattern.NORM_DATE_PATTERN));

        return this.getOne(wrapper);
    }
}
