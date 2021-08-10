package com.allinlink.platformapp.video_project.contract.activity;

import com.unistrong.common.contract.IBaseExtraView;
import com.allinlink.platformapp.video_project.bean.BaseBean;
import com.allinlink.platformapp.video_project.bean.OrderBean;

import java.util.List;

import rx.Observable;

public interface OrderContract {
    public interface View extends IBaseExtraView<OrderContract.Presenter> {

        void setOrderData(List<OrderBean.DatasBean> datas);

        void closeSuccess();
    }

    public interface Model {
        Observable<BaseBean<OrderBean>> queryOrser(int statue,int page);
        Observable<BaseBean> closeOrder(OrderBean.DatasBean bean, String resaon);
    }

    interface Presenter {


    }
}
