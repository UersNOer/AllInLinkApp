package com.unistrong.network.log.impl;

import android.text.TextUtils;
import android.util.Log;

import com.unistrong.network.log.ILog;
import com.unistrong.utils.UNIMetaData;


/**
 * Description:Log的默认实现类 DefaultLog
 * Created by ltd ON 2020/5/25
 * Phone:18600920091
 * Email:td.liu@unistrong.com
 */
public class DefaultLog implements ILog {

    private static boolean mDebug = UNIMetaData.NETWORK_LOG;
    private static String mTag = DefaultLog.class.getSimpleName();

    @Override
    public void debug(boolean debug) {
        mDebug = debug;
    }

    @Override
    public void debug(String tag, boolean debug) {
        if (!TextUtils.isEmpty(tag)) {
            mTag = tag;
        }
        mDebug = debug;
    }

    @Override
    public void v(String msg) {
        if (mDebug)
            Log.v(mTag, msg);
    }

    @Override
    public void v(String tag, String msg) {
        if (mDebug)
            Log.v(tag, msg, null);
    }

    @Override
    public void v(String tag, String msg, Throwable throwable) {
        if (mDebug) {
            Log.v(TextUtils.isEmpty(tag) ? mTag : tag, msg, throwable);
        }
    }

    @Override
    public void d(String msg) {
        if (mDebug)
            Log.d(mTag, msg);
    }

    @Override
    public void d(String tag, String msg) {
        if (mDebug)
            Log.d(tag, msg, null);
    }

    @Override
    public void d(String tag, String msg, Throwable throwable) {
        if (mDebug) {
            Log.d(TextUtils.isEmpty(tag) ? mTag : tag, msg, throwable);
        }
    }

    @Override
    public void i(String msg) {
        if (mDebug)
            Log.i(mTag, msg);
    }

    @Override
    public void i(String tag, String msg) {
        if (mDebug)
            Log.i(tag, msg, null);
    }

    @Override
    public void i(String tag, String msg, Throwable throwable) {
        if (mDebug) {
            Log.i(TextUtils.isEmpty(tag) ? mTag : tag, msg, throwable);
        }
    }

    @Override
    public void w(String msg) {
        if (mDebug)
            Log.w(mTag, msg);
    }

    @Override
    public void w(String tag, String msg) {
        if (mDebug)
            Log.w(tag, msg, null);
    }

    @Override
    public void w(String tag, String msg, Throwable throwable) {
        if (mDebug) {
            Log.w(TextUtils.isEmpty(tag) ? mTag : tag, msg, throwable);
        }
    }

    @Override
    public void e(String msg) {
        if (mDebug)
            Log.e(mTag, msg);
    }

    @Override
    public void e(String tag, String msg) {
        if (mDebug)
            Log.e(tag, msg, null);
    }

    @Override
    public void e(String tag, String msg, Throwable throwable) {
        if (mDebug) {
            Log.e(TextUtils.isEmpty(tag) ? mTag : tag, msg, throwable);
        }
    }

    @Override
    public void json(String json) {
        json(Log.DEBUG, mTag, json);
    }

    @Override
    public void json(int logLevel, String tag, String json) {
        if (mDebug) {
            String formatJson = LogFormat.formatBorder(new String[]{LogFormat.formatJson(json)});
            XPrinter.println(logLevel, TextUtils.isEmpty(tag) ? mTag : tag, formatJson);
        }
    }

    @Override
    public void xml(String xml) {
        xml(Log.DEBUG, mTag, xml);
    }

    @Override
    public void xml(int logLevel, String tag, String xml) {
        if (mDebug) {
            String formatXml = LogFormat.formatBorder(new String[]{LogFormat.formatXml(xml)});
            XPrinter.println(logLevel, TextUtils.isEmpty(tag) ? mTag : tag, formatXml);
        }
    }
}
