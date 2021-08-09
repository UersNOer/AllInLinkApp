package com.unistrong.model.activity.baseui;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import androidx.annotation.StringRes;
import androidx.databinding.ViewDataBinding;
import androidx.fragment.app.Fragment;


import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.reactivex.annotations.NonNull;
import io.reactivex.annotations.Nullable;
import me.goldze.mvvmhabit.base.BaseViewModel;

/**
 * Fragment基类
 * 构造基本逻辑与添加常用方法
 * 未绑定presenter
 */
public abstract class BaseFragmentSimple<V extends ViewDataBinding, VM extends BaseViewModel> extends Fragment  {
    private Unbinder unbinder;

    private boolean isFirstLoad = true; // 是否第一次加载


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(setContentView(), container, false);
        view.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });
        initView(view);
        return view;
    }


    /**
     * 初始化视图
     *
     * @param view
     */
    protected void initView(View view) {
        unbinder = ButterKnife.bind(this,view);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        isFirstLoad = true;
    }



    @Override
    public void onResume() {
        super.onResume();
        if (isFirstLoad) {
            // 将数据加载逻辑放到onResume()方法中
            isFirstLoad = false;
        }
    }


    @Override
    public void onPause() {
        super.onPause();
        //TODO 移除功能
    }

    @Override

    public void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }

    /**
     * 关闭软键盘
     *
     * @param view getWindowToken()
     */

    protected void closeKeyboard(@NonNull View view) {
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    /**
     * @return 默认SharedPreferences
     */

    protected SharedPreferences getSharedPreferences() {
        return getActivity().getSharedPreferences(getString(R.string.app_name), Activity.MODE_PRIVATE);
    }


    /**
     * 弹吐司
     *
     * @param stringRes 文字资源
     */

    protected void showToast(@StringRes int stringRes) {
        Toast.makeText(getContext(), getString(stringRes), Toast.LENGTH_SHORT).show();
    }

    public void showToast(String msg) {
        Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();
    }

    /**
     * 设置contentView
     *
     * @return 布局id
     */

    protected abstract int setContentView();

//    public void startLogin() {
//        showDialog(R.string.dialog_titleNeedLogin,
//                R.string.dialog_login,
//                R.string.goLogin,
//                new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        getActivity().startActivityForResult(
//                                new Intent(getContext(), LoginActivity.class),
//                                REQUEST_LOGIN);
//                    }
//                },
//                R.string.cancel, null, false);
//    }
//    /**
//     * 显示提示信息
//     */
//    public void showDialog(@StringRes int title,
//                           @StringRes int msg,
//                           @StringRes int negative,
//                           DialogInterface.OnClickListener negativeListener,
//                           @StringRes int positive,
//                           DialogInterface.OnClickListener positiveListener,
//                           boolean cancelable) {
//        new AlertDialog.Builder(getContext())
//                .setTitle(title)
//                .setMessage(msg)
//                .setNegativeButton(negative, negativeListener)
//                .setPositiveButton(positive, positiveListener)
//                .setCancelable(cancelable)
//                .show();
//    }
}
