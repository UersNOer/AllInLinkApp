package com.leador.mapcore;

public class IPoint implements Cloneable
{
  public int x;
  public int y;
  
  public IPoint() {}
  
  public IPoint(int x, int y)
  {
    this.x = x;
    this.y = y;
  }
  
  public Object clone()
  {
    IPoint localIPoint = null;
    try
    {
      localIPoint = (IPoint)super.clone();
    }
    catch (CloneNotSupportedException localCloneNotSupportedException)
    {
      localCloneNotSupportedException.printStackTrace();
    }
    return localIPoint;
  }

  @Override
  public boolean equals(Object paramObject) {
    if (this == paramObject) {
      return true;
    }
    if (!(paramObject instanceof IPoint)) {
      return false;
    }
    IPoint locala = (IPoint) paramObject;
    return (this.x == locala.x) && (this.y == locala.y);
  }
}
