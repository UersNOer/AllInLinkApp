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
import com.example.android_supervisor.ui.TaskDetailActivity;
import com.example.android_supervisor.ui.adapter.TaskItemAdapter;
import com.example.android_supervisor.ui.view.ProgressText;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;

/**
 * @author wujie
 */
public class MyTaskListFragment extends SearchListFragment {
    private TaskItemAdapter adapter;

    private int status;
    private int pageIndex = 1;

    private String searchKey;

    private boolean isCallBack = false;

    public static MyTaskListFragment newInstance(int status) {
        MyTaskListFragment fragment = new MyTaskListFragment();
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
        etSearch.setHint(status == 1 ? "通过案件号、事发位置、案件描述搜索" : "通过受理号、事发位置、案件描述搜索");

        etSearch.setInputType(EditorInfo.TYPE_TEXT_VARIATION_LONG_MESSAGE);
        setSearchInfo("共%d条任务", 0);

        adapter = new TaskItemAdapter(getContext());
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
        }
        QueryBody queryBody;
        if (TextUtils.isEmpty(searchKey)) {
            isSerach = false;
            queryBody = new QueryBody.Builder()
                    .pageIndex(pageIndex)
                    .pageSize(pageSize)
                    .put("isHistory", 1)
//                    .desc(status == 1 ? "caseCode" : "acceptCode")
                    .create();
        } else {
            isSerach = true;
            queryBody = new QueryBody.Builder()
                    .pageIndex(pageIndex)
                    .pageSize(pageSize)
                    .put("isHistory", 1)
                    .beginGroup()
                    .like(status == 1 ? "caseCode" : "acceptCode", searchKey)
                    .or()
                    .like("position", searchKey)
                    .like("questionDesc", searchKey)
                    .endGroup()
//                    .desc(status == 1 ? "caseCode" : "acceptCode")
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
                observable = questionService.getHcTaskList(queryBody);
                break;
            case 2:
                observable = questionService.getHsTaskList(queryBody);
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

                            if (!loadMore){
                                adapter.clear();//不是加载更多的情况下 而且也能查到数据的情况下  才清理重置数据
                            }

                            adapter.addAll(data);
                            adapter.setSearchKey(searchKey);
                            pageIndex++;
                        } else {
                            setNoMoreData(true);
                            if (!loadMore && isSerach){//既不是下拉、也不是加载更多情况 。搜索情况下
                                adapter.clear();
                            }
                            if (isCallBack){
                                adapter.clear();
                            }
                        }

                        setNoData(adapter.size() == 0);
                        setSearchInfo("共%d条任务", adapter.size());
                    }
                });
    }

    @Override
    public void onItemClick(RecyclerView parent, View view, int position, long id) {
        EventRes data = adapter.get(position);
        Intent intent = new Intent(getContext(), TaskDetailActivity.class);
        intent.putExtra("id", data.getId());
        intent.putExtra("status", status);
        intent.putExtra("taskStatus", 0);
        intent.putExtra("caseTitle;", data.getTitle());
        startActivityForResult(intent, 1);
        getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK && requestCode == 1) {
            getActivity().setResult(Activity.RESULT_OK);
            fetchData(true, false);
            isCallBack = true;
        }
    }
}
