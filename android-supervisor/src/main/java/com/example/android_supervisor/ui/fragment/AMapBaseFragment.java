package com.example.android_supervisor.ui.fragment;

import android.app.Activity;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.amap.api.location.AMapLocation;
import com.amap.api.maps.AMap;
import com.amap.api.maps.AMapUtils;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.MapView;
import com.amap.api.maps.UiSettings;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.MyLocationStyle;
import com.amap.api.maps.model.NaviPara;
import com.amap.api.maps.model.PolylineOptions;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.route.BusRouteResult;
import com.amap.api.services.route.DrivePath;
import com.amap.api.services.route.DriveRouteResult;
import com.amap.api.services.route.DriveStep;
import com.amap.api.services.route.RideRouteResult;
import com.amap.api.services.route.RouteSearch;
import com.amap.api.services.route.WalkRouteResult;
import com.example.android_supervisor.R;
import com.example.android_supervisor.entities.Area;
import com.example.android_supervisor.entities.EventRes;
import com.example.android_supervisor.entities.MapBundle;
import com.example.android_supervisor.map.AMapActivity;
import com.example.android_supervisor.map.MapLocationAPI;
import com.example.android_supervisor.map.poisearch.Constants;
import com.example.android_supervisor.sqlite.PublicSqliteHelper;
import com.example.android_supervisor.ui.TaskDetailActivity;
import com.example.android_supervisor.ui.view.CustomPopupWindow;
import com.example.android_supervisor.utils.CoordinateUtils;
import com.example.android_supervisor.utils.Environments;
import com.example.android_supervisor.utils.LogUtils;
import com.example.android_supervisor.utils.MapUtil;
import com.example.android_supervisor.utils.ToastUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by yj on 2019/9/10
 */
public class AMapBaseFragment extends BaseFragment implements  CustomPopupWindow.OnItemClickListener {

    AMapLocation aMapLocation;

//    @BindView(R.id.et_map_address_search)
//    AutoCompleteTextView etSearch;
//
//    @BindView(R.id.iv_map_address_clear)
//    AppCompatImageView buttonDelete;

    View view;

    @BindView(R.id.main_keywords)
    TextView mKeywordsTextView;

    @BindView(R.id.clean_keywords)
    ImageView mCleanKeyWords;

    @BindView(R.id.map_grid)
    AppCompatImageView map_grid;

    @BindView(R.id.map_case)
    AppCompatImageView map_case;

    @BindView(R.id.map_locate)
    AppCompatImageView map_locate;

    private MapView mMapView;
    public AMap aMap = null;

    public CustomPopupWindow mPop;
    private String mCity;
    private LatLng myLatlng;

    private boolean isShowCasePosition = false;//?????????????????? ??????????????????????????????
    private boolean isShowGird = false;//????????????
    private boolean isFirst = true;

    public boolean isShowCase = false;//??????????????????

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_amap, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        initMapView(view, savedInstanceState);
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        //??????????????????
        initPoiSerach();
        initMarker();

        MapBundle mapBundle = (MapBundle) getActivity().getIntent().getSerializableExtra("mapBundle");
        if (mapBundle == null) {
            Bundle bundle = getArguments();
            if (bundle != null) {
                mapBundle = (MapBundle) bundle.getSerializable("mapBundle");
            }
        }
        if (mapBundle != null) {
            isShowGird = mapBundle.isShowGird;
            isShowCase = mapBundle.isShowCase;
            setMapUI(isShowGird, isShowCase);
            setCaseLocation(mapBundle);


            if (isShowGird) {
                map_grid.setVisibility(isShowGird ? View.VISIBLE : View.GONE);
                setWorkGrid();
                if (getActivity() instanceof AMapActivity){
                    ((AMapActivity) getActivity()).setTitleName("????????????");
                }


            }

            if (!TextUtils.isEmpty(mapBundle.taskId)) {
                loadZXMarker(mapBundle.taskId);
            }
        }

//aMap.setOnMyLocationChangeListener(this);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                moveTo();

            }
        },300);

    }

    private void moveTo() {
        if (isFirst && !isShowCasePosition && !isShowGird) {
            //?????????????????????
            if (Environments.userBase != null && !TextUtils.isEmpty(Environments.userBase.getAreaCoordinateStr())) {
                try {
                    String[] latlng_str = Environments.userBase.getAreaCoordinateStr().split(",");
                    if (latlng_str.length == 2) {
                        LatLng latLng = new LatLng(Double.valueOf(latlng_str[1]), Double.valueOf(latlng_str[0]));
                        aMap.clear();
                        aMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 16));
                    }
                } catch (Exception e) {

                }
            }
            isFirst = false;
        }
    }


    public void setWorkGrid() {
    }

    public void loadZXMarker(String taskId) {

    }

    public void initMarker() {

    }

    public void setSingerMarker(LatLng myLatlng){

    }

    public void setSingerMarker(LatLng myLatlng,EventRes eventRes){

    }

    public void initPoiSerach() {
    }

    private void setCaseLocation(MapBundle mapBundle) {
        double longitude = mapBundle.longitude;
        double latitude = mapBundle.latitude;
        if (longitude == 0.0 || latitude == 0.0) {
            //????????????????????????????????????????????? ?????????????????????
//            view.findViewById(R.id.map_locate).performClick();
            return;
        }

        //???????????? 84 - Gcj02
        double[] latLng_Gcj02 = CoordinateUtils.wgs84ToGcj02(longitude, latitude);
        if (latLng_Gcj02 != null) {
            latitude = latLng_Gcj02[1];
            longitude = latLng_Gcj02[0];
            LogUtils.e("latLng???"+latitude+"   "+longitude +"sou:"+ mapBundle.longitude +"--"+ mapBundle.latitude);
        }

        isShowCasePosition = true;
        myLatlng = new LatLng(latitude, longitude);
        aMap.clear();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                aMap.animateCamera(CameraUpdateFactory.newLatLngZoom(myLatlng, 16));
                if (mapBundle.eventRes!=null){
                    setSingerMarker(myLatlng,mapBundle.eventRes);
                }else {
                    setSingerMarker(myLatlng);
                }


            }
        },300);

    }


    private void initMapView(View view, Bundle savedInstanceState) {
        mMapView = (MapView) view.findViewById(R.id.map);
        mMapView.onCreate(savedInstanceState);
        mPop = new CustomPopupWindow(getContext());
        mPop.setOnItemClickListener(this);

        aMap = mMapView.getMap();
        aMap.setMyLocationEnabled(true);
        aMap.moveCamera(CameraUpdateFactory.zoomTo(16));

        UiSettings uiSettings = aMap.getUiSettings();
        uiSettings.setCompassEnabled(false);
        uiSettings.setScaleControlsEnabled(true);
        uiSettings.setMyLocationButtonEnabled(true);

        MyLocationStyle myLocationStyle = new MyLocationStyle();//??????????????????????????????m
        // yLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATION_ROTATE);//???????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????1???1???????????????????????????myLocationType????????????????????????????????????
        myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATE );//
        myLocationStyle.interval(5000); //???????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????
        myLocationStyle.myLocationIcon(BitmapDescriptorFactory.fromBitmap(BitmapFactory
                .decodeResource(getResources(), R.drawable.tmap_poi)));
        aMap.setMyLocationStyle(myLocationStyle);//?????????????????????Style


        initCity();
    }

    private void initCity() {
        PublicSqliteHelper sqliteHelper = PublicSqliteHelper.getInstance(getContext());
        List<Area> mMapGridList = sqliteHelper.getAreaDao().queryForAll();
        if (mMapGridList != null && mMapGridList.size() > 0) {
            mCity = mMapGridList.get(0).getName();
            Constants.DEFAULT_CITY = mCity;
        } else {
            //?????????????????? ???????????????
            // Location lacation = aMap.getMyLocation();
        }
    }


    @OnClick(R.id.map_locate)
    public void onMap_locate() {
        if (myLatlng == null || myLatlng.latitude == 0.0 || myLatlng.longitude == 0.0) {
            return;
        }
        aMap.clear();
        aMap.animateCamera(CameraUpdateFactory.newLatLngZoom(myLatlng, 16));
    }


    @OnClick(R.id.map_grid)
    public void onOpenNet() {
        aMap.clear();
        setWorkGrid();
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
//        clearPoiMarkers();
        aMap.clear();
        mMapView.onDestroy();
    }

    @Override
    public void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mMapView.onPause();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mMapView.onSaveInstanceState(outState);
    }


//    @Override
//    public void onMyLocationChange(Location location) {
//        if (location != null) {
////            if (location.getErrorCode() == 0) {
//                //?????????????????????????????????????????????
//
//            myLatlng = new LatLng(location.getLatitude(), location.getLongitude());
//
//            new Handler().postDelayed(new Runnable() {
//                @Override
//                public void run() {
//                    if (myLatlng != null && myLatlng.latitude!=0.0 && myLatlng.longitude!=0.0 && isFirst && !isShowCasePosition && !isShowGird){
//                        aMap.clear();
//                        //?????????????????????
//                        if (Environments.userBase!=null &&!TextUtils.isEmpty(Environments.userBase.getAreaCoordinateStr())){
//                            try{
//                                String[] latlng_str = Environments.userBase.getAreaCoordinateStr().split(",");
//                                if (latlng_str.length ==2){
//                                    LatLng latLng = new LatLng(Double.valueOf(latlng_str[1]),Double.valueOf(latlng_str[0]));
//                                    aMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 16));
//                                }
//                            }catch (Exception e){
//                                aMap.animateCamera(CameraUpdateFactory.newLatLngZoom(myLatlng, 16));
//                            }
//                        }else {
//                            aMap.animateCamera(CameraUpdateFactory.newLatLngZoom(myLatlng, 16));
//                        }
//                        isFirst = false;
//                    }
//
//                    //??????Marker??????????????????
//                    if (locationMarker == null) {
//                        //?????????????????????????????????,icon????????????????????????????????????????????????
////                    locationMarker = aMap.addMarker(new MarkerOptions()
////                            .position(myLatlng)
////                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.tmap_poi)));
//                    }
//                }
//            },100);
//
//            }
//        Log.d("onMyLocationChange: ", "getAltitude:" + location.getAltitude()
//                + ",getLatitude:" + location.getLatitude()
//                + ",getLongitude:" + location.getLongitude());
//
//        }


    public LatLng getMapCenter() {
        return aMap.getCameraPosition().target;
    }

//    @Override
//    public void onCameraChange(CameraPosition cameraPosition) {
//        if (cameraPosition.zoom < 16) {
//            for (Marker marker : poiMarkers) {
//                marker.setVisible(false);
//            }
//        } else {
//            for (Marker marker : poiMarkers) {
//                marker.setVisible(true);
//            }
//        }
//    }
//
//    @Override
//    public void onCameraChangeFinish(CameraPosition cameraPosition) {
//
//    }



    @Override
    public void setOnItemClick(EventRes eventRes) {
        mPop.dismiss();//EventDetailActivity
        Intent intent = new Intent(getContext(), TaskDetailActivity.class);
        intent.putExtra("id", eventRes.getId());
        intent.putExtra("status", 2);
//        intent.putExtra("handled", true);

        intent.putExtra("status", eventRes.getCurrentLink().contains("??????") ? 2 : 1);
        intent.putExtra("taskStatus", 0);

        startActivityForResult(intent, 1);
    }

    @Override
    public void toNavi(Marker marker) {
        aMapLocation = MapLocationAPI.getLocation();
        if (aMapLocation != null) {
            myLatlng = new LatLng(aMapLocation.getLatitude(), aMapLocation.getLongitude());
            startAMapNavi(marker);
        }else {
            ToastUtils.show(getContext(),"?????????gps?????? ??? ??????????????????????????????!");
        }
       // naviInMap(marker);
    }

    /**
     * ??????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????app???????????????
     */
    public void startAMapNavi(Marker marker) {
        // ??????????????????
        NaviPara naviPara = new NaviPara();
        // ??????????????????
        naviPara.setTargetPoint(marker.getPosition());
        // ??????????????????????????????????????????
        naviPara.setNaviStyle(AMapUtils.DRIVING_AVOID_CONGESTION);

        //????????????????????????
        try {
            if (MapUtil.isGdMapInstalled()){
                AMapUtils.openAMapNavi(naviPara, getContext());
            }

            else if (MapUtil.isBaiduMapInstalled()){
                MapUtil.openBaiDuNavi(getContext(), myLatlng.latitude, myLatlng.longitude, "????????????",
                        marker.getPosition().latitude, marker.getPosition().longitude, ((EventRes) marker.getObject()).getAreaName());
            }

            else if (MapUtil.isTencentMapInstalled()){
                MapUtil.openTencentMap(getContext(), myLatlng.latitude, myLatlng.longitude, "????????????",
                        marker.getPosition().latitude, marker.getPosition().longitude, ((EventRes) marker.getObject()).getAreaName());
            }

            else{
                ToastUtils.show(getContext(),"????????????????????????");
                naviInMap(marker);
            }

        } catch (com.amap.api.maps.AMapException e) {
            ToastUtils.show(getContext(), e.getMessage());
        }

    }

    private void naviInMap(Marker marker) {
        RouteSearch routeSearch = new RouteSearch(getContext());
        LatLonPoint startLatLonPoint = new LatLonPoint(myLatlng.latitude, myLatlng.longitude);
        LatLonPoint endLatLonPoint = new LatLonPoint(marker.getPosition().latitude, marker.getPosition().longitude);
        aMap.clear();
        //setAmapMarker(eventResList);
        RouteSearch.FromAndTo fromAndTo = new RouteSearch.FromAndTo(startLatLonPoint, endLatLonPoint);
        RouteSearch.DriveRouteQuery query = new RouteSearch.DriveRouteQuery
                (fromAndTo, RouteSearch.DRIVING_MULTI_STRATEGY_FASTEST_SHORTEST_AVOID_CONGESTION, null, null, "");
        routeSearch.calculateDriveRouteAsyn(query);
        routeSearch.setRouteSearchListener(new RouteSearch.OnRouteSearchListener() {
            @Override
            public void onBusRouteSearched(BusRouteResult busRouteResult, int i) {

            }

            @Override
            public void onDriveRouteSearched(DriveRouteResult driveRouteResult, int i) {
                List<DrivePath> drivePathList = driveRouteResult.getPaths();
                List<DriveStep> driveStepList = drivePathList.get(0).getSteps();
                ArrayList<LatLng> drivePoint = new ArrayList<>();
                for (DriveStep driveStep : driveStepList) {
                    List<LatLonPoint> polyline = driveStep.getPolyline();
                    for (LatLonPoint lp : polyline) {
                        drivePoint.add(new LatLng(lp.getLatitude(), lp.getLongitude()));
                    }
                }
                aMap.addPolyline(new PolylineOptions()
                        .addAll(drivePoint)
                        .width(5)
                        //??????????????????
                        .geodesic(false)
                        //?????????????????????
                        .color(Color.argb(200, 0, 140, 0)));

                Marker startMarker = aMap.addMarker((new MarkerOptions())
                        .position(drivePoint.get(0)).icon(BitmapDescriptorFactory.fromResource(R.drawable.amap_start)));
                // startMarker.showInfoWindow();

                Marker endMarker = aMap.addMarker((new MarkerOptions()).position(drivePoint.get(drivePoint.size()-1))
                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.amap_end)));
                aMap.moveCamera(CameraUpdateFactory.newLatLngZoom(drivePoint.get(0),16));
//                 getShowRouteZoom()));
            }

            @Override
            public void onWalkRouteSearched(WalkRouteResult result, int errorCode) {
//                WalkRouteResult mWalkRouteResult = result;
//                if (errorCode == AMapException.CODE_AMAP_SUCCESS) {
//                    if (result != null && result.getPaths() != null) {
//                        if (result.getPaths().size() > 0) {
//
//                            final WalkPath walkPath = mWalkRouteResult.getPaths()
//                                    .get(0);
//                            if(walkPath == null) {
//                                return;
//                            }
//                            WalkRouteOverlay walkRouteOverlay = new WalkRouteOverlay(
//                                    this, aMap, walkPath,
//                                    mWalkRouteResult.getStartPos(),
//                                    mWalkRouteResult.getTargetPos());
//                            walkRouteOverlay.removeFromMap();
//                            walkRouteOverlay.addToMap();
//                            walkRouteOverlay.zoomToSpan();
//
//
//                        } else if (result != null && result.getPaths() == null) {
//                            ToastUtil.show(mContext, R.string.no_result);
//                        }
//
//                    } else {
//                        ToastUtil.show(mContext, R.string.no_result);
//                    }
//                } else {
//                    ToastUtil.showerror(this.getApplicationContext(), errorCode);
//                }


            }

            @Override
            public void onRideRouteSearched(RideRouteResult rideRouteResult, int i) {

            }
        });
    }


    public void setMapUI(boolean isShowGird, boolean isShowCase) {
        if (getActivity() != null) {
            map_grid.setVisibility(isShowGird ? View.VISIBLE : View.GONE);
            map_case.setVisibility(isShowCase ? View.VISIBLE : View.GONE);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == 1) {
                //??????????????????
                refreshMap();
            }
        }
    }

    public void refreshMap() {

    }


}
