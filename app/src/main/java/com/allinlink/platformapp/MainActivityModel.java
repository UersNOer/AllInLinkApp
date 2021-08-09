package com.allinlink.platformapp;

import android.app.Application;

import androidx.annotation.NonNull;

import me.goldze.mvvmhabit.base.BaseViewModel;


public class MainActivityModel extends BaseViewModel {
    private String name;
    public MainActivityModel(@NonNull Application application) {
        super(application);
    }
}
