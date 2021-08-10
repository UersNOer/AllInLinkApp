package com.leador.mapcore;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class SingalThread
  extends Thread
{
  private final Lock mLock = new ReentrantLock();
  private final Condition mWaiting = this.mLock.newCondition();
  private boolean isWaiting = true;
  String logTag = "SingalThread";

  public void doWait()
    throws InterruptedException
  {
    this.mLock.lock();
    
    this.isWaiting = true;
    this.mWaiting.await();
    
    this.mLock.unlock();
  }
  
  public void doAwake()
  {
    if (this.isWaiting)
    {
      this.mLock.lock();
      
      this.isWaiting = false;
      this.mWaiting.signal();
      this.mLock.unlock();
    }
  }
}
