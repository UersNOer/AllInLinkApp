package com.example.android_supervisor.ui.media;

import android.content.Context;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.cncoderx.recyclerviewhelper.RecyclerViewHelper;
import com.example.android_supervisor.R;

import java.util.ArrayList;
import java.util.List;

/**
 * @author wujie
 */
public class MediaGridView extends FrameLayout implements OnDataChangedListener {
    public MediaGridAdapter adapter;
    private OnDataChangedListener onDataChangedListener;

    private TextView tv_verifier;

    public MediaGridView(Context context) {
        this(context, null);
    }

    public MediaGridView(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater inflater = LayoutInflater.from(context);
        inflater.inflate(R.layout.media_grid_view, this, true);
        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setItemAnimator(null);

        recyclerView.setLayoutManager(new GridLayoutManager(context, 4));
        recyclerView.addItemDecoration(new SpacingItemDecoration(
                getResources().getDimensionPixelOffset(R.dimen.space_width), false));

        adapter = new MediaGridAdapter(context);
        adapter.setOnDataChangedListener(this);

        RecyclerViewHelper.setAdapter(recyclerView, adapter);

        tv_verifier = findViewById(R.id.tv_verifier);
    }

    public boolean isEmpty() {
        return adapter.size() == 0;
    }

    public int getMediaItemSize() {
        return adapter.size();
    }

    public void addMediaItem(MediaItem mediaItem) {
        adapter.add(mediaItem);
    }

    public void clearMediaItems() {
        adapter.clear();
    }

    public void addMediaItems(List<MediaItem> mediaItems) {
        adapter.addAll(mediaItems);
    }

    public List<MediaItem> getMediaItems() {
        ArrayList<MediaItem> mediaItems = new ArrayList<>();
        for (int i = 0; i < adapter.size(); i++) {
            mediaItems.add(adapter.get(i));
        }
        return mediaItems;
    }

    public void setOnDataChangedListener(OnDataChangedListener listener) {
        onDataChangedListener = listener;
    }

    @Override
    public void onRemove(MediaItem mediaItem) {
        if (onDataChangedListener != null) {
            onDataChangedListener.onRemove(mediaItem);
        }
    }

    @Override
    public void onDataChanged(MediaItem mediaItem) {
        if (onDataChangedListener != null) {
            onDataChangedListener.onDataChanged(mediaItem);
        }
    }

    public boolean CanUpload() {
        boolean canUpload = true;
        if (adapter == null) {
            canUpload =  false;
        }
        if (adapter.size() == 0){
            canUpload = false;
        }
        for (int i=0;i<adapter.size();i++){
            int progress = adapter.get(i).getProgress();
            if (progress != 100){
                canUpload = false;
                break;
            }
        }
        return canUpload;
    }


    /**
     * 获取可上传的附件
     * @return
     */
    public List<MediaItem>  getCanUploadAttach() {
        List<MediaItem> mediaItems = new ArrayList<>();

        if (CanUpload()){
            for (int i=0;i<adapter.size();i++){
                int progress = adapter.get(i).getProgress();
                if (progress == 100){
                    mediaItems.add(adapter.get(i));
                }
            }
        }
        return mediaItems;
    }

    public void setDrawableHide(){
        tv_verifier.setCompoundDrawables(null,null,null,null);

    }
}
