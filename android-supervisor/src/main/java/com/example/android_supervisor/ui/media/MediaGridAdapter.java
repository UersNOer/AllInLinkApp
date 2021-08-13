package com.example.android_supervisor.ui.media;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.cncoderx.recyclerviewhelper.adapter.ObjectAdapter;
import com.squareup.picasso.Picasso;
import com.example.android_supervisor.R;
import com.example.android_supervisor.common.UserSession;
import com.example.android_supervisor.ui.view.RingProgressBar;
import com.example.android_supervisor.utils.ToastUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * @author yangjie
 */
public class MediaGridAdapter extends ObjectAdapter<MediaItem> {
    private boolean editable;
    private Context context;
    private UploadServiceManager uploadService;
    private OnDataChangedListener onDataChangedListener;

    public MediaGridAdapter(Context context) {
        super(R.layout.item_media_attach);
        this.context=context;
        uploadService = new UploadServiceManager();
    }

    public boolean isEditable() {
        return editable;
    }

    public void setEditable(boolean editable) {
        this.editable = editable;
    }

    public OnDataChangedListener getOnDataChangedListener() {
        return onDataChangedListener;
    }

    public void setOnDataChangedListener(OnDataChangedListener listener) {
        onDataChangedListener = listener;
    }

    @Override
    public void onBindViewHolder(BaseViewHolder holder, MediaItem item, int position) {
        RingProgressBar progress = holder.getView(R.id.pb_media_progress, RingProgressBar.class);
        ImageView ivPreview = holder.getView(R.id.iv_media_preview, ImageView.class);
        ImageView ivRemove = holder.getView(R.id.iv_media_remove, ImageView.class);
        ImageView ivPlay = holder.getView(R.id.iv_video_play, ImageView.class);
        TextView tv_progress = holder.getView(R.id.tv_progress, TextView.class);

        if (item.isUpload()) {
            tv_progress.setVisibility(View.VISIBLE);
            tv_progress.setText(item.getProgress()+"");

            progress.setVisibility(View.VISIBLE);
            progress.setProgress(item.getProgress());


            ivPreview.setVisibility(View.INVISIBLE);
            ivPreview.setOnClickListener(null);

            ivRemove.setVisibility(View.GONE);
            ivRemove.setOnClickListener(null);

            ivPlay.setVisibility(View.GONE);
        } else {
            tv_progress.setVisibility(View.GONE);
            progress.setVisibility(View.GONE);
            progress.setProgress(0);
            tv_progress.setText("");

            ivPreview.setVisibility(View.VISIBLE);
            if (item.getType() == 1) {
                ivPreview.setImageResource(R.drawable.audio);
                ivPreview.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
            } else {
                Picasso.get().load(item.getThumbnailUri())
                        .resizeDimen(R.dimen.thumb_img_width, R.dimen.thumb_img_height)
                        .into(ivPreview);
            }
            ivPreview.setOnClickListener(new OnPreviewClickListener(item));

            if (isEditable()) {
                ivRemove.setVisibility(View.VISIBLE);
                ivRemove.setOnClickListener(new OnRemoveClickListener(item, position));
            } else {
                ivRemove.setVisibility(View.GONE);
                ivRemove.setOnClickListener(null);
            }

            if (item.getType() == 2) {
                ivPlay.setVisibility(View.VISIBLE);
            } else {
                ivPlay.setVisibility(View.GONE);
            }
        }
    }

    public void upload(final Context context, int type, final MediaInfo mediaInfo) {
        MediaItem mediaItem = new MediaItem();
        mediaItem.setUpload(true);
        mediaItem.setType(type);
        File file =new File(mediaInfo.getOriginalPath());
        if (file.exists()){
            mediaItem.setFileName(file.getName());
        }

        add(mediaItem);
        upLoadMedia(context, mediaInfo, mediaItem);
    }

    private void upLoadMedia(Context context, MediaInfo mediaInfo, MediaItem mediaItem) {
        final int position = size() - 1;
        uploadService.upload(context, mediaInfo, new MediaUploadCallback(mediaItem, position));
    }

    class OnPreviewClickListener implements View.OnClickListener {
        private MediaItem mediaItem;

        public OnPreviewClickListener(MediaItem mediaItem) {
            this.mediaItem = mediaItem;
        }

        @Override
        public void onClick(View v) {
            Uri uri = mediaItem.getUri();
            Uri ThumbnailUr = mediaItem.getThumbnailUri();
            if (TextUtils.isEmpty(uri.toString())) {
                return;
            }

            Intent intent = new Intent();

            switch (mediaItem.getType()) {
                case 0:
                    List<MediaItem> mediaItems = getMediaItems();
                    List<String> strings = new ArrayList<>();
                    for (int i=0;i<mediaItems.size();i++){
                        if (!TextUtils.isEmpty(String.valueOf(mediaItems.get(i).getUri()))){

                            if ((uri.toString()).equals(String.valueOf(mediaItems.get(i).getUri()))){
                                strings.add(0,String.valueOf(mediaItems.get(i).getUri()));
                            }else {
                                strings.add(String.valueOf(mediaItems.get(i).getUri()));
                            }
                        }
                    }
                    intent.setClass(v.getContext(), ImagePreviewActivity.class);
                    intent.putExtra("urls", strings.toArray( new String[]{}));
                    v.getContext().startActivity(intent);
                    break;
                case 1:
                case 2:
                    intent.setClass(v.getContext(), VideoActivity.class);
                    intent.setDataAndType(uri, mediaItem.getMediaType());
                    v.getContext().startActivity(intent);
                    break;
                default:
                    try {
                        intent.setAction(Intent.ACTION_VIEW);
                        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                        intent.setDataAndType(uri, mediaItem.getMediaType());
                        v.getContext().startActivity(intent);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
            }
        }
    }

    class OnRemoveClickListener implements View.OnClickListener {
        private MediaItem mediaItem;
        private int position;

        public OnRemoveClickListener(MediaItem mediaItem, int position) {
            this.mediaItem = mediaItem;
            this.position = position;
        }

        @Override
        public void onClick(View v) {
            remove(position);
            if (onDataChangedListener != null) {
                onDataChangedListener.onRemove(mediaItem);
            }
            uploadService.delete(mediaItem);
        }
    }

    public class MediaUploadCallback implements Handler.Callback, UploadServiceManager.Callback {
        private Handler handler;
        private MediaItem media;
        private int position;

        private int lastProgress;
        private int countProgress;

        public MediaUploadCallback(MediaItem media, int position) {
            this.handler = new Handler(this);
            this.media = media;
            this.position = position;
        }

        @Override
        public void onStart() {
            handler.obtainMessage(0).sendToTarget();
        }

        @Override
        public void onProgress(long progress, long length) {
            Message message = handler.obtainMessage(1);
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("progress",progress);
                jsonObject.put("length",length);

                message.obj = jsonObject.toString();
                message .sendToTarget();

            } catch (JSONException e) {
                e.printStackTrace();
            }


        }

        @Override
        public void originalUploaded(String url) {
            Log.d("uploadUrl", "originalUploaded: "+url);
            String path=getPath(url);
            handler.obtainMessage(2, path).sendToTarget();
        }

        @Override
        public void thumbnailUploaded(String url) {
            String path=getPath(url);
            if (!TextUtils.isEmpty(path)){
                handler.obtainMessage(3, path).sendToTarget();
            }

        }

        @Override
        public void onFinish() {
            handler.obtainMessage(4).sendToTarget();
        }

        @Override
        public void onError(Throwable throwable) {
            handler.obtainMessage(5, throwable).sendToTarget();
        }

        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case 0: // onStart
                    media.setUpload(true);
                    media.setProgress(0);
                    notifyItemChanged(position);
                    break;
                case 1: // onProgress
                    try {
                        JSONObject jsonObject = new JSONObject((String) msg.obj);
                        long progresss = jsonObject.getLong("progress");
                        long length = jsonObject.getLong("length");

                        int progress = (int) (progresss * 100 / length);
                        Log.d("-25",progresss +" " +length +" " +  (progresss * 100)  +  +progress);

                        if (progress>50){
                            progress = Math.max(50,progress-8);
                        }
                        lastProgress  = progress;
                        media.setProgress(lastProgress);
                        notifyItemChanged(position);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    break;
                case 2: // originalUploaded
                    Uri uri = Uri.parse((String) msg.obj);
                    media.setUri(uri);
                    if (onDataChangedListener != null) {
                        onDataChangedListener.onDataChanged(media);
                    }
                    break;
                case 3: // thumbnailUploaded
                    Uri thumbnailUri = Uri.parse((String) msg.obj);
                    media.setThumbnailUri(thumbnailUri);
                    break;
                case 4: // onFinish
                    media.setUpload(false);
                    media.setProgress(100);
                    notifyItemChanged(position);
                    break;
                case 5: // onError

                    Throwable throwable = (Throwable) msg.obj;
                    ToastUtils.show(context,"附件上传失败:"+throwable.getMessage(),5000);
                    remove(media);
                    break;
            }
            return false;
        }
    }

    public String getPath(String url) {
//        String fileServer = SaveObjectUtils.getInstance(context).getObject("fileServer", null);
        String fileServer = UserSession.getFileServer(context);//文件服务器配置
        if (!TextUtils.isEmpty(fileServer)){
            String path=new StringBuilder(fileServer).append(url).toString();
            return path;
        }
       return "";
    }



    public List<MediaItem> getMediaItems() {
        ArrayList<MediaItem> mediaItems = new ArrayList<>();
        for (int i = 0; i < this.size(); i++) {
            mediaItems.add(this.get(i));
        }
        return mediaItems;
    }
}
