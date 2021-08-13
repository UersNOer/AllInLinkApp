package com.example.android_supervisor.Presenter;

import android.content.Context;


import com.example.android_supervisor.common.UserSession;
import com.example.android_supervisor.entities.WorkPlanData;
import com.example.android_supervisor.http.ServiceGenerator;
import com.example.android_supervisor.http.response.Response;
import com.example.android_supervisor.http.response.ResponseObserver;
import com.example.android_supervisor.http.service.PublicService;
import com.example.android_supervisor.sqlite.PrimarySqliteHelper;

import java.sql.SQLException;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;


/**
 * 考勤点获取
 */
public class AttendanceNetPresenter {


    public void getAttendancePoint(final Context context, AttendanceCallBack callBack){

        PublicService publicService = ServiceGenerator.create(PublicService.class);

        Observable<Response<List<WorkPlanData>>> observable = publicService.getPointTask(UserSession.getUserId(context));

        observable
                .observeOn(AndroidSchedulers.mainThread())
//                .compose(new ProgressTransformer<Response<EventRes>>(context, ProgressText.load))
                .subscribe(new ResponseObserver<List<WorkPlanData>>(context) {
                    @Override
                    public void onSuccess(List<WorkPlanData> data) {
                       if (data!=null && data.size()>0){
                           //可能有多个排班
                           PrimarySqliteHelper primarySqliteHelper = PrimarySqliteHelper.getInstance(context);

                           //加工数据 后台未处理

                           try {
                               primarySqliteHelper.getWorkPointDao().deleteBuilder().delete();
                               primarySqliteHelper.getWorkPointDao().create(data);
                           } catch (SQLException e) {
                               e.printStackTrace();
                           }


                           if (callBack!=null){
                               callBack.onSuccess(data);
                           }

                       }

                    }
                });

    }


    public interface AttendanceCallBack {

        void onSuccess(List<WorkPlanData> data);
    }
}
