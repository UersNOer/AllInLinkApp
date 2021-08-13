package com.example.android_supervisor.ui;


import android.content.Intent;
import android.os.Bundle;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.cncoderx.recyclerviewhelper.adapter.ObjectAdapter;
import com.example.android_supervisor.R;
import com.example.android_supervisor.entities.CensusTaskRes;

import java.util.ArrayList;
import java.util.List;

public class YqZHQKActivity extends ListActivity {

    ArrayAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_yq_zhqk);

        setEnableRefresh(false);
        setEnableLoadMore(false);

        adapter = new ArrayAdapter();
        setAdapter(adapter);

        initData();

    }

    private void initData() {

        List<CensusTaskRes> list  = new ArrayList<>();
        {
            CensusTaskRes taskRes = new CensusTaskRes();
            taskRes.setName("场所人员每日健康情况");
            list.add(taskRes);
        }

        {
            CensusTaskRes taskRes = new CensusTaskRes();
            taskRes.setName("卡点涉疫人员/车辆检查情况");
            list.add(taskRes);
        }


        adapter.addAll(list);

    }


    public class ArrayAdapter extends ObjectAdapter<CensusTaskRes> {

        public ArrayAdapter() {
            super(R.layout.task_list_template);
        }

        @Override
        public void onBindViewHolder(BaseViewHolder holder, CensusTaskRes object, int position) {

            TextView tvTitle = holder.getView(R.id.tv_census_task_title, TextView.class);
            tvTitle.setText(object.getName());

        }


    }


    public void onItemClick(RecyclerView parent, View view, int position, long id) {


        if (position == 0){

            Intent intent = new Intent(this, YqZHQKDayActivity.class);
            startActivity(intent);
        }else if (position ==1){

            Intent intent = new Intent(this, YqkdDayDetailsActivity.class);
            startActivity(intent);

        }


        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }
}
