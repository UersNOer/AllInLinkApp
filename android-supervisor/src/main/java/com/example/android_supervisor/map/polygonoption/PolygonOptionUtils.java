package com.example.android_supervisor.map.polygonoption;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Handler;
import android.text.TextUtils;

import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.CoordinateConverter;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.PolygonOptions;
import com.example.android_supervisor.Presenter.AttendanceNetPresenter;
import com.example.android_supervisor.attendance.AttendanceManager;
import com.example.android_supervisor.cache.MemorySourceData;
import com.example.android_supervisor.cache.SourceData;
import com.example.android_supervisor.entities.WorkGridSys;
import com.example.android_supervisor.entities.WorkPlanData;
import com.example.android_supervisor.map.mark.CaseMarker;
import com.example.android_supervisor.utils.CoordinateUtils;
import com.example.android_supervisor.utils.Environments;
import com.example.android_supervisor.utils.LogUtils;
import com.example.android_supervisor.utils.ToastUtils;

import java.util.ArrayList;
import java.util.List;

public class PolygonOptionUtils implements PolyLoad{

    Context context;
    AMap aMap;

    CaseMarker caseMarker;

    SourceData sourceData =  new MemorySourceData();

    public PolygonOptionUtils(Context context, AMap aMap) {
        this.context = context;
        this.aMap = aMap;
        caseMarker = new CaseMarker(context,aMap,false);
    }


    @Override
    public void loadPoly() {

        if (sourceData.loadData()!=null){

            aMap.clear();

            if (sourceData.loadData().getWorkGridSys()!=null){

                PolygonOptions polygonOptions = new PolygonOptions();

                for (WorkGridSys areaGridSys : sourceData.loadData().getWorkGridSys()) {
                    List<LatLng> latLngs = new ArrayList<LatLng>();
                    for (int i = 0; i < areaGridSys.getGeometry().getCoordinates().get(0).get(0).size(); i++) {

                        LatLng latLng =  new LatLng(areaGridSys.getGeometry().getCoordinates().get(0).get(0).get(i).get(1),
                                areaGridSys.getGeometry().getCoordinates().get(0).get(0).get(i).get(0));

                        double[] latLng02 =  CoordinateUtils.wgs84ToGcj02(latLng.longitude, latLng.latitude);

                        if (latLng02!=null){
                            latLng = new LatLng(latLng02[1],latLng02[0]);

                            LatLng sourceLatLng =  new LatLng(areaGridSys.getGeometry().getCoordinates().get(0).get(0).get(i).get(1),
                                    areaGridSys.getGeometry().getCoordinates().get(0).get(0).get(i).get(0));
                            CoordinateConverter converter  = new CoordinateConverter(context);
                            converter.from(CoordinateConverter.CoordType.GPS);
                            converter.coord(sourceLatLng);
                            LatLng desLatLng = converter.convert();

                            LogUtils.e("sourceLatLng:"+latLng.longitude+"--"+latLng.latitude +"----"+ desLatLng.longitude+"----"+desLatLng.latitude);
                        }
                        latLngs.add(latLng);
                    }

                    polygonOptions.addAll(latLngs);
                    polygonOptions.strokeWidth(10) // 多边形的边框
                            .strokeColor(Color.RED) // 边框颜色
                            .fillColor(Color.argb(127,255,0,255));   // 多边形的填充色


                    aMap.addPolygon(polygonOptions);

                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            aMap.animateCamera(CameraUpdateFactory.newLatLngZoom(getCenterOfGravityPoint(latLngs), 16));
                        }
                    },300);

                }


                if (sourceData.loadData().getWorkGridSys().size()>0){
                    loadMark();
                }

            }else {
                ToastUtils.show(context,"当前未做排班");
                ((Activity)context).finish();
                if (Environments.userBase!=null &&!TextUtils.isEmpty(Environments.userBase.getAreaCoordinateStr())){
                    try{
                        String[] latlng_str = Environments.userBase.getAreaCoordinateStr().split(",");
                        if (latlng_str.length ==2){
                            LatLng latLng = new LatLng(Double.valueOf(latlng_str[1]),Double.valueOf(latlng_str[0]));
                            if (latLng==null){
                                return;
                            }
                            aMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 16));
                        }
                    }catch (Exception e){

                    }
                }else {

                }
            }

        }



    }

    private void loadMark() {

        AttendanceManager.getInstance(context).loadAttendancePoint(new AttendanceNetPresenter.AttendanceCallBack() {
            @Override
            public void onSuccess(List<WorkPlanData> data) {
                caseMarker.loadAttendance(data);
            }
        });



    }

    /**
     * 获取工作网格
     */
//    private void initGridView() {
//        PrimarySqliteHelper sqliteHelper = PrimarySqliteHelper.getInstance(getContext());
//        List<WorkGridSys> mAreaGridList = sqliteHelper.getAreaGridDao().queryForAll();
//        Log.d("dawn", "initGridView: " + mAreaGridList.size());
//        for (WorkGridSys areaGridSys : mAreaGridList) {
//            List<LatLng> latLngs = new ArrayList<LatLng>();
//            for (int i = 0; i < areaGridSys.getGeometry().getCoordinates().get(0).get(0).size(); i++) {
//                latLngs.add(new LatLng(areaGridSys.getGeometry().getCoordinates().get(0).get(0).get(i).get(1), areaGridSys.getGeometry().getCoordinates().get(0).get(0).get(i).get(0)));
//            }
//            aMap.addPolyline(new PolylineOptions().addAll(latLngs).width(5).color(Color.argb(255, 200, 1, 1)));
//        }
//    }


    public static LatLng getCenterOfGravityPoint(List<LatLng> mPoints) {
        double area = 0.0;//多边形面积
        double Gx = 0.0, Gy = 0.0;// 重心的x、y
        for (int i = 1; i <= mPoints.size(); i++) {
            double iLat = mPoints.get(i % mPoints.size()).latitude;
            double iLng = mPoints.get(i % mPoints.size()).longitude;
            double nextLat = mPoints.get(i - 1).latitude;
            double nextLng = mPoints.get(i - 1).longitude;
            double temp = (iLat * nextLng - iLng * nextLat) / 2.0;
            area += temp;
            Gx += temp * (iLat + nextLat) / 3.0;
            Gy += temp * (iLng + nextLng) / 3.0;
        }
        Gx = Gx / area;
        Gy = Gy / area;
        return new LatLng(Gx, Gy);
    }

}
