package com.unistrong.api.maps.model;

import android.os.Parcel;
import android.os.Parcelable;
import java.util.ArrayList;

public class MarkerOptionsCreator
  implements Parcelable.Creator<MarkerOptions>
{
  public MarkerOptions createFromParcel(Parcel paramParcel)
  {
    MarkerOptions localMarkerOptions = new MarkerOptions();
    LatLng localLatLng = (LatLng)paramParcel.readParcelable(LatLng.class.getClassLoader());
    localMarkerOptions.position(localLatLng);
    BitmapDescriptor localBitmapDescriptor = (BitmapDescriptor)paramParcel.readParcelable(BitmapDescriptor.class
      .getClassLoader());
    localMarkerOptions.icon(localBitmapDescriptor);
    localMarkerOptions.title(paramParcel.readString());
    localMarkerOptions.snippet(paramParcel.readString());
    localMarkerOptions.anchor(paramParcel.readFloat(), paramParcel.readFloat());
    localMarkerOptions.setInfoWindowOffset(paramParcel.readInt(), paramParcel.readInt());
    boolean[] arrayOfBoolean = new boolean[4];
    paramParcel.readBooleanArray(arrayOfBoolean);
    localMarkerOptions.visible(arrayOfBoolean[0]);
    localMarkerOptions.draggable(arrayOfBoolean[1]);
    localMarkerOptions.setGps(arrayOfBoolean[2]);
    localMarkerOptions.setFlat(arrayOfBoolean[3]);
    localMarkerOptions.id = paramParcel.readString();
    localMarkerOptions.period(paramParcel.readInt());
    ArrayList localArrayList = paramParcel.readArrayList(BitmapDescriptor.class
      .getClassLoader());
    localMarkerOptions.icons(localArrayList);
    localMarkerOptions.zIndex(paramParcel.readFloat());
    return localMarkerOptions;
  }
  
  public MarkerOptions[] newArray(int paramInt)
  {
    return new MarkerOptions[paramInt];
  }
}
