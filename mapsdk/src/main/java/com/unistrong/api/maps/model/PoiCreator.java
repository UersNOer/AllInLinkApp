package com.unistrong.api.maps.model;

import android.os.Parcel;
import android.os.Parcelable;

public class PoiCreator
  implements Parcelable.Creator<Poi>
{
  public Poi createFromParcel(Parcel paramParcel)
  {
    String str1 = paramParcel.readString();
    LatLng localLatLng = (LatLng)paramParcel.readParcelable(LatLng.class.getClassLoader());
    String str2 = paramParcel.readString();
    return new Poi(str1, localLatLng, str2);
  }
  
  public Poi[] newArray(int paramInt)
  {
    return new Poi[paramInt];
  }
}
