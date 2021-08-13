package com.example.android_supervisor.utils;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.example.android_supervisor.R;

import java.util.ArrayList;

public class SelectorSingleDialog extends AlertDialog {
    private Context mContext;
    private EasyPickerView epvView;
    private Callback callback;
    private ArrayList<String>list = new ArrayList<>();
    private int selectPosition = 0;
    private TextView tvSure;
    private TextView tvCancel;

    public SelectorSingleDialog(Context context, Callback callback) {
        super(context, R.style.dialog);
        this.mContext = context;
        this.callback = callback;

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
        initData();
        addListener();
    }

    private void addListener() {
        epvView.setOnScrollChangedListener(new EasyPickerView.OnScrollChangedListener() {
            @Override
            public void onScrollChanged(int curIndex) {

            }

            @Override
            public void onScrollFinished(int curIndex) {
                selectPosition = curIndex;
            }
        });
        tvSure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                if (callback != null){
                    callback.onSelector(list.get(selectPosition),selectPosition);
                }
            }
        });
        tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }

    private void initData() {
        epvView.setDataList(list);
    }

    public void initView(){
        View view = LayoutInflater.from(mContext).inflate(R.layout.dialog_selectsingle,null);
        epvView = view.findViewById(R.id.epvView);
        tvCancel = view.findViewById(R.id.tvCancel);
        tvSure = view.findViewById(R.id.tvSure);
        setContentView(view);
        setCanceledOnTouchOutside(false);
        getWindow().setGravity(Gravity.BOTTOM);
        WindowManager.LayoutParams attributes = getWindow().getAttributes();
        attributes.width = WindowManager.LayoutParams.MATCH_PARENT;
        getWindow().setAttributes(attributes);
    }

    public void setData(ArrayList<String> list){
        this.list.clear();
        if (list != null){
            this.list.addAll(list);
        }
        if (epvView != null){

            epvView.setDataList(this.list);
        }
    }

    public interface Callback{
        void onSelector(String param, int position);
    }
}
