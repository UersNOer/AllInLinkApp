package com.allinlink.platformapp.video_project.ui.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.unistrong.api.maps.MapController;
import com.unistrong.api.maps.MapView;
import com.unistrong.api.maps.UnistrongException;
import com.unistrong.api.maps.model.BitmapDescriptorFactory;
import com.unistrong.api.maps.model.LatLng;
import com.unistrong.api.maps.model.Marker;
import com.unistrong.api.maps.model.MarkerOptions;
import com.unistrong.api.maps.model.TileOverlayOptions;
import com.unistrong.api.maps.model.UrlTileProvider;
import com.unistrong.utils.RxBus;
import com.allinlink.platformapp.R;
import com.allinlink.platformapp.video_project.bean.UpChannelBus;

import java.net.MalformedURLException;
import java.net.URL;


public class MapActivity extends AppCompatActivity implements MapController.OnMapClickListener, View.OnClickListener {
    int index;
    MapView mapView;
    MapController map;
    Marker marker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        String lat = getIntent().getStringExtra("lat");
        String lng = getIntent().getStringExtra("lng");
        findViewById(R.id.bt_sure).setOnClickListener(this);
        index = getIntent().getIntExtra("index", 0);
        mapView = findViewById(R.id.mapview);
        mapView.setSavedInstanceState(savedInstanceState);
        mapView.onCreate();
        initMap();

        try {
            map = mapView.getMap();
            map.setOnMapClickListener(this);
        } catch (UnistrongException e) {
            e.printStackTrace();
        }
        if (lat != null && lng != null) {
            addMarker2Map(lat, lng, R.drawable.icon_map);
        }
    }

    private void initMap() {
        try {
            map = mapView.getMap();
        } catch (UnistrongException e) {
            e.printStackTrace();
        }


        /**
         * 当url 返回null 并且 xml中loadwts=false 时
         * 加载时空互联默认地图
         */
        final TileOverlayOptions tileOverlayOptions =
                new TileOverlayOptions().tileProvider(new UrlTileProvider(256, 256) {

                    @Override
                    public synchronized URL getTileUrl(int x, int y, int zoom) {
                        //x y坐标 zoom 层级

                        try {
                            String format = String.format("http://10.16.68.120:8082/zw-map/geoesb/proxy/565a9739ebdb424b82f24fb1f6b437cb/08d7994c219c4dd58c9bb15403438624?layer=PUBDLG2019HKG_564&style=default&tilematrixset=CustomCRS4490ScalePUBDLG2019HKG_564&Service=WMTS&Request=GetTile&Version=1.0.0&Format=PNG&TileMatrix=%d&TileCol=%d&TileRow=%d", zoom - 6, x, y);
                            URL url = new URL(format);//  支持本地路径 file://...
                            return url;
                        } catch (MalformedURLException e) {
                            Log.i("TAG", e.toString());
                            e.printStackTrace();
                        }
                        return null;
                    }
                });
        tileOverlayOptions.diskCacheEnabled(false)//是否缓存
                .diskCacheDir("/storage/emulated/0/amap/OMCcache")//缓存路径 true生效
                .diskCacheSize(100000)
                .memoryCacheEnabled(true)
                .memCacheSize(100000)
                .zIndex(1);//图层位置
        map.addTileOverlay(tileOverlayOptions);
    }

    private Marker addMarker2Map(String lat, String lng, int resId) {
        MarkerOptions markerOption = new MarkerOptions();
        markerOption.position(new LatLng(Double.parseDouble(lat), Double.parseDouble(lng)));
        markerOption.draggable(true).anchor(0.5f, 0.5f);
        markerOption.icon(BitmapDescriptorFactory.fromResource(resId));
        // 将Marker设置为贴地显示，可以双指下拉看效果
        markerOption.setFlat(false);
        Marker marker = map.addMarker(markerOption);
        marker.showInfoWindow();
        return marker;
    }

    /**
     * 方法重写
     */
    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }


    /**
     * 方法重写
     */
    @Override
    public void onPause() {
        mapView.onPause();
        super.onPause();
    }

    /**
     * 方法重写
     */
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    /**
     * 方法重写
     */
    @Override
    public void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    public void onMapClick(LatLng point) {
        if (marker != null) {
            marker.remove();
        }
        marker = addMarker2Map(point.latitude + "", point.longitude + "", R.drawable.icon_map2);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_sure:
                RxBus.getInstance().send(new UpChannelBus(index, marker.getPosition()));
                finish();
                break;
        }
    }
}