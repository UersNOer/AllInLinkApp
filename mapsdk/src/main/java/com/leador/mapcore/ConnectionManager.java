package com.leador.mapcore;

import com.unistrong.api.mapcore.util.LogManager;
import com.unistrong.api.mapcore.util.DeviceInfo;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ConnectionManager
  extends SingalThread
{
  private static final int MAX_THREAD_COUNT = 1;
  boolean threadFlag = true;
  MapCore mGLMapEngine;
  private ExecutorService threadPool = Executors.newFixedThreadPool(1);
  private ArrayList<LoadTask> threadPoolList = new ArrayList();
  private ArrayList<BaseMapLoader> connPool = new ArrayList();
  
  public ConnectionManager(MapCore paramMapCore)
  {
    this.mGLMapEngine = paramMapCore;
  }
  
  public void shutDown()
  {
    if (this.connPool != null) {
      this.threadPool.shutdownNow();
    }
  }
  
  public void insertConntionTask(BaseMapLoader paramBaseMapLoader)
  {
    synchronized (this.connPool)
    {
      this.connPool.add(paramBaseMapLoader);
    }
    doAwake();
  }
  
  void checkListPoolOld()
  {
    BaseMapLoader localBaseMapLoader = null;
    Iterator localIterator = this.threadPoolList.iterator();
    while (localIterator.hasNext())
    {
      localBaseMapLoader = ((LoadTask)localIterator.next()).mapLoader;
      if ((!localBaseMapLoader.isRequestValid()) || (localBaseMapLoader.hasFinished()))
      {
        localBaseMapLoader.doCancel();
        localIterator.remove();
      }
    }
  }
  
  private void checkListPool()
  {
    BaseMapLoader localBaseMapLoader = null;
    LoadTask locala = null;
    ArrayList localArrayList = new ArrayList();
    int i = this.threadPoolList.size();
    for (int j = 0; j < i; j++)
    {
      locala = (LoadTask)this.threadPoolList.get(j);
      localBaseMapLoader = locala.mapLoader;
      if ((!localBaseMapLoader.isRequestValid()) || (localBaseMapLoader.hasFinished()))
      {
        localArrayList.add(locala);
        localBaseMapLoader.doCancel();
      }
    }
    this.threadPoolList.removeAll(localArrayList);
  }
  
  public void run()
  {
    try
    {
      DeviceInfo.a();
      
      doAsyncRequest();
    }
    catch (Throwable localThrowable)
    {
      localThrowable.printStackTrace();
    }
  }
  
  private void doAsyncRequest()
  {
    while (this.threadFlag)
    {
      int i = 0;
      synchronized (this.connPool)
      {
        BaseMapLoader localBaseMapLoader = null;
        checkListPool();
        while (this.connPool.size() > 0)
        {
          if (this.threadPoolList.size() > 1)
          {
            i = 1;
            break;
          }
          localBaseMapLoader = (BaseMapLoader)this.connPool.remove(0);
          LoadTask task = new LoadTask(localBaseMapLoader);
          
          LogManager.writeLog(LogManager.productInfo, hashCode() + " add Task, get mapdata from net, datasource: " + localBaseMapLoader.datasource, 111);
          this.threadPoolList.add(task);
          if (!this.threadPool.isShutdown()) {
            this.threadPool.execute(task);
          }
        }
      }
      if (i != 0) {
        try
        {
          sleep(30L);
        }
        catch (Exception localException) {}
      } else if (this.threadFlag) {
        try
        {
          doWait();
        }
        catch (Throwable localThrowable) {}
      }
    }
  }
  class LoadTask
          implements Runnable
  {
    public BaseMapLoader mapLoader = null;

    public LoadTask(BaseMapLoader paramBaseMapLoader)
    {
      this.mapLoader = paramBaseMapLoader;
    }

    public void run()
    {
      try
      {
        switch (mapLoader.datasource){
          case BaseMapLoader.DATA_SOURCE_TYPE_DATA_BMP_USER_GIRDSMAP:
            this.mapLoader.doGridRequest();
          default:
            this.mapLoader.doRequest();
        }
      }
      catch (Throwable localThrowable) {}
    }
  }
}
