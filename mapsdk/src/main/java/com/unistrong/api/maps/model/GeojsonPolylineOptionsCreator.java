package com.unistrong.api.maps.model;


import android.os.Parcel;
import android.os.Parcelable;

public class GeojsonPolylineOptionsCreator implements Parcelable.Creator<GeojsonPolylineOptions>{

    @Override
    public GeojsonPolylineOptions createFromParcel(Parcel paramParcel) {
        GeojsonPolylineOptions localPolylineOptions = new GeojsonPolylineOptions();
//        ArrayList localArrayList1 = new ArrayList();
//        paramParcel.readTypedList(localArrayList1, LatLng.CREATOR);

        float f1 = paramParcel.readFloat();
        int i = paramParcel.readInt();
        float f2 = paramParcel.readFloat();
//        localPolylineOptions.a = paramParcel.readString();
        boolean[] arrayOfBoolean = new boolean[2];
        paramParcel.readBooleanArray(arrayOfBoolean);
        BitmapDescriptor localBitmapDescriptor = (BitmapDescriptor)paramParcel.readParcelable(BitmapDescriptor.class
                .getClassLoader());
//        localPolylineOptions.addAll(localArrayList1);
        localPolylineOptions.width(f1);
        localPolylineOptions.color(i);
        localPolylineOptions.zIndex(f2);
//        localPolylineOptions.visible(arrayOfBoolean[0]);
        localPolylineOptions.setDottedLine(arrayOfBoolean[0]);
        localPolylineOptions.geodesic(arrayOfBoolean[1]);
//        localPolylineOptions.useGradient(arrayOfBoolean[3]);
        localPolylineOptions.setCustomTexture(localBitmapDescriptor);
//        ArrayList localArrayList2 = paramParcel.readArrayList(BitmapDescriptor.class
//                .getClassLoader());
//        localPolylineOptions.setCustomTextureList(localArrayList2);

//        ArrayList localArrayList3 = paramParcel.readArrayList(Integer.class
//                .getClassLoader());
//        localPolylineOptions.setCustomTextureIndex(localArrayList3);
//        ArrayList localArrayList4 = paramParcel.readArrayList(Integer.class
//                .getClassLoader());
//        localPolylineOptions.colorValues(localArrayList4);
        return localPolylineOptions;
    }

    @Override
    public GeojsonPolylineOptions[] newArray(int size) {
        return new GeojsonPolylineOptions[size];
    }
}
