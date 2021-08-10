package com.unistrong.api.mapcore.util;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map.Entry;

public class TaskManager//q
{
  private static TaskManager instance;
  private ThreadPool b;
  private LinkedHashMap<String, ThreadTask> taskList = new LinkedHashMap<String, ThreadTask>();
  
  public static TaskManager getInstance(int nThreads)
  {
    return getInstance(true, nThreads);
  }
  
  private static synchronized TaskManager getInstance(boolean paramBoolean, int nThreads)
  {
    try
    {
      if (instance == null) {
        instance = new TaskManager(paramBoolean, nThreads);
      } else if ((paramBoolean) && (instance.b == null)) {
        instance.b = ThreadPool.getInstance(nThreads);
      }
    }
    catch (Throwable localThrowable)
    {
      localThrowable.printStackTrace();
    }
    return instance;
  }
  
  private TaskManager(boolean paramBoolean, int nThreads)
  {
    try
    {
      if (paramBoolean) {
        this.b = ThreadPool.getInstance(nThreads);
      }
    }
    catch (Throwable localThrowable)
    {
      localThrowable.printStackTrace();
    }
  }
  
  public void a()
  {
    synchronized (this.taskList)
    {
      if (this.taskList.size() < 1) {
        return;
      }
      Iterator<Entry<String,ThreadTask>> localIterator = this.taskList.entrySet().iterator();
      while (localIterator.hasNext())
      {
        //Map.Entry localEntry = (Map.Entry)localIterator.next();
    	//Object localObject1 = localEntry.getKey();
        Entry<String,ThreadTask> localEntry = (Entry<String,ThreadTask>)localIterator.next();
        String localObject1 = localEntry.getKey();
        
      }
      this.taskList.clear();
    }
  }
  


  public void b()
  {
    a();
    ThreadPool.onDestroy();
    this.b = null;
    instance = null;
  }
  

  private boolean d = true;
}
