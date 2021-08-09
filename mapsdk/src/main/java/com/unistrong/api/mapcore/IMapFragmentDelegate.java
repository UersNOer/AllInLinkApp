package com.unistrong.api.mapcore;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.RemoteException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.unistrong.api.maps.UnistrongException;
import com.unistrong.api.maps.MapOptions;

public abstract interface IMapFragmentDelegate
{
  public abstract IMapDelegate getMapDelegate()
    throws RemoteException, UnistrongException;

  public abstract void onInflate(Activity paramActivity, MapOptions mapOptions, Bundle paramBundle)
    throws RemoteException;
  
  public abstract void setContext(Context context);
  
  public abstract void setMapOptions(MapOptions mapOptions);
  
  public abstract void onCreate(Bundle paramBundle)
    throws RemoteException;
  
  public abstract View onCreateView(LayoutInflater paramLayoutInflater, ViewGroup paramViewGroup, Bundle paramBundle)
    throws RemoteException, UnistrongException;
  
  public abstract void onResume()
    throws RemoteException;
  
  public abstract void onPause()
    throws RemoteException;
  
  public abstract void onDestroyView()
    throws RemoteException;
  
  public abstract void onDestroy()
    throws RemoteException;
  
  public abstract void onLowMemory()
    throws RemoteException;
  
  public abstract void onSaveInstanceState(Bundle paramBundle)
    throws RemoteException;
  
  public abstract void setVisible(int visible);
}
