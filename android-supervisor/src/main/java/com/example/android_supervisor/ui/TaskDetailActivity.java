package com.example.android_supervisor.ui;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;

import com.flyco.tablayout.SlidingTabLayout;
import com.example.android_supervisor.R;
import com.example.android_supervisor.ui.fragment.EventFlowFragment;
import com.example.android_supervisor.ui.fragment.EventInfoFragment;
import com.example.android_supervisor.ui.fragment.TaskHandleFragment;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;


public class TaskDetailActivity extends BaseActivity implements TabLayout.OnTabSelectedListener {
//    @BindView(R.id.tab_layout)
//    TabLayout mTabLayout;


    @BindView(R.id.stl_his)
    SlidingTabLayout mSltLayout;
    @BindView(R.id.vp_his)
    ViewPager mViewPager;


    private EventInfoFragment eventInfoFragment;
    private EventFlowFragment eventFlowFragment;
    private TaskHandleFragment taskHandleFragment;

    private ArrayList<Fragment> mFragments;
    private int currentPosition = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_detail);
        ButterKnife.bind(this);

        initFragments(savedInstanceState);
//        mTabLayout.addOnTabSelectedListener(this);
//
//        int savedPosition = -1;
//        if (savedInstanceState != null) {
//            savedPosition = savedInstanceState.getInt("position", -1);
//        }
//        if (savedPosition != -1) {
//            mTabLayout.setScrollPosition(savedPosition, 0, true);
//        } else {
//            mTabLayout.setScrollPosition(0, 0, true);
//            selectFragment(0);
//        }
    }


    private void initFragments(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            eventInfoFragment = (EventInfoFragment) fragmentManager.findFragmentByTag("0");
            eventFlowFragment = (EventFlowFragment) fragmentManager.findFragmentByTag("1");
            taskHandleFragment = (TaskHandleFragment) fragmentManager.findFragmentByTag("2");
        }

        final int status = getIntent().getIntExtra("status", 1);
        final String id = getIntent().getStringExtra("id");
        final int taskStatus = getIntent().getIntExtra("taskStatus", 0);
        final boolean handled = getIntent().getBooleanExtra("handled", false);

        mFragments = new ArrayList<>(3);
        if (eventInfoFragment == null) {
            eventInfoFragment = EventInfoFragment.newInstance(status, id, handled,taskStatus);
        }
        mFragments.add(eventInfoFragment);
        if (eventFlowFragment == null) {
            eventFlowFragment = EventFlowFragment.newInstance(status, id);
        }
        mFragments.add(eventFlowFragment);
        if (taskHandleFragment == null) {
            taskHandleFragment = TaskHandleFragment.newInstance(status, id);
        }
        mFragments.add(taskHandleFragment);

        mSltLayout.setViewPager(mViewPager, new String[]{"基础信息", "任务流程", "办理任务"}, this, mFragments);
        mSltLayout.setCurrentTab(0);
        mViewPager.setOffscreenPageLimit(0);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("position", currentPosition);
    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        int position = tab.getPosition();
        selectFragment(position);
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {
        int position = tab.getPosition();
        unselectFragment(position);
    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }

    private void selectFragment(int position) {
        if (position != -1 && currentPosition != position) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            Fragment fragment = mFragments.get(position);
            if (fragment != null) {
                if (!fragment.isAdded()) {
                    ft.add(R.id.container, fragment, String.valueOf(position));
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
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
    }
}
