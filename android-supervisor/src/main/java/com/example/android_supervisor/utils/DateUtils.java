package com.example.android_supervisor.utils;

import android.annotation.SuppressLint;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * @author wujie
 */
public class DateUtils {
    @SuppressLint("SimpleDateFormat")
    private static final DateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @SuppressLint("SimpleDateFormat")
    private static final DateFormat DATE_FORMAT_ONLY_DATE = new SimpleDateFormat("yyyy-MM-dd");

    @SuppressLint("SimpleDateFormat")
    private static final DateFormat DATE_FORMAT_ONLY_TIME = new SimpleDateFormat("HH:mm:ss");

    @SuppressLint("SimpleDateFormat")
    private static final DateFormat DATE_FORMAT2 = new SimpleDateFormat("yyyy-MM-dd HH:mm");


    @SuppressLint("SimpleDateFormat")
    private static final DateFormat DATE_YMD = new SimpleDateFormat("yyyyMMdd");


    public static String format(Date date) {
        return format(date, 0);
    }

    public static String format(Date date, int type) {
        String str = null;
        if (date != null) {
            switch (type) {
                case 0:
                    str = DATE_FORMAT.format(date);
                    break;
                case 1:
                    str = DATE_FORMAT_ONLY_DATE.format(date);
                    break;
                case 2:
                    str = DATE_FORMAT_ONLY_TIME.format(date);
                    break;
                case 3:
                    str = DATE_FORMAT2.format(date);
                case 4:
                    str = DATE_YMD.format(date);
            }
        }
        return str;
    }

    public static Date parse(String source) {
        return parse(source, 0);
    }

    public static Date parse(String source, int type) {
        Date date = null;
        try {
            switch (type) {
                case 0:
                    date = DATE_FORMAT.parse(source);
                    break;
                case 1:
                    date = DATE_FORMAT_ONLY_DATE.parse(source);
                    break;
                case 2:
                    date = DATE_FORMAT_ONLY_TIME.parse(source);
                    break;
                case 3:
                    date = DATE_FORMAT2.parse(source);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    @SuppressLint("SimpleDateFormat")
    private static final SimpleDateFormat DATE_TIME_FORMAT_FULL = new SimpleDateFormat("yyyy-MM-dd HH:mm");

    @SuppressLint("SimpleDateFormat")
    private static final SimpleDateFormat DATE_TIME_FORMAT = new SimpleDateFormat("MM-dd HH:mm");

    @SuppressLint("SimpleDateFormat")
    private static final SimpleDateFormat TIME_FORMAT = new SimpleDateFormat("HH:mm");

    public static String smartFormat(Date date) {
        Calendar now = Calendar.getInstance();
        now.setTime(new Date());

        Calendar cal = Calendar.getInstance();

        cal.setTime(date);

        if (now.get(Calendar.YEAR) - cal.get(Calendar.YEAR) != 0) {
            return DATE_TIME_FORMAT_FULL.format(date);
        }

        if (now.get(Calendar.DAY_OF_YEAR) - cal.get(Calendar.DAY_OF_YEAR) != 0) {
            return DATE_TIME_FORMAT.format(date);
        }

        return TIME_FORMAT.format(date);
    }

    public static String getTimeShort(Date date ) {
//        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss");
//        String dateString = formatter.format(data);
        return DATE_TIME_FORMAT.format(date);
    }

    public static String str2Date2Str(String dateString) {
        try {
            Date time =new Date(dateString);
            return DATE_TIME_FORMAT_FULL.format(time);
        } catch (Exception e) {
            throw new RuntimeException("时间转化格式错误");
        }
    }
}
