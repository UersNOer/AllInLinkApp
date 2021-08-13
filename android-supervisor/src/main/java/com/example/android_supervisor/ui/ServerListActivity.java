package com.example.android_supervisor.ui;

import android.content.Context;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.RecyclerView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cncoderx.recyclerviewhelper.adapter.ObjectAdapter;
import com.example.android_supervisor.R;
import com.example.android_supervisor.config.AppConfig;
import com.example.android_supervisor.config.ServiceConfig;

/**
 * @author wujie
 */
public class ServerListActivity extends ListActivity {
    private ServerAdapter adapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        adapter = new ServerAdapter();
        setAdapter(adapter);

        setEnableRefresh(false);
        setEnableLoadMore(false);

        adapter.addAll(AppConfig.getLocalServiceConfigs());
    }

    @Override
    protected boolean isEnableAppLock() {
        return false;
    }

    @Override
    public RecyclerView.ItemDecoration getItemDecoration(Context context) {
        DividerItemDecoration itemDecoration = new DividerItemDecoration(context, LinearLayout.VERTICAL);
        itemDecoration.setDrawable(getResources().getDrawable(R.drawable.divider_space));
        return itemDecoration;
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
    }

    class ServerAdapter extends ObjectAdapter<ServiceConfig> {

        public ServerAdapter() {
            super(R.layout.item_server_setting);
        }

        @Override
        public void onBindViewHolder(BaseViewHolder holder, ServiceConfig object, int position) {
            TextView tvName = holder.getView(R.id.tv_server_setting_http_name, TextView.class);
            tvName.setText(object.getName());

            TextView tvHost = holder.getView(R.id.tv_server_setting_http, TextView.class);
            tvHost.setText(object.getDomain());
        }
    }
}
