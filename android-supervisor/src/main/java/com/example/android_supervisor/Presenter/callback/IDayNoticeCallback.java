package com.example.android_supervisor.Presenter.callback;


import com.example.android_supervisor.entities.DayNoticeModel;

import java.util.List;

public interface IDayNoticeCallback {
    void getDataSuccessCallback(List<DayNoticeModel> list);

    void getDataComplete();

    void submitSuccessCallback();

}
