package com.unistrong.api.services.route;

import com.unistrong.api.services.busline.BusStationItem;
import com.unistrong.api.services.core.JsonResultHandler;
import com.unistrong.api.services.core.LatLonPoint;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

class RouteJsonParser {
    String[] str = {"北", "东北", "东北", "东", "东南", "东南", "南", "西南", "西南", "西",
            "西北", "西北"};
//    String[] turn = {"无效", "直行", "右前方转弯", "右转", "右后方转弯", "掉头", "左后方转弯", "左转",
//            "左前方转弯", "左侧", "右侧", "分歧-左", "分歧中央", "分歧右", "环岛", "进渡口", "出渡口"};
    String[] turn = {"无效", "左转", "右转", "向左前方行驶", "向右前方行驶", "向左后方行驶", "向右后方行驶", "左转掉头",
            "直行", "靠左", "靠右", "进入环岛", "离开环岛", "减速行驶", "插入直行"};
    float walkDis = 0;//缓存步行距离总和


    /**
     * 驾车json解析
     * @param obj
     * @return
     */
    DriveRouteResult driveParse(JSONObject obj){

        DriveRouteResult result = new DriveRouteResult();
        try {

            boolean hasRes = obj.has("result");
            if (hasRes == false) {
                return null;
            }
            if (obj.has("message")) {
                result.setMessage(obj.getString("message"));
            }
            if (obj.has("type")) {
                if (!"".equals(obj.getString("type")))
                    result.setType(Integer.valueOf(obj.getString("type")));
            }
            JSONObject resultObj = obj.getJSONObject("result");

            if (resultObj.has("routes")) {
                JSONArray pathArray = resultObj.getJSONArray("routes");
                List<DrivePath> paths = new ArrayList<DrivePath>();
                LatLonPoint startPoint = new LatLonPoint();
                LatLonPoint endPoint = new LatLonPoint();
                parseStartPointAndEndPoint(resultObj, startPoint, endPoint);//解析起点终点

                RoutePoint sRoutePoint = new RoutePoint();
                sRoutePoint.setLocation(startPoint.copy());
                result.setStartPosInfo(sRoutePoint);
                RoutePoint eRoutePoint = new RoutePoint();
                eRoutePoint.setLocation(endPoint.copy());
                result.setTargetPosInfo(eRoutePoint);

//                parseStartDetailInfoAndEndDetailInfo(resultObj,sRoutePoint,eRoutePoint);//解析起终点的其他信息，暂时没有数据

                if (pathArray != null) {
                    int size = pathArray.length();
                    for (int i = 0; i < size; i++) {
                        JSONObject pathObj = pathArray.getJSONObject(i);
                        DrivePath path = new DrivePath();
                        path.setOriginLocation(startPoint.copy());
                        path.setDestinationLocation(endPoint.copy());
                        //驾车方案解析
                        parseDrivePath(pathObj, path);
                        paths.add(path);
                    }
                }
                result.setPaths(paths);

            }

        } catch (JSONException e) {
            e.printStackTrace();
        }


        return result;
    }

    private void parseDrivePath(JSONObject pathObj, DrivePath path) throws  JSONException{
        path.setDistance(Float.parseFloat(JsonResultHandler.getJsonStringValue(pathObj, "distance", "")));
        path.setDuration(Long.parseLong(JsonResultHandler.getJsonStringValue(pathObj, "duration", "")));
        path.setTolls(Float.parseFloat(JsonResultHandler.getJsonStringValue(pathObj, "toll", "")));
        if(pathObj.has("steps")){
            JSONArray stepsArray = pathObj .getJSONArray("steps");
            List<DriveStep> steps = new ArrayList<DriveStep>();
            if(stepsArray!=null){
                int length =  stepsArray.length();
                for (int j = 0; j < length; j++) {
                    JSONObject stepObj = stepsArray  .getJSONObject(j);
                    DriveStep driveStep = new DriveStep();
                    parseDriveStep(stepObj, driveStep);
                    steps.add(driveStep);

                }
                path.setSteps(steps);
            }

        }

    }

    private void parseDriveStep(JSONObject stepObj, DriveStep driveStep) throws  JSONException{
        if (stepObj.has("turn")) {
            driveStep.setTurn(Integer.parseInt(JsonResultHandler.getJsonStringValue(stepObj,"turn","")));
            driveStep.setInstruction(turn[Integer.parseInt(JsonResultHandler.getJsonStringValue(stepObj,"turn",""))]);// 设置路段指令
        }

        if (stepObj.has("ispasspoi")) {
            if ("1".equals(stepObj .getString("ispasspoi")))
                driveStep.setIsPasspoi(true);// 是否是途径点
            else
                driveStep.setIsPasspoi(false);
        }

        driveStep.setDuration(Float.valueOf(JsonResultHandler.getJsonStringValue(stepObj,"duration","")));
        driveStep.setRoad(JsonResultHandler.getJsonStringValue(stepObj, "instruction", ""));
        driveStep.setDistance(Float.valueOf(JsonResultHandler.getJsonStringValue(stepObj,"distance","")));
        //step的终点
        LatLonPoint destPoint = getDestPoint(stepObj);
        driveStep.setDestinationLocation(destPoint);
        //step的起点
        LatLonPoint originPoint = getOriginPoint(stepObj);
        driveStep.setOriginLocation(originPoint);

        int  directionID  = parseDirection(stepObj);
        driveStep.setOrientation(str[directionID]);
        List<LatLonPoint> locationList = getPathPoints(stepObj);
        driveStep.setPolyline(locationList);


    }


    /**
     * 步行解析
     * @param obj
     * @return
     */

    WalkRouteResult walkParse(JSONObject obj){
        WalkRouteResult result = new WalkRouteResult();
        walkDis = 0;
        try {
            boolean hasRes = obj.has("result");
            if (hasRes == false) {
                return null;
            }
            if (obj.has("message")) {
                result.setMessage(obj.getString("message"));
            }
            if (obj.has("type")) {
                if (!"".equals(obj.getString("type")))
                    result.setType(Integer.valueOf(obj.getString("type")));
            }
            JSONObject resultObj = obj.getJSONObject("result");

            if (resultObj.has("routes")) {
                JSONArray pathArray = resultObj.getJSONArray("routes");
                List<WalkPath> paths = new ArrayList<WalkPath>();
                LatLonPoint startPoint = new LatLonPoint();
                LatLonPoint endPoint = new LatLonPoint();
                parseStartPointAndEndPoint(resultObj, startPoint, endPoint);//解析起点终点

                RoutePoint sRoutePoint = new RoutePoint();
                sRoutePoint.setLocation(startPoint.copy());
                result.setStartPosInfo(sRoutePoint);
                RoutePoint eRoutePoint = new RoutePoint();
                eRoutePoint.setLocation(endPoint.copy());
                result.setTargetPosInfo(eRoutePoint);

//                parseStartDetailInfoAndEndDetailInfo(resultObj,sRoutePoint,eRoutePoint);//解析起终点的其他信息，暂时没有数据

                if (pathArray != null) {
                    int size = pathArray.length();
                    walkDis = 0;
                    for (int i = 0; i < size; i++) {
                        JSONObject pathObj = pathArray.getJSONObject(i);
                        WalkPath path = new WalkPath();
                        path.setOriginLocation(startPoint.copy());
                        path.setDestinationLocation(endPoint.copy());
                       //步行方案解析
                        parseWalkPath(pathObj, path);
                        paths.add(path);
                    }
                }
                result.setPaths(paths);

            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return  result;
    }

    /**
     * 步行path解析
     * @param pathObj
     * @param path
     * @throws JSONException
     */
    private void parseWalkPath(JSONObject pathObj, WalkPath path) throws  JSONException{
        path.setDistance(Float.parseFloat(JsonResultHandler.getJsonStringValue(pathObj, "distance", "")));
        path.setDuration(Long.parseLong(JsonResultHandler.getJsonStringValue(pathObj, "duration", "")));
        if(pathObj.has("steps")){
            JSONArray stepsArray = pathObj .getJSONArray("steps");
            List<WalkStep> steps = new ArrayList<WalkStep>();
            if(stepsArray!=null){
               int length =  stepsArray.length();
                for (int j = 0; j < length; j++) {
                    JSONObject stepObj = stepsArray  .getJSONObject(j);
                    WalkStep walkStep = new WalkStep();
                    parseWalkStep(stepObj,walkStep);
                    steps.add(walkStep);

                }
                path.setSteps(steps);
            }

        }

    }

    private void parseWalkStep(JSONObject stepObj, WalkStep walkStep) throws JSONException{
        if (stepObj.has("turn")) {
            walkStep.setTurn(Integer.parseInt(JsonResultHandler.getJsonStringValue(stepObj,"turn","")));
            walkStep.setInstruction(turn[Integer.parseInt(JsonResultHandler.getJsonStringValue(stepObj,"turn",""))]);// 设置路段指令
        }
        walkStep.setDuration(Float.valueOf(JsonResultHandler.getJsonStringValue(stepObj,"duration","")));
        walkStep.setRoad(JsonResultHandler.getJsonStringValue(stepObj, "instruction", ""));
        walkStep.setDistance(Float.valueOf(JsonResultHandler.getJsonStringValue(stepObj,"distance","")));

        int  directionID = parseDirection(stepObj);
        walkStep.setOrientation(str[directionID]);

        List<LatLonPoint> pathPoints = getPathPoints(stepObj);
        walkStep.setPolyline(pathPoints);

    }

    private int parseDirection(JSONObject stepObj) {
        int angle = Integer .valueOf(JsonResultHandler.getJsonStringValue(stepObj,"direction",""));
        int directionID = 0;
        if ((angle - 15) % 30 > 0) {
            directionID = (angle - 15) / 30 + 1;
        } else {
            directionID = (angle - 15) / 30;
        }
         return directionID;

    }

    private void parseStartDetailInfoAndEndDetailInfo(JSONObject resultObj,RoutePoint sRoutePoint,RoutePoint eRoutePoint) {
        //TODO 由于没有一下字段信息，暂时不解析，等服务器增加
//        if(resultObj.has("origin")){
//            if (resultObj.has("uid")) {
//                sRoutePoint.setId(resultObj.getString("uid"));
//            }
//            if (resultObj.has("area_id")) {
//                sRoutePoint
//                        .setCityCode(resultObj.getString("area_id"));
//            }
//            if (resultObj.has("wd")) {
//                sRoutePoint.setSearchKey(resultObj.getString("wd"));
//            }
//            if (resultObj.has("cname")) {
//                eRoutePoint.setCityName(resultObj.getString("cname"));
//            }
//
//        }
        //TODO 由于没有一下字段信息，暂时不解析，等服务器增加
//        if(resultObj.has("destination")){
//            if (resultObj.has("uid")) {
//                eRoutePoint.setId(resultObj.getString("uid"));
//            }
//            if (resultObj.has("area_id")) {
//                eRoutePoint  .setCityCode(resultObj.getString("area_id"));
//            }
//            if (resultObj.has("wd")) {
//                eRoutePoint.setSearchKey(resultObj.getString("wd"));
//            }
//            if (resultObj.has("cname")) {
//                eRoutePoint.setCityName(resultObj.getString("cname"));
//            }
//
//
//        }

    }



    /**
     * 公交换乘方案解析。
     * @param obj
     * @return
     */
    BusRouteResult busParse(JSONObject obj) {
        BusRouteResult result =  new BusRouteResult();
        try {
            boolean hasRes = obj.has("result");
            if (hasRes == false) {
                return null;
            }
            if (obj.has("message")) {
                result.setMessage(obj.getString("message"));
            }
            if (obj.has("type")) {
                if (!"".equals(obj.getString("type")))
                    result.setType(Integer.valueOf(obj.getString("type")));
            }
            JSONObject resultObj = obj.getJSONObject("result");

            if (resultObj.has("routes")) {
                JSONArray pathArray = resultObj.getJSONArray("routes");
                List<BusPath> paths = new ArrayList<BusPath>();
                LatLonPoint startPoint = new LatLonPoint();
                LatLonPoint endPoint = new LatLonPoint();
                parseStartPointAndEndPoint(resultObj, startPoint, endPoint);//解析起点终点

                RoutePoint sRoutePoint = new RoutePoint();
                sRoutePoint.setLocation(startPoint.copy());
                result.setStartPosInfo(sRoutePoint);
                RoutePoint eRoutePoint = new RoutePoint();
                eRoutePoint.setLocation(endPoint.copy());
                result.setTargetPosInfo(eRoutePoint);

                if (pathArray != null) {
                    int size = pathArray.length();
                    walkDis = 0;
                    for (int i = 0; i < size; i++) {
                        JSONObject pathObj = pathArray.getJSONObject(i);
                        BusPath path = new BusPath();
                        path.setOriginLocation(startPoint.copy());
                        path.setDestinationLocation(endPoint.copy());
                        //pathObj解析为path
                        parsePath(pathObj, path);
                        path.setWalkDistance(walkDis);
                        walkDis = 0;
                        paths.add(path);
                    }
                }
                result.setPaths(paths);

            }
        } catch (JSONException e) {
            e.printStackTrace();
            throw new IllegalArgumentException("json解析失败");// e.getLocalizedMessage()
        }
        return result;
    }
    private void parseStartPointAndEndPoint(JSONObject resultObj,LatLonPoint startPoint,LatLonPoint endPoint)throws JSONException{
        if(resultObj.has("origin")){
            JSONObject originObj = resultObj.getJSONObject("origin");
            JSONObject originPtObj = originObj.getJSONObject("originPt");
            Double x = Double.parseDouble(originPtObj.getString("lng"));
            Double y = Double.parseDouble(originPtObj.getString("lat"));
            startPoint.setLongitude(x);
            startPoint.setLatitude(y);
        }
        if(resultObj.has("destination")){
            JSONObject destinationObj = resultObj.getJSONObject("destination");
            JSONObject destinationPtObj = destinationObj.getJSONObject("destinationPt");
            Double x = Double.parseDouble(destinationPtObj.getString("lng"));
            Double y = Double.parseDouble(destinationPtObj.getString("lat"));
            endPoint.setLongitude(x);
            endPoint.setLatitude(y);
        }
    }
    private void parsePath(JSONObject pathObj,BusPath path)throws JSONException{
        if (pathObj.has("scheme")) {
            JSONArray jsonArray = pathObj.getJSONArray("scheme");
            if (jsonArray != null) {
                int size = jsonArray.length();
                for (int j = 0; j < size; j++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(j);
                    path.setDistance(Long.valueOf(JsonResultHandler.getJsonIntValue(jsonObject, "distance", 0)));

                    String price = JsonResultHandler.getJsonStringValue(jsonObject,"price","");
                    if(price.length()>0){
                        path.setPrice(Float.parseFloat(price));
                    }
                    if (jsonObject.has("steps")) {
                        JSONArray stepsArray = jsonObject.getJSONArray("steps");
                        List<BusStep> steps = new ArrayList<BusStep>();
                        parseSteps(stepsArray,steps);
                        path.setSteps(steps);
                    }
                }
            }

        }
    }
    private void parseSteps(JSONArray stepsArray ,List<BusStep> steps)throws JSONException{
        int size =stepsArray.length();
        RouteBusWalkItem walkItem = null;
        for (int i = 0 ; i < size ; i++) {
            JSONObject stepObj = stepsArray.getJSONObject(i);
            String stepType = JsonResultHandler.getJsonStringValue(stepObj,"type","");
            if (stepType.length()>0){
                int type = Integer.parseInt(stepType);
                switch (type){
                    case 5://步行
                        walkItem = parseWalkItem(stepObj);
                        break;
                    default:
                        BusStep busStep = parseBusStep(stepObj);
                        if (walkItem!=null){
                            busStep.setWalk(walkItem);
                            walkItem = null;
                        }
                        steps.add(busStep);
                        break;
                }
            }else{
                continue;
            }
        }
        if (walkItem!=null){
            BusStep busStep = new BusStep();
            busStep.setWalk(walkItem);
            steps.add(busStep);
            walkItem = null;
        }
    }
    private LatLonPoint getDestPoint(JSONObject stepObj)throws JSONException{
        if (stepObj.has("stepDestinationLocation")) {// 路段终点
            JSONObject destLocObj = stepObj.getJSONObject("stepDestinationLocation");
            String lat = destLocObj.getString("lat");
            String lng = destLocObj.getString("lng");
            return new LatLonPoint(Double.parseDouble(lat), Double.parseDouble(lng));
        }
        return null;
    }
    private LatLonPoint getOriginPoint(JSONObject stepObj)throws JSONException{
        if (stepObj.has("stepOriginLocation")) {// 路段起点
            JSONObject origLocObj = stepObj.getJSONObject("stepOriginLocation");
            String lat = origLocObj.getString("lat");
            String lng = origLocObj.getString("lng");
            return new LatLonPoint(Double.parseDouble(lat), Double.parseDouble(lng));
        }
        return null;
    }
    private List<LatLonPoint> getPathPoints(JSONObject stepObj)throws JSONException{
        List<LatLonPoint> locationList = new ArrayList<LatLonPoint>();
        if (stepObj.has("path")) {
            String pathStr = stepObj.getString("path");
            if (!"".equals(pathStr)) {
                String[] locations = pathStr.split(";");
                for (String str : locations) {
                    String[] latlon = str.split(",");
                    LatLonPoint location = new LatLonPoint(Double.valueOf(latlon[1]), Double.valueOf(latlon[0]));
                    locationList.add(location);
                }
            }
        }
        return locationList;
    }
    /**
     * 解析公交信息
     * @param stepObj
     * @return
     */
    private BusStep parseBusStep(JSONObject stepObj)throws JSONException{
        BusStep busStep = new BusStep();
        List<RouteBusLineItem> routeList = new ArrayList<RouteBusLineItem>();
        RouteBusLineItem busLineItem = new RouteBusLineItem();
        busLineItem.setDuration(Long.valueOf(BusSearchServerHandler.getJsonIntValue(stepObj, "duration", 0)));
        busLineItem.setDistance(Long.valueOf(BusSearchServerHandler.getJsonIntValue(stepObj, "distance", 0)));


        BusStationItem startStation = new BusStationItem();
        BusStationItem endStation = new BusStationItem();

        LatLonPoint destPoint = getDestPoint(stepObj);
        if(destPoint!=null){
            endStation.setLatLonPoint(destPoint);
            busLineItem.setArrivalBusStation(endStation);
        }
        LatLonPoint originPoint = getOriginPoint(stepObj);
        if(originPoint!=null){
            startStation.setLatLonPoint(originPoint);
            busLineItem.setDepartureBusStation(startStation);
        }
        List<LatLonPoint> pointList = getPathPoints(stepObj);
        busLineItem.setPolyline(pointList);

        if (stepObj.has("vehicle")) {
            JSONObject infoObject = stepObj.getJSONObject("vehicle");
            busLineItem.setBusLineId(BusSearchServerHandler.getJsonStringValue(infoObject, "uid", ""));
            busLineItem.setPassStationNum(Integer.parseInt(BusSearchServerHandler.getJsonStringValue(infoObject, "stop_num", "")));
            busLineItem.setFirstBusTime(BusSearchServerHandler.getJsonStringValue(infoObject,"start_time",""));
            busLineItem.setLastBusTime(BusSearchServerHandler.getJsonStringValue(infoObject,"end_time",""));
            startStation.setBusStationName(BusSearchServerHandler.getJsonStringValue(infoObject, "start_name", ""));
            startStation.setBusStationId(JsonResultHandler.getJsonStringValue(infoObject, "start_uid", ""));

            busLineItem.setDepartureBusStation(startStation);

            endStation.setBusStationName(BusSearchServerHandler.getJsonStringValue(infoObject, "end_name", ""));
            endStation.setBusStationId(JsonResultHandler.getJsonStringValue(infoObject,"end_uid",""));
             busLineItem.setArrivalBusStation(endStation);

            busLineItem.setFirstBusTime(BusSearchServerHandler.getJsonStringValue(infoObject, "start_time", ""));
            busLineItem.setLastBusTime(BusSearchServerHandler.getJsonStringValue(infoObject, "end_time", ""));
            busLineItem.setBusLineName(BusSearchServerHandler.getJsonStringValue(infoObject, "name", ""));
            busLineItem.setTotalPrice(Float.parseFloat(BusSearchServerHandler.getJsonStringValue(infoObject, "total_price", "")));
        }
        routeList.add(busLineItem);
        busStep.setBusLines(routeList);
        return busStep;
    }
    /**
     * 解析步行信息
     * @param stepObj
     * @return
     */
    private RouteBusWalkItem parseWalkItem(JSONObject stepObj)throws JSONException{
        RouteBusWalkItem walkItem = new RouteBusWalkItem();
        walkItem.setDistance(Float.parseFloat(BusSearchServerHandler.getJsonStringValue(stepObj, "distance", "")));
        if(stepObj.has("vehicle")){
            JSONObject object = stepObj.getJSONObject("vehicle");
            walkItem.setOriginatingName(BusSearchServerHandler.getJsonStringValue(object, "start_name", ""));
            walkItem.setTerminalName(BusSearchServerHandler.getJsonStringValue(object, "end_name", ""));
        }

        this.walkDis += walkItem.getDistance();
        String pathStr = BusSearchServerHandler.getJsonStringValue(stepObj, "path", "");
        //解析步行线路
        if (pathStr.length()>0) {
            List<WalkStep> walkSteps = new ArrayList<WalkStep>();
            WalkStep walkStep = new WalkStep();
            List<LatLonPoint> pointList = getPathPoints(stepObj);
            walkStep.setPolyline(pointList);
            walkStep.setDistance(walkItem.getDistance());
            walkSteps.add(walkStep);
            walkItem.setSteps(walkSteps);
        }else{
            return null;
        }
        walkItem.setDuration(Long.valueOf(BusSearchServerHandler.getJsonStringValue(stepObj, "duration", "")));
        LatLonPoint destPoint = getDestPoint(stepObj);
        if(destPoint!=null){
            walkItem.setDestinationLocation(destPoint);
        }
        LatLonPoint originPoint = getOriginPoint(stepObj);
        if(originPoint!=null){
            walkItem.setOriginLocation(originPoint);
        }
        return walkItem;
    }
}
