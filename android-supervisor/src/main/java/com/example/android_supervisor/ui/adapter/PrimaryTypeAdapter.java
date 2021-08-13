package com.example.android_supervisor.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.android_supervisor.R;
import com.example.android_supervisor.entities.EventType;

import java.util.ArrayList;
import java.util.List;

/**
 * @author wujie
 */
public class PrimaryTypeAdapter extends BaseAdapter {
    private Context mContext;
    private List<EventType> mData = new ArrayList<>();

    public PrimaryTypeAdapter(Context context) {
        mContext = context;
    }

    public void setData(List<EventType> data) {
        mData.addAll(data);
        notifyDataSetChanged();
    }

    public void clearData() {
        mData.clear();
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public EventType getItem(int position) {
        return mData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v;
        if (convertView == null) {
            v = LayoutInflater.from(mContext).inflate(R.layout.item_primary_type, parent, false);
        } else {
            v = convertView;
        }
        TextView textView = v.findViewById(android.R.id.text1);
        textView.setText(getItem(position).getName());
        return v;
    }
}
