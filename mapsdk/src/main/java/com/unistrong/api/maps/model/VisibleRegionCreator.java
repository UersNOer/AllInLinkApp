package com.unistrong.api.maps.model;

import android.os.BadParcelableException;
import android.os.Parcel;
import android.os.Parcelable;

public class VisibleRegionCreator
  implements Parcelable.Creator<VisibleRegion>
{
  public static final int CONTENT_DESCRIPTION = 0;
  
  public VisibleRegion createFromParcel(Parcel paramParcel)
  {
    int i = 0;
    LatLng localLatLng1 = null;
    LatLng localLatLng2 = null;
    LatLng localLatLng3 = null;
    LatLng localLatLng4 = null;
    LatLngBounds localLatLngBounds = null;
    i = paramParcel.readInt();
    try
    {
      localLatLng1 = (LatLng)paramParcel.readParcelable(LatLng.class.getClassLoader());
      localLatLng2 = (LatLng)paramParcel.readParcelable(LatLng.class.getClassLoader());
      localLatLng3 = (LatLng)paramParcel.readParcelable(LatLng.class.getClassLoader());
      localLatLng4 = (LatLng)paramParcel.readParcelable(LatLng.class.getClassLoader());
      localLatLngBounds = (LatLngBounds)paramParcel.readParcelable(LatLngBounds.class
        .getClassLoader());
    }
    catch (BadParcelableException localBadParcelableException)
    {
      localBadParcelableException.printStackTrace();
    }
    return new VisibleRegion(i, localLatLng1, localLatLng2, localLatLng3, localLatLng4, localLatLngBounds);
  }
  
  public VisibleRegion[] newArray(int paramInt)
  {
    return new VisibleRegion[paramInt];
  }
  
  static void a(VisibleRegion paramVisibleRegion, Parcel paramParcel, int paramInt)
  {
    paramParcel.writeInt(paramVisibleRegion.a());
    paramParcel.writeParcelable(paramVisibleRegion.nearLeft, paramInt);
    paramParcel.writeParcelable(paramVisibleRegion.nearRight, paramInt);
    paramParcel.writeParcelable(paramVisibleRegion.farLeft, paramInt);
    paramParcel.writeParcelable(paramVisibleRegion.farRight, paramInt);
    paramParcel.writeParcelable(paramVisibleRegion.latLngBounds, paramInt);
  }
}
