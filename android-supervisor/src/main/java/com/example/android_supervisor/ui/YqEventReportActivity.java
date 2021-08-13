package com.example.android_supervisor.ui;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.android_supervisor.Presenter.YqReportPresenter;
import com.example.android_supervisor.Presenter.YqTypePresenter;
import com.example.android_supervisor.R;
import com.example.android_supervisor.common.ResultCallback;
import com.example.android_supervisor.common.UserSession;
import com.example.android_supervisor.entities.Attach;
import com.example.android_supervisor.entities.ChooseData;
import com.example.android_supervisor.entities.MapBundle;
import com.example.android_supervisor.map.AMapManager;
import com.example.android_supervisor.ui.dialog.VoiceRecognizeDialog;
import com.example.android_supervisor.ui.media.MediaItem;
import com.example.android_supervisor.ui.media.MediaUploadGridView;
import com.example.android_supervisor.ui.model.CaseClassTreeVO;
import com.example.android_supervisor.ui.model.EsEventVO;
import com.example.android_supervisor.ui.view.MenuItemView;
import com.example.android_supervisor.ui.view.OptionalTextView;
import com.example.android_supervisor.utils.DialogUtils;
import com.example.android_supervisor.utils.LogUtils;
import com.example.android_supervisor.utils.SystemUtils;
import com.example.android_supervisor.utils.TextChangeUtils;
import com.example.android_supervisor.utils.ToastUtils;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnTextChanged;


/**
 * @author yj
 */
public class YqEventReportActivity extends BaseActivity {

//    @BindView(R.id.mv_case_type)
//    MenuItemView mv_case_type;

//    @BindView(R.id.mv_case_big)
//    MenuItemView mv_case_big;
//

    @BindView(R.id.mv_case_type)
    MenuItemView mv_case_type;

    @BindView(R.id.ll_cs_name)
    LinearLayout ll_cs_name;

    @BindView(R.id.ll_cs_lxr)
    LinearLayout tv_lxr;

    @BindView(R.id.ll_cs_lxfs)
    LinearLayout ll_cs_lxfs;


    @BindView(R.id.et_event_new_position)
    EditText etPos;

    @BindView(R.id.et_event_cs_name)
    EditText et_event_cs_name;


    @BindView(R.id.et_event_lxr)
    EditText et_event_lxr;

    @BindView(R.id.et_event_lxfs)
    EditText et_event_lxfs;


    @BindView(R.id.tv_showCoordinate)//坐标位置
     TextView tv_showCoordinate;

    @BindView(R.id.et_event_new_desc)
    EditText etDesc;

    @BindView(R.id.tv_hint_pz)
    TextView tv_hint_pz;


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


    View btnSave;

    private String cachedId;
    private EsEventVO mEventPara;
    private EsEventVO copiedEventPara;
    private VoiceRecognizeDialog voiceRecognizeDialog;
    private boolean isUpload = true;//是否上传图片（默认：是）

    ArrayList<Attach> attaches;

    private int source = 4;

    //List<EventProc> eventProcs = null;//案件流程数据
    EsEventVO.WhProperties  whProperties = new EsEventVO.WhProperties();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_report_yq);
        ButterKnife.bind(this);
       // getEventProc();//获取案件流程

        initReportType();
       // addSaveMenu();
        initialize();
       // loadCachedEvent();
        updateAttachUi();

    }



    private void updateAttachUi() {
        if (mEventPara.getSource() == CaseSourceConstant.YQCS ) {//普通上报
            setTitle("疫情场所上报");
            mediaUploadGridView_later.setVisibility(View.GONE);
            tv_attahc_hint.setText("场所图片");
            tv_laterAttahc_hint.setVisibility(View.GONE);
            mediaUploadGridView.setDrawableHide();
        } else if (mEventPara.getSource() ==  CaseSourceConstant.YQSJ ) {
            setTitle("疫情事件上报");
            mediaUploadGridView_later.setVisibility(View.VISIBLE);
            tv_attahc_hint.setVisibility(View.VISIBLE);
            tv_laterAttahc_hint.setVisibility(View.VISIBLE);

            ll_cs_name.setVisibility(View.GONE);
            tv_lxr.setVisibility(View.GONE);
            ll_cs_lxfs.setVisibility(View.GONE);

            mv_case_type.setLetfTitle("事件类别");
            tv_hint_pz.setText("事发位置");
        }

    }



    private void initReportType() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            source = bundle.getInt("source");
        }


        YqTypePresenter presenter = new YqTypePresenter();
        presenter.getType(YqEventReportActivity.this, new YqTypePresenter.CallBack() {
            @Override
            public void onSuccess(List<CaseClassTreeVO> types) {
                if (types!=null){
                    ArrayList<ChooseData> chooseDatas = new ArrayList<>();

                    for (CaseClassTreeVO type:types){
                        //场所
                        if (source  == CaseSourceConstant.YQCS){
                            if (type.getQuestionClassId().equals("0101")){
                                ChooseData chooseData = new ChooseData(String.valueOf(type.getId()),type.getNodeName(),type.getWhType());
                                chooseDatas.add(chooseData);
                            }
                        }else {
                             if(type.getQuestionClassId().equals("0102")){
                                //事件
                                ChooseData chooseData = new ChooseData(String.valueOf(type.getId()),type.getNodeName(),type.getWhType());
                                chooseDatas.add(chooseData);
                            }
                        }
                    }

                    mv_case_type.setSelectListener(new OptionalTextView.SelectListener() {
                        @Override
                        public void selected(int which, ChooseData chooseData) {
                            if (source  == CaseSourceConstant.YQSJ){
                                mv_case_type.setValue("事件/其他/"+chooseData.getName());
                            }else {
                                mv_case_type.setValue("部件/其他/"+chooseData.getName());
                            }

                            mEventPara.setSmallClassName(chooseData.getName());
                            mEventPara.setSmallClassId(Long.valueOf(chooseData.getId()));
                            mEventPara.setTypeName(chooseData.getName());
                            mEventPara.setTypeId(Long.parseLong(chooseData.id));

                            whProperties.bgcode = chooseData.id;
//                            whProperties.objname = chooseData.getName();
                            whProperties.type  = chooseData.getWhType();
                            whProperties.typeName = chooseData.getName();
                        }
                    });
                    mv_case_type.setChooseData(chooseDatas);
                    mv_case_type.setDefault();

//                        mv_case_type.setAdapter(
//                                new ArrayAdapter<YqTypePresenter.YqType>(YqEventReportActivity.this,
//                                        android.R.layout.simple_list_item_1,types));
                }
            }
        });


    }



//    private void addSaveMenu() {
//        btnSave = addMenu("暂存", new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                try {
//                    if (cachedId == null) {
//                        cachedId = UUID.randomUUID().toString();
//                    }
//                    //至少保存一项才能提交
//                    if (!check()) {
//                        ToastUtils.show(YqEventReportActivity.this, "请填写至少一项");
//                        return;
//                    }
//                    loadAttaches();
//                    //暂存不需要完全保存数据
//                    PrimarySqliteHelper sqliteHelper = PrimarySqliteHelper.getInstance(YqEventReportActivity.this);
//                    sqliteHelper.getCacheEventDao().createOrUpdate(new CacheEvent(cachedId, new Date(), mEventPara, source));
//                    ToastUtils.show(YqEventReportActivity.this, "案件保存成功");
//
//
//                    setResult(Activity.RESULT_OK);
//                    finish();
//
//                } catch (Exception e) {
//                    e.printStackTrace();
//                    ToastUtils.show(YqEventReportActivity.this, "案件保存失败:" + e.getMessage());
//                }
//
//
//            }
//        });
//    }

//    private void loadCachedEvent() {
////        cachedId = getIntent().getStringExtra("cachedId");
////        if (cachedId != null) {
////            PrimarySqliteHelper sqliteHelper = PrimarySqliteHelper.getInstance(YqEventReportActivity.this);
////            CacheEvent cacheEvent = sqliteHelper.getCacheEventDao().queryForId(cachedId);
////            if (cacheEvent != null) {
////                mEventPara = cacheEvent.getData();
////                if (mEventPara != null) {
////                    updateUI();
////                }
////            }
////        } else {
////            EventPara eventPara = (EventPara) getIntent().getSerializableExtra("eventPara");
////            if (eventPara != null) {
////                mEventPara = eventPara;
////                if (mEventPara != null) {
////                    updateUI();
////                }
////            }
////        }
////    }

    private void initialize() {
        if (mEventPara == null) {
            mEventPara = new EsEventVO();
//            copiedEventPara = mEventPara.clone();
            //requestCurrentLocation();
        } else {
            //copiedEventPara = mEventPara.clone();
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
//    private void updateUI() {
//        source = mEventPara.getSource();
//
//        mv_case_small.setValue(mEventPara.getTypeName());
//
//
//        if (mEventPara.getTypeId() != null) {
//            EventType eventType_small = PublicSqliteHelper.getInstance(this).getEventTypeDao().queryForId(mEventPara.getTypeId());
//            if (eventType_small != null) {
//                EventType eventType_big = CaseBindUtils.bindCaseUIById(YqEventReportActivity.this, eventType_small.getPid(), mv_case_big);
//                if (eventType_big != null) {
//                    EventType eventType_type = CaseBindUtils.bindCaseUIById(YqEventReportActivity.this, eventType_big.getPid(), mv_case_type);
//                    if (eventType_type!=null && (eventType_type.getName().contains("绿化") || eventType_type.getName().contains("环卫"))){
//                        ll_bd.setVisibility(View.VISIBLE);
//                        rb_qb.setChecked("0".equals(mEventPara.getBidType())?true:false);
//                        rb_jb.setChecked("1".equals(mEventPara.getBidType())?true:false);
//                    }
//                }
//            }
//        }
//        CaseBindUtils.bindLevleUIById(YqEventReportActivity.this, String.valueOf(mEventPara.getLevel()), mv_level);
//        etPos.setText(mEventPara.getAddress());
//        etDesc.setText(mEventPara.getDesc());
//        tv_showCoordinate.setText(mEventPara.getGeoX() + "," + mEventPara.getGeoY());
//
//        mediaUploadGridView.setVisibility(isUpload ? View.VISIBLE : View.GONE);
//        mediaUploadGridView_later.setVisibility(isUpload && source == 83 ? View.VISIBLE : View.GONE);
//
//        updateAttachData(mEventPara.getAttaches(), mediaUploadGridView);
//        if (source == 83) updateAttachData(mEventPara.getLaterAttaches(), mediaUploadGridView_later);
//    }

//    private void updateAttachData(List<Attach> attaches, MediaUploadGridView mediaUploadGridView) {
//        if (attaches == null || attaches.size() <= 0) {
//            return;
//        }
//        List<MediaItem> mediaItems = new ArrayList<>();
//
//        for (Attach attach : attaches) {
//            MediaItem mediaItem = new MediaItem();
//            mediaItem.setType(attach.getType());
//            mediaItem.setProgress(100);
//            if (!TextUtils.isEmpty(attach.getUrl())) {
//                String path = FileServerUtils.getPath(this, attach.getUrl());
//                mediaItem.setUri(Uri.parse(path));
//            }
//            if (!TextUtils.isEmpty(attach.getThumbUrl())) {
//                String path = FileServerUtils.getUrl(this, attach.getThumbUrl());
//                mediaItem.setThumbnailUri(Uri.parse(path));
//            }
//            mediaItems.add(mediaItem);
//        }
//        mediaUploadGridView.setAttach(mediaItems);
//    }

//    @OnClick(R.id.mv_case_type)
//    public void onEventType(View v) {
//        Intent intent = new Intent(this, EventTypeActivity.class);
//        intent.putExtra("typeId", mEventPara.getTypeId());
//        intent.putExtra("standardId", mEventPara.getStandardId());
//        startActivityForResult(intent, 1);
//        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
//    }


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
                mEventPara.setEsPosition(result.getStringExtra("address"));
                etPos.setText(mEventPara.getEsPosition());
                tv_showCoordinate.setText(mEventPara.getGeoX() + "," + mEventPara.getGeoY());

                mEventPara.setAreaName(result.getStringExtra("areaName"));
                mEventPara.setAreaCode(result.getStringExtra("areaCode"));
//                mEventPara.setWorkGridId(result.getStringExtra("workGridId"));
//                mEventPara.setGridId(result.getStringExtra("gridId"));
//                mEventPara.setManageGridId(result.getStringExtra("manageGridId"));
//                ToastUtils.show(this,mEventPara.getManageGridId());
                LogUtils.d("网格"+mEventPara.getAreaCode());


                EsEventVO.Geometry geometry = new EsEventVO.Geometry();
                geometry.coordinates = new double[]{mEventPara.getGeoX(),mEventPara.getGeoY()};
                mEventPara.setGeometry(geometry);

                whProperties.objpos = mEventPara.getEsPosition();
                whProperties.areaCode = mEventPara.getAreaCode();
                whProperties.areaName = mEventPara.getAreaName();
                whProperties.bgcode =result.getStringExtra("gridId");//单元网格

            }
        });
    }

    private String temp;

    //位置
    @OnTextChanged(R.id.et_event_new_desc)
    public void onDescChanged(CharSequence s, int start, int before, int count) {
        String desc = s.toString().trim();
        if (TextUtils.isEmpty(desc)) {
            btnDescRecord.show();
            mEventPara.setEsDesc(null);
            whProperties.remake=null;
        } else {
            //  btnDescRecord.hide();
            mEventPara.setEsDesc(desc);
            whProperties.remake=desc;
        }
    }

    @OnTextChanged(R.id.et_event_new_position)
    public void onMapPosChanged(CharSequence s, int start, int before, int count) {
        String desc = s.toString().trim();
        if (TextUtils.isEmpty(desc)) {
            iv_recordMap.show();
            mEventPara.setEsPosition(null);
            whProperties.objpos = null;
        } else {
            //  btnDescRecord.hide();
            mEventPara.setEsPosition(desc);
            whProperties.objpos = desc;
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
    }

//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if (resultCode == RESULT_OK && requestCode == 1) {
//            String typeId = data.getStringExtra("typeId");
//            String typeName = data.getStringExtra("typeName");
//
//            String bigId = data.getStringExtra("bigId");
//            String bigName = data.getStringExtra("bigName");
//            String smallId = data.getStringExtra("smallId");
//            String smallName = data.getStringExtra("smallName");
//
//            String standardId = data.getStringExtra("standardId");
//            String standardName = data.getStringExtra("standardName");
//
//            if (!TextUtils.isEmpty(typeId)) {
//
//                mv_case_type.setValue(typeName);
//            }
//            //加入大类 和小类
//            if (!TextUtils.isEmpty(bigId)) {
//
//                mv_case_big.setValue(bigName);
//            }
//            if (!TextUtils.isEmpty(smallId)) {
//
//                mEventPara.setTypeId(smallId);
//                mEventPara.setTypeName(smallName);
//
//                mv_case_small.setValue(smallName);
//            }
//
//            if (!TextUtils.isEmpty(standardId)) {
//                mEventPara.setStandardId(standardId);
//                mEventPara.setStandardName(standardName);
//            }
//
//            ll_bd.setVisibility(mv_case_type.getValue().contains("绿化") ||mv_case_type.getValue().contains("环卫")?View.VISIBLE:View.GONE);
//        }
//    }

    @OnClick(R.id.btn_event_new_submit)
    public void onSubmit(View v) {
        SystemUtils.hideSoftInput(this, v);
        if (!validateParas()) {
            return;
        }

        loadAttaches();
        mEventPara.setEsDesc(etDesc.getText().toString().trim());
        mEventPara.setCreateId(UserSession.getUserId(this));
        mEventPara.setCreateName(UserSession.getUserName(this));

        // 场所上报
        whProperties.dirName = et_event_lxr.getText().toString().trim();
        whProperties.dirPhone = et_event_lxfs.getText().toString().trim();
        whProperties.objpos= etPos.getText().toString().trim();
        whProperties.remake = etDesc.getText().toString().trim();
        whProperties.objname = et_event_cs_name.getText().toString().trim();

        whProperties.createId=UserSession.getUserId(this);
        mEventPara.setWhProperties(whProperties);//最后装填


        DialogUtils.askYesNo(this, "提示", "您确定提交？", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                YqReportPresenter presenter = new YqReportPresenter();

                presenter.report(YqEventReportActivity.this, mEventPara, new YqReportPresenter.CallBack() {

                    @Override
                    public void onSuccess() {
                        ToastUtils.show(YqEventReportActivity.this,"疫情上报成功");
                        finish();

                    }

                    @Override
                    public void onFailure() {
                        ToastUtils.show(YqEventReportActivity.this,"疫情上报失败");
                    }
                });
            }
        });
    }

    int count;

    private boolean validateParas() {

        if (source ==CaseSourceConstant.YQSJ){

            if (TextUtils.isEmpty(mEventPara.getSmallClassName())) {
                ToastUtils.show(this, "请选择疫情类别");
                return false;
            }

            if (mEventPara.getGeoX() <= 0 || mEventPara.getGeoY() <= 0 || TextUtils.isEmpty(mEventPara.getEsPosition())) {
                ToastUtils.show(this, "请选择位置");
                return false;
            }

            if (TextUtils.isEmpty(mEventPara.getAreaCode())){
                ToastUtils.show(this, "请选择正确位置");
                return false;
            }


        }

        else if (source ==CaseSourceConstant.YQCS ){

//            if (TextUtils.isEmpty(whProperties.objname)){
//                ToastUtils.show(this, "请选择类别");
//                return false;
//            }

            if (mEventPara.getGeometry()==null || mEventPara.getGeometry().coordinates==null  || TextUtils.isEmpty(whProperties.objpos)) {
                ToastUtils.show(this, "请选择位置");
                return false;
            }




            if (TextUtils.isEmpty(et_event_cs_name.getText().toString().trim())) {
                ToastUtils.show(this, "场所名称");
                return false;
            }

            if (TextUtils.isEmpty(whProperties.areaCode)){
                ToastUtils.show(this, "请选择正确位置");
                return false;
            }



        }



        if (isUpload) {


            //                if (!mediaUploadGridView.CanUpload()) {
//                    ToastUtils.show(this, "请添加附件");
//                    return false;
//                }


            if (source == 83 && !mediaUploadGridView_later.CanUpload()&&!mediaUploadGridView.CanUpload()) {

//                if (!mediaUploadGridView.CanUpload()) {
//                    ToastUtils.show(this, "请添加附件");
//                    return false;
//                }
                ToastUtils.show(this, "请添加附件");
  //              ToastUtils.show(this, "请添加处理后的附件");
                return false;
            }}

            if (!mediaUploadGridView.isEmpty() && !mediaUploadGridView.CanUpload()){
                ToastUtils.show(this, "附件尚未上传完成");
                return false;
            }
            if (!mediaUploadGridView_later.isEmpty() && !mediaUploadGridView_later.CanUpload()){
                ToastUtils.show(this, "附件尚未上传完成");
                return false;
            }

//        }else {
//            //后台设置不必上传图片时
//            if (!mediaUploadGridView.isEmpty() && !mediaUploadGridView.CanUpload()){
//                ToastUtils.show(this, "请添加附件");
//                return false;
//            }
//            if (!mediaUploadGridView_later.isEmpty() && !mediaUploadGridView_later.CanUpload()){
//                ToastUtils.show(this, "请添加附件");
//                return false;
//            }
//        }
        return true;
    }

    private void loadAttaches() {

        //获取附件信息
        attaches = getAttaches(mediaUploadGridView.getCanUploadAttach(), 0);


        if (source == CaseSourceConstant.YQCS){

            if (attaches!=null){
                ArrayList<EsEventVO.WhProperties.ImgPath> imgPaths  = new ArrayList<>();
                for (Attach attach:attaches){
                    EsEventVO.WhProperties.ImgPath  imgPath= new EsEventVO.WhProperties.ImgPath();
                    imgPath.filePath  = attach.getUrl();
                    imgPaths.add(imgPath);
                }
                whProperties.imgPathList = imgPaths;
            }
        }

        if (source == CaseSourceConstant.YQSJ) {

            if (attaches!=null){
                for (Attach attach:attaches){
                    attach.setFilePath(attach.getUrl());
                    attach.setType(attach.getType());
                }
                mEventPara.setBeforeImgUrls(attaches);
            }


            ArrayList<Attach> attaches =getAttaches(mediaUploadGridView_later.getCanUploadAttach(), 0);
            if (attaches!=null){
                for (Attach attach:attaches){
                    attach.setFilePath(attach.getUrl());
                    attach.setType(attach.getType());
                }
                mEventPara.setAfterImgUrls(attaches);
            }



        }
    }

//    private void reportEvent(String isChangeCaseUrl) {
//
//        try {
//            if (eventProcs == null) {
//                throw new IllegalAccessException("获取案件流程失败");
//            }
//
//            String procId = null;
//            if (source == 4) {//一般上报
//                for (EventProc eventProc : eventProcs) {
//                    if (eventProc.getKey().equals("process")) {
//                        procId = eventProc.getId();
//                        break;
//                    }
//                }
//            } else if (source == 5) {//快速流程
//                for (EventProc eventProc : eventProcs) {
//                    if (eventProc.getKey().equals("fast_process")) {
//                        procId = eventProc.getId();
//                        break;
//                    }
//                }
//            }
//            if (procId == null) {
//                throw new IllegalAccessException("获取案件流程失败");
//            }
//
//            mEventPara.setProcId(procId);
//            mEventPara.setUserName(UserSession.getUserName(YqEventReportActivity.this));
//            mEventPara.setMobile(UserSession.getMobile(YqEventReportActivity.this));
//
//            mEventPara.setBidType(rb_jb.isChecked()?"0":"1");
//
////            if (Environments.userBase != null) {
////                mEventPara.setAreaCode(Environments.userBase.getDefaultDepartment().getAreaCode1());
////                mEventPara.setWorkGridId(Environments.userBase.getDefaultDepartment().getWorkGridId());
////                mEventPara.setGridId(Environments.userBase.getDefaultDepartment().getGridId());
////                mEventPara.setManageGridId(Environments.userBase.getDefaultDepartment().getManageGridId());
////                //ToastUtils.show(this,mEventPara.getManageGridId());
////                LogUtils.d("网格"+mEventPara.getAreaCode());
////            }
//            loadAttaches();
//            Intent intent = new Intent(YqEventReportActivity.this, CaseReportService.class);
//            intent.putExtra("EventPara", mEventPara);
//            intent.putExtra("cachedId", cachedId);
//            intent.putExtra("changeUrl", isChangeCaseUrl);
//            startService(intent);
//            finish();
//
//        } catch (IllegalAccessException e) {
//            ToastUtils.show(YqEventReportActivity.this, e.getMessage());
//            e.printStackTrace();
//        }


//        QuestionService questionService = ServiceGenerator.create(QuestionService.class);
//        questionService.getEventProc("1")
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new Consumer<Response<List<EventProc>>>() {
//                    @Override
//                    public void accept(Response<List<EventProc>> response) throws Exception {
//                        try {
//                            if (!response.isSuccess()) {
//
//                                throw new IllegalAccessException("获取案件流程失败");
//
//                            }
//                            if (response.getData() == null) {
//                                throw new IllegalAccessException("获取案件流程失败");
//                            }
//
//                            String procId = null;
//                            if (source == 4) {//一般上报
//                                for (EventProc eventProc : response.getData()) {
//                                    if (eventProc.getKey().equals("process")) {
//                                        procId = eventProc.getId();
//                                        break;
//                                    }
//                                }
//                            } else if (source == 5) {//快速流程
//                                for (EventProc eventProc : response.getData()) {
//                                    if (eventProc.getKey().equals("fast_process")) {
//                                        procId = eventProc.getId();
//                                        break;
//                                    }
//                                }
//                            }
//                            if (procId == null) {
//                                throw new IllegalAccessException("获取案件流程失败");
//                            }
//                            mEventPara.setProcId(procId);
//                            mEventPara.setUserName(UserSession.getUserName(EventNewActivity.this));
//                            mEventPara.setMobile(UserSession.getMobile(EventNewActivity.this));
//                            if (Environments.userBase != null) {
//                                mEventPara.setAreaCode(Environments.userBase.getDefaultDepartments().areaCode);
//                            }
//                            loadAttaches();
//
//                            Intent intent = new Intent(EventNewActivity.this, CaseReportService.class);
//                            intent.putExtra("EventPara", mEventPara);
//                            intent.putExtra("cachedId", cachedId);
//                            startService(intent);
//                        } catch (IllegalAccessException e) {
//                            ToastUtils.show(EventNewActivity.this, e.getMessage());
//                            e.printStackTrace();
//                        }
//                    }
//                }, new Consumer<Throwable>() {
//                    @Override
//                    public void accept(Throwable throwable) throws Exception {
//                        Log.e("Envent", "获取失败");
//                    }
//                });
//                .subscribe(new Observer<Response<List<EventProc>>>() {
//                    @Override
//                    public void onSubscribe(@NonNull Disposable d) {
//
//                    }
//
//                    @Override
//                    public void onNext(@NonNull Response<List<EventProc>> response) {
//
//
//                    }
//
//                    @Override
//                    public void onError(@NonNull Throwable e) {
//                        LogUtils.d("envent", e.getMessage());
//
//                    }
//
//                    @Override
//                    public void onComplete() {
//
//                    }
//                });

//        Observable<Response<List<EventProc>>> observable =  questionService.getEventProc("1");
//        observable.compose(this.<Response<List<EventProc>>>bindToLifecycle())
//                .flatMap(new Function<Response<List<EventProc>>, ObservableSource<Response<String>>>() {
//                    @Override
//                    public ObservableSource<Response<String>> apply(Response<List<EventProc>> response) throws Exception {
//                        if (!response.isSuccess()) {
//                            throw new IllegalAccessException("获取案件流程失败");
//                        }
//                        if (response.getData() == null) {
//                            throw new IllegalAccessException("获取案件流程失败");
//                        }
//                        String procId = null;
//                        if (source == 4) {//一般上报
//                            for (EventProc eventProc : response.getData()) {
//                                if (eventProc.getKey().equals("process")) {
//                                    procId = eventProc.getId();
//                                    break;
//                                }
//                            }
//                        } else if (source == 5) {//快速流程
//                            for (EventProc eventProc : response.getData()) {
//                                if (eventProc.getKey().equals("fast_process")) {
//                                    procId = eventProc.getId();
//                                    break;
//                                }
//                            }
//                        }
//
//
//                        if (procId == null) {
//                            throw new IllegalAccessException("获取案件流程失败");
//                        }
//                        mEventPara.setProcId(procId);
//                        mEventPara.setUserName(UserSession.getUserName(EventNewActivity.this));
//                        mEventPara.setMobile(UserSession.getMobile(EventNewActivity.this));
//                        if (Environments.userBase != null) {
//                            mEventPara.setAreaCode(Environments.userBase.getDefaultDepartments().areaCode);
//                        }
//                        loadAttaches();
//
//                        QuestionService questionService = ServiceGenerator.create(QuestionService.class);
//                        return questionService.addEvent(new JsonBody(mEventPara));
//                    }
//                })
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new ResponseObserver<String>(this) {
//                    @Override
//                    public void onSuccess(String title) {
//                        // 上报成功后需要删除暂存案件
//                        if (cachedId != null) {
//                            PrimarySqliteHelper sqliteHelper = PrimarySqliteHelper.getInstance(EventNewActivity.this);
//                            sqliteHelper.getCacheEventDao().deleteById(cachedId);
//                        }
//
//                        String message = "案件\"" + title + "\"上报成功";
//                        DialogUtils.show(EventNewActivity.this, message, new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialog, int which) {
//                                setResult(Activity.RESULT_OK);
//                                finish();
//                            }
//                        });
//                    }
//
//                    @Override
//                    public void onFailure(int code, String errMsg) {
//                        super.onFailure(code, errMsg);
//                        showFailureDialog();
//                    }
//                });
//    }

//    private void showFailureDialog() {
//        String message = "案件上报失败，是否保存此案件？";
//        DialogUtils.askYesNo(YqEventReportActivity.this, null, message, new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                try {
//                    loadAttaches();
//                    if (cachedId == null) {
//                        cachedId = UUID.randomUUID().toString();
//                    }
//                    PrimarySqliteHelper sqliteHelper = PrimarySqliteHelper.getInstance(YqEventReportActivity.this);
//                    sqliteHelper.getCacheEventDao().createOrUpdate(new CacheEvent(cachedId, new Date(), mEventPara, 0));
//                    ToastUtils.show(YqEventReportActivity.this, "案件保存成功");
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
            DialogUtils.askYesNo(YqEventReportActivity.this, "提示", message, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    YqEventReportActivity.super.onBackPressed();
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



//    public void getEventProc() {
//        QuestionService questionService = ServiceGenerator.create(QuestionService.class);
//        Call<Response<List<EventProc>>> call = questionService.getEventProc_bg("1");
//        call.enqueue(new Callback<Response<List<EventProc>>>() {
//
//            @Override
//            public void onResponse(Call<Response<List<EventProc>>> call, retrofit2.Response<Response<List<EventProc>>> response) {
//                if (response.isSuccessful()) {
//
//                    if (response.body().getData()!=null){
//                        eventProcs = response.body().getData();
//                    }else {
//                        Toast.makeText(YqEventReportActivity.this,"案件流程获取失败"+response.code(), Toast.LENGTH_SHORT).show();
//                    }
//                }
//            }
//
//            @Override
//            public void onFailure(Call<Response<List<EventProc>>> call, Throwable t) {
//                Toast.makeText(YqEventReportActivity.this,"案件流程获取失败", Toast.LENGTH_SHORT).show();
//            }
//        });
//    }



}
