package com.example.android_supervisor.map;

import android.util.Log;

import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.android_supervisor.R;
import com.example.android_supervisor.entities.AmapEntities;

import java.util.List;

/**
 * Created by dw on 2019/7/24.
 */
public class AMapAdapter extends BaseQuickAdapter<AmapEntities, BaseViewHolder> {
    public AMapAdapter(int layoutResId, @Nullable List<AmapEntities> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, AmapEntities item) {
        Log.d(TAG, "search: "+item.getTitle());
        helper.setText(R.id.tv_map_search_title,item.getTitle());
        helper.setText(R.id.tv_map_search_address,item.getAddress());
    }
}
