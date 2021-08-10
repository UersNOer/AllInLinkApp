package com.unistrong.network.log;

/**
 * Description: Log 的接口定义 ILog
 * Created by ltd ON 2020/5/25
 * Phone:18600920091
 * Email:td.liu@unistrong.com
 */
public interface ILog {
    void debug(boolean debug);

    void debug(String tag, boolean debug);

    void v(String msg);

    void v(String tag, String msg);

    void v(String tag, String msg, Throwable throwable);

    void d(String msg);

    void d(String tag, String msg);

    void d(String tag, String msg, Throwable throwable);

    void i(String msg);

    void i(String tag, String msg);

    void i(String tag, String msg, Throwable throwable);

    void w(String msg);

    void w(String tag, String msg);

    void w(String tag, String msg, Throwable throwable);

    void e(String msg);

    void e(String tag, String msg);

    void e(String tag, String msg, Throwable throwable);

    void json(String json);

    void json(int logLevel, String tag, String json);

    void xml(String xml);

    void xml(int logLevel, String tag, String xml);
}
