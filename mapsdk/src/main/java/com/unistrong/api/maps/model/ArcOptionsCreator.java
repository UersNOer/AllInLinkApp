package com.unistrong.api.maps.model;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;

public class ArcOptionsCreator
  implements Parcelable.Creator<ArcOptions>
{
  public ArcOptions createFromParcel(Parcel paramParcel)
  {
    ArcOptions localArcOptions = new ArcOptions();
    Bundle localBundle = paramParcel.readBundle();
    LatLng localLatLng1 = new LatLng(localBundle.getDouble("startlat"), localBundle.getDouble("startlng"));
    LatLng localLatLng2 = new LatLng(localBundle.getDouble("passedlat"), localBundle.getDouble("passedlng"));
    LatLng localLatLng3 = new LatLng(localBundle.getDouble("endlat"), localBundle.getDouble("endlng"));
    localArcOptions.point(localLatLng1, localLatLng2, localLatLng3);
    localArcOptions.strokeWidth(paramParcel.readFloat());
    localArcOptions.strokeColor(paramParcel.readInt());
    localArcOptions.zIndex(paramParcel.readInt());
    localArcOptions.visible(paramParcel.readByte() == 1);
    localArcOptions.a = paramParcel.readString();
    return localArcOptions;
  }
  
  public ArcOptions[] newArray(int paramInt)
  {
    return new ArcOptions[paramInt];
  }
}
