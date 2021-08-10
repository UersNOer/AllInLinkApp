package com.unistrong.api.mapcore.util;

import android.content.Context;
import android.view.MotionEvent;
import java.lang.reflect.Method;

public class MultiTouchSupportDecode
{
	public static final int ACTION_MASK = 255;
	public static final int ACTION_POINTER_ID_SHIFT = 8;
	public static final int ACTION_POINTER_DOWN = 5;
	public static final int ACTION_POINTER_UP = 6;
	
  private boolean multiTouchAPISupported = false;
  private final MultiTouchDragListener listener;
  protected Method getPointerCount;
  protected Method getX;
  protected Method getY;
  private long mUpTime = 0L;
  
  public MultiTouchSupportDecode(Context paramContext, MultiTouchDragListener listener)
  {
    this.listener = listener;
    initMethods();
  }
  
  public boolean isDragMode()
  {
    return this.inDragMode;
  }
  
  public long getUpdateTime()
  {
    return this.mUpTime;
  }
  
  private void initMethods()
  {
    try
    {
      this.getPointerCount = MotionEvent.class.getMethod("getPointerCount", new Class[0]);
      
      this.getX = MotionEvent.class.getMethod("getX", new Class[] { Integer.TYPE });
      this.getY = MotionEvent.class.getMethod("getY", new Class[] { Integer.TYPE });
      this.multiTouchAPISupported = true;
    }
    catch (Throwable localThrowable)
    {
      this.multiTouchAPISupported = false;
      localThrowable.printStackTrace();
    }
  }
  
  private boolean inDragMode = false;
  
  public boolean onTouchEvent(MotionEvent paramMotionEvent)
  {
    if (!this.multiTouchAPISupported) {
      return false;
    }
    int actionCode = paramMotionEvent.getAction() & ACTION_MASK;
    try
    {
      Integer localInteger = (Integer)this.getPointerCount.invoke(paramMotionEvent, new Object[0]);
      if (localInteger.intValue() < 2)
      {
        this.mUpTime = 0L;
        this.inDragMode = false;
        return false;
      }
      Float localFloat1 = (Float)this.getX.invoke(paramMotionEvent, new Object[] { Integer.valueOf(0) });
      Float localFloat2 = (Float)this.getX.invoke(paramMotionEvent, new Object[] { Integer.valueOf(1) });
      Float localFloat3 = (Float)this.getY.invoke(paramMotionEvent, new Object[] { Integer.valueOf(0) });
      Float localFloat4 = (Float)this.getY.invoke(paramMotionEvent, new Object[] { Integer.valueOf(1) });
      float f1 = (float)Math.sqrt((localFloat2.floatValue() - localFloat1.floatValue()) * (localFloat2.floatValue() - localFloat1.floatValue()) + (localFloat4.floatValue() - localFloat3.floatValue()) * (localFloat4
        .floatValue() - localFloat3.floatValue()));
      if (actionCode == ACTION_POINTER_DOWN)
      {
        this.listener.onMultiTouchGestureInit(f1, localFloat1.floatValue(), localFloat3.floatValue(), localFloat2.floatValue(), localFloat4.floatValue());
        this.inDragMode = true;
        return true;
      }
      if (actionCode == ACTION_POINTER_UP)
      {
        this.mUpTime = paramMotionEvent.getEventTime();
        if ((paramMotionEvent.getPointerCount() == 2) && (this.mUpTime - paramMotionEvent.getDownTime() < 100L)) {
          this.listener.onMultiTouchSingleTap();
        }
        if (this.inDragMode) {
          this.inDragMode = false;
        }
        return false;
      }
      if ((this.inDragMode) && (actionCode == MotionEvent.ACTION_MOVE)) {
        return this.listener.onMultiTouchDraging(paramMotionEvent, localFloat1.floatValue(), localFloat3.floatValue(), localFloat2.floatValue(), localFloat4.floatValue());
      }
    }
    catch (Throwable localThrowable)
    {
      localThrowable.printStackTrace();
    }
    return false;
  }
  
  public static abstract interface MultiTouchDragListener
  {
    public abstract boolean onMultiTouchDraging(MotionEvent paramMotionEvent, float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4);
    
    public abstract void onMultiTouchGestureInit(float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4, float paramFloat5);
    
    public abstract void onMultiTouchSingleTap();
  }
}
