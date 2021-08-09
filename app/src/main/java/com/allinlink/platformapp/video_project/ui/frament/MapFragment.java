package com.allinlink.platformapp.video_project.ui.frament;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.Nullable;

import com.unistrong.api.mapcore.ClusterListener;
import com.unistrong.api.maps.CameraUpdate;
import com.unistrong.api.maps.CameraUpdateFactory;
import com.unistrong.api.maps.MapController;
import com.unistrong.api.maps.MapOptions;
import com.unistrong.api.maps.MapView;
import com.unistrong.api.maps.UnistrongException;
import com.unistrong.api.maps.model.BitmapDescriptor;
import com.unistrong.api.maps.model.BitmapDescriptorFactory;
import com.unistrong.api.maps.model.CameraPosition;
import com.unistrong.api.maps.model.CircleOptions;
import com.unistrong.api.maps.model.ClusterOptions;
import com.unistrong.api.maps.model.LatLng;
import com.unistrong.api.maps.model.LatLngBounds;
import com.unistrong.api.maps.model.Marker;
import com.unistrong.api.maps.model.MarkerOptions;
import com.unistrong.api.maps.model.QuadTreeNodeData;
import com.unistrong.api.maps.model.TileOverlay;
import com.unistrong.api.maps.model.TileOverlayOptions;
import com.unistrong.api.maps.model.UrlTileProvider;
import com.unistrong.utils.camera.BitmapUtils;
import com.allinlink.platformapp.R;
import com.allinlink.platformapp.video_project.bean.CameraBean;
import com.allinlink.platformapp.video_project.bean.ChannelBean;
import com.allinlink.platformapp.video_project.bean.MapMarkerBean;
import com.allinlink.platformapp.video_project.contract.fragment.MapFragmentContract;
import com.allinlink.platformapp.video_project.ui.activity.AddCameraActivity;
import com.allinlink.platformapp.video_project.presenter.fragment.MapFragmentPresenter;
import com.allinlink.platformapp.video_project.ui.activity.SearchCameraActivity;
import com.allinlink.platformapp.video_project.utils.UserCache;
import com.allinlink.platformapp.video_project.widget.MapDialogLinerLayout;
import com.unistrong.view.base.BaseFragment;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;


public class MapFragment extends BaseFragment<MapFragmentPresenter> implements MapFragmentContract.View, MapController.OnMarkerClickListener, MapController.OnMapLoadedListener, View.OnClickListener, MapController.OnMapClickListener, MapDialogLinerLayout.DialogOnClickListance, MapController.OnMapLevelChangeListener, MapController.OnMapLongClickListener, MapController.OnMapTouchListener, MapController.OnCameraChangeListener {
    MapView mapView;
    MapController map = null;
    MapDialogLinerLayout mapDialogLinerLayout;
    List<MapMarkerBean.FeaturesBean> features = new ArrayList<>();
    ArrayList<Marker> markers = new ArrayList<>();
    public Marker lsatClickMark;
    private float zoom = 10;
    List<LatLng> latLngs = new ArrayList<>();
    private String JD[] = new String[]{"113.84156256800009", "113.84129606500005", "113.84085925600004"};
    private String WD[] = new String[]{"34.57023725000005", "34.57008874200005", "34.57047198200007"};

    @Override
    protected View onCreateContentView() {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_map, null);
        view.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });
        return view;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mPresenter = new MapFragmentPresenter(this);
        mapView = getActivity().findViewById(R.id.mapview);
        mapView.setSavedInstanceState(savedInstanceState);
        mapView.onCreate();
        initMap();
        setUpMap();
        mapDialogLinerLayout = getActivity().findViewById(R.id.lin_dialogmap);
        mapDialogLinerLayout.initView();
        mapDialogLinerLayout.setListance(this);
        getActivity().findViewById(R.id.lin_serach).setOnClickListener(this);
        getActivity().findViewById(R.id.iv_add_camera).setOnClickListener(this);
        mPresenter.getAllCameraInfo();
    }

    private void initMap() {
        try {
            map = mapView.getMap();
        } catch (UnistrongException e) {
            e.printStackTrace();
        }


        final TileOverlayOptions tileOverlayOptions =
                new TileOverlayOptions().tileProvider(new UrlTileProvider(256, 256) {

                    @Override
                    public synchronized URL getTileUrl(int x, int y, int zoom) {
                        //x y坐标 zoom 层级

                        try {
                            String format = String.format("http://10.16.68.120:8082/zw-map/geoesb/proxy/a25bb2d7de5747c9adc113d2d1f01cd9/08d7994c219c4dd58c9bb15403438624?layer=PUBDLG2019HKG_564&style=default&tilematrixset=CustomCRS4490ScalePUBDLG2019HKG_564&Service=WMTS&Request=GetTile&Version=1.0.0&Format=PNG&TileMatrix=%d&TileCol=%d&TileRow=%d", zoom - 6, x, y);
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


        /**
         * 当url 返回null 并且 xml中loadwts=false 时
         * 加载时空互联默认地图
         */
        final TileOverlayOptions tileOverlayOptions2 =
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
        tileOverlayOptions2.diskCacheEnabled(false)//是否缓存
                .diskCacheDir("/storage/emulated/0/amap/OMCcache")//缓存路径 true生效
                .diskCacheSize(100000)
                .memoryCacheEnabled(true)
                .memCacheSize(100000)
                .zIndex(1);//图层位置
        map.addTileOverlay(tileOverlayOptions2);


    }


    //初始化地图设置
    private void setUpMap() {
        try {
            map = mapView.getMap();
            map.setOnMapClickListener(this);
            map.setOnMarkerClickListener(this);
            map.setOnMapLoadedListener(this);
            map.setOnMapLevelChangeListener(this);
            map.getUiSettings().setZoomPosition(MapOptions.ZOOM_POSITION_RIGHT_CENTER);
            map.setOnMapClickListener(this);
            map.setOnMapLongClickListener(this);

            map.setOnMapTouchListener(this);
            map.setOnMarkerClickListener(this);
            map.setOnCameraChangeListener(this);
            map.getUiSettings().setCompassEnabled(false);
        } catch (UnistrongException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        if (hidden) {
            //相当于Fragment的onPause
        } else {

        }
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
        mapDialogLinerLayout.setIsShow(false);
        mapDialogLinerLayout.setVisibility(View.GONE);
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
    public void onError(String msg) {

    }

    //设置地图Marker
    @Override
    public void setMapMarker(MapMarkerBean markers) {
        features.clear();
        features.addAll(markers.getFeatures());

    }

    List<ChannelBean> datas;

    @Override
    public void setCameraData(List<ChannelBean> result) {
        if (mapDialogLinerLayout != null) {
            if (mapDialogLinerLayout.isShown()) {
                mapDialogLinerLayout.setViewVisibility(View.GONE);
                lsatClickMark = null;
            }
        }

        for (Marker marker : markers) {
            marker.remove();
        }
        datas = result;
        new Thread() {
            public void run() {
                try {
                    map.cleanQuad();

                } catch (Exception e) {
                    Log.i("TAG", "点聚合构建失败");
                }
//                List<LatLng> latLngs = HttpUtil.readFromAssets(ClusterActivity.this, "poi.txt");
                latLngs.clear();
                latLngs.addAll(initDoc(datas));
                if (latLngs.size() == 0) {
                    return;
                }
                List<QuadTreeNodeData> items = new ArrayList<QuadTreeNodeData>();

                LatLngBounds.Builder builder = new LatLngBounds.Builder();

                for (int i = 0; i < latLngs.size(); i++) {
                    QuadTreeNodeData data = new QuadTreeNodeData();
                    LatLng latLng = latLngs.get(i);
                    MarkerOptions markerOptions = new MarkerOptions();
                    markerOptions.position(latLng);
                    latLng.setTAG(latLng.getTAG() + ";" + (i + 1));
                    markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.icon_map));
                    data.setMarkerOptions(markerOptions);
                    items.add(data);
                    builder.include(latLng);
                }
                ClusterOptions options = new ClusterOptions();
                options.setQuadTreeNodeDataList(items);
                options.setMaxClusterLevel(17);
                options.setClusterListener(new ClusterListener() {
                    @Override
                    public void builderFinish() {
                        map.showCluster();
                    }
                });
                map.setClusterOptions(options);
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        map.buildCluster();
                    }
                });

            }
        }.start();


    }

    //初始化点位信息

    private ArrayList<LatLng> initDoc(List<ChannelBean> markers) {
        ArrayList<LatLng> latLngs = new ArrayList<>();
//        for (ChannelBean featuresBean : markers) {
//            latLngs.add(new LatLng(Double.parseDouble(featuresBean.getWd()), Double.parseDouble(featuresBean.getJd())));
//        }

        for (int i = 0; i < markers.size(); i++) {
            ChannelBean featuresBean = markers.get(i);
//            featuresBean.setWd(WD[i]);
//            featuresBean.setJd(JD[i]);
            try {
                latLngs.add(new LatLng(Double.parseDouble(featuresBean.getWd()), Double.parseDouble(featuresBean.getJd())));
            }catch (Exception e){
                Log.i("TAG","当前数据没有经纬度");
            }

        }
        return latLngs;
    }

    @Override
    public void onMapLoaded() {
//        mPresenter.getMapMarker();
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        List<QuadTreeNodeData> cluster = (List<QuadTreeNodeData>) marker.getObject();
        if (cluster == null || cluster.isEmpty()) return false;
        if (cluster.size() > 1) {
            LatLngBounds.Builder builder = new LatLngBounds.Builder();
            for (QuadTreeNodeData clusterItem : cluster) {
                builder.include(clusterItem.getMarkerOptions().getPosition());
            }
            LatLngBounds latLngBounds = builder.build();
            map.animateCamera(CameraUpdateFactory.newLatLngBounds(latLngBounds, 0));
            mapDialogLinerLayout.setViewVisibility(View.GONE);
            return true;
        } else if (cluster.size() == 1) {
            if (null != lsatClickMark) {
                lsatClickMark.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.icon_map));
                lsatClickMark = null;
            }
            marker.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.icon_map2));
            lsatClickMark = marker;
            moveToCenter(marker.getPosition(), zoom);
            ChannelBean clickLatlngData = getClickLatlngData(marker.getPosition());
            mapDialogLinerLayout.onPause();
            mapDialogLinerLayout.setSteamUrl(clickLatlngData);
            mapDialogLinerLayout.setVisibility(View.VISIBLE);
        }


        return true;
    }

    /**
     * 获得点击的数据
     *
     * @param latLng
     * @return
     */
    private ChannelBean getClickLatlngData(LatLng latLng) {
        ArrayList<LatLng> latLngs = initDoc(datas);
        for (int i = 0; i < latLngs.size(); i++) {
            if (latLng.equals(latLngs.get(i)))
                return datas.get(i);
        }
        return null;
    }

    /**
     * 点击点位并移动到中间位置
     *
     * @param latLng 坐标
     * @param zoom   缩放级别
     */
    private void moveToCenter(LatLng latLng, float zoom) {
        CameraUpdate cameraUpdate = CameraUpdateFactory.newCameraPosition(new CameraPosition(
                latLng, zoom, 30, 0));
        map.animateCamera(cameraUpdate, 10, null);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.lin_serach:
                Intent intent = new Intent(getContext(), SearchCameraActivity.class);
                intent.putExtra("type", 3);
                startActivity(intent);
                break;
            case R.id.iv_add_camera:
                startActivity(new Intent(getContext(), AddCameraActivity.class));

                break;
        }
    }

    @Override
    public void onMapClick(LatLng point) {
        mapDialogLinerLayout.setViewVisibility(View.GONE);
    }

    @Override
    public void collectClick(boolean collect, ChannelBean data) {
        if (collect) {
            mPresenter.addChannelUserfavorites(data);
        } else {
            mPresenter.removeChannelUserfavoritesById(data);

        }
    }

    @Override
    public void onMapLevelChanged(float level) {
        zoom = level;
    }

    @Override
    public void onMapLongClick(LatLng point) {

    }

    @Override
    public void onTouch(MotionEvent event) {

    }

    @Override
    public void onCameraChange(CameraPosition position) {

    }

    @Override
    public void onCameraChangeFinish(CameraPosition position) {
        map.showCluster();
    }
}
