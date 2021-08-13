package com.example.android_supervisor.Presenter;

import android.content.Context;
import android.content.Intent;


import com.example.android_supervisor.common.UserSession;
import com.example.android_supervisor.entities.CheckStatusRes;
import com.example.android_supervisor.http.ServiceGenerator;
import com.example.android_supervisor.http.response.Response;
import com.example.android_supervisor.http.response.ResponseObserver;
import com.example.android_supervisor.http.service.PublicService;
import com.example.android_supervisor.ui.view.CheckStausDetailActivity;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;

public class CheckPresenter {

    public void getCheckStatusByID(final Context context, String checkId, CallBack callBack){

        PublicService publicService = ServiceGenerator.create(PublicService.class);

        Observable<Response<CheckStatusRes>> observable = publicService.getAuditDetail(checkId, UserSession.getUserId(context));

        observable
                .observeOn(AndroidSchedulers.mainThread())
//                .compose(new ProgressTransformer<Response<EventRes>>(context, ProgressText.load))
                .subscribe(new ResponseObserver<CheckStatusRes>(context) {
                    @Override
                    public void onSuccess(CheckStatusRes data) {
                        if (data!=null){

                        Intent intent = new Intent(context, CheckStausDetailActivity.class);
                        intent.putExtra("data", data);
                        context.startActivity(intent);
                        }
                    }
                });
    }


    public interface CallBack{
        void onSuccess();
    }
}
