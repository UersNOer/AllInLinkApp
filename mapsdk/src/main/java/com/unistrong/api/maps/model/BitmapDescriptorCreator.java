package com.unistrong.api.maps.model;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;

public class BitmapDescriptorCreator
  implements Parcelable.Creator<BitmapDescriptor>
{
  public BitmapDescriptor createFromParcel(Parcel paramParcel)
  {
    BitmapDescriptor localBitmapDescriptor = new BitmapDescriptor(null);
    localBitmapDescriptor.icon = ((Bitmap)paramParcel.readParcelable(BitmapDescriptor.class
      .getClassLoader()));
    localBitmapDescriptor.width = paramParcel.readInt();
    localBitmapDescriptor.height = paramParcel.readInt();
    return localBitmapDescriptor;
  }
  
  public BitmapDescriptor[] newArray(int paramInt)
  {
    return new BitmapDescriptor[paramInt];
  }
}
