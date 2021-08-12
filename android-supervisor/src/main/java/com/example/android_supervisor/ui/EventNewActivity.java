package com.example.android_supervisor.ui;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import androidx.annotation.NonNull;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android_supervisor.Presenter.CaseConfirmPresenter;
import com.example.android_supervisor.R;
import com.example.android_supervisor.common.ResultCallback;
import com.example.android_supervisor.common.UserSession;
import com.example.android_supervisor.entities.Attach;
import com.example.android_supervisor.entities.CacheEvent;
import com.example.android_supervisor.entities.ChooseData;
import com.example.android_supervisor.entities.EventPara;
import com.example.android_supervisor.entities.EventProc;
import com.example.android_supervisor.entities.EventType;
import com.example.android_supervisor.entities.MapBundle;
import com.example.android_supervisor.entities.PhotoEntity;
import com.example.android_supervisor.http.ServiceGenerator;
import com.example.android_supervisor.http.response.Response;
import com.example.android_supervisor.http.service.QuestionService;
import com.example.android_supervisor.map.AMapManager;
import com.example.android_supervisor.service.CaseReportService;
import com.example.android_supervisor.sqlite.PrimarySqliteHelper;
import com.example.android_supervisor.sqlite.PublicSqliteHelper;
import com.example.android_supervisor.ui.dialog.VoiceRecognizeDialog;
import com.example.android_supervisor.ui.media.MediaItem;
import com.example.android_supervisor.ui.media.MediaUploadGridView;
import com.example.android_supervisor.ui.view.MenuItemView;
import com.example.android_supervisor.ui.view.OptionalTextView;
import com.example.android_supervisor.utils.CaseBindUtils;
import com.example.android_supervisor.utils.DialogUtils;
import com.example.android_supervisor.utils.Environments;
import com.example.android_supervisor.utils.FileServerUtils;
import com.example.android_supervisor.utils.LogUtils;
import com.example.android_supervisor.utils.SystemUtils;
import com.example.android_supervisor.utils.TextChangeUtils;
import com.example.android_supervisor.utils.ToastUtils;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnTextChanged;
import retrofit2.Call;
import retrofit2.Callback;


/**
 * @author yj
 */
public class EventNewActivity extends BaseActivity {
    @BindView(R.id.mv_case_type)
    MenuItemView mv_case_type;

    @BindView(R.id.mv_case_big)
    MenuItemView mv_case_big;

    @BindView(R.id.mv_case_small)
    MenuItemView mv_case_small;

    @BindView(R.id.mv_standard)
    MenuItemView mv_standard;

    @BindView(R.id.mv_level)
    MenuItemView mv_level;

    @BindView(R.id.et_event_new_position)
    EditText etPos;

    @BindView(R.id.tv_showCoordinate)//坐标位置
            TextView tv_showCoordinate;

    @BindView(R.id.et_event_new_desc)
    EditText etDesc;


    @BindView(R.id.tv_pos_character)
    TextView tv_pos_character;

    @BindView(R.id.tv_desc_character)
    TextView tv_desc_character;

    @BindView(R.id.iv_recordMap)
    FloatingActionButton iv_recordMap;

    @BindView(R.id.btn_event_new_desc_record)
    FloatingActionButton btnDescRecord;

    @BindView(R.id.gv_event_new_attaches)
    MediaUploadGridView mediaUploadGridView;

    @BindView(R.id.gv_event_new_attaches_later)
    MediaUploadGridView mediaUploadGridView_later;


    @BindView(R.id.tv_attahc_hint)
    TextView tv_attahc_hint;

    @BindView(R.id.tv_laterAttahc_hint)
    TextView tv_laterAttahc_hint;


    @BindView(R.id.rb_qb)
    RadioButton rb_qb;

    @BindView(R.id.rb_jb)
    RadioButton rb_jb;

    @BindView(R.id.ll_bd)
    LinearLayout ll_bd;


    View btnSave,btnAutoSave;

    private String cachedId;
    private EventPara mEventPara;
    private EventPara copiedEventPara;
    private VoiceRecognizeDialog voiceRecognizeDialog;
    private boolean isUpload = true;//是否上传图片（默认：是）

    ArrayList<Attach> attaches;

    private boolean isFastCaseReport = false;//案件上报是4，快速上报是5 source来区分
    private int source = 4;
    private boolean isRb;

    List<EventProc> eventProcs = null;//案件流程数据

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_report);
        ButterKnife.bind(this);
        getEventProc();//获取案件流程
        initCaseConfirm();

        initReportType();
        addSaveMenu();
        initialize();
        dataBind();
        loadCachedEvent();
        updateAttachUi();
        initPhotoConfig();

    }

    private void initCaseConfirm() {
        //得到案件二次确认
        CaseConfirmPresenter caseConfirmPresenter = new CaseConfirmPresenter();
        List<String> arrayList = new ArrayList<>();
        arrayList.add(CaseConfirmPresenter.CASE_SECONDARY_REPORT_VALID);
        arrayList.add(CaseConfirmPresenter.CASE_SECONDARY_REPORT_URL);
        caseConfirmPresenter.getCaseConfirm(this, arrayList, new CaseConfirmPresenter.CaseConfirmCallBack() {
            @Override
            public void onSuccess() {
                if (Environments.isCaseCheck) {
                    checkOverLayPermission();
                }
            }
        });

    }

    private void updateAttachUi() {
        if (mEventPara.getSource() == 4) {//普通上报
            setTitle("案件上报");
            mediaUploadGridView_later.setVisibility(View.GONE);
            tv_attahc_hint.setVisibility(View.GONE);
            tv_laterAttahc_hint.setVisibility(View.GONE);
        } else if (mEventPara.getSource() == 5) {
            setTitle("快速上报");
            mediaUploadGridView_later.setVisibility(View.VISIBLE);
            tv_attahc_hint.setVisibility(View.VISIBLE);
            tv_laterAttahc_hint.setVisibility(View.VISIBLE);
        }

    }



    private void initReportType() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            source = bundle.getInt("source");
        }
    }

    private void initPhotoConfig() {
        try {
            PhotoEntity photoEntity = PublicSqliteHelper.getInstance(this).getPhotoSettingDao().queryBuilder().where()
                    .eq("type", "1")
                    .queryForFirst();
            if (photoEntity != null) {
                mediaUploadGridView.setUploadNum(photoEntity.getUploadNum());
                isUpload = photoEntity.getIsUpload().equals("1") ? true : false;
                mediaUploadGridView.setIsAlbum(photoEntity.getIsAlbum().equals("1") ? true : false);

                if (source == 5) {
                    mediaUploadGridView_later.setUploadNum(photoEntity.getUploadNum());
                    mediaUploadGridView_later.setIsAlbum(photoEntity.getIsAlbum().equals("1") ? true : false);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void dataBind() {
        mv_level.setSelectListener(new OptionalTextView.SelectListener() {
            @Override
            public void selected(int which, ChooseData chooseData) {
                mv_level.setValue(chooseData.getName());
                mEventPara.setLevel(chooseData.getId());
            }
        });
        CaseBindUtils.bindCaseLevel(this, mv_level);

    }


    private void addSaveMenu() {
        btnSave = addMenu("暂存", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if (cachedId == null) {
                        cachedId = UUID.randomUUID().toString();
                    }
                    //至少保存一项才能提交
                    if (!check()) {
                        ToastUtils.show(EventNewActivity.this, "请填写至少一项");
                        return;
                    }
                    mEventPara.setBidType(rb_jb.isChecked()?"0":"1");
                    loadAttaches();
                    //暂存不需要完全保存数据
                    PrimarySqliteHelper sqliteHelper = PrimarySqliteHelper.getInstance(EventNewActivity.this);
                    sqliteHelper.getCacheEventDao().createOrUpdate(new CacheEvent(cachedId, new Date(), mEventPara, source,0));
                    ToastUtils.show(EventNewActivity.this, "案件暂存成功");


                    setResult(Activity.RESULT_OK);
                    finish();

                } catch (Exception e) {
                    e.printStackTrace();
                    ToastUtils.show(EventNewActivity.this, "案件暂存失败:" + e.getMessage());
                }


            }
        });

        btnAutoSave = addMenu("保存", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if (cachedId == null) {
                        cachedId = UUID.randomUUID().toString();
                    }

                    SystemUtils.hideSoftInput(EventNewActivity.this, v);
                    if (!validateParas()) {
                        return;
                    }
                    addToEntity();

                    PrimarySqliteHelper sqliteHelper = PrimarySqliteHelper.getInstance(EventNewActivity.this);
                    sqliteHelper.getCacheEventDao().createOrUpdate(new CacheEvent(cachedId, new Date(), mEventPara, source,1));
                    ToastUtils.show(EventNewActivity.this, "案件保存成功");


                    //重启
//                    Intent intent = new Intent(EventNewActivity.this, AutoUploadService.class);
//                    startService(intent);

                    setResult(Activity.RESULT_OK);
                    finish();




                } catch (Exception e) {
                    e.printStackTrace();
                    ToastUtils.show(EventNewActivity.this, "案件保存失败:" + e.getMessage());
                }
            }
        });
    }

    private void loadCachedEvent() {
        cachedId = getIntent().getStringExtra("cachedId");
        if (cachedId != null) {
            PrimarySqliteHelper sqliteHelper = PrimarySqliteHelper.getInstance(EventNewActivity.this);
            CacheEvent cacheEvent = sqliteHelper.getCacheEventDao().queryForId(cachedId);
            if (cacheEvent != null) {
                mEventPara = cacheEvent.getData();
                if (mEventPara != null) {
                    updateUI();
                }
            }
        } else {
            EventPara eventPara = (EventPara) getIntent().getSerializableExtra("eventPara");
            if (eventPara != null) {
                mEventPara = eventPara;
                if (mEventPara != null) {
                    updateUI();
                }
            }
        }
    }

    private void initialize() {
        if (mEventPara == null) {
            mEventPara = new EventPara();
            copiedEventPara = mEventPara.clone();
            //requestCurrentLocation();
        } else {
            copiedEventPara = mEventPara.clone();
        }
        mEventPara.setSource(source);
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

        TextChangeUtils textChangeUtils = new TextChangeUtils(etPos, tv_pos_character, this);
        TextChangeUtils textChangeUtil1 = new TextChangeUtils(etDesc, tv_desc_character, this);
    }

    /**
     * 从缓存更新UI
     */
    private void updateUI() {
        source = mEventPara.getSource();

        mv_case_small.setValue(mEventPara.getTypeName());
        mv_standard.setValue(mEventPara.getStandardName());


        if (mEventPara.getTypeId() != null) {
            EventType eventType_small = PublicSqliteHelper.getInstance(this).getEventTypeDao().queryForId(mEventPara.getTypeId());
            if (eventType_small != null) {
                EventType eventType_big = CaseBindUtils.bindCaseUIById(EventNewActivity.this, eventType_small.getPid(), mv_case_big);
                if (eventType_big != null) {
                    EventType eventType_type = CaseBindUtils.bindCaseUIById(EventNewActivity.this, eventType_big.getPid(), mv_case_type);
                    if (eventType_type!=null && (eventType_type.getName().contains("绿化") || eventType_type.getName().contains("环卫"))){
                        ll_bd.setVisibility(View.VISIBLE);
                        rb_qb.setChecked("0".equals(mEventPara.getBidType())?true:false);
                        rb_jb.setChecked("1".equals(mEventPara.getBidType())?true:false);
                    }
                }
            }
        }
        CaseBindUtils.bindLevleUIById(EventNewActivity.this, String.valueOf(mEventPara.getLevel()), mv_level);
        etPos.setText(mEventPara.getAddress());
        etDesc.setText(mEventPara.getDesc());
        tv_showCoordinate.setText(mEventPara.getGeoX() + "," + mEventPara.getGeoY());

        mediaUploadGridView.setVisibility(isUpload ? View.VISIBLE : View.GONE);
        mediaUploadGridView_later.setVisibility(isUpload && source == 5 ? View.VISIBLE : View.GONE);

        updateAttachData(mEventPara.getAttaches(), mediaUploadGridView);
        if (source == 5) updateAttachData(mEventPara.getLaterAttaches(), mediaUploadGridView_later);
    }

    private void updateAttachData(List<Attach> attaches, MediaUploadGridView mediaUploadGridView) {
        if (attaches == null || attaches.size() <= 0) {
            return;
        }
        List<MediaItem> mediaItems = new ArrayList<>();

        for (Attach attach : attaches) {
            MediaItem mediaItem = new MediaItem();
            mediaItem.setType(attach.getType());
            mediaItem.setProgress(100);
            if (!TextUtils.isEmpty(attach.getUrl())) {
                String path = FileServerUtils.getPath(this, attach.getUrl());
                mediaItem.setUri(Uri.parse(path));
            }
            if (!TextUtils.isEmpty(attach.getThumbUrl())) {
                String path = FileServerUtils.getUrl(this, attach.getThumbUrl());
                mediaItem.setThumbnailUri(Uri.parse(path));
            }
            mediaItems.add(mediaItem);
        }
        mediaUploadGridView.setAttach(mediaItems);
    }

    @OnClick(R.id.mv_case_type)
    public void onEventType(View v) {
        Intent intent = new Intent(this, EventTypeActivity.class);
        intent.putExtra("typeId", mEventPara.getTypeId());
        intent.putExtra("standardId", mEventPara.getStandardId());
        startActivityForResult(intent, 1);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }

    @OnClick(R.id.mv_standard)
    public void onEventStandard(View v) {
        final String evtTypeId = mEventPara.getTypeId();
        if (TextUtils.isEmpty(evtTypeId)) {
            ToastUtils.show(this, "请选择案件类别");
            return;
        }
        CaseBindUtils.bindCaseStandard(this, evtTypeId, mv_standard);
        mv_standard.setSelectListener(new OptionalTextView.SelectListener() {
            @Override
            public void selected(int which, ChooseData chooseData) {
                mv_standard.setValue(chooseData.getName());
                mEventPara.setStandardId(chooseData.getId());
                mEventPara.setStandardName(chooseData.getName());
            }
        });
    }

    @OnClick(R.id.iv_enterMap)
    public void onMap(View v) {
        Bundle bundle = new Bundle();

        MapBundle mapBundle = new MapBundle();
        if (mEventPara.getGeoX() != 0.0 && mEventPara.getGeoY() != 0.0) {
            mapBundle.longitude = mEventPara.getGeoX();
            mapBundle.latitude = mEventPara.getGeoY();
        }
        mapBundle.isShowGird = false;
        mapBundle.isShowCase = false;
        bundle.putSerializable("mapBundle", mapBundle);

        AMapManager.show(this, bundle, new ResultCallback<Intent>() {
            @Override
            public void onResult(Intent result, int tag) {
            }

            @Override
            public void onResult(Intent result) {
                mEventPara.setGeoX(result.getDoubleExtra("longitude", 0.0));
                mEventPara.setGeoY(result.getDoubleExtra("latitude", 0.0));
//                mEventPara.setAreaCode(result.getStringExtra("areaCode"));
                mEventPara.setAddress(result.getStringExtra("address"));
                etPos.setText(mEventPara.getAddress());
                tv_showCoordinate.setText(mEventPara.getGeoX() + "," + mEventPara.getGeoY());

                mEventPara.setAreaCode(result.getStringExtra("areaCode"));
                mEventPara.setWorkGridId(result.getStringExtra("workGridId"));
                mEventPara.setGridId(result.getStringExtra("gridId"));
                mEventPara.setManageGridId(result.getStringExtra("manageGridId"));
//                ToastUtils.show(this,mEventPara.getManageGridId());
                LogUtils.d("网格"+mEventPara.getAreaCode());

            }
        });
    }

    private String temp;

    @OnTextChanged(R.id.et_event_new_desc)
    public void onDescChanged(CharSequence s, int start, int before, int count) {
        String desc = s.toString().trim();
        if (TextUtils.isEmpty(desc)) {
            btnDescRecord.show();
            mEventPara.setDesc(null);
        } else {
            //  btnDescRecord.hide();
            mEventPara.setDesc(desc);
        }
    }

    @OnTextChanged(R.id.et_event_new_position)
    public void onMapPosChanged(CharSequence s, int start, int before, int count) {
        String desc = s.toString().trim();
        if (TextUtils.isEmpty(desc)) {
            iv_recordMap.show();
            mEventPara.setAddress(null);
        } else {
            //  btnDescRecord.hide();
            mEventPara.setAddress(desc);
        }
    }

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


    @OnClick(R.id.rb_jb)
    public void rbChange(View v) {

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
    protected void onDestroy() {
        super.onDestroy();
//        if (RxBus.getDefault().isRegistered(this)) {
//            RxBus.getDefault().unregister(this);
//        }
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

                mv_case_type.setValue(typeName);
            }
            //加入大类 和小类
            if (!TextUtils.isEmpty(bigId)) {

                mv_case_big.setValue(bigName);
            }
            if (!TextUtils.isEmpty(smallId)) {

                mEventPara.setTypeId(smallId);
                mEventPara.setTypeName(smallName);

                mv_case_small.setValue(smallName);
            }

            if (!TextUtils.isEmpty(standardId)) {
                mEventPara.setStandardId(standardId);
                mEventPara.setStandardName(standardName);
                mv_standard.setValue(standardName);
            }

            ll_bd.setVisibility(mv_case_type.getValue().contains("绿化") ||mv_case_type.getValue().contains("环卫")?View.VISIBLE:View.GONE);
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
                if (eventProcs == null) {
                    Toast.makeText(EventNewActivity.this, "案件流程获取失败，正在尝试重新获取，请稍后重新上报", Toast.LENGTH_SHORT).show();
                    getEventProc();
                } else {
                    if (Environments.isCaseCheck){
                        if(!TextUtils.isEmpty(Environments.caseCheckUrl)){
                            reportEvent("");
                        }else {
                            reportEvent("");
                        }
                    }else {
                        reportEvent("");
                    }

                }

            }
        });
    }

    int count;

    private boolean validateParas() {
        if (TextUtils.isEmpty(mEventPara.getTypeId())) {
            ToastUtils.show(this, "请选择案件类别");
            return false;
        }
        if (TextUtils.isEmpty(mEventPara.getStandardId())) {
            ToastUtils.show(this, "请选择立案标准");
            return false;
        }
        if (mEventPara.getGeoX() <= 0 || mEventPara.getGeoY() <= 0 || TextUtils.isEmpty(mEventPara.getAddress())) {
            ToastUtils.show(this, "请选择事发位置");
            return false;
        }

        if (TextUtils.isEmpty(mEventPara.getAreaCode())){
            ToastUtils.show(this, "当前事发位置未获取到网格，请检查网格信息");
            return false;
        }

        if (TextUtils.isEmpty(mEventPara.getDesc())) {
            ToastUtils.show(this, "请输入问题描述");
            return false;
        }

        if (TextUtils.isEmpty(mEventPara.getLevel())) {
            ToastUtils.show(this, "请选择案件等级");
            return false;
        }

        if (isUpload) {

            if (!mediaUploadGridView.CanUpload()) {
                ToastUtils.show(this, "请添加附件");
                return false;
            }
            if (source == 5 && !mediaUploadGridView_later.CanUpload()) {
                ToastUtils.show(this, "请添加处理后的附件");
                return false;
            }
        }else {
            //后台设置不必上传图片时
            if (!mediaUploadGridView.isEmpty() && !mediaUploadGridView.CanUpload()){
                ToastUtils.show(this, "请添加附件");
                return false;
            }
            if (!mediaUploadGridView_later.isEmpty() && !mediaUploadGridView_later.CanUpload()){
                ToastUtils.show(this, "请添加附件");
                return false;
            }
        }
        return true;
    }

    private void loadAttaches() {

        //获取附件信息
        attaches = getAttaches(mediaUploadGridView.getCanUploadAttach(), 0);
        mEventPara.setAttaches(attaches);

        if (source == 5) {
            mEventPara.setLaterAttaches(getAttaches(mediaUploadGridView_later.getCanUploadAttach(), 0));
        }
    }


    private void addToEntity () throws IllegalAccessException {

            if (eventProcs == null) {
                throw new IllegalAccessException("获取案件流程失败");
            }

            String procId = null;
            if (source == 4) {//一般上报
                for (EventProc eventProc : eventProcs) {
                    if (eventProc.getKey().equals("process")) {
                        procId = eventProc.getId();
                        break;
                    }
                }
            } else if (source == 5) {//快速流程
                for (EventProc eventProc : eventProcs) {
                    if (eventProc.getKey().equals("fast_process")) {
                        procId = eventProc.getId();
                        break;
                    }
                }
            }
            if (procId == null) {
                throw new IllegalAccessException("获取案件流程失败");
            }

            mEventPara.setProcId(procId);
            mEventPara.setUserName(Environments.userBase.getUsername());
            mEventPara.setMobile(UserSession.getMobile(EventNewActivity.this));

            mEventPara.setBidType(rb_jb.isChecked()?"0":"1");

            loadAttaches();


    }


    private void reportEvent(String isChangeCaseUrl) {

        try {

            addToEntity();

//            if (Environments.userBase != null) {
//                mEventPara.setAreaCode(Environments.userBase.getDefaultDepartment().getAreaCode1());
//                mEventPara.setWorkGridId(Environments.userBase.getDefaultDepartment().getWorkGridId());
//                mEventPara.setGridId(Environments.userBase.getDefaultDepartment().getGridId());
//                mEventPara.setManageGridId(Environments.userBase.getDefaultDepartment().getManageGridId());
//                //ToastUtils.show(this,mEventPara.getManageGridId());
//                LogUtils.d("网格"+mEventPara.getAreaCode());
//            }

            Intent intent = new Intent(EventNewActivity.this, CaseReportService.class);
            intent.putExtra("EventPara", mEventPara);
            intent.putExtra("cachedId", cachedId);
            intent.putExtra("changeUrl", isChangeCaseUrl);
            startService(intent);
            finish();

        } catch (Exception e) {
            ToastUtils.show(EventNewActivity.this, e.getMessage());
            e.printStackTrace();
        }

    }

//    private void showFailureDialog() {
//        String message = "案件上报失败，是否保存此案件？";
//        DialogUtils.askYesNo(EventNewActivity.this, null, message, new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                try {
//                    loadAttaches();
//                    if (cachedId == null) {
//                        cachedId = UUID.randomUUID().toString();
//                    }
//                    PrimarySqliteHelper sqliteHelper = PrimarySqliteHelper.getInstance(EventNewActivity.this);
//                    sqliteHelper.getCacheEventDao().createOrUpdate(new CacheEvent(cachedId, new Date(), mEventPara, 0));
//                    ToastUtils.show(EventNewActivity.this, "案件保存成功");
//                    setResult(Activity.RESULT_OK);
//                    finish();
//                } catch (Exception e) {
//
//                }
//
//            }
//        });
//    }

    @Override
    public void onBackPressed() {
        loadAttaches();
        if (mEventPara.equals(copiedEventPara)) {
            super.onBackPressed();
        } else {
            String message = "是否放弃已经编辑的信息？";
            DialogUtils.askYesNo(EventNewActivity.this, "提示", message, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    EventNewActivity.super.onBackPressed();
                }
            });
        }
    }


    @NonNull
    private ArrayList<Attach> getAttaches(List<MediaItem> mediaItems, int type) {
        ArrayList<Attach> attaches = new ArrayList<>();
        obtainAttaches(mediaItems, attaches, type);
        return attaches;
    }

    private void obtainAttaches(List<MediaItem> mediaItems, ArrayList<Attach> attaches, int type) {

        mediaUploadGridView.getAttach(mediaItems,attaches,type);

    }


    public boolean check() {
        if (!TextUtils.isEmpty(mEventPara.getTypeId()) || !TextUtils.isEmpty(mEventPara.getStandardId()) ||
                (mEventPara.getGeoX() > 0.0 && mEventPara.getGeoY() > 0.0)
                || !TextUtils.isEmpty(mEventPara.getDesc()) ||
                !TextUtils.isEmpty(mv_level.getValue()) || !TextUtils.isEmpty(mEventPara.getAddress())) {
            return true;
        }
        if (isUpload) {
            if (mediaUploadGridView.CanUpload()) {
                return true;
            }
            if (source == 5 && mediaUploadGridView_later.CanUpload()) {
                return true;
            }
        }
        return false;
    }

    public void getEventProc() {
        QuestionService questionService = ServiceGenerator.create(QuestionService.class);
        Call<Response<List<EventProc>>> call = questionService.getEventProc_bg("1");
        call.enqueue(new Callback<Response<List<EventProc>>>() {

            @Override
            public void onResponse(Call<Response<List<EventProc>>> call, retrofit2.Response<Response<List<EventProc>>> response) {
                if (response.isSuccessful()) {

                    if (response.body().getData()!=null){
                        eventProcs = response.body().getData();
                    }else {
                        Toast.makeText(EventNewActivity.this,"案件流程获取失败"+response.code(), Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<Response<List<EventProc>>> call, Throwable t) {
                Toast.makeText(EventNewActivity.this,"案件流程获取失败", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
