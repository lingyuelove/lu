package com.luxuryadmin.common.utils;

import com.luxuryadmin.common.exception.MyException;
import lombok.extern.slf4j.Slf4j;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 时间工具类
 *
 * @author monkey king
 * @date 2019-12-24 19:38:29
 */
@Slf4j
public class DateUtil {

    public static final String YYYY_MM_DD_HH_MM_SS = "yyyy-MM-dd HH:mm:ss";

    public static final String YYYY_MM_DD = "yyyy-MM-dd";

    public static final String YYYY_MM_DD_HH_MM = "yyyy-MM-dd HH:mm";
    public static final String MM_DD = "MM月dd日";

    public static final String HH_MM = "HH:mm";
    /**
     * 锁对象
     */
    private static final Object lockObj = new Object();

    /**
     * 存放不同的日期模板格式的sdf的Map
     */
    private static Map<String, ThreadLocal<SimpleDateFormat>> sdfMap = new HashMap<>();


    /**
     * 返回一个ThreadLocal的sdf,每个线程只会new一次sdf
     *
     * @param pattern
     * @return
     */
    private static SimpleDateFormat getSdf(final String pattern) {
        ThreadLocal<SimpleDateFormat> tl = sdfMap.get(pattern);

        // 此处的双重判断和同步是为了防止sdfMap这个单例被多次put重复的sdf
        if (tl == null) {
            synchronized (lockObj) {
                tl = sdfMap.get(pattern);
                if (tl == null) {
                    // 只有Map中还没有这个pattern的sdf才会生成新的sdf并放入map
                    System.out.println("put new sdf of pattern " + pattern + " to map");

                    // 这里是关键,使用ThreadLocal<SimpleDateFormat>替代原来直接new SimpleDateFormat
                    tl = new ThreadLocal<SimpleDateFormat>() {

                        @Override
                        protected SimpleDateFormat initialValue() {
                            System.out.println("thread: " + Thread.currentThread() + " init pattern: " + pattern);
                            return new SimpleDateFormat(pattern);
                        }
                    };
                    sdfMap.put(pattern, tl);
                }
            }
        }

        return tl.get();
    }

    /**
     * 使用ThreadLocal<SimpleDateFormat>来获取SimpleDateFormat,这样每个线程只会有一个SimpleDateFormat
     * 如果新的线程中没有SimpleDateFormat，才会new一个
     *
     * @param date
     * @param pattern
     * @return
     */
    public static String format(Date date, String pattern) {
        return getSdf(pattern).format(date);
    }

    /**
     * 获取日期0点
     * @param date

     * @return
     */
    public static String formatDaySt(Date date) {
        String dateNow = DateUtil.format(date,YYYY_MM_DD)+ " 00:00:00";
        return dateNow;
    }
    /**
     * 获取日期23点59分59秒
     * @param date

     * @return
     */
    public static String formatDayEnd(Date date) {
        String dateNow = DateUtil.format(date,YYYY_MM_DD)+ " 23:59:59";
        return dateNow;
    }
    /**
     * 默认: yyyy-MM-dd HH:mm:ss 格式;
     *
     * @param date
     * @return
     */
    public static String format(Date date) {
        return getSdf(YYYY_MM_DD_HH_MM_SS).format(date);
    }

    /**
     * 用于微信支付接口参数<br/>
     * YYYY-MM-DDTHH:mm:ss+TIMEZONE 格式;<br/>
     * 2021-03-21T22:03:34+08:00
     *
     * @param date
     * @return
     */
    public static String formatTimezone(Date date) {
        String createTime = DateUtil.format(date, "yyyy-MM-dd HH:mm:ss");
        return createTime.replaceAll(" ", "T") + "+08:00";
    }

    /**
     * 默认: yyyy-MM-dd格式;
     *
     * @param date
     * @return
     */
    public static String formatShort(Date date) {
        return getSdf(YYYY_MM_DD).format(date);
    }

    /**
     * 默认: MM-dd格式;
     *
     * @param date
     * @return
     */
    public static String formatMonthShort(Date date) {
        return getSdf(MM_DD).format(date);
    }

    public static Date parse(String dateStr, String pattern) throws ParseException {
        return getSdf(pattern).parse(dateStr);
    }

    /**
     * 默认: yyyy-MM-dd HH:mm:ss 格式;
     *
     * @param dateStr
     * @return
     * @throws ParseException
     */
    public static Date parse(String dateStr) throws ParseException {
        return getSdf(YYYY_MM_DD_HH_MM_SS).parse(dateStr);
    }

    /**
     * 默认: yyyy-MM-dd HH:mm:ss 格式;
     *
     * @param millis
     * @return
     */
    public static Date parse(long millis) {
        Calendar instance = Calendar.getInstance();
        instance.setTimeInMillis(millis);
        return instance.getTime();
    }

    /**
     * 默认: yyyy-MM-dd HH:mm 格式;
     *
     * @param dateStr
     * @return
     * @throws ParseException
     */
    public static Date parseHHMM(String dateStr) throws ParseException {
        return getSdf(YYYY_MM_DD_HH_MM).parse(dateStr);
    }

    /**
     * 用于微信支付接口参数<br/>
     * YYYY-MM-DDTHH:mm:ss+TIMEZONE 格式;<br/>
     * 2021-03-21T22:03:34+08:00
     *
     * @return
     */
    public static Date parseTimezone(String dateStr) throws ParseException {
        dateStr = dateStr.replaceAll("T", " ").replaceAll("\\+08:00", "");
        return parse(dateStr);
    }

    /**
     * 将年月日的短时间格式,默认加上时分秒; 00:00:00<br/>
     * 默认: yyyy-MM-dd HH:mm:ss 格式;
     *
     * @param dateStr
     * @return
     * @throws ParseException
     */
    public static Date parseDefaultSt(String dateStr) throws ParseException {
        return getSdf(YYYY_MM_DD_HH_MM_SS).parse(dateStr + " 00:00:00");
    }

    /**
     * 将年月日的短时间格式,默认加上时分秒; 23:59:59<br/>
     * 默认: yyyy-MM-dd HH:mm:ss 格式;
     *
     * @param dateStr
     * @return
     * @throws ParseException
     */
    public static Date parseDefaultEt(String dateStr) throws ParseException {
        return getSdf(YYYY_MM_DD_HH_MM_SS).parse(dateStr + " 23:59:59");
    }

    /**
     * 默认: yyyy-MM-dd格式;
     *
     * @param dateStr
     * @return
     * @throws ParseException
     */
    public static Date parseShort(String dateStr) throws ParseException {
        return getSdf(YYYY_MM_DD).parse(dateStr);
    }

    /**
     * 向一个老日期追加分钟,返回一个新日期;
     *
     * @param oldDate 被追加天数的日期;
     * @param minute  追加的分钟;
     * @return 返回一个日历对象, 追加天数后的日期;通过Calendar.getTime()可获得;
     */
    public static Calendar addMinuteFromOldDate(Date oldDate, int minute) {
        // 添加结束时间;
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(oldDate);
        calendar.add(Calendar.MINUTE, minute);
        return calendar;
    }

    /**
     * 向一个老日期追加小时,返回一个新日期;
     *
     * @param oldDate 被追加天数的日期;
     * @param hour    追加的小时;
     * @return 返回一个日历对象, 追加天数后的日期;通过Calendar.getTime()可获得;
     */
    public static Calendar addHourFromOldDate(Date oldDate, int hour) {
        // 添加结束时间;
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(oldDate);
        calendar.add(Calendar.HOUR_OF_DAY, hour);
        return calendar;
    }

    /**
     * 向一个老日期追加天数,返回一个新日期;
     *
     * @param oldDate 被追加天数的日期;
     * @param days    追加的天数;
     * @return 返回一个日历对象, 追加天数后的日期;通过Calendar.getTime()可获得;
     */
    public static Calendar addDaysFromOldDate(Date oldDate, int days) {
        // 添加结束时间;
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(oldDate);
        calendar.add(Calendar.DATE, days);
        return calendar;
    }

    /**
     * 向一个老日期追加月份,返回一个新日期;
     *
     * @param oldDate 被追加月份的日期;
     * @param months  追加的月份;
     * @return 返回一个日历对象, 追加月份后的日期;通过Calendar.getTime()可获得;
     */
    public static Calendar addMonthsFromOldDate(Date oldDate, int months) {
        // 添加结束时间;
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(oldDate);
        calendar.add(Calendar.MONTH, months);
        return calendar;
    }

    /**
     * 获取两个时间相差的天数,包含开始时间,包含结束时间;只取日期; <br>
     * <b>算法为: 天数 = (结算日-开始日)+1</b><br>
     * 如:2014-08-01 12:00:00 到 2014-08-01 12:00:01;也算一天;<br>
     * 2014-08-01 12:00:00 到 2014-08-02 23:59:59; 就算两天;<br>
     *
     * @param startTime
     * @param endTime
     * @return
     */
    public static int getDifferDays(Date startTime, Date endTime) {
        int days = 0;
        try {
            SimpleDateFormat fDate = new SimpleDateFormat(YYYY_MM_DD);
            startTime = fDate.parse(fDate.format(startTime));
            endTime = fDate.parse(fDate.format(endTime));
            while (endTime.compareTo(startTime) >= 0) {
                Calendar ca = Calendar.getInstance();
                ca.setTime(startTime);
                ca.add(Calendar.DATE, 1);
                days++;
                startTime = fDate.parse(fDate.format(ca.getTime()));
            }
        } catch (ParseException e) {
            log.error("获取时间相差天数异常: ", e);
        }
        return days;
    }

    /**
     * 校验端上的参数和格式化;为null的话, 默认为当天日期;
     *
     * @param startDateStr
     * @param endDateStr
     * @param defaultNowDate true:startDateStr或endDateStr为null时,默认为当天日期 | false:直接返回null
     * @return
     */
    public static Map<String, Date> formatQueryDate(String startDateStr, String endDateStr, boolean defaultNowDate) {
        Date startDate;
        Date endDate;
        try {
            Date nowDate = DateUtil.parseShort(DateUtil.format(new Date()));
            boolean nullStartDate = LocalUtils.isEmptyAndNull(startDateStr);
            boolean nullEndDateStr = LocalUtils.isEmptyAndNull(endDateStr);
            //默认查询当天数据
            startDate = nullStartDate ? (defaultNowDate ? nowDate : null) : DateUtil.parseShort(startDateStr);
            endDate = nullEndDateStr ? (defaultNowDate ? nowDate : null) : DateUtil.parseShort(endDateStr);
        } catch (ParseException e) {
            throw new MyException("时间格式错误!");
        }
        HashMap<String, Date> map = new HashMap<>(16);
        map.put("startDate", startDate);
        map.put("endDate", endDate);
        return map;
    }

    /**
     * 获取当月第一天
     *
     * @return
     */
    public static Date getFirstDayOfCurrentMonth() {
        // 获取当月第一天
        Date firstDay;
        // 获取前月的第一天
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.MONTH, 0);
        cal.set(Calendar.DAY_OF_MONTH, 1);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        firstDay = cal.getTime();

        return firstDay;
    }

    /**
     * 获取当月最后一天
     *
     * @return
     */
    public static Date getLastDayOfCurrentMonth() {
        // 获取当月最后一天
        Date lastDay;
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.MONTH, 1);
        cal.set(Calendar.DAY_OF_MONTH, 0);
        cal.set(Calendar.HOUR_OF_DAY, 23);
        cal.set(Calendar.MINUTE, 59);
        cal.set(Calendar.SECOND, 59);
        cal.set(Calendar.MILLISECOND, 999);
        lastDay = cal.getTime();

        return lastDay;
    }


    public static Integer getMonth(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int month = cal.get(Calendar.MONTH) + 1;
        return month;
    }

    public static Integer getYear(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int year = cal.get(Calendar.YEAR);
        return year;
    }

    /**
     * 计算两个时间之间间隔的天数
     *
     * @param timeEnd
     * @param timeStart
     * @return
     */
    public static int calIntervalDays(Date timeEnd, Date timeStart) {
        int intervalDays = (int) ((timeEnd.getTime() - timeStart.getTime()) / (1000 * 3600 * 24));
        return intervalDays;
    }

    /**
     * 获取指定时间所在天的第一秒，以"00:00:00"结尾
     *
     * @param date
     * @return
     */
    public static Date getStartTimeOfDay(Date date) throws ParseException {
        String dateStr = DateUtil.formatShort(date) + " 00:00:00";
        return DateUtil.parse(dateStr);
    }

    /**
     * 获取改时间的上个月的第一天和上个月的最后一天<br/>
     * 通过startDate和endDate这两个key来获取map的值
     *
     * @param date
     * @return
     */
    public static Map<String, Date> getLastMonthFirstDayAndEndDay(Date date) {

        Map<String, Date> map = new HashMap<>(16);

        if (null == date) {
            date = new Date();
        }
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.MONTH, -1);
        cal.set(Calendar.DAY_OF_MONTH, 1);
        //上个月的1号
        map.put("startDate", cal.getTime());
        cal.setTime(date);
        cal.set(Calendar.DAY_OF_MONTH, 0);
        //上个月的最后一天;
        map.put("endDate", cal.getTime());
        return map;
    }

    public static String getYyyyMmDd(String month) {
        Scanner input = new Scanner(System.in);
        int year = getYear(new Date());
        System.out.print("Enter month between 1 and 12: ");
        return year + "-" + month + "-" + "01";
    }

    /**
     * 获取当前时间的上个月的第一天和上个月的最后一天<br/>
     * default new Date();<br/>
     * 通过startDate和endDate这两个key来获取map的值
     *
     * @return
     */
    public static Map<String, Date> getLastMonthFirstDayAndEndDay() {
        return getLastMonthFirstDayAndEndDay(new Date());
    }

    /**
     * 获取今天0点
     *
     * @return
     */
    public static Date getTodayTime() {

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTime();
    }

    /**
     * 比较两个时间范围是否在合理的月份之内;<br/>
     * 比如,两个时间相差是否超过3个月
     *
     * @param month
     * @param st
     * @param et
     * @return
     */
    public static boolean isBetweenDate(int month, Date st, Date et) {
        try {
            //把时间转化成统一格式;
            st = DateUtil.parse(DateUtil.format(st));
            et = DateUtil.parse(DateUtil.format(et));
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(st);
            calendar.add(Calendar.MONTH, month);
            Date newDate = calendar.getTime();
            return newDate.after(et);
        } catch (ParseException e) {
            log.error(e.getMessage(), e);
        }

        return false;
    }

    public static void main(String[] args) {
        try {
            Date date = parse("2021-01-01", YYYY_MM_DD);
            Date date2 = parse("2021-12-31", YYYY_MM_DD);

            System.out.println("new1: " + getDifferDays(date, date2));
            System.out.println("new2: " + calIntervalDays(date2, date));
            Date da = getLastDayOfCurrentMonth();
            Boolean result = DateUtil.getTodayTime().equals(getTodayTime());
            System.out.println(result);
            //System.out.println(format(date, "yyyy-MM-dd HH:mm:ss"));
            System.out.println(parseTimezone("2021-03-21T22:03:34+08:00"));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        System.out.println("dddd"+formatDayEnd(new Date()));
    }

}