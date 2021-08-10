package com.unistrong.api.services.cloud;

import android.content.Context;

import com.unistrong.api.services.core.CoreUtil;
import com.unistrong.api.services.core.JsonResultHandler;
import com.unistrong.api.services.core.LatLonPoint;
import com.unistrong.api.services.core.UnistrongException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.Proxy;
import java.util.Map;



class CloudStorageUpdataServerHandler extends JsonResultHandler<Object,CloudStorageResult> {
    private String path="/gds/storage/data/update?";
    /**
     * 使用Json解析返回结果。
     *
     * @param context
     * @param tsk
     * @param prx
     * @param device
     */
    public CloudStorageUpdataServerHandler(Context context, Object tsk, Proxy prx, String device) {
        super(context, tsk, prx, device);
    }

    @Override
    protected CloudStorageResult parseJson(JSONObject obj) throws UnistrongException {
        CloudStorageResult result=null;result=new CloudStorageResult();
        try {
            result.setStatus(getJsonStringValue(obj, "status", ""));
            result.setMessage(getJsonStringValue(obj, "message", ""));
//            processServerErrorcode(getJsonStringValue(obj, "status", ""),
//                    getJsonStringValue(obj, "message", ""));
            // 解析结果
            if (obj.has("results")) {
                JSONObject results = obj.optJSONObject("results");
                if (results != null) {

                    JSONArray jsonArray = results.optJSONArray("ids");
                    if(jsonArray!=null&&jsonArray.length()>0) {
                        long[] ids=new long[jsonArray.length()];
                        for (int i = 0; i < jsonArray.length(); i++) {
                            ids[i] = jsonArray.getLong(i);
                        }
                        result.setId(ids);
                    }
                }
            }
//        } catch (UnistrongException e){
//            return result;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    protected boolean getRequestType() {
        return false;
    }

    @Override
    protected int getServiceCode() {
        return 0;
    }


    @Override
    protected String[] getRequestLines() {

        if (task != null) {
            CloudStorage.Updata addObject=(CloudStorage.Updata)task;
            String[] str = new String[1];
            StringBuilder sb = new StringBuilder();
            try {
                // 入参设置
                if (addObject!=null){
                    sb.append("datasetId=").append(addObject.getDataSetId());

                    //拼data json字符串
                    StringBuffer dataJson=new StringBuffer();
                    CloudItem[] items= addObject.getCloudItems();
                    dataJson.append("[");
                    for(int j=0;j<items.length;j++){
                        if(j!=0)dataJson.append(",");
                        CloudItem item=items[j];
                        dataJson.append("{");
                        String coords="\"coordinates\":";
                        if(item.getCoordinates().size()>0){
                            String coord="";
                            for(int i=0;i<item.getCoordinates().size();i++){
                                LatLonPoint point=item.getCoordinates().get(i);
                                if(i!=0)coord+=",";
                                coord+=point.getLongitude()+" "+point.getLatitude();
                            }
                            coords+="\""+coord+"\"";
                        }else {
                            coords+="\""+item.getLon()+" "+item.getLat()+"\"";
                        }
                        dataJson.append(coords);


                        //  自定义字段
                        for(Map.Entry<String ,Object> entry:item.getExtras().entrySet()){
                            dataJson.append(",");
                            String  st="\""+entry.getKey()+"\":\""+entry.getValue().toString()+"\"";
                            dataJson.append(st);
                        }
                        dataJson.append("}");

                    }
                    dataJson.append("]");
                    sb.append("&data=").append(dataJson);

                    StringBuffer idsString=new StringBuffer();
                    long[] ids= addObject.getDataIds();
                    for(int i=0;i<ids.length;i++){
                        if(i!=0) idsString.append(",");
                        idsString.append(ids[i]);
                    }
                    sb.append("&ids=").append(idsString);


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
