package com.example.android_supervisor.http.common;

import android.app.Activity;
import android.content.Context;

import com.example.android_supervisor.ui.view.ProgressDialog;
import com.example.android_supervisor.ui.view.ProgressText;

import java.lang.ref.WeakReference;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;

/**
 * @author wujie
 */
public class ProgressTransformer<T> implements ObservableTransformer<T, T> {
    private WeakReference<Context> mContext;
    private ProgressDialog pDialog;
    private ProgressText text;

    public ProgressTransformer(Context context, ProgressText text) {
        mContext = new WeakReference<>(context);
        this.text = text;
    }

    @Override
    public ObservableSource<T> apply(final Observable<T> upstream) {
        return upstream.doOnSubscribe(new Consumer<Disposable>() {
            @Override
            public void accept(Disposable disposable) throws Exception {
                Context context = mContext.get();
                if (context != null && context instanceof Activity) {
                    pDialog = ProgressDialog.show(context, text);
                }
            }
        }).doOnTerminate(new Action() {
            @Override
            public void run() throws Exception {
                if (pDialog != null) {
                    pDialog.dismiss();
                    pDialog = null;
                }
            }
        });
    }
}
