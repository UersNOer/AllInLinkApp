package com.example.android_supervisor.map;

/**
 * @author wujie
 */
public class SuggestionData {
    public String id;
    public String title;
    public String province;
    public String city;
    public String adcode;
    public String district;
    public int type;
    public float latitude;
    public float longitude;
    public String address;

    @Override
    public String toString() {
        return address;
    }
}
