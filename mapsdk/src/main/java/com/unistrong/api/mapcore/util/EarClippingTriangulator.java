package com.unistrong.api.mapcore.util;

public class EarClippingTriangulator//az
{
  private final ShortArray a = new ShortArray();
  private short[] b;
  private float[] c;
  private int d;
  private final IntArray e = new IntArray();
  private final ShortArray f = new ShortArray();
  
  public ShortArray a(float[] paramArrayOfFloat)
  {
    return a(paramArrayOfFloat, 0, paramArrayOfFloat.length);
  }
  
  public ShortArray a(float[] paramArrayOfFloat, int paramInt1, int paramInt2)
  {
    this.c = paramArrayOfFloat;
    int i = this.d = paramInt2 / 2;
    int j = paramInt1 / 2;
    
    ShortArray localbj1 = this.a;
    localbj1.a();
    localbj1.c(i);
    localbj1.b = i;
    short[] arrayOfShort = this.b = localbj1.a;
    int k;
    if (b(paramArrayOfFloat, paramInt1, paramInt2))
    {
      for (k = 0; k < i; k = (short)(k + 1)) {
        arrayOfShort[k] = ((short)(j + k));
      }
    }
    else
    {
      k = 0;
      for (int m = i - 1; k < i; k++) {
        arrayOfShort[k] = ((short)(j + m - k));
      }
    }
    IntArray localbe = this.e;
    localbe.a();
    localbe.c(i);
    int m = 0;
    for (int n = i; m < n; m++) {
      localbe.a(a(m));
    }
    ShortArray localbj2 = this.f;
    localbj2.a();
    localbj2.c(Math.max(0, i - 2) * 3);
    a();
    return localbj2;
  }
  
  private void a()
  {
    int[] arrayOfInt = this.e.a;
    while (this.d > 3)
    {
      int i = b();
      c(i);
      
      int j = d(i);
      int k = i == this.d ? 0 : i;
      arrayOfInt[j] = a(j);
      arrayOfInt[k] = a(k);
    }
    if (this.d == 3)
    {
      ShortArray localbj = this.f;
      short[] arrayOfShort = this.b;
      localbj.a(arrayOfShort[0]);
      localbj.a(arrayOfShort[1]);
      localbj.a(arrayOfShort[2]);
    }
  }
  
  private int a(int paramInt)
  {
    short[] arrayOfShort = this.b;
    int i = arrayOfShort[d(paramInt)] * 2;
    int j = arrayOfShort[paramInt] * 2;
    int k = arrayOfShort[e(paramInt)] * 2;
    float[] arrayOfFloat = this.c;
    return a(arrayOfFloat[i], arrayOfFloat[(i + 1)], arrayOfFloat[j], arrayOfFloat[(j + 1)], arrayOfFloat[k], arrayOfFloat[(k + 1)]);
  }
  
  private int b()
  {
    int i = this.d;
    for (int j = 0; j < i; j++) {
      if (b(j)) {
        return j;
      }
    }
    int[] arrayOfInt = this.e.a;
    for (int k = 0; k < i; k++) {
      if (arrayOfInt[k] != -1) {
        return k;
      }
    }
    return 0;
  }
  
  private boolean b(int paramInt)
  {
    int[] arrayOfInt = this.e.a;
    if (arrayOfInt[paramInt] == -1) {
      return false;
    }
    int i = d(paramInt);
    int j = e(paramInt);
    short[] arrayOfShort = this.b;
    int k = arrayOfShort[i] * 2;
    int m = arrayOfShort[paramInt] * 2;
    int n = arrayOfShort[j] * 2;
    float[] arrayOfFloat = this.c;
    float f1 = arrayOfFloat[k];float f2 = arrayOfFloat[(k + 1)];
    float f3 = arrayOfFloat[m];float f4 = arrayOfFloat[(m + 1)];
    float f5 = arrayOfFloat[n];float f6 = arrayOfFloat[(n + 1)];
    for (int i1 = e(j); i1 != i; i1 = e(i1)) {
      if (arrayOfInt[i1] != 1)
      {
        int i2 = arrayOfShort[i1] * 2;
        float f7 = arrayOfFloat[i2];
        float f8 = arrayOfFloat[(i2 + 1)];
        if ((a(f5, f6, f1, f2, f7, f8) >= 0) && 
          (a(f1, f2, f3, f4, f7, f8) >= 0) && 
          (a(f3, f4, f5, f6, f7, f8) >= 0)) {
          return false;
        }
      }
    }
    return true;
  }
  
  private void c(int paramInt)
  {
    short[] arrayOfShort = this.b;
    ShortArray localbj = this.f;
    
    localbj.a(arrayOfShort[d(paramInt)]);
    localbj.a(arrayOfShort[paramInt]);
    localbj.a(arrayOfShort[e(paramInt)]);
    
    this.a.b(paramInt);
    this.e.b(paramInt);
    this.d -= 1;
  }
  
  private int d(int paramInt)
  {
    return (paramInt == 0 ? this.d : paramInt) - 1;
  }
  
  private int e(int paramInt)
  {
    return (paramInt + 1) % this.d;
  }
  
  private static boolean b(float[] paramArrayOfFloat, int paramInt1, int paramInt2)
  {
    if (paramInt2 <= 2) {
      return false;
    }
    float f1 = 0.0F;
    int i = paramInt1;
    for (int j = paramInt1 + paramInt2 - 3; i < j; i += 2)
    {
      float f2 = paramArrayOfFloat[i];
      float f3 = paramArrayOfFloat[(i + 1)];
      float f4 = paramArrayOfFloat[(i + 2)];
      float f5 = paramArrayOfFloat[(i + 3)];
      f1 += f2 * f5 - f4 * f3;
    }
    float f2 = paramArrayOfFloat[(paramInt1 + paramInt2 - 2)];
    float f3 = paramArrayOfFloat[(paramInt1 + paramInt2 - 1)];
    float f4 = paramArrayOfFloat[paramInt1];
    float f5 = paramArrayOfFloat[(paramInt1 + 1)];
    return f1 + f2 * f5 - f4 * f3 < 0.0F;
  }
  
  private static int a(float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4, float paramFloat5, float paramFloat6)
  {
    float f1 = paramFloat1 * (paramFloat6 - paramFloat4);
    f1 += paramFloat3 * (paramFloat2 - paramFloat6);
    f1 += paramFloat5 * (paramFloat4 - paramFloat2);
    return (int)Math.signum(f1);
  }
}
