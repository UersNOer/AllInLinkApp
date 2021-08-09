package com.leador.mapcore.geojson;

import android.text.TextUtils;

import com.unistrong.api.maps.MapController;
import com.unistrong.api.maps.MapsInitializer;
import com.unistrong.api.maps.model.LatLng;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.Proxy;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;


public class GeoJsonWorkerTask implements Runnable {
    volatile boolean inRequest = false;
    volatile boolean isTimeOut = false;
    volatile boolean isFinished = false;
    volatile boolean mCanceled = false;
    int timeOutCount=0;
    private String url="https://cloudmap.ishowchina.com/gds";
        private String path="/search/bbox";
    private String parmas="";
    private String key="";
    GeoJsonDownTile tile;
    GeoJsonCallback callback;
    MapController.GeoJsonServerListener serverListener;
    CacheManager cacheManager;
    public HttpURLConnection httpURLConnection = null;
    public GeoJsonWorkerTask(GeoJsonDownTile tile,GeoJsonCallback callback,String key,
                             MapController.GeoJsonServerListener serverListener,CacheManager cacheManager){
        this.tile=tile;
        this.key=key;
        this.callback=callback;
        this.serverListener = serverListener;
        this.cacheManager=cacheManager;
    }
    @Override
    public void run() {
        doRequest();
    }
    public boolean hasFinished(){
        return (this.mCanceled) || (this.isFinished);
    }
    public GeoJsonDownTile getTile(){
        return this.tile;
    }

    public void doRequest() {
        if ((this.mCanceled) || (this.isFinished)) {
            return;
        }
        if (!MapsInitializer.getNetWorkEnable()) {
            return;
        }

        InputStream localInputStream = null;
        this.inRequest = true;
        try {
            Proxy localProxy = null;
            String url = getURL();
            if (TextUtils.isEmpty(url)){
                return;
            }
            if (localProxy != null) {
                this.httpURLConnection = ((HttpURLConnection) new URL(url).openConnection(localProxy));
            } else {
                this.httpURLConnection = ((HttpURLConnection) new URL(url).openConnection());
            }
            this.httpURLConnection.setConnectTimeout(20000);
            this.httpURLConnection.setRequestMethod("GET");
            if (this.httpURLConnection != null) {
                this.httpURLConnection.connect();
                if (this.httpURLConnection.getResponseCode() == 200) {
                    localInputStream = this.httpURLConnection.getInputStream();
                    BufferedReader br = new BufferedReader(new InputStreamReader(localInputStream));
                    StringBuffer buffer = new StringBuffer();
                    String temp = null;

                    while ((temp = br.readLine()) != null) {
                        if (this.mCanceled) {
                            return;
                        }
                        buffer.append(temp);
                    }
                    List<GeoJsonItem> result = parseResult(buffer.toString());
                    cacheManager.saveGeoJsonData(tile.getId_tile(), result);
                    if (this.callback != null) callback.onCallback(tile.getId_tile(), result);
                } else {
                    OnException(this.httpURLConnection.getResponseCode());
                }
            } else {
                OnException(1002);
            }
            return;
        }catch (SocketTimeoutException ex){
            isTimeOut=true;
            timeOutCount++;
        } catch (IllegalArgumentException localIllegalArgumentException) {
//            localIllegalArgumentException.printStackTrace();
        } catch (SecurityException localSecurityException) {
//            localSecurityException.printStackTrace();
        } catch (OutOfMemoryError localOutOfMemoryError) {
//            SDKLogHandler.exception(localOutOfMemoryError, "GeoJsonTaskManager", "doAsyncRequest");
//            localOutOfMemoryError.printStackTrace();
        } catch (IllegalStateException localIllegalStateException) {
//            localIllegalStateException.printStackTrace();
        } catch (IOException localIOException6) {
            OnException(1002);
        } catch (NullPointerException localNullPointerException) {
//            localNullPointerException.printStackTrace();
        } finally {
            this.isFinished=true;
            if ((localInputStream != null) && (!this.mCanceled)) {
                try {
                    localInputStream.close();
                } catch (IOException localIOException9) {
                    OnException(1002);
                }
            }
        }
    }

    private String getMapAddress() {
        return url;
    }
    private String getServerPath(){
        return path;
}

    private String getRequeatParma() {
        if(tile!=null){
            String bbox="";
            if(tile.getCoords()!=null&&tile.getCoords().length>=2)
             bbox=tile.getCoords()[0].longitude+","+tile.getCoords()[0].latitude+";"
                     +tile.getCoords()[1].longitude+","+tile.getCoords()[1].latitude;
            parmas+="bbox="+bbox+"&type=geojson&datasetId="+tile.getDataSetId()+"&ak="+ this.key;
        }
        return parmas;
    }

    protected String getURL() {
        if (this.serverListener != null){
            String url = serverListener.getGegJsonServerUrl(tile.getCoords()[0],
                    tile.getCoords()[1], tile.getId_tile());
            return url;
        }
        String str2 = "";
        String str1 = "?";
        str1 += getRequeatParma();
        str2 = getMapAddress() + getServerPath() + str1;
        return str2;
    }
    private void OnException(int paramInt) {
        this.isFinished = true;
        synchronized (this.callback){
        if(this.callback!=null)callback.onError(paramInt,"");}
    }
    public synchronized void doCancel() {
        if (!this.mCanceled && !this.isFinished) {
            this.mCanceled = true;
            try {
                if (this.httpURLConnection != null && this.inRequest) {
                    this.httpURLConnection.disconnect();
                }
            } catch (Throwable var5) {
//                SDKLogHandler.exception(var5, "GeoJsonTaskManager", "doAsyncRequest");
            }

        }
    }
    //解析json
    public List<GeoJsonItem> parseResult(String json){
        if(json==null||"".equals(json))return null;
        JSONObject ct = null;
        List<GeoJsonItem> itemsList=null;
        try {
            ct = new JSONObject(json);
            if(ct!=null){
                itemsList=new ArrayList<>();
                boolean isHas=ct.has("features");
                boolean isNull=ct.isNull("features");
                if(isHas&&!isNull){

                JSONArray features=ct.getJSONArray("features");
                if(features!=null){
                    for(int i=0;i<features.length();i++) {
                        JSONObject itemObj = features.getJSONObject(i);
                    if(itemObj!=null){
                        GeoJsonItem item=new GeoJsonItem();
                        if(itemObj.has("geometry")){
                        JSONObject geoObj=itemObj.getJSONObject("geometry");
                        if(geoObj!=null){
                            if(geoObj.has("type")){
                                String type=geoObj.getString("type");
                                if("Point".equals(type)){
                                    item.setType(1);
                                }else if("LineString".equals(type)){
                                    item.setType(2);
                                }else if("Polygon".equals(type)){
                                    item.setType(3);
                                }
                            }
                            if(geoObj.has("coordinates")){
                                JSONArray coordArray=geoObj.getJSONArray("coordinates");
                                if(coordArray!=null){
                                    if(item.getType()==1){
                                        if(coordArray.length()==2){
                                           double lat= coordArray.getDouble(0);
                                           double lng=coordArray.getDouble(1);
                                           item.setCoords(new LatLng[]{new LatLng(lat,lng)});
                                        }
                                    }else if(item.getType()==2){
                                            LatLng[] latLngs=new LatLng[coordArray.length()];
                                                for(int j=0;j<coordArray.length();j++){
                                                    JSONArray array=coordArray.getJSONArray(j);
                                                    if(array!=null&&array.length()==2){
                                                        double lat= array.getDouble(0);
                                                        double lng=array.getDouble(1);
                                                        latLngs[j]=new LatLng(lat,lng);
                                                    }
                                                }
                                        item.setCoords(latLngs);
                                    }else if(item.getType()==3){
                                        if(coordArray.length()>0) {
                                            JSONArray arr=coordArray.getJSONArray(0);
                                            if(arr!=null){
                                            LatLng[] latLngs = new LatLng[arr.length()];
                                            for (int j = 0; j < arr.length(); j++) {
                                                JSONArray array = arr.getJSONArray(j);
                                                if (array != null && array.length() == 2) {
                                                    double lat = array.getDouble(0);
                                                    double lng = array.getDouble(1);
                                                    latLngs[j] = new LatLng(lat, lng);
                                                }
                                            }
                                            item.setCoords(latLngs);
                                        }
                                    }
                                    }
                                }
                            }
                        }
                        }
                        if(itemObj.has("properties")){
                            JSONObject proObj=itemObj.getJSONObject("properties");
                            if(proObj!=null){
                                Iterator<String> keys=proObj.keys();
                                Map<String,String> extend=new HashMap<>();
                                while (keys.hasNext()){
                                    String key=keys.next();
                                    if("id".equals(key)){
                                        item.setId(proObj.getInt("id"));
                                    }else{
                                        if(proObj.isNull(key))
                                             extend.put(key,"");
                                        else{
                                            extend.put(key,proObj.getString(key));
                                        }
                                    }
                                }
                                item.setEntend(extend);
                            }
                        }
                        itemsList.add(item);
                    }
                    }
                }
                }
            }
            return itemsList;
        } catch (JSONException var5) {
//            SDKLogHandler.exception(var5, "GeoJsonTaskManager", "doAsyncRequest");
            var5.printStackTrace();
        }
        return null;
    }

}
