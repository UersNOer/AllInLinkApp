package com.unistrong.api.mapcore.tdservices;

import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;

import com.unistrong.api.maps.model.LatLng;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CoverageUtils {
    public static final int READ_OK = 1000;
    public static final int READ_ALL_CITYOK = 1002;
    public static final int READ_ONE_CITYOK = 1003;


    public static final int LINE_XUANZANG = 2001;
    public static final int LINE_SEA_SLink = 2002;
    public static final int LINE_Land_SLink = 3002;


    /**
     * 学校医院分布Task
     */
    public static class SchoolAsyncTask extends AsyncTask {
        String keyWord = "";
        android.os.Handler handler;

        public SchoolAsyncTask(int type, android.os.Handler c) {
            this.handler = c;
            if (type == 1) {
                keyWord = "kindergarten";
            } else if (type == 2) {
                keyWord = "secondary";
            } else if (type == 3) {
                keyWord = "primary_grade";
            } else if (type == 4) {
                keyWord = "universities";
            } else if (type == 5) {
                keyWord = "technical_school";
            } else if (type == 6) {
                keyWord = "clinic";
            } else if (type == 7) {
                keyWord = "polyclinic";
            } else if (type == 8) {
                keyWord = "specialized";

            }
        }


        @Override
        protected Object doInBackground(Object[] objects) {
            InputStream is = null;
            School school = new School();

            try {
                is = CoverageUtils.class.getResourceAsStream("/assets/json/distribute/" + keyWord);
                int byteLength;
                byte[] buffer = new byte[1024];
                StringBuilder builder = new StringBuilder();
                while ((byteLength = is.read(buffer)) != -1)
                    builder.append(new String(buffer, 0, byteLength, "UTF-8"));
                is.close();
                School.StatisticsBean statisticsBean = new School.StatisticsBean();
                JSONObject jsonObject = new JSONObject(builder.toString());
                school.setCount(jsonObject.getString("count"));
                school.setKeyWord(jsonObject.getString("keyWord"));
                school.setResultType(jsonObject.getInt("resultType"));
                JSONObject statistics = jsonObject.getJSONObject("statistics");
                JSONArray allAdmins = statistics.getJSONArray("allAdmins");
                List<School.StatisticsBean.AllAdminsBean> allAdminsBeans = new ArrayList<>();
                for (int i = 0; i < allAdmins.length(); i++) {
                    School.StatisticsBean.AllAdminsBean allAdminsBean = new School.StatisticsBean.AllAdminsBean();
                    JSONObject allAdminsJSONObject = allAdmins.getJSONObject(i);

                    if (!allAdminsJSONObject.has("childAdmins")) {
                        break;
                    }
                    JSONArray childAdminsBean = allAdminsJSONObject.getJSONArray("childAdmins");

                    List<School.StatisticsBean.AllAdminsBean.ChildAdminsBean> childAdminsBeans = new ArrayList<>();
                    for (int o = 0; o < childAdminsBean.length(); o++) {
                        School.StatisticsBean.AllAdminsBean.ChildAdminsBean ChildAdminsBean = new School.StatisticsBean.AllAdminsBean.ChildAdminsBean();


                        JSONObject childAdminsBeanJSONObject = childAdminsBean.getJSONObject(o);
                        if (!childAdminsBeanJSONObject.has("areaStatistics")) {
                            break;
                        }
                        JSONArray areaStatistics = childAdminsBeanJSONObject.getJSONArray("areaStatistics");

                        ArrayList areStatisticsList = new ArrayList();
                        for (int p = 0; p < areaStatistics.length(); p++) {
                            JSONObject jsonObject1 = areaStatistics.getJSONObject(p);
                            School.StatisticsBean.AllAdminsBean.ChildAdminsBean.AreaStatisticsBeanX areaStatisticsBeanX = new School.StatisticsBean.AllAdminsBean.ChildAdminsBean.AreaStatisticsBeanX();
                            areaStatisticsBeanX.setLat(jsonObject1.getDouble("lat"));
                            areaStatisticsBeanX.setLon(jsonObject1.getDouble("lon"));
                            areaStatisticsBeanX.setCount(jsonObject1.getInt("count"));
                            areStatisticsList.add(areaStatisticsBeanX);
                        }
                        ChildAdminsBean.setLat(childAdminsBeanJSONObject.getDouble("lat"));
                        ChildAdminsBean.setLon(childAdminsBeanJSONObject.getDouble("lon"));
                        ChildAdminsBean.setCount(childAdminsBeanJSONObject.getInt("count"));
                        ChildAdminsBean.setAreaStatistics(areStatisticsList);
                        childAdminsBeans.add(ChildAdminsBean);


                    }
                    allAdminsBean.setChildAdmins(childAdminsBeans);

                    allAdminsBeans.add(allAdminsBean);

                }
                statisticsBean.setAllAdmins(allAdminsBeans);
                school.setStatistics(statisticsBean);
                Message message = Message.obtain();
                message.what = READ_OK;
                message.obj = school;
                handler.sendMessage(message);
            } catch (IOException | JSONException e) {
                e.printStackTrace();
            }
            return objects;
        }
    }

    public static class WayTask extends AsyncTask {

        private String keyWord;
        private Handler handler;

        private int requestCode;

        public WayTask(int type, Handler handler) {
            this.handler = handler;
            switch (type) {
                case 1:
                    keyWord = "xuanzang";
                    requestCode = LINE_XUANZANG;
                    break;
                case 2:
                    keyWord = "sea_link";
                    requestCode = LINE_SEA_SLink;
                    break;
                case 3:
                    keyWord = "link_link";
                    requestCode = LINE_Land_SLink;
                    break;
            }
        }

        @Override
        protected Object doInBackground(Object[] objects) {
            InputStream is = null;
            try {
                ArrayList<LatLng> latLngs = new ArrayList<>();
                is = CoverageUtils.class.getResourceAsStream("/assets/json/way/" + keyWord);
                int byteLength;
                byte[] buffer = new byte[1024];
                StringBuilder builder = new StringBuilder();
                while ((byteLength = is.read(buffer)) != -1)
                    builder.append(new String(buffer, 0, byteLength, "UTF-8"));
                is.close();
                JSONObject jsonObject = new JSONObject(builder.toString());
                JSONArray features = jsonObject.getJSONArray("features");
                for (int i = 0; i < features.length(); i++) {
                    JSONObject feature = features.getJSONObject(i);
                    JSONObject geometry = feature.getJSONObject("geometry");
                    JSONArray coordinates = geometry.getJSONArray("coordinates");
                    for (int o = 0; o < coordinates.length(); o++) {
                        JSONArray latlng = coordinates.getJSONArray(o);
                        LatLng latLng = new LatLng(latlng.getDouble(1), latlng.getDouble(0));
                        latLngs.add(latLng);
                    }
                }
                Message message = Message.obtain();
                message.what = requestCode;
                message.obj = latLngs;
                handler.sendMessage(message);
            } catch (Exception e) {
                e.printStackTrace();

            }
            return null;
        }
    }

    public static class AreaTask extends AsyncTask {
        Handler handler;
        String[] names = new String[]{
                "anhui",
                "beijing",
                "chongqing",
                "fujian",
                "gansu",
                "guangdong",
                "guangxi",
                "guizhou",
                "hainan",
                "hebei_1",
                "heilongjiang",
                "henan",
                "hubei",
                "hunan",
                "jiangsu",
                "jiangxi",
                "jilin",
                "liaoning",
                "neimenggu",
                "ningxia",
                "qinghai",
                "shandong",
                "shanghai",
                "shanxi",
                "shanxi_1",
                "sichuan",
                "taiwan",
                "tianjin",
                "xinjiang",
                "xizang",
                "yunan",
                "zhejiang",

        };

        public AreaTask(Handler handler) {
            this.handler = handler;
        }

        @Override
        protected Object doInBackground(Object[] objects) {
            HashMap<Integer, ArrayList<LatLng>> map = new HashMap<>();
            for (String name : names) {
                ArrayList<LatLng> latlngList = new ArrayList<>();
                InputStream is = null;
                try {
                    is = CoverageUtils.class.getResourceAsStream("/assets/json/area/" + name + ".txt");
                    int byteLength;
                    byte[] buffer = new byte[1024];
                    StringBuilder builder = new StringBuilder();
                    while ((byteLength = is.read(buffer)) != -1)
                        builder.append(new String(buffer, 0, byteLength, "UTF-8"));
                    is.close();
                    String allLatlng = builder.toString();
                    String[] split = allLatlng.split(";");
                    for (String q : split) {
                        String[] split1 = q.split(",");
                        String lat = split1[1];
                        String lon = split1[0];
                        LatLng latLng = new LatLng(Double.parseDouble(lat), Double.parseDouble(lon));
                        latlngList.add(latLng);
                    }
                    map.put(CityUtils.getCityCode(name), latlngList);
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            Message message = Message.obtain();
            message.what = READ_ALL_CITYOK;
            message.obj = map;
            handler.sendMessage(message);
            return null;

        }
    }

    /**
     * 获得单一城市的行政区域
     */
    public static class CityAreaTask extends AsyncTask {
        Handler handler;
        String name;

        /**
         * @param handler handler回调
         * @param name    城市拼音，陕西 为 shanxi_1  山西为 shanxi  河北分为hebei_1  hebei_2
         */
        public CityAreaTask(Handler handler, String name) {
            this.handler = handler;
            this.name = name;
        }

        @Override
        protected Object doInBackground(Object[] objects) {
            ArrayList<LatLng> latlngList = new ArrayList<>();
            InputStream is = null;
            try {
                is = CoverageUtils.class.getResourceAsStream("/assets/json/area/" + name + ".txt");
                int byteLength;
                byte[] buffer = new byte[1024];
                StringBuilder builder = new StringBuilder();
                while ((byteLength = is.read(buffer)) != -1)
                    builder.append(new String(buffer, 0, byteLength, "UTF-8"));
                is.close();
                String allLatlng = builder.toString();
                String[] split = allLatlng.split(";");
                for (String q : split) {
                    String[] split1 = q.split(",");
                    String lat = split1[1];
                    String lon = split1[0];
                    latlngList.add(new LatLng(Double.parseDouble(lat), Double.parseDouble(lon)));
                }

            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            Message message = Message.obtain();
            message.what = READ_ONE_CITYOK;
            message.obj = latlngList;
            handler.sendMessage(message);
            return null;

        }
    }
}