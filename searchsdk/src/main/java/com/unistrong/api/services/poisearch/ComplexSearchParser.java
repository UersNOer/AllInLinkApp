package com.unistrong.api.services.poisearch;

import com.unistrong.api.services.busline.BusLineItem;
import com.unistrong.api.services.busline.BusStationItem;
import com.unistrong.api.services.core.LatLonPoint;
import com.unistrong.api.services.district.DistrictItem;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;


class ComplexSearchParser {

    public ComplexSearchResult parser(final ComplexSearch.Query query, JSONObject rootObject) {

        ComplexSearchResult resultData = null;
        List<BusStationItem> busItems = new ArrayList<BusStationItem>();
        List<BusLineItem> busLineItems = new ArrayList<BusLineItem>();
        List<PoiItem> poiItems = new ArrayList<PoiItem>();
        List<DistrictItem> districtItems = new ArrayList<DistrictItem>();
        try {
            int status = rootObject.optInt("status");
            if (status == 0) {

                //
                JSONArray jArray = rootObject.optJSONArray("results");
                for (int i = 0; i < jArray.length(); i++) {
                    JSONObject dsObject = jArray.optJSONObject(i);
                    String datasource = dsObject.optString("datasource");
                    if (ComplexSearch.Query.DATASOURCE_TYPE_BUS.equals(datasource)) {
                        // bus
                        BusStationItem bItem = new BusStationItem();
                        bItem.setBusStationName(dsObject.optString("name"));
                        bItem.setBusStationId(dsObject.optString("uid"));
                        JSONObject location = dsObject.optJSONObject("location");
                        bItem.setLatLonPoint(new LatLonPoint(location.optDouble("lat"), location
                                .optDouble("lng")));
                        bItem.setBusStationType(dsObject.optString("type"));
                        bItem.setIsTransfer("1".equals(dsObject.optString("is_transfer"))?true:false);
                        JSONArray line_info = dsObject.optJSONArray("line_info");
                        // busline
                        List<BusLineItem> lineItems = new ArrayList<BusLineItem>();
                        for (int j = 0; j < line_info.length(); j++) {
                            JSONObject jj = line_info.optJSONObject(j);
                            BusLineItem lineItem = new BusLineItem();
                            lineItem.setBusLineName(jj.optString("line_name"));
                            lineItem.setBusLineId(jj.optString("line_id"));
                            lineItem.setOriginatingStation(jj.optString("start_name"));
                            lineItem.setTerminalStation(jj.optString("end_name"));
                            lineItem.setDatasource(ComplexSearch.Query.DATASOURCE_TYPE_BUSLINE);
                            lineItems.add(lineItem);
                        }
                        bItem.setBusLineItems(lineItems);

                        bItem.setCitycode(dsObject.optString("city_code"));
                        bItem.setCity(dsObject.optString("city"));
                        bItem.setAdCode(dsObject.optString("adcode"));
                        bItem.setDatasource(dsObject.optString("datasource"));
                        busItems.add(bItem);
                    } else if (ComplexSearch.Query.DATASOURCE_TYPE_BUSLINE.equals(datasource)) {
                        BusLineItem bLineItem = new BusLineItem();
                        bLineItem.setBusLineId(dsObject.optString("uid"));
                        bLineItem.setBusLineName(dsObject.optString("name"));
                        // 里程数返回单位 米，转成单位 公里
                        String length = dsObject.optString("length");
                        DecimalFormat df = new DecimalFormat("###.00");
                        bLineItem
                                .setDistance(Float.valueOf(df.format(Double.valueOf(length) / 1000)));
                        // link_type line_type
                        bLineItem.setBusLineType(dsObject.optString("line_type"));
                        bLineItem.setLineStatus(dsObject.optInt("line_status"));
                        bLineItem.setIsAuto(dsObject.optInt("is_auto"));
                        bLineItem.setStationNum(dsObject.optInt("stop_num"));
                        bLineItem.setOriginatingStation(dsObject.optString("start_name"));
                        bLineItem.setTerminalStation(dsObject.optString("end_name"));
                        bLineItem.setFirstBusTime(dsObject.optString("start_time"));
                        bLineItem.setLastBusTime(dsObject.optString("end_time"));
                        bLineItem.setInterval(dsObject.optString("interval"));
                        bLineItem.setIsLoop("1".equals(dsObject.optString("is_loop")));
                        bLineItem.setCityName(dsObject.optString("city_name"));
                        bLineItem.setCityCode(dsObject.optString("city_code"));
                        bLineItem.setAdcode(dsObject.optString("adcode"));
                        bLineItem.setBasicPrice(Float.valueOf(dsObject.optString("basic_price")));
                        bLineItem.setTotalPrice(Float.valueOf(dsObject.optString("total_price")));
                        bLineItem.setKeyName(dsObject.optString("key_name"));
                        bLineItem.setBusCompany(dsObject.optString("company"));
                        // station
                        List<BusStationItem> stationItems = new ArrayList<BusStationItem>();
                        JSONArray stop_info = dsObject.optJSONArray("stop_info");
                        for (int j = 0; j < stop_info.length(); j++) {
                            JSONObject stop = stop_info.optJSONObject(j);
                            BusStationItem bItem = new BusStationItem();
                            bItem.setOrder(stop.optString("order"));
                            bItem.setBusStationName(stop.optString("stop_name"));
                            JSONObject location = stop.optJSONObject("location");
                            LatLonPoint point = new LatLonPoint();
                            point.setLatitude(Double.valueOf(location.optString("lat")));
                            point.setLongitude(Double.valueOf(location.optString("lng")));
                            bItem.setLatLonPoint(point);
                            bItem.setDatasource(ComplexSearch.Query.DATASOURCE_TYPE_BUS);
                            stationItems.add(bItem);
                        }
                        bLineItem.setBusStations(stationItems);
                        // coords
                        List<LatLonPoint> directionsCoordinates = new ArrayList<LatLonPoint>();
                        String coords = dsObject.optString("coords");
                        String[] pointList = coords.split(";");
                        for (int j = 0; j < pointList.length; j++) {
                            LatLonPoint point = new LatLonPoint();
                            point.setLatitude(Double.valueOf(pointList[j].substring(pointList[j]
                                    .indexOf(",") + 1)));
                            point.setLongitude(Double.valueOf(pointList[j].substring(0,
                                    pointList[j].indexOf(","))));
                            directionsCoordinates.add(point);
                        }
                        bLineItem.setDirectionsCoordinates(directionsCoordinates);
                        bLineItem.setDatasource(datasource);

                        busLineItems.add(bLineItem);
                    } else if (ComplexSearch.Query.DATASOURCE_TYPE_POI.equals(datasource)) {
                        PoiItem poiItem = new PoiItem();
                        poiItem.setPoiId(dsObject.optString("uid"));
                        poiItem.setTitle(dsObject.optString("name"));
                        JSONObject location = dsObject.optJSONObject("location");
                        LatLonPoint point = new LatLonPoint();
                        point.setLatitude(Double.valueOf(location.optString("lat")));
                        point.setLongitude(Double.valueOf(location.optString("lng")));
                        poiItem.setLatLonPoint(point);
                        poiItem.setAdcode(dsObject.optString("adcode"));
                        poiItem.setCity(dsObject.optString("city"));
                        poiItem.setCity_code(dsObject.optString("city_code"));
                        poiItem.setDatasource(datasource);
                        poiItem.setSnippet(dsObject.optString("address"));
                        poiItem.setTel(dsObject.optString("telephone"));

                        poiItems.add(poiItem);
                    } else if (ComplexSearch.Query.DATASOURCE_TYPE_DISTRICT.equals(datasource)) {
                        ArrayList<String> coords = new ArrayList<String>();
                        DistrictItem districtItem = new DistrictItem();
                        districtItem.setAdcode(dsObject.optString("adcode"));
                        districtItem.setName(dsObject.optString("name"));
                        // 有问题
//						String[] disStr = dsObject.optString("polyline").split("|");
                        splitString(coords, dsObject.optString("polyline"), "|");
                        String[] disStr = (String[]) coords.toArray(new String[coords.size()]);
//						districtItem.setDistrictBoundary(disStr);
                        districtItem.setBounds(dsObject.optString("bounds"));
                        // add after
                        ArrayList<ArrayList<LatLonPoint>> polyLine = new ArrayList<ArrayList<LatLonPoint>>();
                        for (int j = 0; j < disStr.length; j++) {
                            ArrayList<LatLonPoint> points = new ArrayList<LatLonPoint>();
                            String[] region = disStr[j].split(";");
                            for (int k = 0; k < region.length; k++) {
                                LatLonPoint point = new LatLonPoint();
                                point.setLongitude(Double.valueOf(region[k].substring(0, region[k].indexOf(","))));
                                point.setLatitude(Double.valueOf(region[k].substring(region[k].indexOf(",") + 1)));
                                points.add(point);
                            }
                            polyLine.add(points);
                        }
                        districtItem.setPolyline(polyLine);
                        ArrayList<LatLonPoint> boundsList = new ArrayList<LatLonPoint>();
                        String[] bounds = dsObject.optString("bounds").split(";");
                        for (int j = 0; j < bounds.length; j++) {
                            LatLonPoint point = new LatLonPoint();
                            point.setLongitude(Double.valueOf(bounds[j].substring(0, bounds[j].indexOf(","))));
                            point.setLatitude(Double.valueOf(bounds[j].substring(bounds[j].indexOf(",") + 1)));
                            boundsList.add(point);
                        }
                        districtItem.setBoundsList(boundsList);
						/* add after */
                        JSONObject location = dsObject.optJSONObject("center");
                        LatLonPoint point = new LatLonPoint();
                        point.setLatitude(Double.valueOf(location.optString("lat")));
                        point.setLongitude(Double.valueOf(location.optString("lng")));
                        districtItem.setCenter(point);
                        districtItem.setLevel(dsObject.optString("level"));
                        districtItem.setCitycode(dsObject.optString("city_code"));
                        districtItem.setCity(dsObject.optString("city"));
                        districtItem.setDatasource(datasource);

                        districtItems.add(districtItem);
                    }
                }
                resultData = new ComplexSearchResult(query, poiItems, busItems, busLineItems,
                        districtItems);

                int total = rootObject.optInt("total");
                resultData.setTotal(total);
                String message = rootObject.optString("message");
                resultData.setMessage(message);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            return resultData;
        }
    }


    void splitString(ArrayList<String> coords, final String src, final String argment) {
        if (src != null) {
            if (src.indexOf(argment) != -1) {
                coords.add(src.substring(0, src.indexOf(argment)));
                splitString(coords, src.substring(src.indexOf(argment) + 1), argment);
            } else {
                coords.add(src);
            }
        }

    }

}
