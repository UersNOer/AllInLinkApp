package com.allinlink.platformapp.video_project.contract.fragment;

import com.unistrong.common.contract.IBaseExtraView;
import com.allinlink.platformapp.video_project.bean.ChannelBean;
import com.allinlink.platformapp.video_project.bean.PlayBackBean;

public class DataFragmentContract {

    public interface View extends IBaseExtraView<Presenter> {
        public void setHistotyVideo(PlayBackBean bean);
    }

    public interface Model {


    }

    interface Presenter {


    }
}
