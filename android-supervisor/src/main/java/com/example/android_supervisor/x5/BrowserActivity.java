package com.example.android_supervisor.x5;


import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ProgressBar;

import com.tencent.bugly.beta.Beta;
import com.tencent.smtt.sdk.ValueCallback;
import com.tencent.smtt.sdk.WebChromeClient;
import com.tencent.smtt.sdk.WebView;
import com.tencent.smtt.sdk.WebViewClient;
import com.example.android_supervisor.R;
import com.example.android_supervisor.common.ResultCallback;
import com.example.android_supervisor.ui.media.CameraUtils;
import com.example.android_supervisor.ui.media.MediaDummyFragment;
import com.example.android_supervisor.ui.media.MediaInfo;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public abstract class BrowserActivity extends AppCompatActivity {


    private ProgressBar mPageLoadingProgressBar;
    private X5WebView webView;
    private ValueCallback<Uri> uploadFile;
    private ValueCallback<Uri[]> uploadFiles;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_browser);
        mPageLoadingProgressBar = (ProgressBar) findViewById(R.id.progressBar);// new
        webView = (X5WebView) findViewById(R.id.webview);

        webView.setWebChromeClient(new WebChromeClient() {
            // For Android 3.0+
            public void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType) {
                BrowserActivity.this.uploadFile = uploadFile;
                openFileChooseProcess(acceptType);
            }

            // For Android < 3.0
            public void openFileChooser(ValueCallback<Uri> uploadMsgs) {
                BrowserActivity.this.uploadFile = uploadFile;
                openFileChooseProcess("");
            }

            // For Android  > 4.1.1
            public void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType, String capture) {
                BrowserActivity.this.uploadFile = uploadFile;
                openFileChooseProcess(acceptType);
            }

            // For Android  >= 5.0
            public boolean onShowFileChooser(WebView webView,
                                             ValueCallback<Uri[]> filePathCallback,
                                             FileChooserParams fileChooserParams) {
                Log.i("test", "openFileChooser 4:" + filePathCallback.toString());

                Log.i("test", "【onShowFileChooser】 5.0+" + "   " + fileChooserParams.getMode() + "  " + fileChooserParams.getTitle() + "  "//Mode为0
                        + fileChooserParams.isCaptureEnabled() + "  " + fileChooserParams.getFilenameHint() + "  " +
                        Arrays.toString(fileChooserParams.getAcceptTypes()));

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        BrowserActivity.this.uploadFiles = filePathCallback;
                        openFileChooseProcess(Arrays.toString(fileChooserParams.getAcceptTypes()));
                    }
                }).start();


                return true;
            }

        });


        webView.setWebViewClient(new WebViewClient(){
            @Override
            public void onPageFinished(WebView webView, String s) {
                super.onPageFinished(webView, s);
                mPageLoadingProgressBar.setVisibility(View.GONE);
            }

            @Override
            public void onPageStarted(WebView webView, String s, Bitmap bitmap) {
                super.onPageStarted(webView, s, bitmap);
                mPageLoadingProgressBar.setVisibility(View.VISIBLE);
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url)
            { // 重写此方法表明点击网页里面的链接还是在当前的webview里跳转，不跳到浏览器那边
                Log.i("test", "URL" + url);
                view.loadUrl(url);
                return true;
            }
        });

        webView.addJavascriptInterface(new JSInterface(BrowserActivity.this,webView), "android");

        webView.loadUrl(loadUrl());


        Beta.checkUpgrade();
    }


    public abstract String loadUrl();

    public void openFileChooseProcess(String acceptType){

        switch (acceptType){
            case "[image/*]":
                CameraUtils.camera(BrowserActivity.this, new CameraUtils.CameraCompleted() {
                    @Override
                    public void completed(MediaInfo mediaInfo) {
                        if (mediaInfo == null){
                            Log.i("test", "openFileChooser 4:已取消" );
                            uploadFiles.onReceiveValue(null);
                            uploadFiles = null;
                            return;
                        }
                        uploadFiles.onReceiveValue(new Uri[]{Uri.fromFile(new File(mediaInfo.getOriginalPath()))});
                        uploadFiles = null;
                        Log.i("test", "openFileChooser 4:已回填" );
                        //adapter.upload(FilechooserActivity.this, 0, mediaInfo);
                    }
                });
                break;

            case "[album/*]":
                new MediaDummyFragment(0,false, 5, new ResultCallback<List<MediaInfo>>() {
                    @Override
                    public void onResult(List<MediaInfo> result, int tag) {

                    }

                    @Override
                    public void onResult(List<MediaInfo> mediaInfos) {

                        ArrayList<Uri> list = new ArrayList<>();

                        for (int i = 0; i <mediaInfos.size() ; i++) {
                            list.add(Uri.fromFile(new File(mediaInfos.get(i).getOriginalPath())));
                        }

                        Uri[] items = new Uri[list.size()];
                        for (int i = 0;i<list.size();i++) {
                            items[i] = list.get(i);
                        }

                        uploadFiles.onReceiveValue(items);
                        uploadFiles = null;
                    }
                }).show(BrowserActivity.this, "media_picker");
                break;
            default:
                break;
        }

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (webView != null && webView.canGoBack()) {
                webView.goBack();
                return true;
            } else
                return super.onKeyDown(keyCode, event);
        }
        return super.onKeyDown(keyCode, event);
    }


    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {

        }
        else if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {

        }
    }


}
