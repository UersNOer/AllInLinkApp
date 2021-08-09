package com.unistrong.network;

import com.google.gson.JsonParseException;
import com.google.gson.JsonSyntaxException;
import com.unistrong.network.api.ApiFactory;
import com.unistrong.network.log.LogFactory;
import com.unistrong.utils.UNIMetaData;

import org.json.JSONException;

import java.net.UnknownHostException;

import rx.Subscriber;

/**
 * Description: 基于RxJava 1.2.1 版本
 * Created by ltd ON 2020/5/25
 * Phone:18600920091
 * Email:td.liu@unistrong.com
 */
public abstract class AbsApiSubscriber<T extends IModel> extends Subscriber {

    @Override
    public void onError(Throwable e) {
        if (UNIMetaData.NETWORK_LOG) {
            LogFactory.getInstance().e(this.getClass().getSimpleName(), e.getMessage(), e);
        }
        NetError error;
        if (e != null) {
            if (!(e instanceof NetError)) {
                if (e instanceof UnknownHostException) {
                    error = new NetError(e, NetError.ErrorType.NoConnectError);
                } else if (e instanceof JSONException
                        || e instanceof JsonParseException
                        || e instanceof JsonSyntaxException) {
                    error = new NetError(e, NetError.ErrorType.ParseError);
                } else {
                    error = new NetError(e, NetError.ErrorType.OtherError);
                }
            } else {
                error = (NetError) e;
            }

            if (useCommonErrorHandler() && ApiFactory.getInstance().getCommonProvider() != null) {
                if (ApiFactory.getInstance().getCommonProvider().handleError(error)) {
                    return;
                }
            }
            onFail(error);
        }
    }

    /**
     * 接口请求失败
     *
     * @param error
     */
    protected abstract void onFail(NetError error);

    @Override
    public void onCompleted() {
        // TODO: 2018-6-4
    }

    protected boolean useCommonErrorHandler() {
        return true;
    }

    @Override
    public void onNext(Object o) {
        onNext((T) o);
    }

    /**
     * 返回转型后的数据
     *
     * @param t
     */
    protected abstract void onNext(T t);
}
