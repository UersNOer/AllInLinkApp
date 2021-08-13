package com.example.android_supervisor.ui;

import android.os.Bundle;
import android.widget.TextView;

import com.example.android_supervisor.R;

/**
 * @author wujie
 */
public class TutorialSubActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tutorial_sub);
        TextView textView = findViewById(android.R.id.text1);
        int textId = getIntent().getIntExtra("textId", 0);
        textView.setText(textId);
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
    }
}
