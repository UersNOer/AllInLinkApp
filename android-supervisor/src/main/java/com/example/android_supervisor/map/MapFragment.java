package com.example.android_supervisor.map;

import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;

import com.tencent.lbssearch.TencentSearch;
import com.tencent.lbssearch.httpresponse.BaseObject;
import com.tencent.lbssearch.httpresponse.HttpResponseListener;
import com.tencent.lbssearch.object.param.DistrictChildrenParam;
import com.tencent.lbssearch.object.param.SearchParam;
import com.tencent.lbssearch.object.result.DistrictResultObject;
import com.tencent.lbssearch.object.result.SearchResultObject;
import com.tencent.map.geolocation.TencentLocation;
import com.tencent.map.geolocation.TencentLocationListener;
import com.tencent.map.geolocation.TencentLocationUtils;
import com.tencent.tencentmap.mapsdk.maps.CameraUpdate;
import com.tencent.tencentmap.mapsdk.maps.CameraUpdateFactory;
import com.tencent.tencentmap.mapsdk.maps.LocationSource;
import com.tencent.tencentmap.mapsdk.maps.MapView;
import com.tencent.tencentmap.mapsdk.maps.TencentMap;
import com.tencent.tencentmap.mapsdk.maps.TencentMapOptions;
import com.tencent.tencentmap.mapsdk.maps.UiSettings;
import com.tencent.tencentmap.mapsdk.maps.model.BitmapDescriptorFactory;
import com.tencent.tencentmap.mapsdk.maps.model.CameraPosition;
import com.tencent.tencentmap.mapsdk.maps.model.LatLng;
import com.tencent.tencentmap.mapsdk.maps.model.Marker;
import com.tencent.tencentmap.mapsdk.maps.model.MarkerOptions;
import com.tencent.tencentmap.mapsdk.maps.model.MyLocationStyle;
import com.example.android_supervisor.R;
import com.example.android_supervisor.common.ResultCallback;
import com.example.android_supervisor.common.TXLocationApi;
import com.example.android_supervisor.config.AppConfig;
import com.example.android_supervisor.ui.fragment.BaseFragment;
import com.example.android_supervisor.utils.SystemUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @author wujie
 */
public class MapFragment extends BaseFragment implements LocationSource, TencentMap.OnMapLoadedCallback,
        TencentMap.OnCameraChangeListener, TencentMap.OnMarkerClickListener, TencentLocationListener {
    @BindView(R.id.mapview)
    MapView mapView;

    @BindView(R.id.et_map_address_search)
    AutoCompleteTextView etSearch;

    protected TencentMap tencentMap;
    private OnLocationChangedListener locationChangedListener;

    private List<Marker> poiMarkers = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_map, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        initSearchView();

        tencentMap = mapView.getMap();
        tencentMap.setSatelliteEnabled(false);
        tencentMap.setTrafficEnabled(false);
        tencentMap.setOnMapLoadedCallback(this);
        tencentMap.setOnCameraChangeListener(this);
        tencentMap.setOnMarkerClickListener(this);

        UiSettings uiSettings = tencentMap.getUiSettings();
        uiSettings.setLogoPosition(TencentMapOptions.LOGO_POSITION_BOTTOM_LEFT);
        uiSettings.setScaleViewPosition(TencentMapOptions.SCALEVIEW_POSITION_BOTTOM_RIGHT);
        uiSettings.setMyLocationButtonEnabled(true);
        uiSettings.setZoomControlsEnabled(true);
        uiSettings.setZoomGesturesEnabled(true);

        tencentMap.setMyLocationEnabled(true);
        tencentMap.setLocationSource(this);
        tencentMap.setMyLocationStyle(new MyLocationStyle()
                .myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATION_ROTATE_NO_CENTER));
    }

    private void initSearchView() {
        final SearchSuggestionAdapter adapter = new SearchSuggestionAdapter(getContext());
        adapter.setRegion(AppConfig.getDefaultAreaCode(getContext()));
        etSearch.setAdapter(adapter);
        etSearch.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String address = adapter.getItem(position).toString();
                searchPoi(address);
            }
        });
    }

    @Override
    public final void activate(OnLocationChangedListener listener) {
        locationChangedListener = listener;
    }

    @Override
    public final void deactivate() {
        locationChangedListener = null;
    }

    @Override
    public void onMapLoaded() {
        requestMyLocation();
//        loadMapBounds();
    }

    private void requestMyLocation() {
        TXLocationApi.requestSingleLocation(getContext(), TXLocationApi.COORDINATE_TYPE_GCJ02, new ResultCallback<TencentLocation>() {
            @Override
            public void onResult(TencentLocation result, int tag) {

            }

            @Override
            public void onResult(TencentLocation location) {
                if (locationChangedListener != null) {
                    locationChangedListener.onLocationChanged(convertLocalLocation(location));
                }
                LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
                CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, 16);
                tencentMap.moveCamera(cameraUpdate);
            }
        });
    }

    private void loadMapBounds() {
        final String areaCode = AppConfig.getDefaultAreaCode(getContext());
        DistrictChildrenParam param = new DistrictChildrenParam();
        param.id(Integer.valueOf(areaCode));
        new TencentSearch(getContext()).getDistrictChildren(param, new HttpResponseListener() {
            @Override
            public void onSuccess(int i, BaseObject baseObj) {
                DistrictResultObject resultObj = (DistrictResultObject) baseObj;
                if (resultObj.result != null) {
                    if (resultObj.result.size() > 0) {
                        List<DistrictResultObject.DistrictResult> districts = resultObj.result.get(0);
                        for (DistrictResultObject.DistrictResult district : districts) {
                            if (String.valueOf(district.id).startsWith(areaCode)) {
                                LatLng latLng = new LatLng(district.location.lat, district.location.lng);
                                CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, 16);
                                tencentMap.moveCamera(cameraUpdate);
                                break;
                            }
                        }
                    }
                }
            }

            @Override
            public void onFailure(int i, String s, Throwable throwable) {
                throwable.printStackTrace();
            }
        });
    }

    @Override
    public void onCameraChange(CameraPosition cameraPosition) {
        // Todo
    }

    @Override
    public void onCameraChangeFinished(CameraPosition cameraPosition) {
        if (cameraPosition.zoom < 16) {
            for (Marker marker : poiMarkers) {
                marker.setVisible(false);
            }
        } else {
            for (Marker marker : poiMarkers) {
                marker.setVisible(true);
            }
        }
    }

    public LatLng getMapCenter() {
        return tencentMap.getCameraPosition().target;
    }

    @OnClick(R.id.iv_map_address_clear)
    public void onSearchClear(View v) {
        etSearch.setText(null);
        clearPoiMarkers();
    }

    private void searchPoi(String keyword) {
        clearPoiMarkers();

        SearchParam param = new SearchParam();
        param.keyword(keyword);
        param.boundary(new SearchParam.Region().poi(AppConfig.getDefaultAreaCode(getContext())).autoExtend(true));
        new TencentSearch(getContext()).search(param, new HttpResponseListener() {

            @Override
            public void onSuccess(int i, BaseObject baseObj) {
                SearchResultObject searchResult = (SearchResultObject) baseObj;
                for (SearchResultObject.SearchResultData searchData : searchResult.data) {
                    if (searchData.location != null) {
                        float lat = searchData.location.lat;
                        float lng = searchData.location.lng;
                        poiMarkers.add(tencentMap.addMarker(new MarkerOptions()
                                .position(new LatLng(lat, lng))
                                .anchor(.5f, .5f)
                                .icon(BitmapDescriptorFactory.fromResource(R.drawable.tmap_poi))));
                    }
                }
                if (searchResult.data.size() > 0) {
                    SearchResultObject.SearchResultData searchData = searchResult.data.get(0);
                    LatLng latLng = new LatLng(searchData.location.lat, searchData.location.lng);
                    CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, 18);
                    tencentMap.moveCamera(cameraUpdate);
                }
                SystemUtils.hideSoftInput(getContext(), etSearch);
            }

            @Override
            public void onFailure(int i, String s, Throwable throwable) {
                throwable.printStackTrace();
                SystemUtils.hideSoftInput(getContext(), etSearch);
            }
        });
    }

    public void clearPoiMarkers() {
        for (Marker marker : poiMarkers) {
            marker.remove();
        }
        poiMarkers.clear();
    }

    @Override
    public void onStart() {
        TXLocationApi.addLocationListener(this);
        mapView.onStart();
        super.onStart();
    }

    @Override
    public void onResume() {
        mapView.onResume();
        super.onResume();
    }

    @Override
    public void onPause() {
        mapView.onPause();
        super.onPause();
    }

    @Override
    public void onStop() {
        TXLocationApi.removeLocationListener(this);
        mapView.onStop();
        super.onStop();
    }

    @Override
    public void onDestroy() {
        mapView.onDestroy();
        super.onDestroy();
    }

    @Override
    public void onLocationChanged(TencentLocation location, int error, String reason) {
        if (error == TencentLocation.ERROR_OK) {
            if (locationChangedListener != null) {
                locationChangedListener.onLocationChanged(convertLocalLocation(location));
            }
        }
    }

    private Location convertLocalLocation(TencentLocation tencentLocation) {
        Location localLocation = new Location(tencentLocation.getProvider());
        if (tencentLocation.getCoordinateType() == TXLocationApi.COORDINATE_TYPE_GCJ02) {
            localLocation.setLatitude(tencentLocation.getLatitude());
            localLocation.setLongitude(tencentLocation.getLongitude());
        } else {
            double[] latlng = new double[2];
            if (TencentLocationUtils.wgs84ToGcj02(new double[]{
                    tencentLocation.getLatitude(),
                    tencentLocation.getLongitude()},
                    latlng)) {
                localLocation.setLatitude(latlng[0]);
                localLocation.setLongitude(latlng[1]);
            } else {
                localLocation.setLatitude(tencentLocation.getLatitude());
                localLocation.setLongitude(tencentLocation.getLongitude());
            }
        }
        localLocation.setAltitude(tencentLocation.getAltitude());
        localLocation.setAccuracy(tencentLocation.getAccuracy());
        localLocation.setBearing(tencentLocation.getBearing());
        localLocation.setSpeed(tencentLocation.getSpeed());
        localLocation.setTime(tencentLocation.getTime());
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.JELLY_BEAN) {
            localLocation.setElapsedRealtimeNanos(tencentLocation.getElapsedRealtime());
        }
        return localLocation;
    }

    @Override
    public void onStatusUpdate(String status, int error, String reason) {

    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        return false;
    }
}
