package com.allinlink.platformapp.video_project.widget;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Point;
import android.media.AudioManager;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.shuyu.gsyvideoplayer.GSYVideoManager;
import com.shuyu.gsyvideoplayer.listener.GSYStateUiListener;
import com.shuyu.gsyvideoplayer.listener.GSYVideoProgressListener;
import com.shuyu.gsyvideoplayer.model.VideoOptionModel;
import com.shuyu.gsyvideoplayer.player.IPlayerManager;
import com.shuyu.gsyvideoplayer.player.IjkPlayerManager;
import com.shuyu.gsyvideoplayer.player.PlayerFactory;
import com.shuyu.gsyvideoplayer.utils.Debuger;
import com.shuyu.gsyvideoplayer.video.StandardGSYVideoPlayer;
import com.shuyu.gsyvideoplayer.video.base.GSYBaseVideoPlayer;
import com.shuyu.gsyvideoplayer.video.base.GSYVideoViewBridge;
import com.unistrong.utils.RxBus;
import com.allinlink.platformapp.R;
import com.allinlink.platformapp.video_project.bean.ControlBean;
import com.allinlink.platformapp.video_project.config.Configs;

import java.util.ArrayList;
import java.util.List;

import tv.danmaku.ijk.media.player.IMediaPlayer;
import tv.danmaku.ijk.media.player.IjkMediaPlayer;


/**
 * 多个同时播放的播放控件
 * Created by guoshuyu on 2018/1/31.
 */

public class MultiSampleVideo extends StandardGSYVideoPlayer implements GSYStateUiListener {
    private ImageView ivControlUp, ivControlDown, ivControlLeft, ivControlRight, ivZoomOut, ivZoomIn;//云台控制按钮
    VideoControl videoControl;

    public void setVideoControl(VideoControl videoControl) {
        this.videoControl = videoControl;
    }

    private final static String TAG = "MultiSampleVideo";
    private String gid;
    ImageView mCoverImage;


    String mCoverOriginUrl;

    int mDefaultRes;

    public void setGid(String gid) {
        this.gid = gid;
    }

    /**
     * 强制全屏模式为横屏
     *
     * @param context
     * @param fullFlag
     */
    public MultiSampleVideo(Context context, Boolean fullFlag) {
        super(context, fullFlag);
        setRotateViewAuto(false);
        setLockLand(true);
        setRotateWithSystem(false);
    }

    public MultiSampleVideo(Context context) {
        super(context);
        setRotateViewAuto(false);
        setLockLand(true);
        setRotateWithSystem(false);
    }

    public MultiSampleVideo(Context context, AttributeSet attrs) {
        super(context, attrs);
        setRotateViewAuto(false);
        setLockLand(true);
        setRotateWithSystem(false);
    }

    @Override
    protected void init(Context context) {
        super.init(context);
//        seOpttList();

        setAutoFullWithSize(true);
        //实时播放隐藏进度条
        VideoControlView videoControlView = findViewById(R.id.view_contro);

        mProgressBar.setAlpha(0);
        mProgressBar.setOnSeekBarChangeListener(null);
        mProgressBar.setOnClickListener(null);
        mCurrentTimeTextView.setAlpha(0);
        mTotalTimeTextView.setAlpha(0);
        //获取父布局并设置背景为透明
        View parent = (View) mTotalTimeTextView.getParent();
        parent.setBackgroundColor(Color.TRANSPARENT);
        mCoverImage = (ImageView) findViewById(R.id.thumbImage);
        if (mThumbImageViewLayout != null &&
                (mCurrentState == -1 || mCurrentState == CURRENT_STATE_NORMAL || mCurrentState == CURRENT_STATE_ERROR)) {
            mThumbImageViewLayout.setVisibility(VISIBLE);
        }
        onAudioFocusChangeListener = new AudioManager.OnAudioFocusChangeListener() {
            @Override
            public void onAudioFocusChange(int focusChange) {
                switch (focusChange) {
                    case AudioManager.AUDIOFOCUS_GAIN:
                        break;
                    case AudioManager.AUDIOFOCUS_LOSS:
                        //todo 判断如果不是外界造成的就不处理
                        break;
                    case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT:
                        //todo 判断如果不是外界造成的就不处理
                        break;
                    case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK:
                        break;
                }
            }
        };

        ivControlUp = findViewById(R.id.iv_control_up);
        ivControlDown = findViewById(R.id.iv_control_down);
        ivControlLeft = findViewById(R.id.iv_control_left);
        ivControlRight = findViewById(R.id.iv_control_right);

        ivZoomOut = findViewById(R.id.iv_zoom_out);
        ivZoomIn = findViewById(R.id.iv_zoom_in);
        findViewById(R.id.tv_control_preset).setVisibility(GONE);
        findViewById(R.id.iv_control_stop).setVisibility(GONE);

        ivZoomOut.setOnClickListener(this);
        ivZoomIn.setOnClickListener(this);
        ivControlUp.setOnClickListener(this);
        ivControlDown.setOnClickListener(this);
        ivControlLeft.setOnClickListener(this);
        ivControlRight.setOnClickListener(this);
        //添加触摸事件，单独添加。不与默认事件冲突
        ivZoomOut.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                onTouchSendControl(v, event);
                return false;
            }
        });
        ivZoomIn.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                onTouchSendControl(v, event);

                return false;
            }
        });
        ivControlUp.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                onTouchSendControl(v, event);

                return false;
            }
        });
        ivControlDown.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                onTouchSendControl(v, event);
                return false;
            }
        });
        ivControlLeft.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                onTouchSendControl(v, event);

                return false;
            }
        });
        ivControlRight.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                onTouchSendControl(v, event);

                return false;
            }
        });

        //播放器大小改变，显示控制布局
        getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                boolean ifCurrentIsFullscreen = isIfCurrentIsFullscreen();
                MultiSampleVideo.super.setIsTouchWiget(false);
                MultiSampleVideo.super.setIsTouchWigetFull(false);
                videoControlView.setVisibility(ifCurrentIsFullscreen ? VISIBLE : GONE);
            }
        });
        super.setIsTouchWiget(false);
        super.setIsTouchWigetFull(false);
        setGSYStateUiListener(this);
    }


    @Override
    public void setGSYVideoProgressListener(GSYVideoProgressListener videoProgressListener) {
        super.setGSYVideoProgressListener(videoProgressListener);
    }

    /**
     * 统一发送控制命令
     *
     * @param v
     * @param event
     */
    private void onTouchSendControl(View v, MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            switch (v.getId()) {
                case R.id.iv_control_up:
                    RxBus.getInstance().send(new ControlBean(getPlayTag(), Configs.BST_PTZ_UP));
                    break;
                case R.id.iv_control_right:
                    RxBus.getInstance().send(new ControlBean(getPlayTag(), Configs.BST_PTZ_RIGHT));

                    break;
                case R.id.iv_control_down:
                    RxBus.getInstance().send(new ControlBean(getPlayTag(), Configs.BST_PTZ_DOWN));

                    break;
                case R.id.iv_control_left:
                    RxBus.getInstance().send(new ControlBean(getPlayTag(), Configs.BST_PTZ_LEFT));

                    break;
                case R.id.iv_zoom_out:
                    RxBus.getInstance().send(new ControlBean(getPlayTag(), Configs.BST_PTZ_ZOOM_OUT));
                    break;
                case R.id.iv_zoom_in:
                    RxBus.getInstance().send(new ControlBean(getPlayTag(), Configs.BST_PTZ_ZOOM_IN));
                    break;
            }
        } else if (event.getAction() == MotionEvent.ACTION_UP) {
            RxBus.getInstance().send(new ControlBean(getPlayTag(), "0"));
        }
    }

    @Override
    public void onStateChanged(int state) {
        Log.i(TAG, "--------------" + state);
        if (CURRENT_STATE_PLAYING_BUFFERING_START == state || CURRENT_STATE_AUTO_COMPLETE == state || CURRENT_STATE_ERROR == state) {
            Log.i(TAG, state + "重新播放视频");
            startPlayLogic();
        }
    }


    public interface VideoControl {
        void sendControl(String gid, String cmd);
    }


    @Override
    public GSYVideoViewBridge getGSYVideoManager() {
        CustomManager customManager = CustomManager.getCustomManager(getKey());
        customManager.initContext(getContext().getApplicationContext());
        List<VideoOptionModel> list = new ArrayList<>();
        // 支持硬解 1：开启 O:关闭
        list.add(new VideoOptionModel(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "mediacodec-hevc", 1));
        // 设置播放前的探测时间 1,达到首屏秒开效果
        list.add(new VideoOptionModel(IjkMediaPlayer.OPT_CATEGORY_FORMAT, "analyzeduration", 1));
        /**
         * 播放延时的解决方案
         */
        // 如果是rtsp协议，可以优先用tcp(默认是用udp)
        list.add(new VideoOptionModel(IjkMediaPlayer.OPT_CATEGORY_FORMAT, "rtsp_transport", "tcp"));
        // 设置播放前的最大探测时间 （100未测试是否是最佳值）
        list.add(new VideoOptionModel(IjkMediaPlayer.OPT_CATEGORY_FORMAT, "analyzemaxduration", 100));
        // 每处理一个packet之后刷新io上下文
        list.add(new VideoOptionModel(IjkMediaPlayer.OPT_CATEGORY_FORMAT, "flush_packets", 1));
        // 需要准备好后自动播放
        list.add(new VideoOptionModel(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "start-on-prepared", 1));
        // 不额外优化（使能非规范兼容优化，默认值0 ）
        list.add(new VideoOptionModel(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "fast", 1));
        // 是否开启预缓冲，一般直播项目会开启，达到秒开的效果，不过带来了播放丢帧卡顿的体验
        list.add(new VideoOptionModel(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "packet-buffering", 0));
        // 处理分辨率变化
        list.add(new VideoOptionModel(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "mediacodec-handle-resolution-change", 0));
        // 最大缓冲大小,单位kb
        list.add(new VideoOptionModel(IjkMediaPlayer.OPT_CATEGORY_FORMAT, "max-buffer-size", 500));
        // 默认最小帧数2
        list.add(new VideoOptionModel(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "min-frames", 2));
        // 最大缓存时长
        list.add(new VideoOptionModel(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "max_cached_duration", 10)); //300
        // 是否限制输入缓存数
        list.add(new VideoOptionModel(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "infbuf", 1));
        // 缩短播放的rtmp视频延迟在1s内
        list.add(new VideoOptionModel(IjkMediaPlayer.OPT_CATEGORY_FORMAT, "fflags", "nobuffer"));
        // 播放前的探测Size，默认是1M, 改小一点会出画面更快
        list.add(new VideoOptionModel(IjkMediaPlayer.OPT_CATEGORY_FORMAT, "probesize", 100)); //1024L)
        list.add(new VideoOptionModel(IjkMediaPlayer.OPT_CATEGORY_FORMAT, "buffer_size", 1500));
        // 播放重连次数
        list.add(new VideoOptionModel(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "reconnect", 1));
        // TODO:
        list.add(new VideoOptionModel(IjkMediaPlayer.OPT_CATEGORY_FORMAT, "http-detect-range-support", 0));
        // 设置是否开启环路过滤: 0开启，画面质量高，解码开销大，48关闭，画面质量差点，解码开销小
        list.add(new VideoOptionModel(IjkMediaPlayer.OPT_CATEGORY_CODEC, "skip_loop_filter", 48));
        // 跳过帧 ？？
        list.add(new VideoOptionModel(IjkMediaPlayer.OPT_CATEGORY_CODEC, "skip_frame", 0));
        // 视频帧处理不过来的时候丢弃一些帧达到同步的效果
        list.add(new VideoOptionModel(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "framedrop", 5));


        list.add(new VideoOptionModel(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "enable-accurate-seek", 1));
        /* 暂未使用
z
         */
        // 超时时间，timeout参数只对http设置有效，若果你用rtmp设置timeout，ijkplayer内部会忽略timeout参数。rtmp的timeout参数含义和http的不一样。
        list.add(new VideoOptionModel(IjkMediaPlayer.OPT_CATEGORY_FORMAT, "timeout", 3000));
        // 因为项目中多次调用播放器，有网络视频，resp，本地视频，还有wifi上http视频，所以得清空DNS才能播放WIFI上的视频
        //ijkMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_FORMAT, "dns_cache_clear", 1);
        list.add(new VideoOptionModel(IjkMediaPlayer.OPT_CATEGORY_FORMAT, "dns_cache_clear", 1));

        customManager.setOptionModelList(list);
        return customManager;
    }

    @Override
    protected boolean backFromFull(Context context) {
        return CustomManager.backFromWindowFull(context, getKey());
    }

    @Override
    protected void releaseVideos() {
        CustomManager.releaseAllVideos(getKey());
    }


    @Override
    protected int getFullId() {
        return CustomManager.FULLSCREEN_ID;
    }

    @Override
    protected int getSmallId() {
        return CustomManager.SMALL_ID;
    }


    @Override
    public int getLayoutId() {
        return R.layout.video_layout_cover;
    }

    public void loadCoverImage(String url, int res) {
        mCoverOriginUrl = url;
        mDefaultRes = res;
        Glide.with(getContext().getApplicationContext())
                .setDefaultRequestOptions(
                        new RequestOptions()
                                .frame(1000000)
                                .centerCrop()
                                .error(res)
                                .placeholder(res))
                .load(url)
                .into(mCoverImage);

    }

    @Override
    public GSYBaseVideoPlayer startWindowFullscreen(Context context, boolean actionBar, boolean statusBar) {

        GSYBaseVideoPlayer gsyBaseVideoPlayer = super.startWindowFullscreen(context, actionBar, statusBar);

        MultiSampleVideo multiSampleVideo = (MultiSampleVideo) gsyBaseVideoPlayer;

        multiSampleVideo.loadCoverImage(mCoverOriginUrl, mDefaultRes);

        return multiSampleVideo;
    }


    @Override
    public GSYBaseVideoPlayer showSmallVideo(Point size, boolean actionBar, boolean statusBar) {
        //下面这里替换成你自己的强制转化
        MultiSampleVideo multiSampleVideo = (MultiSampleVideo) super.showSmallVideo(size, actionBar, statusBar);
        multiSampleVideo.mStartButton.setVisibility(GONE);
        multiSampleVideo.mStartButton = null;
        return multiSampleVideo;
    }

    public String getKey() {
        if (mPlayPosition == -22) {
            Debuger.printfError(getClass().getSimpleName() + " used getKey() " + "******* PlayPosition never set. ********");
        }
        if (TextUtils.isEmpty(mPlayTag)) {
            Debuger.printfError(getClass().getSimpleName() + " used getKey() " + "******* PlayTag never set. ********");
        }
        return TAG + mPlayPosition + mPlayTag;
    }


}