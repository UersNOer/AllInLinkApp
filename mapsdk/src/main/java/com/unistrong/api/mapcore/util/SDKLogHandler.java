package com.unistrong.api.mapcore.util;

import android.content.Context;
import android.os.Looper;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.RejectedExecutionException;

//日志收集
public class SDKLogHandler extends BasicLogHandler implements Thread.UncaughtExceptionHandler {
    private Context context;
    private static ExecutorService executorService;

    static synchronized ExecutorService getLogSingleThreadExecutor() {
        try {
            if ((executorService == null) || (executorService.isShutdown())) {
                executorService = Executors.newSingleThreadExecutor();
            }
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
        return executorService;
    }

    public static synchronized SDKLogHandler a(Context paramContext, SDKInfo sdkInfo) throws IMMapCoreException {
        if (sdkInfo == null) {
            throw new IMMapCoreException("sdk info is null");
        }
        if ((sdkInfo.getProduct() == null) || ("".equals(sdkInfo.getProduct()))) {
            throw new IMMapCoreException("sdk name is invalid");
        }
        try {
            if (logHandler == null) {
                logHandler = new SDKLogHandler(paramContext, sdkInfo);
            } else {
                logHandler.c = false;
            }
            logHandler.a(paramContext, sdkInfo, logHandler.c);
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
        return (SDKLogHandler) logHandler;
    }

    public static synchronized SDKLogHandler getLogHandler() {
        return (SDKLogHandler) logHandler;
    }

    public static void exception(Throwable throwable, String className, String methodName) {
//    if (BasicLogHandler.logHandler != null) {
//      BasicLogHandler.logHandler.outPutLog(throwable, 1, className, methodName);//待实现
//    }
    }

    public static void normalLog(int customid, String content, String className, String methodName) {
        if (logHandler != null) {
            logHandler.normalLog(customid, content, 3, className, methodName);
        }
    }

    public static synchronized void colseLogThread() {
        try {
            if (executorService != null) {
                executorService.shutdown();
            }
        } catch (Throwable localThrowable1) {
            localThrowable1.printStackTrace();
        }
        try {
            if ((logHandler != null) && (Thread.getDefaultUncaughtExceptionHandler() == logHandler) && (logHandler.caughtExceptionThread != null)) {
                Thread.setDefaultUncaughtExceptionHandler(logHandler.caughtExceptionThread);
            }
            logHandler = null;
        } catch (Throwable localThrowable2) {
            localThrowable2.printStackTrace();
        }
    }

    private SDKLogHandler(Context context, SDKInfo parambv) {
        this.context = context;
        LogNetListener locala = new LogNetListener(context);
        HttpUrlUtil.setNetCompleteListener(locala);
        setExceptionHandler();
    }

    private void setExceptionHandler() {
        try {
            this.caughtExceptionThread = Thread.getDefaultUncaughtExceptionHandler();
            if (this.caughtExceptionThread == null) {
                Thread.setDefaultUncaughtExceptionHandler(this);
                this.c = true;
            } else {
                String str = this.caughtExceptionThread.toString();
                if (str.indexOf("com.leador.api") != -1) {
                    this.c = false;
                } else {
                    Thread.setDefaultUncaughtExceptionHandler(this);
                    this.c = true;
                }
            }
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
    }

    /**
     * crashException未捕获异常
     *
     * @param paramThread
     * @param paramThrowable
     */
    public void uncaughtException(Thread paramThread, Throwable paramThrowable) {
        if (paramThrowable == null) {
            return;
        }
        outPutLog(paramThrowable, 0, null, null);
        if (this.caughtExceptionThread != null) {
            this.caughtExceptionThread.uncaughtException(paramThread, paramThrowable);
        }
    }

    public void b(Throwable paramThrowable, String paramString1, String paramString2) {
        try {
            if (paramThrowable == null) {
                return;
            }
            outPutLog(paramThrowable, 1, paramString1, paramString2);
        } catch (Throwable localThrowable) {
            localThrowable.printStackTrace();
        }
    }

    protected void outPutLog(Throwable throwable, int logType, String className, String methodName) {
        Log.toLog(this.context, throwable, logType, className, methodName);//TODO  输出日志到数据库
    }

    protected void normalLog(int customid, String content, int logType, String className, String methodName) {
        Log.toLog(this.context, customid, content, logType, className, methodName);//TODO  输出日志到数据库
    }

    protected void a(final Context paramContext, final SDKInfo sdkInfo, final boolean paramBoolean) {
        try {
            ExecutorService localExecutorService = getLogSingleThreadExecutor();
            if ((localExecutorService == null) || (localExecutorService.isShutdown())) {
                return;
            }
            localExecutorService.submit(new Runnable() {
                public void run() {
                    try {
                        SDKDBOperation localObject1;
                        synchronized (sdkInfo) {
                            localObject1 = new SDKDBOperation(paramContext, true);

                            ((SDKDBOperation) localObject1).a(sdkInfo);
                        }
                        if (paramBoolean) {
                            synchronized (Looper.getMainLooper()) {
                                UpdateLogDBOperation localObject2 = new UpdateLogDBOperation(paramContext);

                                UpdateLogInfo localdc = new UpdateLogInfo();
                                localdc.c(true);
                                localdc.a(true);
                                localdc.b(true);
                                localObject2.updateLog(localdc);
                            }
                            Log.writerANRLog(paramContext);//查看是否存在ANR
                        }
                    } catch (Throwable localThrowable) {
                        localThrowable.printStackTrace();
                    }
                }
            });
        } catch (RejectedExecutionException localRejectedExecutionException) {
        } catch (Throwable localThrowable) {
            localThrowable.printStackTrace();
        }
    }

    private static class LogNetListener implements NetCompleteListener {
        private Context context1;

        LogNetListener(Context context) {
            this.context1 = context;
        }

        @Override
        public void complete() {
            try {
                Log.processLog(this.context1);//TODO  输出日志到数据库
            } catch (Throwable localThrowable) {
                SDKLogHandler.exception(localThrowable, "LogNetListener", "onNetCompleted");
                localThrowable.printStackTrace();
            }
        }
    }
}
