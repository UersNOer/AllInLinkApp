package com.unistrong.api.mapcore;

import android.location.Location;
import android.os.RemoteException;

import com.unistrong.api.maps.LocationSource;

class MapOnLocationChangedListener
  implements LocationSource.OnLocationChangedListener
{
  private IMapDelegate map; //b
  Location location; // a
  
  MapOnLocationChangedListener(IMapDelegate map)
  {
    this.map = map;
  }
  
  public void onLocationChanged(Location location)
  {
    this.location = location;
    try
    {
      if (this.map.isMyLocationEnabled()) {
        this.map.showMyLocationOverlay(location);
      }
    }
    catch (RemoteException localRemoteException)
    {
//      SDKLogHandler.exception(localRemoteException, "MapOnLocationChangedListener", "onLocationChanged");
      localRemoteException.printStackTrace();
    }
  }
}
