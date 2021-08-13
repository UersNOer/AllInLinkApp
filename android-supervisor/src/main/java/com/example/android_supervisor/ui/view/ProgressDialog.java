package com.example.android_supervisor.ui.view;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import androidx.annotation.NonNull;
import android.view.KeyEvent;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.android_supervisor.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author wujie
 */
public class ProgressDialog extends Dialog {
    @BindView(R.id.progress)
    ProgressBar mProgressBar;

    @BindView(R.id.text)
    TextView mTextView;

    public ProgressDialog(@NonNull Context context) {
        super(context, R.style.ProgressDialogStyle);
        setContentView(R.layout.progress_dialog);
        ButterKnife.bind(this);
        setCancelable(false);
        setCanceledOnTouchOutside(false);
        setOnKeyListener(new OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                return keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0;
            }
        });
    }

    public void setText(ProgressText text) {
        mTextView.setText(text.toString());
    }

    public void setText(String text) {
        mTextView.setText(text);
    }

    public static ProgressDialog show(Context context, ProgressText text) {
        ProgressDialog dialog = new ProgressDialog(context);
        dialog.setText(text);
        dialog.show();
        return dialog;
    }

    public static ProgressDialog show(Context context, String text) {
        ProgressDialog dialog = new ProgressDialog(context);
        dialog.setText(text);
        dialog.show();
        return dialog;
    }
}
