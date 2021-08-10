package com.unistrong.api.services.poisearch;

import com.unistrong.api.services.core.UnistrongException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.GZIPInputStream;

public class XGPoiJsonParser {
    /**
     * poi搜索解析
     *
     * @return
     */


    public XGPoiResult loadData(String json) throws UnistrongException {
        XGPoiResult result = null;

        try {
            result = Poiparser(new JSONObject(json));

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return result;
    }

    protected String readString(InputStream inputStream) throws UnistrongException {
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        try {
            byte[] buffer = new byte[1024];
            int length = -1;
            while ((length = (inputStream.read(buffer))) != -1) {
                outStream.write(buffer, 0, length);
                outStream.flush();
            }
        } catch (IOException e) {
            throw new UnistrongException(UnistrongException.ERROR_IO);
        } finally {
            if (outStream != null) {
                try {
                    outStream.close();
                } catch (IOException e) {
                    throw new UnistrongException(UnistrongException.ERROR_IO);
                }
            }
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    throw new UnistrongException(UnistrongException.ERROR_IO);
                }
            }
        }
        try {
            return new String(outStream.toByteArray(), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return new String();
    }

    protected String zipInputStream(InputStream is) throws UnistrongException {
        StringBuffer buffer = null;
        GZIPInputStream gzip = null;
        try {
            gzip = new GZIPInputStream(is);
            BufferedReader in = new BufferedReader(new InputStreamReader(gzip, "UTF-8"));
            buffer = new StringBuffer();
            String line;
            while ((line = in.readLine()) != null)
                buffer.append(line + "\n");
            is.close();
        } catch (IOException e) {
            throw new UnistrongException(UnistrongException.ERROR_IO);
        } finally {
            if (gzip != null) {
                try {
                    gzip.close();
                } catch (IOException e) {
                    throw new UnistrongException(UnistrongException.ERROR_IO);
                }
            }
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    throw new UnistrongException(UnistrongException.ERROR_IO);
                }
            }
        }
        if (buffer != null) {
            return buffer.toString();
        }
        return new String();
    }

    public XGPoiResult Poiparser(JSONObject obj) {
        XGPoiResult result = null;
        boolean tag = obj.has("features");
        if (tag == false) {
            return null;
        }

        try {
            JSONArray resultArray = obj.getJSONArray("features");
            if (resultArray == null)
                return null;
            ArrayList<XGPoiItem> list = new ArrayList<XGPoiItem>();
            int length = resultArray.length();
            for (int i = 0; i < length; i++) {
                JSONObject itemObj = resultArray.getJSONObject(i);
                XGPoiItem poiItem = new XGPoiItem();
                parsePoiItem(itemObj, poiItem);
                list.add(poiItem);
            }
            result = new XGPoiResult(list);
            if (obj.has("message")) {
                result.setMessage(obj.getString("message"));
            }
            if (obj.has("total")) {
                if (!"".equals(obj.getString("total")))
                    result.setTotal(Integer.valueOf(obj.getString("total")));
            }
        } catch (JSONException e) {
            throw new IllegalArgumentException("json解析失败");
        }
        return result;
    }

    private void parseDetailInfo(JSONObject itemObj, PoiItem poiItem) throws JSONException {
        if (itemObj.has("detail_info")) {
            JSONObject detailinfo = itemObj.getJSONObject("detail_info");
            if (!"".equals(detailinfo.getString("type"))) {
                poiItem.setTypeDes(detailinfo.getString("type"));
            }
            if (!"".equals(detailinfo.getString("distance"))) {
                poiItem.setDistance(Double.parseDouble(detailinfo.getString("distance")));
            }
        }

    }

    /**
     * poiid解析
     *
     * @param obj
     * @return
     */
    List<PoiItem> PoiIdparser(JSONObject obj) {
        List<PoiItem> poiItems = null;
        boolean tag = obj.has("results");
        if (tag == false) {
            return null;
        }

        try {
            JSONArray resultArray = obj.getJSONArray("results");
            if (resultArray == null) {
                return null;
            } else {
                poiItems = new ArrayList<PoiItem>();
                int length = resultArray.length();
                for (int i = 0; i < length; i++) {
                    JSONObject itemObj = resultArray.getJSONObject(i);
                    PoiItem poiItem = new PoiItem();
//                    parsePoiItem(itemObj, poiItem);
                    poiItems.add(poiItem);
                }

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return poiItems;
    }

    /**
     * 解析poiItem
     *
     * @param obj
     * @param poiItem
     * @throws JSONException
     */
    private void parsePoiItem(JSONObject obj, XGPoiItem poiItem) throws JSONException {
        poiItem.setId(obj.getString("id"));
        poiItem.setGeometry_name(obj.getString("geometry_name"));
        JSONObject geometry = obj.getJSONObject("geometry");
        JSONArray coordinates = geometry.getJSONArray("coordinates");
        ArrayList<Double> laction = new ArrayList<>();
        for (int i = 0; i < 2; i++) {
            laction.add((Double) coordinates.get(i));
        }
        poiItem.setGeometry(new XGPoiItem.GeometryBean(laction));


        JSONObject properties = obj.getJSONObject("properties");
        XGPoiItem.PropertiesBean proertiesBean=new XGPoiItem.PropertiesBean();

        proertiesBean.setPoiname(properties.getString("poiname"));
        proertiesBean.setAddress(properties.getString("address"));


        poiItem.setProperties(proertiesBean);

    }


}
