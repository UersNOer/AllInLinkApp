package com.allinlink.platformapp.video_project.ui.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.shuyu.gsyvideoplayer.GSYVideoManager;
import com.shuyu.gsyvideoplayer.video.GSYADVideoPlayer;
import com.shuyu.gsyvideoplayer.video.StandardGSYVideoPlayer;
import com.allinlink.platformapp.R;
import com.allinlink.platformapp.video_project.bean.CameraBean;
import com.allinlink.platformapp.video_project.bean.ChannelBean;
import com.allinlink.platformapp.video_project.ui.activity.SearchCameraActivity;
import com.allinlink.platformapp.video_project.utils.LogUtil;
import com.allinlink.platformapp.video_project.utils.MediaUrlUtils;
import com.allinlink.platformapp.video_project.utils.UserCache;
import com.allinlink.platformapp.video_project.widget.CustomManager;
import com.allinlink.platformapp.video_project.widget.MultiSampleVideo;

import java.util.ArrayList;


public class CheckVideoAdapter extends RecyclerView.Adapter<CheckVideoAdapter.ViewHolder> {
    private static final String TAG = "CheckVideoAdapter";
    private int mode = 0;
    private Context context;
    ArrayList<MultiSampleVideo> jzvdStds = new ArrayList<>();
    ArrayList<ChannelBean> list = new ArrayList<>();
    int height;
    int width;
    MultiSampleVideo.VideoControl videoControl;

    public void setVideoControl(MultiSampleVideo.VideoControl videoControl) {
        this.videoControl = videoControl;
    }


    public void setMode(int mode) {
        this.mode = mode;
    }


    public int getSize() {
        return list.size();
    }


    public CheckVideoAdapter(Activity deviceFragment) {
        context = deviceFragment;
        height = deviceFragment.getWindowManager().getDefaultDisplay().getHeight();
        width = deviceFragment.getWindowManager().getDefaultDisplay().getWidth();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.item_check_video, parent, false);
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(mode == 0 ? (int) (width / 2) - 2 : width, mode == 0 ? (int) (width / 2 / 4 * 3) : width);
        layoutParams.leftMargin = 1;
        layoutParams.rightMargin = 1;
        layoutParams.bottomMargin = 1;
        itemView.setLayoutParams(layoutParams);
        ViewHolder viewHolder = new ViewHolder(itemView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if (list.size() > position) {

            holder.ivAdd.setVisibility(View.GONE);
            holder.gsyVideoPlayer.setVisibility(View.VISIBLE);
            holder.gsyVideoPlayer.setGid(list.get(position).getGid());

            //多个播放时必须在setUpLazy、setUp和getGSYVideoManager()等前面设置
            holder.gsyVideoPlayer.setPlayTag(list.get(position).getGid());
            holder.gsyVideoPlayer.setPlayPosition(position);


            holder.gsyVideoPlayer.setUpLazy(list.get(position).getMediaAddress(), true, null, null, "");

            //增加title
            holder.gsyVideoPlayer.getTitleTextView().setVisibility(View.GONE);
            //设置返回键
            holder.gsyVideoPlayer.getBackButton().setVisibility(View.GONE);

//设置全屏按键功能
            holder.gsyVideoPlayer.getFullscreenButton().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    holder.gsyVideoPlayer.startWindowFullscreen(context, false, true);
                }
            });
            holder.gsyVideoPlayer.setVideoControl(videoControl);

//            holder.player.fullscreenButton.setVisibility(View.GONE);
        } else {
            holder.ivAdd.setVisibility(View.VISIBLE);
            holder.gsyVideoPlayer.setVisibility(View.GONE);
            jzvdStds.add(holder.gsyVideoPlayer);
            holder.ivAdd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    context.startActivity(new Intent(context, SearchCameraActivity.class));
                }
            });
        }

    }

    @Override
    public int getItemCount() {
        return mode == 0 ? 4 : 1;
    }

    public void setCameraData(ArrayList<ChannelBean> cameraList) {
        for (MultiSampleVideo player : jzvdStds) {
            onPause();
        }
        this.list.clear();
        this.list.addAll(0, cameraList);

        this.notifyDataSetChanged();
    }

    public void onPause() {
        CustomManager.clearAllVideo();
    }

    public void startPlay() {
        for (MultiSampleVideo video : jzvdStds) {
            if (!video.isInPlayingState()) {
                video.startPlayLogic();

            }
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        RelativeLayout ivAdd;
        MultiSampleVideo gsyVideoPlayer;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ivAdd = itemView.findViewById(R.id.iv_add);
            gsyVideoPlayer = itemView.findViewById(R.id.detail_player);
        }
    }

}
