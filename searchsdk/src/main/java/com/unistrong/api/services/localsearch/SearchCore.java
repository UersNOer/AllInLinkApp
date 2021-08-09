package com.unistrong.api.services.localsearch;

import android.util.Log;

import com.unistrong.api.services.localsearch.model.SearchResultInfo;

/**
 * Created by gaoyh on 16/7/23.
 */
 class SearchCore {

    static {
        try {
            System.loadLibrary("ldlocalsearch");
        }catch (Exception ee){
            Log.i("LocalSearch", "load library ldlocalsearch error!");
        }
    }
    public static String deviceid="";
    protected static native int nativeInit(String dataPath,String snPath);

    protected static native void nativeExit();

    protected static native String nativeGetVersion();

    //关键字检索相关
    protected static native int nativeResetNameSearch(String [] typeCodes, int typeCount, String adCode, int adLevel, int searchType);

    protected static native int nativeInputName(String inputs);

    protected static native int nativeStartNameSearch();

    protected static native SearchResultInfo nativeGetNameRecordByIndex(int index);

    //首拼检索相关
    protected static native int nativeResetFirstLetterSearch(String [] typeCodes, int typeCount, String adCode, int adLevel, int searchType);

    protected static native int nativeInputFirstLetters(String inputs);

    protected static native int nativeStartFirstLetterSearch();

    protected static native SearchResultInfo nativeGetFirstLetterRecordByIndex(int index);

    protected static native int nativeGetAdcode(double longitude,double latitude);

    public static String GetDeviceID(){return deviceid;}
}
