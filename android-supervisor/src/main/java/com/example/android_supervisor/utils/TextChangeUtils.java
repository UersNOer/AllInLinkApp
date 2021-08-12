package com.example.android_supervisor.utils;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by yk on 2019/9/9.
 */
public class TextChangeUtils implements TextWatcher {
    private EditText editText;

    private TextView tv_character;

    private Context context;

    public TextChangeUtils(EditText editText,TextView tv_character,Context context) {
        this.editText  = editText;
        this.tv_character = tv_character;
        this.context = context;
        editText.addTextChangedListener(this);
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        tv_character.setText(String.valueOf(s.length()) + "-200");
        if(s.length()>=200){
            Toast.makeText(context, "字数已经超过上限", Toast.LENGTH_SHORT).show();
        }

    }
}
