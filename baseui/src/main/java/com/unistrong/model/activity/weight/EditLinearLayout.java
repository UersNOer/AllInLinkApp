package com.unistrong.model.activity.weight;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.unistrong.model.activity.baseui.R;
import com.unistrong.uni_utils.MyUtils;

/*
 * 带删除按钮的输入框
 * */
public class EditLinearLayout extends LinearLayout implements View.OnFocusChangeListener {
    EditText editText;
    ImageView ivDelete;
    TextView tvDesc;
    private String msg = "";
    private String text = "";

    public EditLinearLayout(Context context) {
        super(context);
    }

    public EditLinearLayout(Context context, AttributeSet set) {
        super(context, set);
    }

    public String getText() {
        if (editText != null) {
            return editText.getText().toString().trim();

        }
        return "";
    }

    public String getMsg() {
        return msg;
    }

    public boolean checkEmpty() {
        return TextUtils.isEmpty(getText());
    }

    public void initEdit(String msg) {
        this.text = msg;
        initEdit();
    }

    public void setText(String text) {
        if (text == null) {
            text = "";
        }
        this.text = text;
    }

    public String getToast() {
        return "请输入" + tvDesc.getText().toString().trim();
    }

    public void initEdit() {
        for (int i = 0; i < getChildCount(); i++) {
            if (getChildAt(i) instanceof EditText) {
                editText = (EditText) getChildAt(i);
                editText.setSingleLine();
                editText.setText(text);

                if (MyUtils.txtCheckEmpty(editText.getHint().toString())) {
                    editText.setHint("请输入...");
                }
                break;
            }
        }
        for (int i = 0; i < getChildCount(); i++) {
            if (getChildAt(i) instanceof ImageView) {
                ivDelete = (ImageView) getChildAt(i);
                break;
            }
        }

        for (int i = 0; i < getChildCount(); i++) {
            if (getChildAt(i) instanceof TextView) {
                tvDesc = (TextView) getChildAt(i);
                break;
            }
        }


        if (editText != null && ivDelete != null) {
            ivDelete.setAlpha(0);
            ivDelete.setImageResource(R.mipmap.img_edit_clear);
            editText.setOnFocusChangeListener(this);

        }
    }

    public EditText getEditText() {
        return editText;
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        ivDelete.setImageAlpha(hasFocus ? 255 : 0);

        ivDelete.setOnClickListener(!hasFocus ? null : new OnClickListener() {
            @Override
            public void onClick(View v) {
                editText.setText("");
            }
        });
    }

}
