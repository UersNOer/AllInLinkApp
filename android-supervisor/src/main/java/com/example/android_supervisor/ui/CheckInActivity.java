package com.example.android_supervisor.ui;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.tabs.TabLayout;
import com.squareup.picasso.Picasso;
import com.example.android_supervisor.R;
import com.example.android_supervisor.common.OvalBitmapTransformation;
import com.example.android_supervisor.common.UserSession;
import com.example.android_supervisor.entities.WorkScheRes;
import com.example.android_supervisor.http.ServiceGenerator;
import com.example.android_supervisor.http.request.JsonBody;
import com.example.android_supervisor.http.response.Response;
import com.example.android_supervisor.http.service.PublicService;
import com.example.android_supervisor.ui.fragment.CheckInFragment;
import com.example.android_supervisor.utils.DateUtils;
import com.example.android_supervisor.utils.ToastUtils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;

/**
 * @author wujie
 */
public class CheckInActivity extends BaseActivity {
    @BindView(R.id.iv_check_in_avatar)
    ImageView ivAvatar;

    @BindView(R.id.tv_check_in_name)
    TextView tvName;

    @BindView(R.id.tv_check_in_address)
    TextView tvAddress;

    @BindView(R.id.tl_check_in_work_sche)
    TabLayout tlWorkSche;

    @BindView(R.id.vp_check_in_content)
    ViewPager vpContent;

    private MyFragmentStatePagerAdapter pagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_in);
        ButterKnife.bind(this);

        tvName.setText(UserSession.getUserName(this));
//        searchAddress();

        Picasso.get().load(R.drawable.male)
                .transform(new OvalBitmapTransformation())
                .into(ivAvatar);

        getWorkScheList();

        pagerAdapter = new MyFragmentStatePagerAdapter(getSupportFragmentManager());
        vpContent.setAdapter(pagerAdapter);

        tlWorkSche.setupWithViewPager(vpContent);
    }

//    private void searchAddress() {
//        TXLocationApi.requestSingleLocation(this, TXLocationApi.COORDINATE_TYPE_GCJ02, new ResultCallback<TencentLocation>() {
//            @Override
//            public void onResult(TencentLocation result, int tag) {
//
//            }
//
//            @Override
//            public void onResult(TencentLocation location) {
//                tvAddress.setText(location.getAddress());
//            }
//        });
//    }

    private void getWorkScheList() {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.HOUR_OF_DAY, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        Date begin = c.getTime();

        c.set(Calendar.HOUR_OF_DAY, c.getActualMaximum(Calendar.HOUR_OF_DAY));
        c.set(Calendar.MINUTE, c.getActualMaximum(Calendar.MINUTE));
        c.set(Calendar.SECOND, c.getActualMaximum(Calendar.SECOND));
        Date end = c.getTime();

        HashMap<String, String> map = new HashMap<>();
        map.put("userId", UserSession.getUserId(this));
        map.put("beginTime", DateUtils.format(begin));
        map.put("endTime", DateUtils.format(end));
        JsonBody jsonBody = new JsonBody(map);

        //公共服务
        PublicService publicService = ServiceGenerator.create(PublicService.class);
        Observable<Response<List<WorkScheRes>>> observable = publicService.getWorkScheList(jsonBody);
        observable.compose(this.<Response<List<WorkScheRes>>>bindToLifecycle())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Response<List<WorkScheRes>>>() {
                    @Override
                    public void accept(Response<List<WorkScheRes>> response) throws Exception {
                        if (response.isSuccess()) {
                            List<WorkScheRes> data = response.getData();
                            if (data != null && data.size() > 0) {
                                pagerAdapter.addAll(data);
                            } else {
                                ToastUtils.show(CheckInActivity.this, "今天没有排班，5秒后退出当前页面");
                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        finish();
                                    }
                                }, 5000);
                            }
                        } else {
                            ToastUtils.show(CheckInActivity.this, "获取排班数据失败：" + response.getMessage());
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        throwable.printStackTrace();
                        ToastUtils.show(CheckInActivity.this, "获取排班数据失败");
                    }
                });
    }

    @Override
    protected void onStart() {
        super.onStart();
        IntentFilter intentFilter = new IntentFilter(Intent.ACTION_TIME_TICK);
        registerReceiver(timeTickReceiver, intentFilter);
    }

    @Override
    protected void onStop() {
        super.onStop();
        unregisterReceiver(timeTickReceiver);
    }

    BroadcastReceiver timeTickReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(Intent.ACTION_TIME_TICK)) {
                pagerAdapter.timeTick();
            }
        }
    };

    class MyFragmentStatePagerAdapter extends FragmentStatePagerAdapter {
        final List<WorkScheRes> data = new ArrayList<>();
        final List<CheckInFragment> fragments = new ArrayList<>();

        public MyFragmentStatePagerAdapter(FragmentManager fm) {
            super(fm);
        }

        public void add(WorkScheRes object) {
            this.data.add(object);
            CheckInFragment fragment = new CheckInFragment();
            fragment.setData(object);
            fragments.add(fragment);
            notifyDataSetChanged();
        }

        public void addAll(List<WorkScheRes> data) {
            this.data.addAll(data);
            for (WorkScheRes res : data) {
                CheckInFragment fragment = new CheckInFragment();
                fragment.setData(res);
                fragments.add(fragment);
            }
            notifyDataSetChanged();
        }

        public void clear() {
            data.clear();
            fragments.clear();
            notifyDataSetChanged();
        }

        public void timeTick() {
            Calendar calendar = Calendar.getInstance();
            int hour = calendar.get(Calendar.HOUR_OF_DAY);
            int minute = calendar.get(Calendar.MINUTE);
            String time = String.format("%02d:%02d", hour, minute);
            for (CheckInFragment fragment : fragments) {
                fragment.timeTick(time);
            }
        }

        @Override
        public Fragment getItem(int i) {
            return fragments.get(i);
        }

        @Override
        public int getCount() {
            return data.size();
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            WorkScheRes res = data.get(position);
            String beginTime = res.getBeginTime();
            String endTime = res.getEndTime();
            String title = String.format(Locale.CHINESE, "%s(%s~%s)",
                    res.getName(), beginTime, endTime);
            return title;
        }
    }
}
