package com.unistrong.api.services.poisearch;


import android.content.Context;
import android.text.TextUtils;

import com.unistrong.api.services.core.CoreUtil;
import com.unistrong.api.services.core.JsonResultHandler;
import com.unistrong.api.services.core.UnistrongException;

import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.Proxy;
import java.net.URLEncoder;

class ComplexSearchServerHanler extends JsonResultHandler<ComplexSearch.Query, ComplexSearchResult>{


    /**
     * 使用Json解析返回结果。
     *
     * @param context
     * @param tsk
     * @param prx
     * @param device
     */
    public ComplexSearchServerHanler(Context context, ComplexSearch.Query tsk, Proxy prx, String device) {
        super(context, tsk, prx, device);
    }

    @Override
    protected boolean getRequestType() {
        return true;
    }

    @Override
    protected int getServiceCode() {
        return 20;
    }

    @Override
    protected String getUrl() {
        String url = CoreUtil.getSearchUrl(this.context);
        String path = "/online/search/detail?";

        return url+path;
    }

    @Override
    protected String[] getRequestLines() throws UnistrongException {
        if (task != null) {
            String[] str = new String[1];
            StringBuilder sb = new StringBuilder();

            try {
                if (!TextUtils.isEmpty(this.task.getDatasource())){
                    sb.append("datasource=").append(URLEncoder.encode(this.task.getDatasource(),"UTF-8"));
                } else {
                    throw new UnistrongException("datasource不能为空!");
                }
                if (!TextUtils.isEmpty(this.task.getQuery()) && !TextUtils.isEmpty(this.task.getType())){
                    throw new UnistrongException("query和type二者不可同时存在!");
                } else {
                    if (!TextUtils.isEmpty(this.task.getQuery())){
                        sb.append("&query=").append(URLEncoder.encode(this.task.getQuery(),"UTF-8"));
                    }
                    if (!TextUtils.isEmpty(this.task.getType())){
                        sb.append("&type=").append(URLEncoder.encode(this.task.getType(),"UTF-8"));
                    }
                }
                if (!TextUtils.isEmpty(this.task.getLocation()) && !TextUtils.isEmpty(this.task.getRegion())){
                    throw new UnistrongException("location和region二者不可同时存在!");
                } else {
                    if (!TextUtils.isEmpty(this.task.getLocation())){
                        sb.append("&location=").append(URLEncoder.encode(this.task.getLocation(),"UTF-8"));
                    }
                    if (!TextUtils.isEmpty(this.task.getRegion())){
                        sb.append("&region=").append(URLEncoder.encode(this.task.getRegion(),"UTF-8"));
                    }
                }
                if (this.task.getRadius()>0){
                    sb.append("&radius=").append(this.task.getRadius());
                }
                if (this.task.getPageSize()>0){
                    sb.append("&page_size=").append(this.task.getPageSize());
                }
                if (this.task.getPageNum()>0){
                    sb.append("&page_num=").append(this.task.getPageNum());
                }
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }

            str[0] = sb.toString();
            return str;
        }

        return null;
    }

    @Override
    protected ComplexSearchResult parseJson(JSONObject obj) throws UnistrongException {
        ComplexSearchResult result = new ComplexSearchResult();

        result.setStatus(obj.optInt("status", 0));
        result.setMessage(getJsonStringValue(obj, "message", ""));
        processServerErrorcode(getJsonStringValue(obj, "status", ""),
                getJsonStringValue(obj, "message", ""));
        result = new ComplexSearchParser().parser(this.task, obj);

        return result;
    }
}
