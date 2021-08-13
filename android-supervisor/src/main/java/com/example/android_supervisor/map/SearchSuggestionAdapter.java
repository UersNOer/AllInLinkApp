package com.example.android_supervisor.map;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.tencent.lbssearch.TencentSearch;
import com.tencent.lbssearch.a.b.d;
import com.tencent.lbssearch.httpresponse.BaseObject;
import com.tencent.lbssearch.httpresponse.HttpResponseListener;
import com.tencent.lbssearch.object.param.SuggestionParam;
import com.tencent.lbssearch.object.result.SuggestionResultObject;
import com.example.android_supervisor.R;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

/**
 * @author wujie
 */
public class SearchSuggestionAdapter extends BaseAdapter implements Filterable {
    private Context context;
    private Filter mFilter;
    private String region;
    private List<SuggestionData> objects = new ArrayList<>();

    public SearchSuggestionAdapter(Context context) {
        this.context = context;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    @Override
    public int getCount() {
        return objects.size();
    }

    @Override
    public SuggestionData getItem(int position) {
        return objects.get(position);
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
            view = LayoutInflater.from(context).inflate(R.layout.item_map_search_suggestion, parent, false);
            vh = new ViewHolder();
            vh.tvTitle = view.findViewById(R.id.tv_map_search_title);
            vh.tvAddress = view.findViewById(R.id.tv_map_search_address);
            view.setTag(vh);
        } else {
            view = convertView;
            vh = (ViewHolder) view.getTag();
        }
        SuggestionData data = getItem(position);
        vh.tvTitle.setText(data.title);
        vh.tvAddress.setText(data.address);
        return view;
    }

    class ViewHolder {
        TextView tvTitle;
        TextView tvAddress;
    }

    @Override
    public final Filter getFilter() {
        if (mFilter == null) {
            mFilter = new SearchSuggestionFilter(context);
        }
        return mFilter;
    }

    private class SearchSuggestionFilter extends Filter {
        private Context context;

        SearchSuggestionFilter(Context context) {
            this.context = context;
        }

        @Override
        protected FilterResults performFiltering(CharSequence prefix) {
            final FilterResults results = new FilterResults();

            final ArrayList<String> list = new ArrayList<>();
            if (TextUtils.isEmpty(prefix)) {
                results.values = list;
                results.count = list.size();
            } else {
                final CountDownLatch countDownLatch = new CountDownLatch(1);

                SuggestionParam param = new SuggestionParam();
                param.keyword(prefix.toString());
                param.region(region);
                new TencentSearch(context).suggestion(param, new HttpResponseListener() {

                    @Override
                    public void onSuccess(int i, BaseObject baseObj) {
                        SuggestionResultObject resultObj = (SuggestionResultObject) baseObj;
                        results.count = resultObj.count;
                        results.values = getSuggestionDataList(resultObj);
                        countDownLatch.countDown();
                    }

                    @Override
                    public void onFailure(int i, String s, Throwable throwable) {
                        results.values = list;
                        results.count = list.size();
                        countDownLatch.countDown();
                    }
                });
                try {
                    countDownLatch.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            //noinspection unchecked
            objects = (List<SuggestionData>) results.values;
            if (results.count > 0) {
                notifyDataSetChanged();
            } else {
                notifyDataSetInvalidated();
            }
        }

        private List<SuggestionData> getSuggestionDataList(SuggestionResultObject object) {
            List<SuggestionData> result = new ArrayList<>();
            for (SuggestionResultObject.SuggestionData data : object.data) {
                SuggestionData data2 = new SuggestionData();
                data2.id = data.id;
                data2.title = data.title;
                data2.province = data.province;
                data2.city = data.city;
                data2.adcode = data.adcode;
                data2.district = data.district;
                data2.type = data.type;
                data2.latitude = data.location.lat;
                data2.longitude = data.location.lng;
                data2.address = data.address;
                result.add(data2);
            }
            return result;
        }
    }

    class SuggestionParam2 extends SuggestionParam {

        @Override
        public d buildParameters() {
            d params = super.buildParameters();
            params.a("page_index", "1");
            params.a("page_size", "25");
            return params;
        }
    }
}
