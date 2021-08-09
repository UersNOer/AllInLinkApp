package com.unistrong.api.maps.model;

import android.os.Parcel;
import android.os.Parcelable;

public class TileOverlayOptionsCreator
  implements Parcelable.Creator<TileOverlayOptions>
{
  public static final int CONTENT_DESCRIPTION = 0;
  
  public TileOverlayOptions createFromParcel(Parcel paramParcel)
  {
    int i = paramParcel.readInt();
    TileProvider localTileProvider = (TileProvider)paramParcel.readValue(TileProvider.class.getClassLoader());
    boolean bool1 = paramParcel.readByte() != 0;
    float f = paramParcel.readFloat();
    int j = paramParcel.readInt();
    int k = paramParcel.readInt();
    String str = paramParcel.readString();
    boolean bool2 = paramParcel.readByte() != 0;
    boolean bool3 = paramParcel.readByte() != 0;
    TileOverlayOptions localTileOverlayOptions = new TileOverlayOptions(i, null, bool1, f);
    if (localTileProvider != null) {
      localTileOverlayOptions.tileProvider(localTileProvider);
    }
    localTileOverlayOptions.memCacheSize(j);
    localTileOverlayOptions.diskCacheSize(k);
    localTileOverlayOptions.diskCacheDir(str);
    localTileOverlayOptions.memoryCacheEnabled(bool2);
    localTileOverlayOptions.diskCacheEnabled(bool3);
    return localTileOverlayOptions;
  }
  
  public TileOverlayOptions[] newArray(int paramInt)
  {
    return new TileOverlayOptions[paramInt];
  }
}
