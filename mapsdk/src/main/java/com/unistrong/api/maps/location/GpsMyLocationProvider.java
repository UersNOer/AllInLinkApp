package com.unistrong.api.maps.location;


import android.annotation.SuppressLint;
import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Set;

/**
 * location provider, by default, uses {@link LocationManager#GPS_PROVIDER} and {@link LocationManager#NETWORK_PROVIDER}
 */
public class GpsMyLocationProvider implements IMyLocationProvider, LocationListener {
    private LocationManager mLocationManager;
    private Location mLocation;
    private Context context;
    private IMyLocationConsumer mMyLocationConsumer;
    private long mLocationUpdateMinTime = 0;
    private float mLocationUpdateMinDistance = 0.0f;
    private NetworkLocationIgnorer mIgnorer = new NetworkLocationIgnorer();
    private final Set<String> locationSources = new HashSet<>();

    public static int REQUEST_OK = 1000;

    public GpsMyLocationProvider(Context context) {
        this.context = context;
        mLocationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        locationSources.add(LocationManager.GPS_PROVIDER);
        locationSources.add(LocationManager.NETWORK_PROVIDER);
    }

    // ===========================================================
    // Getter & Setter
    // ===========================================================

    /**
     * removes all sources, again, only useful before startLocationProvider is called
     */
    public void clearLocationSources() {
        locationSources.clear();
    }

    /**
     * adds a new source to listen for location data. Has no effect after startLocationProvider has been called
     * unless startLocationProvider is called again
     */
    public void addLocationSource(String source) {
        locationSources.add(source);
    }

    /**
     * returns the live list of GPS sources that we accept, changing this list after startLocationProvider
     * has no effect unless startLocationProvider is called again
     *
     * @return
     */
    public Set<String> getLocationSources() {
        return locationSources;
    }

    public long getLocationUpdateMinTime() {
        return mLocationUpdateMinTime;
    }

    public void setLocationUpdateMinTime(final long milliSeconds) {
        mLocationUpdateMinTime = milliSeconds;
    }

    public float getLocationUpdateMinDistance() {
        return mLocationUpdateMinDistance;
    }


    public void setLocationUpdateMinDistance(final float meters) {
        mLocationUpdateMinDistance = meters;
    }

    //
    // IMyLocationProvider
    //

    /**
     * Enable location updates and show your current location on the map. By default this will
     * request location updates as frequently as possible, but you can change the frequency and/or
     * distance by calling {@link #setLocationUpdateMinTime} and/or {@link
     * #setLocationUpdateMinDistance} before calling this method.
     */
    @SuppressLint("MissingPermission")
    @Override
    public boolean startLocationProvider(IMyLocationConsumer myLocationConsumer) {
        mMyLocationConsumer = myLocationConsumer;
        boolean result = false;
        for (final String provider : mLocationManager.getProviders(true)) {
            if (locationSources.contains(provider)) {

                try {
                    mLocationManager.requestLocationUpdates(provider, mLocationUpdateMinTime,
                            mLocationUpdateMinDistance, GpsMyLocationProvider.this, Looper.getMainLooper());
                    result = true;
                } catch (Throwable ex) {
                }
            }
        }

        return result;
    }

    @SuppressLint("MissingPermission")
    @Override
    public void stopLocationProvider() {
        mMyLocationConsumer = null;
        if (mLocationManager != null) {
            try {
                mLocationManager.removeUpdates(this);
            } catch (Throwable ex) {
            }
        }
    }

    @Override
    public Location getLastKnownLocation() {
        return mLocation;
    }

    @Override
    public void destroy() {
        stopLocationProvider();
        mLocation = null;
        mLocationManager = null;
        mMyLocationConsumer = null;
        mIgnorer = null;
    }

    //
    // LocationListener
    //

    @Override
    public void onLocationChanged(final Location location) {
        if (mIgnorer == null) {
            return;
        }
        if (location == null || location.getProvider() == null) {

            return;
        }
        // ignore temporary non-gps fix
        if (mIgnorer.shouldIgnore(location.getProvider(), System.currentTimeMillis())) {
            return;
        }

        mLocation = location;
        if (mMyLocationConsumer != null && mLocation != null) {
            mMyLocationConsumer.onLocationChanged(mLocation, this);
        }
    }

    //调用原生api进行地理解析
    public static List<Address> getAddress(Context context, double latitude, double longitude) {
        Geocoder geocoder = new Geocoder(context, Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(latitude, longitude, 5);
            return addresses;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void getAddress(final double lat, final double lng, final Handler handler) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                InputStream input = null;
                HttpURLConnection httpURLConnection = null;
                String url_path = null;
                double latDifference = (40.045534396702 - 40.0443);
                double lngDifference = (116.281755913629 - 116.2757);
                Log.i("TAG",lat+"---"+lng);
                url_path = "https://restapi.amap.com/v3/geocode/regeo?output=json&location=" + (lng + lngDifference) + "," + (lat + latDifference) + "&key=c0d2f3afcc4cc18a05d976d242dc3a70&radius=150&extensions=all";
                //模拟参数
                try {
                    URL url = null;
                    if (url_path.equals("")) {
                    }
                    Log.i("TAG",url_path);
                    url = new URL(url_path);
                    httpURLConnection = (HttpURLConnection) url.openConnection();
                    httpURLConnection.setDoInput(true);
                    httpURLConnection.setDoOutput(true);    //可以创建输出流，将请求参数写入
                    httpURLConnection.setRequestMethod("GET"); //请求方式为POST
                    Iterator<String> keyIterator;
                    int byteLength;
                    byte[] buffer = new byte[1024];
                    StringBuilder builder = new StringBuilder();
                    input = httpURLConnection.getInputStream();
                    while ((byteLength = input.read(buffer)) != -1)
                        builder.append(new String(buffer, 0, byteLength, "UTF-8"));
                    Log.i("TAG",builder.toString());
                    Message message = Message.obtain();
                    message.what = REQUEST_OK;
                    message.obj = readJson(builder.toString(),latDifference,lngDifference);
                    handler.sendMessage(message);
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    try {
                        /**
                         * 关闭连接/流
                         */

                        if (httpURLConnection != null) {
                            httpURLConnection.disconnect();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }


    private static ArrayList<Address> readJson(String Json, double latDifference, double lngDifference) throws JSONException {
        ArrayList<Address> addresses = new ArrayList<>();
        JSONObject jsonObject = new JSONObject(Json);
        JSONObject regeocode = jsonObject.getJSONObject("regeocode");
        JSONObject addressComponent = regeocode.getJSONObject("addressComponent");
        String city = addressComponent.getString("city");
        JSONArray pois = regeocode.getJSONArray("pois");
        for (int i = 0; i < pois.length(); i++) {
            Address address = new Address(null);
            JSONObject poi = pois.getJSONObject(i);
            String location = poi.getString("location");
            String[] split = location.split(",");
            Log.i("TAG",split[1]+"---"+split[0]);

            address.setLatitude(Double.parseDouble(split[1]));
            address.setLongitude(Double.parseDouble(split[0]));
            address.setCountryName(city);

            address.setAddressLine(0, poi.getString("name"));
            addresses.add(address);
        }
        return addresses;
    }

    @Override
    public void onProviderDisabled(final String provider) {
    }

    @Override
    public void onProviderEnabled(final String provider) {
    }

    @Override
    public void onStatusChanged(final String provider, final int status, final Bundle extras) {
    }
}
