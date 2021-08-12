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

import com.example.android_supervisor.ui.CsDescActivity;
import com.example.android_supervisor.ui.model.YqcsDayPara;
import com.example.android_supervisor.ui.model.YqcsDayRes;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.example.android_supervisor.R;
import com.example.android_supervisor.common.UserSession;
import com.example.android_supervisor.http.ServiceGenerator;
import com.example.android_supervisor.http.common.ProgressTransformer;
import com.example.android_supervisor.http.common.RefreshLayoutTransformer;
import com.example.android_supervisor.http.request.JsonBody;
import com.example.android_supervisor.http.response.Response;
import com.example.android_supervisor.http.response.ResponseObserver;
import com.example.android_supervisor.http.service.YqytsService;
import com.example.android_supervisor.ui.adapter.YqcsEventItemAdapter;
import com.example.android_supervisor.ui.view.ProgressText;

import java.util.Collections;

import io.reactivex.Observable;
import io.reactivex.ObservableTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;

/**
 * A simple {@link Fragment} subclass.
 */
public class YqCsEventFragmen extends SearchListFragment {

    private YqcsEventItemAdapter adapter;

    private int pageIndex = 1;
    private int pageSize = 50;
    private String searchKey;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        etSearch.setHint("疫情场所名称/场所位置");
        etSearch.setInputType(EditorInfo.TYPE_TEXT_VARIATION_LONG_MESSAGE);
        setSearchInfo("共%d条数据", 0);

        adapter = new YqcsEventItemAdapter(getContext(),0);
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

        YqcsDayPara.PageBean page = new YqcsDayPara.PageBean();
        YqcsDayPara para = new YqcsDayPara();
        page.setCurrent(pageIndex);
        para.setCreateId(Long.parseLong(UserSession.getUserId(getContext())));
        para.setPage(page);
        if (TextUtils.isEmpty(searchKey)) {

        } else {
            para.setQueryStr(searchKey);
        }

        ObservableTransformer<Response<YqcsDayRes>, Response<YqcsDayRes>> transformer;
        if (refresh || loadMore) {
            transformer = new RefreshLayoutTransformer<>(mRefreshLayout);
        } else {
            transformer = new ProgressTransformer<>(getActivity(), ProgressText.load);
        }

        YqytsService service = ServiceGenerator.create(YqytsService.class);
        Observable<Response<YqcsDayRes>> observable =  observable =  service.ListWhByTypeForYq(new JsonBody(para));

        observable
                .observeOn(AndroidSchedulers.mainThread())
                .compose(new ProgressTransformer<Response<YqcsDayRes>>(getContext(), ProgressText.load))
                .compose(transformer)
                .subscribe(new ResponseObserver<YqcsDayRes>(getContext()){

                    @Override
                    public void onSuccess(YqcsDayRes data) {
                        if (data.getWhDetailsList().size() > 0) {
                            Collections.reverse(data.getWhDetailsList());
                            adapter.addAll(data.getWhDetailsList());
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
        YqcsDayRes.WhDetailsListBean data = adapter.get(position);
        Intent intent = new Intent(getContext(), CsDescActivity.class);
        intent.putExtra("id", data.getId());
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


    public YqCsEventFragmen() {
        // Required empty public constructor
    }



}
