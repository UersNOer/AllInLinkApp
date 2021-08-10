package com.leador.mapcore;

import com.leador.mapcore.MapCore;

import javax.microedition.khronos.opengles.GL10;

public abstract interface IMapCallback
{
  public abstract void OnMapSurfaceCreate(GL10 gl10, MapCore paramMapCore);

  public abstract void OnMapSurfaceRenderer(GL10 gl10, MapCore mapCore, int paramInt);

  public abstract void OnMapSufaceChanged(GL10 gl, MapCore mapCore, int paramInt1, int paramInt2);

  public abstract void OnMapProcessEvent(MapCore mapCore);

  public abstract void OnMapDestory(GL10 gl10, MapCore mapCore);

  public abstract void OnMapDataRequired(MapCore mapCore, int paramInt, String[] paramArrayOfString);

  public abstract void OnMapLabelsRequired(MapCore mapCore, int[] paramArrayOfInt, int paramInt);

  public abstract byte[] OnMapCharsWidthsRequired(MapCore mapCore, int[] paramArrayOfInt, int paramInt1, int paramInt2);

  public abstract void OnMapReferencechanged(MapCore mapCore, String paramString1, String paramString2);

  public abstract void requestRender();

  public abstract void onIndoorBuildingActivity(MapCore mapCore, byte[] paramArrayOfByte);

  public abstract void onIndoorDataRequired(MapCore mapCore, int paramInt, String[] paramArrayOfString, int[] paramArrayOfInt1, int[] paramArrayOfInt2);
}
