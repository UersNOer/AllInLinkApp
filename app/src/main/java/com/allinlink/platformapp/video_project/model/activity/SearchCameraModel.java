package com.allinlink.platformapp.video_project.model.activity;

import com.unistrong.utils.SharedPreferencesUtil;
import com.allinlink.platformapp.video_project.bean.BaseBean;
import com.allinlink.platformapp.video_project.bean.CameraBean;
import com.allinlink.platformapp.video_project.bean.ChannelBean;
import com.allinlink.platformapp.video_project.contract.activity.SearchCameraContract;
import com.allinlink.platformapp.video_project.presenter.activity.SearchCameraPresenter;
import com.allinlink.platformapp.video_project.service.VideoApi;
import com.allinlink.platformapp.video_project.utils.StringUtil;
import com.unistrong.view.base.BaseModel;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import rx.Observable;
import rx.functions.Func1;

public class SearchCameraModel extends BaseModel<SearchCameraPresenter> implements SearchCameraContract.Model {

    public SearchCameraModel(SearchCameraPresenter presenter) {
        super(presenter);
    }

    @Override
    public Observable<BaseBean<List<ChannelBean>>> getAllCamera(String key, int page) {
        Map<String, Object> map = new HashMap();
        map.put("channelName", key);
//        map.put("currentPage", 1);
//        map.put("pageSize", 10);
        return VideoApi.getVideoService().queryChannelById(map).filter(new Func1<BaseBean<List<ChannelBean>>, Boolean>() {
            @Override
            public Boolean call(BaseBean<List<ChannelBean>> loginOutputBaseOutput) {
                return loginOutputBaseOutput != null;
            }
        });
    }


    @Override
    public Observable<BaseBean> addChannelUserfavorites(ChannelBean data) {
        Map<String, Object> map = new HashMap();
        map.put("passportInfo",SharedPreferencesUtil.getInstance().getUserId());
        map.put("channelInfo",data.getGid());
        map.put("favorTime", StringUtil.simpleDateString(new Date()));
        return VideoApi.getVideoService().addChannelUserfavorites(map).filter(new Func1<BaseBean, Boolean>() {
            @Override
            public Boolean call(BaseBean loginOutputBaseOutput) {
                return loginOutputBaseOutput != null;
            }
        });
    }


    @Override
    public Observable<BaseBean> removeChannelUserfavoritesById(ChannelBean data) {
        Map<String, Object> map = new HashMap();

//        map.put("passportInfo",SharedPreferencesUtil.getInstance().getUserId());
        map.put("gid",data.getGid());
//        map.put("favorTime", StringUtil.simpleDateString(new Date()));
        return VideoApi.getVideoService().removeChannelUserfavoritesById(map).filter(new Func1<BaseBean, Boolean>() {
            @Override
            public Boolean call(BaseBean loginOutputBaseOutput) {
                return loginOutputBaseOutput != null;
            }
        });
    }


}
