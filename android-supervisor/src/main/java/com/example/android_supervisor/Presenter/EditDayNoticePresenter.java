package com.example.android_supervisor.Presenter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.TextUtils;
import android.widget.EditText;

import com.google.gson.Gson;
import com.example.android_supervisor.Presenter.callback.IEditDayNoticeCallback;
import com.example.android_supervisor.common.UserSession;
import com.example.android_supervisor.http.ServiceGenerator;
import com.example.android_supervisor.http.common.ProgressTransformer;
import com.example.android_supervisor.http.response.Response;
import com.example.android_supervisor.http.service.PersonalizedService;
import com.example.android_supervisor.ui.view.ProgressText;
import com.example.android_supervisor.utils.ToastUtils;

import java.util.HashMap;
import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.ResourceObserver;
import okhttp3.MediaType;
import okhttp3.RequestBody;

public class EditDayNoticePresenter {

    private Context mContext;
    private IEditDayNoticeCallback callback;

    public EditDayNoticePresenter(Context mContext) {
        this.mContext = mContext;
        this.callback = (IEditDayNoticeCallback) mContext;
    }


    public void saveData(String content){

        Map<String,String>map = new HashMap<>();
        map.put("userId", UserSession.getUserId(mContext));
        map.put("fillContent",content);
        map.put("fillState","0");
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"),new Gson().toJson(map));
        PersonalizedService service = ServiceGenerator.create(PersonalizedService.class);
        Observable<Response<String>> observable = service.saveDayNotice(requestBody);
        observable.observeOn(AndroidSchedulers.mainThread())
                .compose(new ProgressTransformer<Response<String>>(mContext, ProgressText.save))
                .subscribe(new ResourceObserver<Response<String>>() {
                    @Override
                    public void onNext(Response<String> str) {
                        if (!TextUtils.isEmpty(str.getData())){
                            callback.saveDataSuccessCallback();
                        }else{

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
    public void showSureDialog(String content,String id){
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setMessage("提交之后不可修改，您确定提交吗？");
        builder.setTitle("温馨提示");

        builder.setNegativeButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                submitData(content,id);
            }
        });
        builder.setPositiveButton("取消",null);
        builder.create().show();
    }
    public void submitData(String content,String id){

        Map<String,String>map = new HashMap<>();
        map.put("userId", UserSession.getUserId(mContext));
        map.put("fillContent",content);
        map.put("fillState","1");
        if (!TextUtils.isEmpty(id)){
            map.put("id",id);
        }
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"),new Gson().toJson(map));
        PersonalizedService service = ServiceGenerator.create(PersonalizedService.class);
        Observable<Response<String>> observable = service.submitDayNotice(requestBody);
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

    public boolean isCanSubmit(EditText etContent){
        String content = etContent.getText().toString();
        if (TextUtils.isEmpty(content)){
            ToastUtils.show(mContext,"请输入日志内容");
            return false;
        }
        return true;
    }
}
