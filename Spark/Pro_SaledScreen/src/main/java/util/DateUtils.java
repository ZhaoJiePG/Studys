package util;

import org.apache.commons.lang3.StringUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.*;
import java.time.chrono.ChronoLocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.time.temporal.IsoFields;
import java.time.temporal.TemporalAdjusters;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import static java.time.LocalTime.MAX;

public class DateUtils {

    private static final DateTimeFormatter DATE_PATTERN = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static final DateTimeFormatter TIME_PATTERN = DateTimeFormatter.ofPattern("HH:mm:ss");
    private static final DateTimeFormatter DATE_TIME_PATTERN = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private static final DateTimeFormatter DATE_TIME_MILLS_PATTERN = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSSSSS");

    /**
     * 将date类型转换成LocalDateTime
     *
     * @param date
     * @return
     */
    public static LocalDateTime dateConvertToLocalDateTime(Date date) {
        return date.toInstant().atOffset(ZoneOffset.of("+8")).toLocalDateTime();
    }

    /**
     * 将localDateTime类型转换成date类型
     *
     * @param localDateTime
     * @return
     */
    public static Date localDateTimeConvertToDate(LocalDateTime localDateTime) {
        return Date.from(localDateTime.toInstant(ZoneOffset.of("+8")));
    }

    /**
     * 获取当前时间的秒值
     *
     * @return
     */
    public static long getSecond() {
        return LocalDateTime.now().toEpochSecond(ZoneOffset.of("+8"));
    }

    /**
     * 获取当前时间的毫秒值
     *
     * @return
     */
    public static long getMilliSecond() {
        return LocalDateTime.now().toInstant(ZoneOffset.of("+8")).toEpochMilli();
    }

    /**
     * 获取两个时间的Period
     *
     * @param startYear
     * @param startMonth
     * @param startDay
     * @return
     */
    public static Period getPeriodBetweenTwoDate(int startYear, int startMonth, int startDay) {
        LocalDate start = LocalDate.of(startYear, startMonth, startDay);
        LocalDate end = LocalDate.now();
        return Period.between(start, end);
    }

    /**
     * 解析yyyy-MM-dd格式的字符串,返回LocalDate
     *
     * @param date
     * @return LocalDate
     */
    public static LocalDate parseDate(String date) {
        return LocalDate.parse(date, DATE_PATTERN);
    }

    /**
     * 解析一个LocalDate, 转换成yyyy-MM-dd格式的字符串
     *
     * @param date
     * @return String
     */
    public static String formatDate(LocalDate date) {
        return date.format(DATE_PATTERN);
    }

    /**
     * 解析HH:mm:ss格式的字符串,返回LocalTime
     *
     * @param time
     * @return LocalTime
     */
    public static LocalTime parseTime(String time) {
        return LocalTime.parse(time, TIME_PATTERN);
    }

    /**
     * 解析LocalTime, 返回HH:mm:ss格式的字符串
     *
     * @param time
     * @return String
     */
    public static String formatDate(LocalTime time) {
        return time.format(TIME_PATTERN);
    }

    /**
     * 解析yyyy-MM-dd HH:mm:ss格式的字符串 返回LocalDateTime
     *
     * @param dateTime
     * @return LocalDateTime
     */
    public static LocalDateTime parseDateTime(String dateTime) {
        return LocalDateTime.parse(dateTime, DATE_TIME_PATTERN);
    }

    /**
     * 解析yyyy-MM-dd HH:mm:ss.f格式的字符串 返回LocalDateTime
     *
     * @param dateTime
     * @return LocalDateTime
     */
    public static LocalDateTime parseDateTimeMills(String dateTime) {
        return LocalDateTime.parse(dateTime, DATE_TIME_PATTERN);
    }

    /**
     * 返回LocalDateTime,转换成yyyy-MM-dd HH:mm:ss格式的字符串
     *
     * @param dateTime
     * @return String
     */
    public static String formatDate(LocalDateTime dateTime) {
        return dateTime.format(DATE_TIME_PATTERN);
    }

    /**
     * 获取时间在第几周
     *
     * @param dateTime
     * @return int
     */
    public static int getWeekOfYear(LocalDateTime dateTime) {
        return dateTime.get(IsoFields.WEEK_OF_WEEK_BASED_YEAR);
    }

    /**
     * 获取当前天的开始时间  返回yyyy-MM-dd HH:mm:ss 字符串
     *
     * @return yyyy-MM-dd HH:mm:ss
     */
    public static String getCurrentDateStratDateTimeStr() {
        LocalDateTime firstDateTime = LocalDateTime.of(LocalDate.now(), LocalTime.MIN);
        return firstDateTime.format(DATE_TIME_PATTERN);
    }

    /**
     * 获取当前天的开始时间  返回LoclDateTime
     *
     * @return LocalDateTime
     */
    public static LocalDateTime getCurrentDateStratDateTime() {
        return LocalDateTime.of(LocalDate.now(), LocalTime.MIN);
    }

    /**
     * 获取当前天的结束时间  返回yyyy-MM-dd HH:mm:ss 字符串
     *
     * @return yyyy-MM-dd HH:mm:ss
     */
    public static String getCurrentDateEndDateTimeStr() {
        LocalDateTime firstDateTime = LocalDateTime.of(LocalDate.now(), MAX);
        return firstDateTime.format(DATE_TIME_PATTERN);
    }

    /**
     * 获取当前天的结束时间  返回LoclDateTime
     *
     * @return LocalDateTime
     */
    public static LocalDateTime getCurrentDateEndDateTime() {
        return LocalDateTime.of(LocalDate.now(), MAX);
    }

    /**
     * 获取当前时间  返回yyyy-MM-dd HH:mm:ss 字符串
     *
     * @return yyyy-MM-dd HH:mm:ss
     */
    public static String getCurrentDateTimeStr() {
        LocalDateTime firstDateTime = LocalDateTime.now();
        return firstDateTime.format(DATE_TIME_PATTERN);
    }

    /**
     * 获取当前时间到第二天0时0分0秒的区间秒数
     *
     * @return long
     */
    public static long getDiffCurrent2ZeroTime() {
        return TimeUnit.MILLISECONDS.toSeconds(Duration.between(LocalDateTime.now(), LocalDateTime.of(LocalDate.now().plusDays(1), LocalTime.MIDNIGHT)).toMillis());
    }

    /**
     * 获取当前小时
     *
     * @return
     */
    public static int getCureentHour() {
        return LocalTime.now().getHour();
    }

    /**
     * 获取当天的一个整点最小时间  返回 yyyy-MM-dd XX:00:00
     *
     * @param hour 2位小时数字 例:yyyy-MM-dd 08:00:00
     * @return yyyy-MM-dd min:00:00
     */
    public static String getCurrDayDateTimeStrByHour_Min(String hour) {
        return LocalDate.now().format(DATE_PATTERN) + " " + hour + ":00:00";
    }

    /**
     * 获取当天的一个整点最大时间  返回 yyyy-MM-dd XX:59:59
     *
     * @param hour 2位小时数字 例:08
     * @return yyyy-MM-dd max:59:59
     */
    public static String getCurrDayDateTimeStrByHour_Max(String hour) {
        return LocalDate.now().format(DATE_PATTERN) + " " + hour + ":59:59";
    }


    /**
     * 获取当前月第一天的开始时间 返回yyyy-MM-dd HH:mm:ss 字符串
     *
     * @return yyyy-MM-dd HH:mm:ss
     */
    public static String getCurrMonthFirstDayStartDateTimeStr() {
        LocalDateTime firstDayDateTime = LocalDateTime.of(LocalDate.now().with(TemporalAdjusters.firstDayOfMonth()), LocalTime.MIN);
        return firstDayDateTime.format(DATE_TIME_PATTERN);
    }

    /**
     * 获取前一天的结束时间 返回yyyy-MM-dd HH:mm:ss 字符串
     *
     * @return yyyy-MM-dd HH:mm:ss
     */
    public static String getPreDayEndDateTimeStr() {
        LocalDateTime firstDayDateTime = LocalDateTime.of(LocalDate.now().plusDays(-1), LocalTime.MAX);
        return firstDayDateTime.format(DATE_TIME_PATTERN);
    }

    /**
     * 传入2个 yyyy-MM-dd HH:mm:ss, 比较date1 是否在date2之前
     *
     * @param date1 时间1
     * @param date2 时间2
     * @return boolean
     */
    public static boolean get2DateTimeCompare(String date1, String date2) {
        return parseDateTime(date1).isAfter(parseDateTime(date2));
    }

    /**
     * 获取当前年月日
     *
     * @return 当前yyyy-MM-dd
     */
    public static String getCurrentDateStr() {
        return LocalDate.now().format(DATE_PATTERN);
    }

    /**
     * 获取昨天年月日
     *
     * @return 昨天yyyy-MM-dd
     */
    public static String getYesterdayDateStr() {
        return LocalDate.now().minusDays(1).format(DATE_PATTERN);
    }

    /**
     * 获取昨天年月日
     *
     * @return LocalDate
     */
    public static LocalDate getYesterdayDate() {
        return LocalDate.now().minusDays(1);
    }

    /**
     * 获取昨天前年月日
     *
     * @return 昨天yyyy-MM-dd
     */
    public static String getYesterdayMinus1YearDateStr() {
        return LocalDate.now().minusYears(1).minusDays(1).format(DATE_PATTERN);
    }

    /**
     * 判断一个yyyy-MM-dd 是否在昨日(包含)往前推一年范围内
     *
     * @param date
     * @return
     */
    public static boolean checkInputIsBetweenYesterdayAndYesteryear(String date) {
        LocalDate inputDate = parseDate(date);
        return 365 >= ChronoUnit.DAYS.between(inputDate, getYesterdayDate());
    }

    /**
     * 判断一个字符串日志是否在一个月以内, 传入yyyy-MM-dd HH:mm:ss.SSSSSS, 截取yyyy-MM-dd
     *
     * @param date input
     * @return bool
     */
    public static boolean checkInputDateIsInLastMonth(String date) {
        LocalDate inputDate = parseDate(date.substring(0, 10));
        LocalDate pre30DaylocalDate = LocalDate.now().minusDays(31);
        return pre30DaylocalDate.isBefore(inputDate);
    }

    /**
     * 获取2个时间相差的秒
     *
     * @param time1 yyyy-MM-dd HH:mm:ss.SSSSSS
     * @param time2 yyyy-MM-dd HH:mm:ss.SSSSSS
     * @return long
     */
    public static long get2TimeDiffSeconds(String time1, String time2) {
        try {
            LocalDateTime orderTime = parseDateTime(time1.substring(0, time1.indexOf(".")));
            LocalDateTime createTime = parseDateTime(time2.substring(0, time1.indexOf(".")));
            return ChronoUnit.SECONDS.between(orderTime, createTime);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            System.out.println(time1 + "|" + time2);
        }
        return 0;
    }

    /**
     * 获取存入yyyy-MM-dd HH-mm-ss 时间字符串,返回小时
     *
     * @param time
     * @return int
     */
    public static int getHourByInputDateStr(String time) {
        return parseDateTime(time).getHour();
    }

    /**
     * 比较2个年月日的的相差天数
     *
     * @param date1 yyyyMMdd
     * @param date2 yyyy-MM-dd
     * @return long
     */
    public static long get2DateDiffDay(String date1, String date2) {
        try {
            LocalDate localDate1 = parseDate(date1);
            LocalDate localDate2 = parseDate(date2);
            return ChronoUnit.DAYS.between(localDate1, localDate2);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            System.out.println(date1 + "|" + date2);
        }
        return 0;
    }

    /**
     * 获取当前年月日 返回 LocalDate
     *
     * @return LocalDate
     */
    public static LocalDate getCurrentLocalDate() {
        return LocalDate.now();
    }

    /**
     * 判断一个yyyy-MM-dd 是否在近12个月内
     *
     * @param ymdDate input
     * @return boolean
     */
    public static boolean checkInputInYester12month(String ymdDate) {
        try {
        LocalDate inputDate = parseDate(ymdDate);
        return 11 >= ChronoUnit.MONTHS.between(inputDate, getCurrentLocalDate());
        } catch (Exception e) {
            System.out.println(e.getMessage());
            System.out.println(ymdDate);
        }
        return false;
    }



    public static void main(String[] args) throws Exception {
//        System.out.println(getCurrentDateTimeStr());
//
//        System.out.println(getPreDayEndDateTimeStr());
//        String t = "2017-08-23 13:34:51.0";
//        System.out.println(t.substring(0,t.indexOf(".")));
        //System.out.println(parseDateTimeMills("2017-08-23 13:34:51.0").toString());

//        System.out.println("yyyy-MM-dd HH:mm:ss".substring(0,11));
//        System.out.println("yyyy-MM-dd HH:mm:ss".substring(13,18));
//        System.out.println(getCurrDayDateTimeStrByHour_Min("01"));
//        System.out.println(getCurrentDateStratDateTimeStr());
//        System.out.println(getCurrentDateEndDateTimeStr());
//        LocalDateTime firstDateTime = LocalDateTime.of(LocalDate.now(), LocalTime.MIDNIGHT);
//        System.out.println(firstDateTime.format(DATE_TIME_PATTERN));
//        System.out.println("----------------------------------------");
//
//        System.out.println(LocalDateTime.of(LocalDate.now().plusDays(1), LocalTime.MIDNIGHT).format(DATE_TIME_PATTERN));
//        System.out.println(getDiffCurrent2ZeroTime());
         /*System.out.println(dateTimeToDate(new Date()));
         System.out.println(dateParse("2017-02-04 14:58:20", null));
         System.out.println(dateTimeToDateStringIfTimeEndZero(new Date()));
         System.out.println(dateTimeToDateStringIfTimeEndZero(dateTimeToDate(new Date())));*/
        //System.out.println(dateBetween(dateParse("2017-01-30", null), dateParse("2017-02-01", null)));
        //System.out.println(dateBetweenIncludeToday(dateParse("2017-01-30", null), dateParse("2017-02-01", null)));
        // System.out.println(getDate(dateParse("2017-01-17", null)));
         /*
         System.out.println(getDaysOfMonth(dateParse("2017-02-01", null)));
         System.out.println(getDaysOfYear(dateParse("2017-01-30", null)));*/
        //System.out.println(dateFormat(dateAddMonths(dateParse("2017-02-07", StrUtils.MONTH_PATTERN), -12), StrUtils.MONTH_PATTERN));
         /*System.out.println(dateFormat(maxDateOfMonth(dateParse("2016-02", "yyyy-MM")), null));
         System.out.println(dateFormat(minDateOfMonth(dateParse("2016-03-31", null)), null));*/
//        LocalDate inputDate = parseDate("2018-01-01");
//        System.out.println(ChronoUnit.DAYS.between(inputDate, getYesterdayDate()));
//        System.out.println(checkInputIsBetweenYesterdayAndYesteryear("2019-05-14"));
//        System.out.println(get2TimeDiffSeconds("2018-07-17 09:24:25.076" , "2018-07-17 09:24:31.909"));
//        System.out.println("2018-07-17 09:24:25.076".substring(0, 10));
//
//        System.out.println(LocalDate.now().minusDays(31));
//        System.out.println(checkInputDateIsInLastMonth("2019-04-21 09:24:25."));
//        System.out.println("2018-07-17 09:24:25.076".substring(11, 13));
//        System.out.println("2018-07-17 09:24:25.076".substring(0, "2018-07-17 09:24:25.076".indexOf(".")));
//        System.out.println("2018-07-17 09:24:25".substring("2018-07-17 09:24:25.076".indexOf(" "), "2018-07-17 09:24:25.076".indexOf(":")));
//        System.out.println("2018-07-17 9:24:25".substring("2018-07-17 09:24:25.076".indexOf(" "), "2018-07-17 09:24:25.076".indexOf(":")));
//        //System.out.println(parseDateTime("2018-07-17 09:24:25.076"));
//        System.out.println("-----------------------------分割线----------------------------");
//        System.out.println("2018-07-17 09:24:25.076".substring(0, "2018-07-17 09:24:25.076".lastIndexOf(":") + 3));
        System.out.println("20190104".substring(0, 4) + "-" + "20190104".substring(4, 6) + "-" + "20190104".substring(6, 8));
        //System.out.println(isDate1BeforeDate2("20190104" , "2019-01-05"));
        System.out.println(checkInputInYester12month("2018-07-04"));

    }
}
