package com.lxy.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.lxy.system.po.StudyStatistics;

import java.util.Date;

/**
 * <p>
 * 学习统计 服务类
 * </p>
 *
 * @author jiacheng yang.
 * @since 2022-12-23
 */
public interface StudyStatisticsService extends IService<StudyStatistics> {

    /**
     * 保存统计数据
     */
    boolean saveStatistics(Long userId, Date day, Integer duration);

    /**
     * 根据日期获取统计数据
     */
    StudyStatistics getStatisticsByDay(Long userId, Date day);

}
