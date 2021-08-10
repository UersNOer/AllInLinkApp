package com.unistrong.api.maps.model;

import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;

public class TextOptionsCreator
  implements Parcelable.Creator<TextOptions>
{
  public TextOptions createFromParcel(Parcel paramParcel)
  {
    TextOptions localTextOptions = new TextOptions();
    localTextOptions.a = paramParcel.readString();
    Bundle localBundle1 = paramParcel.readBundle();
    LatLng localLatLng = new LatLng(localBundle1.getDouble("lat"), localBundle1.getDouble("lng"));
    localTextOptions.position(localLatLng);
    localTextOptions.text(paramParcel.readString());
    localTextOptions.typeface(Typeface.defaultFromStyle(paramParcel.readInt()));
    localTextOptions.rotate(paramParcel.readFloat());
    localTextOptions.align(paramParcel.readInt(), paramParcel.readInt());
    localTextOptions.backgroundColor(paramParcel.readInt());
    localTextOptions.fontColor(paramParcel.readInt());
    localTextOptions.fontSize(paramParcel.readInt());
    localTextOptions.zIndex(paramParcel.readFloat());
    localTextOptions.visible(paramParcel.readByte() == 1);
    try
    {
      Bundle localBundle2 = paramParcel.readBundle();
      Parcelable localParcelable = localBundle2.getParcelable("obj");
      if (localParcelable != null) {
        localTextOptions.setObject(localParcelable);
      }
    }
    catch (Throwable localThrowable)
    {
      localThrowable.printStackTrace();
    }
    return localTextOptions;
  }
  
  public TextOptions[] newArray(int paramInt)
  {
    return new TextOptions[paramInt];
  }
}
