package com.example.android_supervisor.ui.media;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;

import com.cncoderx.recyclerviewhelper.adapter.BaseAdapter;
import com.example.android_supervisor.R;

import java.io.File;

/**
 * @author wujie
 */
public class ImageInfoAdapter extends MediaInfoAdapter {

    public ImageInfoAdapter(Context context,int maxSize) {
        super(context,R.layout.item_image_info, maxSize);
    }

    @Override
    public void onBindViewHolder(BaseAdapter.BaseViewHolder holder, MediaInfoExt mediaInfo, int position) {
        super.onBindViewHolder(holder, mediaInfo, position);
        ImageView ivPreview = holder.getView(R.id.iv_image_preview, ImageView.class);
        Bitmap bitmap = BitmapFactory.decodeFile(mediaInfo.mediaInfo.getThumbnailPath());
        ivPreview.setImageBitmap(bitmap);
        ivPreview.setOnClickListener(new OnPreviewClickListener(mediaInfo));

        ImageView ivPlay = holder.getView(R.id.iv_video_play, ImageView.class);
        String mimeType = mediaInfo.mediaInfo.getMimeType();
        if (!TextUtils.isEmpty(mimeType) && mimeType.startsWith("video")) {
            ivPlay.setVisibility(View.VISIBLE);
        } else {
            ivPlay.setVisibility(View.GONE);
        }
    }

    class OnPreviewClickListener implements View.OnClickListener {
        private MediaInfoExt mediaInfo;

        public OnPreviewClickListener(MediaInfoExt mediaInfo) {
            this.mediaInfo = mediaInfo;
        }

        @Override
        public void onClick(View v) {
            File file = new File(mediaInfo.mediaInfo.getOriginalPath());
            Uri uri = Uri.fromFile(file);
            try {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_VIEW);
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                intent.setDataAndType(uri, mediaInfo.mediaInfo.getMimeType());
                v.getContext().startActivity(intent);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
