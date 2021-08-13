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

import com.example.android_supervisor.ui.SJDescActivity;
import com.example.android_supervisor.ui.model.SjNoteRes;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.example.android_supervisor.R;
import com.example.android_supervisor.common.UserSession;
import com.example.android_supervisor.http.ServiceGenerator;
import com.example.android_supervisor.http.common.ProgressTransformer;
import com.example.android_supervisor.http.common.RefreshLayoutTransformer;
import com.example.android_supervisor.http.request.QueryBody;
import com.example.android_supervisor.http.response.Response;
import com.example.android_supervisor.http.response.ResponseObserver;
import com.example.android_supervisor.http.service.YqService;
import com.example.android_supervisor.ui.adapter.YqsjEventItemAdapter;
import com.example.android_supervisor.ui.view.ProgressText;

import java.util.Collections;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;

/**
 * A simple {@link Fragment} subclass.
 */
public class YqsjEventFragmen extends SearchListFragment {

    private YqsjEventItemAdapter adapter;

    private int pageIndex = 1;
    private int pageSize = 50;
    private String searchKey;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        etSearch.setHint("疫情事件名称/事件位置");
        etSearch.setInputType(EditorInfo.TYPE_TEXT_VARIATION_LONG_MESSAGE);
        setSearchInfo("共%d条数据", 0);

        adapter = new YqsjEventItemAdapter(getContext(),0);
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
                    .eq("dbStatus","1")
//                    .desc("caseCode")
//                    .desc("caseLevel")
                    .create();
        } else {
            queryBody = new QueryBody.Builder()
                    .pageIndex(pageIndex)
                    .pageSize(pageSize)
                    .eq("createId", UserSession.getUserId(getContext()))
                    .eq("dbStatus","1")
                    .beginGroup()
                    .like("smallClassName", searchKey)
                    .or()
                    .like("esPosition", searchKey)
                    .like("esDesc", searchKey)
                    .endGroup()
//                    .desc("caseCode")
                    .create();
        }
        ObservableTransformer<Response<List<SjNoteRes>>, Response<List<SjNoteRes>>> transformer;
        if (refresh || loadMore) {
            transformer = new RefreshLayoutTransformer<>(mRefreshLayout);
        } else {
            transformer = new ProgressTransformer<>(getActivity(), ProgressText.load);
        }
        YqService questionService = ServiceGenerator.create(YqService.class);
        Observable<Response<List<SjNoteRes>>> observable = questionService.ysCsRoportedList(queryBody);
        observable.compose(this.<Response<List<SjNoteRes>>>bindToLifecycle())
                .observeOn(AndroidSchedulers.mainThread())
                .compose(transformer)
                .subscribe(new ResponseObserver<List<SjNoteRes>>(getContext()) {
                    @Override
                    public void onSuccess(List<SjNoteRes> data) {
                        if (data.size() > 0) {
                            Collections.reverse(data);
                            adapter.addAll(data);
                            pageIndex++;
                        } else {
                            setNoMoreData(true);
                        }
                        setNoData(adapter.size() == 0);
                        setSearchInfo("共%d条数据", adapter.size());
                    }
                });
    }

    @Override
    public void onItemClick(RecyclerView parent, View view, int position, long id) {
        SjNoteRes data = adapter.get(position);
        Intent intent = new Intent(getContext(), SJDescActivity.class);
        intent.putExtra("id", data.getId());
//        ArrayList arrayList = (ArrayList) data.getAttaches();
//        intent.putExtra("attachs",arrayList);
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


    public YqsjEventFragmen() {
        // Required empty public constructor
    }



}
