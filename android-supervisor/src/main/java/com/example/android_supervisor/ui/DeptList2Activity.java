package com.example.android_supervisor.ui;

import android.os.Bundle;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.cncoderx.recyclerviewhelper.adapter.ObjectAdapter;
import com.example.android_supervisor.Presenter.YqTypePresenter;
import com.example.android_supervisor.R;
import com.example.android_supervisor.entities.DeptRes;
import com.example.android_supervisor.http.ServiceGenerator;
import com.example.android_supervisor.http.common.ProgressTransformer;
import com.example.android_supervisor.http.response.Response;
import com.example.android_supervisor.http.response.ResponseObserver;
import com.example.android_supervisor.http.service.BasicService;
import com.example.android_supervisor.ui.adapter.DeptListAdapter;
import com.example.android_supervisor.ui.model.CaseClassTreeVO;
import com.example.android_supervisor.ui.view.ProgressText;
import com.example.android_supervisor.utils.LogUtils;
import com.example.android_supervisor.utils.ToastUtils;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;

/**
 * 注册页面-> 选择部门页面
 *
 * @author wujie
 */
public class DeptList2Activity extends ListActivity {

    private ItemAdapter itemAdapter;
    private DeptListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setEnableRefresh(false);
        setEnableLoadMore(false);

//        itemAdapter  = new ItemAdapter();
//        mRecyclerView.setAdapter(itemAdapter);
        adapter = new DeptListAdapter();
        setAdapter(adapter);
        getDayJk();
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

    private void getDayJk() {
        YqTypePresenter presenter = new YqTypePresenter();
        presenter.getType(this, new YqTypePresenter.CallBack() {
            @Override
            public void onSuccess(List<CaseClassTreeVO> types) {
                ArrayList<CaseClassTreeVO> temp = new ArrayList<>();
                for (CaseClassTreeVO caseClassTreeVO:types){
                    if (caseClassTreeVO.getQuestionClassId().equals("0101")){
                        temp.add(caseClassTreeVO);
                    }
                }
                itemAdapter.addAll(temp);
            }
        });

    }


    @Override
    public void onItemClick(RecyclerView parent, View view, int position, long id) {
        ToastUtils.show(DeptList2Activity.this,"dddddd");
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }



    public class ItemAdapter extends ObjectAdapter<CaseClassTreeVO> {


        public ItemAdapter() {
            super(R.layout.item_dept_list);
        }


        @Override
        public void onBindViewHolder(BaseViewHolder holder, CaseClassTreeVO object, int position) {
            TextView tv_cs_type = holder.getView(R.id.tv_dept_name, TextView.class);
            tv_cs_type.setText(object.getCountWhSys() == 0? String.format("%s ",object.getNodeName()):
                    String.format("%s ( %d )",object.getNodeName(),object.getCountWhSys()));

//            LinearLayout ll_child_item = holder.getView(R.id.ll_child_item, LinearLayout.class);
//            ll_child_item.setVisibility(View.VISIBLE);
        }

    }
}
