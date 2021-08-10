package com.unistrong.api.maps.model;

import android.os.Parcel;
import android.os.Parcelable;

class d
  implements Parcelable.Creator<Tile>
{
  public Tile createFromParcel(Parcel paramParcel)
  {
    int i = paramParcel.readInt();
    int j = paramParcel.readInt();
    int k = paramParcel.readInt();
    byte[] arrayOfByte = paramParcel.createByteArray();
    return new Tile(i, j, k, arrayOfByte);
  }
  
  public Tile[] newArray(int paramInt)
  {
    return new Tile[paramInt];
  }
}
