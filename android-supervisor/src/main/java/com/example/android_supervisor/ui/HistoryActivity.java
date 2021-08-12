package com.example.android_supervisor.ui;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager.widget.ViewPager;

import com.flyco.tablayout.SlidingTabLayout;
import com.example.android_supervisor.R;
import com.example.android_supervisor.ui.fragment.AutoCachedEventFragment;
import com.example.android_supervisor.ui.fragment.CachedEventFragment;
import com.example.android_supervisor.ui.fragment.HistoryEventFragment;
import com.example.android_supervisor.ui.fragment.HistoryHfFragment;
import com.example.android_supervisor.ui.fragment.HistoryTaskFragment;
import com.example.android_supervisor.ui.fragment.HistoryTjFragment;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author wujie
 */
public class HistoryActivity extends BaseActivity {

    @BindView(R.id.stl_his)
    SlidingTabLayout mSltLayout;
    @BindView(R.id.vp_his)
    ViewPager mViewPager;

    // 暂存案件
    private CachedEventFragment cachedEventFragment;

    // 保存的案件 用于网络好自动上传
    private AutoCachedEventFragment autoCachedEventFragment;


    // 已上报
    private HistoryEventFragment reportedEventFragment;
    // 核实任务
    private HistoryTaskFragment hsTaskFragment;
    // 核查任务
    private HistoryTaskFragment hcTaskFragment;
    // 已退件
    private HistoryTjFragment tjTaskFragment;

    // 抽查回复的
    private HistoryHfFragment hfFragment;


    private ArrayList<Fragment> mFragments;
    private int index=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        ButterKnife.bind(this);

        initFragments(savedInstanceState);

        int savedPosition = -1;
        index = getIntent().getIntExtra("index", 0);
        if (savedInstanceState != null) {
            savedPosition = savedInstanceState.getInt("position", -1);
        }
        if (savedPosition != -1) {
           index=savedPosition;
        }
        mSltLayout.setViewPager(mViewPager, new String[]{"暂存案件","已保存", "已上报", "已核查","已核实","已退件","已回复"}, this, mFragments);
        mSltLayout.setCurrentTab(index);
    }

    private void initFragments(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            cachedEventFragment = (CachedEventFragment) fragmentManager.findFragmentByTag("0");
            autoCachedEventFragment = (AutoCachedEventFragment) fragmentManager.findFragmentByTag("1");
            reportedEventFragment = (HistoryEventFragment) fragmentManager.findFragmentByTag("2");
            hcTaskFragment = (HistoryTaskFragment) fragmentManager.findFragmentByTag("3");
            hsTaskFragment = (HistoryTaskFragment) fragmentManager.findFragmentByTag("4");
            tjTaskFragment = (HistoryTjFragment) fragmentManager.findFragmentByTag("5");
            hfFragment = (HistoryHfFragment) fragmentManager.findFragmentByTag("6");
        }

        mFragments = new ArrayList<>(4);

        if (cachedEventFragment == null) {
            cachedEventFragment = new CachedEventFragment();
        }
        mFragments.add(cachedEventFragment);

        if (autoCachedEventFragment == null) {
            autoCachedEventFragment = new AutoCachedEventFragment();
        }
        mFragments.add(autoCachedEventFragment);

        if (reportedEventFragment == null) {
            reportedEventFragment = new HistoryEventFragment();
            reportedEventFragment.setShowTime();
        }
        mFragments.add(reportedEventFragment);

        if (hcTaskFragment == null) {
            hcTaskFragment = HistoryTaskFragment.newInstance(1);
        }
        mFragments.add(hcTaskFragment);

        if (hsTaskFragment == null) {
            hsTaskFragment = HistoryTaskFragment.newInstance(2);
        }
        mFragments.add(hsTaskFragment);

        if (tjTaskFragment == null) {
            tjTaskFragment = new HistoryTjFragment();
        }
        mFragments.add(tjTaskFragment);

        if (hfFragment == null) {
            hfFragment = new HistoryHfFragment();
        }
        mFragments.add(hfFragment);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("position", index);
    }
}
