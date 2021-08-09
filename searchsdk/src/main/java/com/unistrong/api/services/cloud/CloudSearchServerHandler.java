package com.unistrong.api.services.cloud;


import android.content.Context;
import android.text.TextUtils;

import com.unistrong.api.services.core.CoreUtil;
import com.unistrong.api.services.core.JsonResultHandler;
import com.unistrong.api.services.core.LatLonPoint;
import com.unistrong.api.services.core.UnistrongException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.Proxy;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

class CloudSearchServerHandler extends
        JsonResultHandler<CloudSearch.Query, CloudSearchResult>{

    public CloudSearchServerHandler(Context context, CloudSearch.Query tsk, Proxy prx, String device) {
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
    protected String[] getRequestLines() throws UnistrongException {
        if (task != null) {
            String[] str = new String[1];
            StringBuilder sb = new StringBuilder();
            try {
                // 入参设置
                if (this.task.isBBoxSearch()){ // bbox
                    if (this.task.getDatasetId()>0 && !isListNull(this.task.getbBox())){
                        sb.append("datasetId=").append(this.task.getDatasetId());
                        sb.append("&bbox=").append(URLEncoder.encode(listToStr(this.task.getbBox()),"UTF-8"));
                    } else {
                        throw new UnistrongException(
                                UnistrongException.ERROR_INVALID_PARAMETER);
                    }
                    if (!TextUtils.isEmpty(this.task.getFilter())){
                        sb.append("&filter=").append(URLEncoder.encode(this.task.getFilter(),"UTF-8"));
                    }
                } else if (this.task.isConditionSearch()){ // 条件检索
                    if (this.task.getDatasetId()>0){
                        sb.append("datasetId=").append(this.task.getDatasetId());
                    } else {
                        throw new UnistrongException(
                                UnistrongException.ERROR_INVALID_PARAMETER);
                    }
                    if (!TextUtils.isEmpty(this.task.getFilter())){
                        sb.append("&filter=").append(URLEncoder.encode(this.task.getFilter(),"UTF-8"));
                    }
                    if (this.task.getPageNumber() > 0){
                        sb.append("&pageNumber=").append(this.task.getPageNumber());
                    }
                    if (this.task.getPageSize() > 0){
                        sb.append("&pageSize=").append(this.task.getPageSize());
                    }
                } else if (this.task.isIdSearch()){ // id检索
                    if (this.task.getDatasetId()>0 && this.task.getId()>0){
                        sb.append("datasetId=").append(this.task.getDatasetId());
                        sb.append("&id=").append(this.task.getId());
                    } else {
                        throw new UnistrongException(
                                UnistrongException.ERROR_INVALID_PARAMETER);
                    }
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
    protected String getUrl() {
        String url = CoreUtil.getCloudUrl(this.context);
        String path="";
        if (this.task.isBBoxSearch()){ // bbox
            path = "/gds/search/bbox?";
        } else if (this.task.isConditionSearch()){ // 条件检索
            path = "/gds/search/condition?";
        } else if (this.task.isIdSearch()){ // id检索
            path = "/gds/search/id?";
        }
        return url+path;
    }

    @Override
    protected CloudSearchResult parseJson(JSONObject obj) throws UnistrongException {
        CloudSearchResult result = new CloudSearchResult();

        try {
            result.setStatus(obj.optInt("status", 0));
            result.setMessage(getJsonStringValue(obj, "message", ""));
            processServerErrorcode(getJsonStringValue(obj, "status", ""),
                    getJsonStringValue(obj, "message", ""));
            // 解析结果
            if (obj.has("results")) {
                JSONObject results = obj.optJSONObject("results");
                if (results != null){
                    if (this.task.isBBoxSearch() || this.task.isConditionSearch()){
                        JSONArray jsonArray = results.optJSONArray("result");
                        List<CloudItem> cloudItems = new ArrayList<>();
                        if (jsonArray != null) {
                            int length = jsonArray.length();
                            for (int i = 0; i < length; i++) {
                                JSONObject jsonObj = jsonArray.getJSONObject(i);
                                CloudItem cloudItem = new CloudItem();
                                // 解析
                                parseCloudItem(jsonObj, cloudItem);
                                cloudItems.add(cloudItem);
                            }
                            result = new CloudSearchResult(this.task, cloudItems);
                            result.setPageNum(results.optInt("pageNum"));
                            result.setPageSize(results.optInt("pageSize"));
                            result.setStartRow(results.optInt("startRow"));
                            result.setEndRow(results.optInt("endRow"));
                            result.setTotal(results.optLong("total"));
                            result.setPages(results.optInt("pages"));
                            result.setMessage(getJsonStringValue(obj, "message", ""));
                        }
                    } else if (this.task.isIdSearch()){
                        List<CloudItem> cloudItems = new ArrayList<>();
                        CloudItem cloudItem = new CloudItem();
                        // 解析
                        parseCloudItem(results, cloudItem);
                        cloudItems.add(cloudItem);

                        result = new CloudSearchResult(this.task, cloudItems);
                        result.setTotal(1);
                        result.setMessage(getJsonStringValue(obj, "message", ""));
                    }

                }
            }
        } catch (UnistrongException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return result;
    }

    private void parseCloudItem(JSONObject obj, CloudItem mapItem) throws JSONException{
        HashMap<String, Object> map = new HashMap<>();

        Iterator iterator = obj.keys();
        while (iterator.hasNext()){
            String key = (String) iterator.next();
            Object value = obj.get(key);
            if ("id".equals(key)){
                mapItem.setId(obj.optLong("id"));
            } else if ("lon".equals(key)){
                mapItem.setLon(obj.optDouble("lon"));
            } else if ("lat".equals(key)){
                mapItem.setLat(obj.optDouble("lat"));
            } else if ("coordinates".equals(key)){
                if (obj.has("coordinates")){
                    String coordinates = obj.optString("coordinates");
                    if (!TextUtils.isEmpty(coordinates)){
                        if (coordinates.contains("(") && coordinates.contains(")")){
                            String dataStr = coordinates.substring(1, coordinates.length()-1);
                            List<LatLonPoint> points = new ArrayList<>();
                            if (dataStr.contains(",")){
                                String[] dataList = dataStr.split(",");
                                for (String data : dataList){
                                    LatLonPoint point = new LatLonPoint(
                                            Double.parseDouble(data.substring(0, data.indexOf(" "))),
                                            Double.parseDouble(data.substring(data.indexOf(" ")+1)));
                                    points.add(point);
                                }
                            } else {
                                LatLonPoint point = new LatLonPoint(
                                        Double.parseDouble(dataStr.substring(0, dataStr.indexOf(" "))),
                                        Double.parseDouble(dataStr.substring(dataStr.indexOf(" ")+1)));
                                points.add(point);
                            }
                            mapItem.setCoordinates(points);
                        }
                    }
                }
            } else if ("adcode".equals(key)){
                mapItem.setAdcode(obj.optString("adcode"));
            } else if ("geoType".equals(key)){
                mapItem.setGeoType(obj.optInt("geoType"));
            } else {
                map.put(key, value);
            }
        }
        mapItem.setExtras(map);
    }

    private boolean isListNull(List list){
        boolean isListNull = false;
        if (list == null || list.isEmpty()){
            isListNull = true;
        } else {
            isListNull = false;
        }
        return isListNull;
    }

    private String listToStr(List<LatLonPoint> points){
        StringBuilder sb = new StringBuilder();
        if (!isListNull(points)){
            int size = points.size();
            for (int i = 0;i<size;i++){
                sb.append(points.get(i).getLongitude()+","+points.get(i).getLatitude());
                if (i != (size-1)){
                    sb.append(";");
                }
            }
        }
        return sb.toString();
    }


}
