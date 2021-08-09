package com.unistrong.api.maps;

import android.os.Parcel;
import android.os.Parcelable;
import com.unistrong.api.maps.model.CameraPosition;

public class MapOptionsCreator
  implements Parcelable.Creator<MapOptions>
{
  public MapOptions createFromParcel(Parcel parcel)
  {
    MapOptions mapOptions = new MapOptions();
    
    CameraPosition localCameraPosition = (CameraPosition)parcel.readParcelable(CameraPosition.class.getClassLoader());
    mapOptions.mapType(parcel.readInt());
    mapOptions.camera(localCameraPosition);
    boolean[] arrayOfBoolean = parcel.createBooleanArray();
    if ((arrayOfBoolean != null) && (arrayOfBoolean.length >= 6))
    {
      mapOptions.rotateGesturesEnabled(arrayOfBoolean[0]);
      mapOptions.scrollGesturesEnabled(arrayOfBoolean[1]);
      mapOptions.tiltGesturesEnabled(arrayOfBoolean[2]);
      mapOptions.zoomGesturesEnabled(arrayOfBoolean[3]);
      mapOptions.zoomControlsEnabled(arrayOfBoolean[4]);
      
      mapOptions.zOrderOnTop(arrayOfBoolean[5]);
      mapOptions.compassEnabled(arrayOfBoolean[6]);
      mapOptions.scaleControlsEnabled(arrayOfBoolean[7]);
    }
    return mapOptions;
  }
  
  public MapOptions[] newArray(int paramInt)
  {
    return new MapOptions[paramInt];
  }
}
