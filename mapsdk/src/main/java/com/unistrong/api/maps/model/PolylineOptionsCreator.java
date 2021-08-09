package com.unistrong.api.maps.model;

import android.os.Parcel;
import android.os.Parcelable;
import java.util.ArrayList;

public class PolylineOptionsCreator
  implements Parcelable.Creator<PolylineOptions>
{
  public PolylineOptions createFromParcel(Parcel paramParcel)
  {
    PolylineOptions localPolylineOptions = new PolylineOptions();
    ArrayList localArrayList1 = new ArrayList();
    paramParcel.readTypedList(localArrayList1, LatLng.CREATOR);
    
    float f1 = paramParcel.readFloat();
    int i = paramParcel.readInt();
    float f2 = paramParcel.readFloat();
    localPolylineOptions.a = paramParcel.readString();
    boolean[] arrayOfBoolean = new boolean[4];
    paramParcel.readBooleanArray(arrayOfBoolean);
    BitmapDescriptor localBitmapDescriptor = (BitmapDescriptor)paramParcel.readParcelable(BitmapDescriptor.class
      .getClassLoader());
    localPolylineOptions.addAll(localArrayList1);
    localPolylineOptions.width(f1);
    localPolylineOptions.color(i);
    localPolylineOptions.zIndex(f2);
    localPolylineOptions.visible(arrayOfBoolean[0]);
    localPolylineOptions.setDottedLine(arrayOfBoolean[1]);
    localPolylineOptions.geodesic(arrayOfBoolean[2]);
    localPolylineOptions.useGradient(arrayOfBoolean[3]);
    localPolylineOptions.setCustomTexture(localBitmapDescriptor);
    ArrayList localArrayList2 = paramParcel.readArrayList(BitmapDescriptor.class
      .getClassLoader());
    localPolylineOptions.setCustomTextureList(localArrayList2);
    
    ArrayList localArrayList3 = paramParcel.readArrayList(Integer.class
      .getClassLoader());
    localPolylineOptions.setCustomTextureIndex(localArrayList3);
    ArrayList localArrayList4 = paramParcel.readArrayList(Integer.class
      .getClassLoader());
    localPolylineOptions.colorValues(localArrayList4);
    return localPolylineOptions;
  }
  
  public PolylineOptions[] newArray(int paramInt)
  {
    return new PolylineOptions[paramInt];
  }
}
