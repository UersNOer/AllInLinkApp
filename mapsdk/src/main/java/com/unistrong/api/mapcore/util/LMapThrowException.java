package com.unistrong.api.mapcore.util;

public final class LMapThrowException
  extends Exception
{
  public static <T> T ThrowNullPointerException(T paramT)
  {
    if (paramT == null) {
      throw new NullPointerException("null reference");
    }
    return paramT;
  }
  
  public static <T> T ThrowNullPointerException(T paramT, Object paramObject)
  {
    if (paramT == null) {
      throw new NullPointerException(String.valueOf(paramObject));
    }
    return paramT;
  }
  
  public static void ThrowIllegalStateException(boolean paramBoolean, Object paramObject)
  {
    if (!paramBoolean) {
      throw new IllegalStateException(String.valueOf(paramObject));
    }
  }
  
  public static void bThrowIllegalArgumentException(boolean paramBoolean, Object paramObject)
  {
    if (!paramBoolean) {
      throw new IllegalArgumentException(String.valueOf(paramObject));
    }
  }
  
  public static void ThrowIllegalArgumentException(boolean paramBoolean, String paramString, Object[] paramArrayOfObject)
  {
    if (!paramBoolean) {
      throw new IllegalArgumentException(String.format(paramString, paramArrayOfObject));
    }
  }
  
  private LMapThrowException()
  {
    throw new AssertionError("Uninstantiable");
  }
}
