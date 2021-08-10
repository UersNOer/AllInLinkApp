package com.allinlink.platformapp.video_project.ui.frament;

import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;

import androidx.annotation.Nullable;

import com.shuyu.gsyvideoplayer.video.StandardGSYVideoPlayer;
import com.allinlink.platformapp.R;
import com.allinlink.platformapp.video_project.bean.ChannelBean;
import com.allinlink.platformapp.video_project.bean.PlayBackBean;
import com.allinlink.platformapp.video_project.contract.fragment.DataFragmentContract;
import com.allinlink.platformapp.video_project.ui.activity.SerachVideoActivity;
import com.allinlink.platformapp.video_project.presenter.fragment.DataFragmentPresenter;
import com.allinlink.platformapp.video_project.ui.activity.SearchCameraActivity;
import com.allinlink.platformapp.video_project.utils.LogUtil;
import com.allinlink.platformapp.video_project.utils.StringUtil;
import com.allinlink.platformapp.video_project.utils.UserCache;
import com.allinlink.platformapp.video_project.widget.CustomManager;
import com.allinlink.platformapp.video_project.widget.MultiSampleVideo;
import com.allinlink.platformapp.video_project.widget.MySeekBar;
import com.unistrong.view.base.BaseFragment;

public class DataFragment extends BaseFragment<DataFragmentPresenter> implements DataFragmentContract.View {

    private RelativeLayout ivAdd;
    //    MultiSampleVideo gsyVideoPlayer;
    WebView wvLoad;
    MySeekBar mSeekBar;
    RelativeLayout rlVideo;
    boolean isTouchSeeked = false;
    private static final int PROGRESS_SEEKTO = 1008;
    private long startTime, endTime;
    private boolean flag = false;

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        if (hidden) {
            String format = String.format("javascript:pause()");
            wvLoad.loadUrl(format);
        } else {
            if (flag) {
                String format = String.format("javascript:playContinue()");
                wvLoad.loadUrl(format);
            }
        }
    }

    @Override
    protected View onCreateContentView() {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_data, null);
        initView(view);
        return view;
    }

    private void initView(View view) {
        mPresenter = new DataFragmentPresenter(this);
        view.findViewById(R.id.iv_serach).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), SerachVideoActivity.class));
            }
        });
        rlVideo = view.findViewById(R.id.rl_video);
        ivAdd = view.findViewById(R.id.iv_add);
        wvLoad = view.findViewById(R.id.wv_load);
        initWebView();
        ivAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), SearchCameraActivity.class);
                intent.putExtra("type", 1);
                getActivity().startActivity(intent);
            }
        });

        mSeekBar = view.findViewById(R.id.seek_progress);
        mSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    if (isTouchSeeked) {
                        mSeekBar.showSeekDialog(makeTimeString(DataFragment.this.startTime + progress));//动态展示当前播放时间
                    } else {
                        mSeekBar.hideSeekDialog();
                    }
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                isTouchSeeked = true;

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                mSeekBar.hideSeekDialog();
                isTouchSeeked = false;
                String format = String.format("javascript:getCurrentTime('" + (startTime + seekBar.getProgress()) + "')");
                Log.i("TAG", format);
                wvLoad.loadUrl(format);
//                1606791113000
//                160679111300014411346

            }
        });
        Message handmsg = Message.obtain();
        handmsg.what = PROGRESS_SEEKTO;
    }


    private void initWebView() {
        wvLoad.loadUrl("file:///android_asset/videohtml/index.html");
        WebSettings webSettings = wvLoad.getSettings();
        webSettings.setJavaScriptEnabled(true);
        wvLoad.addJavascriptInterface(this, "android");
        wvLoad.setWebChromeClient(new WebChromeClient());


    }

    @Override
    public void setHistotyVideo(PlayBackBean bean) {

        ivAdd.setVisibility(View.GONE);
        rlVideo.setVisibility(View.VISIBLE);
        PlayBackBean.ItemsBean itemsBean = bean.getItems().get(0);
        PlayBackBean.ItemsBean.ServersBean serversBean = itemsBean.getServers().get(0);
        String format = String.format("javascript:getTime('%s','%s','%s','%s','%s')", itemsBean.getChannelId(), itemsBean.getStartTime(), itemsBean.getEndTime(), serversBean.getServerIp(), serversBean.getServerPort());
//        String format = String.format("javascript:getTime('%s','%s','%s','%s','%s')", "2020121114031200016-0000", "2020-12-12 12:06:24", "2020-12-13 12:06:24", "10.16.67.106", "10090");
//                                                                   javascript:getTime('2020121114020100007-0000','2020-11-14 09:25:57','2020-12-23 09:26:02','10.16.67.106','10090')                                                               javascript:getTime('2020121114020100007-0000', '2020-11-27 09:07:42', '2020-12-23 09:07:45', '10.16.67.106', '10090')
        Log.i("TAG",format);
        wvLoad.loadUrl(format);
    }

    @Override
    public void onError(String msg) {

    }

    @JavascriptInterface
    public void setMessage(String obj) {
    }

    @JavascriptInterface
    public void setProgress(long stsrtTime, long endTime) {
        flag = true;
        this.startTime = stsrtTime;
        this.endTime = endTime;
        mSeekBar.setMax((int) (this.endTime - this.startTime));
        mSeekBar.setProgress(0);
    }

    @JavascriptInterface
    public void setCurrTime(long currTime) {
        if (!isTouchSeeked) {
            mSeekBar.setProgress((int) (currTime - this.startTime));
        }
    }

    /**
     * 转换进度值为时间
     *
     * @param secs
     * @return
     */
    private String makeTimeString(long secs) {
        /**
         * %[argument_index$][flags][width]conversion 可选的
         * argument_index 是一个十进制整数，用于表明参数在参数列表中的位置。第一个参数由 "1$"
         * 引用，第二个参数由 "2$" 引用，依此类推。 可选 flags
         * 是修改输出格式的字符集。有效标志集取决于转换类型。 可选 width
         * 是一个非负十进制整数，表明要向输出中写入的最少字符数。 可选 precision
         * 是一个非负十进制整数，通常用来限制字符数。特定行为取决于转换类型。 所需 conversion
         * 是一个表明应该如何格式化参数的字符。给定参数的有效转换集取决于参数的数据类型。
         */
        return StringUtil.simpleDateString(secs);
    }
}
