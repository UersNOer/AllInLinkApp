package com.unistrong.api.mapcore.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.os.Looper;

/**
 * Created by wcb on 16/12/8.
 */
public class NormalLogUpDateProcessor extends LogUpDateProcessor
{
    private static boolean a = true;

    protected NormalLogUpDateProcessor(Context paramContext)
    {
        super(paramContext);
    }

    protected String getDir()
    {
        return Log.normallogdir;
    }

    protected int getLogType()
    {
        return 3;
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