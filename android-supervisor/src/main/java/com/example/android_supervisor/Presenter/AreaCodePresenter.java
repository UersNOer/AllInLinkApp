package com.example.android_supervisor.Presenter;

import android.content.Context;

import com.amap.api.maps.model.LatLng;
import com.example.android_supervisor.entities.GridCodeRes;
import com.example.android_supervisor.entities.GridCodeRes1;
import com.example.android_supervisor.entities.GridRes;
import com.example.android_supervisor.http.ServiceGenerator;
import com.example.android_supervisor.http.common.ProgressTransformer;
import com.example.android_supervisor.http.request.QueryBody;
import com.example.android_supervisor.http.response.Response;
import com.example.android_supervisor.http.response.ResponseObserver;
import com.example.android_supervisor.http.service.CodeService;
import com.example.android_supervisor.ui.view.ProgressText;
import com.example.android_supervisor.utils.Environments;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;


/**
 * yj  区域code
 */
public class AreaCodePresenter {

    public final static String T_MAP_DISTRICT_GRID = "T_MAP_DISTRICT_GRID"; //区域网格
    public final static String T_MAP_STREET_GRID = "T_MAP_STREET_GRID";//街道网格
    public final static String T_MAP_COMM_GRID = "T_MAP_COMM_GRID"; //社区网格
    public final static String T_MAP_UNIT_GRID = "T_MAP_UNIT_GRID";//单元网格

    public final static String T_MAP_CUSTOM_DUTYGRID = "T_MAP_CUSTOM_DUTYGRID";//责任单位网格
    public final static String T_MAP_CUSTOM_WORKGRID = "T_MAP_CUSTOM_WORKGRID";//所在工作网格

    GridRes gridRes = new GridRes();

    public void getAreaCode(Context context, LatLng centerPoint, AreaCodeCallBack callBack) {

        Environments.userBase.getDefaultDepartment().setManageGridId("");
        Environments.userBase.getDefaultDepartment().setWorkGridId("");
        Environments.userBase.getDefaultDepartment().setAreaCode1(Environments.userBase.getDefaultDepartment().getAreaCode());
        Environments.userBase.getDefaultDepartment().setGridId("");

        if (centerPoint == null) {
            return;
        }
        List<Double> doubles = new ArrayList<>();
        doubles.add(centerPoint.longitude);
        doubles.add(centerPoint.latitude);

        QueryBody queryBody = new QueryBody.Builder()
                .type("Point")
                .coordinates(doubles)
                .create();

        CodeService codeService = ServiceGenerator.create(CodeService.class);

        Observable<Response<List<GridCodeRes>>> observable1 = codeService.getByPoint(queryBody);
        observable1
                .observeOn(AndroidSchedulers.mainThread())
                .compose(new ProgressTransformer<Response<List<GridCodeRes>>>(context, ProgressText.code))
                .subscribe(new ResponseObserver<List<GridCodeRes>>(context) {

                    public void onSuccess(List<GridCodeRes> data) {

                        if (data != null && data.size() > 0) {
                            try {

                                for (GridCodeRes res : data) {

                                    if (res.getAreaGridSysProperties() != null) {

                                        if (res.getAreaGridSysProperties().getType().equals(T_MAP_UNIT_GRID)) {
                                            if (Environments.userBase != null) {
                                                Environments.userBase.getDefaultDepartment().setGridId(res.getAreaGridSysProperties().getGrCode());
                                            }
                                            gridRes.setGridId(res.getAreaGridSysProperties().getGrCode());
                                        }

                                        if (res.getAreaGridSysProperties().getType().equals(T_MAP_COMM_GRID)) {
                                            if (Environments.userBase != null) {
                                                Environments.userBase.getDefaultDepartment().setAreaCode1(res.getAreaGridSysProperties().getGrCode());
                                            }
                                            gridRes.setAreaCode(res.getAreaGridSysProperties().getGrCode());
                                            gridRes.setGrName(res.getAreaGridSysProperties().getGrName());
                                        }
                                    }

                                }

                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                            Observable<Response<List<GridCodeRes1>>> observable = codeService.getGridByCondition(queryBody);
                            observable
                                    .observeOn(AndroidSchedulers.mainThread())
                                    .compose(new ProgressTransformer<Response<List<GridCodeRes1>>>(context, ProgressText.code))
                                    .subscribe(new ResponseObserver<List<GridCodeRes1>>(context) {
                                        @Override
                                        public void onSuccess(List<GridCodeRes1> data) {
                                            if (data != null && data.size() > 0) {
                                                try {

                                                    for (GridCodeRes1 res : data) {

                                                        if (res.getWorkGridProperties() != null) {
                                                            if (T_MAP_CUSTOM_WORKGRID.equals(res.getWorkGridProperties().getType())) {
                                                                if (Environments.userBase != null) {
                                                                    Environments.userBase.getDefaultDepartment().setWorkGridId(res.getWorkGridProperties().getZrCode());
                                                                }
                                                                gridRes.setWorkGridId(res.getWorkGridProperties().getZrCode());
                                                            }
                                                        }

                                                        if (res.getDutyGridProperties() != null) {
                                                            if (T_MAP_CUSTOM_DUTYGRID.equals(res.getDutyGridProperties().getType())) {
                                                                if (Environments.userBase != null) {
                                                                    Environments.userBase.getDefaultDepartment().setManageGridId(res.getDutyGridProperties().getCode());
                                                                }
                                                                gridRes.setManageGridId(res.getDutyGridProperties().getCode());
                                                            }
                                                        }
                                                    }
                                                } catch (Exception e) {
                                                    e.printStackTrace();
                                                }


                                                if (callBack != null) {
                                                    callBack.onSuccess(gridRes);
                                                }

                                            } else {
                                                onFailure();
                                            }

                                        }

                                        @Override
                                        public void onFailure() {
                                            super.onFailure();
                                            if (callBack != null) {
                                                callBack.onError();
                                            }
                                        }
                                    });


                        } else {
                            onFailure();
                        }


                    }

                    @Override
                    public void onFailure() {
                        super.onFailure();
                        if (callBack != null) {
                            callBack.onError();
                        }
                    }
                });
    }


    public interface AreaCodeCallBack {

       // void onSuccess(List<GridCodeRes1> data);

        void onSuccess(GridRes data);

        void onError();
    }
}







