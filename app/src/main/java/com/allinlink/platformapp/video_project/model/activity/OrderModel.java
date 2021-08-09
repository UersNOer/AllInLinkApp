package com.allinlink.platformapp.video_project.model.activity;

import com.unistrong.utils.SharedPreferencesUtil;
import com.allinlink.platformapp.video_project.bean.BaseBean;
import com.allinlink.platformapp.video_project.bean.ChannelBean;
import com.allinlink.platformapp.video_project.bean.OrderBean;
import com.allinlink.platformapp.video_project.contract.activity.OrderContract;
import com.allinlink.platformapp.video_project.presenter.activity.OrderPresenter;
import com.allinlink.platformapp.video_project.service.VideoApi;
import com.unistrong.view.base.BaseModel;

import java.util.HashMap;
import java.util.Map;

import rx.Observable;
import rx.functions.Func1;

public class OrderModel extends BaseModel<OrderPresenter> implements OrderContract.Model {

    public OrderModel(OrderPresenter presenter) {
        super(presenter);
    }

    @Override
    public Observable<BaseBean<OrderBean>> queryOrser(int statue, int page) {
        Map<String, Object> map = new HashMap();
        map.put("currentPage", page);
        map.put("pageSize", 10);
        map.put("acceptor", SharedPreferencesUtil.getInstance().getUserId());
        map.put("status", statue + "");
        return VideoApi.getVideoService().queryOrder(map).filter(new Func1<BaseBean<OrderBean>, Boolean>() {
            @Override
            public Boolean call(BaseBean<OrderBean> loginOutputBaseOutput) {
                return loginOutputBaseOutput != null;
            }
        });
    }
    @Override
    public Observable<BaseBean> closeOrder(OrderBean.DatasBean bean, String resaon) {
        Map<String, Object> map = new HashMap();
        map.put("mark", resaon);
        map.put("gid", bean.getGid());
        map.put("status","1");
        map.put("acceptor", SharedPreferencesUtil.getInstance().getUserId());
        return VideoApi.getVideoService().coloseOrder(map).filter(new Func1<BaseBean, Boolean>() {
            @Override
            public Boolean call(BaseBean loginOutputBaseOutput) {
                return loginOutputBaseOutput != null;
            }
        });
    }
}
