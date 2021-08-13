package com.example.android_supervisor.ui.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.LinearLayout;

import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.example.android_supervisor.R;
import com.example.android_supervisor.entities.EventRes;
import com.example.android_supervisor.http.ServiceGenerator;
import com.example.android_supervisor.http.common.ProgressTransformer;
import com.example.android_supervisor.http.common.RefreshLayoutTransformer;
import com.example.android_supervisor.http.request.QueryBody;
import com.example.android_supervisor.http.response.Response;
import com.example.android_supervisor.http.response.ResponseObserver;
import com.example.android_supervisor.http.service.QuestionService;
import com.example.android_supervisor.ui.EventDetailActivity;
import com.example.android_supervisor.ui.adapter.EventItemAdapter;
import com.example.android_supervisor.ui.view.ProgressText;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;


//        已核查   jumpType=0, type=1
//        已核实   jumpType=0, type=0
//        已退件   jumpType=1

public class HistoryTaskFragment extends SearchListFragment {
    private EventItemAdapter adapter;

    private int status; //  1 核查  2 核实
    private int pageIndex = 1;
    private int pageSize = 20;
    private String searchKey;

    public static HistoryTaskFragment newInstance(int status) {
        HistoryTaskFragment fragment = new HistoryTaskFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("status", status);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Bundle bundle = getArguments();
        if (bundle != null) {
            this.status = bundle.getInt("status");
        }

        if (status == 1) {
            etSearch.setHint("通过案件号、事发位置、案件描述搜索");
        } else {
            etSearch.setHint("通过受理号、事发位置、案件描述搜索");
        }
        etSearch.setInputType(EditorInfo.TYPE_TEXT_VARIATION_LONG_MESSAGE);
        setSearchInfo("共%d条案件", 0);

        adapter = new EventItemAdapter(getContext(),1);
        setAdapter(adapter);
        adapter.setShowCaseType(false);
        fetchData(false, false);
    }

    @Override
    public RecyclerView.ItemDecoration getItemDecoration(Context context) {
        DividerItemDecoration divider = new DividerItemDecoration(context, LinearLayout.VERTICAL);
        divider.setDrawable(getResources().getDrawable(R.drawable.divider_space));
        return divider;
    }

    @Override
    public void onRefresh(RefreshLayout refreshLayout) {
        fetchData(true, false);
    }

    @Override
    public void onLoadMore(RefreshLayout refreshLayout) {
        fetchData(false, true);
    }

    @Override
    public void onSearch(String key) {
        searchKey = key;
        fetchData(false, false);
    }

    private void fetchData(final boolean refresh, final boolean loadMore) {
        if (!loadMore) {
            pageIndex = 1;
            adapter.clear();
        }
        int type = status ==1?1:status ==2?0:0;
        QueryBody queryBody;
        if (TextUtils.isEmpty(searchKey)) {
            queryBody = new QueryBody.Builder()
                    .pageIndex(pageIndex)
                    .pageSize(pageSize)
                    .put("isHistory", 0)
                    .put("jumpType",0)
                    .put("type",type)
                    .create();
        } else {
            queryBody = new QueryBody.Builder()
                    .pageIndex(pageIndex)
                    .pageSize(pageSize)
                    .put("isHistory", 0)
                    .put("jumpType",0)
                    .put("type",type)
                    .beginGroup()
                    .like(status == 1 ? "caseCode" : "acceptCode", searchKey)
                    .or()
                    .like("position", searchKey)
                    .like("questionDesc", searchKey)
                    .endGroup()
                    .create();
        }
        ObservableTransformer<Response<List<EventRes>>, Response<List<EventRes>>> transformer;
        if (refresh || loadMore) {
            transformer = new RefreshLayoutTransformer<>(mRefreshLayout);
        } else {
            transformer = new ProgressTransformer<>(getContext(), ProgressText.load);
        }
        QuestionService questionService = ServiceGenerator.create(QuestionService.class);
        Observable<Response<List<EventRes>>> observable;
        switch (status) {
            case 1:
                observable = questionService.listVerifyCheckAndTheProduct(queryBody);
                break;
            case 2:
                observable = questionService.listVerifyCheckAndTheProduct(queryBody);
                break;
            default:
                return;
        }
        observable.compose(this.<Response<List<EventRes>>>bindToLifecycle())
                .observeOn(AndroidSchedulers.mainThread())
                .compose(transformer)
                .subscribe(new ResponseObserver<List<EventRes>>(getContext()) {
                    @Override
                    public void onSuccess(List<EventRes> data) {
                        if (data.size() > 0) {
                            adapter.addAll(data);
                            adapter.setSearchKey(searchKey);
                            pageIndex++;
                        } else {
                            setNoMoreData(true);
                        }
                        setNoData(adapter.size() == 0);
                        setSearchInfo("共%d条案件", adapter.size());
                    }
                });
    }

    @Override
    public void onItemClick(RecyclerView parent, View view, int position, long id) {
        EventRes data = adapter.get(position);
        Intent intent = new Intent(getContext(), EventDetailActivity.class);
        intent.putExtra("id", data.getId());
        intent.putExtra("status", status);
        intent.putExtra("handled", true);
        startActivityForResult(intent, 1);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK && requestCode == 1) {
            fetchData(false, false);
        }
    }
}
