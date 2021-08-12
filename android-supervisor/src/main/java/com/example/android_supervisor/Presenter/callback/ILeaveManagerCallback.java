package com.example.android_supervisor.Presenter.callback;


import com.example.android_supervisor.entities.LeaveTypeModel;

import java.util.ArrayList;
import java.util.List;

public interface ILeaveManagerCallback {
    void getTypeSuccessCallback(List<LeaveTypeModel> data, ArrayList<String> list);

    void submitDataSuccessCallback();
}
