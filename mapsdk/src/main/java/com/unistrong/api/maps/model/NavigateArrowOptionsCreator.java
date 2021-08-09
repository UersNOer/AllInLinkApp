package com.unistrong.api.maps.model;

import android.os.Parcel;
import android.os.Parcelable;
import java.util.ArrayList;

public class NavigateArrowOptionsCreator
  implements Parcelable.Creator<NavigateArrowOptions>
{
  public NavigateArrowOptions createFromParcel(Parcel paramParcel)
  {
    NavigateArrowOptions localNavigateArrowOptions = new NavigateArrowOptions();
    ArrayList<LatLng> localArrayList = new ArrayList();
    paramParcel.readTypedList(localArrayList, LatLng.CREATOR);
    
    float f1 = paramParcel.readFloat();
    int i = paramParcel.readInt();
    int j = paramParcel.readInt();
    float f2 = paramParcel.readFloat();
    int k = paramParcel.readByte();
    boolean bool = k == 1;
    localNavigateArrowOptions.addAll(localArrayList);
    localNavigateArrowOptions.width(f1);
    localNavigateArrowOptions.topColor(i);
    localNavigateArrowOptions.zIndex(f2);
    localNavigateArrowOptions.visible(bool);
    localNavigateArrowOptions.a = paramParcel.readString();
    return localNavigateArrowOptions;
  }
  
  public NavigateArrowOptions[] newArray(int paramInt)
  {
    return new NavigateArrowOptions[paramInt];
  }
}
