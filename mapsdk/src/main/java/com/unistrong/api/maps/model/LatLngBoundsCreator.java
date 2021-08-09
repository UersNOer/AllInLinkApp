package com.unistrong.api.maps.model;

import android.os.BadParcelableException;
import android.os.Parcel;
import android.os.Parcelable;

public class LatLngBoundsCreator
  implements Parcelable.Creator<LatLngBounds>
{
  public static final int CONTENT_DESCRIPTION = 0;
  
  public LatLngBounds createFromParcel(Parcel paramParcel)
  {
    int i = 0;
    LatLng localLatLng1 = null;
    LatLng localLatLng2 = null;
    i = paramParcel.readInt();
    try
    {
      localLatLng1 = (LatLng)paramParcel.readParcelable(LatLngBounds.class.getClassLoader());
      localLatLng2 = (LatLng)paramParcel.readParcelable(LatLngBounds.class.getClassLoader());
    }
    catch (BadParcelableException localBadParcelableException)
    {
      localBadParcelableException.printStackTrace();
    }
    LatLngBounds localLatLngBounds = new LatLngBounds(i, localLatLng1, localLatLng2);
    
    return localLatLngBounds;
  }
  
  public LatLngBounds[] newArray(int paramInt)
  {
    return new LatLngBounds[paramInt];
  }
  
  static void a(LatLngBounds paramLatLngBounds, Parcel paramParcel, int paramInt)
  {
    paramParcel.writeInt(paramLatLngBounds.a());
    paramParcel.writeParcelable(paramLatLngBounds.southwest, paramInt);
    paramParcel.writeParcelable(paramLatLngBounds.northeast, paramInt);
  }
}
