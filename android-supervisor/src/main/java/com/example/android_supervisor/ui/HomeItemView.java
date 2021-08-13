package com.example.android_supervisor.ui;

import android.content.Context;
import android.graphics.PorterDuff;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.android_supervisor.R;
import com.example.android_supervisor.utils.MainIcon;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author yangjie
 */

public class HomeItemView extends LinearLayout {

    @BindView(R.id.iv_home_menu)
    AppCompatImageView iv_home_menu;

    @BindView(R.id.tv_home_menu)
    TextView tv_home_menu;


    private String httpurl;
    private MainIcon.GridTag tag;

    private Class activityLauncher;


    public HomeItemView(Context context) {
        this(context,null);
    }

    public HomeItemView(Context context, @Nullable AttributeSet attrs) {
        this(context,null,0);
    }

    public HomeItemView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);

    }

    private void initView(Context context) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.home_item_menu, this, true);
        ButterKnife.bind(this, view);
    }


    public void setGridImg(int resId)
    {
        if(resId == 0)
        {
            iv_home_menu.setVisibility(View.GONE);
            iv_home_menu.setVisibility(View.VISIBLE);
        }
        else
        {
            iv_home_menu.setImageResource(resId);
            this.setId(resId);
        }
    }




    public void setGridTitle(CharSequence title)
    {
        tv_home_menu.setText(title);
    }

    public void setHttpUrl(String url)
    {
        httpurl = url;
    }

    public void setTags(MainIcon.GridTag tags)
    {
        tag = tags;
    }

    public String getName()
    {
        return tv_home_menu.getText().toString();
    }

    public MainIcon.GridTag getTags()
    {
        return tag;
    }

    public String getHttpUrl()
    {
        return httpurl;
    }

    public void setLauncher(Class activityLauncher) {
        this.activityLauncher = activityLauncher;
    }

    public Class getLauncher() {
        return activityLauncher;
    }

    public void setBgTint(int parseColor) {

        iv_home_menu.getBackground().setColorFilter(parseColor, PorterDuff.Mode.SRC_ATOP);

    }

//    public void setGridCount(int count)
//    {
//        if(count > 0)
//        {
//            gridCount.setText(String.valueOf(count));
//            gridCount.setVisibility(View.VISIBLE);
//        }
//        else
//        {
//            gridCount.setVisibility(View.GONE);
//        }
//    }


}
