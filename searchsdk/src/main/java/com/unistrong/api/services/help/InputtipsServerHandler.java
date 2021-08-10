package com.unistrong.api.services.help;

import android.content.Context;
import android.net.Uri;
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
import java.util.List;

/**
 * 提示查询控制类。
 */
class InputtipsServerHandler extends
		JsonResultHandler<InputtipsQuery, List<Tip>> {

	public InputtipsServerHandler(Context context, InputtipsQuery tsk,
			Proxy prx, String device) {
		super(context, tsk, prx, device);
	}

	@Override
	protected String[] getRequestLines() {
		if (task != null) {
			String[] str = new String[1];
			StringBuilder sb = new StringBuilder();
            try {
                sb.append("query=").append(URLEncoder.encode(this.task.getKeyword(), "UTF-8"));
                sb.append("&region=").append(URLEncoder.encode(this.task.getCity(),"UTF-8"));
				if (!TextUtils.isEmpty(this.task.getDatasource())){
					sb.append("&datasource=").append(Uri.encode(this.task.getDatasource()));
				}
				if (!TextUtils.isEmpty(this.task.getType())){
					sb.append("&datasource=").append(Uri.encode(this.task.getType()));
				}
				if (this.task.getLocation() != null
						&& this.task.getLocation().getLatitude() != 0
						&& this.task.getLocation().getLongitude() != 0)
					sb.append("&location=").append( this.task.getLocation().getLongitude() + ","
							+ this.task.getLocation().getLatitude());
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }

		    sb.append("&scope=").append(this.task.getScope());
		
			str[0] = sb.toString();
			return str;
		}
		return null;
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
		String url = null;
		url = CoreUtil.getSearchUrl(this.context) + "/sug?";
		return url;
	}

	/**
	 * json解析
	 * 
	 * @throws UnistrongException
	 */
	@Override
	protected List<Tip> parseJson(JSONObject obj) throws UnistrongException {
		List<Tip> tipList = new ArrayList<Tip>();
		processServerErrorcode(getJsonStringValue(obj, "status", ""),
				getJsonStringValue(obj, "message", ""));
		try {
			boolean tag = obj.has("results");
			if (tag == false) {
				return null;
			}
			JSONArray resultArray = obj.getJSONArray("results");
            if (resultArray != null){
               int lenth =  resultArray.length();
                for (int i = 0; i < lenth; i++){
                    JSONObject jsonObject = resultArray.getJSONObject(i);
                    Tip tip = new Tip();
                    parseTip(jsonObject,tip);

                    tipList.add(tip);
                }
            }
		} catch (JSONException e) {
			throw new IllegalArgumentException("json解析失败");// e.getLocalizedMessage()
		}
		return tipList;
	}


    private void parseTip(JSONObject jsonObject, Tip tip) throws JSONException{
        tip.setPoiID(getJsonStringValue(jsonObject, "uid", ""));
        tip.setName(getJsonStringValue(jsonObject, "name", ""));
        tip.setAdcode(getJsonStringValue(jsonObject, "adcode", ""));
        tip.setDistrict(getJsonStringValue(jsonObject, "district", ""));
        tip.setType(getJsonStringValue(jsonObject, "type", ""));
        tip.setBusiness(getJsonStringValue(jsonObject, "business", ""));
        tip.setCity(getJsonStringValue(jsonObject, "city", ""));
		tip.setDatasource(getJsonStringValue(jsonObject,"datasource",""));
        tip.setCitycode(getJsonStringValue(jsonObject, "city_code", ""));
        parseLocationAndDetail(jsonObject,tip);
    }

    private void parseLocationAndDetail(JSONObject jsonObject, Tip tip) throws JSONException{
        if (jsonObject.has("location")) {
            JSONObject json = jsonObject.getJSONObject("location");
            if (json != null) {
				String lonstr = getJsonStringValue(json,"lng","");
				String latstr = getJsonStringValue(json,"lat","");
				if(lonstr.length()>0&&latstr.length()>0){
					tip.setPoint(new LatLonPoint(Double.parseDouble(latstr), Double.parseDouble(lonstr)));
				}else{
					tip.setPoint(null);
				}
            } else {
                tip.setPoint(null);
            }

        }

        if (jsonObject.has("detail_info")) {
            JSONObject json =  jsonObject.getJSONObject("detail_info");
            if(json!=null){
                tip.setAddress(getJsonStringValue(json, "address", ""));
                tip.setTelephone(getJsonStringValue(json, "telephone", ""));
            }

        }
    }


}
