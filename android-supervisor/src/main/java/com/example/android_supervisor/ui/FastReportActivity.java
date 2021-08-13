package com.example.android_supervisor.ui;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.example.android_supervisor.R;
import com.example.android_supervisor.common.ResultCallback;
import com.example.android_supervisor.common.UserSession;
import com.example.android_supervisor.entities.Attach;
import com.example.android_supervisor.entities.CacheEvent;
import com.example.android_supervisor.entities.EventPara;
import com.example.android_supervisor.entities.EventProc;
import com.example.android_supervisor.entities.EventType;
import com.example.android_supervisor.http.ServiceGenerator;
import com.example.android_supervisor.http.common.ProgressTransformer;
import com.example.android_supervisor.http.request.JsonBody;
import com.example.android_supervisor.http.response.Response;
import com.example.android_supervisor.http.response.ResponseObserver;
import com.example.android_supervisor.http.service.QuestionService;
import com.example.android_supervisor.map.MapManager;
import com.example.android_supervisor.sqlite.PrimarySqliteHelper;
import com.example.android_supervisor.sqlite.PublicSqliteHelper;
import com.example.android_supervisor.ui.dialog.VoiceRecognizeDialog;
import com.example.android_supervisor.ui.media.MediaItem;
import com.example.android_supervisor.ui.media.MediaUploadGridView;
import com.example.android_supervisor.ui.view.ProgressText;
import com.example.android_supervisor.utils.DialogUtils;
import com.example.android_supervisor.utils.SystemUtils;
import com.example.android_supervisor.utils.ToastUtils;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnTextChanged;
import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Function;

/**
 * Created by dw on 2019/7/16.  废弃 yj 公用案件上报
 */
public class FastReportActivity extends BaseActivity {
    @BindView(R.id.tv_event_new_type)
    TextView tvType;

//    @BindView(R.id.tv_event_new_position)
//    TextView tvPos;

    @BindView(R.id.et_event_new_position)
    EditText etPos;


    @BindView(R.id.tv_event_new_big)
    TextView tvBig;

    @BindView(R.id.tv_event_new_small)
    TextView tvSmall;


    @BindView(R.id.tv_event_new_standard)
    TextView tvStandard;

    @BindView(R.id.et_event_new_desc)
    EditText etDesc;


    @BindView(R.id.btn_event_new_desc_record)
    FloatingActionButton btnDescRecord;

    @BindView(R.id.gv_event_new_attaches)
    MediaUploadGridView gvAttaches;

    @BindView(R.id.gv_event_later_attaches)
    MediaUploadGridView gvLaterAttaches;

    View btnSave;

    private String cachedId;
    private EventPara mEventPara;
    private EventPara copiedEventPara;
    private VoiceRecognizeDialog voiceRecognizeDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fast_report);
        ButterKnife.bind(this);
        addSaveMenu();
        loadCachedEvent();
        initialize();
        updateUI();

        voiceRecognizeDialog = new VoiceRecognizeDialog(this);
        voiceRecognizeDialog.setCallback(new ResultCallback<String>() {
            @Override
            public void onResult(String result, int tag) {
            }

            @Override
            public void onResult(String text) {
                if (!TextUtils.isEmpty(text)) {
                    etDesc.setText(text);
                    etDesc.setSelection(text.length());
                }
            }
        });
    }

    private void addSaveMenu() {
        btnSave = addMenu("保存", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validateParas()) {
                    if (cachedId == null) {
                        cachedId = UUID.randomUUID().toString();
                    }
                    if (gvAttaches.getMediaItems() != null && gvAttaches.getMediaItems().size() > 0) {
                        if (gvLaterAttaches.getMediaItems() != null && gvLaterAttaches.getMediaItems().size() > 0) {
                            loadAttaches();
                            PrimarySqliteHelper sqliteHelper = PrimarySqliteHelper.getInstance(FastReportActivity.this);
                            sqliteHelper.getCacheEventDao().createOrUpdate(new CacheEvent(cachedId, new Date(), mEventPara, 1,0));
                            ToastUtils.show(FastReportActivity.this, "案件保存成功");
                            setResult(Activity.RESULT_OK);
                            finish();
                        } else
                            ToastUtils.show(FastReportActivity.this, "处理后附件需要上传");
                    } else
                        ToastUtils.show(FastReportActivity.this, "处理前附件需要上传");
                }
            }
        });
    }


    private void loadCachedEvent() {
        cachedId = getIntent().getStringExtra("cachedId");
        if (cachedId != null) {
            PrimarySqliteHelper sqliteHelper = PrimarySqliteHelper.getInstance(FastReportActivity.this);
            CacheEvent cacheEvent = sqliteHelper.getCacheEventDao().queryForId(cachedId);
            if (cacheEvent != null) {
                mEventPara = cacheEvent.getData();
            }
        } else {
            EventPara eventPara = (EventPara) getIntent().getSerializableExtra("eventPara");
            if (eventPara != null)
                mEventPara = eventPara;
        }
    }


    private void initialize() {
        if (mEventPara == null) {
            mEventPara = new EventPara();
            copiedEventPara = mEventPara.clone();
            // requestCurrentLocation();暂不要
        } else {
            copiedEventPara = mEventPara.clone();
        }
    }

    private void updateUI() {
        tvType.setText(mEventPara.getTypeName());
        tvStandard.setText(mEventPara.getStandardName());
        etPos.setText(mEventPara.getAddress());
        etDesc.setText(mEventPara.getDesc());
        ArrayList<Attach> attaches = mEventPara.getAttaches();
        ArrayList<Attach> laterAttaches = mEventPara.getLaterAttaches();
        if (attaches != null && attaches.size() > 0) {
            List<MediaItem> mediaItems = new ArrayList<>();
            mediaItems = obtainMediaItems(attaches);
            gvAttaches.addMediaItems(mediaItems);
        }
        if (laterAttaches != null && laterAttaches.size() > 0) {
            List<MediaItem> mediaItems = new ArrayList<>();
            mediaItems = obtainMediaItems(laterAttaches);
            gvLaterAttaches.addMediaItems(mediaItems);
        }
    }

    private List<MediaItem> obtainMediaItems(ArrayList<Attach> attaches) {
        List<MediaItem> mediaItems = new ArrayList<>();
        for (Attach attach : attaches) {
            MediaItem mediaItem = new MediaItem();
            mediaItem.setType(attach.getType());
            if (!TextUtils.isEmpty(attach.getUrl())) {
                mediaItem.setUri(Uri.parse(attach.getUrl()));
            }
            if (!TextUtils.isEmpty(attach.getThumbUrl())) {
                mediaItem.setThumbnailUri(Uri.parse(attach.getThumbUrl()));
            }
            mediaItems.add(mediaItem);
        }
        return mediaItems;
    }

//    private void requestCurrentLocation() {
//        TXLocationApi.requestSingleLocation(this, TXLocationApi.COORDINATE_TYPE_GCJ02, new ResultCallback<TencentLocation>() {
//            @Override
//            public void onResult(TencentLocation result, int tag) {
//
//            }
//
//            @Override
//            public void onResult(TencentLocation location) {
//                String areaCode = location.getCityCode();
//                if (TextUtils.isEmpty(areaCode)) {
//                    ToastUtils.show(FastReportActivity.this, "获取区域信息失败");
//                    return;
//                }
//
//                FaceSqliteHelper sqliteHelper = FaceSqliteHelper.getInstance(FastReportActivity.this);
//                List<Area> areas = sqliteHelper.getAreaDao().queryForEq("code", areaCode);
//                if (areas == null || areas.isEmpty()) {
//                    ToastUtils.show(FastReportActivity.this, "已超出管理区域，请重新选择位置");
//                    return;
//                }
//
//                mEventPara.setGeoX(location.getLongitude());
//                mEventPara.setGeoY(location.getLatitude());
//                mEventPara.setAddress(location.getAddress());
//                mEventPara.setAreaCode(areaCode);
//
//                etPos.setText(location.getAddress());
//                copiedEventPara = mEventPara.clone();
//            }
//        });
//    }

    @OnClick(R.id.tv_event_new_type)
    public void onEventType(View v) {
        Intent intent = new Intent(this, EventTypeActivity.class);
        intent.putExtra("typeId", mEventPara.getTypeId());
        intent.putExtra("standardId", mEventPara.getStandardId());
        startActivityForResult(intent, 1);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }

    @OnClick(R.id.tv_event_new_standard)
    public void onEventStandard(View v) {
        final String evtTypeId = mEventPara.getTypeId();
        if (TextUtils.isEmpty(evtTypeId)) {
            ToastUtils.show(this, "请选择案件类别");
            return;
        }

        PublicSqliteHelper sqliteHelper = PublicSqliteHelper.getInstance(this);
        final List<EventType> data = sqliteHelper.getEventTypeDao().queryForEq("pid", evtTypeId);
        final int dataSize = data.size();
        if (dataSize == 0) {
            ToastUtils.show(this, "当前选择的案件类别没有立案标准");
            return;
        }

        final List<String> dataNames = new ArrayList<>();
        for (EventType evtType : data) {
            dataNames.add(evtType.getName());
        }

        final String[] items = dataNames.toArray(new String[dataSize]);
        new AlertDialog.Builder(this)
                .setTitle("立案标准")
                .setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        EventType evtType = data.get(which);
                        mEventPara.setStandardId(evtType.getId());
                        mEventPara.setStandardName(evtType.getName());
                        tvStandard.setText(evtType.getName());
                    }
                }).show();
    }

    @OnClick(R.id.map_locate)
    public void onMap(View v) {
        MapManager.show(this, new ResultCallback<Intent>() {
            @Override
            public void onResult(Intent result, int tag) {

            }

            @Override
            public void onResult(Intent result) {
                mEventPara.setGeoX(result.getDoubleExtra("longitude", 0.0));
                mEventPara.setGeoY(result.getDoubleExtra("latitude", 0.0));
                mEventPara.setAreaCode(result.getStringExtra("areaCode"));
                mEventPara.setAddress(result.getStringExtra("address"));
                etPos.setText(mEventPara.getAddress());
            }
        });
    }

    @OnTextChanged(R.id.et_event_new_desc)
    public void onDescChanged(CharSequence s, int start, int before, int count) {
        String desc = s.toString();
        if (TextUtils.isEmpty(desc)) {
            btnDescRecord.show();
            mEventPara.setDesc(null);
        } else {
            btnDescRecord.hide();
            mEventPara.setDesc(desc);
        }
    }

    @OnClick(R.id.btn_event_new_desc_record)
    public void onDescRecord(View v) {
        voiceRecognizeDialog.show(getSupportFragmentManager(), "");
    }

    @Override
    protected void onStart() {
        super.onStart();
        voiceRecognizeDialog.register();
    }

    @Override
    protected void onStop() {
        super.onStop();
        voiceRecognizeDialog.unregister();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == 1) {
            String typeId = data.getStringExtra("typeId");
            String typeName = data.getStringExtra("typeName");
            String bigId = data.getStringExtra("bigId");
            String bigName = data.getStringExtra("bigName");
            String smallId = data.getStringExtra("smallId");
            String smallName = data.getStringExtra("smallName");

            String standardId = data.getStringExtra("standardId");
            String standardName = data.getStringExtra("standardName");

            if (!TextUtils.isEmpty(typeId)) {

                tvType.setText(typeName);
            }
            //加入大类 和小类
            if (!TextUtils.isEmpty(bigId)) {

                tvBig.setText(bigName);
            }
            if (!TextUtils.isEmpty(smallId)) {
                mEventPara.setTypeId(smallId);
                mEventPara.setTypeName(smallName);
                tvSmall.setText(smallName);
            }


            if (!TextUtils.isEmpty(standardId)) {
                mEventPara.setStandardId(standardId);
                mEventPara.setStandardName(standardName);
                tvStandard.setText(standardName);
            }


        }
    }

    @OnClick(R.id.btn_event_new_submit)
    public void onSubmit(View v) {
        SystemUtils.hideSoftInput(this, v);
        if (!validateParas()) {
            return;
        }
        DialogUtils.askYesNo(this, "提示", "您确定提交此案件？", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                reportEvent();
            }
        });
    }

    private boolean validateParas() {
        if (TextUtils.isEmpty(mEventPara.getTypeId())) {
            ToastUtils.show(this, "请选择案件类别");
            return false;
        }
        if (TextUtils.isEmpty(tvStandard.getText().toString())) {
            ToastUtils.show(this, "请选择立案标准");
            return false;
        }
        if (mEventPara.getGeoX() <= 0 || mEventPara.getGeoY() <= 0) {
            ToastUtils.show(this, "请选择事发位置");
            return false;
        }
        if (TextUtils.isEmpty(mEventPara.getDesc())) {
            ToastUtils.show(this, "请输入问题描述");
            return false;
        }
        if (gvAttaches.isEmpty()) {
            ToastUtils.show(this, "请添加处理前附件");
            return false;
        }
        if (gvLaterAttaches.isEmpty()) {
            ToastUtils.show(this, "请添加处理后附件");
            return false;
        }
        return true;
    }

    private void loadAttaches() {
        mEventPara.setSource(5);
        ArrayList<Attach> attaches = getAttaches(gvAttaches.getMediaItems(), 0);
        mEventPara.setAttaches(attaches);
        ArrayList<Attach> attache = getAttaches(gvLaterAttaches.getMediaItems(), 3);
        mEventPara.setLaterAttaches(attache);
    }

    @NonNull
    private ArrayList<Attach> getAttaches(List<MediaItem> mediaItems, int type) {
        ArrayList<Attach> attaches = new ArrayList<>();
        obtainAttaches(mediaItems, attaches, type);
        return attaches;
    }

    private void obtainAttaches(List<MediaItem> mediaItems, ArrayList<Attach> attaches, int type) {
        if (mediaItems.size() > 0) {
            for (MediaItem mediaItem : mediaItems) {
                Attach attach = new Attach();
                attach.setUsage(type);
                attach.setType(mediaItem.getType());
                if (mediaItem.getUri() != null) {
                    String[] split = mediaItem.getUri().toString().split("/");
                    Log.d("report", "obtainAttaches url: " + mediaItem.getUri().toString());
                    Log.d("report", "obtainAttaches type: " + split[split.length - 1]);
                    if (mediaItem.getUri().toString().contains("http"))
                        attach.setUrl(split[split.length - 1]);
                    else
                        attach.setUrl(mediaItem.getUri().toString());
                }
                if (mediaItem.getThumbnailUri() != null) {
                    attach.setThumbUrl(mediaItem.getThumbnailUri().toString());
                }
                if (attach.getUrl() != null) {
                    attaches.add(attach);
                }
            }
        }
    }

    private void reportEvent() {
        QuestionService questionService = ServiceGenerator.create(QuestionService.class);
        Observable<Response<List<EventProc>>> observable = questionService.getEventlistProcess("1");
        observable.compose(this.<Response<List<EventProc>>>bindToLifecycle())
                .flatMap(new Function<Response<List<EventProc>>, ObservableSource<Response<String>>>() {
                    @Override
                    public ObservableSource<Response<String>> apply(Response<List<EventProc>> response) throws Exception {
                        if (!response.isSuccess()) {
                            throw new IllegalAccessException("获取案件流程失败");
                        }
                        if (response.getData() == null) {
                            throw new IllegalAccessException("获取案件流程失败");
                        }
                        String procId = null;
                        for (EventProc eventProc : response.getData()) {
                            if (eventProc.getKey().equals("fast_process")) {
                                procId = eventProc.getId();
                                break;
                            }
                        }
                        if (procId == null) {
                            throw new IllegalAccessException("获取案件流程失败");
                        }
                        mEventPara.setProcId(procId);
                        mEventPara.setUserName(UserSession.getUserName(FastReportActivity.this));
                        mEventPara.setMobile(UserSession.getMobile(FastReportActivity.this));
                        // loadAttaches();
                        QuestionService questionService = ServiceGenerator.create(QuestionService.class);
                        return questionService.addEvent(new JsonBody(mEventPara));
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .compose(new ProgressTransformer<Response<String>>(this, ProgressText.submit))
                .subscribe(new ResponseObserver<String>(this) {
                    @Override
                    public void onSuccess(String title) {
                        // 上报成功后需要删除暂存案件
                        if (cachedId != null) {
                            PrimarySqliteHelper sqliteHelper = PrimarySqliteHelper.getInstance(FastReportActivity.this);
                            sqliteHelper.getCacheEventDao().deleteById(cachedId);
                        }

                        String message = "案件\"" + title + "\"上报成功";
                        DialogUtils.show(FastReportActivity.this, message, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                setResult(Activity.RESULT_OK);
                                finish();
                            }
                        });
                    }

                    @Override
                    public void onFailure(int code, String errMsg) {
                        super.onFailure(code, errMsg);
                        showFailureDialog();
                    }
                });
    }

    private void showFailureDialog() {
        String message = "案件上报失败，是否保存此案件？";
        DialogUtils.askYesNo(FastReportActivity.this, null, message, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                loadAttaches();
                if (cachedId == null) {
                    cachedId = UUID.randomUUID().toString();
                }
                PrimarySqliteHelper sqliteHelper = PrimarySqliteHelper.getInstance(FastReportActivity.this);
                sqliteHelper.getCacheEventDao().createOrUpdate(new CacheEvent(cachedId, new Date(), mEventPara, 1,0));

                ToastUtils.show(FastReportActivity.this, "案件保存成功");
                setResult(Activity.RESULT_OK);
                finish();
            }
        });
    }

    @Override
    public void onBackPressed() {
        loadAttaches();
        if (mEventPara.equals(copiedEventPara)) {
            super.onBackPressed();
        } else {
            String message = "是否放弃已经编辑的信息？";
            DialogUtils.askYesNo(FastReportActivity.this, "提示", message, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    FastReportActivity.super.onBackPressed();
                }
            });
        }
    }

    public EventPara getValue() {
        return mEventPara;
    }
}
