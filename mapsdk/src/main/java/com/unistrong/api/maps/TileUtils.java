package com.unistrong.api.maps;

import android.text.TextUtils;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by zhaokai on 2020/5/28.
 */

public class TileUtils {

    //google地图
    public static final String GOOGLE_HYBRID = "Google-Hybrid";
    public static final String GOOGLE_SAT = "Google-Sat";
    public static final String GOOGLE_ROADS = "Google-Roads";
    public static final String GOOGLE_TERRAIN = "Google-Terrain";
    public static final String GOOGLE_TERRAIN_HYBRID = "Google-Terrain-Hybrid";
    //高德地图
    public static final String GAODE_SATELLITE = "Google-satellite";
    public static final String GAODE_DIAGRAM = "Google-diagram";
    public static final String GAODE_DETAIL = "Google-detail";
    //ARG地图
    public static final String ARG_COMMUNITY_MOBILE = "ARG_MOBILE";
    public static final String ARG_COMMUNITY_ENG = "ARG_ENT";
    public static final String ARG_STREE_GRAY = "ARG_GRAY";
    public static final String ARG_STREE_PURPLISHBULE = "ARG_PURPLISHBLUE";
    public static final String ARG_STREE_WARM = "ARG_WARM";


    /**
     * @param sb    TileURL地址
     * @param layer 瓦片样式
     */
    public static void formatTileLayerUrl(StringBuffer sb, String layer) {
        switch (layer) {
            //Google 地图
            case GOOGLE_HYBRID:
                sb.append("/vt/lyrs=y&scale=2&hl=zh-CN&gl=CN&src=app&x=%d&y=%d&z=%d");
                break;
            case GOOGLE_SAT:
                sb.append("/vt/lyrs=s&scale=2&hl=zh-CN&gl=CN&src=app&x=%d&y=%d&z=%d");
                break;
            case GOOGLE_ROADS:
                sb.append("/vt/lyrs=m&scale=2&hl=zh-CN&gl=CN&src=app&x=%d&y=%d&z=%d");
                break;
            case GOOGLE_TERRAIN:
                sb.append("/vt/lyrs=t&scale=2&hl=zh-CN&gl=CN&src=app&x=%d&y=%d&z=%d");
                break;
            case GOOGLE_TERRAIN_HYBRID:
                sb.append("/vt/lyrs=p&scale=2&hl=zh-CN&gl=CN&src=app&x=%d&y=%d&z=%d");
                break;

            //高德 地图
            case GAODE_SATELLITE:
                sb.append("/appmaptile?x=%d&y=%d&z=%d&style=6");
                break;
            case GAODE_DIAGRAM:
                sb.append("/appmaptile?x=%d&y=%d&z=%d&size=1&scale=1&style=7");
                break;
            case GAODE_DETAIL:
                sb.append("/appmaptile?x=%d&y=%d&z=%d&size=1&scale=1&style=8");
                break;

            //ARG 地图格式
            case ARG_COMMUNITY_MOBILE:
                sb.append("/arcgis/rest/services/ChinaOnlineCommunity_Mobile/MapServer/tile/%d/%d/%d");
                break;
            case ARG_COMMUNITY_ENG:
                sb.append("/arcgis/rest/services/ChinaOnlineCommunityENG/MapServer/tile/%d/%d/%d");
                break;
            case ARG_STREE_GRAY:
                sb.append("/arcgis/rest/services/ChinaOnlineStreetGray/MapServer/tile/%d/%d/%d");
                break;
            case ARG_STREE_PURPLISHBULE:
                sb.append("/arcgis/rest/services/ChinaOnlineStreetPurplishBlue/MapServer/tile/%d/%d/%d");
                break;
            case ARG_STREE_WARM:
                sb.append("/arcgis/rest/services/ChinaOnlineStreetWarm/MapServer/tile/%d/%d/%d");
                break;
            default:
                break;
        }
    }

    /**
     * 有些地图是 zoom x y 格式  有些是 zoom y x 或者 y x zoom 判断layer进行追加
     *
     * @param uri              瓦片url
     * @param filePath         缓存地址
     * @param layer            瓦片layer
     * @param zoom             缩放等级
     * @param x                x坐标
     * @param y                y坐标
     * @param diskCacheEnabled
     * @return URL
     */


    public static URL formatUrl(String uri, String filePath, String layer, int zoom, int x, int y, boolean diskCacheEnabled) {

        if (layer.toString().equals(ARG_COMMUNITY_MOBILE) || layer.toString().equals(ARG_COMMUNITY_ENG) || layer.toString().equals(ARG_STREE_GRAY)) {
            uri = String.format(uri.toString(), zoom, y, x);

        } else if (layer.toString().equals(ARG_STREE_PURPLISHBULE) || layer.toString().equals(ARG_STREE_WARM)) {
            uri = String.format(uri.toString(), zoom, x, y);

        } else
            uri = String.format(uri.toString(), x, y, zoom);

        if (!diskCacheEnabled) {
            try {
                return new URL(uri);
            } catch (MalformedURLException e1) {
                e1.printStackTrace();
            }
        } else {
            try {
                String saveFilePath = TextUtils.isEmpty(filePath) ? "/storage/emulated/0/amap/OMCcache/" + layer : filePath;
                final File file = new File(saveFilePath + "/" + zoom + "_" + x + "_" + y + ".data");
                if (file.exists()) {
                    return new URL("file://" + file.getAbsolutePath());
                } else {
                    File file1 = new File(saveFilePath);
                    if (!file1.exists()) {
                        file1.mkdirs();
                    }
                    if (saveFile(uri, file)) {

                        return new URL("file://" + file.getAbsolutePath());
                    } else {
                        new URL(uri);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;

    }


    public static URL formatUrl(String uri, String filePath, int zoom, int x, int y, boolean diskCacheEnabled) {
        if (uri.toString().contains("http://map.xg.ha.cn:8082") || uri.contains("http://10.16.68.120:8082")) {
//            diskCacheEnabled = true;
            uri = String.format(uri.toString() + "/geoesb/proxy/b90c8da39ca84b91a06c7f414599a630/5c3ec122f5af43daad484d3be5349f62?layer=PUBDLG2019HKG_564&style=default&tilematrixset=CustomCRS4490ScalePUBDLG2019HKG_564&Service=WMTS&Request=GetTile&Version=1.0.0&Format=PNG&TileMatrix=%d&TileCol=%d&TileRow=%d", zoom - 6, x, y);
        } else if (uri.toString().contains("map.geoq.cn/arcgis/rest/services"))
            uri = String.format(uri.toString(), zoom, y, x);
        else if (uri.toString().contains("https://api.map.baidu.com") || uri.toString().contains("tianditu.gov.cn"))
            uri = String.format(uri.toString(), x, y, zoom);

        else
            uri = String.format(uri.toString(), zoom, x, y);
        if (!diskCacheEnabled) {
            try {
                return new URL(uri);
            } catch (MalformedURLException e1) {
                e1.printStackTrace();
            }
        } else {
            try {
                String[] split = uri.replaceAll("//", "/").split("/");

                String layer;

                if (uri.contains("tianditu.gov.cn")) {
                    String typeName;
                    if(split[2].length()<7){
                        typeName=split[2];
                        layer = split[1] + "_" + typeName;
                    }else{
                        typeName = split[2].split("\\?")[1].split("=")[1];
                        layer = split[1] + "_" + typeName;
                    }

                } else
                    layer = split[1];
                String saveFilePath = TextUtils.isEmpty(filePath) ? "/storage/emulated/0/amap/OMCcache/" + layer : filePath;
                final File file = new File(saveFilePath + "/" + zoom + "_" + x + "_" + y + ".data");
                if (file.exists()) {
                    return new URL("file://" + file.getAbsolutePath());
                } else {
                    File file1 = new File(saveFilePath);
                    if (!file1.exists()) {
                        file1.mkdirs();
                    }
                    if (saveFile(uri, file)) {

                        return new URL("file://" + file.getAbsolutePath());
                    } else {
                        new URL(uri);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;


    }

    private static boolean saveFile(String titleUrl, File file) throws IOException {
        HttpURLConnection conn = null;
        InputStream is = null;
        FileOutputStream fos = null;
        try {
            URL url = new URL(titleUrl);
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setConnectTimeout(10000);
            conn.setReadTimeout(10000);
            conn.connect();
            is = conn.getInputStream();
            fos = new FileOutputStream(file);
            byte[] buffer = new byte[4096];
            int i;
            while ((i = is.read(buffer)) != -1) {
                fos.write(buffer, 0, i);
            }
            return true;

        } catch (IOException e1) {
            if (is != null)
                is.close();
            if (fos != null)
                fos.close();
            file.delete();
            e1.printStackTrace();
            return false;
        } finally {
            if (is != null)
                is.close();
            if (fos != null)
                fos.close();

        }
    }


}
