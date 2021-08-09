package com.unistrong.api.mapcore.util;

import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.RejectedExecutionException;

public final class ThreadPool//du
{
	class dw
	  implements ThreadTask.RunnableInterface
	{
		ThreadPool pool;
	  dw(ThreadPool paramdu) {
        pool = paramdu;}
	  
	  public void begin(ThreadTask runnable) {}
	  
	  public void end(ThreadTask runnable)
	  {
		  pool.removeQueue(runnable, false);
	  }
	  
	  public void stop(ThreadTask runnable)
	  {
	    pool.removeQueue(runnable, true);
	  }
	}
	
  private static ThreadPool instance = null;
  private ExecutorService executorService;
  private ConcurrentHashMap<ThreadTask, Future<?>> c = new ConcurrentHashMap();
  private ThreadTask.RunnableInterface d = new dw(this);
  
  public static synchronized ThreadPool getInstance(int nThreads)
  {
    if (instance == null) {
      instance = new ThreadPool(nThreads);
    }
    return instance;
  }
  
  private ThreadPool(int nThreads)
  {
    try
    {
      this.executorService = Executors.newFixedThreadPool(nThreads);
    }
    catch (Throwable localThrowable)
    {
      SDKLogHandler.exception(localThrowable, "TPool", "ThreadPool");
      localThrowable.printStackTrace();
    }
  }
  
  public void addTask(ThreadTask task)
    throws IMMapCoreException
  {
    try
    {
      if (contain(task)) {
        return;
      }
      if ((this.executorService == null) || (this.executorService.isShutdown())) {
        return;
      }
      task.myRunnable = this.d;
      Future future = null;
      try
      {
        future = this.executorService.submit(task);
      }
      catch (RejectedExecutionException e)
      {
        return;
      }
      if (future == null) {
        return;
      }
      addQueue(task, future);
    }
    catch (Throwable localThrowable)
    {
      localThrowable.printStackTrace();
      SDKLogHandler.exception(localThrowable, "TPool", "addTask");
      throw new IMMapCoreException("thread pool has exception");
    }
  }
  
  public static synchronized void onDestroy()
  {
    try
    {
      if (instance != null)
      {
        instance.destroy();
        instance = null;
      }
    }
    catch (Throwable localThrowable)
    {
      SDKLogHandler.exception(localThrowable, "TPool", "onDestroy");
      localThrowable.printStackTrace();
    }
  }
  
  private void destroy()
  {
    try
    {
    	//Set<Map.Entry<K,V>>
      Iterator localIterator = this.c.entrySet().iterator();
      while (localIterator.hasNext())
      {
        Map.Entry localEntry = (Map.Entry)localIterator.next();
        
        ThreadTask localdv = (ThreadTask)localEntry.getKey();
        Future localFuture = (Future)this.c.get(localdv);
        try
        {
          if (localFuture != null) {
            localFuture.cancel(true);
          }
        }
        catch (Exception localException)
        {
          localException.printStackTrace();
        }
      }
      this.c.clear();
      this.executorService.shutdown();
    }
    catch (Throwable localThrowable)
    {
      SDKLogHandler.exception(localThrowable, "TPool", "destroy");
      localThrowable.printStackTrace();
    }
  }
  
  private synchronized boolean contain(ThreadTask paramdv)
  {
    boolean bool = false;
    try
    {
      bool = this.c.containsKey(paramdv);
    }
    catch (Throwable localThrowable)
    {
      SDKLogHandler.exception(localThrowable, "TPool", "contain");
      localThrowable.printStackTrace();
    }
    return bool;
  }
  
  private synchronized void addQueue(ThreadTask paramdv, Future<?> paramFuture)
  {
    try
    {
      this.c.put(paramdv, paramFuture);
    }
    catch (Throwable localThrowable)
    {
      SDKLogHandler.exception(localThrowable, "TPool", "addQueue");
      localThrowable.printStackTrace();
    }
  }
  
  private synchronized void removeQueue(ThreadTask paramdv, boolean paramBoolean)
  {
    try
    {
      Future localFuture = (Future)this.c.remove(paramdv);
      if ((paramBoolean) && (localFuture != null)) {
        localFuture.cancel(true);
      }
    }
    catch (Throwable localThrowable)
    {
      SDKLogHandler.exception(localThrowable, "TPool", "removeQueue");
      localThrowable.printStackTrace();
    }
  }
}
