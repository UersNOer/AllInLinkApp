package com.example.android_supervisor.map;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;

import com.tencent.lbssearch.TencentSearch;
import com.tencent.lbssearch.httpresponse.BaseObject;
import com.tencent.lbssearch.httpresponse.HttpResponseListener;
import com.tencent.lbssearch.object.Location;
import com.tencent.lbssearch.object.param.CoordTypeEnum;
import com.tencent.lbssearch.object.param.Geo2AddressParam;
import com.tencent.lbssearch.object.result.Geo2AddressResultObject;
import com.tencent.map.geolocation.TencentLocation;
import com.tencent.map.geolocation.TencentLocationListener;
import com.tencent.map.geolocation.TencentLocationUtils;
import com.tencent.map.geolocation.TencentPoi;
import com.example.android_supervisor.common.TXLocationApi;

import java.util.ArrayList;
import java.util.List;

/**
 * @author wujie
 */
@Deprecated
public abstract class TencentLocationAdapter implements TencentLocationListener {
    private Context context;
    private int coordinateType;

    public TencentLocationAdapter(Context context, int coordinateType) {
        this.context = context;
        this.coordinateType = coordinateType;
    }

    @Override
    public final void onLocationChanged(final TencentLocation location, int error, String reason) {
        if (error != TencentLocation.ERROR_OK) {
            onError(error, reason);
            return;
        }

        float latitude = (float) location.getLatitude();
        float longitude = (float) location.getLongitude();

        CoordTypeEnum coordType;
        if (location.getCoordinateType() == TXLocationApi.COORDINATE_TYPE_WGS84) {
            coordType = CoordTypeEnum.GPS;
        } else {
            coordType = CoordTypeEnum.DEFAULT;
        }

        Geo2AddressParam param = new Geo2AddressParam()
                .coord_type(coordType)
                .get_poi(true)
                .location(new Location(latitude, longitude));

        new TencentSearch(context).geo2address(param, new HttpResponseListener() {

            @Override
            public void onSuccess(int statusCode, BaseObject baseObj) {
                Geo2AddressResultObject geoAddress = (Geo2AddressResultObject) baseObj;
                onLocationChanged(new TencentLocationWrapper(location, coordinateType, geoAddress.result));
            }

            @Override
            public void onFailure(int statusCode, String errorMsg, Throwable throwable) {
                onLocationChanged(new TencentLocationWrapper(location, coordinateType, null));
            }
        });
    }

    public abstract void onLocationChanged(TencentLocation location);

    public abstract void onError(int error, String reason);

    @Override
    public void onStatusUpdate(String status, int error, String reason) {
        // Todo
    }

    static class TencentLocationWrapper implements TencentLocation {
        final TencentLocation location;
        final int coordinateType;
        final double latitude;
        final double longitude;
        final Geo2AddressResultObject.ReverseAddressResult addressResult;

        public TencentLocationWrapper(TencentLocation location,
                                      int coordinateType,
                                      Geo2AddressResultObject.ReverseAddressResult addressResult) {
            this.location = location;
            this.coordinateType = coordinateType;
            this.addressResult = addressResult;

            final double lat = location.getLatitude();
            final double lng = location.getLongitude();

            if (coordinateType == TXLocationApi.COORDINATE_TYPE_GCJ02
                    && location.getCoordinateType() == TXLocationApi.COORDINATE_TYPE_WGS84) {
                double[] latlng = new double[2];
                if (TencentLocationUtils.wgs84ToGcj02(new double[]{lat, lng}, latlng)) {
                    this.latitude = latlng[0];
                    this.longitude = latlng[1];
                } else {
                    this.latitude = lat;
                    this.longitude = lng;
                }
            } else {
                this.latitude = lat;
                this.longitude = lng;
            }
        }

        @Override
        public String getProvider() {
            return location.getProvider();
        }

        @Override
        public double getLatitude() {
            return this.latitude;
        }

        @Override
        public double getLongitude() {
            return this.longitude;
        }

        @Override
        public double getAltitude() {
            return location.getAltitude();
        }

        @Override
        public float getAccuracy() {
            return location.getAccuracy();
        }

        @Override
        public String getName() {
            String name = location.getName();
            if (TextUtils.isEmpty(name)) {
                if (addressResult != null && addressResult.ad_info != null) {
                    name = addressResult.ad_info.name;
                }
            }
            return name;
        }

        @Override
        public String getAddress() {
            String address = location.getAddress();
            if (TextUtils.isEmpty(address)) {
                if (addressResult != null) {
                    address = addressResult.address;
                }
            }
            return address;
        }

        @Override
        public String getNation() {
            String nation = location.getNation();
            if (TextUtils.isEmpty(nation)) {
                if (addressResult != null && addressResult.ad_info != null) {
                    nation = addressResult.ad_info.nation;
                }
            }
            return nation;
        }

        @Override
        public String getProvince() {
            String province = location.getProvince();
            if (TextUtils.isEmpty(province)) {
                if (addressResult != null && addressResult.ad_info != null) {
                    province = addressResult.ad_info.province;
                }
            }
            return province;
        }

        @Override
        public String getCity() {
            String city = location.getCity();
            if (TextUtils.isEmpty(city)) {
                if (addressResult != null && addressResult.ad_info != null) {
                    city = addressResult.ad_info.city;
                }
            }
            return city;
        }

        @Override
        public String getDistrict() {
            String district = location.getDistrict();
            if (TextUtils.isEmpty(district)) {
                if (addressResult != null && addressResult.ad_info != null) {
                    district = addressResult.ad_info.district;
                }
            }
            return district;
        }

        @Override
        public String getTown() {
            return location.getTown();
        }

        @Override
        public String getVillage() {
            return location.getVillage();
        }

        @Override
        public String getStreet() {
            return location.getStreet();
        }

        @Override
        public String getStreetNo() {
            return location.getStreetNo();
        }

        @Override
        public Integer getAreaStat() {
            return location.getAreaStat();
        }

        @Override
        public List<TencentPoi> getPoiList() {
            List<TencentPoi> pois = location.getPoiList();
            if (pois == null) {
                pois = new ArrayList<>();
            }
            if (pois.isEmpty()) {
                if (addressResult != null && addressResult.pois != null) {
                    for (Geo2AddressResultObject.ReverseAddressResult.Poi poi : addressResult.pois) {
                        pois.add(new TencentPoiWrapper(poi));
                    }
                }
            }
            return pois;
        }

        @Override
        public float getBearing() {
            return location.getBearing();
        }

        @Override
        public float getSpeed() {
            return location.getSpeed();
        }

        @Override
        public long getTime() {
            return location.getTime();
        }

        @Override
        public long getElapsedRealtime() {
            return location.getElapsedRealtime();
        }

        @Override
        public int getGPSRssi() {
            return location.getGPSRssi();
        }

        @Override
        public String getIndoorBuildingId() {
            return location.getIndoorBuildingId();
        }

        @Override
        public String getIndoorBuildingFloor() {
            return location.getIndoorBuildingFloor();
        }

        @Override
        public int getIndoorLocationType() {
            return location.getIndoorLocationType();
        }

        @Override
        public double getDirection() {
            return location.getDirection();
        }

        @Override
        public String getCityCode() {
            String cityCode = location.getCityCode();
            if (TextUtils.isEmpty(cityCode)) {
                if (addressResult != null && addressResult.ad_info != null) {
                    cityCode = addressResult.ad_info.adcode;
                }
            }
            return cityCode;
        }

        @Override
        public int getCoordinateType() {
            return this.coordinateType;
        }

        @Override
        public int isMockGps() {
            return location.isMockGps();
        }

        @Override
        public Bundle getExtra() {
            return location.getExtra();
        }
    }

    static class TencentPoiWrapper implements TencentPoi {
        Geo2AddressResultObject.ReverseAddressResult.Poi poi;

        public TencentPoiWrapper(Geo2AddressResultObject.ReverseAddressResult.Poi poi) {
            this.poi = poi;
        }

        @Override
        public String getName() {
            return poi == null ? null : poi.title;
        }

        @Override
        public String getAddress() {
            return poi == null ? null : poi.address;
        }

        @Override
        public String getCatalog() {
            return null;
        }

        @Override
        public double getDistance() {
            return poi == null ? 0 : poi._distance;
        }

        @Override
        public double getLatitude() {
            return poi == null ? 0 : poi.location == null ? 0 : poi.location.lat;
        }

        @Override
        public double getLongitude() {
            return poi == null ? 0 : poi.location == null ? 0 : poi.location.lng;
        }

        @Override
        public String getUid() {
            return poi == null ? null : poi.id;
        }

        @Override
        public String getDirection() {
            return null;
        }
    }
}
