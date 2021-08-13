package com.example.android_supervisor.ui;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.flyco.tablayout.SlidingTabLayout;
import com.example.android_supervisor.R;
import com.example.android_supervisor.ui.fragment.CheckUpListFragment;
import com.example.android_supervisor.ui.fragment.MyTaskListFragment;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 个人任务
 */
public class MyTaskListActivity extends BaseActivity {

    @BindView(R.id.stl_his)
    SlidingTabLayout mSltLayout;
    @BindView(R.id.vp_his)
    ViewPager mViewPager;


    private MyTaskListFragment myTaskHcFragment;//核查
    private MyTaskListFragment myTaskHsFragment;//核实
    private CheckUpListFragment myTaskCcFragment;//抽查

    private ArrayList<Fragment> mFragments;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_task);
        ButterKnife.bind(this);
        initViewPager();

    }

    private void initViewPager() {
        mFragments = new ArrayList<>(3);
        if (myTaskHcFragment == null) {
            myTaskHcFragment = MyTaskListFragment.newInstance(1);
        }
        mFragments.add(myTaskHcFragment);
        if (myTaskHsFragment == null) {
            myTaskHsFragment = MyTaskListFragment.newInstance(2);
        }
        mFragments.add(myTaskHsFragment);
        if (myTaskCcFragment == null) {
            myTaskCcFragment = new CheckUpListFragment();
        }
        mFragments.add(myTaskCcFragment);
//      无需编写适配器，一行代码关联TabLayout与ViewPager
        mSltLayout.setViewPager(mViewPager, new String[]{"核查任务", "核实任务", "抽查回复"}, this, mFragments);
        mSltLayout.setCurrentTab(0);
        mViewPager.setOffscreenPageLimit(0);
    }
}
