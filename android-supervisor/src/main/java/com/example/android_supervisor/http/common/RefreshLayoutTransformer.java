package com.example.android_supervisor.http.common;

import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.constant.RefreshState;

import java.lang.ref.WeakReference;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;
import io.reactivex.functions.Action;

/**
 * @author wujie
 */
public class RefreshLayoutTransformer<T> implements ObservableTransformer<T, T> {
    private WeakReference<RefreshLayout> mRefreshLayout;

    public RefreshLayoutTransformer(RefreshLayout refreshLayout) {
        mRefreshLayout = new WeakReference<>(refreshLayout);
    }

    @Override
    public ObservableSource<T> apply(final Observable<T> upstream) {
        return upstream.doOnTerminate(new Action() {
            @Override
            public void run() throws Exception {
                RefreshLayout refreshLayout = mRefreshLayout.get();
                if (refreshLayout != null) {
                    RefreshState state = refreshLayout.getState();
                    if (state == RefreshState.Refreshing) {
                        refreshLayout.finishRefresh();
                    } else if (state == RefreshState.Loading) {
                        refreshLayout.finishLoadMore();
                    }
                }
            }
        });
    }
}
