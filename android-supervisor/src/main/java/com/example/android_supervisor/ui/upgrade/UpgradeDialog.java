package com.example.android_supervisor.ui.upgrade;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;

import android.text.format.Formatter;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.example.android_supervisor.R;
import com.tencent.bugly.beta.Beta;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author wujie
 */
public class UpgradeDialog extends DialogFragment {
    private UpgradePecket mUpgradePecket;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Bundle bundle = getArguments();
        if (bundle != null) {
            mUpgradePecket = bundle.getParcelable(Constants.PECKET);
        }
        Context context = getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_upgrade, null);
        TextView tvVersion = view.findViewById(R.id.tv_upgrade_version);
        TextView tvSize = view.findViewById(R.id.tv_upgrade_packet_size);
        TextView tvUpdate = view.findViewById(R.id.tv_upgrade_update);
        TextView tvFeature = view.findViewById(R.id.tv_upgrade_packet_feature);
        tvVersion.setText(mUpgradePecket.getVersionName());
        tvSize.setText(Formatter.formatShortFileSize(context, mUpgradePecket.getFileSize()));
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        tvUpdate.setText(dateFormat.format(new Date(mUpgradePecket.getPublishTime())));
        tvFeature.setText(mUpgradePecket.getNewFeature());
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context);
        dialogBuilder.setTitle(com.tencent.bugly.beta.R.string.strNotificationHaveNewVersion);
        dialogBuilder.setView(view);
        dialogBuilder.setPositiveButton(com.tencent.bugly.beta.R.string.strUpgradeDialogUpgradeBtn,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Beta.startDownload();
                    }
                });
        if (mUpgradePecket.getUpgradeType() == 1) { // 非强制更新
            dialogBuilder.setNegativeButton(com.tencent.bugly.beta.R.string.strUpgradeDialogCancelBtn, null);
        }
        dialogBuilder.setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                return keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0;
            }
        });
        AlertDialog dialog = dialogBuilder.create();
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        return dialog;
    }

    public static void show(FragmentManager fm, UpgradePecket pecket) {
        UpgradeDialog dialog = new UpgradeDialog();
        Bundle bundle = new Bundle();
        bundle.putParcelable(Constants.PECKET, pecket);
        dialog.setArguments(bundle);
        dialog.show(fm, "");
    }
}