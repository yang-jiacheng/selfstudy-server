package com.lxy.common.util;

import cn.hutool.core.date.*;
import cn.hutool.core.img.ImgUtil;
import cn.hutool.core.io.FileUtil;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAdjusters;
import java.time.temporal.WeekFields;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 * TODO
 * @author jiacheng yang.
 * @since 2022/12/19 16:11
 * @version 1.0
 */
public class DateCusUtil {

    /**
     * 获取现在到今天结束的时间,秒数
     */
    public static long getEndByDay(){
        Date now = new Date();
        //一天的结束
        DateTime endOfDay = DateUtil.endOfDay(now);
        return DateUtil.between(now, endOfDay, DateUnit.SECOND);
    }


    /**
     * 获取指定日期期间的每一天
     */
    public static List<String> getWeedAllDay(String start, String end){
        List<String> allDate = new ArrayList<>();

        allDate.add(start);
        //字符串格式化成Date
        Date dBegin = DateUtil.parse(start, DatePattern.NORM_DATE_PATTERN);
        Date dEnd = DateUtil.parse(end, DatePattern.NORM_DATE_PATTERN);

        Calendar calBegin = Calendar.getInstance();
        // 使用给定的 Date 设置此 Calendar 的时间
        calBegin.setTime(dBegin);
        Calendar calEnd = Calendar.getInstance();
        // 使用给定的 Date 设置此 Calendar 的时间
        calEnd.setTime(dEnd);
        // 测试此日期是否在指定日期之后
        while (dEnd.after(calBegin.getTime())) {
            // 根据日历的规则，为给定的日历字段添加或减去指定的时间量
            calBegin.add(Calendar.DAY_OF_MONTH, 1);
            String format = DateUtil.format(calBegin.getTime(), DatePattern.NORM_DATE_PATTERN);
            allDate.add(format);
        }
        return allDate;
    }

    public static List<String> getWeedAllDayStream(String start,String end){
        String format = "yyyy-MM-dd";
        LocalDate startDate = LocalDate.parse(start, DateTimeFormatter.ofPattern(format));
        LocalDate endDate = LocalDate.parse(end, DateTimeFormatter.ofPattern(format));
        return Stream.iterate(startDate, date -> date.plusDays(1))
                .limit(ChronoUnit.DAYS.between(startDate, endDate.plusDays(1)))
                .map(date -> date.format(DateTimeFormatter.ofPattern(format)))
                .collect(Collectors.toList());
    }

    /**
     * 获取具体的日期是周几
     */
    public static String getWeekByDay(Date date){
        String[] weeks = {"周日","周一","周二","周三","周四","周五","周六"};
        //Date eeee = DateUtil.parse(date, DatePattern.NORM_DATE_PATTERN);
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int week_index = cal.get(Calendar.DAY_OF_WEEK) - 1;
        if(week_index<0){
            week_index = 0;
        }
        return weeks[week_index];
    }

    /**
     * 给定年获取每月的开始和结束时间
     */
    public static List<WeekData> months(int year) {
        List<WeekData> list = new ArrayList<>(12);
        WeekData weekData = null;
        LocalDate start ;
        LocalDate end ;
        for (int i = 1; i <= 12; i++) {
            start = LocalDate.now().withYear(year).withMonth(i).with(TemporalAdjusters.firstDayOfMonth());
            end = LocalDate.now().withYear(year).withMonth(i).with(TemporalAdjusters.lastDayOfMonth());
            weekData = new WeekData();
            weekData.setStart(start);
            weekData.setEnd(end);
            list.add(weekData);
        }
        return list;
    }

    public static List<WeekData> monthsStream(int year){
        return IntStream.rangeClosed(1, 12)
                .mapToObj(month -> {
                    LocalDate start = LocalDate.of(year, month, 1);
                    LocalDate end = start.with(TemporalAdjusters.lastDayOfMonth());
                    return new WeekData(start, end);
                })
                .collect(Collectors.toList());
    }

    /**
     * 给定年月获取每周的开始和结束时间
     */
    public static List<WeekData> weeks(int year, int month) {
        LocalDate start = LocalDate.now().withYear(year).withMonth(month).with(TemporalAdjusters.firstDayOfMonth());
        LocalDate end = LocalDate.now().withYear(year).withMonth(month).with(TemporalAdjusters.lastDayOfMonth());
        Map<Integer, WeekData> map = Stream.iterate(start, localDate -> localDate.plusDays(1))
                .limit(ChronoUnit.DAYS.between(start, end) + 1)
                .collect(Collectors.groupingBy(localDate -> localDate.get(WeekFields.of(DayOfWeek.MONDAY, 1).weekOfMonth()),
                        Collectors.collectingAndThen(Collectors.toList(), WeekData::new)));
        List<WeekData> list = new ArrayList<>(map.size());
        map.forEach((key, value) -> list.add(value));
        list.sort(Comparator.comparing(WeekData::getStart));
        return list;
    }

    public static List<WeekData> getWeeksInMonth(int year, int month) {
        List<WeekData> weeks = new ArrayList<>();
        LocalDate start = LocalDate.of(year, month, 1);
        while (start.getMonthValue() == month) {
            LocalDate endOfWeek = start.with(TemporalAdjusters.nextOrSame(DayOfWeek.SUNDAY));
            if (endOfWeek.getMonthValue() != month) {
                endOfWeek = start.with(TemporalAdjusters.lastDayOfMonth());
            }
            weeks.add(new WeekData( start, endOfWeek));
            start = endOfWeek.plusDays(1);
        }
        return weeks;
    }



    public static void main(String[] args) {
        DateTime parse = DateUtil.parse("2022-12-18 00:00:15", DatePattern.NORM_DATETIME_PATTERN);

        Date mon = getMon(parse);

        System.out.println(DateUtil.format(mon,DatePattern.NORM_DATE_PATTERN));
//        String weekByDay = getWeekByDay(parse);
//        System.out.println(weekByDay);
    }

    /**
     * 获取给定日期的周一
     */
    public static Date getMon(Date date){
        if ("周日".equals(getWeekByDay(date))){
            date = DateUtil.offsetDay(date,-1);
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.setWeekDate(calendar.getWeekYear(), calendar.get(Calendar.WEEK_OF_YEAR), Calendar.MONDAY);
        calendar.set(calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH),
                0, 0, 0);
        return calendar.getTime();
    }

    /**
     * 获取给定日期的周日
     */
    public static Date getSun(Date date){
        if ("周日".equals(getWeekByDay(date))){
            date = DateUtil.offsetDay(date,-1);
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.setWeekDate(calendar.getWeekYear(), calendar.get(Calendar.WEEK_OF_YEAR)+1, Calendar.SUNDAY);
        calendar.set(calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH),
                23, 59, 59);
        return calendar.getTime();
    }

    /**
     * Date 转 LocalDate
     */
    public static LocalDate dateTransToLocalDate(Date date){
        Instant instant =date.toInstant();
        ZoneId zone = ZoneId.systemDefault();
        LocalDateTime localDateTime = LocalDateTime.ofInstant(instant, zone);
        return localDateTime.toLocalDate();
    }

    /**
     * LocalDate 转 Date
     */
    public static Date localDateTransToDate(LocalDate date){
        ZoneId zone = ZoneId.systemDefault();
        Instant instant = date.atStartOfDay().atZone(zone).toInstant();
        return Date.from(instant);
    }


}
