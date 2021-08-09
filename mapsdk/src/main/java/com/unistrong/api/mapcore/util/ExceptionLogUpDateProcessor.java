package com.unistrong.api.mapcore.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.os.Looper;

class ExceptionLogUpDateProcessor//cg
  extends LogUpDateProcessor
{
  private static boolean a = true;
  
  protected ExceptionLogUpDateProcessor(Context paramContext)
  {
    super(paramContext);
  }
  
  protected String getDir()
  {
    return Log.exceptionlogdir;
  }
  
  protected int getLogType()
  {
    return 1;
  }
  
  protected boolean dbIsRight(Context context)
  {
    if ((DeviceInfo.getActiveNetWorkType(context) == ConnectivityManager.TYPE_WIFI) && (a))
    {
      a = false;
      synchronized (Looper.getMainLooper())
      {
        UpdateLogDBOperation localda = new UpdateLogDBOperation(context);
        
        UpdateLogInfo localdc = localda.getUpdateLog();
        if (localdc == null) {
          return true;
        }
        if (localdc.b())
        {
          localdc.b(false);
          localda.updateLog(localdc);
          
          return true;
        }
        return false;
      }
    }
    return false;
  }
}
