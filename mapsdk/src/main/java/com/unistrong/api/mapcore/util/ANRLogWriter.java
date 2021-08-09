package com.unistrong.api.mapcore.util;

import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Iterator;

class ANRLogWriter
  extends LogWriter
{
  private String[] a;
  private int b;
  private boolean c;
  private int d;
  private a e;
  
  ANRLogWriter()
  {
    this.a = new String[10];
    this.b = 0;
    this.c = false;
    this.d = 0;
  }
  
  protected int getType()
  {
    return 2;
  }
  
  protected String getDirName()
  {
    return Log.anrlogdir;
  }
  
  protected String toMD5Encrypt(String paramString)
  {
    String str = MD5.encryptString(paramString);
    return str;
  }
  
  private class a
    implements FileOperationListenerDecode
  {
    private LogDBOperation b;
    
    private a(LogDBOperation paramcv)
    {
      this.b = paramcv;
    }
    
    public void a(String paramString)
    {
      try
      {
        this.b.b(paramString, ANRLogWriter.this.getType());
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
      if (this.e == null) {
        this.e = new a(paramcv);
      }
    }
    catch (Throwable localThrowable)
    {
      SDKLogHandler.exception(localThrowable, "ANRWriter", "getListener");
      localThrowable.printStackTrace();
    }
    return this.e;
  }
  
  protected String a(java.util.List<SDKInfo> list)
  {
    FileInputStream var2 = null;
    StrictLineReader var3 = null;

    Object var5;
    try {
      File var4 = new File("/data/anr/traces.txt");
      if(var4.exists()) {
        var2 = new FileInputStream(var4);
        var3 = new StrictLineReader(var2, dj.a);
        boolean var44 = false;

        while(true) {
          try {
            String var6 = var3.readLine();
            if(var6.contains("pid")) {
              while(!var6.contains("\"main\"")) {
                var6 = var3.readLine();
              }

              var44 = true;
            }

            if(var6.equals("")) {
              var44 = false;
            }

            if(var44) {
              this.b(var6);
              if(this.d == 5) {
                return this.c?this.c():null;
              }

              if(!this.c) {
                Iterator var7 = list.iterator();

                while(var7.hasNext()) {
                  SDKInfo var8 = (SDKInfo)var7.next();
                  this.c = this.isContain(var8.getPackageNames(), var6);
                  if(this.c) {
                    this.setSDKInfo(var8);
                  }
                }
              } else {
                ++this.d;
              }
            }
          } catch (EOFException var40) {
            return this.c?this.c():null;
          }
        }
      }

      var5 = null;
    } catch (FileNotFoundException var41) {
      return this.c?this.c():null;
    } catch (IOException var42) {
      SDKLogHandler.exception(var42, "ANRWriter", "initLog");
      var42.printStackTrace();
      return this.c?this.c():null;
    } finally {
      try {
        if(var3 != null) {
          var3.close();
        }
      } catch (IOException var38) {
        SDKLogHandler.exception(var38, "ANRWriter", "initLog1");
        var38.printStackTrace();
      } catch (Throwable var39) {
        SDKLogHandler.exception(var39, "ANRWriter", "initLog2");
        var39.printStackTrace();
      }

      try {
        if(var2 != null) {
          var2.close();
        }
      } catch (IOException var36) {
        SDKLogHandler.exception(var36, "ANRWriter", "initLog3");
        var36.printStackTrace();
      } catch (Throwable var37) {
        SDKLogHandler.exception(var37, "ANRWriter", "initLog4");
        var37.printStackTrace();
      }

    }

    return (String)var5;
  }
  
  private String c()
  {
    StringBuilder localStringBuilder = new StringBuilder();
    try
    {
      for (int i = this.b; i < 10; i++)
      {
        if (i > 9) {
          break;
        }
        localStringBuilder.append(this.a[i]);
      }
      for (int i = 0; i < this.b; i++) {
        localStringBuilder.append(this.a[i]);
      }
    }
    catch (Throwable localThrowable)
    {
      SDKLogHandler.exception(localThrowable, "ANRWriter", "getLogInfo");
      localThrowable.printStackTrace();
    }
    return localStringBuilder.toString();
  }
  
  private void b(String paramString)
  {
    try
    {
      if (this.b > 9) {
        this.b = 0;
      }
      this.a[this.b] = paramString;
      this.b += 1;
    }
    catch (Throwable localThrowable)
    {
      SDKLogHandler.exception(localThrowable, "ANRWriter", "addData");
      localThrowable.printStackTrace();
    }
  }
}
