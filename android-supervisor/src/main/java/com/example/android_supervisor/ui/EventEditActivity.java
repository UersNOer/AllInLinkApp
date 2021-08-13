package com.example.android_supervisor.ui;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import androidx.recyclerview.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.example.android_supervisor.R;
import com.example.android_supervisor.common.ResultCallback;
import com.example.android_supervisor.common.UserSession;
import com.example.android_supervisor.entities.Attach;
import com.example.android_supervisor.entities.CaseLevel;
import com.example.android_supervisor.entities.EventRes;
import com.example.android_supervisor.entities.EventType;
import com.example.android_supervisor.entities.MapBundle;
import com.example.android_supervisor.http.ServiceGenerator;
import com.example.android_supervisor.http.common.ProgressTransformer;
import com.example.android_supervisor.http.request.JsonBody;
import com.example.android_supervisor.http.response.Response;
import com.example.android_supervisor.http.response.ResponseObserver;
import com.example.android_supervisor.http.service.QuestionService;
import com.example.android_supervisor.map.AMapManager;
import com.example.android_supervisor.sqlite.PublicSqliteHelper;
import com.example.android_supervisor.ui.dialog.VoiceRecognizeDialog;
import com.example.android_supervisor.ui.media.MediaGridView;
import com.example.android_supervisor.ui.media.MediaItem;
import com.example.android_supervisor.ui.media.MediaUploadGridView;
import com.example.android_supervisor.ui.media.OnDataChangedListener;
import com.example.android_supervisor.ui.view.MenuItemView;
import com.example.android_supervisor.ui.view.ProgressText;
import com.example.android_supervisor.utils.DialogUtils;
import com.example.android_supervisor.utils.SystemUtils;
import com.example.android_supervisor.utils.ToastUtils;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnTextChanged;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;

/**
 * @author wujie
 */
public class EventEditActivity extends BaseActivity {

    @BindView(R.id.mv_type)
    public MenuItemView mv_type;

    @BindView(R.id.mv_case_big)
    public MenuItemView mv_case_big;

    @BindView(R.id.mv_case_small)
    public MenuItemView mv_case_small;

    @BindView(R.id.mv_standard)
    public MenuItemView mv_standard;

    @BindView(R.id.mv_level)
    public MenuItemView mv_level;


//    @BindView(R.id.tv_event_new_type)
//    FocusedTextView tvType;
//    @BindView(R.id.tv_event_new_standard)
//    FocusedTextView tvStandard;

//    @BindView(R.id.tv_event_new_big)
//    FocusedTextView tv_event_new_big;
//
//    @BindView(R.id.tv_event_new_small)
//    FocusedTextView  tv_event_new_small;

    @BindView(R.id.et_event_new_position)
    EditText etPos;

    @BindView(R.id.et_event_new_desc)
    EditText etDesc;

    @BindView(R.id.tv_showCoordinate)
    TextView tv_showCoordinate;

//    @BindView(R.id.tv_event_new_caseType)
//    OptionalTextView tv_event_new_caseType;


    @BindView(R.id.btn_event_new_desc_record)
    FloatingActionButton btnDescRecord;

    @BindView(R.id.gv_event_new_attaches)
    MediaUploadGridView gvAttaches;
    @BindView(R.id.gv_event_new_attaches_later)
    MediaUploadGridView gvAttachesLater;

    @BindView(R.id.choose_recycler)
    RecyclerView recyclerView;

    private String id;
    private int status;
    private String areaCode;

    private EventRes mEventRes;
    private VoiceRecognizeDialog voiceRecognizeDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_new);
        ButterKnife.bind(this);
        gvAttaches.setVisibility(View.VISIBLE);
        gvAttachesLater.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.GONE);
        fetchEventById();
        initVoiceRecognize();

    }

    private void initVoiceRecognize() {
        voiceRecognizeDialog = new VoiceRecognizeDialog(this);
        voiceRecognizeDialog.setCallback(new ResultCallback<String>() {
            @Override
            public void onResult(String text, int tag) {
                if (!TextUtils.isEmpty(text)) {
                    if (tag == 0) {
                        StringBuffer sb = new StringBuffer(etDesc.getText().toString().trim()).append(text);
                        etDesc.setText(sb.toString());
                        etDesc.setSelection(sb.toString().length());
                    } else {
                        StringBuffer sb = new StringBuffer(etPos.getText().toString().trim()).append(text);
                        etPos.setText(sb.toString());
                        etPos.setSelection(sb.toString().length());
                    }
                }
            }

            @Override
            public void onResult(String result) {

            }
        });
    }

    private void fetchEventById() {
        this.id = getIntent().getStringExtra("id");
        this.status = getIntent().getIntExtra("status", 1);
        this.areaCode = getIntent().getStringExtra("areaCode");
        QuestionService questionService = ServiceGenerator.create(QuestionService.class);
        Observable<Response<EventRes>> observable;
        switch (status) {
            case 1:
                observable = questionService.getHcTaskById(id);
                break;
            case 2:
                observable = questionService.getHsTaskById(id);
                break;
            default:
                return;
        }
        observable.compose(this.<Response<EventRes>>bindToLifecycle())
                .observeOn(AndroidSchedulers.mainThread())
                .compose(new ProgressTransformer<Response<EventRes>>(this, ProgressText.load))
                .subscribe(new ResponseObserver<EventRes>(this) {
                    @Override
                    public void onSuccess(EventRes data) {
                        mEventRes = data;
                        updateUI();
                    }
                });
    }

    private void updateUI() {
        mv_case_small.setValue(mEventRes.getTypeName());

        mv_standard.setValue(mEventRes.getStandardName());

        etPos.setText(mEventRes.getAddress());
        etPos.setSelection((mEventRes.getAddress()) == null ? 0 : (mEventRes.getAddress()).length());
        etDesc.setText(mEventRes.getDesc());


        EventType eventType_small =  PublicSqliteHelper.getInstance(this).getEventTypeDao().queryForId(mEventRes.getTypeId());
        if (eventType_small!=null){
            //获取大类
            EventType eventType_big =  PublicSqliteHelper.getInstance(this).getEventTypeDao().queryForId(eventType_small.getPid());
            if (eventType_big!=null){
                 mv_case_big.setValue(eventType_big.getName());
                if (eventType_big == null){
                    return;
                }

                //获取类别
                EventType eventType_type =  PublicSqliteHelper.getInstance(this).getEventTypeDao().queryForId(eventType_big.getPid());
                if (eventType_type!=null){
                    mv_type.setValue(eventType_type.getName());
                }

            }
        }
//

        //立案标准
//        EventType eventType_bz =  FaceSqliteHelper.getInstance(this).getEventTypeDao().queryForId(mEventRes.getStandardId());
//        if (eventType_bz!=null){
//            tvStandard.setText(eventType_bz.getName());
//            tvStandard.setClickable(false);
//            mv_standard.setValue(eventType_bz.getName());
//        }

        //修改案件类型
        List<CaseLevel> eventType_levels =  PublicSqliteHelper.getInstance(this).getCaseLevelDao().queryForAll();
        if (eventType_levels!=null&& eventType_levels.size()>0){
            for (CaseLevel caseLevel:eventType_levels) {
                if (caseLevel.getDictCode().equals(String.valueOf(mEventRes.getLevel()))){
                    mv_level.setValue(caseLevel.getDictName());
                    break;
                }
            }
        }

        ArrayList<Attach> attaches = new ArrayList<>();
        ArrayList<Attach> attachesLater = new ArrayList<>();
        for (Attach attach : mEventRes.getAttaches()) {
            if (attach.getUsage() == 3) {
                attachesLater.add(attach);
            } else
                attaches.add(attach);
        }

        List<MediaItem> mediaItem = setAttachesView(attaches);
        List<MediaItem> mediaItemLater = setAttachesView(attachesLater);
        setAttachVisibility(mediaItem, gvAttaches, 0);
        setAttachVisibility(mediaItemLater, gvAttachesLater, 3);
    }

    private void setAttachVisibility(final List<MediaItem> mediaItem, MediaGridView view, final int type) {
        view.setOnDataChangedListener(new OnDataChangedListener() {
            @Override
            public void onRemove(MediaItem mediaItems) {
                List<Attach> removeList = new ArrayList<>();
                Attach attach = obtainAttaches(mediaItems, type);
                if (attach != null)
                    removeList.add(attach);
                mEventRes.setRemoveAttchList(removeList);
            }

            @Override
            public void onDataChanged(MediaItem mediaItems) {
                Log.d("dawn", "onDataChanged: "+"\t"+type);
                List<Attach> addList = new ArrayList<>();
                Attach attach = obtainAttaches(mediaItems, type);
                if (attach != null)
                    addList.add(attach);
                mEventRes.setNewAttchList(addList);
            }
        });
        if (mediaItem != null && mediaItem.size()>0) {
            view.setVisibility(View.VISIBLE);
            view.addMediaItems(mediaItem);
        } else {
            view.setVisibility(View.GONE);
        }
    }

    private Attach obtainAttaches(MediaItem mediaItem, int type) {
        Attach attach = new Attach();
        attach.setUsage(type);
        attach.setType(mediaItem.getType());
        if (mediaItem.getUri() != null) {
            String[] split = mediaItem.getUri().toString().split("/");
            if (mediaItem.getUri().toString().contains("http"))
                attach.setUrl(split[split.length - 1]);
            else
                attach.setUrl(mediaItem.getUri().toString());
        }
        if (mediaItem.getThumbnailUri() != null) {
            String[] split = mediaItem.getThumbnailUri().toString().split("/");
            if (mediaItem.getThumbnailUri().toString().contains("http"))
                attach.setThumbUrl(split[split.length - 1]);
            else
                attach.setThumbUrl(mediaItem.getThumbnailUri().toString());
        }
        return attach;
    }

    private List<MediaItem> setAttachesView(List<Attach> attaches) {
        List<MediaItem> mediaItems = new ArrayList<>();
        if (attaches != null && attaches.size() > 0) {
            for (Attach attach : attaches) {
                MediaItem mediaItem = new MediaItem();
                mediaItem.setType(attach.getType());
                if (!TextUtils.isEmpty(attach.getUrl())) {
                    Uri coverUri = Uri.parse(attach.getUrl());
                    coverUri = getUri(attach.getUrl(), coverUri);
                    mediaItem.setUri(coverUri);
                }
                if (!TextUtils.isEmpty(attach.getThumbUrl())) {
                    Uri coverUri = Uri.parse(attach.getThumbUrl());
                    coverUri = getUri(attach.getThumbUrl(), coverUri);
                    mediaItem.setThumbnailUri(coverUri);
                }
                mediaItems.add(mediaItem);
            }
        }
        return mediaItems;
    }

    private Uri getUri(String attachUrl, Uri coverUri) {
        if (!attachUrl.contains("http")) {
            String fileServer = UserSession.getFileServer(this);
            if (!TextUtils.isEmpty(fileServer)){
                String path = new StringBuilder(fileServer).append(attachUrl).toString();
                coverUri = Uri.parse(path);
            }

        }
        return coverUri;
    }

//    @OnClick(R.id.tv_event_new_type)
//    public void onEventType(View v) {
//        Intent intent = new Intent(this, EventTypeActivity.class);
//        intent.putExtra("typeId", mEventRes.getTypeId());
//        intent.putExtra("standardId", mEventRes.getStandardId());
//        startActivityForResult(intent, 1);
//        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
//    }


    @OnClick(R.id.btn_event_new_desc_record)
    public void onDescRecord(View v) {
        voiceRecognizeDialog.setTag(0);
        voiceRecognizeDialog.show(getSupportFragmentManager(), "");
    }

    @OnClick(R.id.iv_recordMap)
    public void onMapDescRecord(View v) {
        voiceRecognizeDialog.setTag(1);
        voiceRecognizeDialog.show(getSupportFragmentManager(), "");
    }

    @OnClick(R.id.iv_enterMap)
    public void onMap(View v) {
        Bundle bundle  = new Bundle();

        MapBundle mapBundle = new MapBundle();
        if (mEventRes.getGeoX()!=0.0 && mEventRes.getGeoY()!=0.0){
            mapBundle.longitude  = mEventRes.getGeoX();
            mapBundle.latitude  = mEventRes.getGeoY();
        }
        mapBundle.isShowGird = false;
        mapBundle.isShowCase = false;
        bundle.putSerializable("mapBundle",mapBundle);

        AMapManager.show(this, bundle, new ResultCallback<Intent>() {
            @Override
            public void onResult(Intent result, int tag) {
            }

            @Override
            public void onResult(Intent result) {
                if (result == null){
                    return;
                }
                mEventRes.setGeoX(result.getDoubleExtra("longitude", 0.0));
                mEventRes.setGeoY(result.getDoubleExtra("latitude", 0.0));
                mEventRes.setAreaCode(result.getStringExtra("areaCode"));
                mEventRes.setAddress(result.getStringExtra("address"));
                etPos.setText(mEventRes.getAddress());
                tv_showCoordinate.setText(mEventRes.getGeoX() + "," + mEventRes.getGeoY());
            }
        });
    }

    @OnTextChanged(R.id.et_event_new_desc)
    public void onDescChanged(CharSequence s, int start, int before, int count) {
        String desc = s.toString();
        if (TextUtils.isEmpty(desc)) {
            btnDescRecord.show();
            mEventRes.setDesc(null);
        } else {
//            btnDescRecord.hide();
            mEventRes.setDesc(desc);
        }
    }

//    @OnClick(R.id.tv_event_new_standard)
//    public void onEventStandard(View v) {
//        final String evtTypeId = mEventRes.getTypeId();
//        if (TextUtils.isEmpty(evtTypeId)) {
//            ToastUtils.show(this, "请选择案件类别");
//            return;
//        }
//
//        FaceSqliteHelper sqliteHelper = FaceSqliteHelper.getInstance(this);
//        final List<EventType> data = sqliteHelper.getEventTypeDao().queryForEq("pid", evtTypeId);
//        final int dataSize = data.size();
//        if (dataSize == 0) {
//            ToastUtils.show(this, "当前选择的案件类别没有立案标准");
//            return;
//        }
//
//        final List<String> dataNames = new ArrayList<>();
//        for (EventType evtType : data) {
//            dataNames.add(evtType.getName());
//        }
//
//        final String[] items = dataNames.toArray(new String[dataSize]);
//        new AlertDialog.Builder(this)
//                .setTitle("立案标准")
//                .setItems(items, new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        EventType evtType = data.get(which);
//                        mEventRes.setStandardId(evtType.getId());
//                        mEventRes.setStandardName(evtType.getName());
//                        mv_standard.setValue(evtType.getName());
//                    }
//                }).show();
//    }


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

                mv_type.setValue(typeName);
            }
            //加入大类 和小类
            if (!TextUtils.isEmpty(bigId)) {

                mv_case_big.setValue(bigName);
            }
            if (!TextUtils.isEmpty(smallId)) {

                mEventRes.setTypeId(smallId);
                mEventRes.setTypeName(smallName);

                mv_case_small.setValue(smallName);
            }

            if (!TextUtils.isEmpty(standardId)) {
                mEventRes.setStandardId(standardId);
                mEventRes.setStandardName(standardName);
                mv_standard.setValue(standardName);
            }

//            String typeId = data.getStringExtra("typeId");
//            String typeName = data.getStringExtra("typeName");
//            String standardId = data.getStringExtra("standardId");
//            String standardName = data.getStringExtra("standardName");
//
//            if (!TextUtils.isEmpty(typeId)) {
//                mEventRes.setTypeId(typeId);
//                mEventRes.setTypeName(typeName);
//            }
//            if (!TextUtils.isEmpty(standardId)) {
//                mEventRes.setStandardId(standardId);
//                mEventRes.setStandardName(standardName);
//            }
//            tvType.setText(typeName);
//            tvStandard.setText(standardName);
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
        if (TextUtils.isEmpty(mEventRes.getTypeId())) {
            ToastUtils.show(this, "请选择案件类别");
            return false;
        }
        if (TextUtils.isEmpty(mv_standard.getValue())) {
            ToastUtils.show(this, "请选择立案标准");
            return false;
        }
        if (mEventRes.getGeoX() <= 0 || mEventRes.getGeoY() <= 0) {
            ToastUtils.show(this, "请选择事发位置");
            return false;
        }
        if (TextUtils.isEmpty(mEventRes.getDesc())) {
            ToastUtils.show(this, "请输入问题描述");
            return false;
        }
        if (TextUtils.isEmpty(tv_showCoordinate.getText().toString().trim())) {
            ToastUtils.show(this, "请选择坐标");
            return false;
        }

//        if (gvAttaches.isEmpty()) {
//            ToastUtils.show(this, "请添加附件");
//            return false;
//        }
        return true;
    }

    @SuppressWarnings("unchecked")
    private void reportEvent() {
        mEventRes.setAreaCode(areaCode);
        mEventRes.setAddress(etPos.getText().toString().trim());
        QuestionService questionService = ServiceGenerator.create(QuestionService.class);
        Observable<Response> observable;
        switch (status) {
            case 1:
                observable = questionService.editEvent(new JsonBody(mEventRes));
                break;
            case 2:
                observable = questionService.editTmpEvent(new JsonBody(mEventRes));
                break;
            default:
                return;
        }
        observable.compose(this.<Response>bindToLifecycle())
                .observeOn(AndroidSchedulers.mainThread())
                .compose(new ProgressTransformer<Response>(this, ProgressText.submit))
                .subscribe(new ResponseObserver(this) {
                    @Override
                    public void onSuccess(Object data) {
                        ToastUtils.show(EventEditActivity.this, "修改成功");
                        setResult(Activity.RESULT_OK);
                        finish();
                    }
                });
    }
}
