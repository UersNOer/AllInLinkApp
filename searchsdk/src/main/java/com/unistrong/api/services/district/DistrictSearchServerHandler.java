package com.unistrong.api.services.district;

import android.content.Context;

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

class DistrictSearchServerHandler extends
		JsonResultHandler<DistrictSearchQuery, DistrictResult> {

	public DistrictSearchServerHandler(Context context,
			DistrictSearchQuery district, Proxy prx, String device) {
		super(context, district, prx, device);
	}

	@Override
	protected String[] getRequestLines() {
		if (task != null) {
			String[] str = new String[1];
			StringBuilder sb = new StringBuilder();
            try {
                sb.append("query=").append(URLEncoder.encode(this.task.getKeywords(), "UTF-8"));
                if(!"".equals(this.task.getCity()) && this.task.getCity() !=null){
                    sb.append("&city=").append(URLEncoder.encode(this.task.getCity(),"UTF-8"));

                }
                if(!"".equals(this.task.getKeywordsLevel()) && this.task.getKeywordsLevel() !=null){
                    sb.append("&level=").append(this.task.getKeywordsLevel());
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
	protected int getServiceCode() {
		return 20;
	}

	@Override
	protected String getUrl() {
		String url = null;
		url = CoreUtil.getSearchUrl(this.context)+ "/search/district?";
		return url;
	}

	@Override
	protected boolean getRequestType() {
		return true;
	}

	/**
	 * 行政区划json解析
	 */
	@Override
	protected DistrictResult parseJson(JSONObject obj) throws UnistrongException {
		DistrictResult districtResult = new DistrictResult();
		try {
			processServerErrorcode(getJsonStringValue(obj, "status", ""),
			getJsonStringValue(obj, "message", ""));

			if (obj.has("results")) {
				JSONArray jsonArray = obj.getJSONArray("results");
                ArrayList<DistrictItem> districtList = new ArrayList<DistrictItem>();
				if (jsonArray != null) {
                    int length = jsonArray.length();
					for (int i = 0; i < length; i++) {
						JSONObject jsonObj = jsonArray.getJSONObject(i);
						DistrictItem districtItem = new DistrictItem();
                        parseDistrict(jsonObj,districtItem);
						districtList.add(districtItem);
					}
					districtResult.setDistrict(districtList);
				}
			}

		} catch (JSONException e) {
			throw new IllegalArgumentException("json解析失败");// e.getLocalizedMessage()
		}
		return districtResult;

	}


    private void parseDistrict(JSONObject obj, DistrictItem districtItem) throws JSONException{

        districtItem.setLevel(getJsonStringValue(obj, "level", ""));
        districtItem.setCitycode(getJsonStringValue(obj, "citycode", ""));
        districtItem.setName(getJsonStringValue(obj, "name", ""));
        districtItem.setAdcode(getJsonStringValue(obj, "adcode", ""));
		districtItem.setBounds(getJsonStringValue(obj, "bounds", ""));

        if (obj.has("polyline")) {
            String polyline = obj.getString("polyline");
            if(!polyline.equals("")){
                if(polyline.contains("|")){
                    String[] bounds = polyline.split("\\|");
                    districtItem.setDistrictBoundary(bounds);
                }else{
                    String[] bounds = {polyline};
                    districtItem.setDistrictBoundary(bounds);
                }
            }
        }

        if (obj.has("center")) {
            JSONObject id = obj.getJSONObject("center");
            districtItem.setCenter(new LatLonPoint(Double
                    .parseDouble(id.getString("lat")), Double
                    .parseDouble(id.getString("lng"))));
        }


    }

}
