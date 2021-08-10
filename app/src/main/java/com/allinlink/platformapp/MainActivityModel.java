package com.allinlink.platformapp;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;

import java.util.HashMap;

import me.goldze.mvvmhabit.base.BaseViewModel;
import me.goldze.mvvmhabit.binding.command.BindingAction;
import me.goldze.mvvmhabit.binding.command.BindingCommand;


public class MainActivityModel extends BaseViewModel {
    private String name;

    public MainActivityModel(@NonNull Application application) {
        super(application);
    }

    //登录按钮的点击事件
    public BindingCommand loginOnClickCommand = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            Log.i("TAG", "点击了");
            AroutUtils.router("VideoAppLoginActivity", new HashMap<>());

        }
    });


}
