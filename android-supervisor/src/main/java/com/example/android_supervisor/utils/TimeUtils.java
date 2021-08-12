package com.example.android_supervisor.utils;

import android.text.TextUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TimeUtils {

    public static boolean isEndOverStart(String startTime,String endTime,String formatStr){
        if (TextUtils.isEmpty(startTime) || TextUtils.isEmpty(endTime) || TextUtils.isEmpty(formatStr)){
            return true;
        }
        SimpleDateFormat format = new SimpleDateFormat(formatStr);
        try {
            Date start = format.parse(startTime);
            Date end = format.parse(endTime);
            if (end.getTime() >= start.getTime()){
                return true;
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return false;
    }
    public static String getLongTimeForDayStr(String time){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy年MM月dd日");
        try {
            Date parse = simpleDateFormat.parse(time);
            return parse.getTime() + "";
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return time;
    }
}
