package com.example.android_supervisor.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;

import com.example.android_supervisor.R;
import com.example.android_supervisor.entities.DeptRes;
import com.example.android_supervisor.http.ServiceGenerator;
import com.example.android_supervisor.http.common.ProgressTransformer;
import com.example.android_supervisor.http.response.Response;
import com.example.android_supervisor.http.response.ResponseObserver;
import com.example.android_supervisor.http.service.BasicService;
import com.example.android_supervisor.ui.adapter.DeptListAdapter;
import com.example.android_supervisor.ui.view.ProgressText;
import com.example.android_supervisor.utils.LogUtils;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;

/**
 * 注册页面-> 选择部门页面
 *
 * @author wujie
 */
public class DeptListActivity extends ListActivity {

    private DeptListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setEnableRefresh(false);
        setEnableLoadMore(false);

        adapter = new DeptListAdapter();
        setAdapter(adapter);

        fetchData();
    }

    public void fetchData() {
        if (adapter.size() > 0) {
            adapter.clear();
        }
        BasicService basicService = ServiceGenerator.create(BasicService.class);
        Observable<Response<List<DeptRes>>> observable = basicService.getDeptList2();
        observable.compose(this.<Response<List<DeptRes>>>bindToLifecycle())
                .observeOn(AndroidSchedulers.mainThread())
                .compose(new ProgressTransformer<Response<List<DeptRes>>>(this, ProgressText.load))
                .subscribe(new ResponseObserver<List<DeptRes>>(this) {
                    @Override
                    public void onSuccess(List<DeptRes> data) {
                        LogUtils.e("data = " + data);
                        if (data.size() > 0) {
                            adapter.addAll(data);
                        }
                        setNoData(adapter.size() == 0);
                    }
                });
    }

    @Override
    public void onItemClick(RecyclerView parent, View view, int position, long id) {
        DeptRes deptRes = adapter.get(position);
        Intent intent = new Intent();
        intent.putExtra("id", deptRes.getId());
        intent.putExtra("name", deptRes.getName());
        setResult(Activity.RESULT_OK, intent);
        finish();
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }
}
