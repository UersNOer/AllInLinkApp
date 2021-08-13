package com.example.android_supervisor.ui.media;

import android.net.Uri;

/**
 * @author wujie
 */
public class MediaItem {
    private boolean isUpload;
    private int progress;

    private int type;
    private Uri uri;
    private Uri thumbnailUri;

    private String fileName;


    public void setIsUpload(boolean isUpload) {
        this.isUpload = isUpload;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    boolean isUpload() {
        return isUpload;
    }

    void setUpload(boolean upload) {
        isUpload = upload;
    }

    public int getProgress() {
        return progress;
    }

    public void setProgress(int progress) {
        this.progress = progress;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public Uri getUri() {
        return uri;
    }

    public void setUri(Uri uri) {
        this.uri = uri;
    }

    public Uri getThumbnailUri() {
        return thumbnailUri;
    }

    public void setThumbnailUri(Uri thumbnailUri) {
        this.thumbnailUri = thumbnailUri;
    }



    public String getMediaType() {
        String mediaType;
        switch (type) {
            case 0:
                mediaType = "image/jpeg";
                break;
            case 1:
                mediaType = "audio/amr";
                break;
            case 2:
                mediaType = "video/mp4";
                break;
            default:
                mediaType = "*/*";
                break;
        }
        return mediaType;
    }
}
