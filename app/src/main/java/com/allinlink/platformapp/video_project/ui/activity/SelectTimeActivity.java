package com.allinlink.platformapp.video_project.ui.activity;

import androidx.annotation.Nullable;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.bigkoo.pickerview.builder.TimePickerBuilder;
import com.bigkoo.pickerview.listener.OnTimeSelectListener;
import com.bigkoo.pickerview.view.TimePickerView;
import com.unistrong.utils.RxBus;
import com.unistrong.utils.StringsUtils;
import com.allinlink.platformapp.R;
import com.allinlink.platformapp.video_project.bean.ChannelBean;
import com.allinlink.platformapp.video_project.bean.PlayBackBean;
import com.allinlink.platformapp.video_project.contract.activity.SelectTimeContract;
import com.allinlink.platformapp.video_project.presenter.activity.SelectTimePresenter;
import com.allinlink.platformapp.video_project.utils.StringUtil;
import com.unistrong.view.base.BaseActivity;
import com.unistrong.view.base.BaseTitleActivity;
import com.unistrong.view.navigationbar.INavigationBarBackListener;
import com.unistrong.view.navigationbar.NavigationBar;
import com.unistrong.view.navigationbar.NavigationBuilder;
import com.unistrong.view.utils.ToastUtil;

import java.util.Calendar;
import java.util.Date;

public class SelectTimeActivity extends BaseActivity<SelectTimePresenter> implements View.OnClickListener, SelectTimeContract.View {
    TextView tvStartTime, tvEndTime;
    private TimePickerView pvEnd;
    private TimePickerView pvStart;
    ChannelBean channelBean;
    Date date1 = new Date();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_time);
        initViews();
    }


    private void initViews() {
        mPresenter = new SelectTimePresenter(this);
        channelBean = (ChannelBean) getIntent().getSerializableExtra("data");
        findViewById(R.id.bt_querychannel).setOnClickListener(this);
        findViewById(R.id.iv_collect).setOnClickListener(this);

        tvStartTime = findViewById(R.id.tv_starttime);
        tvEndTime = findViewById(R.id.tv_endtime);
        tvEndTime.setOnClickListener(this);
        tvStartTime.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.bt_querychannel) {
            if (TextUtils.isEmpty(tvStartTime.getText())) {
                ToastUtil.showErrorToast("请选择开始时间");
                return;

            }
            if (TextUtils.isEmpty(tvEndTime.getText())) {
                ToastUtil.showErrorToast("请选择开始时间");
                return;
            }
            mPresenter.queryChannelById(channelBean.getGid(), tvStartTime.getText().toString(), tvEndTime.getText().toString());

        } else if (v.getId() == R.id.iv_collect) {
            finish();
        } else
            showTimePickView(v);
    }

    private void showTimePickView(View view) {
        if (view.getId() == R.id.tv_starttime) {
            if (pvStart == null) {
                pvStart = new TimePickerBuilder(this, new OnTimeSelectListener() {
                    @Override
                    public void onTimeSelect(Date date, View v) {
                        if (date1.getTime() < date.getTime()) {
                            Toast.makeText(SelectTimeActivity.this, "请选择合理的时间", Toast.LENGTH_SHORT).show();
                            return;
                        } else if ((date1.getYear() == date.getYear())&&(date1.getMonth() == date.getMonth()&&date1.getDay() == date.getDay())) {
                            Calendar c = Calendar.getInstance();
                            c.setTime(date);
                            c.set(Calendar.HOUR_OF_DAY,0);
                            c.set(Calendar.MINUTE, 0);
                            c.set(Calendar.SECOND, 0);
                            tvStartTime.setText(StringUtil.simpleDateString(c.getTime()));

//                            c.add(Calendar.DAY_OF_MONTH, 1);
//                            c.add(Calendar.SECOND, -1);
                            tvEndTime.setText(StringUtil.simpleDateString(date));
                        } else {
                            //选择开始时间，自动添加一天
                            tvStartTime.setText(StringUtil.simpleDateString(date));
                            Calendar c = Calendar.getInstance();
                            c.setTime(date);
                            c.add(Calendar.DAY_OF_MONTH, 1);
                            tvEndTime.setText(StringUtil.simpleDateString(c.getTime()));

                        }


                    }
                }).setTitleSize(16)//标题文字大小
                        .setType(new boolean[]{true, true, true, true, true, true})
                        .setTitleText("选择开始时间")//标题文字
                        .isCyclic(false)//是否循环滚动
//                    .setRangDate(startDate, endDate)//起始终止年月日设定
                        .isCenterLabel(false) //是否只显示中间选中项的label文字，false则每项item全部都带有label。
                        .build();
            }
            pvStart.show();
        }
    }

    @Override
    public void onError(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_LONG).show();

    }

    @Override
    public Context getContext() {
        return null;
    }

    @Override
    public void querySuccess(PlayBackBean backBean) {
        RxBus.getInstance().send(backBean);
        finish();
    }
}