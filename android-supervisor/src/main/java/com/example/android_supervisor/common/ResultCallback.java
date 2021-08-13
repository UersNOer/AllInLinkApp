package com.example.android_supervisor.common;

/**
 * @author wujie
 */
public abstract class ResultCallback<T> {

    public abstract void onResult(T result ,int tag);

    public abstract void onResult(T result);

    public void onError(Throwable throwable) {
        throwable.printStackTrace();
    }
}
