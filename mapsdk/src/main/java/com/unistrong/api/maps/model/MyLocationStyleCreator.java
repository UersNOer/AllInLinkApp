package com.unistrong.api.maps.model;

import android.os.Parcel;
import android.os.Parcelable;

public class MyLocationStyleCreator
  implements Parcelable.Creator<MyLocationStyle>
{
  public MyLocationStyle createFromParcel(Parcel paramParcel)
  {
    MyLocationStyle localMyLocationStyle = new MyLocationStyle();
    BitmapDescriptor localBitmapDescriptor = (BitmapDescriptor)paramParcel.readParcelable(BitmapDescriptor.class
      .getClassLoader());
    localMyLocationStyle.myLocationIcon(localBitmapDescriptor);
    localMyLocationStyle.anchor(paramParcel.readFloat(), paramParcel.readFloat());
    localMyLocationStyle.radiusFillColor(paramParcel.readInt());
    localMyLocationStyle.strokeColor(paramParcel.readInt());
    localMyLocationStyle.strokeWidth(paramParcel.readFloat());
    return localMyLocationStyle;
  }
  
  public MyLocationStyle[] newArray(int paramInt)
  {
    return new MyLocationStyle[paramInt];
  }
}
