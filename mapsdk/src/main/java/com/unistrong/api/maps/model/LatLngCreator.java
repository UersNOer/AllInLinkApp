package com.unistrong.api.maps.model;

import android.os.Parcel;
import android.os.Parcelable;

public class LatLngCreator
  implements Parcelable.Creator<LatLng>
{
  public static final int CONTENT_DESCRIPTION = 0;
  
  public LatLng createFromParcel(Parcel paramParcel)
  {
    double d1 = paramParcel.readDouble();
    double d2 = paramParcel.readDouble();
    return new LatLng(d2, d1);
  }
  
  public LatLng[] newArray(int paramInt)
  {
    return new LatLng[paramInt];
  }
}
