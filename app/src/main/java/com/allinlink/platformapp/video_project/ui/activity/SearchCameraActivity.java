package com.allinlink.platformapp.video_project.ui.activity;

import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Context;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.unistrong.utils.RxBus;
import com.unistrong.utils.StringsUtils;
import com.allinlink.platformapp.R;
import com.allinlink.platformapp.video_project.adapter.SearchCameraAdapter;
import com.allinlink.platformapp.video_project.bean.CameraBean;
import com.allinlink.platformapp.video_project.bean.ChannelBean;
import com.allinlink.platformapp.video_project.contract.activity.SearchCameraContract;
import com.allinlink.platformapp.video_project.presenter.activity.SearchCameraPresenter;
import com.unistrong.view.base.BaseTitleActivity;
import com.unistrong.view.navigationbar.INavigationBarBackListener;
import com.unistrong.view.navigationbar.NavigationBar;
import com.unistrong.view.navigationbar.NavigationBuilder;

import java.util.List;

public class SearchCameraActivity extends BaseTitleActivity<SearchCameraPresenter> implements SearchCameraContract.View, TextView.OnEditorActionListener, View.OnClickListener, SearchCameraAdapter.CollectOnClickListance {
    private SearchCameraAdapter adapter;
    XRecyclerView xRecyclerView;
    EditText edSearch;
    String keyWord;
    TextView tvMode1, tvMode2, tvSure;
    RelativeLayout rlBack;
    private int page;
    //0 单选 1多选
    int mode;
    int type;


    @Override
    protected NavigationBuilder onCreateNavigationBarByBuilder(NavigationBuilder builder) {
        NavigationBuilder navigationBuilder = builder.setTitle(StringsUtils.getResourceString(R.string.activity_searchcamera))
                .setType(NavigationBar.BACK_AND_TITLE).setNavigationBarListener(new INavigationBarBackListener() {
                    @Override
                    public void onBackClick() {
                        finish();
                    }
                });
        return navigationBuilder;

    }


    @Override
    public void onError(String msg) {
        if (msg == null)
            finish();
    }

    @Override
    public Context getContext() {
        return this;
    }

    @Override
    public View onCreateContentView() {
        View view = LayoutInflater.from(this).inflate(R.layout.activity_search_camera2, null);
        initView(view);
        return view;
    }

    private void initView(View view) {

        mPresenter = new SearchCameraPresenter(this);
        rlBack = view.findViewById(R.id.rl_back);
        tvMode1 = view.findViewById(R.id.tv_mode_1);
        tvMode2 = view.findViewById(R.id.tv_mode_2);
        tvSure = view.findViewById(R.id.tv_sure);
        tvMode1.setOnClickListener(this);
        tvMode2.setOnClickListener(this);
        tvSure.setOnClickListener(this);

        xRecyclerView = view.findViewById(R.id.ry_data);
        xRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        xRecyclerView.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                keyWord = edSearch.getText().toString();
                if (TextUtils.isEmpty(keyWord)) {
                    xRecyclerView.refreshComplete();
                    return;
                }
                page = 1;
                //上拉
                mPresenter.queryAllCamera(keyWord, page);
            }

            @Override
            public void onLoadMore() {
                page++;
                //下拉
                mPresenter.queryAllCamera(keyWord, page);
            }
        });

        adapter = new SearchCameraAdapter(this);
        adapter.setCollectOnClickListance(this);
        type = getIntent().getIntExtra("type", 0);
        mode = getIntent().getIntExtra("mode", 0);
        if (type == 1 || type == 3) {
            adapter.setTpye(type);
        }
        if (mode == 1) {
            adapter.setMax(getIntent().getIntExtra("max", 0));
        }
        xRecyclerView.setAdapter(adapter);
        view.findViewById(R.id.iv_search).setOnClickListener(this);
        edSearch = view.findViewById(R.id.ed_search);
        ImageView ivSerach = view.findViewById(R.id.iv_clear);
        ivSerach.setOnClickListener(this);
        ivSerach.setVisibility(View.GONE);
        edSearch.setOnEditorActionListener(this);
        edSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!TextUtils.isEmpty(s)) {
                    ivSerach.setVisibility(View.VISIBLE);
                } else {
                    ivSerach.setVisibility(View.GONE);
                }
            }
        });
    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if (actionId == EditorInfo.IME_ACTION_NEXT) {
            onClick(findViewById(R.id.iv_search));
            //TODO 测试使用
//            RxBus.getInstance().send(new CameraBean.DatasBean());
//            finish();
            return true;
        }
        return false;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_search:
                keyWord = edSearch.getText().toString().trim();
                page = 1;
                mPresenter.queryAllCamera(keyWord, page);
                break;
            case R.id.tv_mode_1:
                tvMode1.setVisibility(View.GONE);
                tvMode2.setVisibility(View.VISIBLE);
                tvSure.setVisibility(View.VISIBLE);
                adapter.setMode(1);
                break;
            case R.id.tv_mode_2:
                tvMode1.setVisibility(View.VISIBLE);
                tvMode2.setVisibility(View.GONE);
                tvSure.setVisibility(View.GONE);
                adapter.setMode(0);
                break;
            case R.id.tv_sure:
                adapter.getData();
                break;
            case R.id.iv_clear:
                edSearch.setText("");
                break;
        }
    }

    @Override
    public void setCameraData(List<ChannelBean> bean) {
        if (bean.size() > 0 && mode != 0) {
            rlBack.setVisibility(View.VISIBLE);
        }
        if (page == 1) {
            xRecyclerView.refreshComplete();
            adapter.setData(bean);
        } else {
            adapter.addData(bean);
//
            xRecyclerView.loadMoreComplete();
        }
    }

    @Override
    public void collectClick(boolean collect, ChannelBean data) {
        if (collect) {
            mPresenter.addChannelUserfavorites(data);
        } else {
            mPresenter.removeChannelUserfavoritesById(data);
        }
    }
}