package com.allinlink.platformapp.video_project.ui.frament;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.allinlink.platformapp.video_project.bean.LoginOutput;
import com.tangxiaolv.router.Resolve;
import com.unistrong.utils.AppContextUtils;
import com.allinlink.platformapp.video_project.contract.fragment.PersonFragmentContract;
import com.allinlink.platformapp.video_project.presenter.fragment.PersonFragmentPresenter;
import com.allinlink.platformapp.R;
import com.allinlink.platformapp.video_project.router.DialogUtilRouter;
import com.allinlink.platformapp.video_project.ui.activity.AboutActivity;
import com.allinlink.platformapp.video_project.ui.activity.LoginActivity;
import com.allinlink.platformapp.video_project.ui.activity.OrderActivity;
import com.allinlink.platformapp.video_project.ui.activity.UpPasswordActivity;
import com.allinlink.platformapp.video_project.ui.activity.UserDatumActivity;
import com.allinlink.platformapp.video_project.utils.LoginUtils;
import com.allinlink.platformapp.video_project.utils.UserCache;
import com.allinlink.platformapp.video_project.utils.CacheUtil;
import com.allinlink.platformapp.video_project.widget.CircleImageView;
import com.unistrong.view.base.BaseFragment;

public class PersonFragment extends BaseFragment<PersonFragmentPresenter> implements View.OnClickListener, PersonFragmentContract.View {
    private CircleImageView imgUserHead;
    private TextView tvUserName, tvCache;
    private Button btnQuit;

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mPresenter = new PersonFragmentPresenter(this);
        mPresenter.getUserInfo();
        imgUserHead = getActivity().findViewById(R.id.img_user_head);
        tvUserName = getActivity().findViewById(R.id.tv_user_name);
        tvCache = getActivity().findViewById(R.id.tv_cache);
        btnQuit = getActivity().findViewById(R.id.btn_quit);
        try {
            tvCache.setText(CacheUtil.getTotalCacheSize(AppContextUtils.getAppContext()));
        } catch (Exception e) {
            e.printStackTrace();
        }
//        new ImgModulRouter().displayImage(getContext(), ""
//                , imgUserHead, R.drawable.ic_default_app);

        setViewListener();
    }

    public void setViewListener() {
        getActivity().findViewById(R.id.lin_aboue).setOnClickListener(this);
        getActivity().findViewById(R.id.lin_clear).setOnClickListener(this);
        getActivity().findViewById(R.id.lin_user_password).setOnClickListener(this);
        getActivity().findViewById(R.id.lin_user_datum).setOnClickListener(this);
        getActivity().findViewById(R.id.btn_quit).setOnClickListener(this);
        getActivity().findViewById(R.id.lin_order).setOnClickListener(this);

    }

    @Override
    public void onResume() {
        super.onResume();
        try {
            tvCache.setText(CacheUtil.getTotalCacheSize(AppContextUtils.getAppContext()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected View onCreateContentView() {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_person, null);

        view.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });
        return view;
    }


    @Override
    public void onHiddenChanged(boolean hidden) {
        if (hidden) {

        } else {

        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.lin_order:
                startActivity(new Intent(getContext(), OrderActivity.class));
                break;
            case R.id.lin_user_datum:
                Intent intent = new Intent(getContext(), UserDatumActivity.class);
                startActivity(intent);
                break;
            case R.id.lin_user_password:
                startActivity(new Intent(getContext(), UpPasswordActivity.class));
                break;
            case R.id.lin_aboue:
                startActivity(new Intent(getContext(), AboutActivity.class));
                break;
            case R.id.lin_clear:
                new DialogUtilRouter().showDialogTwoBtn(getContext(), "提示", "是否清理缓存数据？", "取消", "确定").call(new Resolve<Integer>() {
                    @Override
                    public void call(Integer result) {
                        if (result == 1) {
                            CacheUtil.clearAllCache(AppContextUtils.getAppContext());
                            try {
                                tvCache.setText(CacheUtil.getTotalCacheSize(AppContextUtils.getAppContext()));
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }
                });

                break;
            case R.id.btn_quit:
                new DialogUtilRouter().showDialogTwoBtn(getContext(), "提示", "是否退出当前账号？", "取消", "确定").call(new Resolve<Integer>() {
                    @Override
                    public void call(Integer result) {
                        if (result == 1) {
                            LoginUtils.clearUserLoginData();
                            UserCache.loginOutput = null;
                            startActivity(new Intent(getContext(), LoginActivity.class));
                        }
                    }
                });

                break;

        }
    }

    @Override
    public void onError(String msg) {

    }

    @Override
    public void setUserInfo(LoginOutput loginOutput) {
        tvUserName.setText(UserCache.loginOutput.getUserName());
    }
}
