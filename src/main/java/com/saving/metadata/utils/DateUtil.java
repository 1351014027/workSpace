package com.saving.metadata.utils;

import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author Saving
 */
@Slf4j
public class DateUtil {
    public static final String YYYYMMDDHH = "yyyyMMddHH";
    private static final String ERRORMSG = "时间工具类调用异常";
    private static final String YYYY = "yyyy";
    private static final String MM = "MM";
    private static final String DD = "dd";
    private static final String YYYY_MM_DD = "yyyy-MM-dd";
    private static final String YYYY_MM = "yyyy-MM";
    private static final String HH_MM_SS = "HH:mm:ss";
    private static final String YYYY_MM_DD_HH_MM_SS = "yyyy-MM-dd HH:mm:ss";
    private static final int TWO = 2;
    private static final int THERE = 3;
    private static final int FOUR = 4;
    private static final int FIVE = 5;
    private static final int SIX = 6;
    private static final int SEVEN = 7;
    private static final int EIGHT = 8;
    private static final int NINE = 9;
    private static final int TEN = 10;
    private static final int ELEVEN = 11;
    private static final int TWELVE = 12;
    private static final int DAY_MILLISECOND = 86400000;


    private DateUtil() {

    }

    /**
     * 日期格式化－将<code>Date</code>类型的日期格式化为<code>String</code>型
     *
     * @param date    日期
     * @param pattern 格式化字串
     * @return 返回类型 String 日期字符串
     */
    public static String format(Date date, String pattern) {
        if (date == null) {
            return "";
        } else {
            return getFormatter(pattern).format(date);
        }
    }

    public static Object getLastMilliSecond(Object date) throws NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException {
        Calendar cal = Calendar.getInstance();
        cal.setTime((Date) date);
        cal.add(Calendar.DATE, Calendar.YEAR);
        cal.add(Calendar.MILLISECOND, -1);
        Constructor constructor = date.getClass().getConstructor(Long.TYPE.getClass());
        return constructor.newInstance(cal.getTimeInMillis());
    }

    /**
     * 默认把日期格式化成yyyy-MM-dd格式
     *
     * @param date 被格式化的时间
     * @return 返回类型 String 日期字符串
     */
    public static String format(Date date) {
        if (date == null) {
            return "";
        } else {
            return getFormatter(YYYY_MM_DD).format(date);
        }
    }

    public static String getYyyyMmDd() {
        return DateUtil.format(Calendar.getInstance().getTime(), "YYYYMMdd");
    }

    /**
     * 日期解析－将<code>String<	类型的日期解析为<code>Date</code>型
     *
     * @param strDate 日期字串
     * @param pattern 格式化字串
     * @return 返回类型 Date 一个被格式化了的<code>Date</code>日期
     * @throws ParseException error
     */
    public static Date parse(String strDate, String pattern) throws ParseException {
        try {
            return getFormatter(pattern).parse(strDate);
        } catch (ParseException pe) {
            throw new ParseException("Method parse in Class DateUtil  err: parse strDate fail.", pe.getErrorOffset());
        }
    }

    /**
     * 获取当前日期
     *
     * @return 返回类型 Date 返回类型 一个包含年月日的<code>Date</code>型日期
     */
    private static synchronized Date getCurrDate() {
        Calendar calendar = Calendar.getInstance();
        return calendar.getTime();
    }

    /**
     * 将日期类型转换为simpleDateFormat类型
     *
     * @param parttern 要转换的日期类型
     * @return 返回类型 SimpleDateFormat 返回一个SimpleDateFormat类型的日期字符串
     */
    private static SimpleDateFormat getFormatter(String parttern) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(parttern);
        simpleDateFormat.setTimeZone(TimeZone.getTimeZone("Asia/Shanghai"));
        return simpleDateFormat;
    }


    /**
     * 获取当前时间
     *
     * @return 返回类型 String 一个包含年月日时分秒的<code>String</code>型日期。hh:mm:ss
     */
    public static String getCurrTimeStr() {
        return format(getCurrDate(), HH_MM_SS);
    }

    /**
     * 获取当前完整时间
     *
     * @return 返回类型 String 一个包含年月日时分秒的<code>String</code>型日期。yyyy-MM-dd hh:mm:ss
     */
    public static String getCurrDateTimeStr() {
        return format(getCurrDate(), YYYY_MM_DD_HH_MM_SS);
    }

    /**
     * 获取当前年份样式
     *
     * @return 返回类型 String 当前年分 yyyy
     */
    public static String getYear() {
        return format(getCurrDate(), YYYY);
    }

    /**
     * 获取当前季度的第一个月
     *
     * @param quarter 第几季度:如 1,2,3,4
     * @return 返回类型 String 当前季度的第一个月数
     */
    public static String getFirstMonthOfQuarter(String quarter) {

        int qu = Integer.parseInt(quarter);
        String month = "";
        if (qu == 1) {
            month = "01";
        } else if (qu == TWO) {
            month = "04";
        } else if (qu == THERE) {
            month = "07";
        } else if (qu == FOUR) {
            month = "10";
        }
        return month;
    }

    /**
     * 获取当前季度的最后一个月
     *
     * @param quarter 第几季度 如 1,2,3,4
     * @return 返回类型 String 当前季度的最后一个月
     */
    public static String getLastMonthOfQuarter(String quarter) {
        int qu = Integer.parseInt(quarter);
        String month = "";
        if (qu == 1) {
            month = "03";
        } else if (qu == TWO) {
            month = "06";
        } else if (qu == THERE) {
            month = "09";
        } else if (qu == FOUR) {
            month = "12";
        }
        return month;
    }

    /**
     * 获取当前季度
     *
     * @return 返回类型 String 当前季度 如1,2,3,4
     */
    public static String getQuarter() {
        String quar = "";
        if (Integer.parseInt(getMonth()) == 1 || Integer.parseInt(getMonth()) == TWO || Integer.parseInt(getMonth()) == THERE) {
            quar = "1";
        } else if (Integer.parseInt(getMonth()) == FOUR || Integer.parseInt(getMonth()) == FIVE
                || Integer.parseInt(getMonth()) == SIX) {
            quar = "2";
        } else if (Integer.parseInt(getMonth()) == SEVEN || Integer.parseInt(getMonth()) == EIGHT
                || Integer.parseInt(getMonth()) == NINE) {
            quar = "3";
        } else if (Integer.parseInt(getMonth()) == TEN || Integer.parseInt(getMonth()) == ELEVEN
                || Integer.parseInt(getMonth()) == TWELVE) {
            quar = "4";
        }
        return quar;
    }

    /**
     * 获取当前月
     *
     * @return 返回类型 String 获取当前月
     */
    private static String getMonth() {
        return format(getCurrDate(), MM);
    }


    /**
     * 根据输入的年月周数来取该周第一天
     *
     * @param year  年份(> 0)
     * @param month 月份(1-12)
     * @param week  当月周数(1-5)
     * @return 返回类型 String 该周第一天（周日）
     */
    public static String getFirstDayByMonthWeek(int year, int month, int week) {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd ");
        Calendar c = Calendar.getInstance();
        c.set(Calendar.YEAR, year);
        c.set(Calendar.MONTH, month - 1);
        c.set(Calendar.WEEK_OF_MONTH, week);

        int firstDayofweek = c.getFirstDayOfWeek();

        Calendar c1 = Calendar.getInstance();
        c1.set(Calendar.YEAR, year);
        c1.set(Calendar.MONTH, month - 1);
        c1.set(Calendar.WEEK_OF_MONTH, week);
        c1.set(Calendar.DAY_OF_WEEK, firstDayofweek);
        Date d1 = new Date(c1.getTimeInMillis());
        return df.format(d1);
    }

    /**
     * 根据输入的年月周数来取该周最后一天
     *
     * @param year  年份(> 0)
     * @param month 月份(1-12)
     * @param week  当月周数(1-5)
     * @return 返回类型 String 该周最后一天（周六）
     */
    public static String getLastDayByMonthWeek(int year, int month, int week) {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd ");
        Calendar c = Calendar.getInstance();
        c.set(Calendar.YEAR, year);
        c.set(Calendar.MONTH, month - 1);
        c.set(Calendar.WEEK_OF_MONTH, week);

        int firstDayofweek = c.getFirstDayOfWeek();
        int lastDayofweek = firstDayofweek + 6;

        Calendar c2 = Calendar.getInstance();
        c2.set(Calendar.YEAR, year);
        c2.set(Calendar.MONTH, month - 1);
        c2.set(Calendar.WEEK_OF_MONTH, week);
        c2.set(Calendar.DAY_OF_WEEK, lastDayofweek);
        Date d2 = new Date(c2.getTimeInMillis());
        return df.format(d2);
    }

    /**
     * 计算两个日期之间的月差
     *
     * @param strDate1 日期字串1
     * @param strDate2 日期字串2
     * @return 返回类型 int 两个日期之间的月差
     */
    public static int getIntevalMonth(String strDate1, String strDate2) {
        int iMonth = 0;
        int flag = 0;
        try {
            Calendar objCalendarDate1 = Calendar.getInstance();
            objCalendarDate1.setTime(DateFormat.getDateInstance().parse(strDate1));

            Calendar objCalendarDate2 = Calendar.getInstance();
            objCalendarDate2.setTime(DateFormat.getDateInstance().parse(strDate2));

            if (objCalendarDate2.equals(objCalendarDate1)) {
                return 0;
            }
            if (objCalendarDate1.after(objCalendarDate2)) {
                Calendar temp = objCalendarDate1;
                objCalendarDate1 = objCalendarDate2;
                objCalendarDate2 = temp;
            }
            if (objCalendarDate2.get(Calendar.DAY_OF_MONTH) < objCalendarDate1.get(Calendar.DAY_OF_MONTH)) {
                flag = 1;
            }
            if (objCalendarDate2.get(Calendar.YEAR) > objCalendarDate1.get(Calendar.YEAR)) {
                iMonth = ((objCalendarDate2.get(Calendar.YEAR) - objCalendarDate1.get(Calendar.YEAR)) * 12
                        + objCalendarDate2.get(Calendar.MONTH) - flag)
                        - objCalendarDate1.get(Calendar.MONTH);
            } else {
                iMonth = objCalendarDate2.get(Calendar.MONTH) - objCalendarDate1.get(Calendar.MONTH) - flag;
            }
        } catch (Exception e) {
            log.error(ERRORMSG, e);
        }
        return iMonth;
    }

    /**
     * 获取当前星期
     *
     * @return 返回类型 String 当前星期几(一、二、三、四等)
     */
    public static String getDayOfWeek() {
        String s = "";
        Calendar aCalendar = Calendar.getInstance();
        int x = aCalendar.get(Calendar.DAY_OF_WEEK);
        String a = Integer.toString(x - 1);
        if (String.valueOf(1).equals(a)) {
            s = "一";
        } else if (String.valueOf(TWO).equals(a)) {
            s = "二";
        } else if (String.valueOf(THERE).equals(a)) {
            s = "三";
        } else if (String.valueOf(FOUR).equals(a)) {
            s = "四";
        } else if (String.valueOf(FIVE).equals(a)) {
            s = "五";
        } else if (String.valueOf(SIX).equals(a)) {
            s = "六";
        } else if (String.valueOf(SEVEN).equals(a)) {
            s = "日";
        }
        return s;
    }


    /**
     * 取得系统当前时间前n个月的相对应的一天
     *
     * @param n int
     * @return String yyyy-mm-dd
     */
    public static String getNMonthBeforeCurrentDay(int n) {
        Calendar c = Calendar.getInstance();
        c.add(Calendar.MONTH, -n);
        return " " + c.get(Calendar.YEAR) + "- " + (c.get(Calendar.MONTH) + 1) + "- " + c.get(Calendar.DATE);

    }

    /**
     * 取得系统当前时间后n个月的相对应的一天
     *
     * @param n int
     * @return String yyyy-mm-dd
     */
    public static String getNMonthAfterCurrentDay(int n) {
        Calendar c = Calendar.getInstance();
        c.add(Calendar.MONTH, n);
        return " " + c.get(Calendar.YEAR) + "- " + (c.get(Calendar.MONTH) + 1) + "- " + c.get(Calendar.DATE);

    }

    /**
     * 取得系统当前时间前n天
     *
     * @param n int
     * @return String yyyy-mm-dd
     */
    public static String getNDayBeforeCurrentDate(String dateStr, String dateFormat, int n) {
        try {
            Calendar c = Calendar.getInstance();
            Date date = parse(dateStr, dateFormat);
            c.setTime(date);
            c.add(Calendar.DAY_OF_MONTH, -n);
            return c.get(Calendar.YEAR) + "-" + (c.get(Calendar.MONTH) + 1) + "-" + c.get(Calendar.DATE);
        } catch (ParseException e) {
            log.error(ERRORMSG, e);
            return "";
        }
    }

    /**
     * 取得系统当前时间后n天
     *
     * @param n int
     * @return String yyyy-mm-dd
     */
    public static String getNDayAfterCurrentDate(String dateStr, String dateFormat, int n) {
        try {
            Calendar c = Calendar.getInstance();
            Date date = parse(dateStr, dateFormat);
            c.setTime(date);
            c.add(Calendar.DAY_OF_MONTH, n);
            return c.get(Calendar.YEAR) + "-" + (c.get(Calendar.MONTH) + 1) + "-" + c.get(Calendar.DATE);
        } catch (ParseException e) {
            log.error(ERRORMSG, e);
            return "";
        }
    }

    /**
     * 获取当前日期号样式：dd
     *
     * @return 返回类型 String 当前日期号
     */
    public static String getDay() {
        return format(getCurrDate(), DD);
    }

    /**
     * 按给定日期样式判断给定字符串是否为合法日期数据
     *
     * @param strDate 要判断的日期
     * @param pattern 日期样式
     * @return 返回类型 boolean 如果是 返回true,否则返回false
     */
    public static boolean isDate(String strDate, String pattern) {
        try {
            parse(strDate, pattern);
            return true;
        } catch (ParseException pe) {
            return false;
        }
    }

    /**
     * 判断给定字符串是否为特定格式年份（格式：yyyy）数据
     *
     * @param strDate 要判断的日期
     * @return 返回类型 boolean 如果是返回true，否则返回false
     */
    public static boolean isYYYY(String strDate) {
        try {
            parse(strDate, YYYY);
            return true;
        } catch (ParseException pe) {
            return false;
        }
    }

    /**
     * 判断给定字符串是否为特定格式年月（格式：yyyy-MM）数据
     *
     * @param strDate 要判断的日期字串
     * @return 返回类型 boolean 如果是返回true，否则返回false
     */
    public static boolean isYearMonth(String strDate) {
        try {
            parse(strDate, YYYY_MM);
            return true;
        } catch (ParseException pe) {
            return false;
        }
    }

    /**
     * 判断给定字符串是否为特定格式的年月日（格式：yyyy-MM-dd）数据
     *
     * @param strDate 要判断的日期字串
     * @return 返回类型 boolean 如果是返回true，否则返回false
     */
    public static boolean isYearMonthDay(String strDate) {
        try {
            parse(strDate, YYYY_MM_DD);
            return true;
        } catch (ParseException pe) {
            return false;
        }
    }

    /**
     * 判断给定字符串是否为特定格式年月日时分秒（格式：yyyy-MM-dd HH:mm:ss）数据
     *
     * @param strDate 要判断的日期
     * @return 返回类型 boolean 如果是返回true，否则返回false
     */
    public static boolean isYearMonthDayTimeMinuteSecond(String strDate) {
        try {
            parse(strDate, YYYY_MM_DD_HH_MM_SS);
            return true;
        } catch (ParseException pe) {
            return false;
        }
    }

    /**
     * 判断给定字符串是否为特定格式时分秒（格式：HH:mm:ss）数据
     *
     * @param strDate 要判断的日期
     * @return 返回类型 boolean 如果是返回true，否则返回false
     */
    public static boolean isTimeMinuteSecond(String strDate) {
        try {
            parse(strDate, HH_MM_SS);
            return true;
        } catch (ParseException pe) {
            return false;
        }
    }

    /**
     * 获取给定日期字串的后intevalDay天的日期
     *
     * @param refenceDate 给定日期（格式为：yyyy-MM-dd）
     * @param intevalDays 间隔天数
     * @return 返回类型 String 计算后的日期
     */
    public static String getNextDate(String refenceDate, int intevalDays) {
        try {
            return getNextDate(parse(refenceDate, YYYY_MM_DD), intevalDays);
        } catch (Exception ee) {
            return "";
        }
    }

    public static Date getNextDay(Date refenceDate, int intevalDays) {
        Date nd = null;
        try {
            nd = DateUtil.parse(DateUtil.getNextDate(refenceDate, intevalDays), DateUtil.YYYY_MM_DD);
        } catch (ParseException e) {
            log.error(ERRORMSG, e);
        }

        return nd;
    }

    /**
     * 获取给定日期的后intevalDay天的日期
     *
     * @param refenceDate 给定日期
     * @param intevalDays 间隔天数
     * @return 返回类型 String 计算后的日期
     */
    private static String getNextDate(Date refenceDate, int intevalDays) {
        try {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(refenceDate);
            calendar.set(Calendar.DATE, calendar.get(Calendar.DATE) + intevalDays);
            return format(calendar.getTime(), YYYY_MM_DD);
        } catch (Exception ee) {
            return "";
        }
    }


    /**
     * 把指定日期时间转换为字符串
     *
     * @param datetime 指定日期时间
     * @return 返回类型 String 日期时间字符串
     */
    public static String dateTimeToString(Date datetime) {
        GregorianCalendar calendar = new GregorianCalendar();
        calendar.setTime(datetime);
        return calendar.get(Calendar.YEAR) + "" + (calendar.get(Calendar.MONTH) + 1 > 9 ? "" : "0")
                + (calendar.get(Calendar.MONTH) + 1) + "" + (calendar.get(Calendar.DATE) > 9 ? "" : "0")
                + calendar.get(Calendar.DATE) + "" + (calendar.get(Calendar.HOUR_OF_DAY) > 9 ? "" : "0")
                + calendar.get(Calendar.HOUR_OF_DAY) + "" + (calendar.get(Calendar.MINUTE) > 9 ? "" : "0")
                + calendar.get(Calendar.MINUTE) + "" + (calendar.get(Calendar.SECOND) > 9 ? "" : "0")
                + calendar.get(Calendar.SECOND);
    }

    /**
     * 根据指定年,月,日得到一周的第几天
     *
     * @param year  指定年
     * @param month 指定月
     * @param day   指定日
     * @return 返回类型 int 得到一周的第几天
     */
    public static int getDayOfWeek(int year, int month, int day) {
        Calendar cal = Calendar.getInstance();
        cal.clear();
        cal.set(year, month - Calendar.YEAR, day);
        int dayofWeek = cal.get(Calendar.DAY_OF_WEEK);
        switch (dayofWeek) {
            case 1:
                dayofWeek = 7;
                break;
            case 2:
                dayofWeek = 1;
                break;
            case 3:
                dayofWeek = 2;
                break;
            case 4:
                dayofWeek = 3;
                break;
            case 5:
                dayofWeek = 4;
                break;
            case 6:
                dayofWeek = 5;
                break;
            case 7:
                dayofWeek = 6;
                break;
            default:

        }

        return dayofWeek;
    }

    /**
     * 根据指定年,月,日得到一年的第几周
     *
     * @param year  指定年
     * @param month 指定月
     * @param day   指定日
     * @return 返回类型 int 得到一年的第几周
     */
    public static int getWeekOfYear(int year, int month, int day) {
        Calendar cal = Calendar.getInstance();
        cal.clear();
        cal.set(year, month - Calendar.YEAR, day);
        return cal.get(Calendar.WEEK_OF_YEAR);
    }

    /**
     * 根据指定年,月,日得到一月的第几天
     *
     * @param year  指定年
     * @param month 指定月
     * @param day   指定日
     * @return 返回类型 int 得到一月的第几天
     */
    public static int getDayOfMonth(int year, int month, int day) {
        Calendar cal = Calendar.getInstance();
        cal.clear();
        cal.set(year, month - Calendar.YEAR, day);
        return cal.get(Calendar.DAY_OF_MONTH);
    }

    /**
     * 根据指定年,得到上一月
     *
     * @param month 指定月
     * @return 返回类型 String 返回上月是第几月
     */
    public static String getFrontMonth(String month) {
        int mon = Integer.parseInt(month);
        String frontmonth;
        if (mon == 1) {
            frontmonth = "12";
        } else if (mon < ELEVEN) {
            frontmonth = "0" + (mon - 1);
        } else {
            frontmonth = String.valueOf(mon - 1);
        }
        return frontmonth;
    }

    /**
     * 根据指定年,月得到下一月
     *
     * @param month 指定月
     * @return 返回类型 String 返回下月是第几月
     */
    public static String getNextMonth(String month) {
        int year = Integer.parseInt(month.substring(0, 4));
        int mon = Integer.parseInt(month.substring(4, 6));
        String nextmonth;
        String nextyear = String.valueOf(year);
        if (mon == TWELVE) {
            nextmonth = "01";
            nextyear = String.valueOf(year + 1);
        } else if (mon < NINE) {
            nextmonth = "0" + (mon + 1);
        } else {
            nextmonth = String.valueOf(mon + 1);
        }
        return nextyear + nextmonth;
    }

    /**
     * 根据指定年月字符串得到下个季度的月份
     *
     * @param yearAndMonth 指定年月字符 如"200910"
     * @return 返回类型 String 下个季度的月份 如"201001"
     */
    public static String getMonthOfNextQuarter(String yearAndMonth) {
        int year = Integer.parseInt(yearAndMonth.substring(0, 4));
        int mon = Integer.parseInt(yearAndMonth.substring(4, 6));
        String nextmonth;
        String nextyear = String.valueOf(year);
        if (mon == TEN) {
            nextmonth = "01";
            nextyear = String.valueOf(year + 1);
        } else if (mon == ELEVEN) {
            nextmonth = "02";
            nextyear = String.valueOf(year + 1);
        } else if (mon == TWELVE) {
            nextmonth = "03";
            nextyear = String.valueOf(year + 1);
        } else if (mon < SEVEN) {
            nextmonth = "0" + (mon + 3);
        } else {
            nextmonth = String.valueOf(mon + 3);
        }
        return nextyear + nextmonth;
    }

    /**
     * 求当前日期和指定字符串日期的相差小时数
     *
     * @param date 指定字符串日期
     * @return 返回类型 long 当前日期和指定字符串日期的相差小时数
     */

    public static long getIntevalHours(String date) {
        try {
            Date currentDate = new Date();

            SimpleDateFormat myFormatter = new SimpleDateFormat(YYYY_MM_DD_HH_MM_SS);
            Date theDate = myFormatter.parse(date);
            return (currentDate.getTime() - theDate.getTime()) / (60 * 60 * 1000);
        } catch (Exception ee) {
            return 0L;
        }
    }

    /**
     * 求两日期对象间的小时数间隔
     *
     * @param startDate 起始日期
     * @param endDate   结束日期
     * @return 返回类型 long 两日期对象间的小时数间隔
     */
    public static long getIntevalHours(Date startDate, Date endDate) {
        try {
            long hours;
            if (endDate == null) {
                hours = ((new Date()).getTime() - startDate.getTime()) / (60 * 60 * 1000);
            } else {
                hours = (endDate.getTime() - startDate.getTime()) / (60 * 60 * 1000);
            }

            return hours;
        } catch (Exception ee) {
            return 0;
        }
    }

    /**
     * 将指定日期格式化成"yyyy年M月d日"的形式，如将2000-01-01转换为2000年1月1日
     *
     * @param date 指定日期
     * @return 返回类型 String 格式化后日期字串
     */
    public static String getChineseDate(Date date) {
        DateFormat df = DateFormat.getDateInstance(DateFormat.LONG, Locale.CHINESE);
        return df.format(date);
    }

    /**
     * 将日期格式化成"yyyy-MM-dd HH:mm:ss"的形式，如"2000-12-3 12:53:48"
     *
     * @param date 指定日期
     * @return 返回类型 String 格式化后日期字串
     */
    public static String getLongFormatTime(Date date) {
        DateFormat df = new SimpleDateFormat(YYYY_MM_DD_HH_MM_SS);
        return df.format(date);
    }

    /**
     * 将指定日期格式化成"yyyy-MM-dd"的形式，如"2000-12-3"
     *
     * @param date 指定日期
     * @return 返回类型 String 格式化后的时间字串
     */
    public static String getyyyyMMddDateStr(Date date) {
        DateFormat df = new SimpleDateFormat(YYYY_MM_DD);
        return df.format(date);
    }


    /**
     * 当返回值为true时表示指定时间与当前时间之差在24小时内，若为false则表示不在24小时之内
     *
     * @param time 指定时间串 如:"12000293293"
     * @return 返回类型 boolean 指定时间与当前时间之差在24小时内，若为false则表示不在24小时之内
     */
    public static boolean dateCompare(String time) {
        Date date = new Date();

        Long dateLongValue = date.getTime();
        Long timeLongVlaue = 0L;
        if (time != null && !"".equals(time)) {

            timeLongVlaue = Long.parseLong(time);
        }

        return (dateLongValue - timeLongVlaue) <= DAY_MILLISECOND;
    }

    /**
     * 当返回值为true时表示指定时间与当前时间之差在同一月，若为false则表示不在同一月
     *
     * @param time 指定时间串 如:"12000293293"
     * @return 返回类型 boolean 指定时间与当前时间之差在同一月，若为false则表示不在同一月
     */
    public static boolean compare(String time) {
        Date currentDate = new Date();
        int currentDay;
        int compareDay;

        Long timeLongVlaue = 0L;
        if (time != null && !"".equals(time)) {
            timeLongVlaue = Long.parseLong(time);
        }
        Date compareDate = new Date(timeLongVlaue);
        Calendar compareCalendar = Calendar.getInstance();
        Calendar currentCalendar = Calendar.getInstance();
        currentCalendar.setTime(currentDate);
        compareCalendar.setTime(compareDate);
        currentDay = currentCalendar.get(Calendar.DAY_OF_MONTH);
        compareDay = compareCalendar.get(Calendar.DAY_OF_MONTH);
        int temp = currentDay - compareDay;
        return temp == 1 || temp == 0;
    }

    /**
     * 求当前日期和指定字符串日期的相差天数
     *
     * @param startDate 开始时间
     * @return 长整型
     */
    public static long getIntevalHours(Timestamp startDate, Timestamp endDate) {
        try {
            long hours;
            if (endDate == null) {
                hours = ((new Date()).getTime() - startDate.getTime()) / (60 * 60 * 1000);
            } else {
                hours = (endDate.getTime() - startDate.getTime()) / (60 * 60 * 1000);
            }

            return hours;
        } catch (Exception e) {
            log.error(ERRORMSG, e);
            return 0;
        }
    }

    /**
     * 求两个日期相差的分钟数
     *
     * @param startDate 开始时间
     * @return 长整型
     */
    public static long getIntevalMinutes(String startDate, String endDate) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(YYYY_MM_DD_HH_MM_SS);

            Date d1 = sdf.parse(startDate);
            Date d2 = sdf.parse(endDate);
            return (d2.getTime() - d1.getTime()) / 1000 / 60;
        } catch (Exception e) {
            log.error(ERRORMSG, e);
            return 0;
        }
    }

    /**
     * 得到连个日期间的月份差
     *
     * @param fromDate 起始日期
     * @param toDate   结束日期
     * @return 整数
     */
    public static int getMonthsBetweenTwoDate(Date fromDate, Date toDate) {
        int iMonth = 0;
        int flag = 0;
        try {
            Calendar objCalendarDate1 = Calendar.getInstance();
            objCalendarDate1.setTime(fromDate);

            Calendar objCalendarDate2 = Calendar.getInstance();
            objCalendarDate2.setTime(toDate);

            if (objCalendarDate2.equals(objCalendarDate1)) {
                return 0;
            }
            if (objCalendarDate1.after(objCalendarDate2)) {
                Calendar temp = objCalendarDate1;
                objCalendarDate1 = objCalendarDate2;
                objCalendarDate2 = temp;
            }
            if (objCalendarDate2.get(Calendar.DAY_OF_MONTH) < objCalendarDate1.get(Calendar.DAY_OF_MONTH)) {
                flag = 1;
            }
            if (objCalendarDate2.get(Calendar.YEAR) > objCalendarDate1.get(Calendar.YEAR)) {
                iMonth = ((objCalendarDate2.get(Calendar.YEAR) - objCalendarDate1.get(Calendar.YEAR)) * 12
                        + objCalendarDate2.get(Calendar.MONTH) - flag)
                        - objCalendarDate1.get(Calendar.MONTH);
            } else {
                iMonth = objCalendarDate2.get(Calendar.MONTH) - objCalendarDate1.get(Calendar.MONTH) - flag;
            }
        } catch (Exception e) {
            log.error(ERRORMSG, e);
        }
        return iMonth;
    }


    /**
     * @return 返回类型
     * 获取上个月的第一天
     */
    public static String getLastMonthFirstDay() {
        // 获取上个月 第一天
        SimpleDateFormat sdf = new SimpleDateFormat(YYYY_MM_DD, Locale.CHINA);
        Calendar c = Calendar.getInstance();
        c.add(Calendar.MONTH, -1);
        c.set(Calendar.DAY_OF_MONTH, 1);
        return sdf.format(c.getTime());

    }

    /**
     * @return 获取当前日历的上一个月的最后一天
     */
    public static String getLastMonthLastDay() {
        SimpleDateFormat sdf = new SimpleDateFormat(YYYY_MM_DD, Locale.CHINA);
        Calendar c = Calendar.getInstance();
        c.set(Calendar.DAY_OF_MONTH, 0);
        return sdf.format(c.getTime());
    }

}