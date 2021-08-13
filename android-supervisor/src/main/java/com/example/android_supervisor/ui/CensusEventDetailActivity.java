package com.example.android_supervisor.ui;

import android.os.Bundle;
import android.view.View;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.android_supervisor.R;
import com.example.android_supervisor.entities.Attach;
import com.example.android_supervisor.ui.fragment.CensusEventInfoFragment;
import com.example.android_supervisor.ui.fragment.EventFlowFragment;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author 专项普查详情内容
 */
public class CensusEventDetailActivity extends BaseActivity implements TabLayout.OnTabSelectedListener {
    @BindView(R.id.tl_census)
    TabLayout mTabLayout;

    @BindView(R.id.btn_event_info_edit)
    FloatingActionButton btn_event_info_edit;

    private CensusEventInfoFragment eventInfoFragment;
    private EventFlowFragment eventFlowFragment;

    private List<Fragment> mFragments;
    private int currentPosition = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_detail_tablayout);
        ButterKnife.bind(this);

        initFragments(savedInstanceState);
        mTabLayout.addOnTabSelectedListener(this);

        int savedPosition = -1;
        if (savedInstanceState != null) {
            savedPosition = savedInstanceState.getInt("position", -1);
        }
        if (savedPosition != -1) {
            mTabLayout.setScrollPosition(savedPosition, 0, true);
        } else {
            mTabLayout.setScrollPosition(0, 0, true);
            selectFragment(0);
        }
        mTabLayout.addOnTabSelectedListener(this);
        btn_event_info_edit.setVisibility(View.GONE);
    }

    private void initFragments(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            eventInfoFragment = (CensusEventInfoFragment) fragmentManager.findFragmentByTag("0");
            eventFlowFragment = (EventFlowFragment) fragmentManager.findFragmentByTag("1");
        }

        final String id = getIntent().getStringExtra("id");
        ArrayList<Attach> attachs  = (ArrayList<Attach>) getIntent().getSerializableExtra("attachs");

        mFragments = new ArrayList<>(2);
        if (eventInfoFragment == null) {
//            eventInfoFragment = CensusEventInfoFragment.newInstance(id);
            eventInfoFragment = CensusEventInfoFragment.newInstance(id,attachs);
        }
        mFragments.add(eventInfoFragment);
        if (eventFlowFragment == null) {
            eventFlowFragment = EventFlowFragment.newInstance(1, id);
        }
        mFragments.add(eventFlowFragment);
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
        if (mFragments.size() >0){
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
