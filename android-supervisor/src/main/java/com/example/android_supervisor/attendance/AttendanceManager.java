package com.example.android_supervisor.attendance;

import android.content.Context;
import android.util.Log;

import com.amap.api.maps.AMapUtils;
import com.amap.api.maps.model.LatLng;
import com.example.android_supervisor.Presenter.AttendanceNetPresenter;
import com.example.android_supervisor.Presenter.ClockPresenter;
import com.example.android_supervisor.common.UserSession;
import com.example.android_supervisor.entities.AutuSysData;
import com.example.android_supervisor.entities.ClockPara;
import com.example.android_supervisor.entities.GpsPoint;
import com.example.android_supervisor.entities.WorkPlanData;
import com.example.android_supervisor.map.MapLocationAPI;
import com.example.android_supervisor.sqlite.PrimarySqliteHelper;
import com.example.android_supervisor.ui.view.ProgressDialog;
import com.example.android_supervisor.utils.CoordinateUtils;
import com.example.android_supervisor.utils.DateUtils;
import com.example.android_supervisor.utils.Environments;
import com.example.android_supervisor.utils.ToastUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * yj  打卡功能
 */
public class AttendanceManager {

    private  Context context;
    static AttendanceManager instance;
    PrimarySqliteHelper primarySqliteHelper;

    private Date lastTime =  new Date();


    ProgressDialog dialog;

    Object object = new Object();


    private AttendanceManager(Context context){
        this.context = context;
        primarySqliteHelper= PrimarySqliteHelper.getInstance(context);

    }

    public static AttendanceManager getInstance(Context context) {
        if (instance == null) {
            instance = new AttendanceManager(context);

        }
        return instance;
    }

    public void setDialog(ProgressDialog dialog) {
        this.dialog = dialog;
    }

    CallBack callBack;
    public void setCallBack(CallBack callBack ){
        this.callBack =callBack;
    }

    public void  loadAttendancePoint() {
        AttendanceNetPresenter presenter =  new AttendanceNetPresenter();
        presenter.getAttendancePoint(context,null);
    }

    public void  loadAttendancePoint(AttendanceNetPresenter.AttendanceCallBack callBack) {
        AttendanceNetPresenter presenter =  new AttendanceNetPresenter();
        presenter.getAttendancePoint(context,callBack);
    }


    public void Clock1(WorkPlanData workPlanData, WorkPlanData.PointListBean point, GpsPoint gpsPoint, boolean isShowDialog){

        ClockPara clockPara = new ClockPara();
        clockPara.setPointId(Long.valueOf(point.getPointId()));
        clockPara.setPunchTime(DateUtils.format(new Date()));

        if (gpsPoint!=null){
            clockPara.setPunchX(String.valueOf(gpsPoint.getLongitude()));
            clockPara.setPunchY(String.valueOf(gpsPoint.getLatitude()));
        }else {
            if (MapLocationAPI.getLocation()!=null){

                double[] latLng_Gcj02  =  CoordinateUtils.wgs84ToGcj02(MapLocationAPI.getLocation().getLongitude(),MapLocationAPI.getLocation().getLatitude());
                if (latLng_Gcj02!=null){
                    clockPara.setPunchX(String.valueOf(latLng_Gcj02[0]));
                    clockPara.setPunchY(String.valueOf(latLng_Gcj02[1]));
                }


            }else {
                ToastUtils.show(context,"当前未获取到坐标信息");
            }

        }

        clockPara.setUserId(Long.valueOf(UserSession.getUserId(context)));
        clockPara.setWorkPlanId(Long.valueOf(workPlanData.getWorkPlanId()));
        clockPara.setWorkGridCode(workPlanData.getWorkGridCode());

        if (isShowDialog){
            dialog.setText(point.getPointName()+" 卡点正在打卡");
            dialog.show();
        }

        ClockPresenter presenter = new ClockPresenter();
        presenter.clockWork(context, workPlanData, point,clockPara, new ClockPresenter.ClockWorkCallBack() {
            @Override
            public void onSuccess(WorkPlanData.PointListBean data,WorkPlanData workPlanData, WorkPlanData.PointListBean pointBean) {

                if (dialog!=null){
                    dialog.dismiss();
                }

                //更新排班 下的某个开考勤点的数据
//                WorkPlanData workPlanData1 = PrimarySqliteHelper.getInstance(context).getWorkPointDao().queryForId(data.getWorkPlanId());
//                updataAttendanceDb(workPlanData1);

                try{
                    WorkPlanData workPlanData2 = PrimarySqliteHelper.getInstance(context).getWorkPointDao().queryForId(data.getWorkPlanId());
                    if (workPlanData2!=null){
                        ArrayList<WorkPlanData.PointListBean> pointLists = workPlanData2.getPointList();

                        if (pointLists!=null && pointLists.size()>0){
                            for (WorkPlanData.PointListBean point: pointLists) {

                                if (point.getPointId().equals(data.getPointId()) && "1".equals(data.getPunchStatus())){

                                    point.setPunchStatus("1");

                                    updataAttendanceDb(workPlanData2);

                                    if (callBack!=null){
                                        callBack.onSuccess(workPlanData2);
                                    }

                                }else {
                                    //ToastUtils.show(context,"打卡成功，数据返回异常，请检查接口返回的数据");
                                }
                            }
                        }

                    }
                }catch (Exception e){

                }


            }

            @Override
            public void onFailure() {
                if (dialog!=null){
                    dialog.dismiss();
                }

            }
        });


    }


    public void autuClock(GpsPoint gpsPoint84){


        synchronized (object) {

            Date dtNow = new Date();

            if (Math.abs((dtNow.getTime() - lastTime.getTime())/(1000*60)) < 0.5){
                return;
            }

            lastTime = dtNow;

            GpsPoint gpsPoint =gpsPoint84;

            List<AutuSysData.WorkPlanBean> workPlanBeans = null;

            boolean isCanClock = false;

            try {
                if (Environments.autuSysData != null && Environments.autuSysData.getWorkPlan() != null) {
                    workPlanBeans = Environments.autuSysData.getWorkPlan();

                    for (AutuSysData.WorkPlanBean workPlanBean : workPlanBeans) {

                        String beginTime = workPlanBean.getBeginTime();
                        String endTime = workPlanBean.getEndTime();

                        Date begin = DateUtils.parse(beginTime,3);
                        Date end = DateUtils.parse(endTime,3);

                        Date nowData = new Date();
                      //  Date nowData  = DateUtils.parse("2019-11-08 17:00",3);
                        if (nowData.after(begin) && nowData.before(end)) {

                            try {
                                WorkPlanData workPlanData = primarySqliteHelper.getWorkPointDao()
                                        .queryForId(workPlanBean.getId());

                                if (workPlanData != null) {

                                    List<WorkPlanData.PointListBean> pointListBeans = workPlanData.getPointList();
                                    if (pointListBeans != null && pointListBeans.size() > 0 ) {

                                        workPlanBean.setCanClock(true);
                                        isCanClock = true;
                                    }
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }

                if (workPlanBeans == null || !isCanClock) {
                    return;
                }


                for (AutuSysData.WorkPlanBean workPlanBean : workPlanBeans) {

                    if (workPlanBean.isCanClock()) {

                        WorkPlanData workPlanData = primarySqliteHelper.getWorkPointDao().queryForId(workPlanBean.getId());

                        if (workPlanData != null ) {

                            List<WorkPlanData.PointListBean> pointListBeans = workPlanData.getPointList();

                            LatLng var0 = new LatLng(gpsPoint.getLatitude(), gpsPoint.getLongitude());

                            if (pointListBeans != null && pointListBeans.size() > 0) {

                                for (WorkPlanData.PointListBean point : pointListBeans) {

                                    LatLng var1 = new LatLng(Double.valueOf(point.getAbsY()), Double.valueOf(point.getAbsX()));
                                    float distance = AMapUtils.calculateLineDistance(var0, var1);

                                    if (point.getPunchStatus().equals("1")){
                                        return;
                                    }


                                    if (distance <= workPlanData.getOffset()) {

                                        Clock1(workPlanData,point,gpsPoint,false);

                                    }
                                }

                            }

                        }

                    }
                }


            } catch (Exception e) {
                e.printStackTrace();
                Log.e("tagtagtagtag","7"+e.getMessage());
            }
        }


    }



    public boolean checkPointValid(WorkPlanData.PointListBean point) {
        if (point == null){
            return false;
        }

        try{
            WorkPlanData workPlanData = primarySqliteHelper.getWorkPointDao().queryForId(point.getWorkPlanId());

            String beginTime = workPlanData.getWorkBeginTime();
            String endTime =  workPlanData.getWorkEndTime();

            Date begin = DateUtils.parse(beginTime,2);
            Date end = DateUtils.parse(endTime,2);
            Date nowData = new Date();

            if (nowData.after(begin) && nowData.before(end)){

                LatLng var0 = new LatLng(MapLocationAPI.getLocation().getLatitude(), MapLocationAPI.getLocation().getLongitude());
                LatLng var1 = new LatLng(Double.valueOf(point.getAbsY()), Double.valueOf(point.getAbsX()));
                float distance = AMapUtils.calculateLineDistance(var0, var1);

                if (distance <= workPlanData.getOffset()) {
                    return true;
                }
            }

        }catch (Exception e){
            e.printStackTrace();
        }

        return false;

    }


    public interface CallBack {

        void onSuccess( WorkPlanData workPlanData2);
    }

    public synchronized void updataAttendanceDb(WorkPlanData workPlanData){

        PrimarySqliteHelper.getInstance(context).getWorkPointDao().update(workPlanData);
    }



    /**
     * 某个点是否在区域内
     * @param aMap 地图元素
     * @param latLngList 区域坐标合集
     * @param latLng 需要判断的点
     * @return
     */
//    public static boolean polygonCon(List<LatLng> latLngList , LatLng latLng ){
//        PolygonOptions options = new PolygonOptions();
//        for (LatLng i : latLngList){
//            options.add(i);
//        }
//        options.visible(false); //设置区域是否显示
//        Polygon polygon = aMap.addPolygon(options);
//        boolean contains = polygon.contains(latLng);
//        polygon.remove();
//        return contains;
//    }


}
