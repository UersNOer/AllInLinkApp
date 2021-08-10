package com.unistrong.api.services.cloud;

import android.content.Context;

import com.unistrong.api.services.core.CoreUtil;
import com.unistrong.api.services.core.JsonResultHandler;
import com.unistrong.api.services.core.UnistrongException;

import org.json.JSONObject;

import java.net.Proxy;



class CloudStorageDelSetServerHandler extends JsonResultHandler<Object,CloudStorageResult> {
    private String path="/gds/storage/dataSet/delete?";
    /**
     * 使用Json解析返回结果。
     *
     * @param context
     * @param tsk
     * @param prx
     * @param device
     */
    public CloudStorageDelSetServerHandler(Context context, Object tsk, Proxy prx, String device) {
        super(context, tsk, prx, device);
    }

    @Override
    protected CloudStorageResult parseJson(JSONObject obj) throws UnistrongException {
        CloudStorageResult result=null;

            result=new CloudStorageResult();
            result.setStatus(getJsonStringValue(obj, "status", ""));
            result.setMessage(getJsonStringValue(obj, "message", ""));

        return result;
    }

    @Override
    protected boolean getRequestType() {
        return true;
    }

    @Override
    protected int getServiceCode() {
        return 0;
    }

    @Override
    protected String[] getRequestLines() {
        if (task != null) {
            CloudStorage.Del addObject=(CloudStorage.Del)task;
            String[] str = new String[1];
            StringBuilder sb = new StringBuilder();
            try {
                // 入参设置
                if (addObject!=null){
                    sb.append("id=").append(addObject.getDataSetId());
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
            str[0] = sb.toString();
            return str;
        }
        return null;
    }

    @Override
    protected String getUrl() {
        return  CoreUtil.getCloudUrl(this.context)+path;
    }
}
