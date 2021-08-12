package com.example.android_supervisor.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.View;

import com.example.android_supervisor.R;
import com.example.android_supervisor.entities.MapBundle;
import com.example.android_supervisor.ui.fragment.AMapFragment;
import com.example.android_supervisor.ui.fragment.CensusEventListFragment;
import com.example.android_supervisor.ui.fragment.CensusTaskInfoFragment;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @author 普查任务展示
 */
public class CensusTaskInfoActivity extends BaseActivity implements TabLayout.OnTabSelectedListener {
    @BindView(R.id.tab_layout)
    TabLayout mTabLayout;

    private CensusTaskInfoFragment censusTaskInfoFragment;
    private CensusEventListFragment censusEventListFragment;
   // private CensusMapFragment censusMapFragment;
    private AMapFragment censusMapFragment ;
    private List<Fragment> mFragments;
    private int currentPosition = -1;

    private String taskId;
    private String taskCode;
    private String taskName;
    private String taskArea;
    private Double geoX,geoY;

    @BindView(R.id.btn_census_task_info_new)
    FloatingActionButton fab_task;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_census_task_info);
        ButterKnife.bind(this);

        initFragments(savedInstanceState);
        mTabLayout.addOnTabSelectedListener(this);

        int savedPosition = -1;
        if (savedInstanceState != null) {
            savedPosition = savedInstanceState.getInt("position", -1);
        }
        if (savedPosition != -1) {
            mTabLayout.setScrollPosition(savedPosition, 0, true);
            selectFragment(savedPosition);
        } else {
            mTabLayout.setScrollPosition(0, 0, true);
            selectFragment(0);
        }
    }

    private void initFragments(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            censusTaskInfoFragment = (CensusTaskInfoFragment) fragmentManager.findFragmentByTag("0");
            censusEventListFragment = (CensusEventListFragment) fragmentManager.findFragmentByTag("1");
            censusMapFragment = (AMapFragment) fragmentManager.findFragmentByTag("2");
        }

        taskId = getIntent().getStringExtra("id");
        taskCode = getIntent().getStringExtra("code");
        taskName = getIntent().getStringExtra("name");
        taskArea = getIntent().getStringExtra("areaName");

        geoX = getIntent().getDoubleExtra("geoX", 0.0);
        geoY = getIntent().getDoubleExtra("geoY",0.0);


        mFragments = new ArrayList<>(3);
        if (censusTaskInfoFragment == null) {
            censusTaskInfoFragment = CensusTaskInfoFragment.newInstance(taskId);
        }
        mFragments.add(censusTaskInfoFragment);
        if (censusEventListFragment == null) {
            censusEventListFragment = CensusEventListFragment.newInstance(taskId);
        }
        mFragments.add(censusEventListFragment);
        if (censusMapFragment == null) {

            Bundle bundle  = new Bundle();
            MapBundle mapBundle = new MapBundle();
            mapBundle.isShowGird = false;
            mapBundle.isShowCase = false;
            mapBundle.taskId = taskId;
            bundle.putSerializable("mapBundle",mapBundle);

            censusMapFragment = AMapFragment.newInstance(bundle);
        }
        mFragments.add(censusMapFragment);
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
                if (fragment instanceof AMapFragment){
                    fab_task.setVisibility(View.GONE);
                }else {
                    fab_task.setVisibility(View.VISIBLE);
                }
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

    @OnClick(R.id.btn_census_task_info_new)
    public void onNewEvent(View v) {
        Intent intent = new Intent(this, CensusEventNewActivity.class);
        intent.putExtra("taskId", taskId);
        intent.putExtra("taskCode", taskCode);
        intent.putExtra("taskName", taskName);
        intent.putExtra("taskArea",taskArea);
        intent.putExtra("geoX", geoX);
        intent.putExtra("geoY",geoY);

        intent.putExtra("TaskClass", censusTaskInfoFragment.getTaskClass());
        ;
        startActivityForResult(intent, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK && requestCode == 1) {
            censusEventListFragment.fetchData(false, false);
            censusMapFragment.loadZXMarker(taskId);
        }
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
    }
}
