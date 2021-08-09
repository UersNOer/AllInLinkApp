package com.unistrong.api.mapcore.util;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.ViewConfiguration;

public abstract class TwoFingerGestureDetectorDecode
  extends BaseGestureDetectorDecode
{
  private final float l;
  private float m;
  private float n;
  protected float h;
  protected float i;
  protected float j;
  protected float k;
  private float o;
  private float p;
  
  public TwoFingerGestureDetectorDecode(Context paramContext)
  {
    super(paramContext);
    
    ViewConfiguration localViewConfiguration = ViewConfiguration.get(paramContext);
    this.l = localViewConfiguration.getScaledEdgeSlop();
  }
  
  protected void updateStateByEvent(MotionEvent paramMotionEvent)
  {
    super.updateStateByEvent(paramMotionEvent);
    if (this.mPrevEvent == null) {
      return;
    }
    MotionEvent localMotionEvent = this.mPrevEvent;
    
    this.o = -1.0F;
    this.p = -1.0F;
    
    float f1 = localMotionEvent.getX(0);
    float f2 = localMotionEvent.getY(0);
    float f3 = localMotionEvent.getX(1);
    float f4 = localMotionEvent.getY(1);
    float f5 = f3 - f1;
    float f6 = f4 - f2;
    this.h = f5;
    this.i = f6;
    
    float f7 = paramMotionEvent.getX(0);
    float f8 = paramMotionEvent.getY(0);
    float f9 = paramMotionEvent.getX(1);
    float f10 = paramMotionEvent.getY(1);
    float f11 = f9 - f7;
    float f12 = f10 - f8;
    this.j = f11;
    this.k = f12;
  }
  
  public float c()
  {
    if (this.o == -1.0F)
    {
      float f1 = this.j;
      float f2 = this.k;
      this.o = ((float)Math.sqrt(f1 * f1 + f2 * f2));
    }
    return this.o;
  }
  
  protected static float a(MotionEvent paramMotionEvent, int paramInt)
  {
    float f = paramMotionEvent.getRawX() - paramMotionEvent.getX();
    if (paramInt < paramMotionEvent.getPointerCount()) {
      return paramMotionEvent.getX(paramInt) + f;
    }
    return 0.0F;
  }
  
  protected static float b(MotionEvent paramMotionEvent, int paramInt)
  {
    float f = paramMotionEvent.getRawY() - paramMotionEvent.getY();
    if (paramInt < paramMotionEvent.getPointerCount()) {
      return paramMotionEvent.getY(paramInt) + f;
    }
    return 0.0F;
  }
  
  protected boolean d(MotionEvent paramMotionEvent)
  {
    DisplayMetrics localDisplayMetrics = this.mContext.getResources().getDisplayMetrics();
    this.m = (localDisplayMetrics.widthPixels - this.l);
    this.n = (localDisplayMetrics.heightPixels - this.l);
    
    float f1 = this.l;
    float f2 = this.m;
    float f3 = this.n;
    
    float f4 = paramMotionEvent.getRawX();
    float f5 = paramMotionEvent.getRawY();
    float f6 = a(paramMotionEvent, 1);
    float f7 = b(paramMotionEvent, 1);
    
    int i1 = (f4 < f1) || (f5 < f1) || (f4 > f2) || (f5 > f3) ? 1 : 0;
    
    int i2 = (f6 < f1) || (f7 < f1) || (f6 > f2) || (f7 > f3) ? 1 : 0;
    if ((i1 != 0) || (i2 != 0)) {
      return true;
    }
    return false;
  }
}
