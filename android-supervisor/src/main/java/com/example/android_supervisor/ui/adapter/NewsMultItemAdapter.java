package com.example.android_supervisor.ui.adapter;

import android.util.Log;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.squareup.picasso.Picasso;
import com.example.android_supervisor.R;
import com.example.android_supervisor.entities.NewsMultItem;

import java.util.List;

/**
 * Created by dw on 2019/6/28.
 */
public class NewsMultItemAdapter extends BaseMultiItemQuickAdapter<NewsMultItem,BaseViewHolder> {
    /**
     * Same as QuickAdapter#QuickAdapter(Context,int) but with
     * some initialization data.
     *
     * @param data A new list is created out of this one to avoid mutable list
     */
    public NewsMultItemAdapter(List<NewsMultItem> data) {
        super(data);
        addItemType(NewsMultItem.IMG, R.layout.image_view);
        addItemType(NewsMultItem.VIDEO, R.layout.video_view);
        addItemType(NewsMultItem.AUDIO, R.layout.image_view);
    }

    @Override
    protected void convert(BaseViewHolder helper, NewsMultItem item) {
        switch (helper.getItemViewType()) {
            case NewsMultItem.AUDIO:
                break;
            case NewsMultItem.VIDEO:
                break;
            case NewsMultItem.IMG:
                Log.d(TAG, "convert: "+item.getUrl());
                ImageView view = helper.getView(R.id.news_image);
                Picasso.get().load(item.getUrl()).into(view);
                break;
        }
    }
}
