package com.example.android_supervisor.ui.media;

import android.app.Activity;
import android.content.Context;
import androidx.core.widget.ContentLoadingProgressBar;
import androidx.viewpager.widget.PagerAdapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.chrisbanes.photoview.PhotoView;
import com.squareup.picasso.Picasso;
import com.example.android_supervisor.R;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * @author wujie
 */
public class ImagePreviewAdapter extends PagerAdapter {
    private Context mContext;
    private List<String> mData = new ArrayList<>();

    public ImagePreviewAdapter(Context context) {
        mContext = context;
    }

    public int getSize() {
        return mData.size();
    }

    public String get(int index) {
        return mData.get(index);
    }

    public void add(String url) {
        mData.add(url);
    }

    public void addAll(String... urls) {
        Collections.addAll(mData, urls);
    }

    public void addAll(Collection<String> urls) {
        mData.addAll(urls);
    }

    public void remove(int index) {
        mData.remove(index);
    }

    public void clear() {
        mData.clear();
    }

    @Override
    public int getCount() {
        return getSize();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        View view = (View) object;
        view.findViewById(R.id.preview).setOnClickListener(null);

        container.removeView(view);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.pager_image_preview, null);
        PhotoView photoView = (PhotoView) view.findViewById(R.id.preview);
        ContentLoadingProgressBar progressBar = (ContentLoadingProgressBar) view.findViewById(R.id.progress);
        progressBar.hide();

        photoView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((Activity) mContext).finish();
            }
        });

        String url = mData.get(position);
        Picasso.get().load(url).into(photoView);

        container.addView(view);
        return view;
    }
}
