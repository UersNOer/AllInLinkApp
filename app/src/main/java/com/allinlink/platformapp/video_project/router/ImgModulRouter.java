package com.allinlink.platformapp.video_project.router;

import android.content.Context;
import android.widget.ImageView;

import com.tangxiaolv.router.AndroidRouter;
import com.unistrong.common.config.CommonConfig;

import java.util.HashMap;
import java.util.Map;

/**
 * 图片加载模块Router
 *
 * @author ltd
 * @date 2020/6/5
 **/
public class ImgModulRouter {

    /**
     * 显示图片
     *
     * @param context   上下文
     * @param url       图片链接
     * @param imageView imageview
     * @param defRes    默认图片
     */
    public void displayImage(Context context, String url, ImageView imageView, int defRes) {
        Map map = new HashMap();
        map.put("context", context);
        map.put("url", url);
        map.put("imageView", imageView);
        map.put("defRes", defRes);
        AndroidRouter.open(CommonConfig.SCHEME, CommonConfig.IMAGE_PROVIDER, CommonConfig.DISPLAY_IMAGE, map).call();
    }
}