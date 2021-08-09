package com.unistrong.api.mapcore.util;

public class IntArray
{
  public int[] a;
  public int b;
  public boolean c;
  
  public IntArray()
  {
    this(true, 16);
  }
  
  public IntArray(boolean paramBoolean, int paramInt)
  {
    this.c = paramBoolean;
    this.a = new int[paramInt];
  }
  
  public void a(int paramInt)
  {
    int[] arrayOfInt = this.a;
    if (this.b == arrayOfInt.length) {
      arrayOfInt = d(Math.max(8, (int)(this.b * 1.75F)));
    }
    arrayOfInt[(this.b++)] = paramInt;
  }
  
  public int b(int paramInt)
  {
    if (paramInt >= this.b) {
      throw new IndexOutOfBoundsException("index can't be >= size: " + paramInt + " >= " + this.b);
    }
    int[] arrayOfInt = this.a;
    int i = arrayOfInt[paramInt];
    this.b -= 1;
    if (this.c) {
      System.arraycopy(arrayOfInt, paramInt + 1, arrayOfInt, paramInt, this.b - paramInt);
    } else {
      arrayOfInt[paramInt] = arrayOfInt[this.b];
    }
    return i;
  }
  
  public void a()
  {
    this.b = 0;
  }
  
  public int[] c(int paramInt)
  {
    int i = this.b + paramInt;
    if (i > this.a.length) {
      d(Math.max(8, i));
    }
    return this.a;
  }
  
  protected int[] d(int paramInt)
  {
    int[] arrayOfInt1 = new int[paramInt];
    int[] arrayOfInt2 = this.a;
    System.arraycopy(arrayOfInt2, 0, arrayOfInt1, 0, Math.min(this.b, arrayOfInt1.length));
    this.a = arrayOfInt1;
    return arrayOfInt1;
  }
  
  public boolean equals(Object paramObject)
  {
    if (paramObject == this) {
      return true;
    }
    if (!(paramObject instanceof IntArray)) {
      return false;
    }
    IntArray localbe = (IntArray)paramObject;
    int i = this.b;
    if (i != localbe.b) {
      return false;
    }
    for (int j = 0; j < i; j++) {
      if (this.a[j] != localbe.a[j]) {
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
    int[] arrayOfInt = this.a;
    StringBuilder localStringBuilder = new StringBuilder(32);
    localStringBuilder.append('[');
    localStringBuilder.append(arrayOfInt[0]);
    for (int i = 1; i < this.b; i++)
    {
      localStringBuilder.append(", ");
      localStringBuilder.append(arrayOfInt[i]);
    }
    localStringBuilder.append(']');
    return localStringBuilder.toString();
  }
}
