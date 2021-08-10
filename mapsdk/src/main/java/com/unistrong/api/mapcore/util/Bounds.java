package com.unistrong.api.mapcore.util;

import com.leador.mapcore.DPoint;

public class Bounds
{
  public final double left;
  public final double top;
  public final double right;
  public final double bottom;
  public final double centerX;
  public final double centerY;

  public Bounds(double left, double right, double top, double bottom)
  {
    this.left = left;
    this.top = top;
    this.right = right;
    this.bottom = bottom;

    this.centerX = ((left + right) / 2.0D);
    this.centerY = ((top + bottom) / 2.0D);
  }

  public boolean a(double paramDouble1, double paramDouble2)
  {
    return (this.left <= paramDouble1) && (paramDouble1 <= this.right) && (this.top <= paramDouble2) && (paramDouble2 <= this.bottom);
  }

  public boolean a(DPoint paramDPoint)
  {
    return a(paramDPoint.x, paramDPoint.y);
  }

  public boolean a(double paramDouble1, double paramDouble2, double paramDouble3, double paramDouble4)
  {
    return (paramDouble1 < this.right) && (this.left < paramDouble2) && (paramDouble3 < this.bottom) && (this.top < paramDouble4);
  }

  public boolean a(Bounds paramay)
  {
    return a(paramay.left, paramay.right, paramay.top, paramay.bottom);
  }

  public boolean isContain(Bounds bounds)
  {
    return (bounds.left >= this.left) && (bounds.right <= this.right) && (bounds.top >= this.top) && (bounds.bottom <= this.bottom);
  }
}
