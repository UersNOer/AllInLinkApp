package com.example.android_supervisor.ui;

import android.content.Intent;
import android.os.Bundle;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.example.android_supervisor.R;
import com.example.android_supervisor.entities.ActualNews;
import com.example.android_supervisor.http.ServiceGenerator;
import com.example.android_supervisor.http.request.QueryBody;
import com.example.android_supervisor.http.response.Response;
import com.example.android_supervisor.http.response.ResponseObserver;
import com.example.android_supervisor.http.service.PublicService;
import com.example.android_supervisor.ui.adapter.ActualNewsAdapter;
import com.example.android_supervisor.utils.ToastUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;


public class ActualNewsListActivity extends BaseActivity {

    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_list);
        ButterKnife.bind(this);
        initData();
    }

    private void initData() {
        PublicService publicService = ServiceGenerator.create(PublicService.class);
        QueryBody queryBody = new QueryBody.Builder()
                .desc("createTime")
                .create();

//       Call<ResponseBody> call = publicService.getNoticeNewsData(queryBody);
//        call.enqueue(new Callback<ResponseBody>() {
//            @Override
//            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
//                LogUtils.d(":",response.body());
//            }
//
//            @Override
//            public void onFailure(Call<ResponseBody> call, Throwable t) {
//                LogUtils.d(":",t.getMessage());
//            }
//        });

        Observable<Response<List<ActualNews>>> observable1 = publicService.getNoticeNews(queryBody);
        observable1.compose(this.<Response<List<ActualNews>>>bindToLifecycle())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new ResponseObserver<List<ActualNews>>(this) {
                    @Override
                    public void onSuccess(List<ActualNews> data) {
                        if (data!=null && data.size()>0){
                            setRecyclerVeiw(data);
                        }else {
                            ToastUtils.show(ActualNewsListActivity.this,"暂无数据");
                            finish();
                        }

                    }

                });
    }
    private void setRecyclerVeiw(final List<ActualNews> actualNews) {
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addItemDecoration(new SimpleDividerItemDecoration(this, 1));
        ActualNewsAdapter actualNewsAdapter = new ActualNewsAdapter(R.layout.item_actual_news, actualNews);
        actualNewsAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                Intent intent=new Intent(ActualNewsListActivity.this, ActualNewsDetailActivity.class);
                intent.putExtra("id",actualNews.get(position).getId());
                startActivity(intent);

            }
        });
        recyclerView.setAdapter(actualNewsAdapter);
    }

}
