package com.unistrong.api.mapcore;

import android.content.Context;
//import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.NinePatch;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.NinePatchDrawable;
import com.unistrong.api.mapcore.util.ResourcesUtilDecode;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Field;

class NinePatchToolDecode
{
  public static Drawable getDrawable(Context paramContext, String paramString)
    throws Exception
  {
    Bitmap localBitmap = getAssetsBitmap(paramContext, paramString);
    if (null == localBitmap.getNinePatchChunk())
    {
      Drawable localObject = new BitmapDrawable(localBitmap);
      return localObject;
    }
    Rect localObject = new Rect();
    a(localBitmap.getNinePatchChunk(), localObject);
    
    NinePatchDrawable localNinePatchDrawable = new NinePatchDrawable(paramContext.getResources(), localBitmap, localBitmap.getNinePatchChunk(), (Rect)localObject, null);
    return localNinePatchDrawable;
  }
  
  private static Bitmap a(InputStream paramInputStream)
    throws Exception
  {
    Bitmap localBitmap1 = BitmapFactory.decodeStream(paramInputStream);
    byte[] arrayOfByte = a(localBitmap1);
    boolean bool = NinePatch.isNinePatchChunk(arrayOfByte);
    if (bool)
    {
      Bitmap localBitmap2 = Bitmap.createBitmap(localBitmap1, 1, 1, localBitmap1
        .getWidth() - 2, localBitmap1.getHeight() - 2);
      localBitmap1.recycle();
      
      localBitmap1 = null;
      Field localField = localBitmap2.getClass().getDeclaredField("mNinePatchChunk");
      localField.setAccessible(true);
      localField.set(localBitmap2, arrayOfByte);
      return localBitmap2;
    }
    return localBitmap1;
  }
  
  private static Bitmap getAssetsBitmap(Context paramContext, String paramString)
    throws Exception
  {
    InputStream localInputStream = ResourcesUtilDecode.getSelfAssets(paramContext).open(paramString);
    Bitmap localBitmap = a(localInputStream);
    localInputStream.close();
    return localBitmap;
  }
  
  private static void a(byte[] paramArrayOfByte, Rect paramRect)
  {
    paramRect.left = a(paramArrayOfByte, 12);
    paramRect.right = a(paramArrayOfByte, 16);
    paramRect.top = a(paramArrayOfByte, 20);
    paramRect.bottom = a(paramArrayOfByte, 24);
  }
  
  private static byte[] a(Bitmap paramBitmap)
    throws IOException
  {
    int i = paramBitmap.getWidth();
    int j = paramBitmap.getHeight();
    
    int k = 0;
    int m = 0;
    
    int n = 0;
    int i1 = 0;
    
    ByteArrayOutputStream localByteArrayOutputStream = new ByteArrayOutputStream();
    for (int i2 = 0; i2 < 32; i2++) {
      localByteArrayOutputStream.write(0);
    }
    int[] arrayOfInt = new int[i - 2];
    paramBitmap.getPixels(arrayOfInt, 0, i, 1, 0, i - 2, 1);
    int i4 = arrayOfInt[0] == -16777216 ? 1 : 0;
    int i5 = arrayOfInt[(arrayOfInt.length - 1)] == -16777216 ? 1 : 0;
    int i6 = 0;
    int i7 = 0;
    for (int i8 = arrayOfInt.length; i7 < i8; i7++) {
      if (i6 != arrayOfInt[i7])
      {
        k++;
        a(localByteArrayOutputStream, i7);
        i6 = arrayOfInt[i7];
      }
    }
    if (i5 != 0)
    {
      k++;
      a(localByteArrayOutputStream, arrayOfInt.length);
    }
    n = k + 1;
    if (i4 != 0) {
      n--;
    }
    if (i5 != 0) {
      n--;
    }
    arrayOfInt = new int[j - 2];
    paramBitmap.getPixels(arrayOfInt, 0, 1, 0, 1, 1, j - 2);
    i4 = arrayOfInt[0] == -16777216 ? 1 : 0;
    i5 = arrayOfInt[(arrayOfInt.length - 1)] == -16777216 ? 1 : 0;
    i6 = 0;
    i7 = 0;
    for (int i8 = arrayOfInt.length; i7 < i8; i7++) {
      if (i6 != arrayOfInt[i7])
      {
        m++;
        a(localByteArrayOutputStream, i7);
        i6 = arrayOfInt[i7];
      }
    }
    if (i5 != 0)
    {
      m++;
      a(localByteArrayOutputStream, arrayOfInt.length);
    }
    i1 = m + 1;
    if (i4 != 0) {
      i1--;
    }
    if (i5 != 0) {
      i1--;
    }
    for (int i3 = 0; i3 < n * i1; i3++) {
      a(localByteArrayOutputStream, 1);
    }
    byte[] arrayOfByte = localByteArrayOutputStream.toByteArray();
    arrayOfByte[0] = 1;
    arrayOfByte[1] = ((byte)k);
    arrayOfByte[2] = ((byte)m);
    arrayOfByte[3] = ((byte)(n * i1));
    a(paramBitmap, arrayOfByte);
    return arrayOfByte;
  }
  
  private static void a(Bitmap paramBitmap, byte[] paramArrayOfByte)
  {
    int[] arrayOfInt = new int[paramBitmap.getWidth() - 2];
    paramBitmap.getPixels(arrayOfInt, 0, arrayOfInt.length, 1, paramBitmap
      .getHeight() - 1, arrayOfInt.length, 1);
    for (int i = 0; i < arrayOfInt.length; i++) {
      if (-16777216 == arrayOfInt[i])
      {
        a(paramArrayOfByte, 12, i);
        break;
      }
    }
    for (int i = arrayOfInt.length - 1; i >= 0; i--) {
      if (-16777216 == arrayOfInt[i])
      {
        a(paramArrayOfByte, 16, arrayOfInt.length - i - 2);
        break;
      }
    }
    arrayOfInt = new int[paramBitmap.getHeight() - 2];
    paramBitmap.getPixels(arrayOfInt, 0, 1, paramBitmap.getWidth() - 1, 0, 1, arrayOfInt.length);
    for (int i = 0; i < arrayOfInt.length; i++) {
      if (-16777216 == arrayOfInt[i])
      {
        a(paramArrayOfByte, 20, i);
        break;
      }
    }
    for (int i = arrayOfInt.length - 1; i >= 0; i--) {
      if (-16777216 == arrayOfInt[i])
      {
        a(paramArrayOfByte, 24, arrayOfInt.length - i - 2);
        break;
      }
    }
  }
  
  private static void a(OutputStream paramOutputStream, int paramInt)
    throws IOException
  {
    paramOutputStream.write(paramInt >> 0 & 0xFF);
    paramOutputStream.write(paramInt >> 8 & 0xFF);
    paramOutputStream.write(paramInt >> 16 & 0xFF);
    paramOutputStream.write(paramInt >> 24 & 0xFF);
  }
  
  private static void a(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
  {
    paramArrayOfByte[(paramInt1 + 0)] = ((byte)(paramInt2 >> 0));
    paramArrayOfByte[(paramInt1 + 1)] = ((byte)(paramInt2 >> 8));
    paramArrayOfByte[(paramInt1 + 2)] = ((byte)(paramInt2 >> 16));
    paramArrayOfByte[(paramInt1 + 3)] = ((byte)(paramInt2 >> 24));
  }
  
  private static int a(byte[] paramArrayOfByte, int paramInt)
  {
    int i = paramArrayOfByte[(paramInt + 0)];
    int j = paramArrayOfByte[(paramInt + 1)];
    int k = paramArrayOfByte[(paramInt + 2)];
    int m = paramArrayOfByte[(paramInt + 3)];
    int n = i & 0xFF | j << 8 | k << 16 | m << 24;
    return n;
  }
}
