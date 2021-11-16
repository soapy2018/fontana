package com.bluetron.nb.common.util.date;

import cn.hutool.core.date.DateField;
import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.format.FastDateFormat;
import lombok.extern.slf4j.Slf4j;

import java.text.ParseException;
import java.util.Date;
import java.util.TimeZone;

@Slf4j
public class DateUtil {

    public static final String DEFAULT_PATTERN_DAY = DatePattern.NORM_DATE_PATTERN;
    public static final String DEFAULT_PATTERN_DAY_TIME = DatePattern.NORM_DATETIME_PATTERN;
    public static final String UTC_PATTERN_DAY_TIME = DatePattern.UTC_PATTERN;
    public static final String UTC_PATTERN_DAY_TIME_ZONE = DatePattern.UTC_MS_WITH_ZONE_OFFSET_PATTERN;
    public static final String UTC_PATTERN_DAY_TIME_ZONE_XXX = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX";
    private DateUtil() {

    }

    /**
     * @return 今年
     */
    public static Integer currentYear() {
        return cn.hutool.core.date.DateUtil.thisYear();
    }

    /**
     * @return 当前真实月份，不是从0开始
     */
    public static Integer currentMonth() {
        return cn.hutool.core.date.DateUtil.thisMonth() + 1;
    }

    /**
     * 格式 yyyy-MM-dd
     *
     * @return 当前日期，形如：2019-01-01
     */
    public static String currentDateStr() {
        Date date = new Date();
        return dateToString(date, DEFAULT_PATTERN_DAY);
    }

    /**
     * 格式 yyyy-MM-dd HH:mm:ss
     *
     * @return 当前日期，形如：2019-01-01 12:12:12
     */
    public static String currentDateTimeStr() {
        return cn.hutool.core.date.DateUtil.now();
    }


    /**
     * 不带时分秒的转换
     *
     * @param date
     * @return
     */
    public static Date stringToDateNoTime(String date) {
        return stringToDate(date, DEFAULT_PATTERN_DAY);
    }

    /**
     * 字符串转Date默认
     *
     * @param date
     * @return
     */
    public static Date stringToDate(String date) {
        return stringToDate(date, DEFAULT_PATTERN_DAY_TIME);
    }

    /**
     * 字符串根据Patten转Date
     *
     * @param date
     * @param pattern
     * @return
     */
    public static Date stringToDate(String date, String pattern) {

        Date myDate = null;

        FastDateFormat fastFormat = FastDateFormat.getInstance(pattern);

        if (date != null) {
            try {
                myDate = fastFormat.parse(date);
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }
        }
        return myDate;
    }

    /**
     * 字符串根据Patten 和 timezone 转Date
     *
     * @param date
     * @param pattern
     * @param timezone
     * @return
     */
    public static Date stringToDate(String date, String pattern, TimeZone timezone) {

        Date myDate = null;

        FastDateFormat fastFormat = FastDateFormat.getInstance(pattern, timezone);

        if (date != null) {
            try {
                myDate = fastFormat.parse(date);
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }
        }
        return myDate;
    }

    /**
     * 不带时分秒的转换
     *
     * @param date
     * @return
     */
    public static String dateToStringNoTime(Date date) {
        return dateToString(date, DEFAULT_PATTERN_DAY);
    }

    public static String dateToString(Date date) {
        return dateToString(date, DEFAULT_PATTERN_DAY_TIME);
    }

    /**
     * date转字符串
     *
     * @param date
     * @param pattern
     * @return
     */
    public static String dateToString(Date date, String pattern) {
        return cn.hutool.core.date.DateUtil.format(date, pattern);
    }

    /**
     * 带冒号的时区格式
     *
     * @param date
     * @return
     */
    public static String dateToStringWithColon(Date date) {
        return cn.hutool.core.date.DateUtil.format(date, UTC_PATTERN_DAY_TIME_ZONE_XXX);
    }

    /**
     * 格式化
     *
     * @param date
     * @param pattern
     * @param timezone
     * @return
     * @throws RuntimeException
     */
    public static String dateToString(Date date, String pattern, TimeZone timezone) throws RuntimeException {
        FastDateFormat fastFormat = FastDateFormat.getInstance(pattern, timezone);
        return fastFormat.format(date);
    }

    /**
     * 两个日期计算相差天数
     *
     * @param date
     * @param otherDate
     * @param reset     相邻一天是否差1
     * @return
     */
    public static long getIntervalDays(Date date, Date otherDate, boolean reset) {
        return cn.hutool.core.date.DateUtil.betweenDay(date, otherDate, reset);
    }

    /**
     * 两个日期相差年数 保留一位小数，返回float
     *
     * @param date
     * @param otherDate
     * @return
     */
    public static double getIntervalYears(Date date, Date otherDate) {
        double k = getIntervalDays(date, otherDate, true) / 365.0;
        return Math.round(k * 10) / 10.0;
    }

    /**
     * 两个日期相差年数，用来计算年龄，返回整形
     *
     * @param date
     * @param otherDate
     * @return
     */
    public static int getIntervalYearsInt(Date date, Date otherDate) {
        return (int) (getIntervalDays(date, otherDate, true) / 365);
    }

    /**
     * 返回指定年数位移后的日期
     */
    public static Date yearOffset(Date date, int offset) {
        return cn.hutool.core.date.DateUtil.offset(date, DateField.YEAR, offset);
    }

    /**
     * 返回指定月数位移后的日期
     */
    public static Date monthOffset(Date date, int offset) {
        return cn.hutool.core.date.DateUtil.offset(date, DateField.MONTH, offset);
    }

    /**
     * 返回指定天数位移后的日期
     */
    public static Date dayOffset(Date date, int offset) {
        return cn.hutool.core.date.DateUtil.offset(date, DateField.DAY_OF_YEAR, offset);
    }

    /**
     * 返回指定秒数位移后的日期
     */
    public static Date secondOffset(Date date, int offset) {
        return cn.hutool.core.date.DateUtil.offset(date, DateField.SECOND, offset);
    }

}
