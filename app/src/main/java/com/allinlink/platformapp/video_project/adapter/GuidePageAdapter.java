package com.allinlink.platformapp.video_project.adapter;

import android.view.View;
import android.view.ViewGroup;

import androidx.viewpager.widget.PagerAdapter;

import java.util.List;

/**
 * Description:引导页适配器
 * Created by ltd ON 2020/4/12
 * Phone:18600920091
 * Email:td.liu@unistrong.com
 */
public class GuidePageAdapter extends PagerAdapter {

    List<View> mImageView;

    public GuidePageAdapter(List<View> imageView) {
        this.mImageView = imageView;
    }

    @Override
    public int getCount() {
        return mImageView == null ? 0 : mImageView.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        if (mImageView != null && position < mImageView.size()) {
            View view = mImageView.get(position);
            container.removeView(view);
        }
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        if (mImageView != null && position < mImageView.size()) {
            View view = mImageView.get(position);
            container.addView(view);
            return view;
        } else {
            return null;
        }
    }
}
