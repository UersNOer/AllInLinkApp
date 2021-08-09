package com.unistrong.api.mapcore;

import android.graphics.Typeface;
import android.os.RemoteException;

public abstract interface ITextDelegate
  extends IMarkerDelegate
{
  public abstract void c(String paramString)
    throws RemoteException;
  
  public abstract String a()
    throws RemoteException;
  
  public abstract void b(int paramInt)
    throws RemoteException;
  
  public abstract int J()
    throws RemoteException;
  
  public abstract void c(int paramInt)
    throws RemoteException;
  
  public abstract int K()
    throws RemoteException;
  
  public abstract void d(int paramInt)
    throws RemoteException;
  
  public abstract int L()
    throws RemoteException;
  
  public abstract void a(Typeface paramTypeface)
    throws RemoteException;
  
  public abstract Typeface M()
    throws RemoteException;
  
  public abstract void b(int paramInt1, int paramInt2)
    throws RemoteException;
  
  public abstract int N()
    throws RemoteException;
  
  public abstract int O()
    throws RemoteException;
}
