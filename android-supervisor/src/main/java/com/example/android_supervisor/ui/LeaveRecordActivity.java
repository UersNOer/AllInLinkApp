package com.example.android_supervisor.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;

import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.example.android_supervisor.Presenter.LeaveRecordPresenter;
import com.example.android_supervisor.Presenter.callback.ILeaveRecordCallback;
import com.example.android_supervisor.R;
import com.example.android_supervisor.entities.LeaveRecordModel;
import com.example.android_supervisor.ui.adapter.LeaveRecordAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class LeaveRecordActivity extends BaseActivity implements ILeaveRecordCallback {

    @BindView(R.id.srlRefresh)
    SmartRefreshLayout srlRefresh;
    @BindView(R.id.rvRecord)
    RecyclerView rvRecord;
    private LeaveRecordPresenter mPresenter;

    private List<LeaveRecordModel>mList = new ArrayList<>();
    private LeaveRecordAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leave_record);
        initView();
        setListener();
    }

    private void setListener() {
        addMenu("新增请假", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LeaveManagerActivity.start(LeaveRecordActivity.this);
            }
        });
        mAdapter.setOnItemClickListener(new LeaveRecordAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                LeaveManagerActivity.start(LeaveRecordActivity.this,mList.get(position));
            }
        });
    }

    private void initView() {
        ButterKnife.bind(this);
        initRefresh(srlRefresh);
        initRecycle(rvRecord);

        srlRefresh.setEnableLoadMore(false);
        srlRefresh.setEnableRefresh(false);

        mAdapter = new LeaveRecordAdapter(mContext,mList);
        rvRecord.setAdapter(mAdapter);
        mPresenter = new LeaveRecordPresenter(this,this);
        mPresenter.getData();
    }

    public static void start(Context context) {
        Intent starter = new Intent(context, LeaveRecordActivity.class);
        context.startActivity(starter);
    }

    @Override
    public void getDataSuccessCallback(List<LeaveRecordModel> data) {
        mList.clear();
        mList.addAll(data);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void getDataComplete() {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null && requestCode == 123){
            mPresenter.getData();
        }
    }
}
