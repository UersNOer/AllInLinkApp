package com.example.android_supervisor.ui.view;

/**
 * @author wujie
 */
public enum ProgressText {
    load("正在加载..."),
    submit("正在提交..."),
    sync("正在同步..."),
    save("正在保存..."),
    login("正在登录..."),
    refreshCase("正在刷新地图数据中..."),
    code("正在获取区域编号...");

    private String text;

    ProgressText(String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return text;
    }
}
