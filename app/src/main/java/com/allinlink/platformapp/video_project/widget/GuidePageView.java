package com.allinlink.platformapp.video_project.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.allinlink.platformapp.R;


/**
 * Description:引导页自定义view
 * Created by ltd ON 2020/4/12
 * Phone:18600920091
 * Email:td.liu@unistrong.com
 */

public class GuidePageView extends LinearLayout {
    private ImageView mGuideImgView, mGuideEnterImageView;

    public GuidePageView(Context context) {
        this(context, null);
    }

    public GuidePageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public GuidePageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        LayoutInflater.from(context).inflate(R.layout.item_guide_view_page, this);
    }

    public void setGuidePage(int page) {
        if (page == 1) {  //1为常规的引导页样式，以后若变更为多个样式则陆续添加即可
            mGuideImgView = findViewById(R.id.guidePageImg);
            mGuideEnterImageView = findViewById(R.id.guidePageEnterButton);
        }
    }

    public void setGuideImgView(int background) {
        if (mGuideImgView != null) {
//            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) mGuideImgView.getLayoutParams();
//            params.height = ScreenUtil.getViewHeight(1090);
//            params.setMargins(0, ScreenUtil.getViewHeight(0), 0, 0);
//            mGuideImgView.setLayoutParams(params);
            try {
                Bitmap bitmap = BitmapFactory.decodeStream(getResources().openRawResource(background));
                if (null != bitmap) {
                    mGuideImgView.setImageBitmap(bitmap);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    public void setGuideEnterImageView(int background) {
        if (mGuideEnterImageView != null) {
            mGuideEnterImageView.setVisibility(VISIBLE);
//            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ScreenUtil.getViewWidth(402), ScreenUtil.getViewHeight(162));
//            params.setMargins(0, ScreenUtil.getViewHeight(50), 0, 0);
//            mGuideEnterImageView.setLayoutParams(params);
            mGuideEnterImageView.setBackgroundResource(background);
        }
    }

    public ImageView getGuideEnterImageView() {
        return mGuideEnterImageView;
    }

}