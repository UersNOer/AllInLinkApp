package com.allinlink.platformapp.video_project.widget;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.TranslateAnimation;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.shuyu.gsyvideoplayer.GSYVideoManager;
import com.shuyu.gsyvideoplayer.video.StandardGSYVideoPlayer;
import com.allinlink.platformapp.R;
import com.allinlink.platformapp.video_project.bean.CameraBean;
import com.allinlink.platformapp.video_project.bean.ChannelBean;
import com.allinlink.platformapp.video_project.bean.MapMarkerBean;
import com.allinlink.platformapp.video_project.ui.activity.SingleVideoActivity;


public class MapDialogLinerLayout extends LinearLayout implements CompoundButton.OnCheckedChangeListener {
    private boolean ignoreNet;
    MultiSampleVideo gsyVideoPlayer;
    TextView tvName, tvLocation, tvLatlng;
    private int dWidth;
    private int dHeight;
    public String steamUrl;
    public static boolean isShow = false;
    CheckBox cbCollect;
    private ChannelBean clickLatlngData;
    private DialogOnClickListance listance;

    public void setListance(DialogOnClickListance listance) {
        this.listance = listance;
    }

    public MapDialogLinerLayout(Context context) {
        super(context);
    }

    public MapDialogLinerLayout(Context context, AttributeSet attrs) {
        super(context, attrs, 0);
        LayoutInflater.from(context).inflate(R.layout.linear_map_player, this);
    }

    public MapDialogLinerLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        LayoutInflater.from(context).inflate(R.layout.linear_map_player, this);
    }

    public void initView() {
        findViewById(R.id.iv_video_full).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), SingleVideoActivity.class);
                intent.putExtra("data", clickLatlngData);
                intent.putExtra("url", clickLatlngData.getMediaAddress());
                getContext().startActivity(intent);
            }
        });
        gsyVideoPlayer = findViewById(R.id.detail_player);
        cbCollect = findViewById(R.id.cb_collect);
        cbCollect.setOnCheckedChangeListener(this);
        tvLocation = findViewById(R.id.tv_location);
        tvLatlng = findViewById(R.id.tv_latlng);
        tvName = findViewById(R.id.tv_name);
    }


    private void startPlay() {
        if (!isShow) {
            TranslateAnimation translateAnimation = new TranslateAnimation(0, 0, 1990, 0);
            translateAnimation.setDuration(1000);
            this.startAnimation(translateAnimation);
            isShow = true;
        }
        cbCollect.setChecked(clickLatlngData.getFavoriteFlag() == 1);
        tvName.setText(clickLatlngData.getChannelName());
        tvLocation.setText(clickLatlngData.getCameraGroup());
        tvLatlng.setText("经度:"+clickLatlngData.getJd()+",纬度:"+clickLatlngData.getWd());
        if (TextUtils.isEmpty(clickLatlngData.getMediaAddress())) {
            gsyVideoPlayer.setVisibility(GONE);
            findViewById(R.id.iv_video_full).setVisibility(GONE);
        } else {
            gsyVideoPlayer.setUpLazy(clickLatlngData.getMediaAddress(), true, null, null, clickLatlngData.getChannelName());
//增加title
            gsyVideoPlayer.getTitleTextView().setVisibility(View.GONE);
//设置返回键
            gsyVideoPlayer.getBackButton().setVisibility(View.GONE);
//设置全屏按键功能
            gsyVideoPlayer.getFullscreenButton().setVisibility(GONE);
            gsyVideoPlayer.setVisibility(VISIBLE);
            findViewById(R.id.iv_video_full).setVisibility(VISIBLE);
        }
    }


    public void onPause() {
        gsyVideoPlayer.getCurrentPlayer().release();
        CustomManager.clearAllVideo();
    }

    public void setViewVisibility(int type) {
        if (isShow) {
            TranslateAnimation translateAnimation = new TranslateAnimation(0, 0, 0, 1990);
            translateAnimation.setDuration(1000);
            this.startAnimation(translateAnimation);
            onPause();
            isShow = false;
        }
        this.setVisibility(type);
    }

    public void setIsShow(boolean isShow) {
        MapDialogLinerLayout.isShow = isShow;
    }

    public void setSteamUrl(ChannelBean clickLatlngData) {
        this.clickLatlngData = clickLatlngData;
        this.cbCollect.setChecked(false);
        startPlay();
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        listance.collectClick(isChecked, clickLatlngData);
    }

    public interface DialogOnClickListance {
        void collectClick(boolean collect, ChannelBean data);
    }
}
