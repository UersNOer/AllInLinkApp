package com.unistrong.api.mapcore.util;

public abstract class ThreadTask//dv
  implements Runnable
{
  RunnableInterface myRunnable;
  
  public final void run()
  {
    try
    {
      if (this.myRunnable != null) {
        this.myRunnable.begin(this);
      }
      if (Thread.interrupted()) {
        return;
      }
      myRun();
      if (Thread.interrupted()) {
        return;
      }
      if (this.myRunnable != null) {
        this.myRunnable.end(this);
      }
    }
    catch (Throwable localThrowable)
    {
      SDKLogHandler.exception(localThrowable, "ThreadTask", "run");
      localThrowable.printStackTrace();
    }
  }
  
  public abstract void myRun();
  
  public final void cancelTask()
  {
    try
    {
      if (this.myRunnable != null) {
        this.myRunnable.stop(this);
      }
    }
    catch (Throwable throwable)
    {
      SDKLogHandler.exception(throwable, "ThreadTask", "cancelTask");
      throwable.printStackTrace();
    }
  }
  
  static abstract interface RunnableInterface
  {
    public abstract void begin(ThreadTask paramdv);
    
    public abstract void end(ThreadTask paramdv);
    
    public abstract void stop(ThreadTask paramdv);
  }
}
