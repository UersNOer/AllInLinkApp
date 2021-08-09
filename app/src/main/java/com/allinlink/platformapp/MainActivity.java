package com.allinlink.platformapp;

import android.os.Bundle;
import android.view.View;

import androidx.lifecycle.ViewModelProviders;

import com.allinlink.platformapp.databinding.ActivityMainBinding;
import com.unistrong.model.activity.baseui.BaseActivity;

import me.goldze.mvvmhabit.base.BaseViewModel;

public class MainActivity extends BaseActivity<ActivityMainBinding, MainActivityModel> {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public int initContentView(Bundle savedInstanceState) {
        return R.layout.activity_main;
    }

    @Override
    public int initVariableId() {
        return BR.viewModel;
    }

    @Override
    public BaseViewModel initViewModel() {
       return ViewModelProviders.of(this).get(MainActivityModel.class);
    }
}