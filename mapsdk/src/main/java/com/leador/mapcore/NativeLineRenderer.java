package com.leador.mapcore;

public class NativeLineRenderer
{

  public static native int nativeDrawLineByTextureID(int[] pointsArray, int pointNum, long mapstate, float glTextLen, float width, int texId, int color_argb, boolean complextex, boolean useColor);

  public static native int nativeDrawArrowLine(int[] pointsArray, int pointNum, long mapstate, float width, int color_border, int color_argb);

  public static native void nativeDrawLineByMultiTextureID(float[] paramArrayOfFloat, int paramInt1, float paramFloat1, int[] paramArrayOfInt1, int paramInt2, int[] paramArrayOfInt2, int paramInt3, float paramFloat2);
  
  public static native void nativeDrawLineByMultiColor(float[] paramArrayOfFloat, int paramInt1, float paramFloat, int paramInt2, int[] paramArrayOfInt1, int paramInt3, int[] paramArrayOfInt2, int paramInt4);
  
  public static native void nativeDrawGradientColorLine(float[] paramArrayOfFloat, int paramInt1, float paramFloat, int[] paramArrayOfInt1, int paramInt2, int[] paramArrayOfInt2, int paramInt3, int paramInt4);

//  static
//  {
//    try
//    {
//      System.loadLibrary("gdinamapv4sdk752ex"); //使用最新的SO库
//    }
//    catch (Throwable localThrowable)
//    {
//      localThrowable.printStackTrace();
//    }
//  }
}
