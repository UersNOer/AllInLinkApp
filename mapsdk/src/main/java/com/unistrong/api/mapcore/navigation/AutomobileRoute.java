package com.unistrong.api.mapcore.navigation;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.unistrong.api.maps.model.LatLng;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;

//驾车路径规划
public class AutomobileRoute {
    private String origin;//出发点
    private String destination;//目的地
    private Handler handler;

    public AutomobileRoute(String origin, String destination) {
        this.origin = origin;
        this.destination = destination;
    }

    public void setHandle(android.os.Handler handler) {
        this.handler = handler;
    }

    public void doSearch() {
        if (handler == null) {
            return;
        }
        Log.i("TAG", "开始查询路线规划");
        new Thread(new Runnable() {
            @Override
            public void run() {
                InputStream input = null;
                HttpURLConnection httpURLConnection = null; //&strategy=10
                String url_path = "https://restapi.amap.com/v3/direction/driving?origin=" + origin + "&destination=" + destination + "&extensions=all&output=json&key=c0d2f3afcc4cc18a05d976d242dc3a70&strategy=10";
                Log.i("TAG", url_path);
                //模拟参数
                try {
                    URL url = null;
                    if (url_path.equals("")) {
                    }
                    url = new URL(url_path);
                    httpURLConnection = (HttpURLConnection) url.openConnection();
                    httpURLConnection.setDoInput(true);
                    httpURLConnection.setDoOutput(true);    //可以创建输出流，将请求参数写入
                    httpURLConnection.setRequestMethod("GET"); //请求方式为POST
                    Iterator<String> keyIterator;
                    int byteLength;
                    byte[] buffer = new byte[1024];
                    StringBuilder builder = new StringBuilder();
                    input = httpURLConnection.getInputStream();
                    while ((byteLength = input.read(buffer)) != -1)
                        builder.append(new String(buffer, 0, byteLength, "UTF-8"));
                    Log.i("TAG", builder.toString());

                    ArrayList<AutoMobileEntity.RouteBean.PathsBean> pathsBeans = jsonToEntity(builder.toString());
                    Message message = Message.obtain();
                    message.what = 1;
                    message.obj = pathsBeans;
                    handler.sendMessage(message);
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    try {
                        /**
                         * 关闭连接/流
                         */

                        if (httpURLConnection != null) {
                            httpURLConnection.disconnect();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }

    private ArrayList<AutoMobileEntity.RouteBean.PathsBean> jsonToEntity(String json) throws JSONException {
        ArrayList<AutoMobileEntity.RouteBean.PathsBean> pathsBeans = new ArrayList<>();
        JSONObject jsonObject = new JSONObject(json);



        JSONObject route = jsonObject.getJSONObject("route");
        JSONArray paths = route.getJSONArray("paths");
        for (int i = 0; i < paths.length(); i++) {
            AutoMobileEntity.RouteBean.PathsBean pathsBean = new AutoMobileEntity.RouteBean.PathsBean();
            JSONObject pathListObj = paths.getJSONObject(i);
            pathsBean.setDistance(pathListObj.getString("distance"));
            pathsBean.setDuration(pathListObj.getString("duration"));
            pathsBean.setStrategy(pathListObj.getString("strategy"));
            pathsBean.setTolls(pathListObj.getString("tolls"));
            pathsBean.setTraffic_lights(pathListObj.getString("traffic_lights"));
            ArrayList<AutoMobileEntity.RouteBean.PathsBean.StepsBean> stepsBeanArrayList = new ArrayList<>();
            JSONArray steps = pathListObj.getJSONArray("steps");
            for (int o = 0; o < steps.length(); o++) {
                JSONObject stepsJSONObject = steps.getJSONObject(o);
                AutoMobileEntity.RouteBean.PathsBean.StepsBean stepsBean = new AutoMobileEntity.RouteBean.PathsBean.StepsBean();
                ArrayList<LatLng> latLngs = new ArrayList<>();
                stepsBean.setInstruction(stepsJSONObject.getString("instruction"));
                stepsBean.setOrientation(stepsJSONObject.getString("orientation"));
                String[] split = stepsJSONObject.getString("polyline").split(";");
                for (String q : split) {
                    String[] split1 = q.split(",");
                    Double lng = Double.parseDouble(split1[1]) + 0.0012;
                    Double lat = Double.parseDouble(split1[0]) - 0.0059;
//                    Double lng = Double.parseDouble(split1[1]);
//                    Double lat = Double.parseDouble(split1[0]);
                    latLngs.add(new LatLng(lng, lat));
                }

                stepsBean.setPolyline(latLngs);
                stepsBeanArrayList.add(stepsBean);

            }
            pathsBean.setSteps(stepsBeanArrayList);
            pathsBeans.add(pathsBean);
        }
        return pathsBeans;
    }

    private boolean checkCode(int code) {
        switch (code) {
            case 0:
                return true;
            default:
                return false;
        }
    }
}
