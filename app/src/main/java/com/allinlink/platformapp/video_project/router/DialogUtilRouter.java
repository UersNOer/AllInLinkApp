
package com.allinlink.platformapp.video_project.router;

import android.app.Activity;
import android.content.Context;

import com.tangxiaolv.router.AndroidRouter;
import com.tangxiaolv.router.Resolve;
import com.tangxiaolv.router.operators.CPromise;
import com.unistrong.common.config.CommonConfig;

import java.util.HashMap;
import java.util.List;

/**
 * Description:Dialog  Router
 * Created by ltd ON 2020/4/21
 * Phone:18600920091
 * Email:td.liu@unistrong.com
 */
public class DialogUtilRouter {
    public static final String scheme = CommonConfig.SCHEME;

    public static final String host = CommonConfig.VIEW_PROVIDER;

    public static final String path_dialogUtils = CommonConfig.DIALOG_UTILS;

    /**
     * 两个按钮
     *
     * @param title           标题
     * @param message         展示内容
     * @param btnLeftContent  左边按钮文字
     * @param btnRightContent 右边按钮文字
     * @return
     */
    public CPromise showDialogTwoBtn(Context context, String title, String message,
                                     String btnLeftContent, String btnRightContent) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("context", context);
        map.put("title", title);
        map.put("message", message);
        map.put("btnLeftContent", btnLeftContent);
        map.put("btnRightContent", btnRightContent);
        return AndroidRouter.open(scheme, host, path_dialogUtils, map);
    }

    /**
     * 两个按钮
     *
     * @param title   标题
     * @param message 展示内容
     * @return
     */
    public CPromise showDialogOneBtnHaveGetValues(Context context, String title, String message) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("context", context);
        map.put("title", title);
        map.put("message", message);
        return AndroidRouter.open(scheme, host, path_dialogUtils, map);
    }

    /**
     * 一个按钮
     *
     * @param title   标题
     * @param message 展示内容
     */
    public void showDialogOneBtn(final Context context, String title, String message) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("context", context);
        map.put("title", title);
        map.put("message", message);
        AndroidRouter.open(scheme, host, path_dialogUtils, map).call();
    }

    /**
     * 带输入框dialog
     *
     * @param title   标题
     * @param message 展示内容
     * @return
     */
    public CPromise showDialogExistEtText(Context context, String title, String message,
                                          String hintContent, String btnLeftContent, String btnRightContent) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("context", context);
        map.put("title", title);
        map.put("message", message);
        map.put("hintContent", hintContent);
        map.put("btnLeftContent", btnLeftContent);
        map.put("btnRightContent", btnRightContent);
        return AndroidRouter.open(scheme, host, path_dialogUtils, map);
    }


    /**
     * 弹出强制更新对话框
     */
    public CPromise showSafeTimeOutDialog(final Activity activity, final String message) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("context", activity);
        map.put("message", message);
        map.put("btnRightContent", "确定");
        map.put("isVersionUpdate", true);
        return AndroidRouter.open(scheme, host, path_dialogUtils, map);

    }

    /**
     * 两个按钮
     *
     * @param title             标题
     * @param message           展示内容
     * @param btnLeftContent    左边按钮文字
     * @param btnRightContent   右边按钮文字
     * @param btnRightTextColor 右边按钮颜色
     * @return
     */
    public CPromise showDialogTwoBtn(Context context, String title, String message,
                                     String btnLeftContent, String btnRightContent,
                                     int btnRightTextColor) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("context", context);
        map.put("title", title);
        map.put("message", message);
        map.put("btnLeftContent", btnLeftContent);
        map.put("btnRightContent", btnRightContent);
        map.put("btnRightTextColor", btnRightTextColor);
        return AndroidRouter.open(scheme, host, path_dialogUtils, map);
    }


    /**
     * 操作弹窗
     *
     * @param context
     * @param list        存放操作的字符串集合
     * @param colors      对应相应list中字符串的颜色值
     * @param position    0 中间 1 底部
     * @param isNotCancel 点击弹框外部不能取消弹框
     * @param promise
     */
    public void dialogOperate(Context context, List<String> list, List<Integer> colors, int position, boolean isNotCancel, Resolve promise) {
        HashMap<String, Object> params = new HashMap<>();
        params.put("context", context);
        params.put("list", list);
        if (colors != null && !colors.isEmpty()) {
            params.put("colors", colors);
        }
        params.put("position", position);
        params.put("isNotCancel", isNotCancel);
        AndroidRouter.open(scheme, host, "/dialogOperate", params).call(promise);
    }
}
