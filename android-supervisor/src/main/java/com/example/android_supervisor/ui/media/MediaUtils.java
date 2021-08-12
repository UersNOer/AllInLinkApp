package com.example.android_supervisor.ui.media;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;

import com.example.android_supervisor.common.Storage;
import com.example.android_supervisor.utils.ImageUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * @author wujie
 */
public class MediaUtils {
    private static Uri IMAGES_URI = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
    private static String[] IMAGES_PROJ;
    static {
        ArrayList<String> projection = new ArrayList<>();
        projection.add(MediaStore.Images.Media._ID);
        projection.add(MediaStore.Images.Media.TITLE);
        projection.add(MediaStore.Images.Media.DATA);
        projection.add(MediaStore.Images.Media.BUCKET_ID);
        projection.add(MediaStore.Images.Media.BUCKET_DISPLAY_NAME);
        projection.add(MediaStore.Images.Media.MIME_TYPE);
        projection.add(MediaStore.Images.Media.DATE_ADDED);
        projection.add(MediaStore.Images.Media.DATE_MODIFIED);
        projection.add(MediaStore.Images.Media.LATITUDE);
        projection.add(MediaStore.Images.Media.LONGITUDE);
        projection.add(MediaStore.Images.Media.ORIENTATION);
        projection.add(MediaStore.Images.Media.SIZE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            projection.add(MediaStore.Images.Media.WIDTH);
            projection.add(MediaStore.Images.Media.HEIGHT);
        }
        IMAGES_PROJ = projection.toArray(new String[projection.size()]);
    }

    private static Uri VIDEO_URI = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
    private static String[] VIDEO_PROJ;
    static {
        ArrayList<String> projection = new ArrayList<>();
        projection.add(MediaStore.Video.Media._ID);
        projection.add(MediaStore.Video.Media.TITLE);
        projection.add(MediaStore.Video.Media.DATA);
        projection.add(MediaStore.Video.Media.BUCKET_ID);
        projection.add(MediaStore.Video.Media.BUCKET_DISPLAY_NAME);
        projection.add(MediaStore.Video.Media.MIME_TYPE);
        projection.add(MediaStore.Video.Media.DATE_ADDED);
        projection.add(MediaStore.Video.Media.DATE_MODIFIED);
        projection.add(MediaStore.Video.Media.LATITUDE);
        projection.add(MediaStore.Video.Media.LONGITUDE);
        projection.add(MediaStore.Video.Media.SIZE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            projection.add(MediaStore.Video.Media.WIDTH);
            projection.add(MediaStore.Video.Media.HEIGHT);
        }
        VIDEO_PROJ = projection.toArray(new String[projection.size()]);
    }

    private static Uri AUDIO_URI = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
    private static String[] AUDIO_PROJ;
    static {
        ArrayList<String> projection = new ArrayList<>();
        projection.add(MediaStore.Audio.Media._ID);
        projection.add(MediaStore.Audio.Media.TITLE);
        projection.add(MediaStore.Audio.Media.DATA);
        projection.add(MediaStore.Audio.Media.MIME_TYPE);
        projection.add(MediaStore.Audio.Media.DATE_ADDED);
        projection.add(MediaStore.Audio.Media.DATE_MODIFIED);
        projection.add(MediaStore.Audio.Media.SIZE);
        AUDIO_PROJ = projection.toArray(new String[projection.size()]);
    }

    public static List<MediaInfo> getMediaListWithImage(Context context, String bucketPath, int page, int limit) {
        List<MediaInfo> mediaInfos = new ArrayList<>();
        ContentResolver contentResolver = context.getContentResolver();
        int offset = (page - 1) * limit;

        Cursor cursor = contentResolver.query(
                IMAGES_URI,
                IMAGES_PROJ,
                MediaStore.Images.Media.DATA + " LIKE ? AND " + MediaStore.Images.Media.DATE_ADDED + " >= ?",
                new String[]{bucketPath+"%", String.valueOf(getTodayTimestamp())},
                MediaStore.Images.Media.DATE_ADDED + " DESC LIMIT " + limit + " OFFSET " + offset);

        if (cursor != null) {
            while (cursor.moveToNext()) {
                mediaInfos.add(parseMediaInfoWithImage(context, cursor));
            }
            cursor.close();
        }

        if (cursor != null && !cursor.isClosed()) {
            cursor.close();
        }
        return mediaInfos;
    }

    public static List<MediaInfo> getMediaListWithVideo(Context context, String bucketPath, int page, int limit) {
        List<MediaInfo> mediaInfos = new ArrayList<>();
        ContentResolver contentResolver = context.getContentResolver();
        int offset = (page - 1) * limit;

        Cursor cursor = contentResolver.query(
                VIDEO_URI,
                VIDEO_PROJ,
                MediaStore.Video.Media.DATA + " LIKE ? AND " + MediaStore.Images.Media.DATE_ADDED + " >= ?",
                new String[]{bucketPath+"%", String.valueOf(getTodayTimestamp())},
                MediaStore.Video.Media.DATE_ADDED + " DESC LIMIT " + limit + " OFFSET " + offset);

        if (cursor != null) {
            while (cursor.moveToNext()) {
                mediaInfos.add(parseMediaInfoWithVideo(context, cursor));
            }
            cursor.close();
        }

        return mediaInfos;
    }

    public static List<MediaInfo> getMediaListWithAudio(Context context, String bucketPath, int page, int limit) {
        List<MediaInfo> mediaInfos = new ArrayList<>();
        ContentResolver contentResolver = context.getContentResolver();
        int offset = (page - 1) * limit;

        Cursor cursor = contentResolver.query(
                AUDIO_URI,
                AUDIO_PROJ,
                MediaStore.Audio.Media.DATA + " LIKE ? AND " + MediaStore.Images.Media.DATE_ADDED + " > ?",
                new String[]{bucketPath+"%", String.valueOf(getTodayTimestamp())},
                MediaStore.Audio.Media.DATE_ADDED + " DESC LIMIT " + limit + " OFFSET " + offset);

        if (cursor != null) {
            while (cursor.moveToNext()) {
                mediaInfos.add(parseMediaInfoWithAudio(context, cursor));
            }
            cursor.close();
        }

        return mediaInfos;
    }

    public static MediaInfo getMediaInfoWithImage(Context context, String originalPath) {
        MediaInfo mediaInfo = null;
        ContentResolver contentResolver = context.getContentResolver();

        Cursor cursor = contentResolver.query(
                IMAGES_URI,
                IMAGES_PROJ,
                MediaStore.Images.Media.DATA + "=?",
                new String[]{originalPath}, null);

        if (cursor != null) {
            if (cursor.moveToNext()) {
                mediaInfo = parseMediaInfoWithImage(context, cursor);
            }
            cursor.close();
        }

        return mediaInfo;
    }

    public static MediaInfo getMediaInfoWithVideo(Context context, String originalPath) {
        MediaInfo mediaInfo = null;
        ContentResolver contentResolver = context.getContentResolver();

        Cursor cursor = contentResolver.query(
                VIDEO_URI,
                VIDEO_PROJ,
                MediaStore.Video.Media.DATA + "=?",
                new String[]{originalPath}, null);

        if (cursor != null) {
            if (cursor.moveToNext()) {
                mediaInfo = parseMediaInfoWithVideo(context, cursor);
            }
            cursor.close();
        }

        return mediaInfo;
    }

    public static MediaInfo getMediaInfoWithAudio(Context context, String originalPath) {
        MediaInfo mediaInfo = null;
        ContentResolver contentResolver = context.getContentResolver();

        Cursor cursor = contentResolver.query(
                AUDIO_URI,
                AUDIO_PROJ,
                MediaStore.Audio.Media.DATA + "=?",
                new String[]{originalPath}, null);

        if (cursor != null) {
            if (cursor.moveToNext()) {
                mediaInfo = parseMediaInfoWithAudio(context, cursor);
            }
            cursor.close();
        }

        return mediaInfo;
    }

    private static MediaInfo parseMediaInfoWithImage(Context context, Cursor cursor) {
        long id = cursor.getLong(cursor.getColumnIndex(MediaStore.Images.Media._ID));
        String title = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.TITLE));
        String mimeType = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.MIME_TYPE));
        long createDate = cursor.getLong(cursor.getColumnIndex(MediaStore.Images.Media.DATE_ADDED));
        long modifiedDate = cursor.getLong(cursor.getColumnIndex(MediaStore.Images.Media.DATE_MODIFIED));
        String originalPath = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));

        MediaInfo mediaInfo = new MediaInfo();
        mediaInfo.setId(id);
        mediaInfo.setTitle(title);
        mediaInfo.setMimeType(mimeType);
        mediaInfo.setCreateDate(createDate);
        mediaInfo.setModifiedDate(modifiedDate);
        mediaInfo.setOriginalPath(originalPath);
        mediaInfo.setThumbnailPath(createImageThumbnailPath(context, originalPath));

        int width = 0, height = 0;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            width = cursor.getInt(cursor.getColumnIndex(MediaStore.Images.Media.WIDTH));
            height = cursor.getInt(cursor.getColumnIndex(MediaStore.Images.Media.HEIGHT));
        } else {
            try {
                ExifInterface exifInterface = new ExifInterface(originalPath);
                width = exifInterface.getAttributeInt(ExifInterface.TAG_IMAGE_WIDTH, 0);
                height = exifInterface.getAttributeInt(ExifInterface.TAG_IMAGE_LENGTH, 0);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        mediaInfo.setWidth(width);
        mediaInfo.setHeight(height);

        double latitude = cursor.getDouble(cursor.getColumnIndex(MediaStore.Images.Media.LATITUDE));
        double longitude = cursor.getDouble(cursor.getColumnIndex(MediaStore.Images.Media.LONGITUDE));
        int orientation = cursor.getInt(cursor.getColumnIndex(MediaStore.Images.Media.ORIENTATION));
        long length = cursor.getLong(cursor.getColumnIndex(MediaStore.Images.Media.SIZE));
        mediaInfo.setLatitude(latitude);
        mediaInfo.setLongitude(longitude);
        mediaInfo.setOrientation(orientation);
        mediaInfo.setLength(length);

        return mediaInfo;
    }

    private static MediaInfo parseMediaInfoWithVideo(Context context, Cursor cursor) {
        long id = cursor.getLong(cursor.getColumnIndex(MediaStore.Video.Media._ID));
        String title = cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.TITLE));
        String mimeType = cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.MIME_TYPE));
        long createDate = cursor.getLong(cursor.getColumnIndex(MediaStore.Video.Media.DATE_ADDED));
        long modifiedDate = cursor.getLong(cursor.getColumnIndex(MediaStore.Video.Media.DATE_MODIFIED));
        long length = cursor.getLong(cursor.getColumnIndex(MediaStore.Video.Media.SIZE));
        String originalPath = cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.DATA));

        MediaInfo mediaInfo = new MediaInfo();
        mediaInfo.setId(id);
        mediaInfo.setTitle(title);
        mediaInfo.setMimeType(mimeType);
        mediaInfo.setCreateDate(createDate);
        mediaInfo.setModifiedDate(modifiedDate);
        mediaInfo.setLength(length);
        mediaInfo.setOriginalPath(originalPath);
        mediaInfo.setThumbnailPath(createVideoThumbnailPath(context, id, originalPath));

        int width = 0, height = 0;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            width = cursor.getInt(cursor.getColumnIndex(MediaStore.Video.Media.WIDTH));
            height = cursor.getInt(cursor.getColumnIndex(MediaStore.Video.Media.HEIGHT));
        } else {
            try {
                ExifInterface exifInterface = new ExifInterface(originalPath);
                width = exifInterface.getAttributeInt(ExifInterface.TAG_IMAGE_WIDTH, 0);
                height = exifInterface.getAttributeInt(ExifInterface.TAG_IMAGE_LENGTH, 0);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        mediaInfo.setWidth(width);
        mediaInfo.setHeight(height);

        double latitude = cursor.getDouble(cursor.getColumnIndex(MediaStore.Video.Media.LATITUDE));
        double longitude = cursor.getDouble(cursor.getColumnIndex(MediaStore.Video.Media.LONGITUDE));
        mediaInfo.setLatitude(latitude);
        mediaInfo.setLongitude(longitude);

        return mediaInfo;
    }

    private static MediaInfo parseMediaInfoWithAudio(Context context, Cursor cursor) {
        long id = cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.Media._ID));
        String title = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.TITLE));
        String mimeType = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.MIME_TYPE));
        long createDate = cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.Media.DATE_ADDED));
        long modifiedDate = cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.Media.DATE_MODIFIED));
        long length = cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.Media.SIZE));
        String originalPath = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DATA));

        //录音没有简图  用原文件尝试
        String thumbnailFile = originalPath;

        MediaInfo mediaInfo = new MediaInfo();
        mediaInfo.setId(id);
        mediaInfo.setTitle(title);
        mediaInfo.setMimeType(mimeType);
        mediaInfo.setCreateDate(createDate);
        mediaInfo.setModifiedDate(modifiedDate);
        mediaInfo.setLength(length);
        mediaInfo.setOriginalPath(originalPath);
        mediaInfo.setThumbnailPath(thumbnailFile);

        return mediaInfo;
    }

    public static String createImageThumbnailPath(Context context, String originalPath) {
        File originalFile = new File(originalPath);
        if (!originalFile.exists()) {
            return null;
        }

        String originalName = originalFile.getName();
        String photosTempDir = getPhotosTempDir(context);
        File thumbnailFile = new File(photosTempDir, originalName);

        if (!thumbnailFile.exists()) {
            try {
                thumbnailFile = ImageUtils.compress(originalFile, 256, 256, 90, photosTempDir);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return thumbnailFile.getAbsolutePath();
    }

    private static String getPhotosTempDir(Context context) {
        String photosDir = Storage.getImageDir(context);
        File photosTempDir = new File(photosDir, "temp");
        if (!photosTempDir.exists()) {
            if (photosTempDir.mkdir()) {
                File nomedia = new File(photosTempDir, ".nomedia");
                try {
                    nomedia.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return photosTempDir.getAbsolutePath();
    }

    private static String createVideoThumbnailPath(Context context, long mediaId, String originalPath) {
        File originalFile = new File(originalPath);
        if (!originalFile.exists()) {
            return null;
        }

        String originalName = originalFile.getName();
        String videosTempDir = getVideosTempDir(context);
        File thumbnailFile = new File(videosTempDir, originalName);

        if (!thumbnailFile.exists()) {
            Bitmap bitmap = MediaStore.Video.Thumbnails.getThumbnail(context.getContentResolver(),
                    mediaId, MediaStore.Video.Thumbnails.MINI_KIND, null);

            File tempFile = new File(Storage.getTempDir(context), originalName);
            if (bitmap != null) {
                try {
                    FileOutputStream output = new FileOutputStream(tempFile);
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, output);
                    output.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            if (tempFile.exists()) {
                try {
                    thumbnailFile = ImageUtils.compress(tempFile, 256, 256, 90, videosTempDir);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        return thumbnailFile.getAbsolutePath();
    }

    private static String getVideosTempDir(Context context) {
        String videosDir = Storage.getVideoDir(context);
        File videosTempDir = new File(videosDir, "temp");
        if (!videosTempDir.exists()) {
            if (videosTempDir.mkdir()) {
                File nomedia = new File(videosTempDir, ".nomedia");
                try {
                    nomedia.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return videosTempDir.getAbsolutePath();
    }

    private static long getTodayTimestamp() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTimeInMillis() / 1000;
    }

    public static int clearMediaListWithImage(Context context, String bucketPath) {
        ContentResolver contentResolver = context.getContentResolver();
        return contentResolver.delete(
                IMAGES_URI,
        MediaStore.Images.Media.DATA + " LIKE ?",
                new String[]{bucketPath+"%"});

    }

    public static int clearMediaListWithAudio(Context context, String bucketPath) {
        ContentResolver contentResolver = context.getContentResolver();
        return contentResolver.delete(
                AUDIO_URI,
                MediaStore.Images.Media.DATA + " LIKE ?",
                new String[]{bucketPath+"%"});
    }

    public static int clearMediaListWithVideo(Context context, String bucketPath) {
        ContentResolver contentResolver = context.getContentResolver();
        return contentResolver.delete(
                VIDEO_URI,
                MediaStore.Images.Media.DATA + " LIKE ?",
                new String[]{bucketPath+"%"});
    }

    public static int deleteMediaInfoWithImage(Context context, String originalPath) {
        ContentResolver contentResolver = context.getContentResolver();
        return contentResolver.delete(
                IMAGES_URI,
                MediaStore.Images.Media.DATA + "=?",
                new String[]{originalPath});
    }

    public static int deleteMediaInfoWithAudio(Context context, String originalPath) {
        ContentResolver contentResolver = context.getContentResolver();
        return contentResolver.delete(
                AUDIO_URI,
                MediaStore.Images.Media.DATA + "=?",
                new String[]{originalPath});
    }

    public static int deleteMediaInfoWithVideo(Context context, String originalPath) {
        ContentResolver contentResolver = context.getContentResolver();
        return contentResolver.delete(
                VIDEO_URI,
                MediaStore.Images.Media.DATA + "=?",
                new String[]{originalPath});
    }
}
