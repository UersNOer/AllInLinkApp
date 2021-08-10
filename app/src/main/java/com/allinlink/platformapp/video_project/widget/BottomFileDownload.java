package com.allinlink.platformapp.video_project.widget;

import android.app.ActionBar;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.PopupWindow;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.allinlink.platformapp.R;
import com.allinlink.platformapp.video_project.ui.adapter.FileAdapter;

import java.util.Map;

public class BottomFileDownload extends PopupWindow {

    private final View pop_view;
    PopupDialogOnClick onClick;
    FileAdapter adapter;

    public interface PopupDialogOnClick {
        void startDownload(String fileName);
    }

    public BottomFileDownload(Context ct, PopupDialogOnClick onClick) {

        super(ct);
        this.onClick = onClick;
        /**
         * 打气泵.填充一个布局
         */
        LayoutInflater layoutInflater = (LayoutInflater) ct
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        pop_view = layoutInflater.inflate(R.layout.layout_dialog_download,
                null);
        pop_view.findViewById(R.id.rl_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        this.setContentView(pop_view);
        RecyclerView ryPhone = (RecyclerView) pop_view.findViewById(R.id.ry_callphone);
        ryPhone.setLayoutManager(new LinearLayoutManager(ct));
        adapter = new FileAdapter(ct);
        ryPhone.setAdapter(adapter);

        this.setContentView(pop_view);
        /**
         * 设置宽和高
         */
        this.setWidth(ActionBar.LayoutParams.MATCH_PARENT);
        this.setHeight(ActionBar.LayoutParams.MATCH_PARENT);
        /**
         * 设置弹出的框体可点击
         */
        this.setFocusable(true);
        /**
         * 设置弹出框的动画效果
         */
//        this.setAnimationStyle(R.style.popupAnimation);
        /**
         * 实例化一个背景色
         */
        ColorDrawable dw = new ColorDrawable(0x33000000);
        /**
         * 设置弹出框的背景色
         */
        this.setBackgroundDrawable(dw);
    }

    public void setData(String json, String gid) {
        Map<String, String> map = new Gson().fromJson(json, Map.class);
        adapter.setData(map,gid);
    }

    private View.OnClickListener photoListener;

    private View.OnClickListener listListener;

    public void setPhotoListener(View.OnClickListener photoListener) {
        this.photoListener = photoListener;
    }

    public void setListListener(View.OnClickListener listListener) {
        this.listListener = listListener;
    }

}
