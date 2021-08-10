package com.leador.mapcore;

import android.content.Context;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class ResUtil
{
  public static Bitmap decodeBitmapWithAdaptiveSize(Context paramContext, int paramInt)
  {
    BitmapFactory.Options localOptions = new BitmapFactory.Options();
    localOptions.inScaled = false;
    localOptions.inDensity = 0;
    localOptions.inTargetDensity = 0;
    
    Bitmap localBitmap1 = BitmapFactory.decodeResource(paramContext.getResources(), paramInt, localOptions);
    
    localBitmap1.setDensity(0);
    
    DisplayMetrics localDisplayMetrics = paramContext.getResources().getDisplayMetrics();
    float f = 2.0F / localDisplayMetrics.density;
    if (f == 1.0D) {
      return localBitmap1;
    }
    Bitmap localBitmap2 = Bitmap.createScaledBitmap(localBitmap1, 
      (int)(localBitmap1.getWidth() / f), (int)(localBitmap1.getHeight() / f), false);
    
    localBitmap1.recycle();
    return localBitmap2;
  }
  
  public static Bitmap decodeAssetBitmap(Context paramContext, String paramString)
  {
    AssetManager localAssetManager = paramContext.getAssets();
    Bitmap localBitmap = null;
    try
    {
      InputStream localInputStream = localAssetManager.open(paramString);
      localBitmap = BitmapFactory.decodeStream(localInputStream);
      localBitmap.setDensity(0);
      localInputStream.close();
    }
    catch (IOException localIOException)
    {
      localIOException.printStackTrace();
    }
    return localBitmap;
  }
  
  public static Bitmap decodeAssetBitmapWithSize(Context paramContext, String paramString, int paramInt1, int paramInt2)
  {
    AssetManager localAssetManager = paramContext.getAssets();
    Bitmap localBitmap1 = null;
    try
    {
      InputStream localInputStream = localAssetManager.open(paramString);
      
      localBitmap1 = BitmapFactory.decodeStream(localInputStream);
      
      localInputStream.close();
      
      Bitmap localBitmap2 = Bitmap.createBitmap(paramInt1, paramInt2, Bitmap.Config.ARGB_8888);
      
      Canvas localCanvas = new Canvas(localBitmap2);
      Rect localRect1 = new Rect();
      Rect localRect2 = new Rect();
      localRect1.set(0, 0, localBitmap1.getWidth(), localBitmap1.getHeight());
      localRect2.set(0, 0, paramInt1, paramInt2);
      
      localCanvas.drawBitmap(localBitmap1, localRect1, localRect2, null);
      localBitmap1.recycle();
      localBitmap1 = null;
      return localBitmap2;
    }
    catch (IOException localIOException)
    {
      localIOException.printStackTrace();
    }
    return null;
  }
  
  public static byte[] decodeAssetResData(Context paramContext, String paramString)
  {
    AssetManager localAssetManager = paramContext.getAssets();
    try
    {
      InputStream localInputStream = localAssetManager.open(paramString);
      ByteArrayOutputStream localByteArrayOutputStream = new ByteArrayOutputStream();
      
      byte[] arrayOfByte1 = new byte['Ѐ'];
      int i = -1;
      while ((i = localInputStream.read(arrayOfByte1)) > -1) {
        localByteArrayOutputStream.write(arrayOfByte1, 0, i);
      }
      byte[] arrayOfByte2 = localByteArrayOutputStream.toByteArray();
      localByteArrayOutputStream.close();
      localInputStream.close();
      return arrayOfByte2;
    }
    catch (IOException localIOException)
    {
      localIOException.printStackTrace();
      return null;
    }
    catch (OutOfMemoryError localOutOfMemoryError)
    {
      localOutOfMemoryError.printStackTrace();
    }
    return null;
  }
  
  public static int dipToPixel(Context paramContext, int paramInt)
  {
    if (paramContext == null) {
      return paramInt;
    }
    try
    {
      float f = TypedValue.applyDimension(1, paramInt, paramContext
      
        .getResources().getDisplayMetrics());
      return (int)f;
    }
    catch (Exception localException)
    {
      localException.printStackTrace();
    }
    return paramInt;
  }
  
  public static int spToPixel(Context paramContext, int paramInt)
  {
    float f = TypedValue.applyDimension(2, paramInt, paramContext
      .getResources()
      .getDisplayMetrics());
    return (int)f;
  }
  
  public static String getString(Context paramContext, int paramInt)
  {
    return paramContext.getResources().getString(paramInt);
  }
}
