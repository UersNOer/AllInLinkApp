package com.example.android_supervisor.utils;


import com.example.android_supervisor.entities.AutuSysData;
import com.example.android_supervisor.entities.UserBase;

/**
 * Created by Administrator on 2019/9/6.
 */
public class Environments {

    public static UserBase userBase;

    public static boolean isCaseCheck = false;
    public static String caseCheckUrl;

    public static AutuSysData autuSysData;//工作网格内存数据
    public static String  AreaCode ="";

    //如果返回true，服务器地址为localservice.cfg下得地址，具体要看NetInterceptor.java文件最后配置信息
    public static boolean isDeBug() {
        return false;
    }

}
