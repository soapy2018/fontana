package com.fontana.util.date;

import cn.hutool.core.date.DateField;
import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.format.FastDateFormat;

import java.text.ParseException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAccessor;
import java.util.Date;
import java.util.TimeZone;

/**
 * DateTime 工具类
 *
 * @author L.cm
 */
public class DateTimeUtil {
	public static final String DEFAULT_PATTERN_DAY = DatePattern.NORM_DATE_PATTERN;
	public static final String DEFAULT_PATTERN_DAY_TIME = DatePattern.NORM_DATETIME_PATTERN;
	public static final String DEFAULT_PATTERN_TIME = DatePattern.NORM_TIME_PATTERN;
	public static final String UTC_PATTERN_DAY_TIME = DatePattern.UTC_PATTERN;
	public static final String UTC_PATTERN_DAY_TIME_ZONE = DatePattern.UTC_MS_WITH_ZONE_OFFSET_PATTERN;
	public static final String UTC_PATTERN_DAY_TIME_ZONE_XXX = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX";
	public static final DateTimeFormatter DATETIME_FORMAT = DateTimeFormatter.ofPattern(DEFAULT_PATTERN_DAY_TIME);
	public static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern(DEFAULT_PATTERN_DAY);
	public static final DateTimeFormatter TIME_FORMAT =  DateTimeFormatter.ofPattern(DEFAULT_PATTERN_TIME);

	/**
	 * 日期时间格式化
	 *
	 * @param temporal 时间
	 * @return 格式化后的时间
	 */
	public static String formatDateTime(TemporalAccessor temporal) {
		return DATETIME_FORMAT.format(temporal);
	}

	/**
	 * 日期时间格式化
	 *
	 * @param temporal 时间
	 * @return 格式化后的时间
	 */
	public static String formatDate(TemporalAccessor temporal) {
		return DATE_FORMAT.format(temporal);
	}

	/**
	 * 时间格式化
	 *
	 * @param temporal 时间
	 * @return 格式化后的时间
	 */
	public static String formatTime(TemporalAccessor temporal) {
		return TIME_FORMAT.format(temporal);
	}

	/**
	 * 日期格式化
	 *
	 * @param temporal 时间
	 * @param pattern  表达式
	 * @return 格式化后的时间
	 */
	public static String format(TemporalAccessor temporal, String pattern) {
		return DateTimeFormatter.ofPattern(pattern).format(temporal);
	}

	/**
	 * 将字符串转换为时间
	 *
	 * @param dateStr 时间字符串
	 * @param pattern 表达式
	 * @return 时间
	 */
	public static TemporalAccessor parse(String dateStr, String pattern) {
		DateTimeFormatter format = DateTimeFormatter.ofPattern(pattern);
		return format.parse(dateStr);
	}

	/**
	 * 将字符串转换为时间
	 *
	 * @param dateStr   时间字符串
	 * @param formatter DateTimeFormatter
	 * @return 时间
	 */
	public static TemporalAccessor parse(String dateStr, DateTimeFormatter formatter) {
		return formatter.parse(dateStr);
	}

	/**
	 * 时间转 Instant
	 *
	 * @param dateTime 时间
	 * @return Instant
	 */
	public static Instant toInstant(LocalDateTime dateTime) {
		return dateTime.atZone(ZoneId.systemDefault()).toInstant();
	}

	/**
	 * Instant 转 时间
	 *
	 * @param instant Instant
	 * @return Instant
	 */
	public static LocalDateTime toLocalDateTime(Instant instant) {
		return LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
	}



	public static Date toDate(LocalDateTime localDateTime) {
		return Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
	}


	public static LocalDateTime toLocalDateTime(Date date) {
		return Instant.ofEpochMilli(date.getTime()).atZone(ZoneId.systemDefault()).toLocalDateTime();
	}


	/**
	 * 毫秒时间戳格式化
	 * @param millSecond
	 * @return
	 */
	public static String formatMillSecond(long millSecond){
		return fromMillSecond(millSecond).format(DATETIME_FORMAT);
	}

	/**
	 * 毫秒转localDateTime
	 *
	 * @param milliseconds 毫秒
	 * @return
	 */
	public static LocalDateTime fromMillSecond(long milliseconds) {
		return LocalDateTime.ofInstant(Instant.ofEpochMilli(milliseconds), ZoneId.systemDefault());
	}

	/**
	 * 日期格式化
	 * @param date
	 * @return
	 */
	public static String formatDate(Date date) {
		return fromMillSecond(date.getTime()).format(DATETIME_FORMAT);
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
