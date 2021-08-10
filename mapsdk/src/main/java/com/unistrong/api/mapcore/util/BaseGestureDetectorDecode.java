package com.unistrong.api.mapcore.util;

import android.content.Context;
import android.view.MotionEvent;

public abstract class BaseGestureDetectorDecode
{
  protected final Context mContext; // a
  protected boolean mGestureInProgress;
  protected MotionEvent mPrevEvent;
  protected MotionEvent mCurrEvent;
  protected float mCurrPressure;
  protected float mPrevPressure;
  protected long mTimeDelta;
  

  
  public BaseGestureDetectorDecode(Context context)
  {
    this.mContext = context;
  }
  
  public boolean onTouchEvent(MotionEvent event)
  {
    int actionCode = event.getAction() & MotionEvent.ACTION_MASK;
    if (!this.mGestureInProgress) {
      handleStartProgressEvent(actionCode, event);
    } else {
      handleInProgressEvent(actionCode, event);
    }
    return true;
  }
  
  protected abstract void handleStartProgressEvent(int actionCode, MotionEvent event);
  
  protected abstract void handleInProgressEvent(int action, MotionEvent event);
  
  
  protected void updateStateByEvent(MotionEvent paramMotionEvent)
  {
    if (this.mPrevEvent == null) {
      return;
    }
    MotionEvent localMotionEvent = this.mPrevEvent;
    if (this.mCurrEvent != null)
    {
      this.mCurrEvent.recycle();
      this.mCurrEvent = null;
    }
    this.mCurrEvent = MotionEvent.obtain(paramMotionEvent);
    
    this.mTimeDelta = (paramMotionEvent.getEventTime() - localMotionEvent.getEventTime());
    
    this.mCurrPressure = paramMotionEvent.getPressure(getActionIndex(paramMotionEvent));
    this.mPrevPressure = localMotionEvent.getPressure(getActionIndex(localMotionEvent));
  }
  
  
  public static final int ACTION_POINTER_INDEX_MASK  = 0xff00;
  public static final int ACTION_POINTER_INDEX_SHIFT = 8;
  
  public final int getActionIndex(MotionEvent paramMotionEvent)
  {
    return (paramMotionEvent.getAction() & ACTION_POINTER_INDEX_MASK) >> ACTION_POINTER_INDEX_SHIFT;
  }
  
  protected void resetState()
  {
    if (this.mPrevEvent != null)
    {
      this.mPrevEvent.recycle();
      this.mPrevEvent = null;
    }
    if (this.mCurrEvent != null)
    {
      this.mCurrEvent.recycle();
      this.mCurrEvent = null;
    }
    this.mGestureInProgress = false;
  }
}
