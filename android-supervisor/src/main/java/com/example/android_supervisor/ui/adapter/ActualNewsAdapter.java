package com.example.android_supervisor.ui.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.android_supervisor.R;
import com.example.android_supervisor.entities.ActualNews;
import com.example.android_supervisor.utils.DateUtils;

import java.util.List;

/**
 * Created by dw on 2019/6/28.
 */
public class ActualNewsAdapter extends BaseQuickAdapter<ActualNews, BaseViewHolder> {

    public ActualNewsAdapter(int layoutId, List<ActualNews> actualNewsList) {
        super(layoutId, actualNewsList);
    }

    @Override
    protected void convert(BaseViewHolder helper, ActualNews actualNews) {
        helper.setText(R.id.news_title, actualNews.getTitle());
        helper.setText(R.id.news_time, DateUtils.getTimeShort(DateUtils.parse(actualNews.getCreateTime())));
    }
}
