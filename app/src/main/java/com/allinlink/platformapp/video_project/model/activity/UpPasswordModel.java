package com.allinlink.platformapp.video_project.model.activity;

import com.unistrong.utils.MD5;
import com.unistrong.utils.SharedPreferencesUtil;
import com.allinlink.platformapp.video_project.bean.BaseBean;
import com.allinlink.platformapp.video_project.contract.activity.UpPasswordContract;
import com.allinlink.platformapp.video_project.presenter.activity.UpPasswordPresenter;
import com.allinlink.platformapp.video_project.service.VideoApi;
import com.unistrong.view.base.BaseModel;

import java.util.HashMap;
import java.util.Map;

import rx.Observable;
import rx.functions.Func1;

public class UpPasswordModel extends BaseModel<UpPasswordPresenter> implements UpPasswordContract.Model {

    public UpPasswordModel(UpPasswordPresenter presenter) {
        super(presenter);
    }


    @Override
    public Observable<BaseBean> submitPassword(String oldPassword, String newPassword) {
        Map<String, Object> map = new HashMap();
        map.put("gid", SharedPreferencesUtil.getInstance().getUserId());
        map.put("passportPsw", MD5.getMD5Code(oldPassword).toUpperCase());
        map.put("passportPswNew", MD5.getMD5Code(newPassword).toUpperCase());
        return VideoApi.getVideoService().updatePassword(map).filter(new Func1<BaseBean, Boolean>() {
            @Override
            public Boolean call(BaseBean loginOutputBaseOutput) {
                return loginOutputBaseOutput != null;
            }
        });

    }

}
