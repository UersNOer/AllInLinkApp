package com.unistrong.api.maps.model;

import android.os.Parcel;
import android.os.Parcelable;

public class TileProjectionCreator
  implements Parcelable.Creator<TileProjection>
{
  public TileProjection createFromParcel(Parcel paramParcel)
  {
    TileProjection localTileProjection = new TileProjection(paramParcel.readInt(), paramParcel.readInt(), paramParcel.readInt(), paramParcel.readInt(), paramParcel.readInt(), paramParcel.readInt());
    return localTileProjection;
  }
  
  public TileProjection[] newArray(int paramInt)
  {
    return new TileProjection[paramInt];
  }
}
