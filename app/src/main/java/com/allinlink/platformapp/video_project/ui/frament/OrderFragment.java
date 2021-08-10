package com.allinlink.platformapp.video_project.ui.frament;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;

import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.allinlink.platformapp.R;
import com.allinlink.platformapp.video_project.bean.OrderBean;
import com.allinlink.platformapp.video_project.contract.activity.OrderContract;
import com.allinlink.platformapp.video_project.presenter.activity.OrderPresenter;
import com.allinlink.platformapp.video_project.ui.adapter.OrderAdapter;
import com.allinlink.platformapp.video_project.widget.BottomFileDownload;
import com.allinlink.platformapp.video_project.widget.ButtomPopupDialog;
import com.unistrong.view.base.BaseFragment;

import java.util.List;

public class OrderFragment extends BaseFragment<OrderPresenter> implements OrderContract.View, OrderAdapter.ItemOnClick, BottomFileDownload.PopupDialogOnClick {
    private int type;
    private int page = 1;
    View title;
    OrderAdapter adapter;
    private ButtomPopupDialog buttomPopupDialog;
    XRecyclerView recyclerView;
    BottomFileDownload download;

    @Override
    protected View onCreateContentView() {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.fragment_order, null);
        initViews(view);
        return view;
    }

    private void initViews(View view) {
        type = getArguments().getInt("type");
        mPresenter = new OrderPresenter(this);
        mPresenter.queryAllOrder(type, page);
        title = view.findViewById(R.id.title);


        recyclerView = view.findViewById(R.id.ry_data);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter = new OrderAdapter(getActivity());
        adapter.setOnClick(this);
        adapter.setType(type);
        recyclerView.setAdapter(adapter);


        recyclerView.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                page = 1;
                mPresenter.queryAllOrder(type, page);

            }

            @Override
            public void onLoadMore() {
                page++;
                mPresenter.queryAllOrder(type, page);

            }
        });

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    /**
     * @param type 0待处理  1已处理
     * @return
     */
    public static OrderFragment getInstance(int type) {
        OrderFragment orderFragment = new OrderFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("type", type);
        orderFragment.setArguments(bundle);
        return orderFragment;
    }

    @Override
    public void onError(String msg) {

    }

    @Override
    public void setOrderData(List<OrderBean.DatasBean> datas) {
        if (page == 1) {
            adapter.setData(datas);
            recyclerView.refreshComplete();
        } else {
            adapter.addData(datas);
            recyclerView.loadMoreComplete();
        }

    }

    @Override
    public void closeSuccess() {
        page = 1;
        mPresenter.queryAllOrder(type, page);
    }

    @Override
    public void onItemClick(OrderBean.DatasBean bean) {
        buttomPopupDialog = new ButtomPopupDialog(getActivity(), new ButtomPopupDialog.WaringRelieveClick() {
            @Override
            public void onclick(String resaon) {
                mPresenter.closeOrder(bean, resaon);
            }
        });

        buttomPopupDialog.showAsDropDown(title);
    }

    //显示文件列表
    @Override
    public void onItemClickdownLoad(String filedoc, String gid) {
        if ("{}".equals(filedoc)) {
            Toast.makeText(getActivity(), "该工单没有附件下载", Toast.LENGTH_LONG).show();
            return;
        }
        if (download == null) {
            download = new BottomFileDownload(getActivity(), this);
        }
        download.setData(filedoc, gid);
        download.showAsDropDown(title);
    }

    @Override
    public void startDownload(String fileName) {

    }

    //点击下载按钮 开始下载

}
