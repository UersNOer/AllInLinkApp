package com.example.android_supervisor.ui.upgrade;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import android.text.format.Formatter;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.tencent.bugly.beta.Beta;
import com.example.android_supervisor.R;
import com.example.android_supervisor.utils.DateUtils;

import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @author wujie
 */
public class UpgradeDialogActivity extends Activity {
    @BindView(R.id.tv_upgrade_version)
    TextView tvVersion;

    @BindView(R.id.tv_upgrade_packet_size)
    TextView tvSize;

    @BindView(R.id.tv_upgrade_update)
    TextView tvUpdate;

    @BindView(R.id.tv_upgrade_packet_feature)
    TextView tvFeature;

    @BindView(R.id.btn_upgrade_cancel)
    Button btnCancel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_upgrade);
        ButterKnife.bind(this);

        initialUpgradeInfo(getIntent());
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        initialUpgradeInfo(intent);
    }

    private void initialUpgradeInfo(Intent intent) {
        UpgradePecket pecket = intent.getParcelableExtra(Constants.PECKET);
        if (pecket != null) {
            tvVersion.setText(pecket.getVersionName());
            tvSize.setText(Formatter.formatShortFileSize(this, pecket.getFileSize()));
            tvUpdate.setText(DateUtils.format(new Date(pecket.getPublishTime())));
            tvFeature.setText(pecket.getNewFeature());
            if (pecket.getUpgradeType() == 2) {
                btnCancel.setVisibility(View.GONE);
            } else {
                btnCancel.setVisibility(View.VISIBLE);
            }
        }
    }

    @OnClick(R.id.btn_upgrade_cancel)
    public void onCancel(View v) {
        this.finish();
    }

    @OnClick(R.id.btn_upgrade_ok)
    public void onOk(View v) {
        this.finish();
        Beta.startDownload();
        Toast.makeText(this, "开始下载新版本..", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onBackPressed() {
//        super.onBackPressed();
    }
}
