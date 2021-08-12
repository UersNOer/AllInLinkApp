package com.example.android_supervisor.ui.media;//package com.example.android_supervisor.ui.media;
//
//import android.annotation.SuppressLint;
//import android.content.Context;
//import android.net.Uri;
//import android.util.Log;
//
//import com.example.android_supervisor.config.AppConfig;
//import com.example.android_supervisor.http.ServiceGenerator;
//import com.example.android_supervisor.http.service.FileService;
//import com.example.android_supervisor.http.service.PublicService;
//import com.example.android_supervisor.utils.LogUtils;
//
//import org.json.JSONObject;
//
//import java.io.File;
//import java.io.FileNotFoundException;
//
//import io.reactivex.Observable;
//import io.reactivex.ObservableSource;
//import io.reactivex.disposables.Disposable;
//import io.reactivex.functions.Action;
//import io.reactivex.functions.Consumer;
//import io.reactivex.functions.Function;
//import okhttp3.MediaType;
//import okhttp3.MultipartBody;
//import okhttp3.RequestBody;
//import okhttp3.ResponseBody;
//
//
//public class UploadService {
//    private FileService fileService;
//
//    public UploadService() {
////        fileService = ServiceGenerator.create(FileService.class);
//
//    }
//
//    @SuppressLint("StaticFieldLeak")
//    public synchronized void upload(Context context, MediaInfo mediaInfo, final Callback callback) {
//        final String originalPath = mediaInfo.getOriginalPath();
//        if (originalPath == null) {
//            callback.onError(new NullPointerException("The media original path is null."));
//            return;
//        }
//
//        final File originalFile = new File(originalPath);
//        if (!originalFile.exists()) {
//            callback.onError(new FileNotFoundException("Not found media file."));
//            return;
//        }
//
//        final MediaType mediaType = MediaType.parse(mediaInfo.getMimeType());
//        if (mediaType == null) {
//            callback.onError(new IllegalArgumentException("Invalid media type"));
//            return;
//        }
//
//        Observable<ResponseBody> observable = null;
//        final String parentId = AppConfig.getFilesParentId(context);
//        String thumbnailPath = mediaInfo.getThumbnailPath();
//        if (thumbnailPath != null) {
//            Log.d("upload img", "upload: " + thumbnailPath);
//            File thumbnailFile = new File(thumbnailPath);
//            if (thumbnailFile.exists()) {
//                RequestBody fileRequestBody = RequestBody.create(MediaType.parse("image/jpeg"), thumbnailFile);
//                fileService = ServiceGenerator.create(FileService.class);
//
//                //截取文件格式后缀
////                String name = thumbnailFile.getName().substring(0,thumbnailFile.getName().lastIndexOf(".")+1) ;
////                LogUtils.d("ddd"+name);
//
//                MultipartBody.Part multipartFilePart = MultipartBody.Part.createFormData("file",thumbnailFile.getName(), fileRequestBody);
//
//                //文件上传不要后缀 mediaInfo.getTitle()
//                observable = fileService.upload("", parentId, multipartFilePart);
//
//                observable = observable.flatMap(new Function<ResponseBody, ObservableSource<ResponseBody>>() {
//                    @Override
//                    public ObservableSource<ResponseBody> apply(ResponseBody response) throws Exception {
//                        String url = response.string();
//                        Log.d("upload", "apply url: "+url);
//                        if (callback != null) {
//                            callback.thumbnailUploaded(url);
//                        }
//
//                        if (!mediaType.toString().contains("image")) {
//
//                            //
//                            RequestBody fileRequestBody = new FileRequestBody(RequestBody.create(mediaType, originalFile), callback);
//
//                            MultipartBody.Part multipartFilePart = MultipartBody.Part.createFormData("file", originalFile.getName(), fileRequestBody);
//                            fileService = ServiceGenerator.create(FileService.class);
//                            return fileService.upload("", parentId, multipartFilePart);
//                        } else {
//                            RequestBody fileRequestBody = new FileRequestBody(RequestBody.create(mediaType, originalFile), callback);
////                            RequestBody fileRequestBody = RequestBody.create(mediaType, originalFile);
//                            MultipartBody.Part multipartFilePart = MultipartBody.Part.createFormData("file", originalFile.getName(), fileRequestBody);
//
//                            Observable<ResponseBody> observables = ServiceGenerator.create(PublicService.class)
//                                    .upload(multipartFilePart,0);
//                            observables.subscribe(new Consumer<ResponseBody>() {
//                                @Override
//                                public void accept(ResponseBody responseBody) throws Exception {
//                                    LogUtils.d("来的慢哦subscribe");
//                                    String result = responseBody.string();
//                                    JSONObject resultObj = new JSONObject(result);
//                                    String originalPath = resultObj.getString("data");
//                                    if (callback != null) {
//                                        callback.originalUploaded(originalPath);
//                                        callback.onFinish();
//                                    }
//                                }
//                            }, new Consumer<Throwable>() {
//                                @Override
//                                public void accept(Throwable throwable) throws Exception {
//                                    throwable.printStackTrace();
//                                    if (callback != null) {
//                                        callback.onError(throwable);
//                                    }
//                                }
//                            });
//                            return null;
//                        }
//                    }
//                });
//            }
//        }
//        //音频和视频
//        if (observable == null && !mediaType.toString().contains("image")) {
//
//            RequestBody fileRequestBody = new FileRequestBody(RequestBody.create(mediaType, originalFile), callback);
//            MultipartBody.Part multipartFilePart = MultipartBody.Part.createFormData("file", originalFile.getName(), fileRequestBody);
//            fileService = ServiceGenerator.create(FileService.class);
//            observable = fileService.upload(originalFile.getName(), parentId, multipartFilePart);
//        }
//
//        if (observable != null) {
//            observable.doOnSubscribe(new Consumer<Disposable>() {
//                @Override
//                public void accept(Disposable disposable) throws Exception {
//                    if (callback != null) {
//                        callback.onStart();
//                    }
//                }
//            })
//                    .doOnTerminate(new Action() {
//                        @Override
//                        public void run() throws Exception {
//                            //订阅即将被终止时的监听，无论是正常终止还是异常终止
////                            if (callback != null) {
////                                callback.onFinish();
////                            }
//                        }
//                    })
//                    .subscribe(new Consumer<ResponseBody>() {
//                        @Override
//                        public void accept(ResponseBody response) throws Exception {
//                            Log.d("upload:", "accept2: ");
//                            String url = response.string();
//                            if (callback != null) {
//                                callback.originalUploaded(url);
//                                callback.onFinish();
//                            }
//                        }
//                    }, new Consumer<Throwable>() {
//                        @Override
//                        public void accept(Throwable throwable) throws Exception {
////                            if (callback != null) {
////                                callback.onError(throwable);
////                            }
//                            Log.d("throwable",throwable.getMessage());
//                        }
//                    });
//        }
//    }
//
//    public void delete(MediaItem mediaItem) {
//        final Uri uri = mediaItem.getUri();
//        if (uri == null) {
//            return;
//        }
//
//        Observable<ResponseBody> observable;
//
//        final Uri thumbnailUri = mediaItem.getThumbnailUri();
//        if (thumbnailUri != null) {
//            fileService = ServiceGenerator.create(FileService.class);
//            observable = fileService.delete(thumbnailUri.toString());
//            observable = observable.flatMap(new Function<ResponseBody, ObservableSource<ResponseBody>>() {
//                @Override
//                public ObservableSource<ResponseBody> apply(ResponseBody response) throws Exception {
//                    return fileService.delete(uri.toString());
//                }
//            });
//        } else {
//            observable = fileService.delete(uri.toString());
//        }
//        observable.subscribe(new Consumer<ResponseBody>() {
//            @Override
//            public void accept(ResponseBody response) throws Exception {
//                // Todo
//            }
//        }, new Consumer<Throwable>() {
//            @Override
//            public void accept(Throwable throwable) throws Exception {
//                throwable.printStackTrace();
//            }
//        });
//    }
//
//    public interface Callback {
//        void onStart();
//
//        void onProgress(long progress, long length);
//
//        void originalUploaded(String url);
//
//        void thumbnailUploaded(String url);
//
//        void onFinish();
//
//        void onError(Throwable throwable);
//    }
//}
