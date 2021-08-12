package com.example.android_supervisor.ui.media;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.KeyEvent;
import android.view.View;
import android.widget.CheckBox;
import android.widget.Toast;

import com.cncoderx.recyclerviewhelper.adapter.ObjectAdapter;
import com.example.android_supervisor.R;
import com.example.android_supervisor.entities.PhotoEntity;
import com.example.android_supervisor.sqlite.PublicSqliteHelper;
import com.example.android_supervisor.utils.LogUtils;
import com.example.android_supervisor.utils.SyncDateTime;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author yangjie
 */
public class MediaInfoAdapter extends ObjectAdapter<MediaInfoExt> {
    private int maxSize;
    private OnSelectChangedListener onSelectChangedListener;

    private PublicSqliteHelper publicSqliteHelper;
    private int effectiveTime = 6;


    public MediaInfoAdapter(Context context,int resource, int maxSize) {
        super(resource);
        this.maxSize = maxSize;
        publicSqliteHelper = PublicSqliteHelper.getInstance(context);
        List<PhotoEntity> photoEntitys =  publicSqliteHelper.getPhotoSettingDao().queryForAll();
        if (photoEntitys!=null){
            for (PhotoEntity photoEntity:photoEntitys) {
                effectiveTime = photoEntity.getEffectiveTime() == 0 ? 12: photoEntity.getEffectiveTime();
            }
        }

    }

    public OnSelectChangedListener getOnSelectChangedListener() {
        return onSelectChangedListener;
    }

    public void setOnSelectChangedListener(OnSelectChangedListener listener) {
        onSelectChangedListener = listener;
    }

    @Override
    public void onBindViewHolder(BaseViewHolder holder, MediaInfoExt mediaInfo, int position) {
        CheckBox checkBox = holder.getView(R.id.select_box, CheckBox.class);
        if (mediaInfo.isChecked) {
            checkBox.setChecked(true);
        } else {
            checkBox.setChecked(false);
        }
        checkBox.setOnClickListener(new OnCheckClickListener(mediaInfo));
    }

    public void addMedia(MediaInfo object) {
        super.add(new MediaInfoExt(object));
    }

    public void addMedia(int index, MediaInfo object) {
        super.add(index, new MediaInfoExt(object));
    }

    public void addMedia(List<MediaInfo> objects) {
        List<MediaInfoExt> mediaInfos = new ArrayList<>();
        for (MediaInfo object : objects) {
            mediaInfos.add(new MediaInfoExt(object));
        }
        super.addAll(mediaInfos);
    }

    public void delete(Context context) {
        for (int i = 0, s = size(); i < s; i++) {
            MediaInfoExt mediaInfo = get(i);
            if (mediaInfo.isChecked) {
                mediaInfo.deleteFile(context);
                remove(i--);
                s--;
            }
        }
        if (onSelectChangedListener != null) {
            onSelectChangedListener.onSelectChanged();
        }
    }

    public List<MediaInfo> getSelectMediaInfos() {
        List<MediaInfo> mediaInfos = new ArrayList<>();
        for (int i = 0, s = size(); i < s; i++) {
            MediaInfoExt mediaInfo = get(i);
            if (mediaInfo.isChecked) {
                mediaInfos.add(mediaInfo.mediaInfo);
            }
        }
        return mediaInfos;
    }

    public int getSelectedCount() {
        int count = 0;
        for (int i = 0, s = size(); i < s; i++) {
            MediaInfoExt mediaInfo = get(i);
            if (mediaInfo.isChecked) {
                count++;
            }
        }
        return count;
    }

    class OnCheckClickListener implements View.OnClickListener {
        MediaInfoExt mediaInfo;

        public OnCheckClickListener(MediaInfoExt mediaInfo) {
            this.mediaInfo = mediaInfo;
        }

        @Override
        public void onClick(View v) {
            CheckBox checkBox = (CheckBox) v;
            boolean checked = checkBox.isChecked();

            //核对照片是否过期
            if (!checkDate(mediaInfo.mediaInfo.getFileTime())){
//                mediaInfo.isChecked = checked;
                alertDelete(v.getContext(),mediaInfo);
                checkBox.setChecked(false);
                return;
            }

            if (checked && getSelectedCount() >= maxSize) {
                checkBox.setChecked(false);
                Toast.makeText(v.getContext(), "最多只能选择" + maxSize + "张图片", Toast.LENGTH_SHORT).show();
            } else {
                mediaInfo.isChecked = checked;
                if (onSelectChangedListener != null) {
                    onSelectChangedListener.onSelectChanged();
                }
            }
        }
    }

    private void alertDelete(Context context,MediaInfoExt item) {

        AlertDialog.Builder dialog = new AlertDialog.Builder(context);
        dialog.setTitle("提示");
        dialog.setCancelable(false);
        dialog.setMessage(String.format("该图片的拍摄时间已超过%d小时，不能再做为案件图片上报。",effectiveTime));
        dialog.setPositiveButton("删除", new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                item.deleteFile(context);
                MediaInfoAdapter.this.remove(item);
                notifyDataSetChanged();

            }
        }).setNegativeButton("保留", new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                dialog.dismiss();
            }
        });
        dialog.setOnKeyListener(new DialogInterface.OnKeyListener()
        {
            @Override
            public boolean onKey(DialogInterface dialogInterface, int keyCode, KeyEvent keyEvent) {

                if(keyCode == KeyEvent.KEYCODE_SEARCH)
                {
                    return true;
                }
                return false;
            }

        });
        dialog.create().show();
    }

    public static interface OnSelectChangedListener {
        void onSelectChanged();
    }

    public boolean checkDate(Long date_long)
    {
        if (date_long == null){
            return true;
        }
        Date dtFile = new Date(date_long);

        Date temp = null;

        Date dtNow = new Date();
        Date dtEn = SyncDateTime.getInstance().getDate();

        if(dtNow.getTime() > dtEn.getTime())
        {
            temp = dtNow;
        }
        else if(dtNow.getTime() < dtEn.getTime())
        {
            temp = dtEn;
        }
        else
        {
            temp = dtNow;
        }

        //Math.abs((dtNow.getTime() - lastDateQZAlarm.getTime())/(1000*60)) >= 1
        if(Math.abs(((temp.getTime() - dtFile.getTime())) / (60*1000*60)) >= effectiveTime)
        {
            LogUtils.d(temp.getTime() +"dddd"+Math.abs(((temp.getTime() - dtFile.getTime()) / (60*1000))));
            return false;
        }
        return true;
    }

}
