package com.unistong.netword.queries;


import com.unistong.netword.Data;
import com.unistrong.model.activity.baseui.BaseActivitySimple;
import com.unistrong.uni_utils.MyUtils;

import java.io.InterruptedIOException;
import java.net.SocketException;
import java.net.UnknownHostException;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;


/**
 * Created by Administrator on 2017/5/12.
 * CustomObserver
 */
public abstract class CustomObserver<T> implements Observer<T> {
    private boolean dialogCancelable;
    private BaseActivitySimple baseActivity;

    /**
     * @param dialogCancelable DialogCancelable默认false
     */
    protected CustomObserver( boolean dialogCancelable) {
        this.dialogCancelable = dialogCancelable;
    }

    protected CustomObserver() {

    }


    @Override
    public void onSubscribe(Disposable d) {
        onSubscribeCustom(d);

    }

    @Override
    public void onNext(T t) {
        if (null != t
                && t instanceof Data) {
            Data date = (Data) t;
//            Log.i("TAG", date.toString());
            if (date != null && "success".equals(date.status)) {
                onCustomNext(t);
            } else if (!"success".equals(date.status)) {
//                if (baseActivity != null && date.message == null) {
////                    baseActivity.showToast("网络错误");
//                } else if (null != baseActivity && null != date.message) {
////                    baseActivity.showToast(date.message);
//                }

            } else {
//                if (baseActivity != null) {
//                    if (!MyUtils.txtCheckEmpty(date.message)) {
////                        baseActivity.showToast(date.message);
//                        onDefeated(date.message);
//                    }
//                }
            }
        } else {
            onCustomNext(t);
        }
    }

    /**
     * 经过初筛的返回体
     */
    protected abstract void onDefeated(String msg);

    protected abstract void onCustomNext(T t);

    @Override
    public void onError(Throwable e) {
        dismissDialog();
        onErrorCustom(e);
//        if (baseActivity != null) {
//            if (e instanceof InterruptedIOException
//                    || e instanceof SocketException
//                    || e instanceof UnknownHostException) {
////                Toast.makeText(baseActivity, "网络连接错误", Toast.LENGTH_LONG).show();
//            } else {
////                Toast.makeText(baseActivity, e.getMessage(), Toast.LENGTH_LONG).show();
//            }
//        }
    }


    @Override
    public void onComplete() {
        dismissDialog();
    }

    /**
     * 隐藏Dialog
     */
    private void dismissDialog() {
//        if (baseActivity != null && baseActivity.alertDialog != null) {
//            baseActivity.alertDialog.dismiss();
//        }
    }

    /**
     * onSubscribe具体处理
     *
     * @param disposable
     */
    protected void onSubscribeCustom(Disposable disposable) {
    }

    /**
     * 异常具体处理
     *
     * @param e
     */
    protected void onErrorCustom(Throwable e) {
    }
}
