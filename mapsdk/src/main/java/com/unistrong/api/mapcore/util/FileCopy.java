package com.unistrong.api.mapcore.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class FileCopy//ab
{
  public long copyFile(File srcFile, File destFile, long paramLong1, long paramLong2, a parama)
  {
    if (paramLong1 == 0L)
    {
      System.err.println("sizeOfDirectory is the total Size,  must be a positive number");
      if (parama != null) {
        parama.a("", "", -1);
      }
      return 0L;
    }
    String srcPath = srcFile.getAbsolutePath();
    String destPath = destFile.getAbsolutePath();
    try
    {
    	String [] localObject;
      if (srcFile.isDirectory())
      {
        if ((!destFile.exists()) && (!destFile.mkdirs())) {
          throw new IOException("Cannot create dir " + destFile.getAbsolutePath());
        }
         localObject = srcFile.list();
        if (localObject != null) {
          for (int i = 0; i < localObject.length; i++) {
            paramLong1 = copyFile(new File(srcFile, localObject[i]), new File(destFile, localObject[i]), paramLong1, paramLong2, parama);
          }
        }
      }
      else
      {
        File destFileParentFile = destFile.getParentFile();
        if ((destFileParentFile != null) && (!((File)destFileParentFile).exists()) &&
          (!((File)destFileParentFile).mkdirs())) {
          throw new IOException("Cannot create dir " + ((File)destFileParentFile).getAbsolutePath());
        }
        if ((parama != null) && (paramLong1 <= 0L)) {
          parama.a(srcPath, destPath);
        }
        FileInputStream inputStream = new FileInputStream(srcFile);
        FileOutputStream outputStream = new FileOutputStream(destFile);
        
        byte[] arrayOfByte = new byte['Ѐ'];
        int j;
        while ((j = inputStream.read(arrayOfByte)) > 0)
        {
          outputStream.write(arrayOfByte, 0, j);
          paramLong1 += j;
          if (parama != null)
          {
            float f = toPercent(paramLong1, paramLong2);
            
            parama.a(srcPath, destPath, f);
          }
        }
        inputStream.close();
        outputStream.flush();
        outputStream.close();
        if ((parama != null) && (paramLong1 >= paramLong2 - 1L)) {
          parama.b(srcPath, destPath);
        }
      }
    }
    catch (Exception localException)
    {
      localException.printStackTrace();
      if (parama != null) {
        parama.a(srcPath, destPath, -1);
      }
    }
    return paramLong1;
  }
  
  private float toPercent(long numerator, long denominator)
  {
    return (float)numerator / (float)denominator * 100.0F;
  }
  
  public static abstract interface a
  {
    public abstract void a(String paramString1, String paramString2);
    
    public abstract void a(String paramString1, String paramString2, float paramFloat);
    
    public abstract void b(String paramString1, String paramString2);
    
    public abstract void a(String paramString1, String paramString2, int paramInt);
  }
}
