package com.allinlink.platformapp.video_project.widget;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import androidx.viewpager.widget.ViewPager;


import com.allinlink.platformapp.R;
import com.allinlink.platformapp.video_project.adapter.GuidePageAdapter;
import com.allinlink.platformapp.video_project.contract.activity.GuidePageEnterButtonClick;

import java.util.ArrayList;
import java.util.List;


/**
 * Description:展示引导页
 * Created by ltd ON 2020/4/12
 * Phone:18600920091
 * Email:td.liu@unistrong.com
 */

public class ShowGuidePage implements ViewPager.OnPageChangeListener {
    private List<View> mImageView;
    private ViewPager mViewPager;
    private GuidePageAdapter mPaperAdapter;
    private ImageView[] mIndicators;
    private ImageView mReplaceIndicator;
    private RelativeLayout mIndicatorGroupLayout;
    private LinearLayout mIndicatorLinearLayout;

    private Context mContext;
    private RelativeLayout guide_relativeLayout;

    /**
     * 引导页结束回调接口，进入主页或登录界面
     */
    private GuidePageEnterButtonClick mEnterMain;

    public ShowGuidePage(RelativeLayout guide_relativeLayout, Context context, GuidePageEnterButtonClick back) {
        this.guide_relativeLayout = guide_relativeLayout;
        this.mContext = context;
        this.mEnterMain = back;
        initComponent();
    }

    /**
     * 初始化组件
     */
    private void initComponent() {
        mViewPager = guide_relativeLayout.findViewById(R.id.viewPager);
        initData();
    }

    /**
     * 初始化数据
     */
    private void initData() {
        int[] guide_page_img = new int[]{R.drawable.guide_one, R.drawable.guide_two, R.drawable.guide_three};
        int guide_page_button = R.drawable.guide_button;
        int length = guide_page_img.length;

        //初始化视图
        mImageView = new ArrayList<>();
        for (int i = 0; i < length; i++) {
            GuidePageView imageView = new GuidePageView(mContext);
            imageView.setGuidePage(1);
            imageView.setGuideImgView(guide_page_img[i]);

            if (i == length - 1) {
                imageView.setGuideEnterImageView(guide_page_button);
                imageView.getGuideEnterImageView().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mEnterMain.guidePageFinish();
                        destroyGuidePage();
                    }

                });
            }
            mImageView.add(imageView);
        }

        mPaperAdapter = new GuidePageAdapter(mImageView);
        mViewPager.setAdapter(mPaperAdapter);
        mViewPager.setOnPageChangeListener(this);

    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
    }

    @Override
    public void onPageSelected(int position) {
    }

    @Override
    public void onPageScrollStateChanged(int state) {
    }

    private void destroyGuidePage() {
        mImageView = null;
        mPaperAdapter = null;
        mViewPager = null;
    }
}
