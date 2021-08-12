package com.example.android_supervisor.ui.media;

import android.content.Context;
import android.content.DialogInterface;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cncoderx.recyclerviewhelper.RecyclerViewHelper;
import com.example.android_supervisor.R;
import com.example.android_supervisor.common.ResultCallback;
import com.example.android_supervisor.entities.Attach;

import java.util.ArrayList;
import java.util.List;


public class MediaUploadGridView extends MediaGridView {
    private View footer;
    private int mMaxSize = 8; // 最多容纳的附件数量

    private boolean isAlbum =false;//是否选择相册



    public MediaUploadGridView(Context context) {
        this(context, null);
    }

    public MediaUploadGridView(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater inflater = LayoutInflater.from(context);
        RecyclerView recyclerView = findViewById(R.id.recycler_view);

        footer = createFooterView(inflater, recyclerView);
        RecyclerViewHelper.addFooterView(recyclerView, footer, false);

        adapter.setEditable(true);
        adapter.setOnDataChangedListener(this);
    }

    private View createFooterView(LayoutInflater inflater, ViewGroup parent) {
        View v = inflater.inflate(R.layout.media_picker_add, parent, false);
        v.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // 添加一个附件
                CharSequence[] items;
                if (isAlbum){
                   items = new CharSequence[]{"拍照", "图片", "音频", "视频", "从外部相册读取"};
                }else {
                    items = new CharSequence[]{"拍照", "图片", "音频", "视频"};
                }
                new AlertDialog.Builder(getContext())
                        .setItems(items, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if (which == 0) {
                                    //拍照页面
                                    CameraUtils.camera(getContext(), new CameraUtils.CameraCompleted() {
                                        @Override
                                        public void completed(MediaInfo mediaInfo) {

                                            adapter.upload(getContext(), 0, mediaInfo);
                                        }

                                    });

                                } else {
                                    showMediaListDialog(which - 1);
                                }
                            }
                        }).create().show();
            }
        });
        return v;
    }

    void showMediaListDialog(final int type) {
        new MediaDummyFragment(type,isAlbum, getRemainSize(), new ResultCallback<List<MediaInfo>>() {
            @Override
            public void onResult(List<MediaInfo> result, int tag) {

            }

            @Override
            public void onResult(List<MediaInfo> mediaInfos) {
                for (int i = 0; i <mediaInfos.size() ; i++) {
                    adapter.upload(getContext(), type==3?0:type, mediaInfos.get(i));
                }
            }
        }).show(getContext(), "media_picker");
    }

//    public void setMaxSize(int maxSize) {
//        mMaxSize = maxSize;
//    }

    public int getRemainSize() {
        return Math.max(0, mMaxSize - adapter.size());
    }

    @Override
    public void onDataChanged(MediaItem pos) {
        super.onDataChanged(pos);
        if (getRemainSize() > 0) {
            footer.setVisibility(View.VISIBLE);
        } else {
            footer.setVisibility(View.GONE);
        }
    }

    @Override
    public void onRemove(MediaItem mediaItem) {
        super.onDataChanged(mediaItem);
        onDataChanged(mediaItem);
    }



    public void setAttach(List<MediaItem> mediaItems){

        adapter.addAll(mediaItems);
    }

    public void setUploadNum(int uploadNum) {
        this.mMaxSize = uploadNum;
    }

    public void setIsAlbum(boolean isAlbum) {
        this.isAlbum = isAlbum;
    }


    /**
     * 获取真实的附件info 并添加到attaches中
     * @param mediaItems
     * @param attaches
     * @param type
     */
    public void getAttach(List<MediaItem> mediaItems, ArrayList<Attach> attaches, int type) {

        if (mediaItems.size() > 0) {
            for (MediaItem mediaItem : mediaItems) {
                Attach attach = new Attach();
                attach.setUsage(type);
                attach.setType(mediaItem.getType());

                if (mediaItem.getUri() != null) {

                    String[] split = mediaItem.getUri().toString().split("/");

                    Log.d("report", "obtainAttaches url: " + mediaItem.getUri().toString());
                    Log.d("report", "obtainAttaches type: " + split[split.length - 1]);
                    if (mediaItem.getUri().toString().contains("http")) {
                        attach.setUrl(split[split.length - 1]);
                        attach.setFileName(mediaItem.getFileName());
                        //音频 视频没有缩约图
                        if (mediaItem.getThumbnailUri() != null) {
                            if (!TextUtils.isEmpty(mediaItem.getThumbnailUri().toString())) {
                                String[] split_thumbnailUri = mediaItem.getThumbnailUri().toString().split("/");
                                attach.setThumbUrl((split_thumbnailUri[split_thumbnailUri.length - 1]));
                            }
                        }

                    } else {
                        attach.setUrl(mediaItem.getUri().toString());
                        attach.setFileName(split[split.length - 1]);
                    }
                }

                if (attach.getUrl() != null && attach.getThumbUrl() != null) {
                    attaches.add(attach);
                }else if (attach.getUrl()!=null && attach.getType()==1){
                    attaches.add(attach);
                }
            }
        }
    }
}
