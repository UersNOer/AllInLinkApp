package com.unistrong.api.maps.overlay;

import com.unistrong.api.maps.model.LatLng;
import com.unistrong.api.services.core.LatLonPoint;

import java.util.ArrayList;
import java.util.List;

class MapServicesUtil
{
  public static int a = 2048;
  
  public static LatLng toLatLng(LatLonPoint paramLatLonPoint)
  {
    return new LatLng(paramLatLonPoint.getLatitude(), paramLatLonPoint.getLongitude());
  }
  
  public static ArrayList<LatLng> clone(List<LatLonPoint> paramList)
  {
    ArrayList localArrayList = new ArrayList();
    for (LatLonPoint localLatLonPoint : paramList)
    {
      LatLng localLatLng = toLatLng(localLatLonPoint);
      localArrayList.add(localLatLng);
    }
    return localArrayList;
  }
}
