package com.example.android_supervisor.Presenter.callback;


import com.example.android_supervisor.entities.LeaveRecordModel;

import java.util.List;

public interface ILeaveRecordCallback {
    void getDataSuccessCallback(List<LeaveRecordModel> data);

    void getDataComplete();

}
