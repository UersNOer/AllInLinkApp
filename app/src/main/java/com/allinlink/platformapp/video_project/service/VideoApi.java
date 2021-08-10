package com.allinlink.platformapp.video_project.service;

import com.unistrong.network.api.ApiFactory;
import com.allinlink.platformapp.video_project.config.Configs;

/**
 * video接口
 *
 * @author ltd
 * @date 2020-10-20
 **/
public class VideoApi {

    public static VideoService getVideoService() {
        return ApiFactory.getInstance().getService(Configs.DOMAIN, VideoService.class, true);
    }
}
