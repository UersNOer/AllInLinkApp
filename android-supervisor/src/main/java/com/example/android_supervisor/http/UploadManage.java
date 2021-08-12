package com.example.android_supervisor.http;//package com.example.android_supervisor.http;
//
//import android.content.Context;
//import android.net.Uri;
//import android.util.Log;
//
//import com.luck.picture.lib.config.PictureConfig;
//import com.luck.picture.lib.entity.LocalMedia;
//import com.example.android_supervisor.config.AppConfig;
//import com.example.android_supervisor.entities.Attach;
//import com.example.android_supervisor.http.common.ProgressTransformer;
//import com.example.android_supervisor.http.response.Response;
//import com.example.android_supervisor.http.service.FileService;
//import com.example.android_supervisor.http.service.PublicService;
//import com.example.android_supervisor.ui.view.ProgressText;
//
//import org.json.JSONObject;
//
//import java.io.File;
//import java.util.concurrent.CountDownLatch;
//
//import io.reactivex.Observable;
//import io.reactivex.ObservableSource;
//import io.reactivex.functions.Consumer;
//import io.reactivex.functions.Function;
//import okhttp3.MediaType;
//import okhttp3.MultipartBody;
//import okhttp3.RequestBody;
//import okhttp3.ResponseBody;
//
///**
// * Created by dw on 2019/7/31.
// */
//public class UploadManage {
//
//    private LocalMedia localMedia;
//    private Context context;
//    private Attach attach;
//    private FileService fileService;
//
//    public UploadManage(Context context) {
//        this.context = context;
//        fileService = ServiceGenerator.create(FileService.class);
//    }
//
//    public UploadManage(LocalMedia localMedia, Context context, Attach attach) {
//        this.localMedia = localMedia;
//        this.context = context;
//        this.attach = attach;
//        fileService = ServiceGenerator.create(FileService.class);
//    }
//
//    public Attach uploadFile() {
//        Observable<ResponseBody> observable = null;
//        final String parentId = AppConfig.getFilesParentId(context);
//        String thumbnailPath = null;
//        if (localMedia.getMimeType() == PictureConfig.TYPE_IMAGE) {
//            thumbnailPath = localMedia.getCompressPath();
//            Log.d("upload", "apply thumbnailPath: " + thumbnailPath);
//        } else if (localMedia.getMimeType() == PictureConfig.TYPE_VIDEO) {
//            thumbnailPath = localMedia.getPath();
//        }
//        if (thumbnailPath != null) {
//            final File thumbnailFile = new File(thumbnailPath);
//            final File originalFile = new File(localMedia.getPath());
//            if (thumbnailFile.exists()) {
//                RequestBody fileRequestBody = RequestBody.create(MediaType.parse("image/jpeg"), thumbnailFile);
//                MultipartBody.Part multipartFilePart = MultipartBody.Part.createFormData("file", thumbnailFile.getName(), fileRequestBody);
//                observable = fileService.upload(thumbnailFile.getName(), parentId, multipartFilePart)
//                        .flatMap(new Function<ResponseBody, ObservableSource<ResponseBody>>() {
//                            @Override
//                            public ObservableSource<ResponseBody> apply(ResponseBody response) throws Exception {
//                                String url = response.string();
//                                Log.d("upload", "apply url: " + url);
//                                attach.setThumbUrl(url);
//                                if (localMedia.getMimeType() == PictureConfig.TYPE_VIDEO) {
//                                    attach.setType(PictureConfig.TYPE_VIDEO);
//                                    RequestBody requestBody = RequestBody.create(MediaType.parse("video"), originalFile);
//                                    MultipartBody.Part multipartFilePart = MultipartBody.Part.createFormData("file", originalFile.getName(), requestBody);
//                                    return fileService.upload(originalFile.getName(), parentId, multipartFilePart);
//                                } else {
//                                    RequestBody requestFile = RequestBody.create(MediaType.parse("image/jpg"), thumbnailFile);
//                                    MultipartBody.Part multipartFilePart = MultipartBody.Part.createFormData("file", thumbnailFile.getName(), requestFile);
//                                    Observable<ResponseBody> observables = ServiceGenerator.create(PublicService.class)
//                                            .upload(multipartFilePart, 0);
//                                    return observables;
//                                }
//                            }
//                        });
//            }
//        } else {
//            final File originalFile = new File(localMedia.getPath());
//            RequestBody requestBody = RequestBody.create(MediaType.parse("audio"), originalFile);
//            MultipartBody.Part multipartFilePart = MultipartBody.Part.createFormData("file", originalFile.getName(), requestBody);
//            observable = fileService.upload(originalFile.getName(), parentId, multipartFilePart);
//        }
//        synchronized (context) {
//            final CountDownLatch countDownLatch = new CountDownLatch(1);
//
//            try {
//            observable.subscribe(new Consumer<ResponseBody>() {
//                @Override
//                public void accept(ResponseBody responseBody) throws Exception {
//                    attach.setType(localMedia.getMimeType());
//                    String originalPath;
//                    String result = responseBody.string();
//                    if (localMedia.getMimeType() == PictureConfig.TYPE_IMAGE) {
//                        JSONObject resultObj = new JSONObject(result);
//                        originalPath = resultObj.getString("data");
//                    }else
//                        originalPath=result;
//                    attach.setUrl(originalPath);
//                    Log.d("upload", result + "accept2: " + attach.getType() + ",getUrl:" + attach.getUrl());
//                    countDownLatch.countDown();
//                }
//            }, new Consumer<Throwable>() {
//                @Override
//                public void accept(Throwable throwable) throws Exception {
//
//                    countDownLatch.countDown();
//                }
//            });
//                countDownLatch.await();
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//        }
//        Log.d("upload1", "accept2: " + attach.getType() + ",getUrl:" + attach.getUrl());
//        return attach;
//    }
//
//
//    public void deleteFile(final LocalMedia localMedia) {
//        Observable<ResponseBody> observable;
//
//        String thumbnailPath = null;
//        final String path = localMedia.getPath();
//        if (localMedia.getMimeType() == PictureConfig.TYPE_IMAGE) {
//            thumbnailPath = localMedia.getCompressPath();
//            Log.d("upload", "apply thumbnailPath: " + thumbnailPath);
//        } else if (localMedia.getMimeType() == PictureConfig.TYPE_VIDEO) {
//            thumbnailPath = localMedia.getPath();
//        }
//        if (thumbnailPath != null) {
//            observable = fileService.delete(thumbnailPath)
//                    .flatMap(new Function<ResponseBody, ObservableSource<ResponseBody>>() {
//                        @Override
//                        public ObservableSource<ResponseBody> apply(ResponseBody response) throws Exception {
//                            return fileService.delete(path);
//                        }
//                    });
//        } else {
//            observable = fileService.delete(path);
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
//}
