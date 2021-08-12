package com.example.android_supervisor.ui;

import android.content.Context;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.cncoderx.recyclerviewhelper.adapter.ProxyAdapter;
import com.cncoderx.recyclerviewhelper.listener.OnItemClickListener;
import com.cncoderx.recyclerviewhelper.listener.OnItemLongClickListener;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.footer.ClassicsFooter;
import com.scwang.smartrefresh.layout.header.ClassicsHeader;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener;
import com.example.android_supervisor.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author wujie
 */
public class ListActivity extends BaseActivity implements OnRefreshLoadMoreListener, OnItemClickListener, OnItemLongClickListener {
    @BindView(R.id.refresh_layout)
    protected SmartRefreshLayout mRefreshLayout;

    @BindView(R.id.recycler_view)
    protected RecyclerView mRecyclerView;

    @BindView(R.id.empty)
    protected View mEmpty;

    @BindView(R.id.tv_empty)
    protected View tvEmpty;

    private ProxyAdapter mProxyAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getContentLayoutId());
        ButterKnife.bind(this);

        mRefreshLayout.setRefreshHeader(new ClassicsHeader(this).setEnableLastTime(false));
        mRefreshLayout.setRefreshFooter(new ClassicsFooter(this));

        mRefreshLayout.setOnRefreshLoadMoreListener(this);
        mRefreshLayout.setDisableContentWhenRefresh(true);
        mRefreshLayout.setDisableContentWhenLoading(true);

        RecyclerView.LayoutManager layoutManager = getLayoutManager(this);
        if (layoutManager != null) {
            mRecyclerView.setLayoutManager(layoutManager);
        }

        RecyclerView.ItemDecoration itemDecoration = getItemDecoration(this);
        if (itemDecoration != null) {
            mRecyclerView.addItemDecoration(itemDecoration);
        }

        RecyclerView.ItemAnimator itemAnimator = getItemAnimator(this);
        if (itemAnimator != null) {
            mRecyclerView.setItemAnimator(itemAnimator);
        }
    }

    protected int getContentLayoutId() {
        return R.layout.recycler_view_layout;
    }

    public RecyclerView.LayoutManager getLayoutManager(Context context) {
        return new LinearLayoutManager(context, RecyclerView.VERTICAL, false);
    }

    public RecyclerView.ItemDecoration getItemDecoration(Context context) {
        DividerItemDecoration itemDecoration = new DividerItemDecoration(context, LinearLayout.VERTICAL);
        itemDecoration.setDrawable(getResources().getDrawable(R.drawable.divider));
        return itemDecoration;
    }

    public RecyclerView.ItemAnimator getItemAnimator(Context context) {
        return null;
    }

    public void setAdapter(RecyclerView.Adapter adapter) {
        setAdapter(adapter, true);
    }

    public void setAdapter(RecyclerView.Adapter adapter, boolean clickable) {
        mProxyAdapter = new ProxyAdapter(adapter);
        if (clickable) {
            mProxyAdapter.setOnItemClickListener(this);
            mProxyAdapter.setOnItemLongClickListener(this);
        }

        mRecyclerView.setAdapter(mProxyAdapter);
    }

    public RecyclerView.Adapter getAdapter() {
        if (mProxyAdapter != null) {
            return mProxyAdapter.getWrappedAdapter();
        }
        return null;
    }

    public View onCreateLoadingView(@NonNull LayoutInflater inflater,
                                    @Nullable ViewGroup container) {
        int defaultLoadingLayout = com.cncoderx.recyclerviewhelper.R.layout.layout_loading_view;
        return inflater.inflate(defaultLoadingLayout, container, false);
    }

    public void setEnableLoadMore(boolean enabled) {
        mRefreshLayout.setEnableLoadMore(enabled);
    }

    public void setEnableRefresh(boolean enabled) {
        mRefreshLayout.setEnableRefresh(enabled);
    }

    public void finishRefresh() {
        mRefreshLayout.finishRefresh();
    }

    public void finishLoadMore() {
        mRefreshLayout.finishLoadMore();
    }

    public void setNoMoreData(boolean noMoreData) {
        mRefreshLayout.setNoMoreData(noMoreData);
    }

    public void setNoData(boolean noData) {
        if (noData) {
            mEmpty.setVisibility(View.VISIBLE);
        } else {
            mEmpty.setVisibility(View.GONE);
        }
    }

    public boolean isRefreshing() {
        return mRefreshLayout.isRefreshing();
    }

    public boolean isLoading() {
        return mRefreshLayout.isLoading();
    }

    @Override
    public void onItemClick(RecyclerView parent, View view, int position, long id) {

    }

    @Override
    public boolean onItemLongClick(RecyclerView parent, View view, int position, long id) {
        return false;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mProxyAdapter != null) {
            mProxyAdapter.setOnItemClickListener(null);
            mProxyAdapter.setOnItemLongClickListener(null);
        }
        mRecyclerView.setAdapter(null);
        mRecyclerView.clearOnScrollListeners();
    }

    @Override
    public void onLoadMore(RefreshLayout refreshLayout) {

    }

    @Override
    public void onRefresh(RefreshLayout refreshLayout) {

    }
}
