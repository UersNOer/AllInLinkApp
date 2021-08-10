package com.allinlink.platformapp.video_project.widget;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.allinlink.platformapp.R;


public class EditLinearLayout extends LinearLayout implements View.OnFocusChangeListener {
    EditText editText;
    ImageView ivDelete;
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
        this.msg = msg;
        initEdit();
    }

    public void setText(String text) {
        if(text==null){
            text="";
        }
        this.text = text;
    }

    public void initEdit() {
        for (int i = 0; i < getChildCount(); i++) {
            if (getChildAt(i) instanceof EditText) {
                editText = (EditText) getChildAt(i);
                editText.setSingleLine();
                editText.setText(text);
                break;
            }
        }
        for (int i = 0; i < getChildCount(); i++) {
            if (getChildAt(i) instanceof ImageView) {
                ivDelete = (ImageView) getChildAt(i);
                break;
            }
        }
        if (editText != null && ivDelete != null) {
            ivDelete.setAlpha(0);
            ivDelete.setImageResource(R.drawable.img_edit_clear);
            editText.setOnFocusChangeListener(this);

        }

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
