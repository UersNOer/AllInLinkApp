package com.example.android_supervisor.Presenter;

import android.content.Context;

import com.example.android_supervisor.Presenter.callback.ILeaveRecordCallback;
import com.example.android_supervisor.common.UserSession;
import com.example.android_supervisor.entities.LeaveRecordModel;
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

public class LeaveRecordPresenter {
    private Context mContext;
    private ILeaveRecordCallback callback;

    public LeaveRecordPresenter(Context mContext, ILeaveRecordCallback callback) {
        this.mContext = mContext;
        this.callback = callback;
    }
    public void getData(){
        PersonalizedService service = ServiceGenerator.create(PersonalizedService.class);
        Map<String,String> map = new HashMap<>();
        map.put("leavePeopleId", UserSession.getUserId(mContext));
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"),new Gson().toJson(map));
        Observable<Response<List<LeaveRecordModel>>> observable = service.getLeaveListData(requestBody);

        observable.observeOn(AndroidSchedulers.mainThread())
                .compose(new ProgressTransformer<Response<List<LeaveRecordModel>>>(mContext, ProgressText.load))
                .subscribe(new ResourceObserver<Response<List<LeaveRecordModel>>>() {
                    @Override
                    public void onNext(Response<List<LeaveRecordModel>> data) {
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

}
