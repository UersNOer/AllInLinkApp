package com.example.android_supervisor.utils;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.example.android_supervisor.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * 作者：liangzixun
 * 时间：2018/6/27 09:47
 * 描述：
 */
public class MDatePickerDialog extends AlertDialog {

    private LayoutInflater inflater;
    private TextView tvCancel;
    private TextView tvConfirm;
    private EasyPickerView epvYear;
    private EasyPickerView epvMonth;
    private EasyPickerView epvDay;
    private CallBack callBack;

    private ArrayList<String> yearList;
    private ArrayList<String> monthList;
    private ArrayList<String> dayList;

    private String currentYear = "";
    private String currentMonth = "";
    private String currentDay = "";

    private SimpleDateFormat dateFormat1 = new SimpleDateFormat("yyyy年MM月dd日");
//    private SimpleDateFormat dateFormat2 = new SimpleDateFormat("yyyy-MM-dd HH:mm");

    private String content="";//

    public MDatePickerDialog(Context context, String content, CallBack callBack) {
        super(context, R.style.dialog);
        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.callBack = callBack;
        this.content=content;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
        initData();
        addListener();
    }

    private void initData() {
        Calendar calendar = Calendar.getInstance(Locale.CHINA);
        int cyear = calendar.get(Calendar.YEAR);
        int cmonth = calendar.get(Calendar.MONTH);
        cmonth++;
        int cday = calendar.get(Calendar.DATE);
        currentYear = "" + cyear;
        if (cmonth < 10) {
            currentMonth = "0" + cmonth + "月";
        } else {
            currentMonth = cmonth + "月";
        }
        if (cday < 10) {
            currentDay = "0" + cday + "日";
        } else {
            currentDay = cday + "日";
        }
        yearList = new ArrayList<>();
        for (int i = cyear; i < cyear + 5; i++) {
            yearList.add("" + i);
        }
        monthList = new ArrayList<>();
        for (int i = 1; i < 13; i++) {
            String temp = "" + i;
            if (i < 10) {
                temp = "0" + i;
            }
            monthList.add(temp + "月");
        }
        dayList = getDaylist(cyear, cmonth);
//        for (int i = 1; i < 32; i++) {
//            String temp = "" + i;
//            if (i < 10) {
//                temp = "0" + i;
//            }
//            dayList.add(temp + "日");
//        }

        if (!TextUtils.isEmpty(content)){
            currentYear=content.substring(0,4);
            currentMonth=content.substring(5,7)+"月";
            currentDay=content.substring(8,10)+"日";

        }
        epvYear.setDataList(yearList);
        epvMonth.setDataList(monthList);
        epvDay.setDataList(dayList);

        epvYear.moveTo(yearList.indexOf(currentYear));
        epvMonth.moveTo(monthList.indexOf(currentMonth));
        epvDay.moveTo(dayList.indexOf(currentDay));

    }

    private void initView() {
        View view = inflater.inflate(R.layout.dialog_date_picker, null);
        tvCancel = view.findViewById(R.id.tvCancel);
        tvConfirm = view.findViewById(R.id.tvConfirm);
        epvYear = view.findViewById(R.id.epvYear);
        epvMonth = view.findViewById(R.id.epvMonth);
        epvDay = view.findViewById(R.id.epvDay);
        setContentView(view);
        setCanceledOnTouchOutside(false);
    }

    private void addListener() {
        tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        tvConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
                if (callBack != null) {
                    String temp = currentYear + "年" + currentMonth + currentDay;
                    try {
                        Date date = dateFormat1.parse(temp);
                        callBack.callback(dateFormat1.format(date));
                    } catch (ParseException e) {
                        e.printStackTrace();
                        callBack.callback("");
                    }


                }
            }
        });
        epvYear.setOnScrollChangedListener(new EasyPickerView.OnScrollChangedListener() {
            @Override
            public void onScrollChanged(int curIndex) {

            }

            @Override
            public void onScrollFinished(int curIndex) {
                currentYear = yearList.get(curIndex);
                int temp= Integer.valueOf(currentMonth.substring(0,2));
                if (temp==2){
                    dayList=getDaylist(Integer.valueOf(currentYear),temp);
                    epvDay.setDataList(dayList);
                    if (dayList.contains(currentDay)){
                        epvDay.moveTo(dayList.indexOf(currentDay));
                    }else {
                        currentDay=dayList.get(0);
                    }
                }
            }
        });
        epvMonth.setOnScrollChangedListener(new EasyPickerView.OnScrollChangedListener() {
            @Override
            public void onScrollChanged(int curIndex) {

            }

            @Override
            public void onScrollFinished(int curIndex) {
                int temp= Integer.valueOf(currentMonth.substring(0,2));
                int oldNum=getDayNum(Integer.valueOf(currentYear),temp);
                int newNum=getDayNum(Integer.valueOf(currentYear),curIndex+1);
                if (oldNum!=newNum){
                    dayList=getDaylist(Integer.valueOf(currentYear),curIndex+1);
                    epvDay.setDataList(dayList);
                    if (dayList.contains(currentDay)){
                        epvDay.moveTo(dayList.indexOf(currentDay));
                    }else {
                        currentDay=dayList.get(0);
                    }
                }
                currentMonth = monthList.get(curIndex);
            }
        });
        epvDay.setOnScrollChangedListener(new EasyPickerView.OnScrollChangedListener() {
            @Override
            public void onScrollChanged(int curIndex) {

            }

            @Override
            public void onScrollFinished(int curIndex) {
                currentDay = dayList.get(curIndex);
            }
        });

    }

    public interface CallBack {
        public void callback(String time);
    }

    private ArrayList<String> getDaylist(int year, int month) {
        int num = getDayNum(year,month);
        ArrayList<String> list = new ArrayList<String>();
        for (int i = 1; i <= num; i++) {
            String d = i + "日";
            if (i < 10) {
                d = "0" + i + "日";
            }
            list.add(d);
        }
        return list;
    }

    private int getDayNum(int year,int month){
        int num = 31;
        switch (month) {
            case 1:
            case 3:
            case 5:
            case 7:
            case 8:
            case 10:
            case 12:
                num = 31;
                break;
            case 4:
            case 6:
            case 9:
            case 11:
                num = 30;
                break;
            case 2: {
                if (year % 4 == 0) {
                    num = 29;
                } else {
                    num = 28;
                }
            }
            break;
        }
        return num;
    }

}
