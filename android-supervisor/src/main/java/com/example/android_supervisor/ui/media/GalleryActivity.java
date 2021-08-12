package com.example.android_supervisor.ui.media;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;

import com.cncoderx.recyclerviewhelper.RecyclerViewHelper;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.example.android_supervisor.R;
import com.example.android_supervisor.common.Storage;
import com.example.android_supervisor.ui.AudioActivity;
import com.example.android_supervisor.ui.ListActivity;
import com.example.android_supervisor.utils.DateUtils;
import com.example.android_supervisor.utils.DialogUtils;
import com.example.android_supervisor.utils.ImageUtils;
import com.example.android_supervisor.utils.SyncDateTime;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * @author yj 附件各种显示类
 */
public class GalleryActivity extends ListActivity {
    private int type;
    private int maxSize;

    private View btnOk, btnDel;

    MediaInfoAdapter mAdapter;

    private int pageIndex = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        type = getIntent().getIntExtra("type", 0);
        maxSize = getIntent().getIntExtra("maxSize", 9);
        ;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);

        setEnableRefresh(false);

        setTitle();
        createOkMenu();
        createDelMenu();

        mAdapter = getMediaInfoAdapter(this);
        setAdapter(mAdapter);

        View headerView = getHeaderView(this);
        RecyclerViewHelper.addHeaderView(mRecyclerView, headerView, false);

        pageIndex = 1;
        loadMediaInfoList();

        setNoData(false);
    }





    private void setTitle() {
        switch (type) {
            case 0:
                setTitle("图片");
                break;
            case 1:
                setTitle("音频");
                break;
            case 2:
                setTitle("视频");
                break;
        }
    }

    private void createOkMenu() {
        btnOk = addMenu("确定", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<MediaInfo> mediaInfos = mAdapter.getSelectMediaInfos();
                if (mediaInfos.size() > 0) {
                    ArrayList<MediaInfo> data = new ArrayList<>();
                    data.addAll(mediaInfos);

                    Intent intent = new Intent();
                    intent.putParcelableArrayListExtra("data", data);
                    setResult(Activity.RESULT_OK, intent);
                    finish();
                }
            }
        });
        btnOk.setVisibility(View.GONE);
    }

    private void createDelMenu() {
        btnDel = addMenu("删除", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogUtils.askYesNo(GalleryActivity.this, null, "是否删除选择的图片？", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mAdapter.delete(GalleryActivity.this);
                    }
                });
            }
        });
        btnDel.setVisibility(View.GONE);
    }

    public MediaInfoAdapter getMediaInfoAdapter(Context context) {
        MediaInfoAdapter adapter;
        switch (type) {
            case 1:
                adapter = new AudioInfoAdapter(GalleryActivity.this,maxSize);
                break;
            case 0:
            case 2:
                adapter = new ImageInfoAdapter(GalleryActivity.this,maxSize);
                break;
            default:
                throw new IllegalArgumentException("Unknown media type：" + type);
        }
        adapter.setOnSelectChangedListener(new MediaInfoAdapter.OnSelectChangedListener() {
            @Override
            public void onSelectChanged() {
                int selectedCount = mAdapter.getSelectedCount();
                if (selectedCount > 0) {
                    btnOk.setVisibility(View.VISIBLE);
                    btnDel.setVisibility(View.VISIBLE);
                } else {
                    btnOk.setVisibility(View.GONE);
                    btnDel.setVisibility(View.GONE);
                }
            }
        });
        return adapter;
    }

    @Override
    public RecyclerView.LayoutManager getLayoutManager(Context context) {
        RecyclerView.LayoutManager layoutManager;
        switch (type) {
            case 0:
            case 2:
                layoutManager = new GridLayoutManager(context, 4);
                break;
            default:
                layoutManager = super.getLayoutManager(context);
                break;
        }
        return layoutManager;
    }

    @Override
    public RecyclerView.ItemDecoration getItemDecoration(Context context) {
        RecyclerView.ItemDecoration itemDecoration;
        switch (type) {
            case 0:
            case 2:
                itemDecoration = new SpacingItemDecoration(
                        getResources().getDimensionPixelOffset(R.dimen.gallery_item_margin), true);
                break;
            default:
                itemDecoration = super.getItemDecoration(context);
                break;
        }
        return itemDecoration;
    }

    public View getHeaderView(Context context) {
        View headerView;
        switch (type) {
            case 0:
            case 2:
                headerView = LayoutInflater.from(context).inflate(
                        R.layout.item_image_info_add, mRecyclerView, false);
                headerView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        captureMedia();
                    }
                });
                break;
            case 1:
                headerView = LayoutInflater.from(context).inflate(
                        R.layout.item_audio_info_add, mRecyclerView, false);
                headerView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        captureMedia();
                    }
                });
                break;
            default:
                throw new IllegalArgumentException("Unknown media type");
        }
        return headerView;
    }

    @Override
    public void onLoadMore(RefreshLayout refreshLayout) {
        loadMediaInfoList();
    }

    public void loadMediaInfoList() {
        Observable.create(new ObservableOnSubscribe<List<MediaInfo>>() {
            @Override
            public void subscribe(ObservableEmitter<List<MediaInfo>> emitter) throws Exception {
                Context context = getApplicationContext();
                List<MediaInfo> mediaInfos;
                switch (type) {
                    case 0:
                        mediaInfos = MediaUtils.getMediaListWithImage(context,
                                Storage.getImageDir(context), pageIndex, pageIndex > 1 ? 32 : 31);
                        break;
                    case 1:
                        mediaInfos = MediaUtils.getMediaListWithAudio(context,
                                Storage.getAudioDir(context), pageIndex, 32);
                        break;
                    case 2:
                        mediaInfos = MediaUtils.getMediaListWithVideo(context,
                                Storage.getVideoDir(context), pageIndex, pageIndex > 1 ? 32 : 31);
                        break;
                    default:
                        throw new IllegalArgumentException("Unknown media type");
                }
                emitter.onNext(mediaInfos);
                emitter.onComplete();
            }
        }).compose(this.<List<MediaInfo>>bindToLifecycle())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnTerminate(new Action() {
                    @Override
                    public void run() throws Exception {
                        finishLoadMore();
                    }
                })
                .subscribe(new Consumer<List<MediaInfo>>() {
                    @Override
                    public void accept(List<MediaInfo> mediaInfos) throws Exception {
                        if (mediaInfos.size() > 0) {
                            if (pageIndex == 1){
                                mAdapter.clear();
                            }
                            tvEmpty.setVisibility(View.VISIBLE);
                            mAdapter.addMedia(mediaInfos);
                            pageIndex++;

                        } else {
                            tvEmpty.setVisibility(View.GONE);
                            setNoMoreData(true);
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {

                    }
                });
    }

    private File mOutputFile;

    private void captureMedia() {
        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd_HHmmss");
        String dateTime = dateFormat.format(new Date());

        Intent intent = null;
        if (type == 1) {

//                mOutputFile = new File(Storage.getAudioDir(this), "AUD_" + dateTime + ".wav");
//                intent = new Intent(MediaStore.Audio.Media.RECORD_SOUND_ACTION);

            intent = new Intent(this, AudioActivity.class);
            startActivityForResult(intent, type);
            return;
        }
        switch (type) {
            case 0:
                mOutputFile = new File(Storage.getTempDir(this), "IMG_" + dateTime + ".jpg");
                intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                break;
            case 2:
                mOutputFile = new File(Storage.getVideoDir(this), "VID_" + dateTime + ".mp4");
                intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
                intent.putExtra(MediaStore.EXTRA_DURATION_LIMIT, 10);
                break;
            default:
                throw new IllegalArgumentException("unknown media type");
        }
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
            intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(mOutputFile));
        } else {
            ContentValues contentValues = new ContentValues(1);
            contentValues.put(MediaStore.Images.Media.DATA, mOutputFile.getAbsolutePath());
            Uri uri = getContentResolver().insert(type ==2?MediaStore.Video.Media.EXTERNAL_CONTENT_URI:MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        }
        startActivityForResult(intent, type);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case 0:
                    String imagePath = mOutputFile.getAbsolutePath();
                    onCaptureImageResult(mOutputFile);
                    break;
                case 1:
                    if ( data.getData()!=null){
                        String audioPath = FileUtils.getPathFromUri(this, data.getData());
                        //onCaptureAudioResult(audioPath);
                    }
                    pageIndex=1;
                    loadMediaInfoList();

                    break;
                case 2:
                    String videoPath = mOutputFile.getAbsolutePath();
                    onCaptureVideoResult(videoPath);
                    break;
            }
        }
    }

    private void onCaptureImageResult(File imageFile) {
        String photosDir = Storage.getImageDir(GalleryActivity.this);
        try {
            File scaledImageFile = ImageUtils.compress(imageFile, 1024, 1024, 90, photosDir);
            BitmapFactory.Options opts = new BitmapFactory.Options();
            Bitmap bitmap = BitmapFactory.decodeFile(scaledImageFile.getAbsolutePath(), opts);
            Date date = SyncDateTime.getInstance().getDate();
            String txtTime = "拍照时间:" + DateUtils.format(date, 0);
            Bitmap watermarkBitmap = ImageUtils.watermarkBitmap(bitmap, txtTime);
            FileOutputStream stream = new FileOutputStream(scaledImageFile.getAbsoluteFile());
            watermarkBitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
            bitmap.recycle();
            watermarkBitmap.recycle();
            stream.flush();
            stream.close();


            MediaScannerConnection.scanFile(this, new String[]{scaledImageFile.getAbsolutePath()}, new String[]{"image/jpeg"},
                    new MediaScannerConnection.OnScanCompletedListener() {
                        @Override
                        public void onScanCompleted(String path, Uri uri) {
                            final MediaInfo mediaInfo = MediaUtils.getMediaInfoWithImage(getApplicationContext(), path);
                            if (!isFinishing() && mediaInfo != null) {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        File file = new File(mediaInfo.getOriginalPath());
                                        if (file != null && file.exists()) {
                                            mediaInfo.setFileDate(file.lastModified());
                                        }
                                        mAdapter.addMedia(0, mediaInfo);
                                    }
                                });
                            }
                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            mOutputFile.delete();
        }
    }

    private void onCaptureVideoResult(String path) {
        MediaScannerConnection.scanFile(this, new String[]{path}, new String[]{"video/*"},
                new MediaScannerConnection.OnScanCompletedListener() {
                    @Override
                    public void onScanCompleted(String path, Uri uri) {
                        final MediaInfo mediaInfo = MediaUtils.getMediaInfoWithVideo(getApplicationContext(), path);
                        if (!isFinishing() && mediaInfo != null) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    File file = new File(mediaInfo.getOriginalPath());
                                    if (file != null && file.exists()) {
                                        mediaInfo.setFileDate(file.lastModified());
                                    }
                                    mAdapter.addMedia(0, mediaInfo);
                                }
                            });
                        }
                    }
                });
    }

    private void onCaptureAudioResult(String path) {

        String outputPath;
        if (mOutputFile!=null){
            outputPath = mOutputFile.getAbsolutePath();
            copyFile(path, outputPath);
        }else {
            outputPath = path;
        }


        MediaScannerConnection.scanFile(this, new String[]{outputPath}, new String[]{"audio/*"},
                new MediaScannerConnection.OnScanCompletedListener() {
                    @Override
                    public void onScanCompleted(String path, Uri uri) {
                        final MediaInfo mediaInfo = MediaUtils.getMediaInfoWithAudio(getApplicationContext(), path);
                        if (!isFinishing() && mediaInfo != null) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    File file = new File(mediaInfo.getOriginalPath());
                                    if (file != null && file.exists()) {
                                        mediaInfo.setFileDate(file.lastModified());
                                    }
                                    mAdapter.addMedia(0, mediaInfo);
                                }
                            });
                        }
                    }
                });
    }

    private void copyFile(String inFilePath, String outFilePath) {
        try {
            FileInputStream input = new FileInputStream(inFilePath);
            FileOutputStream output = new FileOutputStream(outFilePath);
            byte[] bytes = new byte[1024];
            int length;
            while ((length = input.read(bytes)) > 0) {
                output.write(bytes, 0, length);
            }
            input.close();
            output.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
