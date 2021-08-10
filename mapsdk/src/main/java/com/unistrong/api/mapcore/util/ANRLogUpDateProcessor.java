package com.unistrong.api.mapcore.util;

import android.content.Context;
import android.os.Looper;

class ANRLogUpDateProcessor extends LogUpDateProcessor
{
  private static boolean a = true;
  
  protected ANRLogUpDateProcessor(Context paramContext)
  {
    super(paramContext);
  }
  
  protected String getDir()
  {
    return Log.anrlogdir;
  }
  
  protected int getLogType()
  {
    return 2;
  }
  
  protected boolean dbIsRight(Context context)
  {
    if ((DeviceInfo.getActiveNetWorkType(context) == 1) && (a))
    {
      a = false;
      synchronized (Looper.getMainLooper())
      {
        UpdateLogDBOperation localda = new UpdateLogDBOperation(context);
        
        UpdateLogInfo localdc = localda.getUpdateLog();
        if (localdc == null) {
          return true;
        }
        if (localdc.c())
        {
          localdc.c(false);
          localda.updateLog(localdc);
          return true;
        }
        return false;
      }
    }
    return false;
  }
}
