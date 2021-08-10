package com.unistrong.api.mapcore.navigation;

import com.unistrong.api.maps.model.LatLng;

import java.util.ArrayList;
import java.util.List;

public class AutoMobileEntity {

    /**
     * status : 1
     * info : OK
     * infocode : 10000
     * count : 1
     * route : {"origin":"116.481028,39.989643","destination":"116.465302,40.004717","taxi_cost":"13","paths":[{"distance":"3031","duration":"752","strategy":"速度最快","tolls":"0","toll_distance":"0","steps":[{"instruction":"向北行驶109米右转","orientation":"北","distance":"109","tolls":"0","toll_distance":"0","toll_road":[],"duration":"49","polyline":"116.48089,39.989371;116.480551,39.989605;116.480547,39.989657;116.480595,39.989705;116.481094,39.990048","action":"右转","assistant_action":[],"tmcs":[{"lcode":[],"distance":"52","status":"未知","polyline":"116.48089,39.989371;116.480551,39.989605;116.480547,39.989657;116.480595,39.989705"},{"lcode":[],"distance":"57","status":"未知","polyline":"116.480595,39.989705;116.481094,39.990048"}],"cities":[{"name":"北京城区","citycode":"010","adcode":"110100","districts":[{"name":"朝阳区","adcode":"110105"}]}]},{"instruction":"向东南行驶36米右转","orientation":"东南","distance":"36","tolls":"0","toll_distance":"0","toll_road":[],"duration":"22","polyline":"116.481094,39.990048;116.481111,39.99;116.481345,39.989792","action":"右转","assistant_action":[],"tmcs":[{"lcode":[],"distance":"36","status":"未知","polyline":"116.481094,39.990048;116.481111,39.99;116.481345,39.989792"}],"cities":[{"name":"北京城区","citycode":"010","adcode":"110100","districts":[{"name":"朝阳区","adcode":"110105"}]}]},{"instruction":"向西南行驶76米右转进入主路","orientation":"西南","distance":"76","tolls":"0","toll_distance":"0","toll_road":[],"duration":"36","polyline":"116.481345,39.989792;116.481372,39.989753;116.481367,39.989718;116.480937,39.989284;116.480885,39.989223","action":"右转","assistant_action":"进入主路","tmcs":[{"lcode":[],"distance":"69","status":"未知","polyline":"116.481345,39.989792;116.481372,39.989753;116.481367,39.989718;116.480937,39.989284"},{"lcode":[],"distance":"7","status":"未知","polyline":"116.480937,39.989284;116.480885,39.989223"}],"cities":[{"name":"北京城区","citycode":"010","adcode":"110100","districts":[{"name":"朝阳区","adcode":"110105"}]}]},{"instruction":"沿阜荣街向西北行驶682米左转进入主路","orientation":"西北","road":"阜荣街","distance":"682","tolls":"0","toll_distance":"0","toll_road":[],"duration":"211","polyline":"116.480816,39.989154;116.479805,39.989826;116.479727,39.989883;116.479193,39.990234;116.477908,39.991085;116.477574,39.991285;116.477478,39.99135;116.477049,39.991645;116.476024,39.992331;116.474813,39.993116;116.474735,39.993173","action":"左转","assistant_action":"进入主路","tmcs":[{"lcode":[],"distance":"122","status":"畅通","polyline":"116.480816,39.989154;116.479805,39.989826;116.479727,39.989883"},{"lcode":[],"distance":"252","status":"畅通","polyline":"116.479727,39.989883;116.479193,39.990234;116.477908,39.991085;116.477574,39.991285;116.477478,39.99135"},{"lcode":[],"distance":"165","status":"畅通","polyline":"116.477478,39.99135;116.477049,39.991645;116.476024,39.992331"},{"lcode":[],"distance":"135","status":"畅通","polyline":"116.476024,39.992331;116.474813,39.993116"},{"lcode":[],"distance":"8","status":"畅通","polyline":"116.474813,39.993116;116.474735,39.993173"}],"cities":[{"name":"北京城区","citycode":"010","adcode":"110100","districts":[{"name":"朝阳区","adcode":"110105"}]}]},{"instruction":"沿阜安西路向西南行驶259米右转进入主路","orientation":"西南","road":"阜安西路","distance":"259","tolls":"0","toll_distance":"0","toll_road":[],"duration":"52","polyline":"116.474592,39.99316;116.473576,39.992235;116.473477,39.992131;116.47276,39.991502;116.472617,39.991376","action":"右转","assistant_action":"进入主路","tmcs":[{"lcode":[],"distance":"134","status":"畅通","polyline":"116.474592,39.99316;116.473576,39.992235"},{"lcode":[],"distance":"14","status":"畅通","polyline":"116.473576,39.992235;116.473477,39.992131"},{"lcode":[],"distance":"92","status":"畅通","polyline":"116.473477,39.992131;116.47276,39.991502"},{"lcode":[],"distance":"19","status":"畅通","polyline":"116.47276,39.991502;116.472617,39.991376"}],"cities":[{"name":"北京城区","citycode":"010","adcode":"110100","districts":[{"name":"朝阳区","adcode":"110105"}]}]},{"instruction":"沿广顺南大街途径广顺北大街向北行驶1.7千米左转","orientation":"北","road":"广顺南大街","distance":"1656","tolls":"0","toll_distance":"0","toll_road":[],"duration":"338","polyline":"116.472552,39.991315;116.471793,39.991814;116.471406,39.992079;116.470699,39.992535;116.470404,39.992721;116.470304,39.992786;116.469839,39.993099;116.469154,39.993576;116.468958,39.993746;116.468802,39.993906;116.468585,39.994197;116.468472,39.994366;116.468303,39.99477;116.468234,39.995104;116.468207,39.995469;116.468164,39.996675;116.468142,39.996897;116.468121,39.997205;116.468116,39.997283;116.468095,39.997726;116.468051,39.999015;116.467925,40.001194;116.467925,40.00122;116.467925,40.001272;116.467947,40.001628;116.467938,40.001723;116.46793,40.001966;116.467912,40.002387;116.467895,40.002561;116.467886,40.002708;116.467812,40.004332;116.467791,40.004679;116.467778,40.004944","action":"左转","assistant_action":[],"tmcs":[{"lcode":[],"distance":"84","status":"畅通","polyline":"116.472552,39.991315;116.471793,39.991814"},{"lcode":[],"distance":"44","status":"畅通","polyline":"116.471793,39.991814;116.471406,39.992079"},{"lcode":[],"distance":"79","status":"畅通","polyline":"116.471406,39.992079;116.470699,39.992535"},{"lcode":[],"distance":"43","status":"畅通","polyline":"116.470699,39.992535;116.470404,39.992721;116.470304,39.992786"},{"lcode":[],"distance":"52","status":"畅通","polyline":"116.470304,39.992786;116.469839,39.993099"},{"lcode":[],"distance":"163","status":"畅通","polyline":"116.469839,39.993099;116.469154,39.993576;116.468958,39.993746;116.468802,39.993906;116.468585,39.994197"},{"lcode":[],"distance":"146","status":"畅通","polyline":"116.468585,39.994197;116.468472,39.994366;116.468303,39.99477;116.468234,39.995104;116.468207,39.995469"},{"lcode":[],"distance":"134","status":"畅通","polyline":"116.468207,39.995469;116.468164,39.996675"},{"lcode":[],"distance":"24","status":"畅通","polyline":"116.468164,39.996675;116.468142,39.996897"},{"lcode":[],"distance":"42","status":"畅通","polyline":"116.468142,39.996897;116.468121,39.997205;116.468116,39.997283"},{"lcode":[],"distance":"49","status":"畅通","polyline":"116.468116,39.997283;116.468095,39.997726"},{"lcode":[],"distance":"143","status":"畅通","polyline":"116.468095,39.997726;116.468051,39.999015"},{"lcode":[],"distance":"241","status":"畅通","polyline":"116.468051,39.999015;116.467925,40.001194"},{"lcode":[],"distance":"9","status":"畅通","polyline":"116.467925,40.001194;116.467925,40.00122;116.467925,40.001272"},{"lcode":[],"distance":"50","status":"畅通","polyline":"116.467925,40.001272;116.467947,40.001628;116.467938,40.001723"},{"lcode":[],"distance":"26","status":"畅通","polyline":"116.467938,40.001723;116.46793,40.001966"},{"lcode":[],"distance":"47","status":"畅通","polyline":"116.46793,40.001966;116.467912,40.002387"},{"lcode":[],"distance":"18","status":"畅通","polyline":"116.467912,40.002387;116.467895,40.002561"},{"lcode":[],"distance":"16","status":"畅通","polyline":"116.467895,40.002561;116.467886,40.002708"},{"lcode":[],"distance":"180","status":"畅通","polyline":"116.467886,40.002708;116.467812,40.004332"},{"lcode":[],"distance":"38","status":"畅通","polyline":"116.467812,40.004332;116.467791,40.004679"},{"lcode":[],"distance":"28","status":"畅通","polyline":"116.467791,40.004679;116.467778,40.004944"}],"cities":[{"name":"北京城区","citycode":"010","adcode":"110100","districts":[{"name":"朝阳区","adcode":"110105"}]}]},{"instruction":"沿河荫西路向西行驶184米左转","orientation":"西","road":"河荫西路","distance":"184","tolls":"0","toll_distance":"0","toll_road":[],"duration":"29","polyline":"116.467391,40.005004;116.466445,40.004991;116.465234,40.004991","action":"左转","assistant_action":[],"tmcs":[{"lcode":[],"distance":"184","status":"畅通","polyline":"116.467391,40.005004;116.466445,40.004991;116.465234,40.004991"}],"cities":[{"name":"北京城区","citycode":"010","adcode":"110100","districts":[{"name":"朝阳区","adcode":"110105"}]}]},{"instruction":"向南行驶23米左转","orientation":"南","distance":"23","tolls":"0","toll_distance":"0","toll_road":[],"duration":"13","polyline":"116.465226,40.004891;116.465234,40.00474;116.465234,40.004679","action":"左转","assistant_action":[],"tmcs":[{"lcode":[],"distance":"17","status":"未知","polyline":"116.465226,40.004891;116.465234,40.00474"},{"lcode":[],"distance":"6","status":"未知","polyline":"116.465234,40.00474;116.465234,40.004679"}],"cities":[{"name":"北京城区","citycode":"010","adcode":"110100","districts":[{"name":"朝阳区","adcode":"110105"}]}]},{"instruction":"向东行驶6米到达目的地","orientation":"东","distance":"6","tolls":"0","toll_distance":"0","toll_road":[],"duration":"2","polyline":"116.465234,40.004679;116.465304,40.004683","action":[],"assistant_action":"到达目的地","tmcs":[{"lcode":[],"distance":"6","status":"未知","polyline":"116.465234,40.004679;116.465304,40.004683"}],"cities":[{"name":"北京城区","citycode":"010","adcode":"110100","districts":[{"name":"朝阳区","adcode":"110105"}]}]}],"restriction":"0","traffic_lights":"7"}]}
     */

    private String status;
    private String info;
    private String infocode;
    private String count;
    private RouteBean route;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public String getInfocode() {
        return infocode;
    }

    public void setInfocode(String infocode) {
        this.infocode = infocode;
    }

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }

    public RouteBean getRoute() {
        return route;
    }

    public void setRoute(RouteBean route) {
        this.route = route;
    }

    public static class RouteBean {
        /**
         * origin : 116.481028,39.989643
         * destination : 116.465302,40.004717
         * taxi_cost : 13
         * paths : [{"distance":"3031","duration":"752","strategy":"速度最快","tolls":"0","toll_distance":"0","steps":[{"instruction":"向北行驶109米右转","orientation":"北","distance":"109","tolls":"0","toll_distance":"0","toll_road":[],"duration":"49","polyline":"116.48089,39.989371;116.480551,39.989605;116.480547,39.989657;116.480595,39.989705;116.481094,39.990048","action":"右转","assistant_action":[],"tmcs":[{"lcode":[],"distance":"52","status":"未知","polyline":"116.48089,39.989371;116.480551,39.989605;116.480547,39.989657;116.480595,39.989705"},{"lcode":[],"distance":"57","status":"未知","polyline":"116.480595,39.989705;116.481094,39.990048"}],"cities":[{"name":"北京城区","citycode":"010","adcode":"110100","districts":[{"name":"朝阳区","adcode":"110105"}]}]},{"instruction":"向东南行驶36米右转","orientation":"东南","distance":"36","tolls":"0","toll_distance":"0","toll_road":[],"duration":"22","polyline":"116.481094,39.990048;116.481111,39.99;116.481345,39.989792","action":"右转","assistant_action":[],"tmcs":[{"lcode":[],"distance":"36","status":"未知","polyline":"116.481094,39.990048;116.481111,39.99;116.481345,39.989792"}],"cities":[{"name":"北京城区","citycode":"010","adcode":"110100","districts":[{"name":"朝阳区","adcode":"110105"}]}]},{"instruction":"向西南行驶76米右转进入主路","orientation":"西南","distance":"76","tolls":"0","toll_distance":"0","toll_road":[],"duration":"36","polyline":"116.481345,39.989792;116.481372,39.989753;116.481367,39.989718;116.480937,39.989284;116.480885,39.989223","action":"右转","assistant_action":"进入主路","tmcs":[{"lcode":[],"distance":"69","status":"未知","polyline":"116.481345,39.989792;116.481372,39.989753;116.481367,39.989718;116.480937,39.989284"},{"lcode":[],"distance":"7","status":"未知","polyline":"116.480937,39.989284;116.480885,39.989223"}],"cities":[{"name":"北京城区","citycode":"010","adcode":"110100","districts":[{"name":"朝阳区","adcode":"110105"}]}]},{"instruction":"沿阜荣街向西北行驶682米左转进入主路","orientation":"西北","road":"阜荣街","distance":"682","tolls":"0","toll_distance":"0","toll_road":[],"duration":"211","polyline":"116.480816,39.989154;116.479805,39.989826;116.479727,39.989883;116.479193,39.990234;116.477908,39.991085;116.477574,39.991285;116.477478,39.99135;116.477049,39.991645;116.476024,39.992331;116.474813,39.993116;116.474735,39.993173","action":"左转","assistant_action":"进入主路","tmcs":[{"lcode":[],"distance":"122","status":"畅通","polyline":"116.480816,39.989154;116.479805,39.989826;116.479727,39.989883"},{"lcode":[],"distance":"252","status":"畅通","polyline":"116.479727,39.989883;116.479193,39.990234;116.477908,39.991085;116.477574,39.991285;116.477478,39.99135"},{"lcode":[],"distance":"165","status":"畅通","polyline":"116.477478,39.99135;116.477049,39.991645;116.476024,39.992331"},{"lcode":[],"distance":"135","status":"畅通","polyline":"116.476024,39.992331;116.474813,39.993116"},{"lcode":[],"distance":"8","status":"畅通","polyline":"116.474813,39.993116;116.474735,39.993173"}],"cities":[{"name":"北京城区","citycode":"010","adcode":"110100","districts":[{"name":"朝阳区","adcode":"110105"}]}]},{"instruction":"沿阜安西路向西南行驶259米右转进入主路","orientation":"西南","road":"阜安西路","distance":"259","tolls":"0","toll_distance":"0","toll_road":[],"duration":"52","polyline":"116.474592,39.99316;116.473576,39.992235;116.473477,39.992131;116.47276,39.991502;116.472617,39.991376","action":"右转","assistant_action":"进入主路","tmcs":[{"lcode":[],"distance":"134","status":"畅通","polyline":"116.474592,39.99316;116.473576,39.992235"},{"lcode":[],"distance":"14","status":"畅通","polyline":"116.473576,39.992235;116.473477,39.992131"},{"lcode":[],"distance":"92","status":"畅通","polyline":"116.473477,39.992131;116.47276,39.991502"},{"lcode":[],"distance":"19","status":"畅通","polyline":"116.47276,39.991502;116.472617,39.991376"}],"cities":[{"name":"北京城区","citycode":"010","adcode":"110100","districts":[{"name":"朝阳区","adcode":"110105"}]}]},{"instruction":"沿广顺南大街途径广顺北大街向北行驶1.7千米左转","orientation":"北","road":"广顺南大街","distance":"1656","tolls":"0","toll_distance":"0","toll_road":[],"duration":"338","polyline":"116.472552,39.991315;116.471793,39.991814;116.471406,39.992079;116.470699,39.992535;116.470404,39.992721;116.470304,39.992786;116.469839,39.993099;116.469154,39.993576;116.468958,39.993746;116.468802,39.993906;116.468585,39.994197;116.468472,39.994366;116.468303,39.99477;116.468234,39.995104;116.468207,39.995469;116.468164,39.996675;116.468142,39.996897;116.468121,39.997205;116.468116,39.997283;116.468095,39.997726;116.468051,39.999015;116.467925,40.001194;116.467925,40.00122;116.467925,40.001272;116.467947,40.001628;116.467938,40.001723;116.46793,40.001966;116.467912,40.002387;116.467895,40.002561;116.467886,40.002708;116.467812,40.004332;116.467791,40.004679;116.467778,40.004944","action":"左转","assistant_action":[],"tmcs":[{"lcode":[],"distance":"84","status":"畅通","polyline":"116.472552,39.991315;116.471793,39.991814"},{"lcode":[],"distance":"44","status":"畅通","polyline":"116.471793,39.991814;116.471406,39.992079"},{"lcode":[],"distance":"79","status":"畅通","polyline":"116.471406,39.992079;116.470699,39.992535"},{"lcode":[],"distance":"43","status":"畅通","polyline":"116.470699,39.992535;116.470404,39.992721;116.470304,39.992786"},{"lcode":[],"distance":"52","status":"畅通","polyline":"116.470304,39.992786;116.469839,39.993099"},{"lcode":[],"distance":"163","status":"畅通","polyline":"116.469839,39.993099;116.469154,39.993576;116.468958,39.993746;116.468802,39.993906;116.468585,39.994197"},{"lcode":[],"distance":"146","status":"畅通","polyline":"116.468585,39.994197;116.468472,39.994366;116.468303,39.99477;116.468234,39.995104;116.468207,39.995469"},{"lcode":[],"distance":"134","status":"畅通","polyline":"116.468207,39.995469;116.468164,39.996675"},{"lcode":[],"distance":"24","status":"畅通","polyline":"116.468164,39.996675;116.468142,39.996897"},{"lcode":[],"distance":"42","status":"畅通","polyline":"116.468142,39.996897;116.468121,39.997205;116.468116,39.997283"},{"lcode":[],"distance":"49","status":"畅通","polyline":"116.468116,39.997283;116.468095,39.997726"},{"lcode":[],"distance":"143","status":"畅通","polyline":"116.468095,39.997726;116.468051,39.999015"},{"lcode":[],"distance":"241","status":"畅通","polyline":"116.468051,39.999015;116.467925,40.001194"},{"lcode":[],"distance":"9","status":"畅通","polyline":"116.467925,40.001194;116.467925,40.00122;116.467925,40.001272"},{"lcode":[],"distance":"50","status":"畅通","polyline":"116.467925,40.001272;116.467947,40.001628;116.467938,40.001723"},{"lcode":[],"distance":"26","status":"畅通","polyline":"116.467938,40.001723;116.46793,40.001966"},{"lcode":[],"distance":"47","status":"畅通","polyline":"116.46793,40.001966;116.467912,40.002387"},{"lcode":[],"distance":"18","status":"畅通","polyline":"116.467912,40.002387;116.467895,40.002561"},{"lcode":[],"distance":"16","status":"畅通","polyline":"116.467895,40.002561;116.467886,40.002708"},{"lcode":[],"distance":"180","status":"畅通","polyline":"116.467886,40.002708;116.467812,40.004332"},{"lcode":[],"distance":"38","status":"畅通","polyline":"116.467812,40.004332;116.467791,40.004679"},{"lcode":[],"distance":"28","status":"畅通","polyline":"116.467791,40.004679;116.467778,40.004944"}],"cities":[{"name":"北京城区","citycode":"010","adcode":"110100","districts":[{"name":"朝阳区","adcode":"110105"}]}]},{"instruction":"沿河荫西路向西行驶184米左转","orientation":"西","road":"河荫西路","distance":"184","tolls":"0","toll_distance":"0","toll_road":[],"duration":"29","polyline":"116.467391,40.005004;116.466445,40.004991;116.465234,40.004991","action":"左转","assistant_action":[],"tmcs":[{"lcode":[],"distance":"184","status":"畅通","polyline":"116.467391,40.005004;116.466445,40.004991;116.465234,40.004991"}],"cities":[{"name":"北京城区","citycode":"010","adcode":"110100","districts":[{"name":"朝阳区","adcode":"110105"}]}]},{"instruction":"向南行驶23米左转","orientation":"南","distance":"23","tolls":"0","toll_distance":"0","toll_road":[],"duration":"13","polyline":"116.465226,40.004891;116.465234,40.00474;116.465234,40.004679","action":"左转","assistant_action":[],"tmcs":[{"lcode":[],"distance":"17","status":"未知","polyline":"116.465226,40.004891;116.465234,40.00474"},{"lcode":[],"distance":"6","status":"未知","polyline":"116.465234,40.00474;116.465234,40.004679"}],"cities":[{"name":"北京城区","citycode":"010","adcode":"110100","districts":[{"name":"朝阳区","adcode":"110105"}]}]},{"instruction":"向东行驶6米到达目的地","orientation":"东","distance":"6","tolls":"0","toll_distance":"0","toll_road":[],"duration":"2","polyline":"116.465234,40.004679;116.465304,40.004683","action":[],"assistant_action":"到达目的地","tmcs":[{"lcode":[],"distance":"6","status":"未知","polyline":"116.465234,40.004679;116.465304,40.004683"}],"cities":[{"name":"北京城区","citycode":"010","adcode":"110100","districts":[{"name":"朝阳区","adcode":"110105"}]}]}],"restriction":"0","traffic_lights":"7"}]
         */

        private String origin;
        private String destination;
        private String taxi_cost;
        private List<PathsBean> paths;

        public String getOrigin() {
            return origin;
        }

        public void setOrigin(String origin) {
            this.origin = origin;
        }

        public String getDestination() {
            return destination;
        }

        public void setDestination(String destination) {
            this.destination = destination;
        }

        public String getTaxi_cost() {
            return taxi_cost;
        }

        public void setTaxi_cost(String taxi_cost) {
            this.taxi_cost = taxi_cost;
        }

        public List<PathsBean> getPaths() {
            return paths;
        }

        public void setPaths(List<PathsBean> paths) {
            this.paths = paths;
        }

        public static class PathsBean {
            @Override
            public String toString() {
                return "PathsBean{" +
                        "distance='" + distance + '\'' +
                        ", duration='" + duration + '\'' +
                        ", strategy='" + strategy + '\'' +
                        ", tolls='" + tolls + '\'' +
                        ", toll_distance='" + toll_distance + '\'' +
                        ", restriction='" + restriction + '\'' +
                        ", traffic_lights='" + traffic_lights + '\'' +
                        ", steps=" + steps +
                        '}';
            }

            /**
             * distance : 3031
             * duration : 752
             * strategy : 速度最快
             * tolls : 0
             * toll_distance : 0
             * steps : [{"instruction":"向北行驶109米右转","orientation":"北","distance":"109","tolls":"0","toll_distance":"0","toll_road":[],"duration":"49","polyline":"116.48089,39.989371;116.480551,39.989605;116.480547,39.989657;116.480595,39.989705;116.481094,39.990048","action":"右转","assistant_action":[],"tmcs":[{"lcode":[],"distance":"52","status":"未知","polyline":"116.48089,39.989371;116.480551,39.989605;116.480547,39.989657;116.480595,39.989705"},{"lcode":[],"distance":"57","status":"未知","polyline":"116.480595,39.989705;116.481094,39.990048"}],"cities":[{"name":"北京城区","citycode":"010","adcode":"110100","districts":[{"name":"朝阳区","adcode":"110105"}]}]},{"instruction":"向东南行驶36米右转","orientation":"东南","distance":"36","tolls":"0","toll_distance":"0","toll_road":[],"duration":"22","polyline":"116.481094,39.990048;116.481111,39.99;116.481345,39.989792","action":"右转","assistant_action":[],"tmcs":[{"lcode":[],"distance":"36","status":"未知","polyline":"116.481094,39.990048;116.481111,39.99;116.481345,39.989792"}],"cities":[{"name":"北京城区","citycode":"010","adcode":"110100","districts":[{"name":"朝阳区","adcode":"110105"}]}]},{"instruction":"向西南行驶76米右转进入主路","orientation":"西南","distance":"76","tolls":"0","toll_distance":"0","toll_road":[],"duration":"36","polyline":"116.481345,39.989792;116.481372,39.989753;116.481367,39.989718;116.480937,39.989284;116.480885,39.989223","action":"右转","assistant_action":"进入主路","tmcs":[{"lcode":[],"distance":"69","status":"未知","polyline":"116.481345,39.989792;116.481372,39.989753;116.481367,39.989718;116.480937,39.989284"},{"lcode":[],"distance":"7","status":"未知","polyline":"116.480937,39.989284;116.480885,39.989223"}],"cities":[{"name":"北京城区","citycode":"010","adcode":"110100","districts":[{"name":"朝阳区","adcode":"110105"}]}]},{"instruction":"沿阜荣街向西北行驶682米左转进入主路","orientation":"西北","road":"阜荣街","distance":"682","tolls":"0","toll_distance":"0","toll_road":[],"duration":"211","polyline":"116.480816,39.989154;116.479805,39.989826;116.479727,39.989883;116.479193,39.990234;116.477908,39.991085;116.477574,39.991285;116.477478,39.99135;116.477049,39.991645;116.476024,39.992331;116.474813,39.993116;116.474735,39.993173","action":"左转","assistant_action":"进入主路","tmcs":[{"lcode":[],"distance":"122","status":"畅通","polyline":"116.480816,39.989154;116.479805,39.989826;116.479727,39.989883"},{"lcode":[],"distance":"252","status":"畅通","polyline":"116.479727,39.989883;116.479193,39.990234;116.477908,39.991085;116.477574,39.991285;116.477478,39.99135"},{"lcode":[],"distance":"165","status":"畅通","polyline":"116.477478,39.99135;116.477049,39.991645;116.476024,39.992331"},{"lcode":[],"distance":"135","status":"畅通","polyline":"116.476024,39.992331;116.474813,39.993116"},{"lcode":[],"distance":"8","status":"畅通","polyline":"116.474813,39.993116;116.474735,39.993173"}],"cities":[{"name":"北京城区","citycode":"010","adcode":"110100","districts":[{"name":"朝阳区","adcode":"110105"}]}]},{"instruction":"沿阜安西路向西南行驶259米右转进入主路","orientation":"西南","road":"阜安西路","distance":"259","tolls":"0","toll_distance":"0","toll_road":[],"duration":"52","polyline":"116.474592,39.99316;116.473576,39.992235;116.473477,39.992131;116.47276,39.991502;116.472617,39.991376","action":"右转","assistant_action":"进入主路","tmcs":[{"lcode":[],"distance":"134","status":"畅通","polyline":"116.474592,39.99316;116.473576,39.992235"},{"lcode":[],"distance":"14","status":"畅通","polyline":"116.473576,39.992235;116.473477,39.992131"},{"lcode":[],"distance":"92","status":"畅通","polyline":"116.473477,39.992131;116.47276,39.991502"},{"lcode":[],"distance":"19","status":"畅通","polyline":"116.47276,39.991502;116.472617,39.991376"}],"cities":[{"name":"北京城区","citycode":"010","adcode":"110100","districts":[{"name":"朝阳区","adcode":"110105"}]}]},{"instruction":"沿广顺南大街途径广顺北大街向北行驶1.7千米左转","orientation":"北","road":"广顺南大街","distance":"1656","tolls":"0","toll_distance":"0","toll_road":[],"duration":"338","polyline":"116.472552,39.991315;116.471793,39.991814;116.471406,39.992079;116.470699,39.992535;116.470404,39.992721;116.470304,39.992786;116.469839,39.993099;116.469154,39.993576;116.468958,39.993746;116.468802,39.993906;116.468585,39.994197;116.468472,39.994366;116.468303,39.99477;116.468234,39.995104;116.468207,39.995469;116.468164,39.996675;116.468142,39.996897;116.468121,39.997205;116.468116,39.997283;116.468095,39.997726;116.468051,39.999015;116.467925,40.001194;116.467925,40.00122;116.467925,40.001272;116.467947,40.001628;116.467938,40.001723;116.46793,40.001966;116.467912,40.002387;116.467895,40.002561;116.467886,40.002708;116.467812,40.004332;116.467791,40.004679;116.467778,40.004944","action":"左转","assistant_action":[],"tmcs":[{"lcode":[],"distance":"84","status":"畅通","polyline":"116.472552,39.991315;116.471793,39.991814"},{"lcode":[],"distance":"44","status":"畅通","polyline":"116.471793,39.991814;116.471406,39.992079"},{"lcode":[],"distance":"79","status":"畅通","polyline":"116.471406,39.992079;116.470699,39.992535"},{"lcode":[],"distance":"43","status":"畅通","polyline":"116.470699,39.992535;116.470404,39.992721;116.470304,39.992786"},{"lcode":[],"distance":"52","status":"畅通","polyline":"116.470304,39.992786;116.469839,39.993099"},{"lcode":[],"distance":"163","status":"畅通","polyline":"116.469839,39.993099;116.469154,39.993576;116.468958,39.993746;116.468802,39.993906;116.468585,39.994197"},{"lcode":[],"distance":"146","status":"畅通","polyline":"116.468585,39.994197;116.468472,39.994366;116.468303,39.99477;116.468234,39.995104;116.468207,39.995469"},{"lcode":[],"distance":"134","status":"畅通","polyline":"116.468207,39.995469;116.468164,39.996675"},{"lcode":[],"distance":"24","status":"畅通","polyline":"116.468164,39.996675;116.468142,39.996897"},{"lcode":[],"distance":"42","status":"畅通","polyline":"116.468142,39.996897;116.468121,39.997205;116.468116,39.997283"},{"lcode":[],"distance":"49","status":"畅通","polyline":"116.468116,39.997283;116.468095,39.997726"},{"lcode":[],"distance":"143","status":"畅通","polyline":"116.468095,39.997726;116.468051,39.999015"},{"lcode":[],"distance":"241","status":"畅通","polyline":"116.468051,39.999015;116.467925,40.001194"},{"lcode":[],"distance":"9","status":"畅通","polyline":"116.467925,40.001194;116.467925,40.00122;116.467925,40.001272"},{"lcode":[],"distance":"50","status":"畅通","polyline":"116.467925,40.001272;116.467947,40.001628;116.467938,40.001723"},{"lcode":[],"distance":"26","status":"畅通","polyline":"116.467938,40.001723;116.46793,40.001966"},{"lcode":[],"distance":"47","status":"畅通","polyline":"116.46793,40.001966;116.467912,40.002387"},{"lcode":[],"distance":"18","status":"畅通","polyline":"116.467912,40.002387;116.467895,40.002561"},{"lcode":[],"distance":"16","status":"畅通","polyline":"116.467895,40.002561;116.467886,40.002708"},{"lcode":[],"distance":"180","status":"畅通","polyline":"116.467886,40.002708;116.467812,40.004332"},{"lcode":[],"distance":"38","status":"畅通","polyline":"116.467812,40.004332;116.467791,40.004679"},{"lcode":[],"distance":"28","status":"畅通","polyline":"116.467791,40.004679;116.467778,40.004944"}],"cities":[{"name":"北京城区","citycode":"010","adcode":"110100","districts":[{"name":"朝阳区","adcode":"110105"}]}]},{"instruction":"沿河荫西路向西行驶184米左转","orientation":"西","road":"河荫西路","distance":"184","tolls":"0","toll_distance":"0","toll_road":[],"duration":"29","polyline":"116.467391,40.005004;116.466445,40.004991;116.465234,40.004991","action":"左转","assistant_action":[],"tmcs":[{"lcode":[],"distance":"184","status":"畅通","polyline":"116.467391,40.005004;116.466445,40.004991;116.465234,40.004991"}],"cities":[{"name":"北京城区","citycode":"010","adcode":"110100","districts":[{"name":"朝阳区","adcode":"110105"}]}]},{"instruction":"向南行驶23米左转","orientation":"南","distance":"23","tolls":"0","toll_distance":"0","toll_road":[],"duration":"13","polyline":"116.465226,40.004891;116.465234,40.00474;116.465234,40.004679","action":"左转","assistant_action":[],"tmcs":[{"lcode":[],"distance":"17","status":"未知","polyline":"116.465226,40.004891;116.465234,40.00474"},{"lcode":[],"distance":"6","status":"未知","polyline":"116.465234,40.00474;116.465234,40.004679"}],"cities":[{"name":"北京城区","citycode":"010","adcode":"110100","districts":[{"name":"朝阳区","adcode":"110105"}]}]},{"instruction":"向东行驶6米到达目的地","orientation":"东","distance":"6","tolls":"0","toll_distance":"0","toll_road":[],"duration":"2","polyline":"116.465234,40.004679;116.465304,40.004683","action":[],"assistant_action":"到达目的地","tmcs":[{"lcode":[],"distance":"6","status":"未知","polyline":"116.465234,40.004679;116.465304,40.004683"}],"cities":[{"name":"北京城区","citycode":"010","adcode":"110100","districts":[{"name":"朝阳区","adcode":"110105"}]}]}]
             * restriction : 0
             * traffic_lights : 7
             */

            private String distance;
            private String duration;
            private String strategy;
            private String tolls;
            private String toll_distance;
            private String restriction;
            private String traffic_lights;
            private List<StepsBean> steps;

            public String getDistance() {
                return distance;
            }

            public void setDistance(String distance) {
                this.distance = distance;
            }

            public String getDuration() {
                return duration;
            }

            public void setDuration(String duration) {
                this.duration = duration;
            }

            public String getStrategy() {
                return strategy;
            }

            public void setStrategy(String strategy) {
                this.strategy = strategy;
            }

            public String getTolls() {
                return tolls;
            }

            public void setTolls(String tolls) {
                this.tolls = tolls;
            }

            public String getToll_distance() {
                return toll_distance;
            }

            public void setToll_distance(String toll_distance) {
                this.toll_distance = toll_distance;
            }

            public String getRestriction() {
                return restriction;
            }

            public void setRestriction(String restriction) {
                this.restriction = restriction;
            }

            public String getTraffic_lights() {
                return traffic_lights;
            }

            public void setTraffic_lights(String traffic_lights) {
                this.traffic_lights = traffic_lights;
            }

            public List<StepsBean> getSteps() {
                return steps;
            }

            public void setSteps(List<StepsBean> steps) {
                this.steps = steps;
            }

            public static class StepsBean {
                @Override
                public String toString() {
                    return "StepsBean{" +
                            "instruction='" + instruction + '\'' +
                            ", orientation='" + orientation + '\'' +
                            ", distance='" + distance + '\'' +
                            ", tolls='" + tolls + '\'' +
                            ", toll_distance='" + toll_distance + '\'' +
                            ", duration='" + duration + '\'' +
                            ", polyline=" + polyline +
                            ", action='" + action + '\'' +
                            ", road='" + road + '\'' +
                            ", toll_road=" + toll_road +
                            ", assistant_action=" + assistant_action +
                            ", tmcs=" + tmcs +
                            ", cities=" + cities +
                            '}';
                }

                /**
                 * instruction : 向北行驶109米右转
                 * orientation : 北
                 * distance : 109
                 * tolls : 0
                 * toll_distance : 0
                 * toll_road : []
                 * duration : 49
                 * polyline : 116.48089,39.989371;116.480551,39.989605;116.480547,39.989657;116.480595,39.989705;116.481094,39.990048
                 * action : 右转
                 * assistant_action : []
                 * tmcs : [{"lcode":[],"distance":"52","status":"未知","polyline":"116.48089,39.989371;116.480551,39.989605;116.480547,39.989657;116.480595,39.989705"},{"lcode":[],"distance":"57","status":"未知","polyline":"116.480595,39.989705;116.481094,39.990048"}]
                 * cities : [{"name":"北京城区","citycode":"010","adcode":"110100","districts":[{"name":"朝阳区","adcode":"110105"}]}]
                 * road : 阜荣街
                 */

                private String instruction;
                private String orientation;
                private String distance;
                private String tolls;
                private String toll_distance;
                private String duration;
                private ArrayList<LatLng> polyline;
                private String action;
                private String road;
                private List<?> toll_road;
                private List<?> assistant_action;
                private List<TmcsBean> tmcs;
                private List<CitiesBean> cities;

                public String getInstruction() {
                    return instruction;
                }

                public void setInstruction(String instruction) {
                    this.instruction = instruction;
                }

                public String getOrientation() {
                    return orientation;
                }

                public void setOrientation(String orientation) {
                    this.orientation = orientation;
                }

                public String getDistance() {
                    return distance;
                }

                public void setDistance(String distance) {
                    this.distance = distance;
                }

                public String getTolls() {
                    return tolls;
                }

                public void setTolls(String tolls) {
                    this.tolls = tolls;
                }

                public String getToll_distance() {
                    return toll_distance;
                }

                public void setToll_distance(String toll_distance) {
                    this.toll_distance = toll_distance;
                }

                public String getDuration() {
                    return duration;
                }

                public void setDuration(String duration) {
                    this.duration = duration;
                }

                public ArrayList<LatLng> getPolyline() {
                    return polyline;
                }

                public void setPolyline(ArrayList<LatLng> polyline) {
                    this.polyline = polyline;
                }

                public String getAction() {
                    return action;
                }

                public void setAction(String action) {
                    this.action = action;
                }

                public String getRoad() {
                    return road;
                }

                public void setRoad(String road) {
                    this.road = road;
                }

                public List<?> getToll_road() {
                    return toll_road;
                }

                public void setToll_road(List<?> toll_road) {
                    this.toll_road = toll_road;
                }

                public List<?> getAssistant_action() {
                    return assistant_action;
                }

                public void setAssistant_action(List<?> assistant_action) {
                    this.assistant_action = assistant_action;
                }

                public List<TmcsBean> getTmcs() {
                    return tmcs;
                }

                public void setTmcs(List<TmcsBean> tmcs) {
                    this.tmcs = tmcs;
                }

                public List<CitiesBean> getCities() {
                    return cities;
                }

                public void setCities(List<CitiesBean> cities) {
                    this.cities = cities;
                }

                public static class TmcsBean {
                    /**
                     * lcode : []
                     * distance : 52
                     * status : 未知
                     * polyline : 116.48089,39.989371;116.480551,39.989605;116.480547,39.989657;116.480595,39.989705
                     */

                    private String distance;
                    private String status;
                    private String polyline;
                    private List<?> lcode;

                    public String getDistance() {
                        return distance;
                    }

                    public void setDistance(String distance) {
                        this.distance = distance;
                    }

                    public String getStatus() {
                        return status;
                    }

                    public void setStatus(String status) {
                        this.status = status;
                    }

                    public String getPolyline() {
                        return polyline;
                    }

                    public void setPolyline(String polyline) {
                        this.polyline = polyline;
                    }

                    public List<?> getLcode() {
                        return lcode;
                    }

                    public void setLcode(List<?> lcode) {
                        this.lcode = lcode;
                    }
                }

                public static class CitiesBean {
                    /**
                     * name : 北京城区
                     * citycode : 010
                     * adcode : 110100
                     * districts : [{"name":"朝阳区","adcode":"110105"}]
                     */

                    private String name;
                    private String citycode;
                    private String adcode;
                    private List<DistrictsBean> districts;

                    public String getName() {
                        return name;
                    }

                    public void setName(String name) {
                        this.name = name;
                    }

                    public String getCitycode() {
                        return citycode;
                    }

                    public void setCitycode(String citycode) {
                        this.citycode = citycode;
                    }

                    public String getAdcode() {
                        return adcode;
                    }

                    public void setAdcode(String adcode) {
                        this.adcode = adcode;
                    }

                    public List<DistrictsBean> getDistricts() {
                        return districts;
                    }

                    public void setDistricts(List<DistrictsBean> districts) {
                        this.districts = districts;
                    }

                    public static class DistrictsBean {
                        /**
                         * name : 朝阳区
                         * adcode : 110105
                         */

                        private String name;
                        private String adcode;

                        public String getName() {
                            return name;
                        }

                        public void setName(String name) {
                            this.name = name;
                        }

                        public String getAdcode() {
                            return adcode;
                        }

                        public void setAdcode(String adcode) {
                            this.adcode = adcode;
                        }
                    }
                }
            }
        }
    }
}
