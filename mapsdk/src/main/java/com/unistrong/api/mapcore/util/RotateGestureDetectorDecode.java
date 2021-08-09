package com.unistrong.api.mapcore.util;

import android.content.Context;
import android.view.MotionEvent;

public class RotateGestureDetectorDecode
  extends TwoFingerGestureDetectorDecode
{
  private final OnRotateGestureListener mListener;
  private boolean mSloppyGesture;
  
  public RotateGestureDetectorDecode(Context context, OnRotateGestureListener listener)
  {
    super(context);
    this.mListener = listener;
  }
  
  protected void handleStartProgressEvent(int paramInt, MotionEvent paramMotionEvent)
  {
    switch (paramInt)
    {
    case 5: 
      resetState();
      this.mPrevEvent = MotionEvent.obtain(paramMotionEvent);
      this.mTimeDelta = 0L;
      
      updateStateByEvent(paramMotionEvent);
      
      this.mSloppyGesture = d(paramMotionEvent);
      if (!this.mSloppyGesture) {
        this.mGestureInProgress = this.mListener.onRotateBegin(this);
      }
      break;
    case 2: 
      if (this.mSloppyGesture)
      {
        this.mSloppyGesture = d(paramMotionEvent);
        if (!this.mSloppyGesture) {
          this.mGestureInProgress = this.mListener.onRotateBegin(this);
        }
      }
      break;
    case 6: 
      if (!this.mSloppyGesture) {
        break;
      }
    }
  }
  
  protected void handleInProgressEvent(int paramInt, MotionEvent paramMotionEvent)
  {
    switch (paramInt)
    {
    case 6: 
      updateStateByEvent(paramMotionEvent);
      if (!this.mSloppyGesture) {
        this.mListener.onRotateEnd(this);
      }
      resetState();
      break;
    case 3: 
      if (!this.mSloppyGesture) {
        this.mListener.onRotateEnd(this);
      }
      resetState();
      break;
    case 2: 
      updateStateByEvent(paramMotionEvent);
      if (this.mCurrPressure / this.mPrevPressure > 0.67F)
      {
        boolean bool = this.mListener.onRotate(this);
        if ((bool) && (this.mPrevEvent != null))
        {
          this.mPrevEvent.recycle();
          this.mPrevEvent = MotionEvent.obtain(paramMotionEvent);
        }
      }
      break;
    }
  }
  
  protected void resetState()
  {
    super.resetState();
    this.mSloppyGesture = false;
  }
  
  public float getRotationDegreesDelta()
  {
    double d = Math.atan2(this.i, this.h) - Math.atan2(this.k, this.j);
    return (float)(d * 180.0D / 3.141592653589793D);
  }
  
  /*
	 * Listener which must be implemented which is used by RotateGestureDetector
	 * to perform callbacks to any implementing class which is registered to a
	 * RotateGestureDetector via the constructor.
	 * 
	 * @see RotateGestureDetector.SimpleOnRotateGestureListener
	 */
  public static abstract interface OnRotateGestureListener
  {
    public abstract boolean onRotate(RotateGestureDetectorDecode paramd);
    
    public abstract boolean onRotateBegin(RotateGestureDetectorDecode paramd);
    
    public abstract void onRotateEnd(RotateGestureDetectorDecode paramd);
  }
}
