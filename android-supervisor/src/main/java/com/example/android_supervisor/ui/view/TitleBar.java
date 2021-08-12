package com.example.android_supervisor.ui.view;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.android_supervisor.R;
import com.example.android_supervisor.utils.SystemUtils;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author wujie
 */
public class TitleBar extends LinearLayout {
    @BindView(R.id.tv_title_bar_title)
    TextView tvTitle;

    @BindView(R.id.iv_title_bar_nav)
    ImageView ivNav;

    @BindView(R.id.ll_title_bar_menu)
    LinearLayout llMenuContainer;

    public TitleBar(Context context) {
        this(context, null);
    }

    public TitleBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.title_bar, this, true);
        ButterKnife.bind(this, view);

        setNavigationClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Context context = getContext();
                if (context instanceof Activity) {
                    SystemUtils.hideSoftInput(context, v);
                    ((Activity) context).onBackPressed();
//                    ((Activity) context).finish();
                }
            }
        });
    }

    public void setTitle(CharSequence text) {
        tvTitle.setText(text);
    }

    public void setNavigation(Drawable drawable) {
        ivNav.setImageDrawable(drawable);
    }

    public void setNavigationClickListener(OnClickListener listener) {
        ivNav.setOnClickListener(listener);
    }

    public View addMenu(CharSequence text) {
        return addMenu(text, null);
    }

    public View addMenu(CharSequence text, OnClickListener listener) {
        LayoutInflater layoutInflater = LayoutInflater.from(getContext());
        View view = layoutInflater.inflate(R.layout.menu_item, llMenuContainer, false);
        TextView textView = view.findViewById(R.id.menu);
        textView.setText(text);
        textView.setOnClickListener(listener);
        llMenuContainer.addView(textView);
        return textView;
    }

    public void addMenu(View view) {
        llMenuContainer.addView(view);
    }
}
