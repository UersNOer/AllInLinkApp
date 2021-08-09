package com.unistrong.model.activity.baseui;

import android.os.Bundle;

import androidx.databinding.ViewDataBinding;

import io.reactivex.annotations.Nullable;
import me.goldze.mvvmhabit.base.BaseViewModel;


/**
 * Created by Administrator on 2017/8/4.
 * Fragment基类
 * 绑定Presenter
 *
 * @param <T>
 */

public abstract class BaseFragment<V extends ViewDataBinding, VM extends BaseViewModel> extends me.goldze.mvvmhabit.base.BaseFragment {
    protected V binding;
    protected VM viewModel;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.binding=binding;
        super.viewModel=viewModel;

    }

}
