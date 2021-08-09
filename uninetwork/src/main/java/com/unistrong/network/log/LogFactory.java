package com.unistrong.network.log;


import com.unistrong.network.log.impl.DefaultLog;

/**
 * Description: Log的工程类 LogFactory
 * Created by ltd ON 2020/5/25
 * Phone:18600920091
 * Email:td.liu@unistrong.com
 */
public class LogFactory {

    private LogFactory() {
    }

    public static void setLog(ILog log) {
        Holder.setInstance(log);
    }


    private static class Holder {
        private static ILog instance = new DefaultLog();

        public static void setInstance(ILog instance) {
            Holder.instance = instance;
        }
    }

    public static final ILog getInstance() {
        return Holder.instance;
    }
}
