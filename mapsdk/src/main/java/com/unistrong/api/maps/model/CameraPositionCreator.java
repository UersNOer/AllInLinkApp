package com.unistrong.api.maps.model;

import android.os.Parcel;
import android.os.Parcelable;

public class CameraPositionCreator
  implements Parcelable.Creator<CameraPosition>
{
  public CameraPosition createFromParcel(Parcel paramParcel)
  {
    float f1 = paramParcel.readFloat();
    float f2 = paramParcel.readFloat();
    float f3 = paramParcel.readFloat();
    float f4 = paramParcel.readFloat();
    float f5 = paramParcel.readFloat();
    return new CameraPosition(new LatLng(f2, f3), f5, f4, f1);
  }
  
  public CameraPosition[] newArray(int paramInt)
  {
    return new CameraPosition[paramInt];
  }
}
