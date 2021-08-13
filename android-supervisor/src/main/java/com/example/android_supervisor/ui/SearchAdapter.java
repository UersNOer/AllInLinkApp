package com.example.android_supervisor.ui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.example.android_supervisor.R;
import com.example.android_supervisor.ui.model.YqcsDayRes;

import java.util.ArrayList;
import java.util.List;

/**
 * @author wujie
 */
public class SearchAdapter extends BaseAdapter implements Filterable {
    private Context context;
    private Filter mFilter;
    private String region;
    private List<YqcsDayRes.WhDetailsListBean> mList;
    private ArrayList<YqcsDayRes.WhDetailsListBean> mUnfilteredData;

    public SearchAdapter(Context context,List<YqcsDayRes.WhDetailsListBean> mList) {
        this.context = context;
        this.mList = mList;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public YqcsDayRes.WhDetailsListBean getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view;
        ViewHolder vh;
        if (convertView == null) {
            view = LayoutInflater.from(context).inflate(R.layout.item_map_search, parent, false);
            vh = new ViewHolder();
            vh.tvTitle = view.findViewById(R.id.tv_map_search_title);
            view.setTag(vh);
        } else {
            view = convertView;
            vh = (ViewHolder) view.getTag();
        }
        YqcsDayRes.WhDetailsListBean data = getItem(position);
        vh.tvTitle.setText(data.getObjname());
        return view;
    }

    class ViewHolder {
        TextView tvTitle;
    }


    @Override
    public Filter getFilter() {
        if (mFilter == null) {
            mFilter = new ArrayFilter();
        }
        return mFilter;
    }


    private class ArrayFilter extends Filter {

        @Override
        protected FilterResults performFiltering(CharSequence prefix) {
            FilterResults results = new FilterResults();

            if (mUnfilteredData == null) {
                mUnfilteredData = new ArrayList<>(mList);
            }

            if (prefix == null || prefix.length() == 0) {
                ArrayList<YqcsDayRes.WhDetailsListBean> list = mUnfilteredData;
                results.values = list;
                results.count = list.size();
            } else {
                String prefixString = prefix.toString().toLowerCase();

                ArrayList<YqcsDayRes.WhDetailsListBean> unfilteredValues = mUnfilteredData;
                int count = unfilteredValues.size();

                ArrayList<YqcsDayRes.WhDetailsListBean> newValues = new ArrayList<>(count);

                for (int i = 0; i < count; i++) {
                    YqcsDayRes.WhDetailsListBean pc = unfilteredValues.get(i);
                    if (pc != null) {

                        if (pc.getObjname() != null && pc.getObjname().startsWith(prefixString)) {

                            newValues.add(pc);
                        }
                    }
                }

                results.values = newValues;
                results.count = newValues.size();
            }

            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint,
                                      FilterResults results) {
            //noinspection unchecked
            mList = (List<YqcsDayRes.WhDetailsListBean>) results.values;
            if (results.count > 0) {
                notifyDataSetChanged();
            } else {
                notifyDataSetInvalidated();
            }
        }

    }


}
