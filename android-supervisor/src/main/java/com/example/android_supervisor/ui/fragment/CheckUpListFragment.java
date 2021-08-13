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
import android.widget.LinearLayout;

import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.example.android_supervisor.R;
import com.example.android_supervisor.common.UserSession;
import com.example.android_supervisor.entities.CheckUpRes;
import com.example.android_supervisor.http.ServiceGenerator;
import com.example.android_supervisor.http.common.ProgressTransformer;
import com.example.android_supervisor.http.common.RefreshLayoutTransformer;
import com.example.android_supervisor.http.request.QueryBody;
import com.example.android_supervisor.http.response.Response;
import com.example.android_supervisor.http.response.ResponseObserver;
import com.example.android_supervisor.http.service.PublicService;
import com.example.android_supervisor.ui.CheckUpActivity;
import com.example.android_supervisor.ui.adapter.CheckUpItemAdapter;
import com.example.android_supervisor.ui.view.ProgressText;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;

/**
 * @author yangjie
 */
public class CheckUpListFragment extends SearchListFragment {
    private CheckUpItemAdapter adapter;
    private int pageIndex = 1;
    private int pageSize = 20;

    private boolean isShowDialog = false;
    private String searchKey;
    private boolean isCallBack = false;


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        adapter = new CheckUpItemAdapter();
        setAdapter(adapter);
        fetchData(false, false);
        etSearch.setHint("请输入抽查名称");
    }

    @Override
    public void onResume() {
        super.onResume();
        isShowDialog = true;
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

        }
        QueryBody queryBody;
        if (TextUtils.isEmpty(searchKey)){
            isSerach = false;
             queryBody = new QueryBody.Builder()
                    .pageIndex(pageIndex)
                    .pageSize(pageSize)
                    .put("userId", UserSession.getUserId(getContext()))
                    .eq("dbStatus", "1")
//                .desc("createTime")
                    .create();
        }else {
            isSerach = true;
            queryBody = new QueryBody.Builder()
                    .pageIndex(pageIndex)
                    .pageSize(pageSize)
                    .put("userId", UserSession.getUserId(getContext()))
                    .eq("dbStatus", "1")
                     .like("spotName",searchKey)
//                    .put("areaCode", searchKey)//区域编码
//                    .put("replyContent", searchKey)//回复内容
//                    .put("auditOpinion",searchKey)//抽查回复
                    .create();
        }


        ObservableTransformer<Response<List<CheckUpRes>>, Response<List<CheckUpRes>>> transformer = null;
        if (refresh || loadMore) {
            transformer = new RefreshLayoutTransformer<>(mRefreshLayout);
        } else {
            if (this.isVisible()==true && isShowDialog){
                transformer = new ProgressTransformer<>(getContext(), ProgressText.load);
            }
            transformer = new ProgressTransformer<>(getContext(), ProgressText.load);
        }
        PublicService publicService = ServiceGenerator.create(PublicService.class);
        Observable<Response<List<CheckUpRes>>> observable = publicService.getCheckUpList(queryBody);
        observable.compose(this.<Response<List<CheckUpRes>>>bindToLifecycle())
                .observeOn(AndroidSchedulers.mainThread())
        .compose(transformer)
        .subscribe(new ResponseObserver<List<CheckUpRes>>(getContext()) {
            @Override
            public void onSuccess(List<CheckUpRes> data) {
                if (data.size() > 0) {
                    if (!loadMore){
                        adapter.clear();
                    }

                    List<CheckUpRes> temp = new ArrayList<>();
                    for(CheckUpRes res:data){
                        if (res.getStateOfProgress().equals("1")){
                            temp.add(res);
                        }
                    }
                    adapter.addAll(temp);
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
            }
        });
    }

    @Override
    public void onItemClick(RecyclerView parent, View view, int position, long id) {
        CheckUpRes data = adapter.get(position);
        Intent intent = new Intent(getContext(), CheckUpActivity.class);
        intent.putExtra("id", data.getId());
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
