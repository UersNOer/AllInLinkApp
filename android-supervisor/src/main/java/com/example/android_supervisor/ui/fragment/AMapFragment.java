package com.example.android_supervisor.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;

import com.amap.api.maps.AMap;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.services.help.Tip;
import com.example.android_supervisor.Presenter.AttendanceNetPresenter;
import com.example.android_supervisor.R;
import com.example.android_supervisor.attendance.AttendanceManager;
import com.example.android_supervisor.entities.EventRes;
import com.example.android_supervisor.entities.WorkPlanData;
import com.example.android_supervisor.map.mark.CaseMarker;
import com.example.android_supervisor.map.poisearch.Constants;
import com.example.android_supervisor.map.poisearch.GaoDePoi;
import com.example.android_supervisor.map.poisearch.InputTipsActivity;
import com.example.android_supervisor.map.polygonoption.PolyLoad;
import com.example.android_supervisor.map.polygonoption.PolygonOptionUtils;
import com.example.android_supervisor.ui.view.ProgressDialog;
import com.example.android_supervisor.utils.FullScreenManager;
import com.example.android_supervisor.utils.ToastUtils;

import java.util.List;

import butterknife.OnClick;

public class AMapFragment extends AMapBaseFragment implements AMap.OnMarkerClickListener{

    public static final int REQUEST_CODE = 100;
    public static final int RESULT_CODE_INPUTTIPS = 101;
    public static final int RESULT_CODE_KEYWORDS = 102;

    GaoDePoi gaoDePoi;//兴趣点查找
    CaseMarker caseMarker;//案件 地图点 展示

    PolyLoad polygonOptionUtils = null;//网格数据高亮显示类


    public static AMapFragment newInstance(Bundle bundle) {
        AMapFragment fragment = new AMapFragment();
        fragment.setArguments(bundle);
        return fragment;
    }


    @Override
    public void initPoiSerach(){
        mCleanKeyWords.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mKeywordsTextView.setText("");
                aMap.clear();
                mCleanKeyWords.setVisibility(View.GONE);
            }
        });

        mKeywordsTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), InputTipsActivity.class);
                startActivityForResult(intent, REQUEST_CODE);
            }
        });

        gaoDePoi  = new GaoDePoi(getContext(),aMap);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_CODE_INPUTTIPS && data
                != null) {
            aMap.clear();
            Tip tip = data.getParcelableExtra(Constants.EXTRA_TIP);
            if (tip.getPoiID() == null || tip.getPoiID().equals("")) {
                gaoDePoi.doSearchQuery(tip.getName());
            } else {
                gaoDePoi.addTipMarker(tip);
            }
            mKeywordsTextView.setText(tip.getName());
            if(!tip.getName().equals("")){
                mCleanKeyWords.setVisibility(View.VISIBLE);
            }
        } else if (resultCode == RESULT_CODE_KEYWORDS && data != null) {
            aMap.clear();
            String keywords = data.getStringExtra(Constants.KEY_WORDS_NAME);
            if(keywords != null && !keywords.equals("")){
                gaoDePoi.doSearchQuery(keywords);
            }
            mKeywordsTextView.setText(keywords);
            if(!keywords.equals("")){
                mCleanKeyWords.setVisibility(View.VISIBLE);
            }
        }
    }


    @Override
    public void  initMarker(){

        caseMarker =  new CaseMarker(getContext(),aMap,true);

        aMap.setOnMarkerClickListener(this);
    }

    @OnClick(R.id.map_case)
    public void onOpenCaseView() {
        if (caseMarker == null){
            return;
        }
        caseMarker.obtainMarkerData();
    }

    @Override
    public void loadZXMarker(String taskId){
        if (caseMarker == null){
            return;
        }
        caseMarker.loadTodoEvents(taskId);//专项任务历史上报案件显示
    }

    @Override
    public void refreshMap() {
        onOpenCaseView();
    }



    /**
     * g高亮工作网格
     */
    public void setWorkGrid() {
        if(polygonOptionUtils ==null){
            polygonOptionUtils = new PolygonOptionUtils(getContext(),aMap);
        }
        polygonOptionUtils.loadPoly();


        if (myLatlng!=null && eventRes!=null){
            setSingerMarker(myLatlng,eventRes);
        }
    }


    public void setSingerMarker(LatLng myLatlng){
        if (caseMarker == null){
            return;
        }
        caseMarker.setMarker(myLatlng);
    }

    /**
     * 核实案件单个显示
     * @param myLatlng
     * @param eventRes
     */
    LatLng myLatlng;
    EventRes eventRes;
    public void setSingerMarker(LatLng myLatlng, EventRes eventRes){
        if (caseMarker == null){
            return;
        }
        this.myLatlng =myLatlng;
        this.eventRes = eventRes;
        caseMarker.setMarker(myLatlng,eventRes,this::onMarkerClick);
    }


    @Override
    public boolean onMarkerClick(Marker marker) {
        Log.d("dawn", "onMarkerClick: " + marker.getPosition().latitude);
        marker.hideInfoWindow();

        Object tag = marker.getObject();
        if (tag != null) {
            if (tag instanceof WorkPlanData.PointListBean) {

                try{
                    WorkPlanData.PointListBean  point = (WorkPlanData.PointListBean) marker.getObject();

                    if (!AttendanceManager.getInstance(getContext()).checkPointValid(point)){
                        ToastUtils.show(getContext(),"请在指定时间和指定区域打卡");
                        return  true;

                    }

                    if (point!=null && point.getPunchStatus().equals("0")){

                        WorkPlanData workPlanData = new WorkPlanData();

                        workPlanData.setWorkPlanId(point.getWorkPlanId());
                        workPlanData.setWorkGridCode(point.getWorkGridCode());

                        AttendanceManager.getInstance(getContext()).setCallBack(new AttendanceManager.CallBack() {
                            @Override
                            public void onSuccess(WorkPlanData workPlanData) {

                                if (workPlanData!=null){
                                    AttendanceManager.getInstance(getContext()).loadAttendancePoint(new AttendanceNetPresenter.AttendanceCallBack() {
                                        @Override
                                        public void onSuccess(List<WorkPlanData> data) {
                                            if (caseMarker == null){
                                                return;
                                            }
                                            caseMarker.loadAttendance(data);
                                        }
                                    });
                                }

                            }
                        });
                        AttendanceManager.getInstance(getContext()).setDialog(new ProgressDialog(getContext()));
                        AttendanceManager.getInstance(getContext()).Clock1(workPlanData,point,null,true);
                    }

                }catch (Exception e){

                }
                return true;
            }
        }

        mPop.setEventRes(marker);
        mPop.setCase_detailShow(isShowCase);//当个不显示详情
//        mPop.showAsDropDown(getView());
        //计算全面屏
        int Height = FullScreenManager.getNavigationBarHeight(getContext());

        mPop.showAtLocation(getView().findViewById(R.id.mapLayout), Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, Height);
        return true;
    }






}
