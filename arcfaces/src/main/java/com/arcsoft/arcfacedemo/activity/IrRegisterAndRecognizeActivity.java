package com.arcsoft.arcfacedemo.activity;

import android.Manifest;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.Rect;
import android.hardware.Camera;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.arcsoft.arcfacedemo.common.Constants;
import com.arcsoft.arcfacedemo.model.FacePreviewInfo;
import com.arcsoft.arcfacedemo.util.face.FaceHelperIr;
import com.arcsoft.arcfacedemo.widget.ShowFaceInfoAdapter;
import com.arcsoft.arcfacedemo.faceserver.CompareResult;
import com.arcsoft.arcfacedemo.faceserver.FaceServer;
import com.arcsoft.arcfacedemo.util.ConfigUtil;
import com.arcsoft.arcfacedemo.util.face.FaceListener;
import com.arcsoft.arcfacedemo.util.face.RequestFeatureStatus;
import com.arcsoft.face.AgeInfo;
import com.arcsoft.face.ErrorInfo;
import com.arcsoft.face.FaceEngine;
import com.arcsoft.face.FaceFeature;
import com.arcsoft.face.GenderInfo;
import com.arcsoft.face.LivenessInfo;
import com.arcsoft.face.LivenessParam;
import com.arcsoft.face.VersionInfo;
import com.arcsoft.arcfacedemo.R;
import com.arcsoft.arcfacedemo.widget.FaceRectView;
import com.arcsoft.arcfacedemo.model.DrawInfo;
import com.arcsoft.arcfacedemo.util.camera.DualCameraHelper;
import com.arcsoft.arcfacedemo.util.camera.CameraListener;
import com.arcsoft.arcfacedemo.util.DrawHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * 1.??????????????????IR?????????????????????????????????RGB???????????????
 * <p>
 * 2.??????????????????IR?????????RGB?????????????????????????????????????????????????????????RGB?????????IR??????????????????????????????????????????????????????
 * <p>
 * 3.???IR?????????RGB???????????????????????????????????????????????????????????????????????????????????????{@link FaceHelperIr#adjustFaceInfoForIR}?????????????????????????????????
 * <p>
 * 4.????????????????????????IR Camera???RGB Camera???CameraId???????????????????????????????????????????????????????????????????????????
 * {@link IrRegisterAndRecognizeActivity#cameraRgbId}???
 * {@link IrRegisterAndRecognizeActivity#cameraIrId}??????
 * <p>
 * 5.?????????????????????android??????????????????????????????cameraId???{@link android.hardware.Camera.CameraInfo#CAMERA_FACING_FRONT}???????????????????????????????????????????????????
 * ???????????????????????????????????????????????????????????????????????????????????????????????????????????????demo???cameraId???{@link android.hardware.Camera.CameraInfo#CAMERA_FACING_FRONT}
 * ???????????????????????????????????????????????????????????????????????????
 */
public class IrRegisterAndRecognizeActivity extends AppCompatActivity implements ViewTreeObserver.OnGlobalLayoutListener {
    private static final String TAG = "IrRegisterAndRecognize";
    private static final int MAX_DETECT_NUM = 10;
    /**
     * ???FR??????????????????????????????FR?????????????????????
     */
    private static final int WAIT_LIVENESS_INTERVAL = 50;
    private DualCameraHelper cameraHelper;
    private DualCameraHelper cameraHelperIr;
    private DrawHelper drawHelperRgb;
    private DrawHelper drawHelperIr;
    private Camera.Size previewSize;
    private Camera.Size previewSizeIr;

    /**
     * RGB????????????IR????????????ID??????????????????????????????????????????????????????
     * ????????????????????????????????????VIDEO????????????????????????
     */
    private Integer cameraRgbId = Camera.CameraInfo.CAMERA_FACING_BACK;
    private Integer cameraIrId = Camera.CameraInfo.CAMERA_FACING_FRONT;

    private FaceEngine faceEngine;
    private FaceHelperIr faceHelperIr;
    private List<CompareResult> compareResultList;
    private ShowFaceInfoAdapter adapter;
    /**
     * ?????????????????????
     */
    private boolean livenessDetect = true;

    /**
     * ????????????????????????????????????
     */
    private static final int REGISTER_STATUS_READY = 0;
    /**
     * ?????????????????????????????????
     */
    private static final int REGISTER_STATUS_PROCESSING = 1;
    /**
     * ????????????????????????????????????????????????????????????
     */
    private static final int REGISTER_STATUS_DONE = 2;

    private int registerStatus = REGISTER_STATUS_DONE;

    private int afCode = -1;
    private ConcurrentHashMap<Integer, Integer> requestFeatureStatusMap = new ConcurrentHashMap<>();
    private ConcurrentHashMap<Integer, Integer> livenessMap = new ConcurrentHashMap<>();
    private CompositeDisposable getFeatureDelayedDisposables = new CompositeDisposable();
    /**
     * ????????????????????????????????????SurfaceView???TextureView
     */
    private View previewViewRgb;
    private View previewViewIr;
    /**
     * ????????????????????????
     */
    private FaceRectView faceRectView;
    private FaceRectView faceRectViewIr;

    private Switch switchLivenessDetect;

    private static final int ACTION_REQUEST_PERMISSIONS = 0x001;
    private static final float SIMILAR_THRESHOLD = 0.8F;
    /**
     * ???????????????????????????
     */
    private static final String[] NEEDED_PERMISSIONS = new String[]{
            Manifest.permission.CAMERA,
            Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    private volatile byte[] rgbData;
    private volatile byte[] irData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_and_recognize_ir);

        //????????????
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//            WindowManager.LayoutParams attributes = getWindow().getAttributes();
//            attributes.systemUiVisibility = View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
//            getWindow().setAttributes(attributes);
//        }

        // Activity???????????????????????????????????????
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LOCKED);
        //????????????????????????
        FaceServer.getInstance().init(this);

        initView();
    }

    private void initView() {
        previewViewRgb = findViewById(R.id.texture_preview);
        //???????????????????????????????????????
        previewViewRgb.getViewTreeObserver().addOnGlobalLayoutListener(this);
        previewViewIr = findViewById(R.id.texture_previewIr);
        faceRectView = findViewById(R.id.face_rect_view);
        faceRectViewIr = findViewById(R.id.face_rect_viewIr);
        switchLivenessDetect = findViewById(R.id.switch_liveness_detect);

        switchLivenessDetect.setChecked(livenessDetect);
        switchLivenessDetect.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                livenessDetect = isChecked;
            }
        });
        RecyclerView recyclerShowFaceInfo = findViewById(R.id.recycler_view_person);
        compareResultList = new ArrayList<>();
        adapter = new ShowFaceInfoAdapter(compareResultList, this);
        recyclerShowFaceInfo.setAdapter(adapter);
        DisplayMetrics dm = getResources().getDisplayMetrics();
        int spanCount = (int) (dm.widthPixels / (getResources().getDisplayMetrics().density * 100 + 0.5f));
        recyclerShowFaceInfo.setLayoutManager(new GridLayoutManager(this, spanCount));
        recyclerShowFaceInfo.setItemAnimator(new DefaultItemAnimator());
    }

    /**
     * ???????????????
     */
    private void initEngine() {
        faceEngine = new FaceEngine();
        afCode = faceEngine.init(this, FaceEngine.ASF_DETECT_MODE_VIDEO, ConfigUtil.getFtOrient(this),
                32, MAX_DETECT_NUM, FaceEngine.ASF_FACE_RECOGNITION |
                        FaceEngine.ASF_FACE_DETECT | FaceEngine.ASF_IR_LIVENESS);
        Log.i(TAG, "initEngine:  init: " + afCode);
        if (afCode == ErrorInfo.MOK) {
            afCode = faceEngine.setLivenessParam(new LivenessParam(0.75f, 0.7f));
        }
        VersionInfo versionInfo = new VersionInfo();
        faceEngine.getVersion(versionInfo);
        Log.i(TAG, "initEngine:  init: " + afCode + "  version:" + versionInfo);

        if (afCode != ErrorInfo.MOK) {
            Toast.makeText(this, getString(R.string.init_failed, afCode), Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * ????????????
     */
    private void unInitEngine() {

        if (afCode == ErrorInfo.MOK) {
            afCode = faceEngine.unInit();
            Log.i(TAG, "unInitEngine: " + afCode);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        try {
            if (cameraHelper != null) {
                cameraHelper.start();
            }
            if (cameraHelperIr != null) {
                cameraHelperIr.start();
            }
        } catch (RuntimeException e) {
            Toast.makeText(this, e.getMessage() + getString(R.string.camera_error_notice), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onPause() {
        if (cameraHelper != null) {
            cameraHelper.stop();
        }
        if (cameraHelperIr != null) {
            cameraHelperIr.stop();
        }
        super.onPause();
    }

    @Override
    protected void onDestroy() {

        if (cameraHelper != null) {
            cameraHelper.release();
            cameraHelper = null;
        }
        if (cameraHelperIr != null) {
            cameraHelperIr.release();
            cameraHelperIr = null;
        }

        //faceHelper???????????????FR???????????????????????????????????????crash
        if (faceHelperIr != null) {
            synchronized (faceHelperIr) {
                unInitEngine();
            }
            ConfigUtil.setTrackId(this, faceHelperIr.getCurrentTrackId());
            faceHelperIr.release();
        } else {
            unInitEngine();
        }
        if (getFeatureDelayedDisposables != null) {
            getFeatureDelayedDisposables.dispose();
            getFeatureDelayedDisposables.clear();
        }
        FaceServer.getInstance().unInit();
        super.onDestroy();
    }

    private boolean checkPermissions(String[] neededPermissions) {
        if (neededPermissions == null || neededPermissions.length == 0) {
            return true;
        }
        boolean allGranted = true;
        for (String neededPermission : neededPermissions) {
            allGranted &= ContextCompat.checkSelfPermission(this, neededPermission) == PackageManager.PERMISSION_GRANTED;
        }
        return allGranted;
    }

    private void initRgbCamera() {
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        final FaceListener faceListener = new FaceListener() {
            @Override
            public void onFail(Exception e) {
                Log.e(TAG, "onFail: " + e.getMessage());
            }

            //??????FR?????????
            @Override
            public void onFaceFeatureInfoGet(@Nullable final FaceFeature faceFeature, final Integer requestId) {
                //FR??????
                if (faceFeature != null) {
//                    Log.i(TAG, "onPreview: fr end = " + System.currentTimeMillis() + " trackId = " + requestId);

                    //??????????????????????????????????????????
                    if (!livenessDetect) {
                        searchFace(faceFeature, requestId);
                    }
                    //?????????????????????????????????
                    else if (livenessMap.get(requestId) != null && livenessMap.get(requestId) == LivenessInfo.ALIVE) {
                        searchFace(faceFeature, requestId);
                    }
                    //?????????????????????????????????100ms??????????????????
                    else if (livenessMap.get(requestId) != null && livenessMap.get(requestId) == LivenessInfo.UNKNOWN) {
                        getFeatureDelayedDisposables.add(Observable.timer(WAIT_LIVENESS_INTERVAL, TimeUnit.MILLISECONDS)
                                .subscribe(new Consumer<Long>() {
                                    @Override
                                    public void accept(Long aLong) {
                                        onFaceFeatureInfoGet(faceFeature, requestId);
                                    }
                                }));
                    }
                    //??????????????????
                    else {
                        requestFeatureStatusMap.put(requestId, RequestFeatureStatus.NOT_ALIVE);
                    }

                }
                //FR ??????
                else {
                    requestFeatureStatusMap.put(requestId, RequestFeatureStatus.FAILED);
                }
            }

        };
        CameraListener rgbCameraListener = new CameraListener() {
            @Override
            public void onCameraOpened(Camera camera, int cameraId, int displayOrientation, boolean isMirror) {
                previewSize = camera.getParameters().getPreviewSize();
                ViewGroup.LayoutParams layoutParams = adjustPreviewViewSize(previewViewRgb, faceRectView, previewSize, displayOrientation);
                drawHelperRgb = new DrawHelper(previewSize.width, previewSize.height, layoutParams.width, layoutParams.height, displayOrientation,
                        cameraId, isMirror, false, false);
                faceHelperIr = new FaceHelperIr.Builder()
                        .faceEngine(faceEngine)
                        .frThreadNum(MAX_DETECT_NUM)
                        .previewSize(previewSize)
                        .faceListener(faceListener)
                        .currentTrackId(ConfigUtil.getTrackId(IrRegisterAndRecognizeActivity.this.getApplicationContext()))
                        .build();

                TextView textViewRgb = new TextView(IrRegisterAndRecognizeActivity.this, null);
                textViewRgb.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                textViewRgb.setText(getString(R.string.camera_rgb) + "\n" + previewSize.width + "x" + previewSize.height);
                textViewRgb.setTextColor(Color.WHITE);
                textViewRgb.setBackgroundColor(getResources().getColor(R.color.color_bg_notification));
                ((FrameLayout) previewViewRgb.getParent()).addView(textViewRgb);
            }


            @Override
            public void onPreview(final byte[] nv21, Camera camera) {
                rgbData = nv21;
                processPreviewData();
            }

            @Override
            public void onCameraClosed() {
                Log.i(TAG, "onCameraClosed: ");
            }

            @Override
            public void onCameraError(Exception e) {
                Log.i(TAG, "onCameraError: " + e.getMessage());
            }

            @Override
            public void onCameraConfigurationChanged(int cameraID, int displayOrientation) {
                if (drawHelperRgb != null) {
                    drawHelperRgb.setCameraDisplayOrientation(displayOrientation);
                }
                Log.i(TAG, "onCameraConfigurationChanged: " + cameraID + "  " + displayOrientation);
            }
        };
        cameraHelper = new DualCameraHelper.Builder()
                .previewViewSize(new Point(previewViewRgb.getMeasuredWidth(), previewViewRgb.getMeasuredHeight()))
                .rotation(getWindowManager().getDefaultDisplay().getRotation())
                .specificCameraId(cameraRgbId != null ? cameraRgbId : Camera.CameraInfo.CAMERA_FACING_BACK)
                .previewOn(previewViewRgb)
                .cameraListener(rgbCameraListener)
                .isMirror(cameraRgbId != null && Camera.CameraInfo.CAMERA_FACING_FRONT == cameraRgbId)
                .build();
        cameraHelper.init();
        try {
            cameraHelper.start();
        } catch (RuntimeException e) {
            Toast.makeText(this, e.getMessage() + getString(R.string.camera_error_notice), Toast.LENGTH_SHORT).show();
        }
    }

    private void initIrCamera() {
        CameraListener irCameraListener = new CameraListener() {
            @Override
            public void onCameraOpened(Camera camera, int cameraId, int displayOrientation, boolean isMirror) {
                previewSizeIr = camera.getParameters().getPreviewSize();
                ViewGroup.LayoutParams layoutParams = adjustPreviewViewSize(previewViewIr, faceRectViewIr, previewSizeIr, displayOrientation);
                drawHelperIr = new DrawHelper(previewSizeIr.width, previewSizeIr.height, layoutParams.width, layoutParams.height, displayOrientation,
                        cameraId, isMirror, false, false);
                TextView textViewIr = new TextView(IrRegisterAndRecognizeActivity.this, null);
                textViewIr.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                textViewIr.setText(getString(R.string.camera_ir) + "\n" + previewSizeIr.width + "x" + previewSizeIr.height);
                textViewIr.setTextColor(Color.WHITE);
                textViewIr.setBackgroundColor(getResources().getColor(R.color.color_bg_notification));
                ((FrameLayout) previewViewIr.getParent()).addView(textViewIr);
            }


            @Override
            public void onPreview(final byte[] nv21, Camera camera) {
                irData = nv21;
            }

            @Override
            public void onCameraClosed() {
                Log.i(TAG, "onCameraClosed: ");
            }

            @Override
            public void onCameraError(Exception e) {
                Log.i(TAG, "onCameraError: " + e.getMessage());
            }

            @Override
            public void onCameraConfigurationChanged(int cameraID, int displayOrientation) {
                if (drawHelperIr != null) {
                    drawHelperIr.setCameraDisplayOrientation(displayOrientation);
                }
                Log.i(TAG, "onCameraConfigurationChanged: " + cameraID + "  " + displayOrientation);
            }
        };

        cameraHelperIr = new DualCameraHelper.Builder()
                .previewViewSize(new Point(previewViewIr.getMeasuredWidth(), previewViewIr.getMeasuredHeight()))
                .rotation(getWindowManager().getDefaultDisplay().getRotation())
                .specificCameraId(cameraIrId != null ? cameraIrId : Camera.CameraInfo.CAMERA_FACING_FRONT)
                .previewOn(previewViewIr)
                .cameraListener(irCameraListener)
                .isMirror(cameraIrId != null && Camera.CameraInfo.CAMERA_FACING_FRONT == cameraIrId)
//                .previewSize(new Point(1280, 960)) //???????????????????????????RGB???IR?????????????????????
//                .additionalRotation(270) //??????????????????
                .build();
        cameraHelperIr.init();
        try {
            cameraHelperIr.start();
        } catch (RuntimeException e) {
            Toast.makeText(this, e.getMessage() + getString(R.string.camera_error_notice), Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * ??????View???????????????2?????????????????????
     *
     * @param previewView        ?????????????????????view
     * @param faceRectView       ?????????view
     * @param previewSize        ????????????
     * @param displayOrientation ??????????????????
     * @return ????????????LayoutParams
     */
    private ViewGroup.LayoutParams adjustPreviewViewSize(View previewView, FaceRectView faceRectView, Camera.Size previewSize, int displayOrientation) {
        ViewGroup.LayoutParams layoutParams = previewView.getLayoutParams();
        int measuredWidth = previewView.getMeasuredWidth();
        int measuredHeight = previewView.getMeasuredHeight();
        float ratio = ((float) previewSize.height) / (float) previewSize.width;
        if (ratio > 1) {
            ratio = 1 / ratio;
        }
        if (displayOrientation % 180 == 0) {
            layoutParams.width = measuredWidth;
            layoutParams.height = (int) (measuredWidth * ratio);
        } else {
            layoutParams.height = measuredHeight;
            layoutParams.width = (int) (measuredHeight * ratio);
        }
        Log.i(TAG, "adjustPreviewViewSize: " + layoutParams.width + "x" + layoutParams.height);
        previewView.setLayoutParams(layoutParams);
        faceRectView.setLayoutParams(layoutParams);
        return layoutParams;
    }

    /**
     * ??????????????????
     */
    private synchronized void processPreviewData() {
        if (rgbData != null && irData != null) {
            final byte[] cloneNv21Rgb = rgbData.clone();
            byte[] cloneNv21Ir = irData.clone();
            if (faceRectView != null) {
                faceRectView.clearFaceInfo();
            }
            if (faceRectViewIr != null) {
                faceRectViewIr.clearFaceInfo();
            }
            List<FacePreviewInfo> facePreviewInfoList = faceHelperIr.onPreviewFrame(cloneNv21Rgb, cloneNv21Ir);
            if (facePreviewInfoList != null && faceRectView != null && drawHelperRgb != null
                    && faceRectViewIr != null && drawHelperIr != null) {
                drawPreviewInfo(facePreviewInfoList);
            }
            registerFace(cloneNv21Rgb, facePreviewInfoList);
            clearLeftFace(facePreviewInfoList);

            if (facePreviewInfoList != null && facePreviewInfoList.size() > 0 && previewSize != null) {
                for (int i = 0; i < facePreviewInfoList.size(); i++) {
                    if (livenessDetect) {
                        livenessMap.put(facePreviewInfoList.get(i).getTrackId(),
                                facePreviewInfoList.get(i).getLivenessInfo().getLiveness());
                    }
                    /**
                     * ???????????????????????????????????????????????????????????????FR?????????????????????????????????????????????FR????????????
                     * FR??????????????????????????????{@link FaceListener#onFaceFeatureInfoGet(FaceFeature, Integer)}?????????
                     */
                    if (requestFeatureStatusMap.get(facePreviewInfoList.get(i).getTrackId()) == null
                            || requestFeatureStatusMap.get(facePreviewInfoList.get(i).getTrackId()) == RequestFeatureStatus.FAILED) {
                        requestFeatureStatusMap.put(facePreviewInfoList.get(i).getTrackId(), RequestFeatureStatus.SEARCHING);
                        faceHelperIr.requestFaceFeature(cloneNv21Rgb, facePreviewInfoList.get(i).getFaceInfo(),
                                previewSize.width, previewSize.height, FaceEngine.CP_PAF_NV21,
                                facePreviewInfoList.get(i).getTrackId());
                    }
                }
            }
            rgbData = null;
            irData = null;
        }

    }

    /**
     * ????????????????????????
     *
     * @param facePreviewInfoList {@link FaceHelperIr#onPreviewFrame(byte[], byte[])}?????????????????????
     */
    private void drawPreviewInfo(List<FacePreviewInfo> facePreviewInfoList) {
        List<DrawInfo> drawInfoList = new ArrayList<>();
        List<DrawInfo> drawInfoListIr = new ArrayList<>();
        for (int i = 0; i < facePreviewInfoList.size(); i++) {
            String name = faceHelperIr.getName(facePreviewInfoList.get(i).getTrackId());

            Rect ftRect = facePreviewInfoList.get(i).getFaceInfo().getRect();
            drawInfoList.add(new DrawInfo(drawHelperRgb.adjustRect(ftRect),
                    GenderInfo.UNKNOWN, AgeInfo.UNKNOWN_AGE,
                    livenessMap.containsKey(facePreviewInfoList.get(i).getTrackId()) ?
                            livenessMap.get(facePreviewInfoList.get(i).getTrackId()) : LivenessInfo.UNKNOWN,
                    name == null ? String.valueOf(facePreviewInfoList.get(i).getTrackId()) : name));

            Rect offsetFtRect = new Rect(ftRect);
            offsetFtRect.offset(Constants.HORIZONTAL_OFFSET, Constants.VERTICAL_OFFSET);
            drawInfoListIr.add(new DrawInfo(drawHelperIr.adjustRect(offsetFtRect),
                    GenderInfo.UNKNOWN, AgeInfo.UNKNOWN_AGE,
                    livenessMap.containsKey(facePreviewInfoList.get(i).getTrackId()) ?
                            livenessMap.get(facePreviewInfoList.get(i).getTrackId()) : LivenessInfo.UNKNOWN,
                    name == null ? String.valueOf(facePreviewInfoList.get(i).getTrackId()) : name));
        }
        drawHelperRgb.draw(faceRectView, drawInfoList);
        drawHelperIr.draw(faceRectViewIr, drawInfoListIr);
    }

    /**
     * ????????????
     *
     * @param nv21Rgb             RGB?????????????????????
     * @param facePreviewInfoList {@link FaceHelperIr#onPreviewFrame(byte[], byte[])}?????????????????????
     */
    private void registerFace(final byte[] nv21Rgb, final List<FacePreviewInfo> facePreviewInfoList) {
        if (registerStatus == REGISTER_STATUS_READY && facePreviewInfoList != null && facePreviewInfoList.size() > 0) {
            registerStatus = REGISTER_STATUS_PROCESSING;
            Observable.create(new ObservableOnSubscribe<Boolean>() {
                @Override
                public void subscribe(ObservableEmitter<Boolean> emitter) {
                    boolean success = FaceServer.getInstance().registerNv21(
                            IrRegisterAndRecognizeActivity.this, nv21Rgb,
                            previewSize.width, previewSize.height, facePreviewInfoList.get(0).getFaceInfo(), "registered " + faceHelperIr.getCurrentTrackId(),null,null);
                    emitter.onNext(success);
                }
            })
                    .subscribeOn(Schedulers.computation())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<Boolean>() {
                        @Override
                        public void onSubscribe(Disposable d) {

                        }

                        @Override
                        public void onNext(Boolean success) {
                            String result = success ? "register success!" : "register failed!";
                            Toast.makeText(IrRegisterAndRecognizeActivity.this, result, Toast.LENGTH_SHORT).show();
                            registerStatus = REGISTER_STATUS_DONE;
                        }

                        @Override
                        public void onError(Throwable e) {
                            Toast.makeText(IrRegisterAndRecognizeActivity.this, "register failed!", Toast.LENGTH_SHORT).show();
                            registerStatus = REGISTER_STATUS_DONE;
                        }

                        @Override
                        public void onComplete() {

                        }
                    });
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == ACTION_REQUEST_PERMISSIONS) {
            boolean isAllGranted = true;
            for (int grantResult : grantResults) {
                isAllGranted &= (grantResult == PackageManager.PERMISSION_GRANTED);
            }
            if (isAllGranted) {
                initEngine();
                initRgbCamera();
                initIrCamera();
            } else {
                Toast.makeText(this, R.string.permission_denied, Toast.LENGTH_SHORT).show();
            }
        }
    }

    /**
     * ???????????????????????????
     *
     * @param facePreviewInfoList ?????????trackId??????
     */
    private void clearLeftFace(List<FacePreviewInfo> facePreviewInfoList) {
        Set<Integer> keySet = requestFeatureStatusMap.keySet();
        if (compareResultList != null) {
            for (int i = compareResultList.size() - 1; i >= 0; i--) {
                if (!keySet.contains(compareResultList.get(i).getTrackId())) {
                    compareResultList.remove(i);
                    adapter.notifyItemRemoved(i);
                }
            }
        }
        if (facePreviewInfoList == null || facePreviewInfoList.size() == 0) {
            requestFeatureStatusMap.clear();
            livenessMap.clear();
            return;
        }

        for (Integer integer : keySet) {
            boolean contained = false;
            for (FacePreviewInfo facePreviewInfo : facePreviewInfoList) {
                if (facePreviewInfo.getTrackId() == integer) {
                    contained = true;
                    break;
                }
            }
            if (!contained) {
                requestFeatureStatusMap.remove(integer);
                livenessMap.remove(integer);
            }
        }

    }

    private void searchFace(final FaceFeature frFace, final Integer requestId) {
        Observable
                .create(new ObservableOnSubscribe<CompareResult>() {
                    @Override
                    public void subscribe(ObservableEmitter<CompareResult> emitter) {
//                        Log.i(TAG, "subscribe: fr search start = " + System.currentTimeMillis() + " trackId = " + requestId);
                        CompareResult compareResult = FaceServer.getInstance().getTopOfFaceLib(frFace);
//                        Log.i(TAG, "subscribe: fr search end = " + System.currentTimeMillis() + " trackId = " + requestId);
                        if (compareResult == null) {
                            emitter.onError(null);
                        } else {
                            emitter.onNext(compareResult);
                        }
                    }
                })
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<CompareResult>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(CompareResult compareResult) {
                        if (compareResult == null || compareResult.getUserName() == null) {
                            requestFeatureStatusMap.put(requestId, RequestFeatureStatus.FAILED);
                            faceHelperIr.addName(requestId, "VISITOR " + requestId);
                            return;
                        }

//                        Log.i(TAG, "onNext: fr search get result  = " + System.currentTimeMillis() + " trackId = " + requestId + "  similar = " + compareResult.getSimilar());
                        if (compareResult.getSimilar() > SIMILAR_THRESHOLD) {
                            boolean isAdded = false;
                            if (compareResultList == null) {
                                requestFeatureStatusMap.put(requestId, RequestFeatureStatus.FAILED);
                                faceHelperIr.addName(requestId, "VISITOR " + requestId);
                                return;
                            }
                            for (CompareResult compareResult1 : compareResultList) {
                                if (compareResult1.getTrackId() == requestId) {
                                    isAdded = true;
                                    break;
                                }
                            }
                            if (!isAdded) {
                                //??????????????????????????????????????????????????? MAX_DETECT_NUM ??????????????????????????????????????????????????????
                                if (compareResultList.size() >= MAX_DETECT_NUM) {
                                    compareResultList.remove(0);
                                    adapter.notifyItemRemoved(0);
                                }
                                //?????????????????????????????????trackId
                                compareResult.setTrackId(requestId);
                                compareResultList.add(compareResult);
                                adapter.notifyItemInserted(compareResultList.size() - 1);
                            }
                            requestFeatureStatusMap.put(requestId, RequestFeatureStatus.SUCCEED);
                            faceHelperIr.addName(requestId, compareResult.getUserName());

                        } else {
                            requestFeatureStatusMap.put(requestId, RequestFeatureStatus.FAILED);
                            faceHelperIr.addName(requestId, "VISITOR " + requestId);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        requestFeatureStatusMap.put(requestId, RequestFeatureStatus.FAILED);
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }


    /**
     * ??????????????????????????????{@link #REGISTER_STATUS_READY}
     *
     * @param view ????????????
     */
    public void register(View view) {
        if (registerStatus == REGISTER_STATUS_DONE) {
            registerStatus = REGISTER_STATUS_READY;
        }
    }

    /**
     * ???{@link #previewViewRgb}????????????????????????????????????????????????????????????????????????????????????
     */
    @Override
    public void onGlobalLayout() {
        previewViewRgb.getViewTreeObserver().removeOnGlobalLayoutListener(this);
        if (!checkPermissions(NEEDED_PERMISSIONS)) {
            ActivityCompat.requestPermissions(this, NEEDED_PERMISSIONS, ACTION_REQUEST_PERMISSIONS);
        } else {
            initEngine();
            initRgbCamera();
            initIrCamera();
        }
    }

    public void drawIrRectVerticalMirror(View view) {
        if (drawHelperIr!=null){
            drawHelperIr.setMirrorVertical(!drawHelperIr.isMirrorVertical());
        }
    }
    public void drawIrRectHorizontalMirror(View view) {
        if (drawHelperIr!=null){
            drawHelperIr.setMirrorHorizontal(!drawHelperIr.isMirrorHorizontal());
        }
    }
}
