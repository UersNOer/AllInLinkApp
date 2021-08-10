package com.unistrong.api.mapcore.util;

import android.content.Context;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.RejectedExecutionException;

public class Log//ci
{
  static final String a = "/a/";
  static final String exceptionlogdir = "b";
  static final String crashlogdir = "c";
  static final String anrlogdir = "d";
  static final String normallogdir = "f";
  static void toLog(final Context context, final Throwable throwable, final int logType, final String className, final String methodName)
  {
    try
    {
      ExecutorService executorService = SDKLogHandler.getLogSingleThreadExecutor();
      if ((executorService == null) || (executorService.isShutdown())) {
        return;
      }
      executorService.submit(new Runnable() {
        public void run() {
          try {
            LogWriter writer = LogWriter.getLogWriterByType(logType);
            if (writer == null) {
              return;
            }
            writer.writerLog(context, throwable, className, methodName);
          } catch (Throwable localThrowable) {
            localThrowable.printStackTrace();
          }
        }
      });
    }
    catch (RejectedExecutionException e) {}catch (Throwable throwable1)
    {
      throwable1.printStackTrace();
    }
  }
  static void toLog(final Context context , final int customid, final String content, final int logType, final String className, final String methodName)
  {
    try
    {
      ExecutorService executorService = SDKLogHandler.getLogSingleThreadExecutor();
      if ((executorService == null) || (executorService.isShutdown())) {
        return;
      }
      executorService.submit(new Runnable() {
        public void run() {
          try {
            LogWriter writer = LogWriter.getLogWriterByType(logType);
            if (writer == null) {
              return;
            }
            writer.writerLog(context,customid, content, className, methodName);
          } catch (Throwable localThrowable) {
            localThrowable.printStackTrace();
          }
        }
      });
    }
    catch (RejectedExecutionException e) {}catch (Throwable throwable1)
    {
      throwable1.printStackTrace();
    }
  }
  static void writerANRLog(Context context)
  {
    try
    {
      LogWriter logWriter = LogWriter.getLogWriterByType(2);
      if (logWriter == null) {
        return;
      }
      logWriter.writerANRLog(context);
    }
    catch (Throwable throwable)
    {
      throwable.printStackTrace();
    }
  }
  
  static void processLog(final Context context)
  {
    try
    {
      ExecutorService executorService = SDKLogHandler.getLogSingleThreadExecutor();
      if ((executorService == null) || (executorService.isShutdown())) {
        return;
      }
      executorService.submit(new Runnable() {
        public void run() {
          LogUpDateProcessor crashLog = null;//CrashLogUpDateProcessor
          LogUpDateProcessor exception = null;//ExceptionLogUpDateProcessor
          LogUpDateProcessor anrLog = null;//ANRLogUpDateProcessor
          LogUpDateProcessor normal = null;
          try {
            crashLog = LogUpDateProcessor.getLogProcessor(context, 0);
            exception = LogUpDateProcessor.getLogProcessor(context, 1);
            anrLog = LogUpDateProcessor.getLogProcessor(context, 2);
            normal = LogUpDateProcessor.getLogProcessor(context,3);
            normal.processUpdateLog(context);
            crashLog.processUpdateLog(context);
            exception.processUpdateLog(context);
            anrLog.processUpdateLog(context);
          } catch (RejectedExecutionException e) {

          } catch (Throwable throwable) {
            SDKLogHandler.exception(throwable, "Log", "processLog");
            throwable.printStackTrace();
          } finally {
            if (crashLog != null) {
              crashLog.closeDiskLru();
            }
            if(normal!=null){
              normal.closeDiskLru();
            }
            if (exception != null) {
              exception.closeDiskLru();
            }
            if (anrLog != null) {
              anrLog.closeDiskLru();
            }
          }
        }
      });
    }
    catch (Throwable throwable)
    {
      SDKLogHandler.exception(throwable, "Log", "processLog");
      throwable.printStackTrace();
    }
  }
}
