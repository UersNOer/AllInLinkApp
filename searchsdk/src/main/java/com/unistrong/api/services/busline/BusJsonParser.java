package com.unistrong.api.services.busline;

import com.unistrong.api.services.core.LatLonPoint;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

class BusJsonParser {
	/**
	 * 线路解析。
	 * 
	 * @param obj
	 * @return BusLineResult 公交线路结果。
	 */
	public BusLineResult busLineParse(JSONObject obj) {
		BusLineResult busLineResult = new BusLineResult();;
		try {

			boolean tag = obj.has("result");
			if (tag == false) {
				return null;
			}
			JSONObject resultObj = obj.getJSONObject("result");
			if (resultObj != null) {
				if (resultObj.has("count")) {
					busLineResult.setTotal(resultObj.getInt("count"));
				} else {
					busLineResult.setTotal(0);
				}

			    if (resultObj.has("lines")) {
			        JSONArray jsonArray = resultObj.getJSONArray("lines");
				    if (jsonArray != null ) {
                        int length = jsonArray.length();
                        List<BusLineItem> busLineItems = new ArrayList<BusLineItem>();
				        for (int i = 0; i < length; i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            BusLineItem busLineItem = new BusLineItem();
                            parseBusLineItem(jsonObject,busLineItem);
                            busLineItems.add(busLineItem);
                        }

				        busLineResult.setBusLines(busLineItems);
				    }
			    }

		    }

	    } catch (Exception e) {
			e.printStackTrace();
		}

		return busLineResult;
	}


    private void parseBusLineItem(JSONObject jsonObject, BusLineItem busLineItem) throws JSONException {
        parseBusLineBasicInfo(jsonObject,busLineItem);
        parseStopInfo(jsonObject,busLineItem);
        //解析公交线路的站点列表。
        List<LatLonPoint> locationList = getPoints(jsonObject);
        busLineItem.setDirectionsCoordinates(locationList);

    }


    private List<LatLonPoint> getPoints(JSONObject jsonObject)throws JSONException{
        List<LatLonPoint> locationList = new ArrayList<LatLonPoint>();
        if (jsonObject.has("coords")) {
            String pathStr = jsonObject.getString("coords");
            if (!"".equals(pathStr)) {
                String[] locations = pathStr.split(";");
                for (String str : locations) {
                    String[] latlon = str.split(",");
                    LatLonPoint location = new LatLonPoint( Double.valueOf(latlon[1]), Double.valueOf(latlon[0]));
                    locationList.add(location);
                }
            }

        }
        return locationList;
    }

    /**
     * 解析公交线路中的站点信息。
     * @param jsonObject
     * @param busLineItem
     */
    private void parseStopInfo(JSONObject jsonObject, BusLineItem busLineItem) throws JSONException {
        if (jsonObject.has("stop_info")) {
            JSONArray stationArray = jsonObject .getJSONArray("stop_info");
            if (stationArray != null ) {
                int len =  stationArray.length();
                List<BusStationItem> busStationItems = new ArrayList<BusStationItem>();
                for (int j = 0; j < len; j++) {
                    try {
                            JSONObject stationObject = stationArray.getJSONObject(j);
                        BusStationItem busStationItem = new BusStationItem();
                            busStationItem.setBusStationName(BusLineServerHandler.getJsonStringValue(stationObject, "stop_name", ""));
                            busStationItem.setOrder(BusLineServerHandler.getJsonStringValue(stationObject, "order", ""));
                            if (stationObject.has("xy")) {
                                String string = stationObject.getString("xy");
                                String[] latlon = string.split(";");
                                LatLonPoint location = new LatLonPoint(Double.valueOf(latlon[1]), Double.valueOf(latlon[0]));
                                busStationItem.setLatLonPoint(location);
                            }
                            busStationItems.add(busStationItem);
                    }catch (Exception ex){
                            if( busLineItem.isLoop() && j == len - 1 ){
                                BusStationItem item = busStationItems.get(0);
                                busStationItems.add(item);
                            }
                    }
                }
                busLineItem.setBusStations(busStationItems);
            }

        }


    }

    /**
     * 解析公交线路的基本信息。
     * @param jsonObject
     * @param busLineItem
     */
    private void parseBusLineBasicInfo(JSONObject jsonObject, BusLineItem busLineItem) throws  JSONException{

        parseBusLineCommonInfo(jsonObject,busLineItem);
        busLineItem.setCityName(BusLineServerHandler .getJsonStringValue(jsonObject, "city_name", ""));
        busLineItem.setBasicPrice(Float .parseFloat(BusLineServerHandler.getJsonStringValue(jsonObject, "basic_price", "")));
        busLineItem.setKeyName(BusLineServerHandler .getJsonStringValue(jsonObject, "key_name", ""));
        busLineItem.setTotalPrice(Float .parseFloat(BusLineServerHandler.getJsonStringValue(jsonObject, "total_price", "")));
        busLineItem.setLastBusTime((BusLineServerHandler .getJsonStringValue(jsonObject, "end_time", "")));
        busLineItem.setBusLineType(BusLineServerHandler .getJsonStringValue(jsonObject, "line_type", ""));
        busLineItem .setBusLineName(BusLineServerHandler.getJsonStringValue(jsonObject, "name", ""));
        busLineItem.setDistance(Float .parseFloat(BusLineServerHandler.getJsonStringValue(jsonObject, "length", "")));
        busLineItem.setFirstBusTime((BusLineServerHandler .getJsonStringValue(jsonObject, "start_time", "")));
        busLineItem.setStationNum(Integer .parseInt(BusLineServerHandler.getJsonStringValue(jsonObject, "stop_num", "")));
        busLineItem.setBusCompany(BusLineServerHandler.getJsonStringValue(jsonObject, "company", ""));
        busLineItem.setCityCode(BusLineServerHandler.getJsonStringValue(jsonObject, "city_code",""));
        busLineItem.setAdcode(BusLineServerHandler.getJsonStringValue(jsonObject, "adcode",""));
        busLineItem.setInterval(BusLineServerHandler.getJsonStringValue(jsonObject, "interval",""));

        String loop =  BusLineServerHandler.getJsonStringValue(jsonObject, "is_loop", "");
        if ("1".equals(loop)){
            busLineItem.setIsLoop(true);
        }else{
            busLineItem.setIsLoop(false);
        }
    }

    private void parseBusLineCommonInfo(JSONObject jsonObject, BusLineItem busLineItem) {
        busLineItem .setOriginatingStation(BusStationServerHandler .getJsonStringValue( jsonObject, "start_name", ""));
	    busLineItem .setTerminalStation(BusStationServerHandler .getJsonStringValue( jsonObject, "end_name", ""));
	    busLineItem .setBusLineId(BusStationServerHandler .getJsonStringValue( jsonObject, "line_id", ""));

    }




    /**
	 * 站点解析。
	 * 
	 * @param obj
	 * @return BusStationResult 站点信息。
	 */
	public BusStationResult busStationParse(JSONObject obj) {
		BusStationResult busStationResult = new BusStationResult();
		try {
			boolean tag = obj.has("results");
			if (tag == false) {
				return null;
			}

            if (obj.has("total")) {
                busStationResult.setTotal(obj.getInt("total"));
            } else {
                busStationResult.setTotal(0);
            }
			JSONArray jsonArray = obj.getJSONArray("results");
			if (jsonArray != null) {
                int length = jsonArray.length();
                List<BusStationItem> busStationItems = new ArrayList<BusStationItem>();
				for (int i = 0; i < length; i++) {
					JSONObject jsonObject = jsonArray.getJSONObject(i);
					BusStationItem busStationItem = new BusStationItem();
                    parseBusStationItem(jsonObject, busStationItem);
					busStationItems.add(busStationItem);

				}
				busStationResult.setBusStations(busStationItems);
			}

		} catch (JSONException e) {
			e.printStackTrace();
		}

		return busStationResult;
	}

    private void parseBusStationItem(JSONObject jsonObject, BusStationItem busStationItem) throws  JSONException {
        parseBusStationBasicInfo(jsonObject, busStationItem);
        parseLineInfo(jsonObject, busStationItem);
        LatLonPoint latLonPointat = getLocation(jsonObject);
        busStationItem.setLatLonPoint(latLonPointat);
    }



    private LatLonPoint getLocation(JSONObject jsonObject)throws JSONException {
        if(jsonObject.has("location")){
            JSONObject location = jsonObject.getJSONObject("location");
            Double lng =Double.parseDouble(BusStationServerHandler.getJsonStringValue(location, "lng", "") ) ;
            Double lat = Double.parseDouble(BusStationServerHandler.getJsonStringValue(location, "lat", "")) ;
            return new LatLonPoint(lat, lng);
        }

        return null;
    }

    private void parseLineInfo(JSONObject jsonObject, BusStationItem busStationItem)throws JSONException {
        JSONArray lineArray = jsonObject .getJSONArray("line_info");
        if(lineArray != null){
            int length = lineArray.length();
            List<BusLineItem> busLineItems = new ArrayList<BusLineItem>();
            for(int j = 0; j < length; j++){
                JSONObject jsonObject2 = lineArray .getJSONObject(j);
                BusLineItem busLineItem = new BusLineItem();
                parseBusLineCommonInfo(jsonObject2,busLineItem);
                busLineItem .setBusLineName(BusStationServerHandler .getJsonStringValue( jsonObject2, "line_name", ""));
                busLineItems.add(busLineItem);
            }
            busStationItem.setBusLineItems(busLineItems);
        }

    }

    private void parseBusStationBasicInfo(JSONObject jsonObject, BusStationItem busStationItem)  throws  JSONException{

        busStationItem.setCitycode(BusStationServerHandler .getJsonStringValue(jsonObject, "citycode", ""));
        busStationItem.setBusStationId(BusStationServerHandler .getJsonStringValue(jsonObject, "id", ""));
        busStationItem.setBusStationName(BusStationServerHandler .getJsonStringValue(jsonObject, "name", ""));
        busStationItem.setAdCode(BusStationServerHandler .getJsonStringValue(jsonObject, "adcode", ""));
        busStationItem.setCity(BusStationServerHandler .getJsonStringValue(jsonObject, "city", ""));
        busStationItem.setCitycode(BusStationServerHandler .getJsonStringValue(jsonObject, "citycode", ""));
        busStationItem.setBusStationType(BusStationServerHandler .getJsonStringValue(jsonObject, "type", ""));

        if (jsonObject.has("is_transfer")) {
            if ("1".equals(jsonObject.getString("is_transfer"))) {

                busStationItem.setIsTransfer(true);// 改站点是否是换乘站
            } else {
                busStationItem.setIsTransfer(false);
            }

        }
    }

}
