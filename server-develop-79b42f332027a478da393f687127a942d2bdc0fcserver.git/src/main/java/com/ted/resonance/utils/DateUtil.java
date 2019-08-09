package com.ted.resonance.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class DateUtil {
    private final static SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private final static SimpleDateFormat noFormat = new SimpleDateFormat("yyyyMMddHHmmss");
    public static final String FULL_DATE_TIME = new String("yyyyMMddHHmmss");
    private final static DateTimeFormatter formatterYyMmDd = DateTimeFormatter.ofPattern("YYMMdd");
    private final static SimpleDateFormat formatString = new SimpleDateFormat("yyyy-MM-dd");


    private final static Logger logger = LoggerFactory.getLogger(DateUtil.class);


    public static Date getDate(int number) {
        long sysTime = System.currentTimeMillis() - number * 60 * 1000;
        Date date = new Date(sysTime);
        String dd = format.format(date);
        Date ddd = null;
        try {
            ddd = format.parse(dd);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return ddd;
    }

    public static String getTodayYYMMdd() {
        LocalDate today = LocalDate.now();
        ZoneId zoneId = ZoneId.systemDefault();
        ZonedDateTime todayZ = today.atStartOfDay(zoneId);
        return formatterYyMmDd.format(todayZ);

    }

    public static String stringToDate(String str) {
        long time = Long.parseLong(str);
        Date date = new Date(time);
        return format.format(date);
    }

    public static Long convertTimeToLong(String time) {
        Date date = null;
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            date = sdf.parse(time);
            return date.getTime();
        } catch (Exception e) {
            e.printStackTrace();
            return 0L;
        }
    }

    public static String dateToString(Date date) {
        return format.format(date);
    }

    public static String dateToFormatString(Date date) {
        return formatString.format(date);
    }

    public static String dateToNoFormatString(Date date) {
        return noFormat.format(date);
    }


    /**
     * 获取本周的第一天日期（按中国周）
     *
     * @param time
     * @return
     */
    public static Date getWeekStartDate(Date time) {

        try {
            Calendar cal = Calendar.getInstance();
            cal.setTime(time);
            int d = 0;
            if (cal.get(Calendar.DAY_OF_WEEK) == 1) {
                d = -6;
            } else {
                d = 2 - cal.get(Calendar.DAY_OF_WEEK);
            }
            cal.add(Calendar.DAY_OF_WEEK, d);
            cal.set(Calendar.HOUR_OF_DAY, 0);
            cal.set(Calendar.MINUTE, 0);
            cal.set(Calendar.SECOND, 0);

            //所在周开始日期
            return cal.getTime();
        } catch (Exception ex) {
            logger.error("getWeekStartDate exception={}", ex);

        }

        return null;

    }

    // 得到当前日期时间
    // 当前日期时间格式:yyyyMMddHHmmss
    public static String getFullDateAndTime() {
        SimpleDateFormat sdf = new SimpleDateFormat(DateUtil.FULL_DATE_TIME);
        return sdf.format(new Date());
    }

    /**
     * 获取指定月的第一天日期
     *
     * @param time
     * @return 第一天日期
     * @throws ParseException
     */
    public static Date getMonthStartDate(Date time) {
        try {

            Calendar cal = Calendar.getInstance();
            cal.setTime(time);
            cal.set(Calendar.DAY_OF_MONTH, 1);
            cal.set(Calendar.HOUR_OF_DAY, 0);
            cal.set(Calendar.MINUTE, 0);
            cal.set(Calendar.SECOND, 0);
            return cal.getTime();
        } catch (Exception ex) {
            logger.error("getMonthStartDate exception={}", ex);

        }

        return null;

    }


    /**
     * 获取指定年的第一天日期
     *
     * @param todayTime :"2018-08-15"
     * @return
     * @throws ParseException
     */
    public static Date getYearStartDate(Date todayTime) {
        try {
            Calendar c = Calendar.getInstance();
            c.setTime(todayTime);
            c.set(Calendar.DAY_OF_YEAR, 1);
            c.set(Calendar.HOUR_OF_DAY, 0);
            c.set(Calendar.MINUTE, 0);
            c.set(Calendar.SECOND, 0);
            return c.getTime();
        } catch (Exception ex) {
            logger.error("getYearStartDate exception={}", ex);
        }
        return null;

    }


    /**
     * 获取指定日期的第一天日期
     *
     * @param todayTime :"2018-08-15"
     * @return
     * @throws ParseException
     */
    public static Date getTodayStartDate(Date todayTime) {
        try {
            Calendar c = Calendar.getInstance();
            c.setTime(todayTime);
            c.set(Calendar.HOUR_OF_DAY, 0);
            c.set(Calendar.MINUTE, 0);
            c.set(Calendar.SECOND, 0);
            return c.getTime();
        } catch (Exception ex) {
            logger.error("getTodayStartDate exception={}", ex);
        }
        return null;

    }


    /**
     * 获取多少分钟以前的时间
     *
     * @param time
     * @return
     * @throws ParseException
     */
    public static Date getBeforeMinute(Date time, int minutes) {
        try {
            Calendar c = Calendar.getInstance();
            c.setTime(time);
            c.set(Calendar.MINUTE, c.get(Calendar.MINUTE) - minutes);
            return c.getTime();
        } catch (Exception ex) {
            logger.error("getBeforeMinute exception={}", ex);
        }
        return null;

    }


    /**
     * 获取多少小时后的时间
     *
     * @param time
     * @return
     * @throws ParseException
     */
    public static Date getAfterHour(Date time, int hour) {
        try {
            Calendar c = Calendar.getInstance();
            c.setTime(time);
            c.add(Calendar.HOUR_OF_DAY, hour);
            return c.getTime();
        } catch (Exception ex) {
            logger.error("getAfterHour exception={}", ex);
        }
        return null;

    }


    public static Date getBeforeDay(Date time, int days) {
        try {
            Calendar c = Calendar.getInstance();
            c.setTime(time);
            c.add(Calendar.DATE, -days);
            c.set(Calendar.HOUR_OF_DAY, 0);
            c.set(Calendar.MINUTE, 0);
            c.set(Calendar.SECOND, 0);
            c.set(Calendar.MILLISECOND, 0);
            return c.getTime();
        } catch (Exception ex) {
            logger.error("getBeforeDay exception={}", ex);
        }
        return null;

    }


    public static String getBeforeDayStr(Date time, int days) {
        try {
            Calendar c = Calendar.getInstance();
            c.setTime(time);
            c.add(Calendar.DATE, -days);
            c.set(Calendar.HOUR_OF_DAY, 0);
            c.set(Calendar.MINUTE, 0);
            c.set(Calendar.SECOND, 0);
            return formatString.format(c.getTime());
        } catch (Exception ex) {
            logger.error("getBeforeDayStr exception={}", ex);
        }
        return null;

    }


    //    /**
//     * 获取UTC时间
//     *
//     * @param time
//     * @return
//     * @throws ParseException
//     */
    public static Date getUtcTime(Date time) {
        try {
            Calendar c = Calendar.getInstance();
            c.setTimeZone(TimeZone.getTimeZone("UTC"));
            c.setTime(time);
            return c.getTime();
        } catch (Exception ex) {
            logger.error("getUtcTime exception={}", ex);
        }
        return null;

    }

    public static void main(String args[]) throws Exception {
//       Date time = DateUtil.getUtcTime(new Date());
//
//       System.out.println(time.getTime());

//
//        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//        Date time = sdf.parse("2019-03-23 00:01:13");
//
//        System.out.println(new Date().getTime());
//        System.out.println(getUtcTime(time).getTime());
        System.out.println("args = [" + getBeforeDay(new Date(), 0) + "]");;

    }

    /**
     * 获取UTC时间
     *
     * @param
     * @return
     * @throws ParseException
     */
//    public static Date getUtcTime(Date time) {
//        try {
//          return  DateUtils.addHours(time,-8);
//        } catch (Exception ex) {
//            logger.error("getUtcTime exception={}",ex);
//        }
//        return null;
//
//    }
    public static Date formatDate(String date) {
        try {
            return formatString.parse(date);
        } catch (Exception ex) {
            logger.error("formatDate exception ex={}", ex);
        }
        return null;
    }


    public static Date formatTimeStampDate(String date) {
        try {
            return format.parse(date);
        } catch (Exception ex) {
            logger.error("formatTimeStampDate exception ex={}", ex);
        }
        return null;
    }

    public static Date formatBeginTime(String date) {
        try {
            date += " 00:00:00";
            return format.parse(date);
        } catch (Exception ex) {
            logger.error("formatBeginTime exception ex={}", ex);
        }
        return null;
    }

    public static Date formatEndTime(String date) {
        try {
            date += " 23:59:59";
            return format.parse(date);
        } catch (Exception ex) {
            logger.error("formatEndTime exception ex={}", ex);
        }
        return null;
    }

    public static Date getDayEndTime(Date date) {
        String dateStr = formatString.format(date);
        try {
            dateStr += " 23:59:59";
            return format.parse(dateStr);
        } catch (Exception ex) {
            logger.error("getBeforeDayEndTime exception ex={}", ex);
        }
        return null;
    }

    /**
     *  获取有效时间
     * @param startDate
     * @param endDate
     * @param day
     * @return
     */
    public static Date getValidDate(Date startDate, Date endDate, int day) {
       try {
            long diff = startDate.getTime() - endDate.getTime();//这样得到的差值是毫秒级别
            long days = diff / (1000 * 60 * 60 * 24);

            long hours = (diff - days * (1000 * 60 * 60 * 24)) / (1000 * 60 * 60);
            long minutes = (diff - days * (1000 * 60 * 60 * 24) - hours * (1000 * 60 * 60)) / (1000 * 60);
            System.out.println("" + days + "天" + hours + "小时" + minutes + "分");
            if (days > day) {
                return endDate;
            } else {
                return startDate;
            }


        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     *  获取有效时间
     * @param startDate
     * @param endDate
     * @param day
     * @return
     */
    public static Boolean getValidDates(Date startDate, Date endDate, int day) {
        try {
            long diff = startDate.getTime() - endDate.getTime();//这样得到的差值是毫秒级别
            long days = diff / (1000 * 60 * 60 * 24);
            if (days > day) {
                return false;
            } else {
                return true;
            }


        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
}
