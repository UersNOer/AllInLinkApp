package com.example.android_supervisor.map;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.tencent.lbssearch.TencentSearch;
import com.tencent.lbssearch.httpresponse.BaseObject;
import com.tencent.lbssearch.httpresponse.HttpResponseListener;
import com.tencent.lbssearch.object.Location;
import com.tencent.lbssearch.object.param.Geo2AddressParam;
import com.tencent.lbssearch.object.result.Geo2AddressResultObject;
import com.tencent.tencentmap.mapsdk.maps.model.LatLng;
import com.example.android_supervisor.R;
import com.example.android_supervisor.ui.BaseActivity;
import com.example.android_supervisor.utils.ToastUtils;

import butterknife.ButterKnife;

/**
 * @author wujie
 */
public class MapActivity extends BaseActivity {
    private MapFragment mapFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        ButterKnife.bind(this);

        mapFragment = new MapFragment();

        addMenu("确定", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LatLng centerPoint = mapFragment.getMapCenter();
                setActivityResult(centerPoint);
            }
        });

        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.map_container, mapFragment, "map")
                .commit();
    }

    private void setActivityResult(LatLng centerPoint) {
        if (centerPoint == null) {
            setResult(Activity.RESULT_CANCELED);
            finish();
            return;
        }

        final double latitude = centerPoint.latitude;
        final double longitude = centerPoint.longitude;
        Location location = new Location((float) latitude, (float) longitude);
        Geo2AddressParam param = new Geo2AddressParam().get_poi(true).location(location);
        new TencentSearch(this).geo2address(param, new HttpResponseListener() {

            @Override
            public void onSuccess(int statusCode, BaseObject baseObj) {
                Geo2AddressResultObject geoAddress = (Geo2AddressResultObject) baseObj;
                if (geoAddress.result == null || geoAddress.result.ad_info == null) {
                    ToastUtils.show(MapActivity.this, "获取区域信息失败");
                    return;
                }

                Geo2AddressResultObject.ReverseAddressResult.AdInfo adInfo = geoAddress.result.ad_info;
                String areaCode = adInfo.adcode;
                Log.d("areaCode", "onSuccess: "+areaCode);
                if (areaCode == null) {
                    ToastUtils.show(MapActivity.this, "获取区域信息失败");
                    return;
                }

//                   if (Environments.userBase.getDefaultDepartments().areaCode!=null &&
//                        !Environments.userBase.getDefaultDepartments().areaCode.equals(areaCode)){
//                    ToastUtils.show(MapActivity.this, "已超出管理区域，请重新选择位置");
//                    return;
//                }


                Intent intent = new Intent();
                intent.putExtra("latitude", latitude);
                intent.putExtra("longitude", longitude);
                intent.putExtra("areaCode", areaCode);

                if (geoAddress.result.pois != null && geoAddress.result.pois.size() > 0) {
                    intent.putExtra("address", geoAddress.result.pois.get(0).address);
                } else {
                    intent.putExtra("address", geoAddress.result.address);
                }

                setResult(Activity.RESULT_OK, intent);
                finish();
            }

            @Override
            public void onFailure(int statusCode, String errorMsg, Throwable throwable) {
                ToastUtils.show(MapActivity.this, "获取区域信息失败");
            }
        });
    }
}
