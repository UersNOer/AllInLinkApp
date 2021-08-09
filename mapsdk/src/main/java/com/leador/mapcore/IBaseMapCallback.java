package com.leador.mapcore;

import android.content.Context;

public abstract interface IBaseMapCallback
{
  public abstract Context getContext();

  public abstract boolean isMapEngineValid();

  public abstract void OnMapLoaderError(int paramInt);

  public abstract String getMapSvrAddress();

  public abstract String getUserGridURL(String mesh);
}
