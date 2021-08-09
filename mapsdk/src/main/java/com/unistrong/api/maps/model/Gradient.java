package com.unistrong.api.maps.model;

import android.graphics.Color;
import android.util.Log;
import com.unistrong.api.maps.UnistrongException;
import java.util.HashMap;

/**
 * 热力图渐变颜色定义类。
 */
public class Gradient
{
  private int a;
  private int[] b;
  private float[] c;
  
  private class a
  {
    private final int b;
    private final int c;
    private final float d;
    
    private a(int paramInt1, int paramInt2, float paramFloat)
    {
      this.b = paramInt1;
      this.c = paramInt2;
      this.d = paramFloat;
    }
  }
  
  private boolean d = true;

    /**
     * 构造函数，color和statPoints不能为null，长度不能为0，两数组长度须一致，startPoints数据必须递增。
     * @param colors - 渐变色可以是所有颜色数组 [0x000000, 0xFFFFFF]。例如:{Color.rgb(0, 225, 0), Color.rgb(255, 0, 0) }, 按声明的顺序由冷色到热色。
     * @param startPoints - 每一个颜色对应的起始点数组[0.0f, 1.0f]，例如:{ 0.2f, 1f }，与colors 数组一一对应。
     */
  public Gradient(int[] colors, float[] startPoints)
  {
    this(colors, startPoints, 1000);
  }
  
  private Gradient(int[] paramArrayOfInt, float[] paramArrayOfFloat, int paramInt)
  {
    try
    {
      if ((paramArrayOfInt == null) || (paramArrayOfFloat == null)) {
        throw new UnistrongException("colors and startPoints should not be null");
      }
      if (paramArrayOfInt.length != paramArrayOfFloat.length) {
        throw new UnistrongException("colors and startPoints should be same length");
      }
      if (paramArrayOfInt.length == 0) {
        throw new UnistrongException("No colors have been defined");
      }
      for (int i = 1; i < paramArrayOfFloat.length; i++) {
        if (paramArrayOfFloat[i] <= paramArrayOfFloat[(i - 1)]) {
          throw new UnistrongException("startPoints should be in increasing order");
        }
      }
      this.a = paramInt;
      this.b = new int[paramArrayOfInt.length];
      this.c = new float[paramArrayOfFloat.length];
      System.arraycopy(paramArrayOfInt, 0, this.b, 0, paramArrayOfInt.length);
      System.arraycopy(paramArrayOfFloat, 0, this.c, 0, paramArrayOfFloat.length);
      this.d = true;
    }
    catch (UnistrongException localMapException)
    {
      this.d = false;
      Log.e("leadorAPI", localMapException.getErrorMessage());
      localMapException.printStackTrace();
    }
  }
  
  private HashMap<Integer, a> a()
  {
    HashMap<Integer,a> localHashMap = new HashMap<Integer,a>();
    if (this.c[0] != 0.0F)
    {
      int i = Color.argb(0, 
        Color.red(this.b[0]), Color.green(this.b[0]), Color.blue(this.b[0]));
      localHashMap.put(Integer.valueOf(0), new a(i, this.b[0], this.a * this.c[0]));
    }
    for (int i = 1; i < this.b.length; i++) {
      localHashMap.put(Integer.valueOf((int)(this.a * this.c[(i - 1)])), new a(this.b[(i - 1)], this.b[i], this.a * (this.c[i] - this.c[(i - 1)])));
    }
    if (this.c[(this.c.length - 1)] != 1.0F)
    {
      int i = this.c.length - 1;
      localHashMap.put(Integer.valueOf((int)(this.a * this.c[i])), new a(this.b[i], this.b[i], this.a * (1.0F - this.c[i])));
    }
    return localHashMap;
  }
  
  protected int[] generateColorMap(double paramDouble)
  {
    HashMap<Integer, a> localHashMap = a();
    int[] arrayOfInt = new int[this.a];
    a locala = (a)localHashMap.get(Integer.valueOf(0));
    int i = 0;
    for (int j = 0; j < this.a; j++)
    {
      if (localHashMap.containsKey(Integer.valueOf(j)))
      {
        locala = (a)localHashMap.get(Integer.valueOf(j));
        i = j;
      }
      float f = (j - i) / locala.d;
      arrayOfInt[j] = a(locala.b, locala.c, f);
    }
    if (paramDouble != 1.0D) {
      for (int j = 0; j < this.a; j++)
      {
        int k = arrayOfInt[j];
        arrayOfInt[j] = Color.argb((int)(Color.alpha(k) * paramDouble), 
          Color.red(k), Color.green(k), Color.blue(k));
      }
    }
    return arrayOfInt;
  }
  
  static int a(int paramInt1, int paramInt2, float paramFloat)
  {
    int i = (int)((Color.alpha(paramInt2) - Color.alpha(paramInt1)) * paramFloat + Color.alpha(paramInt1));
    
    float[] arrayOfFloat1 = new float[3];
    Color.RGBToHSV(Color.red(paramInt1), Color.green(paramInt1), Color.blue(paramInt1), arrayOfFloat1);
    float[] arrayOfFloat2 = new float[3];
    Color.RGBToHSV(Color.red(paramInt2), Color.green(paramInt2), Color.blue(paramInt2), arrayOfFloat2);
    if (arrayOfFloat1[0] - arrayOfFloat2[0] > 180.0F) {
      arrayOfFloat2[0] += 360.0F;
    } else if (arrayOfFloat2[0] - arrayOfFloat1[0] > 180.0F) {
      arrayOfFloat1[0] += 360.0F;
    }
    float[] arrayOfFloat3 = new float[3];
    for (int j = 0; j < 3; j++) {
      arrayOfFloat3[j] = ((arrayOfFloat2[j] - arrayOfFloat1[j]) * paramFloat + arrayOfFloat1[j]);
    }
    return Color.HSVToColor(i, arrayOfFloat3);
  }
  
  protected boolean isAvailable()
  {
    return this.d;
  }
}
