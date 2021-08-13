package com.example.android_supervisor.ui.media;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.viewpager.widget.ViewPager;

import android.util.TypedValue;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.example.android_supervisor.R;
import com.example.android_supervisor.ui.view.CirclePageIndicator;

/**
 * @author wujie
 */
public class ImagePreviewActivity extends Activity {

    @SuppressLint("StaticFieldLeak")
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            View decorView = getWindow().getDecorView();
            decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            getWindow().setStatusBarColor(0x00000000);
        }

        setContentView(R.layout.activity_image_preview);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        Intent intent = getIntent();
        final int current = intent.getIntExtra("current", 0);
        final String[] urls = intent.getStringArrayExtra("urls");

        if (urls == null || urls.length == 0) {
            Toast.makeText(getApplicationContext(), "请至少传入一张图片", Toast.LENGTH_SHORT).show();
            return;
        }

        ImagePreviewAdapter adapter = new ImagePreviewAdapter(this);
        adapter.addAll(urls);

        ViewPager viewPager = findViewById(R.id.view_pager);
        viewPager.setAdapter(adapter);
        viewPager.setCurrentItem(current);

        CirclePageIndicator indicator = findViewById(R.id.pager_indicator);
        indicator.setPageColor(0xff808080);
        indicator.setFillColor(0xffffffff);
        indicator.setCentered(true);
        indicator.setRadius(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                3f, getResources().getDisplayMetrics()));
        indicator.setViewPager(viewPager, current);

        if (urls.length == 1) {
            indicator.setVisibility(View.INVISIBLE);
        }
    }
}
