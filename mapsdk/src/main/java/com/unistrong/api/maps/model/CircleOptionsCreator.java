package com.unistrong.api.maps.model;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;

public class CircleOptionsCreator
  implements Parcelable.Creator<CircleOptions>
{
  public CircleOptions createFromParcel(Parcel paramParcel)
  {
    CircleOptions localCircleOptions = new CircleOptions();
    Bundle localBundle = paramParcel.readBundle();
    LatLng localLatLng = new LatLng(localBundle.getDouble("lat"), localBundle.getDouble("lng"));
    localCircleOptions.center(localLatLng);
    localCircleOptions.radius(paramParcel.readDouble());
    localCircleOptions.strokeWidth(paramParcel.readFloat());
    localCircleOptions.strokeColor(paramParcel.readInt());
    localCircleOptions.fillColor(paramParcel.readInt());
    localCircleOptions.zIndex(paramParcel.readInt());
    localCircleOptions.visible(paramParcel.readByte() == 1);
    localCircleOptions.a = paramParcel.readString();
    return localCircleOptions;
  }
  
  public CircleOptions[] newArray(int paramInt)
  {
    return new CircleOptions[paramInt];
  }
}
