package com.example.android_supervisor.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import com.example.android_supervisor.R;
import com.example.android_supervisor.entities.EventType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author wujie
 */
public class SecondaryTypeAdapter extends BaseExpandableListAdapter {
    private Context mContext;
    private List<EventType> mGroupData = new ArrayList<>();
    private Map<String, List<EventType>> mChildData = new HashMap<>();

    public SecondaryTypeAdapter(Context context) {
        mContext = context;
    }

    public void setGroupData(List<EventType> data) {
        mGroupData.addAll(data);
        notifyDataSetChanged();
    }

    public void setChildData(String groupId, List<EventType> data) {
        mChildData.put(groupId, data);
        notifyDataSetChanged();
    }

    public void clearData() {
        mGroupData.clear();
        mChildData.clear();
        notifyDataSetChanged();
    }

    @Override
    public int getGroupCount() {
        return mGroupData.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        String groupId = mGroupData.get(groupPosition).getId();
        List<EventType> childData = mChildData.get(groupId);
        return childData == null ? 0 : childData.size();
    }

    @Override
    public EventType getGroup(int groupPosition) {
        return mGroupData.get(groupPosition);
    }

    @Override
    public EventType getChild(int groupPosition, int childPosition) {
        String groupId = mGroupData.get(groupPosition).getId();
        List<EventType> childData = mChildData.get(groupId);
        return childData == null ? null : childData.get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        View view;
        if (convertView == null) {
            view = LayoutInflater.from(mContext).inflate(R.layout.item_primary_type, parent, false);
        } else {
            view = convertView;
        }
        EventType groupInfo = getGroup(groupPosition);
        TextView textView = view.findViewById(android.R.id.text1);
        textView.setText(groupInfo.getName());
        textView.setSelected(isExpanded);
        if (isExpanded) {
            view.setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.bg_rect_blue));
        } else {
            view.setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.bg_rect_white));
        }
        return view;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        View view;
        if (convertView == null) {
            view = LayoutInflater.from(mContext).inflate(R.layout.item_secondary_type, parent, false);
        } else {
            view = convertView;
        }
        EventType childInfo = getChild(groupPosition, childPosition);
        TextView textView = view.findViewById(android.R.id.text1);
        textView.setText(childInfo.getName());
        return view;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}
