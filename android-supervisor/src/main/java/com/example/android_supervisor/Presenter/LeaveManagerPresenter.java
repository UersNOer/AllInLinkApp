package com.example.android_supervisor.Presenter;

import android.content.Context;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.TextView;

import com.example.android_supervisor.Presenter.callback.ILeaveManagerCallback;
import com.example.android_supervisor.common.UserSession;
import com.example.android_supervisor.entities.LeaveTypeModel;
import com.example.android_supervisor.http.ServiceGenerator;
import com.example.android_supervisor.http.common.ProgressTransformer;
import com.example.android_supervisor.http.response.Response;
import com.example.android_supervisor.http.service.PersonalizedService;
import com.example.android_supervisor.ui.view.ProgressText;
import com.example.android_supervisor.utils.TimeUtils;
import com.example.android_supervisor.utils.ToastUtils;
import com.google.gson.Gson;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.ResourceObserver;
import okhttp3.MediaType;
import okhttp3.RequestBody;

public class LeaveManagerPresenter {

    private Context mContext;
    private ILeaveManagerCallback callback;
    private final SimpleDateFormat dateFormat;

    public LeaveManagerPresenter(Context mContext, ILeaveManagerCallback callback) {
        this.mContext = mContext;
        this.callback = callback;
        dateFormat = new SimpleDateFormat("yyyy年MM月dd日");
    }
    public void getTypeData(){
        PersonalizedService service = ServiceGenerator.create(PersonalizedService.class);
        Map<String,String> map = new HashMap<>();
        map.put("leavePeopleId", UserSession.getUserId(mContext));
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"),new Gson().toJson(map));
        Observable<Response<List<LeaveTypeModel>>> observable = service.getLeaveTypeList();

        observable.observeOn(AndroidSchedulers.mainThread())
                .compose(new ProgressTransformer<Response<List<LeaveTypeModel>>>(mContext, ProgressText.load))
                .subscribe(new ResourceObserver<Response<List<LeaveTypeModel>>>() {
                    @Override
                    public void onNext(Response<List<LeaveTypeModel>> data) {
                        if (data == null || data.getData() == null){
                            callback.getTypeSuccessCallback(new ArrayList<>(), null);
                            return;
                        }
                        int size = data.getData().size();
                        ArrayList<String>list = new ArrayList<>();
                        for (int i = 0;i < size;i++){
                            list.add(data.getData().get(i).getDictName());
                        }
                        callback.getTypeSuccessCallback(data.getData(),list);
                    }

                    @Override
                    public void onError(Throwable e) {
                    }

                    @Override
                    public void onComplete() {

                    }
                });

    }
    public boolean isCanSubmit(TextView tvStartTime, TextView tvEndTime, TextView tvType, TextView tvDayNum, EditText tvReason){
        if (isEmptyTV(tvType)){
            showToast("请选择请假类型");
            return false;
        }
        if (isEmptyTV(tvStartTime)){
            showToast("请选择开始时间");
            return false;
        }
        if (isEmptyTV(tvEndTime)){
            showToast("请选择结束时间");
            return false;
        }
        if (isEmptyTV(tvDayNum)){
            showToast("请假天数不能小于1");
            return false;
        }
        if (isEmptyTV(tvReason)){
            showToast("请输入请假理由");
            return false;
        }
        return true;
    }
    public boolean isEmptyTV(TextView textView){
        if (TextUtils.isEmpty(textView.getText().toString())){
            return true;
        }
        return false;
    }

    public void showToast(String msg){
        ToastUtils.show(mContext,msg);
    }
    public void submitLeaveData(TextView tvStartTime, TextView tvEndTime, String code, TextView tvDayNum, EditText tvReason){
        PersonalizedService service = ServiceGenerator.create(PersonalizedService.class);
        Map<String,String> map = new HashMap<>();
        map.put("leavePeopleId", UserSession.getUserId(mContext));
        map.put("leaveStartTime", TimeUtils.getLongTimeForDayStr(tvStartTime.getText().toString()));
        map.put("leaveEndTime", TimeUtils.getLongTimeForDayStr(tvEndTime.getText().toString()));
        map.put("leaveType", code);
        map.put("leaveDuration", tvDayNum.getText().toString());
        map.put("leaveReason", tvReason.getText().toString());
        map.put("leaveState", "0");
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"),new Gson().toJson(map));
        Observable<Response<Object>> observable = service.submitLeave(requestBody);

        observable.observeOn(AndroidSchedulers.mainThread())
                .compose(new ProgressTransformer<Response<Object>>(mContext, ProgressText.load))
                .subscribe(new ResourceObserver<Response<Object>>() {
                    @Override
                    public void onNext(Response<Object> data) {
                        callback.submitDataSuccessCallback();
                    }

                    @Override
                    public void onError(Throwable e) {
                    }

                    @Override
                    public void onComplete() {

                    }
                });

    }

    public String getDayNum(String startTime,String endTime){
        if (TextUtils.isEmpty(startTime) || TextUtils.isEmpty(endTime)){
            return "";
        }
        try {
            Date start = dateFormat.parse(startTime);
            Date end = dateFormat.parse(endTime);
            long time = end.getTime() - start.getTime();
            int day = (int) (time / (24*3600*1000));
            return String.valueOf(day + 1);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return "";
    }

}
