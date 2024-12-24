package com.lxy.common.service;

import com.lxy.common.po.StudyStatistics;
import com.baomidou.mybatisplus.extension.service.IService;

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
    boolean saveStatistics(Integer userId, Date day,Integer duration);

    /**
     * 根据日期获取统计数据
     */
    StudyStatistics getStatisticsByDay(Integer userId, Date day);

}
