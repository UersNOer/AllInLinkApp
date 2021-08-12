package com.example.android_supervisor.map.mark;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

//import com.amap.api.maps.AMap;
//import com.amap.api.maps.CameraUpdateFactory;
//import com.amap.api.maps.model.BitmapDescriptorFactory;
//import com.amap.api.maps.model.LatLng;
//import com.amap.api.maps.model.Marker;
//import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.bumptech.glide.Glide;
import com.example.android_supervisor.R;
import com.example.android_supervisor.common.UserSession;
import com.example.android_supervisor.entities.CensusEventRes;
import com.example.android_supervisor.entities.EventRes;
import com.example.android_supervisor.entities.LayerConfig;
import com.example.android_supervisor.entities.WorkPlanData;
import com.example.android_supervisor.http.ServiceGenerator;
import com.example.android_supervisor.http.common.ProgressTransformer;
import com.example.android_supervisor.http.request.QueryBody;
import com.example.android_supervisor.http.response.Response;
import com.example.android_supervisor.http.response.ResponseObserver;
import com.example.android_supervisor.http.service.QuestionService;
import com.example.android_supervisor.sqlite.PublicSqliteHelper;
import com.example.android_supervisor.ui.view.ProgressText;
import com.example.android_supervisor.utils.CoordinateUtils;
import com.example.android_supervisor.utils.ToastUtils;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;

/**
 * Created by yj on 2019/9/17.
 */
public class CaseMarker {
    public Context context;
    public AMap aMap;
    private Map<String, Bitmap> caseConfig = new HashMap<>();
    private List<EventRes> eventResList = new ArrayList<>();
    private List<Marker> poiMarkers = new ArrayList<>();


    public CaseMarker(Context context, AMap aMap,boolean isFlag) {
        this.aMap = aMap;
        this.context = context;
        if (isFlag){
            initCaseView();
            //obtainMarkerData();//先加载一次
        }else {
//            aMap.setOnMarkerClickListener(new AMap.OnMarkerClickListener() {
//                @Override
//                public boolean onMarkerClick(Marker marker) {
//                    try{
//
//                        WorkPlanData.PointListBean  point = (WorkPlanData.PointListBean) marker.getObject();
//
//                       if (!AttendanceManager.getInstance(context).checkPointValid(point)){
//                           ToastUtils.show(context,"请在指定时间和指定区域打卡");
//                           return  true;
//
//                       }
//
//                        if (point!=null && point.getPunchStatus().equals("0")){
//
//                            WorkPlanData workPlanData = new WorkPlanData();
//
//                            workPlanData.setWorkPlanId(point.getWorkPlanId());
//                            workPlanData.setWorkGridCode(point.getWorkGridCode());
//
//                            AttendanceManager.getInstance(context).setCallBack(new AttendanceManager.CallBack() {
//                                @Override
//                                public void onSuccess(WorkPlanData workPlanData) {
//
//                                    if (workPlanData!=null){
//                                        AttendanceManager.getInstance(context).loadAttendancePoint(new AttendanceNetPresenter.AttendanceCallBack() {
//                                            @Override
//                                            public void onSuccess(List<WorkPlanData> data) {
//                                                loadAttendance(data);
//                                            }
//                                        });
//                                    }
//
//                                }
//                            });
//                            AttendanceManager.getInstance(context).setDialog(new ProgressDialog(context));
//                            AttendanceManager.getInstance(context).Clock1(workPlanData,point,null,true);
//
//                        }
//
//                    }catch (Exception e){
//
//                    }
//                    return true;
//                }
//            });
        }


    }

    /**
     * 案件图层
     */
    public void initCaseView() {

        new Thread(new Runnable() {
            @Override
            public void run() {

                List<LayerConfig> layerConfig = PublicSqliteHelper.getInstance(context).getLayerConfigDao().queryForAll();
                String baseUrl = UserSession.getFileServer(context);

//                if ((layerConfig == null || layerConfig.size() < 0) || TextUtils.isEmpty(baseUrl)) {
//                    ToastUtils.show(context, "请先进行数据同步");
//                }
                if (layerConfig != null) {
                    for (LayerConfig config : layerConfig) {
                        String path = config.getLayerUrl();
                        if (path.contains("http") || path.contains("https")) {
                            String[] split = path.split("/");
                            path = split[split.length - 1];
                        }
                        if (!path.contains(".jpg") && !path.contains(".png")) {
                            path = new StringBuilder(path).append(".jpg").toString();
                        }
                        final String pathValue = new StringBuilder(baseUrl).append(path).toString();

                        Bitmap caseIcon = null;
                        try {
                            HttpURLConnection connection = (HttpURLConnection) new URL(pathValue).openConnection();
                            connection.setConnectTimeout(5000);
                            connection.setRequestMethod("GET");
                            connection.connect();

                            if (connection.getResponseCode() == 200) {
                                caseIcon = Glide.with(context).asBitmap().load(pathValue).submit(70, 70).get();
                                caseConfig.put(config.getLayerCode(), caseIcon);
                            } else {
                                caseIcon = Glide.with(context).asBitmap().load(R.drawable.tmap_event).submit(70, 70).get();
                                caseConfig.put(config.getLayerCode(), caseIcon);
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        } catch (ExecutionException e) {
                            e.printStackTrace();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }).start();


    }


    /**
     * 获取案件数据  核实核查
     */
    public void obtainMarkerData() {

        String handlerId = UserSession.getUserId(context);
        QueryBody queryBody = new QueryBody.Builder().pageSize(-1)
                .put("handlerId", handlerId)
                .create();
        QuestionService questionService = ServiceGenerator.create(QuestionService.class);
        questionService.getCaseEventListInMap(queryBody)
                .compose(new ProgressTransformer<Response<List<EventRes>>>(context, ProgressText.refreshCase))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new ResponseObserver<List<EventRes>>(context) {
                    @Override
                    public void onSuccess(List<EventRes> data) {
                        eventResList = new ArrayList<>();//layer_video    layer_car  layer_staff  layer_case
                        for (EventRes datum : data) {

//                            超时类型 1表示绿灯（时间在规定处理时限的一半以上） 0表示黄灯（未超时但是在一半以下） -1表示红灯(超时)
//                            if (datum.getSurplusType().equals("-1")) {
//                                datum.setLayerUrl(caseConfig.get("layer_case"));
//                            } else if (datum.getSurplusType().equals("0")) {
//                                datum.setLayerUrl(caseConfig.get("layer_staff"));
//                            } else if (datum.getSurplusType().equals("1")){
//                                datum.setLayerUrl(caseConfig.get("layer_case"));
//                            }else {
//                                datum.setLayerUrl(caseConfig.get("layer_car"));
//                            }


                            double[] latLng_Gcj02  =  CoordinateUtils.wgs84ToGcj02(datum.getGeoX(), datum.getGeoY());
                            if (latLng_Gcj02!=null){
                                datum.setGeoX(latLng_Gcj02[0]);
                                datum.setGeoY(latLng_Gcj02[1]);
                            }
                            eventResList.add(datum);

                        }
                        setAmapMarker(eventResList);
                    }
                });
    }


    /**
     * 绘制marker 案件数据
     *
     * @param resList
     */
    private void setAmapMarker(List<EventRes> resList) {

//        clearPoiMarkers();

        LatLng latLng = null;
        for (final EventRes eventRes : resList) {
            latLng = new LatLng(eventRes.getGeoY(), eventRes.getGeoX());
            MarkerOptions markerOptions = new MarkerOptions().position(latLng).title(eventRes.getTitle());

            if(eventRes.getSurplusType().equals("1")){

                markerOptions.icon(BitmapDescriptorFactory.fromView(View.inflate(context, R.layout.case_map_green,null)));
            }else if (eventRes.getSurplusType().equals("0")){
                markerOptions.icon(BitmapDescriptorFactory.fromView(View.inflate(context,R.layout.case_map_yellow,null)));
            }else if (eventRes.getSurplusType().equals("-1")){
                markerOptions.icon(BitmapDescriptorFactory.fromView(View.inflate(context,R.layout.case_map_red,null)));
            }
            Marker marker = aMap.addMarker(markerOptions);
            marker.setObject(eventRes);
            poiMarkers.add(marker);
        }

        if (latLng != null) {
            aMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 16));
        }

    }

    public void clearPoiMarkers() {
        for (Marker marker : poiMarkers) {
            marker.remove();
        }
        poiMarkers.clear();

    }


    // 加载待办案件
    public void loadTodoEvents(String taskId) {

        clearPoiMarkers();
        aMap.clear();

        QueryBody queryBody = new QueryBody.Builder()
                .pageIndex(1)
                .pageSize(100)
                .eq("censusTaskId", taskId)
                .eq("createId", UserSession.getUserId(context))
                .desc("reportTime")
                .create();

        QuestionService questionService = ServiceGenerator.create(QuestionService.class);
        Observable<Response<List<CensusEventRes>>> observable = questionService.getCensusEventList(queryBody);

        observable.observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Response<List<CensusEventRes>>>() {
                    @Override
                    public void accept(Response<List<CensusEventRes>> response) throws Exception {
                        if (response.isSuccess()) {
                            List<CensusEventRes> data = response.getData();
                            LatLng latLng = null;

                            for (CensusEventRes evtRes : data) {
                                evtRes.taskId = taskId;
                                latLng = new LatLng(evtRes.getGeoY(), evtRes.getGeoX());


                                double[] latLng_Gcj02  =  CoordinateUtils.wgs84ToGcj02(evtRes.getGeoX(), evtRes.getGeoY());
                                if (latLng_Gcj02!=null){
                                    latLng = new LatLng(latLng_Gcj02[1],latLng_Gcj02[0]);
                                }

                                MarkerOptions markerOptions = new MarkerOptions().position(latLng).title(evtRes.getTitle());
                                markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.tmap_event));
                                Marker marker = aMap.addMarker(markerOptions);
                                marker.setObject(evtRes);
                                poiMarkers.add(marker);

                            }

                            if (latLng != null) {
                                aMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 14));
                            }

                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        ToastUtils.show(context, "案件加载失败");
                    }
                });
    }


    public void loadAttendance(List<WorkPlanData> data){

        if (data==null && data.size()==0){
            return;
        }
        clearPoiMarkers();

        for (WorkPlanData workPlanData : data) {

            if (workPlanData.getPointList()!=null && workPlanData.getPointList().size()>0){

                for (WorkPlanData.PointListBean point :workPlanData.getPointList()){

                    LatLng latLng = new LatLng(Double.valueOf(point.getAbsY()),Double.valueOf(point.getAbsX()));

                    double[] latLng_Gcj02  =  CoordinateUtils.wgs84ToGcj02(Double.valueOf(point.getAbsX()),
                            Double.valueOf(point.getAbsY()));
                    if (latLng_Gcj02!=null){
                        latLng = new LatLng(latLng_Gcj02[1],latLng_Gcj02[0]);
                    }


                    MarkerOptions markerOptions = new MarkerOptions().position(latLng).title(point.getPointName());

                    View markView = View.inflate(context, R.layout.poi_overlay, null);

                    TextView tv_poiName = markView.findViewById(R.id.tv_poiName);
                    tv_poiName.setText(point.getPointName());
                    LinearLayout ll_pop = markView.findViewById(R.id.ll_pop);
//                    ll_pop.setBackgroundTintList(new CloStapoint.getPunchStatus().equals("0")?Color.RED:Color.parseColor("#00ff00"));
                    if (point.getPunchStatus().equals("1")){
                        //ll_pop.setBackgroundTintList(Drawable.R.drawable.popup);
                        ll_pop.getBackground().setColorFilter(Color.parseColor("#00ff00"),PorterDuff.Mode.SRC_ATOP);
                    }else {
                        ll_pop .getBackground().setColorFilter(Color.parseColor("#D11B0D"),PorterDuff.Mode.SRC_ATOP);
                    }
                    tv_poiName.setTextColor(point.getPunchStatus().equals("1")?Color.BLACK:Color.WHITE);
                    markerOptions.icon(BitmapDescriptorFactory.fromView(markView));

                    point.setWorkGridCode(workPlanData.getWorkGridCode());
                    point.setWorkPlanId(workPlanData.getWorkPlanId());

                    Marker marker = aMap.addMarker(markerOptions);
                    marker.setObject(point);

                    poiMarkers.add(marker);
                }
            }

        }

    }

    public void setMarker(LatLng myLatlng,EventRes eventRes,AMap.OnMarkerClickListener onMarkerClickListener) {

        ImageView imageView = new ImageView(context);
        imageView.setBackgroundResource(R.drawable.tmap_event);
        LinearLayout.LayoutParams mParams = new LinearLayout.LayoutParams(100, 100);
        imageView.setLayoutParams(mParams);
//        imageView.getLayoutParams().height = DensityUtils.dp2px(context,80);
//        imageView.getLayoutParams().width = DensityUtils.dp2px(context,80);

        Marker marker = aMap.addMarker(new MarkerOptions()
                .anchor(0.5f, 0.5f)
                .position(myLatlng)
                .icon(BitmapDescriptorFactory.fromView(imageView)));
        marker.setObject(eventRes);
//        poiMarkers.add(marker);
        aMap.setOnMarkerClickListener(onMarkerClickListener);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                aMap.animateCamera(CameraUpdateFactory.newLatLngZoom(myLatlng, 16));
            }
        },600);


    }

    public void setMarker(LatLng myLatlng) {


        Marker marker = aMap.addMarker(new MarkerOptions()
                .anchor(0.5f, 0.5f)
                .position(myLatlng)
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.tmap_event)));//BitmapDescriptorFactory.fromView(R.drawable.tmap_event)

    }

}
