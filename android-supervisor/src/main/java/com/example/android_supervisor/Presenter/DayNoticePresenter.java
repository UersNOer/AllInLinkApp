package com.example.android_supervisor.Presenter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.TextUtils;

import com.example.android_supervisor.Presenter.callback.IDayNoticeCallback;
import com.example.android_supervisor.common.UserSession;
import com.example.android_supervisor.entities.DayNoticeModel;
import com.example.android_supervisor.http.ServiceGenerator;
import com.example.android_supervisor.http.common.ProgressTransformer;
import com.example.android_supervisor.http.response.Response;
import com.example.android_supervisor.http.service.PersonalizedService;
import com.example.android_supervisor.ui.view.ProgressText;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.ResourceObserver;
import okhttp3.MediaType;
import okhttp3.RequestBody;

public class DayNoticePresenter {

    private Context mContext;

    private IDayNoticeCallback callback;
    public DayNoticePresenter(Context mContext) {
        this.mContext = mContext;
        callback = (IDayNoticeCallback) mContext;
    }

    public void getData(){
        PersonalizedService service = ServiceGenerator.create(PersonalizedService.class);
        Map<String,String>map = new HashMap<>();
        map.put("userId", UserSession.getUserId(mContext));
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"),new Gson().toJson(map));
        Observable<Response<List<DayNoticeModel>>> observable = service.getDayNoticeList(requestBody);

        observable.observeOn(AndroidSchedulers.mainThread())
                .compose(new ProgressTransformer<Response<List<DayNoticeModel>>>(mContext, ProgressText.load))
                .subscribe(new ResourceObserver<Response<List<DayNoticeModel>>>() {
                    @Override
                    public void onNext(Response<List<DayNoticeModel>> data) {
                        if (data == null || data.getData() == null){
                            callback.getDataSuccessCallback(new ArrayList<>());
                            return;
                        }
                        callback.getDataSuccessCallback(data.getData());
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {
                        callback.getDataComplete();
                    }
                });

    }
    public void showSureDialog(String content,String id){
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setMessage("????????????????????????????????????????????????");
        builder.setTitle("????????????");

        builder.setNegativeButton("??????", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                submitData(content,id);
            }
        });
        builder.setPositiveButton("??????",null);
        builder.create().show();
    }
    public void submitData(String content,String id){

        Map<String,String> map = new HashMap<>();
        map.put("userId", UserSession.getUserId(mContext));
        map.put("fillContent",content);
        map.put("fillState","1");
        if (!TextUtils.isEmpty(id)){
            map.put("id",id);
        }
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"),new Gson().toJson(map));
        PersonalizedService service = ServiceGenerator.create(PersonalizedService.class);
        Observable<Response<String>> observable = service.putDayNotice(requestBody);
        observable.observeOn(AndroidSchedulers.mainThread())
                .compose(new ProgressTransformer<Response<String>>(mContext, ProgressText.save))
                .subscribe(new ResourceObserver<Response<String>>() {
                    @Override
                    public void onNext(Response<String> data) {
                        if (data != null && !TextUtils.isEmpty(data.getData())){
                            callback.submitSuccessCallback();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }
}
