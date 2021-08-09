package com.allinlink.platformapp.video_project.utils;

import android.content.Context;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.jcodecraeer.xrecyclerview.XRecyclerView;

import java.util.List;

public class XRecyclerViewUtils<T extends XRecyclerViewUtils.TextAdapter, M> {
    private T adapter;
    private List<M> data;
    private int page = 0;
    LoadDataListener loadDataListener;
    XRecyclerView xRecyclerView;

    public XRecyclerViewUtils(Context context, XRecyclerView xRecyclerView, T adapter) {
        this.xRecyclerView = xRecyclerView;
        this.adapter = adapter;
        this.xRecyclerView.setLayoutManager(new LinearLayoutManager(context));
        this.xRecyclerView.setAdapter(adapter);

    }

    public void setLoadDataListener(LoadDataListener loadDataListener) {
        this.loadDataListener = loadDataListener;
    }

    public void install() {
        if (loadDataListener == null) {
            return;
        }
        xRecyclerView.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                page = 1;
                loadDataListener.onRefresh(page);
            }

            @Override
            public void onLoadMore() {
                page++;
                loadDataListener.onloadMore(page);
            }
        });
    }


    public void setData(List<M> list) {
        adapter.setData(list);
    }

    public void addData(List<M> list) {
        adapter.addData(list);
    }

    public interface LoadDataListener {
        void onRefresh(int page);

        void onloadMore(int page);
    }

    public abstract class TextAdapter extends RecyclerView.Adapter {
        abstract void setData(List<M> data);

        abstract void addData(List<M> data);

    }

}
