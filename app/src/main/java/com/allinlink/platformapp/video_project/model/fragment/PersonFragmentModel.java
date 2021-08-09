package com.allinlink.platformapp.video_project.model.fragment;

import com.unistrong.utils.SharedPreferencesUtil;
import com.allinlink.platformapp.video_project.bean.BaseBean;
import com.allinlink.platformapp.video_project.bean.LoginOutput;
import com.allinlink.platformapp.video_project.contract.fragment.PersonFragmentContract;
import com.allinlink.platformapp.video_project.presenter.fragment.PersonFragmentPresenter;
import com.allinlink.platformapp.video_project.service.VideoApi;
import com.unistrong.view.base.BaseModel;

import java.util.HashMap;

import rx.Observable;
import rx.functions.Func1;

/**
 * @author ltd
 * @description: 登录model
 * @date 2020-05-28
 **/
public class PersonFragmentModel extends BaseModel<PersonFragmentPresenter> implements PersonFragmentContract.Model {

    public PersonFragmentModel(PersonFragmentPresenter presenter) {
        super(presenter);
    }

    @Override
    public Observable<BaseBean<LoginOutput>> getUserInfo() {
        HashMap<String, Object> map = new HashMap<>();
//        map.put("token", SharedPreferencesUtil.getInstance().getToken());
        return VideoApi.getVideoService().getUserInfo(map).filter(new Func1<BaseBean, Boolean>() {
            @Override
            public Boolean call(BaseBean loginOutputBaseOutput) {
                return loginOutputBaseOutput != null;
            }
        });
    }


}
