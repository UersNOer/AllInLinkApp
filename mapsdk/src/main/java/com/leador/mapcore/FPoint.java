package com.leador.mapcore;

public class FPoint
{
  public float x;
  public float y;
  
  public FPoint() {}
  
  public FPoint(float paramFloat1, float paramFloat2)
  {
    this.x = paramFloat1;
    this.y = paramFloat2;
  }
  
  public boolean equals(Object paramObject)
  {
    FPoint localFPoint = (FPoint)paramObject;
    if (localFPoint == null) {
      return false;
    }
    return (this.x == localFPoint.x) && (this.y == localFPoint.y);
  }

  @Override
  public String toString() {
    return "FPoint{" +
            "x=" + x +
            ", y=" + y +
            '}';
  }
}
