package com.example.android_supervisor.ui;


import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;

import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener;
import com.example.android_supervisor.Presenter.DayNoticePresenter;
import com.example.android_supervisor.Presenter.callback.IDayNoticeCallback;
import com.example.android_supervisor.R;
import com.example.android_supervisor.entities.DayNoticeModel;
import com.example.android_supervisor.ui.adapter.DayNoticeAdapter;
import com.example.android_supervisor.utils.ToastUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DayNoticeActivity extends BaseActivity implements IDayNoticeCallback {

    @BindView(R.id.rvRecord)
    RecyclerView rvRecord;
    @BindView(R.id.srfRefresh)
    SmartRefreshLayout srfRefresh;

    private DayNoticeAdapter mAdapter;
    private List<DayNoticeModel> mList;
    private DayNoticePresenter mPresenter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_day_notice);
        initView();
        setListener();
    }

    private void setListener() {

        srfRefresh.setOnRefreshLoadMoreListener(new OnRefreshLoadMoreListener() {
            @Override
            public void onLoadMore(RefreshLayout refreshLayout) {

            }

            @Override
            public void onRefresh(RefreshLayout refreshLayout) {
                mPresenter.getData();
            }
        });
        mAdapter.setOnItemClickListener(new DayNoticeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                EditDayNoticeActivity.start(mContext,mList.get(position));
            }

            @Override
            public void onSubmitClick(View view, int position) {
                mPresenter.showSureDialog(mList.get(position).getFillContent(),mList.get(position).getId());
            }
        });

    }
    private void initView() {
        ButterKnife.bind(this);
        addMenu("新增",mMenuClick);

        initRefresh(srfRefresh);
        srfRefresh.setEnableLoadMore(false);
        rvRecord.setLayoutManager(new LinearLayoutManager(this));
        rvRecord.addItemDecoration(new DividerItemDecoration(this,DividerItemDecoration.VERTICAL));

        mList = new ArrayList<>();
        mAdapter = new DayNoticeAdapter(mList,this);
        rvRecord.setAdapter(mAdapter);

        mPresenter = new DayNoticePresenter(this);
        mPresenter.getData();
    }

    private View.OnClickListener mMenuClick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            EditDayNoticeActivity.start(mContext);
        }
    };

    @Override
    public void getDataSuccessCallback(List<DayNoticeModel> list) {
        srfRefresh.finishRefresh();
        srfRefresh.finishLoadMore();
        mList.clear();
        mList.addAll(list);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void getDataComplete() {
        srfRefresh.finishRefresh();
        srfRefresh.finishLoadMore();

    }

    @Override
    public void submitSuccessCallback() {
        ToastUtils.show(mContext,"提交成功");
        mPresenter.getData();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null && resultCode == RESULT_OK){
            mPresenter.getData();
        }
    }
}
