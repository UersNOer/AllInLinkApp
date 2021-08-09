package com.allinlink.platformapp.video_project.ui.activity;


import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.ashokvarma.bottomnavigation.BottomNavigationBar;
import com.ashokvarma.bottomnavigation.BottomNavigationItem;
import com.unistrong.skin.CommonSkinUtils;
import com.unistrong.utils.FileUtils;
import com.unistrong.utils.JsonConversionUtil;
import com.unistrong.utils.RxBus;
import com.allinlink.platformapp.R;
import com.allinlink.platformapp.video_project.bean.CameraBean;
import com.allinlink.platformapp.video_project.bean.MainCurruntTab;
import com.allinlink.platformapp.video_project.bean.TabInfoBean;
import com.allinlink.platformapp.video_project.config.Configs;
import com.allinlink.platformapp.video_project.utils.LogUtil;
import com.allinlink.platformapp.video_project.widget.CustomManager;
import com.unistrong.view.base.BaseActivity;
import com.unistrong.view.utils.StatusBarUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import rx.Subscription;
import rx.functions.Action1;

public class UniMainActivity extends BaseActivity implements View.OnClickListener, BottomNavigationBar.OnTabSelectedListener {

    private BottomNavigationBar bottomNavigationBar; //底部导航
    private int curruntTabNum = 0; //默认显示tab索引
    private List<BottomNavigationItem> mBtmItems = new ArrayList<>();//tab集合
    private TabInfoBean tabInfoBean;//tab配置文件解析对象
    private Map<Integer, Object> mFragments = new HashMap<>();//fragment集合
    private FragmentTransaction transaction;//Fragment控制
    private List<Integer> tabPos = new ArrayList<>();//存放点击过的tab位置数

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        initView();
        initTabs();
        setTabBar();
    }

    @Override
    protected void onPause() {
        super.onPause();
        CustomManager.clearAllVideo();
    }

    /**
     * 初始化布局
     */
    public void initView() {
        setContentView(R.layout.activity_main_tab);
        bottomNavigationBar = findViewById(R.id.bottom_navigation_bar);
        bottomNavigationBar.setTabSelectedListener(this);
        Subscription subscription = RxBus.getInstance().toObservable(MainCurruntTab.class)
                .subscribe(new Action1<MainCurruntTab>() {
                    @Override
                    public void call(MainCurruntTab eventBean) {
                        bottomNavigationBar.selectTab(eventBean.position);
                    }
                });
    }


    /**
     * 初始化底部导航
     */
    public void initTabs() {
        //从配置文件拉取配置信息
        String tabJson = FileUtils.getJSONFromAsset(Configs.CONFIG_TAB);
        tabInfoBean = JsonConversionUtil.fromJson(tabJson, TabInfoBean.class);
        //设置底部导航模式
        bottomNavigationBar.setMode(BottomNavigationBar.MODE_FIXED);
        bottomNavigationBar.setBackgroundStyle(BottomNavigationBar.BACKGROUND_STYLE_STATIC);
        //去除冒泡刷新时底部tab的抖动动画
        bottomNavigationBar.setAnimationDuration(0);
    }

    private void setTabBar() {
        int mTabSize = tabInfoBean.getTabInfos().size();
        bottomNavigationBar.clearAll();
        mBtmItems.clear();
        for (int i = 0; i < mTabSize; ++i) {
            BottomNavigationItem btmItem = null;
            btmItem = new BottomNavigationItem(CommonSkinUtils.getDrawableFromPath(tabInfoBean.getTabInfos().get(i).tab_drawable_select),
                    tabInfoBean.getTabInfos().get(i).tabName).setInactiveIcon(CommonSkinUtils.getDrawableFromPath(tabInfoBean.getTabInfos().get(i).tab_drawable_normal))
                    .setActiveColorResource(R.color.c1);
            bottomNavigationBar.addItem(btmItem);
            mBtmItems.add(btmItem);
        }
        bottomNavigationBar
                .setFirstSelectedPosition(curruntTabNum)
                .initialise();
        setCurruntTab(curruntTabNum);

    }


    @Override
    public void onClick(View view) {

    }

    @Override
    public void onTabSelected(int position) {
        curruntTabNum = position;
        setCurruntTab(position);

    }

    @Override
    public void onTabUnselected(int position) {

    }

    @Override
    public void onTabReselected(int position) {

    }

    /**
     * 设置当前tab
     *
     * @param position
     */
    private void setCurruntTab(int position) {
        //修改状态栏颜色
        setStatusBar(position);
        transaction = this.getSupportFragmentManager().beginTransaction();
        Object obj = null;
        try {
            if (mFragments.get(position) != null && mFragments.get(position) instanceof Fragment) {
                obj = mFragments.get(position);
            } else {
                Class cls = Class.forName(tabInfoBean.getTabInfos().get(position).className);
                obj = cls.newInstance();
                mFragments.put(position, obj);
                tabPos.add(position);
            }

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        }
        if (null != obj && !((Fragment) obj).isAdded()) {
            transaction.add(R.id.home_activity_frag_container, (Fragment) obj, obj.getClass().getSimpleName());
        }
        if (tabPos.size() > 0) {
            for (int i = 0; i < tabPos.size(); i++) {
                if (tabPos.get(i) == position) {
                    Fragment fragment = (Fragment) mFragments.get(position);
                    if (fragment != null && fragment.isAdded()) {
                        transaction.show(fragment);
                    }
                } else {
                    Fragment fragment = (Fragment) mFragments.get(tabPos.get(i));
                    if (fragment != null && fragment.isAdded()) {
                        transaction.hide(fragment);
                    }
                }
            }
        }
        transaction.commit();
    }


    /**
     * 修改状态栏颜色
     *
     * @param position tab位置
     */
    protected void setStatusBar(int position) {
        int statusBarColor = 0;
        if (position == 0 || position == 1) {
            statusBarColor = CommonSkinUtils.getColor(this, R.color.navBar_backgroundColor);
        } else {
            statusBarColor = CommonSkinUtils.getColor(this, R.color.white);
        }

        StatusBarUtil.setColorNoTranslucentWithSkin(this, statusBarColor);
    }
    long exitTime;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK
                && event.getAction() == KeyEvent.ACTION_DOWN) {
            if ((System.currentTimeMillis() - exitTime) > 2000) {
                //弹出提示，可以有多种方式
                Toast.makeText(this,"再按一次退出程序",Toast.LENGTH_SHORT).show();
                exitTime = System.currentTimeMillis();
            } else {
                Intent intenn = new Intent();
                intenn.setAction(Intent.ACTION_MAIN);
                intenn.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intenn.addCategory(Intent.CATEGORY_HOME);
                startActivity(intenn);
                android.os.Process.killProcess(android.os.Process.myPid());
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

}
