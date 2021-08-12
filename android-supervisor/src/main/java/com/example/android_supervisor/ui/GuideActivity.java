package com.example.android_supervisor.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.example.android_supervisor.R;
import com.example.android_supervisor.common.UserSession;
import com.example.android_supervisor.utils.OauthHostManager;

import java.util.ArrayList;

public class GuideActivity extends Activity {

    private static final String TAG = GuideActivity.class.getSimpleName();
    private ViewPager viewpager;
    private Button btn_start_main;
    int[] ids = new int[]{R.drawable.guide_1, R.drawable.guide_2, R.drawable.guide_3
    };

    private ArrayList<ImageView> imageViews;


    //http://angrytools.com/android/button/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide);
        viewpager = (ViewPager) findViewById(R.id.viewpager);
        btn_start_main = (Button) findViewById(R.id.btn_start_main);


        imageViews = new ArrayList<>();
        for (int i = 0; i < ids.length; i++) {
            ImageView imageView = new ImageView(this);
            imageView.setBackgroundResource(ids[i]);

            imageViews.add(imageView);

        }

        viewpager.setAdapter(new MyPagerAdapter());

        //根据View的生命周期，当视图执行到onLayout或者onDraw的时候，视图的高和宽，边距都有了
        //iv_red_point.getViewTreeObserver().addOnGlobalLayoutListener(new MyOnGlobalLayoutListener());

        //得到屏幕滑动的百分比
        viewpager.addOnPageChangeListener(new MyOnPageChangeListener());

        btn_start_main.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                UserSession.setGuideStatus(GuideActivity.this, true);
                if (OauthHostManager.getInstance(GuideActivity.this).getConfigStatus()) {
                    // 授权成功->登录页面
                    startActivity(LoginActivity.class);
                } else {
                    // 授权失败->授权页面
                    startActivity(OauthCodeActivity.class);
                }
            }
        });
    }

    class MyOnPageChangeListener implements ViewPager.OnPageChangeListener {

        /**
         * 当页面回调了会回调这个方法
         *
         * @param position             当前滑动页面的位置
         * @param positionOffset       页面滑动的百分比
         * @param positionOffsetPixels 滑动的像数
         */
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        /**
         * 当页面被选中的时候，回调这个方法
         *
         * @param position 被选中页面的对应的位置
         */
        @Override
        public void onPageSelected(int position) {
            if (position == imageViews.size() - 1) {
                //最后一个页面
                btn_start_main.setVisibility(View.VISIBLE);
            } else {
                //其他页面
                btn_start_main.setVisibility(View.GONE);
            }

        }

        /**
         * 当ViewPager页面滑动状态发生变化的时候
         *
         * @param state
         */
        @Override
        public void onPageScrollStateChanged(int state) {

        }
    }

//    class MyOnGlobalLayoutListener implements ViewTreeObserver.OnGlobalLayoutListener {
//
//        @Override
//        public void onGlobalLayout() {
//
//            //执行不只一次
//            iv_red_point.getViewTreeObserver().removeGlobalOnLayoutListener(MyOnGlobalLayoutListener.this);
//
////            间距  = 第1个点距离左边的距离 - 第0个点距离左边的距离
//            leftmax = ll_point_group.getChildAt(1).getLeft() - ll_point_group.getChildAt(0).getLeft();
//            Log.e(TAG, "leftmax==" + leftmax);
//
//        }
//    }


    class MyPagerAdapter extends PagerAdapter {


        @Override
        public int getCount() {
            return imageViews.size();
        }

        /**
         * 作用，getView
         *
         * @param container ViewPager
         * @param position  要创业页面的位置
         * @return 返回和创建当前页面右关系的值
         */
        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            ImageView imageView = imageViews.get(position);
            container.addView(imageView);
            return imageView;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
//            return view ==imageViews.get(Integer.parseInt((String) object));
            return view == object;
        }


        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
//            super.destroyItem(container, position, object);
            container.removeView((View) object);
        }
    }

    private void startActivity(Class activity) {

        Intent intent = new Intent(this, activity);
        startActivity(intent);
        finish();
    }

}