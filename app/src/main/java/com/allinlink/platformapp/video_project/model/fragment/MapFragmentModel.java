package com.allinlink.platformapp.video_project.model.fragment;

import com.unistrong.utils.SharedPreferencesUtil;
import com.allinlink.platformapp.video_project.bean.BaseBean;
import com.allinlink.platformapp.video_project.bean.CameraBean;
import com.allinlink.platformapp.video_project.bean.ChannelBean;
import com.allinlink.platformapp.video_project.bean.MapMarkerBean;
import com.allinlink.platformapp.video_project.contract.fragment.MapFragmentContract;
import com.allinlink.platformapp.video_project.presenter.fragment.MapFragmentPresenter;
import com.allinlink.platformapp.video_project.service.VideoApi;
import com.allinlink.platformapp.video_project.utils.StringUtil;
import com.allinlink.platformapp.video_project.utils.UserCache;
import com.unistrong.view.base.BaseModel;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import rx.Observable;
import rx.functions.Func1;

/**
 * @author ltd
 * @description: 登录model
 * @date 2020-05-28
 **/
public class MapFragmentModel extends BaseModel<MapFragmentPresenter> implements MapFragmentContract.Model {

    public MapFragmentModel(MapFragmentPresenter presenter) {
        super(presenter);
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



    @Override
    public Observable<BaseBean<MapMarkerBean>> getMapMarker() {
        Map<String, Object> map = new HashMap();
        return VideoApi.getVideoService().getMapMarker(map).filter(new Func1<BaseBean<MapMarkerBean>, Boolean>() {
            @Override
            public Boolean call(BaseBean<MapMarkerBean> loginOutputBaseOutput) {
                return loginOutputBaseOutput != null;
            }
        });
    }

    @Override
    public Observable<BaseBean<List<ChannelBean>>> getAllCamera() {
        Map<String, Object> map = new HashMap();
        //iot/api/findAllChannelInfoInList
        return VideoApi.getVideoService().queryChannelById(map).filter(new Func1<BaseBean<List<ChannelBean>>, Boolean>() {
            @Override
            public Boolean call(BaseBean<List<ChannelBean>> loginOutputBaseOutput) {
                return loginOutputBaseOutput != null;
            }
        });
    }

    @Override
    public Observable<BaseBean> addUMengToken(String token) {
        Map<String, Object> map = new HashMap();
        map.put("gid", SharedPreferencesUtil.getInstance().getUserId());
        map.put("appCode", token);
        return VideoApi.getVideoService().addUMengToken(map).filter(new Func1<BaseBean, Boolean>() {
            @Override
            public Boolean call(BaseBean loginOutputBaseOutput) {
                return loginOutputBaseOutput != null;
            }
        });
    }
}
