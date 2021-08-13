package com.example.android_supervisor.ui.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

import com.example.android_supervisor.entities.ChooseData;

import java.util.List;

/**
 * @author YJ
 */
@SuppressLint("AppCompatCustomView")
public class OptionalTextView extends TextView {
    List<ChooseData> chooseDatas;
    private SelectListener selectListener;

    public OptionalTextView(Context context) {
        super(context);

    }


    public OptionalTextView(Context context, @Nullable AttributeSet attrs){
        super(context, attrs);
        setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                alertSelect();
            }
        });

    }

    public void setChildClickable(boolean clickable){
        setClickable(clickable);
    }

    public void alertSelect(){

        if (chooseDatas == null || chooseDatas.size()<=0){
            return;
        }
        String[] items = new String[chooseDatas.size()];
        for (int i = 0;i<chooseDatas.size();i++) {
            items[i] = TextUtils.isEmpty(chooseDatas.get(i).getName()) ? "" : chooseDatas.get(i).getName();
        }
        new AlertDialog.Builder(getContext())
                .setTitle("请选择类型")
                .setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (selectListener!=null){
                            selectListener.selected(which,chooseDatas.get(which));
                        }
                        dialog.cancel();
                    }
                }).show();


    }


    public void setSelectListener(SelectListener selectListener){

        this.selectListener = selectListener;
    }

    public void setData(List<ChooseData> caseLevels) {
        this.chooseDatas = caseLevels;
    }

    public void setDefault() {
        if (chooseDatas!=null && chooseDatas.size()>0){

            if (selectListener!=null){
                selectListener.selected(0,chooseDatas.get(0));
            }
        }
    }

    public void setSelectedItem(int index) {
        if (chooseDatas!=null && chooseDatas.size()>0){

            if (selectListener!=null){
                selectListener.selected(index,chooseDatas.get(index));
            }
        }
    }



    public interface SelectListener{

        void selected(int which,ChooseData chooseData);
    }


}
