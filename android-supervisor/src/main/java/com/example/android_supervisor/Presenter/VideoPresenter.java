package com.example.android_supervisor.Presenter;

import android.content.Context;

import com.example.android_supervisor.entities.VideoRoomPara;
import com.example.android_supervisor.http.ServiceGenerator;
import com.example.android_supervisor.http.common.ProgressTransformer;
import com.example.android_supervisor.http.response.Response;
import com.example.android_supervisor.http.response.ResponseObserver;
import com.example.android_supervisor.http.service.PublicService;
import com.example.android_supervisor.ui.view.ProgressText;

import io.reactivex.android.schedulers.AndroidSchedulers;

public class VideoPresenter {

    public void enterVideoRoom(Context context, VideoRoomPara para, RoomCallBack callBack){

        PublicService publicService = ServiceGenerator.create(PublicService.class);

        publicService.addCloudVideoRoom(para)
                .observeOn(AndroidSchedulers.mainThread())
                .compose(new ProgressTransformer<Response>(context, ProgressText.login))
                .subscribe(new ResponseObserver(context) {

                    @Override
                    public void onSuccess(Object data) {

                    }
                });
    }

    public void leaveVideoRoom(Context context,VideoRoomPara para, RoomCallBack callBack){

        PublicService publicService = ServiceGenerator.create(PublicService.class);

        publicService.deleteCloudVideoRoom(para)
                .observeOn(AndroidSchedulers.mainThread())
                .compose(new ProgressTransformer<Response>(context, ProgressText.login))
                .subscribe(new ResponseObserver(context) {

                    @Override
                    public void onSuccess(Object data) {

                    }
                });
    }


    public interface RoomCallBack {

        void onSuccess();
    }
}
