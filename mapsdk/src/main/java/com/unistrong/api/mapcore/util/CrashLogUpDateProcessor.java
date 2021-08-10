package com.unistrong.api.mapcore.util;

import android.content.Context;
import android.os.Looper;

class CrashLogUpDateProcessor//ce
  extends LogUpDateProcessor
{
  private static boolean a = true;
  
  protected CrashLogUpDateProcessor(Context context)
  {
    super(context);
  }
  
  protected String getDir()
  {
    return Log.crashlogdir;
  }
  
  protected int getLogType()
  {
    return 0;
  }
  
  protected boolean dbIsRight(Context context)
  {
    if (a)
    {
      a = false;
      synchronized (Looper.getMainLooper())
      {
        UpdateLogDBOperation localda = new UpdateLogDBOperation(context);
        
        UpdateLogInfo localdc = localda.getUpdateLog();
        if (localdc == null) {
          return true;
        }
        if (localdc.a())
        {
          localdc.a(false);
          localda.updateLog(localdc);
          
          return true;
        }
        return false;
      }
    }
    return false;
  }
}
