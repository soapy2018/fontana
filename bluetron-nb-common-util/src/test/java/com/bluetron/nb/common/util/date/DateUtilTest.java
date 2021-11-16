package com.bluetron.nb.common.util.date;

import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Test;

import java.util.Date;
import java.util.TimeZone;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
public class DateUtilTest {

    private Long timestamp = 1578628805000L; // 北京时间 2020-01-10 12:00:05

    @Before
    public void initTimeZone() {
        TimeZone.setDefault(TimeZone.getTimeZone("GMT+08:00"));
    }

    @Test
    public void utilsTestDateToString() {

        log.info("thisyear: {}", DateUtil.currentYear());
        log.info("thismonth: {}", DateUtil.currentMonth());
        log.info("currentDateStr: {}", DateUtil.currentDateStr());
        log.info("currentDateTimeStr: {}", DateUtil.currentDateTimeStr());

        log.info(DateUtil.dateToString(DateUtil.dayOffset(new Date(), 2)));
        log.info(DateUtil.dateToString(DateUtil.monthOffset(new Date(), 2)));
        log.info(DateUtil.dateToString(DateUtil.yearOffset(new Date(), 2)));

        Date testDay = DateUtil.stringToDate("2020-01-10 12:00:05");

        assertThat(DateUtil.dateToString(testDay)).isEqualTo("2020-01-10 12:00:05");
        assertThat(DateUtil.dateToString(testDay, "YYYY/MM/dd HH/mm/ss", TimeZone.getDefault())).isEqualTo("2020/01/10 12/00/05");
        assertThat(DateUtil.dateToStringNoTime(testDay)).isEqualTo("2020-01-10");

        DateUtil.stringToDateNoTime("2020-01-10");

        // timezone without colon
        String testValue = DateUtil.dateToString(new Date(timestamp), DateUtil.UTC_PATTERN_DAY_TIME_ZONE);
        assertThat(testValue).isEqualTo("2020-01-10T12:00:05.000+0800");

        // test timezone with colon
        String testValue1 = DateUtil.dateToStringWithColon(new Date(timestamp));
        assertThat(testValue1).isEqualTo("2020-01-10T12:00:05.000+08:00");
    }

    @Test
    public void timeZoneTest() {

        // UTC0 test
        String UTC0 = "2020-01-10T04:00:05Z";
        Date testDay = DateUtil.stringToDate(UTC0, DateUtil.UTC_PATTERN_DAY_TIME, TimeZone.getTimeZone("GMT"));
        assertThat(testDay.getTime()).isEqualTo(timestamp);

        // UTC0 Test
        String UTC8time0 = "2020-01-10T12:00:05.000Z";
        Date utc8date0 = DateUtil.stringToDate(UTC8time0, "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        assertThat(utc8date0.getTime()).isEqualTo(timestamp);

        // UTC8 test
        String UTC8time = "2020-01-10T12:00:05.000+0800";
        Date utc8date = DateUtil.stringToDate(UTC8time, DateUtil.UTC_PATTERN_DAY_TIME_ZONE);
        assertThat(utc8date.getTime()).isEqualTo(timestamp);

        // UTC8 test1
        String UTC8time1 = "2020-01-10T12:00:05.000+08:00";
        Date utc8date1 = DateUtil.stringToDate(UTC8time1, DateUtil.UTC_PATTERN_DAY_TIME_ZONE);
        assertThat(utc8date1.getTime()).isEqualTo(timestamp);

    }

    @Test
    public void testOffset() {

        Date testDay = DateUtil.stringToDate("2020-01-10 12:00:05");

        // 2年后
        assertThat(DateUtil.yearOffset(testDay, 2)).isEqualTo(DateUtil.stringToDate("2022-01-10 12:00:05"));

        // 2月后
        assertThat(DateUtil.monthOffset(testDay, 2)).isEqualTo(DateUtil.stringToDate("2020-03-10 12:00:05"));

        // 2天后
        assertThat(DateUtil.dayOffset(testDay, 2)).isEqualTo(DateUtil.stringToDate("2020-01-12 12:00:05"));

        // 2秒后
        assertThat(DateUtil.secondOffset(testDay, 2)).isEqualTo(DateUtil.stringToDate("2020-01-10 12:00:07"));
    }

    @Test
    public void testInterval() {

        Date beginDay = DateUtil.stringToDate("2020-01-10 12:00:05");

        Date otherDay = DateUtil.stringToDate("2020-01-11 00:00:05");

        assertThat(DateUtil.getIntervalDays(beginDay, otherDay, true)).isEqualTo(1);

        // 2.5年
        assertThat(DateUtil.getIntervalYears(DateUtil.stringToDate("2018-01-10 12:00:05"),
                DateUtil.stringToDate("2020-07-10 00:00:05"))).isEqualTo(2.5);

        // 2岁
        assertThat(DateUtil.getIntervalYearsInt(DateUtil.stringToDate("2018-01-10 12:00:05"),
                DateUtil.stringToDate("2020-07-10 00:00:05"))).isEqualTo(2);
    }

}
