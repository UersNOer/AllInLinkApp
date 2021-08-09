package com.unistrong.api.mapcore.util;

public class ShortArray//bj
{
  public short[] a;
  public int b;
  public boolean c;
  
  public ShortArray()
  {
    this(true, 16);
  }
  
  public ShortArray(boolean paramBoolean, int paramInt)
  {
    this.c = paramBoolean;
    this.a = new short[paramInt];
  }
  
  public void a(short paramShort)
  {
    short[] arrayOfShort = this.a;
    if (this.b == arrayOfShort.length) {
      arrayOfShort = d(Math.max(8, (int)(this.b * 1.75F)));
    }
    arrayOfShort[(this.b++)] = paramShort;
  }
  
  public short a(int paramInt)
  {
    if (paramInt >= this.b) {
      throw new IndexOutOfBoundsException("index can't be >= size: " + paramInt + " >= " + this.b);
    }
    return this.a[paramInt];
  }
  
  public short b(int paramInt)
  {
    if (paramInt >= this.b) {
      throw new IndexOutOfBoundsException("index can't be >= size: " + paramInt + " >= " + this.b);
    }
    short[] arrayOfShort = this.a;
    short s = arrayOfShort[paramInt];
    this.b -= 1;
    if (this.c) {
      System.arraycopy(arrayOfShort, paramInt + 1, arrayOfShort, paramInt, this.b - paramInt);
    } else {
      arrayOfShort[paramInt] = arrayOfShort[this.b];
    }
    return s;
  }
  
  public void a()
  {
    this.b = 0;
  }
  
  public short[] c(int paramInt)
  {
    int i = this.b + paramInt;
    if (i > this.a.length) {
      d(Math.max(8, i));
    }
    return this.a;
  }
  
  protected short[] d(int paramInt)
  {
    short[] arrayOfShort1 = new short[paramInt];
    short[] arrayOfShort2 = this.a;
    System.arraycopy(arrayOfShort2, 0, arrayOfShort1, 0, Math.min(this.b, arrayOfShort1.length));
    this.a = arrayOfShort1;
    return arrayOfShort1;
  }
  
  public boolean equals(Object paramObject)
  {
    if (paramObject == this) {
      return true;
    }
    if (!(paramObject instanceof ShortArray)) {
      return false;
    }
    ShortArray localbj = (ShortArray)paramObject;
    int i = this.b;
    if (i != localbj.b) {
      return false;
    }
    for (int j = 0; j < i; j++) {
      if (this.a[j] != localbj.a[j]) {
        return false;
      }
    }
    return true;
  }
  
  public String toString()
  {
    if (this.b == 0) {
      return "[]";
    }
    short[] arrayOfShort = this.a;
    StringBuilder localStringBuilder = new StringBuilder(32);
    localStringBuilder.append('[');
    localStringBuilder.append(arrayOfShort[0]);
    for (int i = 1; i < this.b; i++)
    {
      localStringBuilder.append(", ");
      localStringBuilder.append(arrayOfShort[i]);
    }
    localStringBuilder.append(']');
    return localStringBuilder.toString();
  }
}
