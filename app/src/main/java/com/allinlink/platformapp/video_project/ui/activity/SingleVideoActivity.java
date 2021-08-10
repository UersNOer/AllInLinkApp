 package com.allinlink.platformapp.video_project.ui.activity;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.shuyu.gsyvideoplayer.video.StandardGSYVideoPlayer;
import com.allinlink.platformapp.R;
import com.allinlink.platformapp.video_project.bean.CameraBean;
import com.allinlink.platformapp.video_project.bean.ChannelBean;
import com.allinlink.platformapp.video_project.bean.MapMarkerBean;
import com.allinlink.platformapp.video_project.config.Configs;
import com.allinlink.platformapp.video_project.contract.activity.SingleVideoContract;
import com.allinlink.platformapp.video_project.presenter.activity.SingleVideoActivityPresenter;
import com.allinlink.platformapp.video_project.widget.CustomManager;
import com.unistrong.view.base.BaseTitleActivity;
import com.unistrong.view.navigationbar.INavigationBarBackListener;
import com.unistrong.view.navigationbar.NavigationBar;
import com.unistrong.view.navigationbar.NavigationBuilder;

public class SingleVideoActivity extends BaseTitleActivity<SingleVideoActivityPresenter> implements SingleVideoContract.View, View.OnClickListener, View.OnTouchListener {
    private StandardGSYVideoPlayer gsyVideoPlayer;
    private String steamUrl;
    private ChannelBean clickLatlngData;
    private ImageView ivControlUp, ivControlDown, ivControlLeft, ivControlRight, ivZoomOut, ivZoomIn, ivControlStop;//云台控制按钮
        private TextView tvControlPreset;//云台控制按钮

        @Override
    protected NavigationBuilder onCreateNavigationBarByBuilder(NavigationBuilder builder) {
        NavigationBuilder navigationBuilder = builder.setTitle("实时视频")
                .setType(NavigationBar.BACK_AND_TITLE).setNavigationBarListener(new INavigationBarBackListener() {
                    @Override
                    public void onBackClick(    ) {
                        finish();
                    }   
                });
        return navigationBuilder;
    }

    @Override
    public View onCreateContentView() {
        View view = LayoutInflater.from(this).inflate(R.layout.activity_single_video, null);
        initView(view);
        setViewListener();
        return view;
    }

    private void initView(View view) {
        mPresenter = new SingleVideoActivityPresenter(this);
        steamUrl=getIntent().getStringExtra("url");
        clickLatlngData= (ChannelBean) getIntent().getSerializableExtra("data");
        gsyVideoPlayer = view.findViewById(R.id.detail_player);
        ivControlUp = view.findViewById(R.id.iv_control_up);
        ivControlDown = view.findViewById(R.id.iv_control_down);
        ivControlLeft = view.findViewById(R.id.iv_control_left);
        ivControlRight = view.findViewById(R.id.iv_control_right);
        ivZoomOut = view.findViewById(R.id.iv_zoom_out);
        ivZoomIn = view.findViewById(R.id.iv_zoom_in);
        tvControlPreset = view.findViewById(R.id.tv_control_preset);
        ivControlStop = view.findViewById(R.id.iv_control_stop);
//        gsyVideoPlayer.setUpLazy("http://domhttp.kksmg.com/2018/02/25/h264_720p_600k_37272-Knews24-20180225195600-3900-313957-600k_mp4.mp4", false, null, null, "这是title");
        gsyVideoPlayer.setUpLazy(clickLatlngData.getMediaAddress(), true, null, null, "");
        gsyVideoPlayer.getTitleTextView().setVisibility(View.GONE);
        gsyVideoPlayer.getBackButton().setVisibility(View.GONE);
        gsyVideoPlayer.getFullscreenButton().setVisibility(View.GONE);
    }

    public void setViewListener() {
        ivControlStop.setOnClickListener(this);
        tvControlPreset.setOnClickListener(this);

        ivZoomOut.setOnClickListener(this);
        ivZoomIn.setOnClickListener(this);

        ivControlUp.setOnClickListener(this);
        ivControlDown.setOnClickListener(this);
        ivControlLeft.setOnClickListener(this);
        ivControlRight.setOnClickListener(this);

        ivZoomOut.setOnTouchListener(this);
        ivZoomIn.setOnTouchListener(this);

        ivControlUp.setOnTouchListener(this);
        ivControlDown.setOnTouchListener(this);
        ivControlLeft.setOnTouchListener(this);
        ivControlRight.setOnTouchListener(this);

    }

    @Override
    protected void onDestroy() {
        gsyVideoPlayer.getCurrentPlayer().release();
        CustomManager.clearAllVideo();
        super.onDestroy();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
//            case R.id.iv_control_up:
//                mPresenter.cloundControl(clickLatlngData.getGid(), Configs.BST_PTZ_UP);
//                break;
//            case R.id.iv_control_right:
//                mPresenter.cloundControl(clickLatlngData.getGid(), Configs.BST_PTZ_RIGHT);
//                break;
//            case R.id.iv_control_down:
//                mPresenter.cloundControl(clickLatlngData.getGid(), Configs.BST_PTZ_DOWN);
//                break;
//            case R.id.iv_control_left:
//                mPresenter.cloundControl(clickLatlngData.getGid(), Configs.BST_PTZ_LEFT);
//                break;
//            case R.id.iv_zoom_out:
//                mPresenter.cloundControl(clickLatlngData.getGid(), Configs.BST_PTZ_ZOOM_OUT);
//                break;
//            case R.id.iv_zoom_in:
//                mPresenter.cloundControl(clickLatlngData.getGid(), Configs.BST_PTZ_ZOOM_IN);
//                break;
            case R.id.iv_control_stop:
//                mPresenter.cloundControl(clickLatlngData.getGid(), Configs.BST_PTZ_STOP);
                gsyVideoPlayer.startPlayLogic();
                break;
            case R.id.tv_control_preset:
                mPresenter.cloundControl(clickLatlngData.getGid(), Configs.BST_PTZ_GOTO_PRESET);
                break;
        }
    }

    @Override
    public void onError(String msg) {

    }

    @Override
    public Context getContext() {
        return null;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            switch (v.getId()) {
                case R.id.iv_control_up:
                    mPresenter.cloundControl(clickLatlngData.getGid(), Configs.BST_PTZ_UP);
                    break;
                case R.id.iv_control_right:
                    mPresenter.cloundControl(clickLatlngData.getGid(), Configs.BST_PTZ_RIGHT);
                    break;
                case R.id.iv_control_down:
                    mPresenter.cloundControl(clickLatlngData.getGid(), Configs.BST_PTZ_DOWN);
                    break;
                case R.id.iv_control_left:
                    mPresenter.cloundControl(clickLatlngData.getGid(), Configs.BST_PTZ_LEFT);
                    break;
                case R.id.iv_zoom_out:
                    mPresenter.cloundControl(clickLatlngData.getGid(), Configs.BST_PTZ_ZOOM_OUT);
                    break;
                case R.id.iv_zoom_in:
                    mPresenter.cloundControl(clickLatlngData.getGid(), Configs.BST_PTZ_ZOOM_IN);
                    break;
            }
        } else if (event.getAction() == MotionEvent.ACTION_UP) {
            mPresenter.stop(clickLatlngData.getGid());
        }
        return false;
    }
}