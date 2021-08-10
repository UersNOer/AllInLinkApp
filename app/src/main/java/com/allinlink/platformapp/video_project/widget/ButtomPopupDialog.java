package com.allinlink.platformapp.video_project.widget;

import android.app.ActionBar;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.allinlink.platformapp.R;
import com.allinlink.platformapp.video_project.utils.StringUtils;
import com.unistrong.view.base.BaseActivity;
import com.unistrong.view.utils.ToastUtil;


/**
 * Created by Administrator on 2017/10/23.
 */

public class ButtomPopupDialog extends PopupWindow {
    WaringRelieveClick onClick;

    public ButtomPopupDialog( final Context ct, WaringRelieveClick onClick) {
        super(ct);
        this.onClick = onClick;
        /**
         * 打气泵.填充一个布局
         */
        LayoutInflater layoutInflater = (LayoutInflater) ct
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View pop_view = layoutInflater.inflate(R.layout.layout_dialog_bottom,
                null);
        final TextView tvNumber = (TextView) pop_view.findViewById(R.id.tv_number);
        final EditText edResaon = (EditText) pop_view.findViewById(R.id.ed_resaon);
        pop_view.findViewById(R.id.ll_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        pop_view.findViewById(R.id.tv_close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        pop_view.findViewById(R.id.tv_sure).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (StringUtils.txtCheckEmpty(edResaon.getText().toString().trim())) {
                    ToastUtil.showErrorToast("请输入解除警报原因");
                    return;
                }
                if (edResaon.getText().toString().trim().length() > 50) {
                    ToastUtil.showErrorToast("请输入解除警报原因");
                }
                ButtomPopupDialog.this.onClick.onclick( edResaon.getText().toString().trim());
                dismiss();
            }
        });


        edResaon.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().length() > 100) {
                    tvNumber.setTextColor(Color.RED);
                } else {
                    tvNumber.setTextColor(Color.parseColor("#353538"));
                }
                tvNumber.setText(s.toString().length() + "/100");
            }
        });


        this.setContentView(pop_view);
        /**
         * 设置宽和高
         */
        this.setWidth(ActionBar.LayoutParams.MATCH_PARENT);
        this.setHeight(ActionBar.LayoutParams.MATCH_PARENT);
        /**
         * 设置弹出的框体可点击
         */
        this.setFocusable(true);
        this.setOutsideTouchable(true);

        /**
         * 实例化一个背景色
         */
        ColorDrawable dw = new ColorDrawable(0x33000000);
        /**
         * 设置弹出框的背景色
         */
        this.setBackgroundDrawable(dw);
    }


    public interface WaringRelieveClick {
        void onclick( String resaon);
    }

}
