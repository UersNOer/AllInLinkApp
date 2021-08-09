package com.unistrong.api.services.geocoder;

import android.content.Context;
import android.net.Uri;

import com.unistrong.api.services.core.CoreUtil;
import com.unistrong.api.services.core.JsonResultHandler;
import com.unistrong.api.services.core.LatLonPoint;
import com.unistrong.api.services.core.UnistrongException;
import com.unistrong.api.services.poisearch.PoiItem;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.Proxy;
import java.util.ArrayList;
import java.util.List;

class RegeocodeServerHandler extends
		JsonResultHandler<RegeocodeQuery, RegeocodeResult> {

	public RegeocodeServerHandler(Context context, RegeocodeQuery district,
			Proxy prx, String device) {
		super(context, district, prx, device);
	}

	@Override
	protected String[] getRequestLines() {
		if (task != null) {
			String[] str = new String[1];
			StringBuilder sb = new StringBuilder();
            String location = "";

            if(this.task.getPoint()!=null && this.task.getPoint().getLongitude()!=0 && this.task.getPoint().getLatitude()!=0){
                sb.append("location=").append(this.task.getPoint().getLongitude() + "," + this.task.getPoint().getLatitude());

            }else if(!isNullString(getLocationString(this.task.getPoints()))){
               sb.append("&location=").append( getLocationString(this.task.getPoints()));//地理坐标集合

            }

            if(this.task.isShow()){
                    sb.append("&pois=").append(1);
            } else {
                    sb.append("&pois=").append(0);
            }
            if (this.task.getRadius() > 0){
                sb.append("&radius=").append(this.task.getRadius());
            }
            if (!isNullString(this.task.getType())){
                sb.append("&type=").append(Uri.encode(this.task.getType()));
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
		url = CoreUtil.getSearchUrl(this.context) + "/rgeo?";
		return url;
	}


    private boolean isNullString(String str) {
        if (null == str || str.equals("")) {
            return true;
        } else {
            return false;
        }
    }


    private String getLocationString(List<LatLonPoint> list) {
        String str = "";
        if (list == null)
            return str;

        int size =  list.size();
        for (int i = 0; i < size; i++) {
            LatLonPoint location = list.get(i);
            double longitude = location.getLongitude();
            double latitude = location.getLatitude();

            if (i == size-1||size==0) {
                str += longitude + "," + latitude;
            } else {
                str += longitude + "," + latitude +";";
            }
        }
        return str;
    }

	@Override
	protected boolean getRequestType() {
		return true;
	}

	@Override
	protected RegeocodeResult parseJson(JSONObject obj) throws UnistrongException {
		RegeocodeResult geocodeResult = new RegeocodeResult();
        geocodeResult.setRegeocodeQuery(this.task);
		try {
			processServerErrorcode(getJsonStringValue(obj, "status", ""),
					getJsonStringValue(obj, "message", ""));

			JSONArray resultArray = obj.getJSONArray("result");
			if (resultArray != null) {
                List<RegeocodeAddress> addressList = new ArrayList<RegeocodeAddress>();
				for (int i = 0; i < resultArray.length(); i++) {
					JSONObject jsonObj = resultArray.getJSONObject(i);
					RegeocodeAddress regeocodeAddress = new RegeocodeAddress();

                    parseRegeocodeAddress(jsonObj,regeocodeAddress);
					addressList.add(regeocodeAddress);
				}
				geocodeResult.setRegeocodeAddressList(addressList);
			}

		} catch (JSONException e) {
            throw new IllegalArgumentException("json解析失败");
		}
		return geocodeResult;
	}


    private void parseRegeocodeAddress(JSONObject jsonObj, RegeocodeAddress regeocodeAddress) throws  JSONException{
        regeocodeAddress.setFormatAddress(getJsonStringValue( jsonObj, "formatted_address", ""));
        parseAddressComponent(jsonObj,regeocodeAddress);
        parsePOI(jsonObj,regeocodeAddress);
        LatLonPoint latLonPoint =  getLocation(jsonObj);
        regeocodeAddress.setLocation(latLonPoint);
    }

    private LatLonPoint getLocation(JSONObject jsonObject) throws  JSONException{
        if (jsonObject.has("location")) {
            JSONObject jsonLocation = jsonObject .getJSONObject("location");
            Double lat = Double.parseDouble(jsonLocation .getString("lat").equals("")?"0":jsonLocation .getString("lat"));
            Double lng  = Double .parseDouble(jsonLocation.getString("lng").equals("")?"0":jsonLocation .getString("lng"));
            return  new LatLonPoint(lat,lng);
        }

        return null;
    }

    private void parsePOI(JSONObject jsonObj, RegeocodeAddress regeocodeAddress) throws  JSONException{
        if (jsonObj.has("pois")) {
            JSONArray jsonArray = jsonObj.getJSONArray("pois");
            if(jsonArray!=null){
                int length= jsonArray.length();
                List<PoiItem> poiList = new ArrayList<PoiItem>();
                for (int j = 0; j < length; j++){
                    JSONObject jsonObject = jsonArray.getJSONObject(j);
                    PoiItem poiItem = new PoiItem();

                    poiItem.setPoiId(getJsonStringValue(jsonObject, "uid", ""));
                    poiItem.setDistance(Double .parseDouble(getJsonStringValue(jsonObject,  "distance", "")));
                    poiItem.setTitle(getJsonStringValue(jsonObject, "name", ""));
                    poiItem.setTel(getJsonStringValue(jsonObject, "tel", ""));
                    poiItem.setSnippet(getJsonStringValue(jsonObject, "addr", ""));
                    String point = getJsonStringValue(jsonObject, "point", "");
                    String[] latlon = point.split(",");
                    poiItem.setLatLonPoint(new LatLonPoint(Double.parseDouble(latlon[1]),Double.parseDouble(latlon[0])));
                    poiList.add(poiItem);

                }
                regeocodeAddress.setPois(poiList);

            }
        }

    }

    private void parseAddressComponent(JSONObject jsonObj, RegeocodeAddress regeocodeAddress) throws  JSONException{
        if (jsonObj.has("addressComponent")) {
            JSONObject jsonObject = jsonObj .getJSONObject("addressComponent");
            regeocodeAddress.setDistance(Float .parseFloat(getJsonStringValue(jsonObject, "distance", "").equals("")?"0":getJsonStringValue(jsonObject, "distance", "")));
            regeocodeAddress.setStreetName(getJsonStringValue( jsonObject, "street", ""));
            regeocodeAddress.setProvince(getJsonStringValue( jsonObject, "province", ""));
            regeocodeAddress.setStreetNumber(getJsonStringValue( jsonObject, "street_number", ""));
            regeocodeAddress.setDistrict(getJsonStringValue( jsonObject, "district", ""));
            regeocodeAddress.setCity(getJsonStringValue(jsonObject, "city", ""));
            regeocodeAddress.setCityCode(getJsonStringValue( jsonObject, "cityCode", ""));
            regeocodeAddress.setAdCode(getJsonStringValue( jsonObject, "adCode", ""));

        }
    }

}
