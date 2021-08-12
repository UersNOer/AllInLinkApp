package com.example.android_supervisor.ui.media;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.widget.TextView;

import com.cncoderx.recyclerviewhelper.adapter.BaseAdapter;
import com.example.android_supervisor.R;

import java.io.File;

/**
 * @author wujie
 */
public class AudioInfoAdapter extends MediaInfoAdapter {

    public AudioInfoAdapter(Context context,int maxSize) {
        super(context,R.layout.item_audio_info, maxSize);
    }

    @Override
    public void onBindViewHolder(BaseAdapter.BaseViewHolder holder, MediaInfoExt mediaInfo, int position) {
        super.onBindViewHolder(holder, mediaInfo, position);
        TextView tvName = holder.getView(R.id.tv_audio_name, TextView.class);
        tvName.setText(mediaInfo.mediaInfo.getTitle());
        tvName.setOnClickListener(new OnPlayClickListener(mediaInfo));
    }

    class OnPlayClickListener implements View.OnClickListener {
        private MediaInfoExt mediaInfo;

        public OnPlayClickListener(MediaInfoExt mediaInfo) {
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
