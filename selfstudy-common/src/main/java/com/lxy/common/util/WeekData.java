package com.lxy.common.util;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.LocalDateTimeUtil;

import java.time.LocalDate;
import java.util.List;

/**
 * TODO
 *
 * @author jiacheng yang.
 * @version 1.0
 * @since 2022/12/09 14:54
 */
public class WeekData {

    // 开始时间
    private LocalDate start;
    // 结束时间
    private LocalDate end;

    public WeekData() {
    }

    public WeekData(LocalDate start, LocalDate end) {
        this.start = start;
        this.end = end;
    }

    public WeekData(List<LocalDate> localDates) {
        this.start = localDates.get(0);
        this.end = localDates.get(localDates.size() - 1);
    }

    public String getStart() {
        return LocalDateTimeUtil.format(start, DatePattern.NORM_DATE_PATTERN);
    }

    public void setStart(LocalDate start) {
        this.start = start;
    }

    public String getEnd() {
        return LocalDateTimeUtil.format(end, DatePattern.NORM_DATE_PATTERN);
    }

    public void setEnd(LocalDate end) {
        this.end = end;
    }

}
