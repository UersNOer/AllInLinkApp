package com.unistrong.api.mapcore;

import android.opengl.GLSurfaceView;

public abstract interface IGLSurfaceView
{
  public abstract void setBackgroundColor(int paramInt);
  
  public abstract void setRenderer(GLSurfaceView.Renderer paramRenderer);
  
  public abstract int getWidth();
  
  public abstract int getHeight();
  
  public abstract void setRenderMode(int paramInt);
  
  public abstract void setZOrderOnTop(boolean paramBoolean);
  
  public abstract void queueEvent(Runnable paramRunnable);
  
  public abstract boolean isEnabled();
  
  public abstract void setVisibility(int visibility);
  
  public abstract void requestRender();
}
