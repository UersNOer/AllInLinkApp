package com.example.android_supervisor.ui.media;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;

import com.example.android_supervisor.config.AppConfig;
import com.example.android_supervisor.http.ServiceGenerator;
import com.example.android_supervisor.http.service.FileService;

import java.io.File;
import java.io.FileNotFoundException;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;


public class UploadServiceManager {
    private FileService fileService;

    public UploadServiceManager() {
//        fileService = ServiceGenerator.create(FileService.class);
    }

    @SuppressLint("StaticFieldLeak")
    public synchronized void upload(Context context, MediaInfo mediaInfo, final Callback callback) {
        final String originalPath = mediaInfo.getOriginalPath();
        if (originalPath == null) {
            callback.onError(new NullPointerException("The media original path is null."));
            return;
        }

        final File originalFile = new File(originalPath);
        if (!originalFile.exists()) {
            callback.onError(new FileNotFoundException("Not found media file."));
            return;
        }

        final MediaType mediaType = MediaType.parse(mediaInfo.getMimeType());// "image/jpeg"
        if (mediaType == null) {
            callback.onError(new IllegalArgumentException("Invalid media type"));
            return;
        }
        Log.d("upload", "mediaType: " + mediaType.toString());

        Observable<ResponseBody> observable = null;
        final String parentId = AppConfig.getFilesParentId(context);
        String thumbnailPath = mediaInfo.getThumbnailPath();

        fileService = ServiceGenerator.create(FileService.class);
        RequestBody requestBody = RequestBody.create(mediaType, originalFile);
        RequestBody fileRequestBody = new FileRequestBody(requestBody, callback);
        Log.d("upload",originalFile.getName()  +originalFile.getName().substring(0,originalFile.getName().lastIndexOf(".")));
        MultipartBody.Part multipartFilePart = MultipartBody.Part.createFormData("file", originalFile.getName(), fileRequestBody);
        //文件上传不要后缀 mediaInfo.getTitle()
        observable = fileService.upload(originalFile.getName().substring(0,originalFile.getName().lastIndexOf(".")), parentId, multipartFilePart);

        observable.doOnSubscribe(new Consumer<Disposable>() {
            @Override
            public void accept(Disposable disposable) throws Exception {
                if (callback != null) {
                    callback.onStart();
                }
            }
        }).subscribe(new Consumer<ResponseBody>() {
            @Override
            public void accept(ResponseBody responseBody) throws Exception {
                try {
                    String fileResult = responseBody.string();
                    Log.d("upload:", "accept2: " + fileResult);

                    if (callback != null && TextUtils.isEmpty(fileResult)) {
                        callback.onError(new IllegalArgumentException("文件服务器数据返回为空"));
                    }
                    else if (callback != null && "null".equalsIgnoreCase(fileResult)) {
                        callback.onError(new IllegalArgumentException("服务器返回为空"));
                    }else if (callback != null && fileResult.contains("null") ){
                        callback.onError(new IllegalArgumentException("文件服务器命名错误"));
                    }else if (callback != null && mediaType.toString().contains("image")){
                        if (!fileResult.contains(".jpg") && !fileResult.contains(".png") &&  !fileResult.contains(".gif") && !fileResult.contains(".jpeg")&& !fileResult.contains(".bmp")){
                            callback.onError(new IllegalArgumentException("服务器返回格式不正确"));
                        }else {
                            callback.thumbnailUploaded(fileResult);
                            callback.originalUploaded(fileResult);
                            callback.onFinish();
                        }
                    }
                    else {
                        if (callback != null) {
                            callback.thumbnailUploaded(fileResult);
                            callback.originalUploaded(fileResult);
                            callback.onFinish();
                        }
                    }
                } catch (Exception e) {
                    if (callback == null) {
                        callback.onError(new IllegalArgumentException("水印服务器出现异常"));
                    }
                }


            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) throws Exception {
                Log.d("throwable",throwable.getMessage());
                if (callback != null) {
                    callback.onError(new IllegalArgumentException(throwable.getMessage()));
            }
            }
        });
    }

    public void delete(MediaItem mediaItem) {
        final Uri uri = mediaItem.getUri();
        if (uri == null) {
            return;
        }

        Observable<ResponseBody> observable;

        final Uri thumbnailUri = mediaItem.getThumbnailUri();
        if (thumbnailUri != null) {
            fileService = ServiceGenerator.create(FileService.class);
            observable = fileService.delete(thumbnailUri.toString());
            observable = observable.flatMap(new Function<ResponseBody, ObservableSource<ResponseBody>>() {
                @Override
                public ObservableSource<ResponseBody> apply(ResponseBody response) throws Exception {
                    return fileService.delete(uri.toString());
                }
            });
        } else {
            observable = fileService.delete(uri.toString());
        }
        observable.subscribe(new Consumer<ResponseBody>() {
            @Override
            public void accept(ResponseBody response) throws Exception {
                // Todo
            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) throws Exception {
                throwable.printStackTrace();
            }
        });
    }

    public interface Callback {
        void onStart();

        void onProgress(long progress, long length);

        void originalUploaded(String url);

        void thumbnailUploaded(String url);

        void onFinish();

        void onError(Throwable throwable);
    }
}
