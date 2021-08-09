package com.unistrong.api.services.core;

class BasicLogHandler//cd
{
  protected static BasicLogHandler logHandler;
  protected Thread.UncaughtExceptionHandler caughtExceptionThread;
  protected boolean c = true;
  
  public static void a(Throwable throwable, String className, String methodName)
  {
    throwable.printStackTrace();
    if (logHandler != null) {
      logHandler.outPutLog(throwable, 1, className, methodName);
    }
  }
  
  protected void outPutLog(Throwable throwable, int paramInt, String className, String methodName) {



  }

  protected void normalLog(int customid,String context, int logType, String className, String methodName){

  }

//  protected void a(Context context, SDKInfo parambv, boolean paramBoolean) {
//
//
//
//  }
}
