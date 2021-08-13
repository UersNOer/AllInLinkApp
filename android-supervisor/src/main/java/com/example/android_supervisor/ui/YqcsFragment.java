package com.example.android_supervisor.ui;

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
import com.example.android_supervisor.common.UserSession;
import com.example.android_supervisor.entities.EventRes;
import com.example.android_supervisor.http.ServiceGenerator;
import com.example.android_supervisor.http.common.ProgressTransformer;
import com.example.android_supervisor.http.common.RefreshLayoutTransformer;
import com.example.android_supervisor.http.request.QueryBody;
import com.example.android_supervisor.http.response.Response;
import com.example.android_supervisor.http.response.ResponseObserver;
import com.example.android_supervisor.http.service.QuestionService;
import com.example.android_supervisor.ui.adapter.EventItemAdapter;
import com.example.android_supervisor.ui.fragment.SearchListFragment;
import com.example.android_supervisor.ui.view.ProgressText;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;

public class YqcsFragment extends SearchListFragment {

    private EventItemAdapter adapter;

    private int pageIndex = 1;
    private int pageSize = 30;
    private String searchKey;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        etSearch.setHint("通过场所名称、场所位置搜索");
        etSearch.setInputType(EditorInfo.TYPE_TEXT_VARIATION_LONG_MESSAGE);
        setSearchInfo("共%d条场所", 0);

        adapter = new EventItemAdapter(getContext(),0);
        setAdapter(adapter);
        adapter.setShowCaseType(true);
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

    private void fetchData(boolean refresh, boolean loadMore) {
        if (!loadMore) {
            pageIndex = 1;
            adapter.clear();
        }
        QueryBody queryBody;
        if (TextUtils.isEmpty(searchKey)) {
            queryBody = new QueryBody.Builder()
                    .pageIndex(pageIndex)
                    .pageSize(pageSize)
                    .eq("createId", UserSession.getUserId(getContext()))
//                    .desc("caseCode")
//                    .desc("caseLevel")
                    .create();
        } else {
            queryBody = new QueryBody.Builder()
                    .pageIndex(pageIndex)
                    .pageSize(pageSize)
                    .eq("createId", UserSession.getUserId(getContext()))
                    .beginGroup()
                    .like("caseCode", searchKey)
                    .or()
                    .like("position", searchKey)
                    .like("questionDesc", searchKey)
                    .endGroup()
//                    .desc("caseCode")
                    .create();
        }
        ObservableTransformer<Response<List<EventRes>>, Response<List<EventRes>>> transformer;
        if (refresh || loadMore) {
            transformer = new RefreshLayoutTransformer<>(mRefreshLayout);
        } else {
            transformer = new ProgressTransformer<>(getActivity(), ProgressText.load);
        }
        QuestionService questionService = ServiceGenerator.create(QuestionService.class);
        Observable<Response<List<EventRes>>> observable = questionService.searchEvent(queryBody);
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
                        setSearchInfo("共%d条场所", adapter.size());
                    }
                });
    }

    @Override
    public void onItemClick(RecyclerView parent, View view, int position, long id) {
        EventRes data = adapter.get(position);
        Intent intent = new Intent(getContext(), EventDetailActivity.class);
        intent.putExtra("id", data.getId());
        intent.putExtra("status", 1);
        intent.putExtra("edit", 2);
        intent.putExtra("handled", true);
        ArrayList arrayList = (ArrayList) data.getAttaches();
        intent.putExtra("attachs",arrayList);
        startActivity(intent);
        getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == Activity.RESULT_OK) {
            fetchData(true,false);
        }
    }


}