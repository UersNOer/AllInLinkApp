package com.example.android_supervisor.ui.view;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.android_supervisor.R;
import com.example.android_supervisor.entities.ChooseData;

import java.util.List;

/**
 * Created by yj on 2019/9/9.
 */
public class MenuItemView extends LinearLayout {
    private static final String NAMESPACE = "http://schemas.android.com/apk/res-auto";

    TextView tv_left_title;

    OptionalTextView tv_type;

    private Context mContext;
    private View mView;

//    private OptionalTextView.SelectListener selectListener;

    public MenuItemView(Context context) {

        this(context, null);
    }

    public MenuItemView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MenuItemView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        //ButterKnife.bind(this);
        initView(attrs);
    }

    private void initView( AttributeSet attrs) {

        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mView = inflater.inflate(R.layout.menu_item_view, this, true);

        tv_left_title = mView.findViewById(R.id.tv_left_title);
        tv_type =mView.findViewById(R.id.tv_type);

        String title = attrs.getAttributeValue(NAMESPACE, "title");
        setLetfTitle(title);

        String hint = attrs.getAttributeValue(NAMESPACE, "hint");
        tv_type.setHint(hint);

        boolean showdDrawable = attrs.getAttributeBooleanValue(NAMESPACE, "showdDrawable", true);
        setDrawable(showdDrawable);

        boolean childClickable = attrs.getAttributeBooleanValue(NAMESPACE, "childClickable", false);
        tv_type.setChildClickable(childClickable);

        boolean isMustTag = attrs.getAttributeBooleanValue(NAMESPACE, "isMustTag", false);
        if (isMustTag){
            Drawable dra= getResources().getDrawable(R.drawable.verifier);
            dra.setBounds( 0, 0, dra.getMinimumWidth(),dra.getMinimumHeight());
            tv_left_title.setCompoundDrawables(dra,null, null, null);
        }

    }

    public void setLetfTitle(String title){
        tv_left_title.setText(title);
    }

    public void setValue(String type){
        tv_type.setText(type);
    }

    public String getValue(){
        return tv_type.getText().toString().trim();
    }

    public void setDrawable(boolean isShowDrawable){
        if (!isShowDrawable)
        tv_type.setCompoundDrawables(null, null, null, null);
    }


    public void setChooseData(List<ChooseData> chooseDatas){
        tv_type.setData(chooseDatas);
    }


    public void setSelectListener(OptionalTextView.SelectListener selectListener){
//        this.selectListener = selectListener;
        tv_type.setSelectListener(selectListener);
    }

    @Override
    public boolean isFocused() {
        return true;
    }


    public void setDefault() {
        tv_type.setDefault();
    }

    public void setSelectedItem(int index) {
        tv_type.setSelectedItem(index);
    }

}
