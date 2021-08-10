package com.unistrong.api.mapcore.util;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;

class FileAccessI//aa
{
  RandomAccessFile accessFile;
  
  public FileAccessI()
    throws IOException
  {
    this("", 0L);
  }
  
  public FileAccessI(String paramString, long paramLong)
    throws IOException
  {
    File localFile = new File(paramString);
    if (!localFile.exists())
    {
      if (!localFile.getParentFile().exists()) {
        localFile.getParentFile().mkdirs();
      }
      try
      {
        if (!localFile.exists()) {
          localFile.createNewFile();
        }
      }
      catch (IOException localIOException)
      {
        SDKLogHandler.exception(localIOException, "FileAccessI", "create");
        localIOException.printStackTrace();
      }
    }
    this.accessFile = new RandomAccessFile(paramString, "rw");
    this.accessFile.seek(paramLong);
  }
  
  public synchronized int a(byte[] paramArrayOfByte)
    throws IOException
  {
    int i = -1;
    this.accessFile.write(paramArrayOfByte);
    i = paramArrayOfByte.length;
    return i;
  }
  
  public void close()
  {
    if (this.accessFile != null)
    {
      try
      {
        this.accessFile.close();
      }
      catch (IOException localIOException)
      {
        localIOException.printStackTrace();
      }
      this.accessFile = null;
    }
  }
}
