package com.example.android_supervisor.ui.adapter;

import android.widget.TextView;

import com.cncoderx.recyclerviewhelper.adapter.ObjectAdapter;
import com.example.android_supervisor.R;
import com.example.android_supervisor.entities.DeptRes;

/**
 * @author wujie
 */
public class DeptListAdapter extends ObjectAdapter<DeptRes> {

    public DeptListAdapter() {
        super(R.layout.item_dept_list);
    }

    @Override
    public void onBindViewHolder(BaseViewHolder holder, DeptRes object, int position) {
        TextView tvName = holder.getView(R.id.tv_dept_name, TextView.class);
        tvName.setText(object.getName());
    }
}
