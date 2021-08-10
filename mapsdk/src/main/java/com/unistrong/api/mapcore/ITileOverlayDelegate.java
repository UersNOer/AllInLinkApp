package com.unistrong.api.mapcore;

import javax.microedition.khronos.opengles.GL10;

public abstract interface ITileOverlayDelegate
{
  public abstract void a();
  
  public abstract void clearTileCache();
  
  public abstract String getId();
  
  public abstract void setZIndex(float paramFloat);
  
  public abstract float getZIndex();
  
  public abstract void setVisible(boolean paramBoolean);
  
  public abstract boolean isVisible();
  
  public abstract boolean equalsRemote(ITileOverlayDelegate paramao); // a
  
  public abstract int hashCodeRemote();
  
  public abstract void drawTiles(GL10 paramGL10); // a
  
  public abstract void refresh(boolean paramBoolean);
  
  public abstract void onPause();
  
  public abstract void onResume();
  
  public abstract void onFling(boolean paramBoolean);
}
