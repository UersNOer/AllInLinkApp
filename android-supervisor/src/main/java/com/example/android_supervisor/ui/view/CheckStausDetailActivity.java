package com.example.android_supervisor.ui.view;

import android.os.Bundle;
import android.widget.TextView;

import com.example.android_supervisor.R;
import com.example.android_supervisor.entities.CheckStatusRes;
import com.example.android_supervisor.ui.BaseActivity;

import butterknife.BindView;
import butterknife.ButterKnife;


public class CheckStausDetailActivity extends BaseActivity {

    @BindView(R.id.tv_message_title)
    TextView tv_message_title;
    @BindView(R.id.tv_message_content)
    TextView tv_message_content;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.item_check_status);
        ButterKnife.bind(this);
        try{
            CheckStatusRes res = (CheckStatusRes) getIntent().getSerializableExtra("data");
            initData(res);
        }catch (Exception e){

        }

    }


    private void initData(CheckStatusRes res) {

        if (res == null){
            return;
        }
        tv_message_title.setText("抽查回复内容:");
        tv_message_content.setText(res.getAuditOpinion());
    }



    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
