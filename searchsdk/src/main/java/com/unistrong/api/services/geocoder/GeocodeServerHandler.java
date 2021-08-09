package com.unistrong.api.services.geocoder;

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

class GeocodeServerHandler extends
		JsonResultHandler<GeocodeQuery, GeocodeResult> {

	public GeocodeServerHandler(Context context, GeocodeQuery district,
			Proxy prx, String device) {
		super(context, district, prx, device);
	}

	@Override
	protected String[] getRequestLines() {
		if (task != null) {
			String[] str = new String[1];
			StringBuilder sb = new StringBuilder();
            try {
                sb.append("address=").append(URLEncoder.encode(this.task.getLocationName(), "UTF-8"));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            sb.append("&city=").append(this.task.getCity());
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
		url = CoreUtil.getSearchUrl(this.context) + "/geo?";
		return url;
	}

	@Override
	protected boolean getRequestType() {
		return true;
	}

	@Override
	protected GeocodeResult parseJson(JSONObject obj) throws UnistrongException {
		GeocodeResult geocodeResult = new GeocodeResult();
        geocodeResult.setGeocodeQuery(this.task);
		try {
			processServerErrorcode(getJsonStringValue(obj, "status", ""),
					getJsonStringValue(obj, "message", ""));

			if (obj.has("results")) {
				JSONArray jsonArray = obj.getJSONArray("results");
				if (jsonArray != null) {
                   int length =  jsonArray.length();
                    ArrayList<GeocodeAddress> addressList = new ArrayList<GeocodeAddress>();
					for (int i = 0; i < length; i++) {
						JSONObject jsonObject = jsonArray.getJSONObject(i);
						GeocodeAddress geocodeAddress = new GeocodeAddress();
                        parseGeocodeAddress(jsonObject,geocodeAddress);
                        addressList.add(geocodeAddress);
					}

                    geocodeResult.setGeocodeAddressList(addressList);
				}

			}

		} catch (JSONException e) {
            throw new IllegalArgumentException("json解析失败");
		}
		return geocodeResult;
	}

    private void parseGeocodeAddress(JSONObject jsonObject, GeocodeAddress geocodeAddress)throws  JSONException {

        geocodeAddress.setLevel(getJsonStringValue(jsonObject, "level", ""));
        geocodeAddress.setConfidence(getJsonStringValue( jsonObject, "confidence", ""));
        geocodeAddress.setPrecise(getJsonStringValue( jsonObject, "precise", ""));
        LatLonPoint latLonPoint  = getLocation(jsonObject);
        geocodeAddress.setLatLonPoint(latLonPoint);

        parsrAddresses(jsonObject,geocodeAddress);


    }

    private void parsrAddresses(JSONObject jsonObject, GeocodeAddress geocodeAddress) throws  JSONException{
        if(jsonObject.has("addresses")){
            JSONObject jsonObject2 = jsonObject.getJSONObject("addresses");
            geocodeAddress.setCity(getJsonStringValue( jsonObject2, "city", ""));
            geocodeAddress.setProvince(getJsonStringValue( jsonObject2, "province", ""));
            geocodeAddress.setDistrict(getJsonStringValue( jsonObject2, "district", ""));
            geocodeAddress.setFormatAddress(getJsonStringValue( jsonObject2, "name", ""));
            geocodeAddress.setAdCode(getJsonStringValue( jsonObject2, "adcode", ""));
        }

    }

    private LatLonPoint getLocation(JSONObject jsonObject) throws  JSONException{
        if (jsonObject.has("location")) {
            JSONObject jsonLocation = jsonObject .getJSONObject("location");
            Double lat = Double.parseDouble(jsonLocation .getString("lat"));
            Double lng  = Double .parseDouble(jsonLocation.getString("lng"));
            return  new LatLonPoint(lat,lng);
        }

        return null;
    }

}
