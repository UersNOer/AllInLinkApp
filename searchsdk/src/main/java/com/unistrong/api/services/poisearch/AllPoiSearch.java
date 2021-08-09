package com.unistrong.api.services.poisearch;

import android.annotation.SuppressLint;
import android.content.Context;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class AllPoiSearch {
    private String key;

    private String city;
    private Context context;
    private PoiSearch.OnPoiSearchListener OnPoiSearchListener;

    public void setOnPoiSearchListener(PoiSearch.OnPoiSearchListener listener) {
        this.OnPoiSearchListener = listener;
    }

    public AllPoiSearch(Context context, String key, String city) {
        this.context = context;
        this.key = key;
        this.city = city;

    }


    @SuppressLint({"SetJavaScriptEnabled", "JavascriptInterface"})
    public void searchPOIAsyn() {
        final WebView webpage = new WebView(context);
        webpage.loadUrl("https://restapi.amap.com/v3/place/text?keywords=" + key + "&city=" + city + "&citylimit=true&offset=20&page=1&extensions=base&key=c0d2f3afcc4cc18a05d976d242dc3a70");
        webpage.getSettings().setJavaScriptEnabled(true);// 支持javascript
        webpage.addJavascriptInterface(new JSHandle(), "handler");  // 相当于在网页的js中增加一个handler类，实现java与WebView的js交互
        webpage.setWebViewClient(new WebViewClient() {
            // 依然在webview打开新页面
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                // 通过内部类定义的方法获取html页面加载的内容，这个需要添加在webview加载完成后的回调中
                view.loadUrl("javascript:window.handler.show(document.body.innerHTML);");
                super.onPageFinished(view, url);
            }

            @Override
            public void onScaleChanged(WebView view, float oldScale,
                                       float newScale) {
                // TODO Auto-generated method stub
                super.onScaleChanged(view, oldScale, newScale);
                webpage.requestFocus();
                webpage.requestFocusFromTouch();
            }
        });
    }

    class JSHandle {
        @JavascriptInterface
        public void show(String data) throws JSONException {  // 这里的data就webview加载的内容，即使页面跳转页都可以获取到，这样就可以做自己的处理了
            ArrayList<XGPoiItem> xgPoiItems = new ArrayList<>();
            int startIndex = data.indexOf("{");
            data = data.substring(startIndex, data.length() - 1);
            int lastIndex = data.lastIndexOf("}") + 1;
            data = data.substring(0, lastIndex);
            JSONObject jsonObject = new JSONObject(data);

            JSONArray pois = jsonObject.getJSONArray("pois");
            for (int i = 0; i < pois.length(); i++) {
                JSONObject poi = pois.getJSONObject(i);
                String name = poi.getString("name");
                String location = poi.getString("location");
                String[] split = location.split(",");
                XGPoiItem xgPoiItem = new XGPoiItem();
                ArrayList<Double> coordinates = new ArrayList<>();
                double latDifference = (40.045534396702 - 40.0443);
                double lngDifference = (116.281755913629 - 116.2757);
                coordinates.add(Double.parseDouble(split[0]));
                coordinates.add(Double.parseDouble(split[1]));
                XGPoiItem.GeometryBean geometryBean = new XGPoiItem.GeometryBean(coordinates);
                XGPoiItem.PropertiesBean propertiesBean = new XGPoiItem.PropertiesBean();
                propertiesBean.setPoiname(poi.getString("name"));
                xgPoiItem.setProperties(propertiesBean);
                xgPoiItem.setGeometry(geometryBean);
                xgPoiItems.add(xgPoiItem);
            }
            XGPoiResult xgPoiResult = new XGPoiResult(xgPoiItems);

            OnPoiSearchListener.onPoiSearched(xgPoiResult, 0);

        }
    }
}
