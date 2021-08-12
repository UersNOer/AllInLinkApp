package com.example.android_supervisor.http.response;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.example.android_supervisor.http.common.HttpExceptionConverter;
import com.example.android_supervisor.utils.ToastUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * @author wujie
 */
public abstract class ResponseObserver<T> implements Observer<Response<T>> {
    private HttpExceptionConverter exceptionConverter = new HttpExceptionConverter();
    private WeakReference<Context> mContext;

    public ResponseObserver(Context context) {
        mContext = new WeakReference<>(context);
    }

    @Override
    public void onSubscribe(Disposable d) {
        // Todo
    }

    @Override
    public void onNext(Response<T> response) {
        Log.d("onNext: ", response.isSuccess() + "");
        Log.d("onNext: ", response.getMessage() + "" + response.getCode());
        if (response.isSuccess()) {
            T data = response.getData();
            if (data == null) {
                Type superType = getClass().getGenericSuperclass();
                if (superType instanceof ParameterizedType) {
                    Type[] args = ((ParameterizedType) superType).getActualTypeArguments();
                    if (args.length > 0) {
                        Class<T> clazz = null;
                        if (args[0] instanceof Class) {
                            clazz = (Class<T>) args[0];
                        } else if (args[0] instanceof ParameterizedType) {
                            ParameterizedType type = (ParameterizedType) args[0];
                            clazz = (Class<T>) type.getRawType();
                        }
                        if (clazz != null) {
                            if (clazz.isAssignableFrom(List.class)) {
                                data = (T) new ArrayList();
                            } else if (clazz.isAssignableFrom(Map.class)) {
                                data = (T) new HashMap();
                            } else {
                                //onFailure(-1, "服务器异常：返回的数据为空");
                                return;
                            }
                        }
                    }
                }
            }
            onSuccess(data);
        } else {
            Log.d("onNext1: ", response.getCode() + "");
            onFailure(response.getCode(), response.getMessage());
        }
    }

    public abstract void onSuccess(T data);
    public void onFailure(){

    }

    public void onFailure(int code, String errMsg) {
        //code -1：错误，other code：业务错误
        Context context = mContext.get();
        if (context != null) {
            if (code==1000)
                ToastUtils.show(context, "用户或密码错误，请重新登录");
            else if (code == -1) {
                ToastUtils.show(context, errMsg);
            }else{
                otherMsg(errMsg,context,code);
            }
            onFailure();//扩展接口
        }
    }

    private void otherMsg(String errMsg, Context context,int code) {
        if (errMsg.contains("用户或密码错误"))
            ToastUtils.show(context, "用户或密码错误");
        else if (errMsg.contains("timed out"))
            ToastUtils.show(context, "当前网络开小差咯！！");
        else if (code == 401) {
            //发生鉴权过期通知  刷新tokne
            JSONObject extraObj = null;
            try {
                extraObj = new JSONObject(errMsg);
                int codes = extraObj.getInt("code");
                String message = extraObj.getString("message");
//                ToastUtils.show(context, message);
                if (!TextUtils.isEmpty(message) && message.contains("用户")){

                }else {
                    //刷新鉴权
                    //ToastUtils.show(context, "重新获取鉴权中...");
                    ToastUtils.show(context, TextUtils.isEmpty(message)?"没有此账号":message);
//                    LoginPresenter loginPresenter = new LoginPresenter();
//                    loginPresenter.login(context, UserSession.getUserName(context),
//                            UserSession.getUserPwd(context), new LoginPresenter.LoginCallBack() {
//                                @Override
//                                public void onSuccess() {
//                                    onSuccess();
//                                }
//
//                                @Override
//                                public void onFailure() {
//
//                                }
//                            });

//                    Intent intent = new Intent("com.example.android_supervisor.action.FORCE_REBOOT");
//                    intent.setPackage(mContext.get().getPackageName());
//                    Log.d("http", "onError getPackageName: " + mContext.get().getPackageName());
//                    mContext.get().sendBroadcast(intent);
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }


        } else {
            try {
                ToastUtils.show(context, errMsg);
//                JSONObject extraObj = new JSONObject(errMsg);
//                int codes = extraObj.getInt("code");
//                if (codes == 401) {
//                    Intent intent = new Intent("com.example.android_supervisor.action.FORCE_REBOOT");
//                    intent.setPackage(mContext.get().getPackageName());
//                    Log.d("http", "onError getPackageName: " + mContext.get().getPackageName());
//                    mContext.get().sendBroadcast(intent);
//                }
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        }
    }

    @Override
    public void onError(Throwable e) {
        ErrorEntity errMsg = exceptionConverter.convert(e);
        onFailure(errMsg.getCode(), errMsg.getMessage());
    }

    @Override
    public void onComplete() {

    }

//    {
//        "success": false,
//            "code": 1000,
//            "message": "用户或密码错误",
//            "errorType": "WARN",
//            "data": null
//    }
}
