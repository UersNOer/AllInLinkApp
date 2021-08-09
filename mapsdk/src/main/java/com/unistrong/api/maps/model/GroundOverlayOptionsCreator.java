package com.unistrong.api.maps.model;

import android.os.Parcel;
import android.os.Parcelable;

public class GroundOverlayOptionsCreator
  implements Parcelable.Creator<GroundOverlayOptions>
{
  public static final int CONTENT_DESCRIPTION = 0;
  
  public GroundOverlayOptions createFromParcel(Parcel paramParcel)
  {
    int i = paramParcel.readInt();
    BitmapDescriptor localBitmapDescriptor = (BitmapDescriptor)paramParcel.readParcelable(BitmapDescriptor.class.getClassLoader());
    LatLng localLatLng = (LatLng)paramParcel.readParcelable(LatLng.class.getClassLoader());
    float f1 = paramParcel.readFloat();
    float f2 = paramParcel.readFloat();
    LatLngBounds localLatLngBounds = (LatLngBounds)paramParcel.readParcelable(LatLngBounds.class.getClassLoader());
    float f3 = paramParcel.readFloat();
    float f4 = paramParcel.readFloat();
    boolean bool = paramParcel.readByte() != 0;
    float f5 = paramParcel.readFloat();
    float f6 = paramParcel.readFloat();
    float f7 = paramParcel.readFloat();
    GroundOverlayOptions localGroundOverlayOptions = new GroundOverlayOptions(i, null, localLatLng, f1, f2, localLatLngBounds, f3, f4, bool, f5, f6, f7);
    
    localGroundOverlayOptions.image(localBitmapDescriptor);
    return localGroundOverlayOptions;
  }
  
  public GroundOverlayOptions[] newArray(int paramInt)
  {
    return new GroundOverlayOptions[paramInt];
  }
}
