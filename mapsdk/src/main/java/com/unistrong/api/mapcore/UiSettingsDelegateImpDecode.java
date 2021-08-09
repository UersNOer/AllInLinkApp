package com.unistrong.api.mapcore;

import android.os.Handler;
import android.os.Message;
import android.os.RemoteException;

class UiSettingsDelegateImpDecode//bo
  implements IUiSettingsDelegate
{
  private IMapDelegate map; //b
  private boolean isRotateGesturesEnabled = true; //c
  private boolean isScrollGesturesEnabled = true; // d
  private boolean isTiltGesturesEnabled = true; //e
  private boolean isMyLocationButtonEnabled = false; //f
  private boolean isZoomGesturesEnabled = true; //g
  private boolean isZoomEnabled = true; //h
  private boolean isCompassEnabled = true; //i
  private boolean isScaleEnabled = false; //j
  private int logoPosition = 0; //k
  private int zoomPosition = 1; //l
  
  private static final int ZOOMCONTROLS = 0;
	private static final int SCALE = 1;
	private static final int COMPASS = 2;
	private static final int MYLOCATION = 3;
	
  final Handler handler = new Handler(){
	  public void handleMessage(Message paramMessage)
	  {
	    if (paramMessage == null) {
	      return;
	    }
	    switch (paramMessage.what)
	    {
	    case ZOOMCONTROLS:
          map.showZoomControlsEnabled(isZoomEnabled);
	      break;
	    case SCALE:
          map.showScaleEnabled(isScaleEnabled);
	      break;
	    case COMPASS:
          map.showCompassEnabled(isCompassEnabled);
	      break;
	    case MYLOCATION:
          map.showMyLocationButtonEnabled(isMyLocationButtonEnabled);
	      break;
	    }
	  } 
  };
 

  
  UiSettingsDelegateImpDecode(IMapDelegate map)
  {
    this.map = map;
  }
  
  public void setScaleControlsEnabled(boolean paramBoolean)
    throws RemoteException
  {
    this.isScaleEnabled = paramBoolean;
    this.handler.obtainMessage(SCALE).sendToTarget();
  }
  
  public void setZoomControlsEnabled(boolean paramBoolean)
    throws RemoteException
  {
    this.isZoomEnabled = paramBoolean;
    this.handler.obtainMessage(ZOOMCONTROLS).sendToTarget();
  }
  
  public void setCompassEnabled(boolean enable)
    throws RemoteException
  {
    this.isCompassEnabled = enable;
    this.handler.obtainMessage(COMPASS).sendToTarget();
  }
  
  public void setMyLocationButtonEnabled(boolean paramBoolean)
    throws RemoteException
  {
    this.isMyLocationButtonEnabled = paramBoolean;
    this.handler.obtainMessage(MYLOCATION).sendToTarget();
  }
  
  public void setScrollGesturesEnabled(boolean paramBoolean)
    throws RemoteException
  {
    this.isScrollGesturesEnabled = paramBoolean;
  }
  
  public void setZoomGesturesEnabled(boolean paramBoolean)
    throws RemoteException
  {
    this.isZoomGesturesEnabled = paramBoolean;
  }
  
  public void setTiltGesturesEnabled(boolean paramBoolean)
    throws RemoteException
  {
    this.isTiltGesturesEnabled = paramBoolean;
  }
  
  public void setRotateGesturesEnabled(boolean paramBoolean)
    throws RemoteException
  {
    this.isRotateGesturesEnabled = paramBoolean;
  }
  
  public void setAllGesturesEnabled(boolean paramBoolean)
    throws RemoteException
  {
    setRotateGesturesEnabled(paramBoolean);
    setTiltGesturesEnabled(paramBoolean);
    setZoomGesturesEnabled(paramBoolean);
    setScrollGesturesEnabled(paramBoolean);
  }
  
  public void setLogoPosition(int paramInt)
    throws RemoteException
  {
    this.logoPosition = paramInt;
    this.map.setLogoPosition(paramInt);
  }
  
  public void setZoomPosition(int paramInt)
    throws RemoteException
  {
    this.zoomPosition = paramInt;
    this.map.setZoomPosition(paramInt);
  }
  public void setCompassViewPosition(int xPix,int yPix)
    throws RemoteException
  {
    this.map.setCompassViewPosition(xPix,yPix);
  }
  public boolean isScaleControlsEnabled()
    throws RemoteException
  {
    return this.isScaleEnabled;
  }
  
  public boolean isZoomControlsEnabled()
    throws RemoteException
  {
    return this.isZoomEnabled;
  }
  
  public boolean isCompassEnabled()
    throws RemoteException
  {
    return this.isCompassEnabled;
  }
  
  public boolean isMyLocationButtonEnabled()
    throws RemoteException
  {
    return this.isMyLocationButtonEnabled;
  }
  
  public boolean isScrollGesturesEnabled()
    throws RemoteException
  {
    return this.isScrollGesturesEnabled;
  }
  
  public boolean isZoomGesturesEnabled()
    throws RemoteException
  {
    return this.isZoomGesturesEnabled;
  }
  
  public boolean isTiltGesturesEnabled()
    throws RemoteException
  {
    return this.isTiltGesturesEnabled;
  }
  
  public boolean isRotateGesturesEnabled()
    throws RemoteException
  {
    return this.isRotateGesturesEnabled;
  }
  
  public int getLogoPosition()
    throws RemoteException
  {
    return this.logoPosition;
  }
  
  public int getZoomPosition()
    throws RemoteException
  {
    return this.zoomPosition;
  }

}
