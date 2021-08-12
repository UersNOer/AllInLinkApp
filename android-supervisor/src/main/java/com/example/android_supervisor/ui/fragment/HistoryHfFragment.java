package com.example.android_supervisor.ui.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
//import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.LinearLayout;

import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.example.android_supervisor.R;
import com.example.android_supervisor.common.UserSession;
import com.example.android_supervisor.entities.CheckHfRes;
import com.example.android_supervisor.http.ServiceGenerator;
import com.example.android_supervisor.http.common.ProgressTransformer;
import com.example.android_supervisor.http.common.RefreshLayoutTransformer;
import com.example.android_supervisor.http.request.QueryBody;
import com.example.android_supervisor.http.response.Response;
import com.example.android_supervisor.http.response.ResponseObserver;
import com.example.android_supervisor.http.service.PublicService;
import com.example.android_supervisor.ui.adapter.CheckHfItemAdapter;
import com.example.android_supervisor.ui.view.ProgressText;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;

/**
 * @author dw
 */
public class HistoryHfFragment extends SearchListFragment {
    private CheckHfItemAdapter adapter;

    private int pageIndex = 1;
    private int pageSize = 20;
    private String searchKey;


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        etSearch.setHint("通过回复内容描述搜索");
        etSearch.setInputType(EditorInfo.TYPE_TEXT_VARIATION_LONG_MESSAGE);
        setSearchInfo("共%d条数据", 0);

        adapter = new CheckHfItemAdapter();
        setAdapter(adapter);

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
        QueryBody queryBody;
        if (TextUtils.isEmpty(searchKey)) {
            queryBody = new QueryBody.Builder()
                    .pageIndex(pageIndex)
                    .pageSize(pageSize)
                    .eq("userId", UserSession.getUserId(getContext()))
                    .ne("replyStatus","0")
                    .create();

        } else {
            queryBody = new QueryBody.Builder()
                    .pageIndex(pageIndex)
                    .pageSize(pageSize)
                    .eq("replyContent", searchKey)
                    .eq("userId", UserSession.getUserId(getContext()))
                    .ne("replyStatus","0")
                    .create();
        }
        ObservableTransformer<Response<List<CheckHfRes>>, Response<List<CheckHfRes>>> transformer;
        if (refresh || loadMore) {
            transformer = new RefreshLayoutTransformer<>(mRefreshLayout);
        } else {
            transformer = new ProgressTransformer<>(getContext(), ProgressText.load);
        }
        PublicService publicService = ServiceGenerator.create(PublicService.class);
        Observable<Response<List<CheckHfRes>>> observable = publicService.getSpotRstsList(queryBody);
        observable.compose(this.<Response<List<CheckHfRes>>>bindToLifecycle())
                .observeOn(AndroidSchedulers.mainThread())
                .compose(transformer)
                .subscribe(new ResponseObserver<List<CheckHfRes>>(getContext()) {
                    @Override
                    public void onSuccess(List<CheckHfRes> data) {
                        if (data.size() > 0) {
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

//    @Override
//    public void onItemClick(RecyclerView parent, View view, int position, long id) {
//        EventRes data = adapter.get(position);
//        Intent intent = new Intent(getContext(), EventDetailActivity.class);
//        intent.putExtra("id", data.getEvtId());
//        if (!TextUtils.isEmpty(data.getActDefId())) {
//            if (data.getActDefId().equals("pre_supervisor_verification"))
//                intent.putExtra("status", 2);
//            else if (data.getActDefId().equals("general_supervisor_verification"))
//                intent.putExtra("status", 1);
//        } else
//            intent.putExtra("status", 2);
//        intent.putExtra("handled", true);
//        intent.putExtra("edit", 2);
//        startActivityForResult(intent, 1);
//    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK && requestCode == 1) {
            fetchData(false, false);
        }
    }
}
