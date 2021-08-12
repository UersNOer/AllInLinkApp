package com.example.android_supervisor.notify;

/**
 * @author wujie
 */

public class Notifies {
    public static final String NOTIFY_ACTION = "com.example.android_supervisor.action.NOTIFY";

    public static final int NOTIFY_STATUS_NONE = 0;
    public static final int NOTIFY_STATUS_ONLINE = 1;
    public static final int NOTIFY_STATUS_OFFLINE = 2;
    public static final int NOTIFY_STATUS_SENDSUCCEED = 3;
    public static final int NOTIFY_STATUS_SENDFAILED = 4;

    public static final int NOTIFY_TYPE_NONE = 0;
    public static final int NOTIFY_TYPE_ALL = 1;
    public static final int NOTIFY_TYPE_GPS = 2;
    public static final int NOTIFY_TYPE_GPRS = 3;
    public static final int NOTIFY_TYPE_MSG = 4;
    public static final int NOTIFY_TYPE_TASK = 5;
    public static final int NOTIFY_TYPE_CHECKUP = 6;
    public static final int NOTIFY_TYPE_CHECKIN = 7;
    public static final int NOTIFY_TYPE_CHECKOUT = 8;
    public static final int NOTIFY_TYPE_CHECKINOUT = 9;
    public static final int NOTIFY_TYPE_HESHI = 10;
    public static final int NOTIFY_TYPE_HECHA = 11;

    public static final int NOTIFY_TYPE_MSG_BADGE = 12;

    public static final int  NOTIFY_TYPE_GPS_COLLECT =13;
}
