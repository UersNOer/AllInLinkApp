package com.example.android_supervisor.Presenter;

import android.content.Context;


import com.example.android_supervisor.entities.ClockPara;
import com.example.android_supervisor.entities.WorkPlanData;
import com.example.android_supervisor.http.ServiceGenerator;
import com.example.android_supervisor.http.request.JsonBody;
import com.example.android_supervisor.http.response.Response;
import com.example.android_supervisor.http.response.ResponseObserver;
import com.example.android_supervisor.http.service.PublicService;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;


/**
 * 考勤点获取
 */
public class ClockPresenter {

//    {
//        "id": 0,
//            "pointId": 0,
//            "punchStatus": "string",
//            "punchTime": "2019-11-06T07:26:00.990Z",
//            "punchX": "string",
//            "punchY": "string",
//            "userId": 0,
//            "workGridCode": "string",
//            "workPlanId": 0
//    }



    public void clockWork(final Context context, WorkPlanData workPlanData, WorkPlanData.PointListBean pointBean, ClockPara para , ClockWorkCallBack callBack){

        PublicService publicService = ServiceGenerator.create(PublicService.class);

        Observable<Response<WorkPlanData.PointListBean>> observable = publicService.pointPunch(new JsonBody(para));

        observable
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new ResponseObserver<WorkPlanData.PointListBean>(context) {
                    @Override
                    public void onSuccess(WorkPlanData.PointListBean data) {
                       if (data!=null){

                           if(callBack!=null){
                               callBack.onSuccess(data,workPlanData,pointBean);
                           }
                       }
                    }

                    @Override
                    public void onFailure() {
                        super.onFailure();
                        if(callBack!=null){
                            callBack.onFailure();
                        }
                    }
                });

    }


    public interface ClockWorkCallBack {

        void onSuccess(WorkPlanData.PointListBean data,WorkPlanData workPlanData,WorkPlanData.PointListBean pointBean);

        void onFailure();
    }


//     "id": "1192712416546701314",
//             "createId": "1176680734874714113",
//             "createTime": "2019-11-08 15:56:14",
//             "updateId": null,
//             "updateTime": null,
//             "pointId": "1191601433421938690",
//             "workPlanId": "1192685281090142209",
//             "workGridCode": "440309001007A0",
//             "punchStatus": "1",
//             "punchX": "28.233002",
//             "punchY": "112.86957",
//             "punchTime": "2019-11-08 15:54:38",
//             "userId": "1176680734874714113"
}
