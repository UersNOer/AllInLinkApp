package com.example.android_supervisor.x5;


//public class FilechooserActivity extends AppCompatActivity {
//
//
//    /**
//     * 用于展示在web端<input type=text>的标签被选择之后，文件选择器的制作和生成
//     */
//
//    private X5WebView webView;
//    private ValueCallback<Uri> uploadFile;
//    private ValueCallback<Uri[]> uploadFiles;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        // TODO Auto-generated method stub
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.filechooser_layout);
//
//        webView = (X5WebView) findViewById(R.id.web_filechooser);
//
//        webView.setWebChromeClient(new WebChromeClient() {
//            // For Android 3.0+
//            public void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType) {
//                FilechooserActivity.this.uploadFile = uploadFile;
//                openFileChooseProcess();
//            }
//
//            // For Android < 3.0
//            public void openFileChooser(ValueCallback<Uri> uploadMsgs) {
//                FilechooserActivity.this.uploadFile = uploadFile;
//                openFileChooseProcess();
//            }
//
//            // For Android  > 4.1.1
//            public void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType, String capture) {
//                FilechooserActivity.this.uploadFile = uploadFile;
//                openFileChooseProcess();
//            }
//
//            // For Android  >= 5.0
//            public boolean onShowFileChooser(WebView webView,
//                                             ValueCallback<Uri[]> filePathCallback,
//                                             FileChooserParams fileChooserParams) {
//                Log.i("test", "openFileChooser 4:" + filePathCallback.toString());
//
//                Log.i("test", "【onShowFileChooser】 5.0+" + "   " + fileChooserParams.getMode() + "  " + fileChooserParams.getTitle() + "  "//Mode为0
//                        + fileChooserParams.isCaptureEnabled() + "  " + fileChooserParams.getFilenameHint() + "  " +
//                        Arrays.toString(fileChooserParams.getAcceptTypes()));
//
//                FilechooserActivity.this.uploadFiles = filePathCallback;
//                switch (Arrays.toString(fileChooserParams.getAcceptTypes())){
//                    case "[image/*]":
//                        CameraUtils.camera(FilechooserActivity.this, new CameraUtils.CameraCompleted() {
//                            @Override
//                            public void completed(MediaInfo mediaInfo) {
//                                uploadFiles.onReceiveValue(new Uri[]{Uri.fromFile(new File(mediaInfo.getOriginalPath()))});
//                                uploadFiles = null;
//                                //adapter.upload(FilechooserActivity.this, 0, mediaInfo);
//                            }
//                        });
//                        break;
//
//                    case "[album/*]":
//                        openFileChooseProcess();
//                        break;
//                    default:
//                        break;
//                }
//
//                return true;
//            }
//
//        });
//
//
//       webView.loadUrl("file:///android_asset/webpage/fileChooser.html");
//     //   webView.loadUrl("https://tykj.cszhx.top/h5");
//    }
//
//    private void openFileChooseProcess() {
////        Intent i = new Intent(Intent.ACTION_GET_CONTENT);
////        i.addCategory(Intent.CATEGORY_OPENABLE);
////        i.setType("*/*");
////        startActivityForResult(Intent.createChooser(i, "test"), 0);
//
//
//
//    }
//
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        // TODO Auto-generated method stub
//        super.onActivityResult(requestCode, resultCode, data);
//
//        if (resultCode == RESULT_OK) {
//            switch (requestCode) {
//                case 0:
//                    if (null != uploadFile) {
//                        Uri result = data == null || resultCode != RESULT_OK ? null
//                                : data.getData();
//                        uploadFile.onReceiveValue(result);
//                        uploadFile = null;
//                    }
//                    if (null != uploadFiles) {
//                        Uri result = data == null || resultCode != RESULT_OK ? null
//                                : data.getData();
//                        uploadFiles.onReceiveValue(new Uri[]{result});
//                        uploadFiles = null;
//                    }
//                    break;
//                default:
//                    break;
//            }
//        } else if (resultCode == RESULT_CANCELED) {
//            if (null != uploadFile) {
//                uploadFile.onReceiveValue(null);
//                uploadFile = null;
//            }
//
//        }
//    }
//
//    /**
//     * 确保注销配置能够被释放
//     */
//    @Override
//    protected void onDestroy() {
//        // TODO Auto-generated method stub
//        if (this.webView != null) {
//            webView.destroy();
//        }
//        super.onDestroy();
//    }
//}
