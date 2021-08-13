package com.example.android_supervisor.ui.media;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.core.os.OperationCanceledException;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;
import com.example.android_supervisor.common.ResultCallback;
import com.example.android_supervisor.common.Storage;
import com.example.android_supervisor.utils.GlideEngine;
import com.example.android_supervisor.utils.ImageUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * @author wujie
 */
@SuppressLint("ValidFragment")
public class MediaDummyFragment extends Fragment {
    private final int type;
    private boolean dismissed ,isAlbum;
    private int maxSize;
    private ResultCallback<List<MediaInfo>> callback;



    public MediaDummyFragment(int type,boolean isAlbum, int maxSize, ResultCallback<List<MediaInfo>> callback) {
        this.type = type;
        this.isAlbum = isAlbum;
        this.maxSize = maxSize;
        this.callback = callback;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (type ==0 || type == 2 || type == 1){
            Intent intent = new Intent(getActivity(), GalleryActivity.class);
            intent.putExtra("type", type);
            intent.putExtra("maxSize", maxSize);
            startActivityForResult(intent, type);
        }else {

            PictureSelector.create(MediaDummyFragment.this)
                    .openGallery(PictureMimeType.ofImage())
                    .loadImageEngine(GlideEngine.createGlideEngine())// 外部传入图片加载引擎，必传项
                    .compress(false)
                    .maxSelectNum(maxSize)
                    .forResult(PictureConfig.CHOOSE_REQUEST);
        }



    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {

            ArrayList<MediaInfo> mediaInfos=new ArrayList<>();

            if (requestCode == PictureConfig.CHOOSE_REQUEST){
                List<LocalMedia> selectList = PictureSelector.obtainMultipleResult(data);
                if (selectList ==null || selectList.size() ==0){
                    return;
                }

                for (int i = 0; i < selectList.size(); i++) {

                    MediaInfo mediaInfo = new MediaInfo();
                    mediaInfo.setId(selectList.get(i).getId());
                    mediaInfo.setTitle(selectList.get(i).getFileName());
                    mediaInfo.setMimeType(selectList.get(i).getMimeType());

                    File file =new File(selectList.get(i).getPath());
                    if (file.exists()){
                        mediaInfo.setCreateDate(file.lastModified());
                        mediaInfo.setModifiedDate(file.lastModified());
                    }

                    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q){
                        mediaInfo.setOriginalPath(selectList.get(i).getPath());
                    }else {
                        mediaInfo.setOriginalPath(selectList.get(i).getAndroidQToPath());
                    }
                    if (selectList.get(0).isCompressed()){
                        mediaInfo.setThumbnailPath(selectList.get(i).getCutPath());
                    }else {

                        try {
                            String photosDir = Storage.getImageDir(getContext());
                            File scaledImageFile = ImageUtils.compress(new File(mediaInfo.getOriginalPath()), 1024, 1024, 90, photosDir);//压缩
                            mediaInfo.setThumbnailPath(scaledImageFile.getAbsolutePath());
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }

                    mediaInfos.add(mediaInfo);
                }
            }

            if (requestCode == type){
                 mediaInfos = data.getParcelableArrayListExtra("data");
            }

            if (callback != null) {

                callback.onResult(mediaInfos);
            }

        } else {
            if (callback != null) {
                callback.onError(new OperationCanceledException());
            }
        }
       // dismissInternal(true);
    }

    public void show(Context context, String tag) {

        FragmentManager fragmentManager = ((FragmentActivity) context).getSupportFragmentManager();

        show(fragmentManager, tag);
    }

    public void show(FragmentManager manager, String tag) {
        dismissed = false;
        FragmentTransaction ft = manager.beginTransaction();
        ft.add(this, tag);
        ft.commit();
    }

    public void dismiss() {
        dismissInternal(false);
    }

    public void dismissAllowingStateLoss() {
        dismissInternal(true);
    }

    void dismissInternal(boolean allowStateLoss) {
        if (dismissed) {
            return;
        }
        dismissed = true;

        onDismiss();

        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.remove(this);
        if (allowStateLoss) {
            ft.commitAllowingStateLoss();
        } else {
            ft.commit();
        }
    }

    protected void onDismiss() {
        // dismiss
    }
}
