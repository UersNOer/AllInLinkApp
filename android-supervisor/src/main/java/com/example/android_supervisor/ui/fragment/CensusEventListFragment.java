package com.example.android_supervisor.ui.fragment;

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
import com.example.android_supervisor.common.UserSession;
import com.example.android_supervisor.entities.CensusEventRes;
import com.example.android_supervisor.http.ServiceGenerator;
import com.example.android_supervisor.http.common.ProgressTransformer;
import com.example.android_supervisor.http.common.RefreshLayoutTransformer;
import com.example.android_supervisor.http.request.QueryBody;
import com.example.android_supervisor.http.response.Response;
import com.example.android_supervisor.http.response.ResponseObserver;
import com.example.android_supervisor.http.service.QuestionService;
import com.example.android_supervisor.ui.CensusEventDetailActivity;
import com.example.android_supervisor.ui.adapter.CensusEventAdapter;
import com.example.android_supervisor.ui.view.ProgressText;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;

/**
 * 专项任务领取列表
 */
public class CensusEventListFragment extends SearchListFragment {
    private CensusEventAdapter adapter;

    private String taskId;
    private int pageIndex = 1;
    private int pageSize = 30;
    private String searchKey;

    private boolean isInited;

    public static CensusEventListFragment newInstance(String taskId) {
        CensusEventListFragment fragment = new CensusEventListFragment();
        Bundle bundle = new Bundle();
        bundle.putString("taskId", taskId);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        etSearch.setHint("通过案件标题、事发位置、案件描述搜索");
        etSearch.setInputType(EditorInfo.TYPE_TEXT_VARIATION_LONG_MESSAGE);
        setSearchInfo("共%d条案件", 0);

        adapter = new CensusEventAdapter(getContext());
        setAdapter(adapter);

        Bundle bundle = getArguments();
        if (bundle != null) {
            this.taskId = bundle.getString("taskId");
        }

        isInited = true;
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

    public void fetchData(boolean refresh, boolean loadMore) {
        if (!isInited) {
            return;
        }

        if (!loadMore) {
            pageIndex = 1;
            adapter.clear();
        }
        QueryBody queryBody;
        if (TextUtils.isEmpty(searchKey)) {
            queryBody = new QueryBody.Builder()
                    .pageIndex(pageIndex)
                    .pageSize(pageSize)
                    .eq("censusTaskId", taskId)
                    .eq("createId", UserSession.getUserId(getContext()))
                    .desc("reportTime")
                    .create();
        } else {
            queryBody = new QueryBody.Builder()
                    .pageIndex(pageIndex)
                    .pageSize(pageSize)
                    .eq("censusTaskId", taskId)
                    .eq("createId", UserSession.getUserId(getContext()))
                    .beginGroup()
                    .like("censusTitle", searchKey)
                    .or()
                    .like("position", searchKey)
                    .like("questionDesc", searchKey)
                    .endGroup()
                    .desc("reportTime")
                    .create();
        }

        ObservableTransformer<Response<List<CensusEventRes>>, Response<List<CensusEventRes>>> transformer;
        if (refresh || loadMore) {
            transformer = new RefreshLayoutTransformer<>(mRefreshLayout);
        } else {
            transformer = new ProgressTransformer<>(getContext(), ProgressText.load);
        }
        QuestionService questionService = ServiceGenerator.create(QuestionService.class);
        Observable<Response<List<CensusEventRes>>> observable = questionService.getCensusEventList(queryBody);
        observable.compose(this.<Response<List<CensusEventRes>>>bindToLifecycle())
                .observeOn(AndroidSchedulers.mainThread())
                .compose(transformer)
                .subscribe(new ResponseObserver<List<CensusEventRes>>(getContext()) {
                    @Override
                    public void onSuccess(List<CensusEventRes> data) {
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
        //CensusEventDetailActivity
        CensusEventRes data = adapter.get(position);
        Intent intent = new Intent(getContext(), CensusEventDetailActivity.class);
        intent.putExtra("id", data.getId());

        ArrayList arrayList = (ArrayList) data.getAttaches();
        intent.putExtra("attachs",arrayList);


        startActivity(intent);
        getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }
}
