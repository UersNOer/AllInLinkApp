package com.unistrong.api.mapcore.util;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.FileDescriptor;

public class ImageResizerDecode
  extends ImageWorkerDecode
{
  protected int mImageWidth; // a
  protected int mImageHeight; //b
  
  public ImageResizerDecode(Context paramContext, int paramInt1, int paramInt2)
  {
    super(paramContext);
    a(paramInt1, paramInt2);
  }
  
  public void a(int paramInt1, int paramInt2)
  {
    this.mImageWidth = paramInt1;
    this.mImageHeight = paramInt2;
  }
  
  private Bitmap a(int paramInt)
  {
    LogManager.writeLog("ImageResizer", "processBitmap - " + paramInt, 111);
    return a(this.mResources, paramInt, this.mImageWidth, this.mImageHeight, 
      getImageCache());
  }
  
  protected Bitmap processBitmap(Object paramObject)
  {
    return a(Integer.parseInt(String.valueOf(paramObject)));
  }
  
  public static Bitmap a(Resources paramResources, int paramInt1, int paramInt2, int paramInt3, ImageCacheDecode paramba)
  {
    BitmapFactory.Options localOptions = new BitmapFactory.Options();
    localOptions.inJustDecodeBounds = true;
    BitmapFactory.decodeResource(paramResources, paramInt1, localOptions);
    
    localOptions.inSampleSize = a(localOptions, paramInt2, paramInt3);
    
    localOptions.inJustDecodeBounds = false;
    return BitmapFactory.decodeResource(paramResources, paramInt1, localOptions);
  }
  
  public static Bitmap a(FileDescriptor paramFileDescriptor, int paramInt1, int paramInt2, ImageCacheDecode paramba)
  {
    BitmapFactory.Options localOptions = new BitmapFactory.Options();
    localOptions.inJustDecodeBounds = true;
    BitmapFactory.decodeFileDescriptor(paramFileDescriptor, null, localOptions);
    
    localOptions.inSampleSize = a(localOptions, paramInt1, paramInt2);
    
    localOptions.inJustDecodeBounds = false;
    
    return BitmapFactory.decodeFileDescriptor(paramFileDescriptor, null, localOptions);
  }
  
  public static int a(BitmapFactory.Options paramOptions, int paramInt1, int paramInt2)
  {
    int i = paramOptions.outHeight;
    int j = paramOptions.outWidth;
    int k = 1;
    if ((i > paramInt2) || (j > paramInt1))
    {
      int m = Math.round(i / paramInt2);
      
      int n = Math.round(j / paramInt1);
      
      k = m < n ? m : n;
      
      float f1 = j * i;
      
      float f2 = paramInt1 * paramInt2 * 2;
      while (f1 / (k * k) > f2) {
        k++;
      }
    }
    return k;
  }
}
