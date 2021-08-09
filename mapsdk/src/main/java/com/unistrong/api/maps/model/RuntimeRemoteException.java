package com.unistrong.api.maps.model;

import android.os.RemoteException;

public final class RuntimeRemoteException
  extends RuntimeException
{
  public RuntimeRemoteException(RemoteException e)
  {
    super(e);
  }
}
