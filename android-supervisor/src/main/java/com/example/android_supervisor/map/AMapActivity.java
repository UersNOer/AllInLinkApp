package com.example.android_supervisor.map;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.amap.api.maps.model.LatLng;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.geocoder.GeocodeResult;
import com.amap.api.services.geocoder.GeocodeSearch;
import com.amap.api.services.geocoder.RegeocodeQuery;
import com.amap.api.services.geocoder.RegeocodeResult;
import com.example.android_supervisor.Presenter.AreaCodePresenter;
import com.example.android_supervisor.R;
import com.example.android_supervisor.entities.GridRes;
import com.example.android_supervisor.entities.WorkGridSys;
import com.example.android_supervisor.sqlite.PrimarySqliteHelper;
import com.example.android_supervisor.ui.BaseActivity;
import com.example.android_supervisor.ui.fragment.AMapFragment;
import com.example.android_supervisor.utils.CoordinateUtils;
import com.example.android_supervisor.utils.LogUtils;
import com.example.android_supervisor.utils.ToastUtils;

import java.util.List;

import butterknife.ButterKnife;

/**
 * Created by yj on 2019/7/24.
 */
public class AMapActivity extends BaseActivity {

    private AMapFragment mapFragment;

    private LatLng centerPoint_jc02;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        ButterKnife.bind(this);

        mapFragment = new AMapFragment();
        Bundle bundle = getIntent().getExtras();
        mapFragment.setArguments(bundle);
        addMenu("确定", new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                LatLng centerPoint = mapFragment.getMapCenter();

                centerPoint_jc02 = centerPoint;

                if (mapFragment != null) {

                    double[] latLng84 = CoordinateUtils.gcj02ToWGS84(centerPoint.longitude, centerPoint.latitude);

                    if (latLng84 != null) {
                        centerPoint = new LatLng(latLng84[1], latLng84[0]);
                    }

                    AreaCodePresenter areaCodePresenter = new AreaCodePresenter();

                    LatLng finalCenterPoint = centerPoint;
                    areaCodePresenter.getAreaCode(AMapActivity.this, centerPoint, new AreaCodePresenter.AreaCodeCallBack() {
                        @Override
                        public void onSuccess(GridRes data) {
                            intent.putExtra("areaCode",data.getAreaCode());
                            intent.putExtra("gridId",data.getGridId());
                            intent.putExtra("workGridId",data.getWorkGridId());
                            intent.putExtra("manageGridId",data.getManageGridId());

                            intent.putExtra("areaName",data.getGrName());
                            setMapCallBack(finalCenterPoint);
                        }

                        @Override
                        public void onError() {
                            ToastUtils.show(AMapActivity.this,"获取网格失败!");
                            setMapCallBack(finalCenterPoint);
                        }
                    });


                }


                //  setActivityResult(centerPoint);
            }
        });

        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.map_container, mapFragment, "map")
                .commit();
    }

    Intent intent = new Intent();
    private void setMapCallBack(LatLng centerPoint) {
        if (centerPoint == null) {

            return;
        }
        double latitude = centerPoint.latitude;
        double longitude = centerPoint.longitude;
//        intent.putExtra("latitude", latitude);
//        intent.putExtra("longitude", longitude);
//
//        setResult(Activity.RESULT_OK, intent);
//        finish();
        LatLonPoint location = new LatLonPoint(centerPoint_jc02.latitude, centerPoint_jc02.longitude);
        RegeocodeQuery query = new RegeocodeQuery(location, 200, GeocodeSearch.AMAP);
        GeocodeSearch geocoderSearch = new GeocodeSearch(this);
        geocoderSearch.getFromLocationAsyn(query);
        geocoderSearch.setOnGeocodeSearchListener(new GeocodeSearch.OnGeocodeSearchListener() {
            @Override
            public void onRegeocodeSearched(RegeocodeResult regeocodeResult, int i) {

                if (regeocodeResult.getRegeocodeAddress() != null &&
                        regeocodeResult.getRegeocodeAddress().getPois() != null) {

//                    intent.putExtra("areaCode", areaCode); regeocodeResult.getRegeocodeAddress().getPois().get(0).getTitle()
                    intent.putExtra("address", regeocodeResult.getRegeocodeAddress().getFormatAddress());
                } else {
                    ToastUtils.show(AMapActivity.this, "当前未获取到位置信息");
                }
                LogUtils.e("latLng：" + latitude + "   " + longitude);

                intent.putExtra("latitude", latitude);
                intent.putExtra("longitude", longitude);

                setResult(Activity.RESULT_OK, intent);
                finish();
            }

            @Override
            public void onGeocodeSearched(GeocodeResult geocodeResult, int i) {

            }
        });


    }


    private void setActivityResult(LatLng centerPoint) {
        if (centerPoint == null) {
            setResult(Activity.RESULT_CANCELED);
            finish();
            return;
        }

        final double latitude = centerPoint.latitude;
        final double longitude = centerPoint.longitude;
        LatLonPoint location = new LatLonPoint(latitude, longitude);
        RegeocodeQuery query = new RegeocodeQuery(location, 200, GeocodeSearch.AMAP);
        GeocodeSearch geocoderSearch = new GeocodeSearch(this);
        geocoderSearch.getFromLocationAsyn(query);
        geocoderSearch.setOnGeocodeSearchListener(new GeocodeSearch.OnGeocodeSearchListener() {
            @Override
            public void onRegeocodeSearched(RegeocodeResult regeocodeResult, int rCode) {
                if (rCode != 1000) {
                    ToastUtils.show(AMapActivity.this, "获取区域信息失败");
                    return;
                } else {
                    if (regeocodeResult.getRegeocodeAddress() == null) {
                        ToastUtils.show(AMapActivity.this, "获取区域信息失败");
                        return;
                    } else {
                        String areaCode = regeocodeResult.getRegeocodeAddress().getAdCode();
                        Log.d("areaCode", "onRegeocodeSearched: " + areaCode);
                        if (areaCode == null) {
                            ToastUtils.show(AMapActivity.this, "获取区域信息失败");
                            return;
                        } else {
                            PrimarySqliteHelper sqliteHelper = PrimarySqliteHelper.getInstance(AMapActivity.this);
                            List<WorkGridSys> areas = sqliteHelper.getAreaGridDao().queryForAll();
                            if (areas != null && areas.size() > 0) {
                                boolean isArea = false;
                                for (WorkGridSys area : areas) {
                                    String myAreaCode = area.getWorkGridProperties().getSqCode();
                                    if (areaCode.equals(myAreaCode)) {
                                        isArea = true;
                                        break;
                                    }
                                }
                                if (isArea) {
                                    Intent intent = new Intent();
                                    intent.putExtra("latitude", latitude);
                                    intent.putExtra("longitude", longitude);
                                    intent.putExtra("areaCode", areaCode);
                                    if (regeocodeResult.getRegeocodeAddress().getPois() != null && regeocodeResult.getRegeocodeAddress().getPois().size() > 0) {
                                        intent.putExtra("address", regeocodeResult.getRegeocodeAddress().getPois().get(0).getTitle());
                                    } else {
                                        String add = regeocodeResult.getRegeocodeAddress().getDistrict() +
                                                regeocodeResult.getRegeocodeAddress().getTownship() +
                                                regeocodeResult.getRegeocodeAddress().getNeighborhood();
                                        intent.putExtra("address", add);
                                    }
                                    setResult(Activity.RESULT_OK, intent);
                                    finish();
                                } else
                                    ToastUtils.show(AMapActivity.this, "已超出管理区域，请重新选择位置");

                            }
                        }
                    }
                }
            }

            @Override
            public void onGeocodeSearched(GeocodeResult geocodeResult, int i) {

            }
        });
    }


    public void setTitleName(String titleName) {
        mTitleBar.setTitle(titleName);
    }


}
