package com.example.android_supervisor.ui;

import android.content.Intent;
import android.os.Bundle;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;

import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.example.android_supervisor.R;
import com.example.android_supervisor.common.UserSession;
import com.example.android_supervisor.entities.CensusTaskRes;
import com.example.android_supervisor.http.ServiceGenerator;
import com.example.android_supervisor.http.common.ProgressTransformer;
import com.example.android_supervisor.http.common.RefreshLayoutTransformer;
import com.example.android_supervisor.http.request.QueryBody;
import com.example.android_supervisor.http.response.Response;
import com.example.android_supervisor.http.response.ResponseObserver;
import com.example.android_supervisor.http.service.QuestionService;
import com.example.android_supervisor.ui.adapter.CensusTaskAdapter;
import com.example.android_supervisor.ui.view.ProgressText;

import java.util.Collections;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;

/**
 * 专项任务 任务列表
 */
public class CensusTaskListActivity extends ListActivity {
    private CensusTaskAdapter adapter;
    private int pageIndex = 1;
    private int pageSize = 30;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        adapter = new CensusTaskAdapter();
        setAdapter(adapter);

        fetchData(false, false);
    }

    @Override
    public void onRefresh(RefreshLayout refreshLayout) {
        pageIndex = 1;
        fetchData(true, false);
    }

    @Override
    public void onLoadMore(RefreshLayout refreshLayout) {
        fetchData(false, true);
    }

    @Override
    public void onItemClick(RecyclerView parent, View view, int position, long id) {
        CensusTaskRes taskRes = adapter.get(position);
        Intent intent = new Intent(this, CensusTaskInfoActivity.class);
        intent.putExtra("id", taskRes.getId());
        intent.putExtra("code", taskRes.getCode());
        intent.putExtra("name", taskRes.getName());
        intent.putExtra("areaName", taskRes.getAreaName());


//        intent.putExtra("geoX", taskRes.get());
//        intent.putExtra("geoY", taskRes.getName());

        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }

    private void fetchData(boolean refresh, boolean loadMore) {
        if (!loadMore) {
            pageIndex = 1;
            adapter.clear();
        }
        QueryBody queryBody = new QueryBody.Builder()
                    .pageIndex(pageIndex)
                    .pageSize(pageSize)
                    .taskStatus("2")//进行中
                    .like("taskUserId", UserSession.getUserId(this))
                    .create();

        QuestionService questionService = ServiceGenerator.create(QuestionService.class);
        Observable<Response<List<CensusTaskRes>>> observable = questionService.getCensusTaskList(queryBody);

        ObservableTransformer<Response<List<CensusTaskRes>>, Response<List<CensusTaskRes>>> transformer;
        if (refresh || loadMore) {
            transformer = new RefreshLayoutTransformer<>(mRefreshLayout);
        } else {
            transformer = new ProgressTransformer<>(this, ProgressText.load);
        }
        observable.compose(this.<Response<List<CensusTaskRes>>>bindToLifecycle())
                .observeOn(AndroidSchedulers.mainThread())
                .compose(transformer)
                .subscribe(new ResponseObserver<List<CensusTaskRes>>(this) {
                    @Override
                    public void onSuccess(List<CensusTaskRes> data) {
                        if (data.size() > 0) {
                            Collections.reverse(data);
                            adapter.addAll(data);
                            pageIndex++;
                        } else {
                            setNoMoreData(true);
                        }
                        setNoData(adapter.size() == 0);
                    }
                });
    }
}
