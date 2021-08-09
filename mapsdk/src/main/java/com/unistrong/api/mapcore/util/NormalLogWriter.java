package com.unistrong.api.mapcore.util;

import java.util.Date;
import java.util.List;

/**
 * Created by wcb on 16/12/8.
 */
public class NormalLogWriter extends LogWriter
{
    private a a;

    protected int getType()
    {
        return 3;
    }

    protected String toMD5Encrypt(String paramString)
    {
        String str1 = null;

        String str2 = Format.getDateAndTime(new Date().getTime());
        String str3 = paramString + str2;
        str1 = MD5.encryptString(str3);

        return str1;
    }

    protected String getDirName()
    {
        return Log.normallogdir;
    }

    class a
            implements FileOperationListenerDecode
    {
        private LogDBOperation db;

        a(LogDBOperation paramcv)
        {
            this.db = paramcv;
        }

        public void a(String paramString)
        {
            try
            {
                this.db.b(paramString, NormalLogWriter.this.getType());
            }
            catch (Throwable localThrowable)
            {
                localThrowable.printStackTrace();
            }
        }
    }

    protected FileOperationListenerDecode a(LogDBOperation paramcv)
    {
        try
        {
            if (this.a == null) {
                this.a = new a(paramcv);
            }
        }
        catch (Throwable localThrowable)
        {
            localThrowable.printStackTrace();
        }
        return this.a;
    }

    protected String a(List<SDKInfo> paramList)
    {
        return null;
    }
}