package com.example.android_supervisor.ui;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;

import com.example.android_supervisor.common.Storage;
import com.example.android_supervisor.entities.WaterMarkSet;
import com.example.android_supervisor.sqlite.PublicSqliteHelper;
import com.example.android_supervisor.ui.media.CameraUtils;
import com.example.android_supervisor.ui.media.MediaInfo;
import com.example.android_supervisor.ui.media.MediaUtils;
import com.example.android_supervisor.utils.DateUtils;
import com.example.android_supervisor.utils.Environments;
import com.example.android_supervisor.utils.ImageUtils;
import com.example.android_supervisor.utils.SyncDateTime;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class CameraActivity extends BaseActivity {

    File mOutputFile;
    private WaterMarkSet mWaterSet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent;

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd_HHmmss");
        String dateTime = dateFormat.format(new Date());
        mOutputFile = new File(Storage.getTempDir(this), "IMG_" + dateTime + ".jpg");
        intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        List<WaterMarkSet> listWaterSet = PublicSqliteHelper.getInstance(this).getWaterSetDao().queryForAll();
        if (listWaterSet != null){
            for (int i = 0; i < listWaterSet.size();i++){
                if ( "0".equals(listWaterSet.get(i).getType())){
                    mWaterSet = listWaterSet.get(i);

                }else {
//                    mWaterSet = listWaterSet.get(i);
                }
            }
        }
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
            intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(mOutputFile));
        } else {
            ContentValues contentValues = new ContentValues(1);
            contentValues.put(MediaStore.Images.Media.DATA, mOutputFile.getAbsolutePath());
            Uri uri =getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        }
        startActivityForResult(intent, 0);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case 0:
                    String imagePath = mOutputFile.getAbsolutePath();
                    String photosDir = Storage.getImageDir(CameraActivity.this);
                    try {
                        File scaledImageFile = ImageUtils.compress(mOutputFile, 1024, 1024, 90, photosDir);//压缩

                        BitmapFactory.Options opts = new BitmapFactory.Options();
                        Bitmap bitmap = BitmapFactory.decodeFile(scaledImageFile.getAbsolutePath(), opts);
                        Date date = SyncDateTime.getInstance().getDate();
                        String txtTime = "拍照时间:"+ DateUtils.format(date,0);
                        if (mWaterSet != null && "1".equals(mWaterSet.getDbStatus())) {
                            String[] texts = null;
                            if ("2".equals(mWaterSet.getWmWord())) {
                                texts = new String[1];
//                                texts[0] =Environments.userBase.getUsername();
                                texts[0] = txtTime;
                            } else if ("1".equals(mWaterSet.getWmWord())) {
                                texts = new String[2];
                                texts[0] = "监督员";
                                texts[1] = txtTime;
                            } else {
                                texts = new String[2];
                                texts[0] = Environments.userBase.getUsername();
                                texts[1] = txtTime;
                            }

                            Bitmap watermarkBitmap = ImageUtils.watermarkBitmap(bitmap,texts,mWaterSet);
                            FileOutputStream stream = new FileOutputStream(scaledImageFile.getAbsoluteFile());
                            watermarkBitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                            bitmap.recycle();
                            watermarkBitmap.recycle();
                            stream.flush();
                            stream.close();
                        }
                        bitmap.recycle();
                        if (mOutputFile.exists()){
                            mOutputFile.delete();
                        }

                        MediaInfo mediaInfo = new MediaInfo();
                        mediaInfo.setCreateDate(scaledImageFile.lastModified());
                        mediaInfo.setMimeType("image/jpeg");
                        mediaInfo.setOriginalPath(scaledImageFile.getPath());
                        mediaInfo.setThumbnailPath(MediaUtils.createImageThumbnailPath(CameraActivity.this,scaledImageFile.getPath()));
                        mediaInfo.setTitle(scaledImageFile.getName());
                        CameraUtils.cameraCompleted(mediaInfo);

                        Uri contentUri = Uri.fromFile(scaledImageFile);
                        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE,contentUri);
                        sendBroadcast(mediaScanIntent);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }finally {
//                        mOutputFile.delete();
                        this.finish();
                    }
                    break;
            }

        }
        finish();
    }



}
