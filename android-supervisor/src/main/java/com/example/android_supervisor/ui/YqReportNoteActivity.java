package com.example.android_supervisor.ui;


import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager.widget.ViewPager;

import com.example.android_supervisor.ui.fragment.YqCsEventFragmen;
import com.example.android_supervisor.ui.fragment.YqsjEventFragmen;
import com.flyco.tablayout.SlidingTabLayout;
import com.example.android_supervisor.R;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class YqReportNoteActivity extends BaseActivity {


    @BindView(R.id.stl_his)
    SlidingTabLayout mSltLayout;
    @BindView(R.id.vp_his)
    ViewPager mViewPager;

    private YqsjEventFragmen yqsjEventFragmen;
    // 已上报
    private YqCsEventFragmen yqCsEventFragmen;



    private ArrayList<Fragment> mFragments;
    private int index=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_yq_report_note);
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
        mSltLayout.setViewPager(mViewPager, new String[]{"疫情场所", "疫情事件"}, this, mFragments);
        mSltLayout.setCurrentTab(index);
    }

    private void initFragments(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            yqCsEventFragmen = (YqCsEventFragmen) fragmentManager.findFragmentByTag("0");
            yqsjEventFragmen = (YqsjEventFragmen) fragmentManager.findFragmentByTag("1");

        }
        mFragments = new ArrayList<>();

        if (yqCsEventFragmen == null) {
            yqCsEventFragmen = new YqCsEventFragmen();
        }
        mFragments.add(yqCsEventFragmen);


        if (yqsjEventFragmen == null) {
            yqsjEventFragmen = new YqsjEventFragmen();
        }
        mFragments.add(yqsjEventFragmen);


    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("position", index);
    }

}
