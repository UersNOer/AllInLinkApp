package com.allinlink.platformapp.video_project.ui.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.unistrong.utils.SharedPreferencesUtil;
import com.allinlink.platformapp.R;
import com.allinlink.platformapp.video_project.adapter.IpHostAdapter;
import com.allinlink.platformapp.video_project.config.Configs;
import com.allinlink.platformapp.video_project.utils.StringUtil;
import com.unistrong.view.EditTextWithDel;
import com.unistrong.view.RippleView;
import com.unistrong.view.base.BaseActivity;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;


/**
 * description: 设置ip和port页面
 *
 * @author ltd
 **/

public class IPSettingActivity extends BaseActivity implements RippleView.OnRippleCompleteListener {

    private EditTextWithDel mEtIp;
    private EditTextWithDel mEtPort;
    private RippleView mRvEnter;
    private TextView mHistoryTag;
    private RecyclerView mRecycleview;
    private IpHostAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ip_setting);
        initView();
        setCurrentData();
        showList();
    }

    private void initView() {
        Toolbar toolbar = findViewById(R.id.activity_main_toolbar);
        toolbar.setTitle(getResources().getString(R.string.setting));
        setSupportActionBar(toolbar);
        //设置是否有NvagitionIcon（返回图标）
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mEtIp = findViewById(R.id.et_ip);
        mEtPort = findViewById(R.id.et_port);
        mRvEnter = findViewById(R.id.rv_enter);
        mHistoryTag = findViewById(R.id.history_tag);
        mRecycleview = findViewById(R.id.recycleview);
        mRecycleview.setLayoutManager(new LinearLayoutManager(this));
        adapter = new IpHostAdapter(this);
        mRecycleview.setAdapter(adapter);
        mRvEnter.setOnRippleCompleteListener(this);

        adapter.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String[] ip_port = TextUtils.split(adapter.getItem(position), ",");
                mEtIp.setText(ip_port[0]);
                mEtPort.setText(ip_port[1]);
            }
        });
    }

    private void setCurrentData() {
        try {
            URL url = new URL(Configs.DOMAIN);
            boolean isHttps = url.toString().startsWith("https://");
            if (isHttps) {
                mEtIp.setText("https://" + url.getHost());
            } else {
                mEtIp.setText("http://" + url.getHost());
            }
            mEtPort.setText(url.getPort() + "");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    private void showList() {
        List<String> ipports = SharedPreferencesUtil.getInstance().getObject("IpPort", ArrayList.class);
        if (ipports != null) {
            mHistoryTag.setVisibility(View.VISIBLE);
            adapter.replaceList(ipports);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onComplete(RippleView rippleView) {
        if (rippleView.getId() == R.id.rv_enter) {
            String ip = mEtIp.getText().toString();
            String port = mEtPort.getText().toString();
            //判断是否有修改
            if (TextUtils.equals(ip + ":" + port + "/", Configs.DOMAIN)) {
                return;
            }

            //判断ip是否正确
            if (!StringUtil.isIP(ip)) {
                Toast.makeText(this, getResources().getString(R.string.ip_error), Toast.LENGTH_SHORT).show();
                return;
            }

            //判断端口是否正确
            if (port.length() != 4) {
                Toast.makeText(this, getResources().getString(R.string.port_error), Toast.LENGTH_SHORT).show();
                return;
            }

            //保存当前最新ip port
            SharedPreferencesUtil.getInstance().setObject("current_ipport", ip + "," + port);

            //保存老得ip port
            saveOld_IP_Port(ip, port);
            //刷新列表
            showList();
            //重新赋值domain
            Configs.DOMAIN = ip + ":" + port + "/";

            Toast.makeText(this, getResources().getString(R.string.setting_success), Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    /**
     * 保存老得ip port
     *
     * @param ip
     * @param port
     */
    private void saveOld_IP_Port(String ip, String port) {
        URL url = null;
        try {
            url = new URL(Configs.DOMAIN);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        boolean isHttps = url.toString().startsWith("https://");
        String old_ip;
        if (isHttps) {
            old_ip = "https://" + url.getHost();
        } else {
            old_ip = "http://" + url.getHost();
        }
        String old_port = url.getPort() + "";

        List<String> ipport = SharedPreferencesUtil.getInstance().getObject("IpPort", ArrayList.class);
        if (ipport == null) {
            List<String> list = new ArrayList<>();
            list.add(old_ip + "," + old_port);
            SharedPreferencesUtil.getInstance().setObject("IpPort", list);
        } else {
            for (int i = ipport.size() - 1; i >= 0; i--) {
                if (TextUtils.equals(ipport.get(i), ip + "," + port)) {
                    ipport.remove(i);
                }
            }
            ipport.add(0, old_ip + "," + old_port);
            SharedPreferencesUtil.getInstance().setObject("IpPort", ipport);
        }
    }
}
