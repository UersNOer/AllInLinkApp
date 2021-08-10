package com.allinlink.platformapp.video_project.ui.activity;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;

import com.flyco.tablayout.SlidingTabLayout;
import com.allinlink.platformapp.R;
import com.allinlink.platformapp.video_project.ui.frament.OrderFragment;
import com.unistrong.view.base.BaseTitleActivity;
import com.unistrong.view.navigationbar.INavigationBarBackListener;
import com.unistrong.view.navigationbar.NavigationBar;
import com.unistrong.view.navigationbar.NavigationBuilder;

import java.util.ArrayList;

public class OrderActivity extends BaseTitleActivity {
    String[] titles = new String[]{"未处理", "已处理"};


    @Override
    protected NavigationBuilder onCreateNavigationBarByBuilder(NavigationBuilder builder) {
        NavigationBuilder navigationBuilder = builder.setTitle("工单列表")
                .setType(NavigationBar.BACK_AND_TITLE).setNavigationBarListener(new INavigationBarBackListener() {
                    @Override
                    public void onBackClick() {
                        finish();
                    }
                });
        return navigationBuilder;
    }

    @Override
    public View onCreateContentView() {
        View view = LayoutInflater.from(this).inflate(R.layout.activity_order, null);
        initViews(view);
        return view;
    }

    private void initViews(View view) {
        ArrayList<Fragment> fragments = new ArrayList<>();
        ViewPager vpData = view.findViewById(R.id.vp_data);

        SlidingTabLayout flowLayout = view.findViewById(R.id.tab);
        fragments.add(OrderFragment.getInstance(0));
        fragments.add(OrderFragment.getInstance(1));
        flowLayout.setViewPager(vpData, titles, this, fragments);
    }
}