package com.unistrong.api.maps.model;

import android.os.Parcel;
import android.os.Parcelable;
import java.util.ArrayList;

public class PolygonOptionsCreator
  implements Parcelable.Creator<PolygonOptions>
{
  public static final int CONTENT_DESCRIPTION = 0;
  
  public PolygonOptions createFromParcel(Parcel paramParcel)
  {
    PolygonOptions localPolygonOptions = new PolygonOptions();
    ArrayList localArrayList = new ArrayList();
    paramParcel.readTypedList(localArrayList, LatLng.CREATOR);
    float f1 = paramParcel.readFloat();
    int i = paramParcel.readInt();
    int j = paramParcel.readInt();
    float f2 = paramParcel.readFloat();
    int k = paramParcel.readByte();
    boolean bool = k == 1;
    
    LatLng[] arrayOfLatLng = new LatLng[localArrayList.size()];
    for (int m = 0; m < localArrayList.size(); m++) {
      arrayOfLatLng[m] = ((LatLng)localArrayList.get(m));
    }
    localPolygonOptions.add(arrayOfLatLng);
    localPolygonOptions.strokeWidth(f1);
    localPolygonOptions.strokeColor(i);
    localPolygonOptions.fillColor(j);
    localPolygonOptions.zIndex(f2);
    localPolygonOptions.visible(bool);
    localPolygonOptions.a = paramParcel.readString();
    return localPolygonOptions;
  }
  
  public PolygonOptions[] newArray(int paramInt)
  {
    return new PolygonOptions[paramInt];
  }
}
