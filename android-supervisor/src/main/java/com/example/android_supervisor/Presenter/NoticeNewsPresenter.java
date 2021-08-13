package com.example.android_supervisor.Presenter;

import android.content.Context;
import android.util.Log;

import com.example.android_supervisor.entities.ActualNews;
import com.example.android_supervisor.http.ServiceGenerator;
import com.example.android_supervisor.http.request.QueryBody;
import com.example.android_supervisor.http.response.Response;
import com.example.android_supervisor.http.response.ResponseObserver;
import com.example.android_supervisor.http.service.PublicService;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;

import static com.makeramen.roundedimageview.RoundedImageView.TAG;

public class NoticeNewsPresenter {



    public void getNoticeNews(final Context context, NoticeNewsCallBack callBack){
        QueryBody queryBody = new QueryBody.Builder()
                .desc("createTime")
                .create();
        PublicService publicService = ServiceGenerator.create(PublicService.class);
        Observable<Response<List<ActualNews>>> observable1 = publicService.getNoticeNews(queryBody);

        try {
            observable1
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new ResponseObserver<List<ActualNews>>(context) {

                        @Override
                        public void onSuccess(List<ActualNews> actualNewsList) {
                            if (callBack!=null){
                                callBack.onSuccess(actualNewsList);
                            }

                        }

                    });
        }catch (Exception e){
            Log.e(TAG, "fetchData: "+e.getMessage() );
        }


    }


    public interface NoticeNewsCallBack {

        void onSuccess(List<ActualNews> actualNewsList);
    }
}
