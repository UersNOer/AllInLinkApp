package com.example.android_supervisor.ui.media;

import android.content.Context;
import android.text.TextUtils;

import java.io.File;

/**
 * @author wujie
 */
public class MediaInfoExt {
    final MediaInfo mediaInfo;
    boolean isChecked;

    public MediaInfoExt(MediaInfo mediaInfo) {
        this.mediaInfo = mediaInfo;
    }

    public void deleteFile(Context context) {
        deleteOriginalFile();
        deleteThumbnailFile();
        deleteMediaFromRes(context);
    }

    public void deleteOriginalFile() {
        String originalPath = mediaInfo.getOriginalPath();
        if (originalPath != null) {
            File originalFile = new File(originalPath);
            if (originalFile.exists()) {
                originalFile.delete();
            }
        }
    }

    public void deleteThumbnailFile() {
        String thumbnailPath = mediaInfo.getThumbnailPath();
        if (thumbnailPath != null) {
            File thumbnailFile = new File(thumbnailPath);
            if (thumbnailFile.exists()) {
                thumbnailFile.delete();
            }
        }
    }

    public void deleteMediaFromRes(Context context) {
        String mimeType = mediaInfo.getMimeType();
        String originalPath = mediaInfo.getOriginalPath();
        if (!TextUtils.isEmpty(mimeType)) {
            if (mimeType.startsWith("image")) {
                MediaUtils.deleteMediaInfoWithImage(context, originalPath);
            } else if (mimeType.startsWith("audio")) {
                MediaUtils.deleteMediaInfoWithAudio(context, originalPath);
            } else if (mimeType.startsWith("video")) {
                MediaUtils.deleteMediaInfoWithVideo(context, originalPath);
            }
        }
    }
}
