package com.example.android_supervisor.ui;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.exoplayer2.metadata.emsg.EventMessage;
import com.example.android_supervisor.R;
import com.example.android_supervisor.attendance.AttendanceManager;
import com.example.android_supervisor.cache.DataCacheManager;
import com.example.android_supervisor.common.UserSession;
import com.example.android_supervisor.common.initializer.TrtcInitializer;
import com.example.android_supervisor.entities.CaseLevel;
import com.example.android_supervisor.entities.EventType;
import com.example.android_supervisor.entities.MapBundle;
import com.example.android_supervisor.service.AppServiceManager;
import com.example.android_supervisor.socketio.MsgManager;
import com.example.android_supervisor.sqlite.PublicSqliteHelper;
import com.example.android_supervisor.ui.fragment.AMapFragment;
import com.example.android_supervisor.ui.fragment.HomeFragment;
import com.example.android_supervisor.ui.fragment.MessageTitleFragment;
import com.example.android_supervisor.ui.fragment.MineFragment;
import com.example.android_supervisor.utils.PdaToServer;
import com.example.android_supervisor.utils.ToastUtils;
import com.google.android.material.tabs.TabLayout;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends BaseActivity implements TabLayout.OnTabSelectedListener {
    @BindView(R.id.iv_title_bar_nav)
    ImageView ivNav;

    @BindView(R.id.main_bottom_tab_layout)
    TabLayout mBottomTabLayout;

    private HomeFragment mHomeFragment;
    private MessageTitleFragment mMsgFragment;
    //    private PreviewMapFragment mMapFragment;
    private AMapFragment mMapFragment;
    //    private SettingFragment mSetFragment;
    private MineFragment mMineFragment;

    private List<Fragment> mFragments;
    private int currentPosition = -1;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //add new
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        ivNav.setVisibility(View.GONE);
        initFragments(savedInstanceState);
        mBottomTabLayout.addOnTabSelectedListener(this);
        int savedPosition = -1;
        if (savedInstanceState != null) {
            savedPosition = savedInstanceState.getInt("position", -1);
        }
        if (savedPosition != -1) {
            mBottomTabLayout.setScrollPosition(savedPosition, 0, true);
        } else {
            mBottomTabLayout.setScrollPosition(0, 0, true);
            selectFragment(0);
        }

        MsgManager.getInstance(this).addTestMsg(new MsgManager.MsgCallBack() {
            @Override
            public void addMsgSuccess() {
                setMsgBadge(MsgManager.getInstance(MainActivity.this).getAllMsgCount());
            }
        });

        EventBus.getDefault().register(this);

        initAppRun();
    }

    private void initAppRun() {
        writePdaToServer();
        //初始化腾讯云视频
        new TrtcInitializer().initialize(this);
        AttendanceManager.getInstance(this).loadAttendancePoint();

    }


    @Subscribe(threadMode = ThreadMode.MAIN,priority = 1)
    public void onReceiveMsg(EventMessage message) {
        Log.e("d", "onReceiveMsg: " + message.toString());
    }

    @Override
    protected void onResume() {
        super.onResume();
        EventBus.getDefault().post(1);
    }

    private void writePdaToServer() {
        PdaToServer.writePda(this);

    }

    public void setMsgBadge(int count) {

        TabLayout.Tab tab =mBottomTabLayout.getTabAt(1);
        View view = tab.getCustomView();
//        //增加动画效果
//        Animation animation = AnimationUtils.loadAnimation(this, R.anim.slide_out_to_right);
//        LinearInterpolator lin = new LinearInterpolator();
//        animation.setInterpolator(lin);
//        view.startAnimation(animation);

        TextView tv_msgCount = view.findViewById(R.id.tv_msgCount);
        tv_msgCount.setVisibility(View.VISIBLE);
        tv_msgCount.setText(String.valueOf(count));
        tv_msgCount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //此处加入 消息角标锚点功能

            }
        });
        if (count == 0){
            tv_msgCount.setVisibility(View.GONE);
        }
    }

    private void initFragments(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            mHomeFragment = (HomeFragment) fragmentManager.findFragmentByTag("0");
            mMsgFragment = (MessageTitleFragment) fragmentManager.findFragmentByTag("1");
            mMapFragment = (AMapFragment) fragmentManager.findFragmentByTag("2");
//            mSetFragment = (SettingFragment) fragmentManager.findFragmentByTag("3");
            mMineFragment = (MineFragment) fragmentManager.findFragmentByTag("3");
        }

        mFragments = new ArrayList<>(3);
        if (mHomeFragment == null) {
            mHomeFragment = new HomeFragment();
        }
        mFragments.add(mHomeFragment);
        if (mMsgFragment == null) {
            mMsgFragment = new MessageTitleFragment();
        }
        mFragments.add(mMsgFragment);

        if (mMapFragment == null) {

            Bundle bundle  = new Bundle();
            MapBundle mapBundle = new MapBundle();
            mapBundle.isShowGird = true;
            mapBundle.isShowCase = true;
            bundle.putSerializable("mapBundle",mapBundle);
            mMapFragment = AMapFragment.newInstance(bundle);
        }
        mFragments.add(mMapFragment);

        if (mMineFragment == null) {
            mMineFragment = new MineFragment();
        }
        mFragments.add(mMineFragment);
//        if (mSetFragment == null) {
//            mSetFragment = new SettingFragment();
//        }
//        mFragments.add(mSetFragment);
    }

    private void selectFragment(int position) {
        if (position != -1 && currentPosition != position) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            Fragment fragment = mFragments.get(position);
            if (fragment != null) {
                if (!fragment.isAdded()) {
                    ft.add(R.id.main_fragment_container, fragment, String.valueOf(position));
                }
                ft.show(fragment).commit();
                currentPosition = position;
            }
        }

    }

    private void unselectFragment(int position) {
        if (position != -1 && currentPosition == position) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            Fragment fragment = mFragments.get(position);
            if (fragment != null && fragment.isAdded() && !fragment.isHidden()) {
                ft.hide(fragment).commit();
            }
            currentPosition = -1;
        }
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        AppServiceManager.start(this);

        try{
            DataCacheManager.automaticSync(this);

            //增加本地判断数据是否为空 后期增加数据更新 发送消息
            boolean isDataEmpty = false;
            List<EventType>  eventTypes= PublicSqliteHelper.getInstance(this).getEventTypeDao().queryForAll();
            if (eventTypes==null || eventTypes.size()==0){
                isDataEmpty = true;
            }
            List<CaseLevel>  caseLevels= PublicSqliteHelper.getInstance(this).getCaseLevelDao().queryForAll();
            if (caseLevels==null || caseLevels.size()==0){
                isDataEmpty = true;
            }

            if (!UserSession.getDataUpdated(this) || isDataEmpty){
                DataCacheManager.manualSync(this, true);
            }

        }catch (Exception e){
            ToastUtils.show(MainActivity.this,"数据同步异常"+e.getMessage()+" 请重启app");
            DataCacheManager.cleanDb(MainActivity.this);
            DataCacheManager.cleanUserDb(MainActivity.this);
        }

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
//        outState.putInt("position", currentPosition);
    }

    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }

    public void logout() {
        AppServiceManager.stop(this);
//        EaseUI.getInstance().getNotifier().reset();
//        EMClient.getInstance().logout(true);
        finish();
    }

    final int[] iconNormalRes = {R.drawable.ic_home, R.drawable.ic_msg, R.drawable.ic_map, R.drawable.ic_set};
    final int[] iconSelectRes = {R.drawable.ic_home_fill, R.drawable.ic_msg_fill, R.drawable.ic_map_fill, R.drawable.ic_set_fill};

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        int position = tab.getPosition();
        if (position != -1) {
            tab.setIcon(iconSelectRes[position]);
        }
        //增加动画效果
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.modal_in);
        LinearInterpolator lin = new LinearInterpolator();
        animation.setInterpolator(lin);
        tab.getCustomView().startAnimation(animation);

        tab.getCustomView().setSelected(true);
        selectFragment(position);
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {
        int position = tab.getPosition();
        if (position != -1) {
            tab.setIcon(iconNormalRes[position]);
        }
        tab.getCustomView().setSelected(false);
        unselectFragment(position);

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }


    public void setHomeIcon(String imageUrl){
        mHomeFragment.setIcon(imageUrl);
    }

}
