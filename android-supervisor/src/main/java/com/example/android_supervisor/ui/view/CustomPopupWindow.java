package com.example.android_supervisor.ui.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import androidx.appcompat.widget.AppCompatImageView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.amap.api.maps.model.Marker;
import com.example.android_supervisor.R;
import com.example.android_supervisor.entities.CensusEventRes;
import com.example.android_supervisor.entities.EventRes;
import com.example.android_supervisor.ui.CensusEventDetailActivity;
import com.example.android_supervisor.utils.SupportMultipleScreensUtil;

/**
 * Created by dw on 2019/8/2.
 */
public class CustomPopupWindow extends PopupWindow implements View.OnClickListener {
    private TextView caseDetail, tvCaseLink, tvCaseAddress, tvCaseType, caseTitle;
    private ImageView markerImg;
    private View mPopView;
    private OnItemClickListener mListener;
    private EventRes eventRes;
    private AppCompatImageView naviBtn;
    private Marker marker;
    private Context context;

    public CustomPopupWindow(Context context) {
        super(context);        // TODO Auto-generated constructor stub
        init(context);
        caseDetail.setOnClickListener(this);
        naviBtn.setOnClickListener(this);
    }

    public void setEventRes(Marker marker) {
        Object tag = marker.getObject();
        if (tag != null) {
            if (tag instanceof EventRes) {
                this.eventRes = (EventRes) marker.getObject();
                this.marker = marker;
                setPopupWindow();
            }else if (tag instanceof CensusEventRes){
                Intent intent = new Intent(context, CensusEventDetailActivity.class);
                intent.putExtra("id",((CensusEventRes) tag).getId());// id  taskId
                context.startActivity(intent);

            }
        }

    }

    /**
     * 初始化
     *
     * @param context
     */
    private void init(Context context) {
        this.context = context;
        LayoutInflater inflater = LayoutInflater.from(context);        //绑定布局
        mPopView = inflater.inflate(R.layout.layout_marker_detail, null);
        SupportMultipleScreensUtil.init(context);
        SupportMultipleScreensUtil.scale(mPopView);
        caseDetail = (TextView) mPopView.findViewById(R.id.case_detail);
        tvCaseLink = (TextView) mPopView.findViewById(R.id.tvCaseLink);
        tvCaseAddress = (TextView) mPopView.findViewById(R.id.tvCaseAddress);
        tvCaseType = (TextView) mPopView.findViewById(R.id.tvCaseType);
        caseTitle = (TextView) mPopView.findViewById(R.id.caseTitle);
        markerImg = (ImageView) mPopView.findViewById(R.id.marker_img);
         naviBtn = (AppCompatImageView) mPopView.findViewById(R.id.navi_btn);
    }

    /**
     * 设置窗口的相关属性
     */
    @SuppressLint("InlinedApi")
    private void setPopupWindow() {
        this.setContentView(mPopView);// 设置View
        this.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);// 设置弹出窗口的宽
        this.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);// 设置弹出窗口的高
        this.setFocusable(true);// 设置弹出窗口可
        this.setAnimationStyle(R.anim.pop_exit_anim);// 设置动画
        caseTitle.setText(!TextUtils.isEmpty(eventRes.getTitle())?eventRes.getTitle():eventRes.getAcceptCode());
        tvCaseType.setText(eventRes.getTypeName());
        tvCaseAddress.setText(eventRes.getAddress());
        markerImg.setImageBitmap(eventRes.getLayerUrl());
        tvCaseLink.setText(eventRes.getCurrentLink());
        this.setBackgroundDrawable(new ColorDrawable(0x00000000));// 设置背景透明
        mPopView.setOnTouchListener(new View.OnTouchListener() {// 如果触摸位置在窗口外面则销毁

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int height = mPopView.findViewById(R.id.id_pop_layout).getTop();
                int y = (int) event.getY();
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (y < height) {
                        dismiss();
                    }
                }
                return true;
            }
        });
    }


    public void setCase_detailShow(boolean isShow){

        caseDetail.setVisibility(isShow?View.VISIBLE:View.GONE);
    }

    /**
     * 定义一个接口，公布出去 在Activity中操作按钮的单击事件
     */
    public interface OnItemClickListener {
        void setOnItemClick(EventRes eventRes);
        void toNavi(Marker marker);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mListener = listener;
    }

    @Override
    public void onClick(View v) {        // TODO Auto-generated method stub
        if (mListener != null) {
            switch (v.getId()){
                case R.id.navi_btn:
                    mListener.toNavi(marker);
                    break;
                case R.id.case_detail:
                    mListener.setOnItemClick(eventRes);
                    break;
            }
        }
    }

}
