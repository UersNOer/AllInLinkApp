package com.example.android_supervisor.ui;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AlertDialog;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.example.android_supervisor.R;
import com.example.android_supervisor.common.ResultCallback;
import com.example.android_supervisor.common.UserSession;
import com.example.android_supervisor.entities.Attach;
import com.example.android_supervisor.entities.CensusEventPara;
import com.example.android_supervisor.entities.EventProc;
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
import com.example.android_supervisor.ui.media.MediaUploadGridView;
import com.example.android_supervisor.ui.view.ProgressText;
import com.example.android_supervisor.utils.DialogUtils;
import com.example.android_supervisor.utils.Environments;
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
import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Function;

/**
 * @author 专项普查上报
 */
public class CensusEventNewActivity extends BaseActivity {
    @BindView(R.id.tv_event_new_task_code)
    TextView tvTaskCode;

    @BindView(R.id.tv_event_new_task_name)
    TextView tvTaskName;

    @BindView(R.id.tv_event_new_type)
    TextView tvType;

    @BindView(R.id.tv_event_new_standard)
    TextView tvStandard;

    @BindView(R.id.tv_event_new_position)
    TextView tvPos;

    @BindView(R.id.et_event_new_desc)
    EditText etDesc;

    @BindView(R.id.tv_event_lng)
    TextView tv_event_lng;

    @BindView(R.id.btn_event_new_desc_record)
    FloatingActionButton btnDescRecord;

    @BindView(R.id.tv_event_new_task_area)
    TextView tvTaskArea;

    @BindView(R.id.tv_character_size)
    TextView tv_character_size;


    @BindView(R.id.gv_event_new_attaches)
    MediaUploadGridView gvAttaches;

    private CensusEventPara mEventPara;
    private CensusEventPara copiedEventPara;
    private VoiceRecognizeDialog voiceRecognizeDialog;

    String[] taskClasss;
    private String temp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_census_event_new);
        ButterKnife.bind(this);
        initialize();

        voiceRecognizeDialog = new VoiceRecognizeDialog(this);
        voiceRecognizeDialog.setCallback(new ResultCallback<String>() {
            @Override
            public void onResult(String result, int tag) {
                if (!TextUtils.isEmpty(result)) {
                    StringBuffer sb = new StringBuffer(etDesc.getText().toString().trim()).append(result);
                    etDesc.setText(sb.toString());
                    etDesc.setSelection(sb.toString().length());
                }
            }

            @Override
            public void onResult(String text) {
            }
        });
        TextChangeUtils textChangeUtils = new TextChangeUtils(etDesc, tv_character_size, this);
    }

    @SuppressLint("RestrictedApi")
    private void initialize() {
        String taskId = getIntent().getStringExtra("taskId");
        String taskCode = getIntent().getStringExtra("taskCode");
        String taskName = getIntent().getStringExtra("taskName");
        String taskArea = getIntent().getStringExtra("taskArea");

        String taskClass = getIntent().getStringExtra("TaskClass");
        if (!TextUtils.isEmpty(taskClass)) {
            taskClasss = taskClass.split(",");
        }

        tvTaskCode.setText(taskCode);
        tvTaskName.setText(taskName);
        tvTaskArea.setText(taskArea);

        mEventPara = new CensusEventPara();
        mEventPara.setTaskId(taskId);
        copiedEventPara = mEventPara.clone();

//        requestCurrentLocation();

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
//                    ToastUtils.show(CensusEventNewActivity.this, "获取区域信息失败");
//                    return;
//                }
//
////                FaceSqliteHelper sqliteHelper = FaceSqliteHelper.getInstance(CensusEventNewActivity.this);
////                List<Area> areas = sqliteHelper.getAreaDao().queryForEq("code", areaCode);
////                if (Environments.userBase.getDefaultDepartments().areaCode!=null &&
////                        !Environments.userBase.getDefaultDepartments().areaCode.equals(areaCode)){
////                    ToastUtils.show(CensusEventNewActivity.this, "已超出管理区域，请重新选择位置");
////                    return;
////                }
//
//
//                mEventPara.setGeoX(location.getLongitude());
//                mEventPara.setGeoY(location.getLatitude());
//                mEventPara.setAddress(location.getAddress());
//                mEventPara.setAreaCode(areaCode);
//
//                tvPos.setText(location.getAddress());
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
            ToastUtils.show(this, "请选择任务类别");
            return;
        }

        PublicSqliteHelper sqliteHelper = PublicSqliteHelper.getInstance(this);
        final List<EventType> data = sqliteHelper.getEventTypeDao().queryForEq("pid", evtTypeId);
        final int dataSize = data.size();
        if (dataSize == 0) {
            ToastUtils.show(this, "当前选择的任务类别没有立案标准");
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

    @OnClick(R.id.tv_event_new_position)
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
                mEventPara.setAreaCode(result.getStringExtra("areaCode"));
                mEventPara.setAddress(result.getStringExtra("address"));
                tvPos.setText(mEventPara.getAddress());
                tv_event_lng.setText(mEventPara.getGeoX() + "," + mEventPara.getGeoY());
            }
        });

    }

    @OnTextChanged(R.id.et_event_new_desc)
    public void onDescChanged(CharSequence s, int start, int before, int count) {
        String desc = s.toString();
        if (TextUtils.isEmpty(desc)) {
//            btnDescRecord.show();
            mEventPara.setDesc(null);
        } else {
//            btnDescRecord.hide();
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
            try{
                String typeId = data.getStringExtra("typeId");
                String typeName = data.getStringExtra("typeName");
                String standardId = data.getStringExtra("standardId");
                String standardName = data.getStringExtra("standardName");

                String bigId = data.getStringExtra("bigId");
                String bigName = data.getStringExtra("bigName");
                String smallId = data.getStringExtra("smallId");
                String smallName = data.getStringExtra("smallName");

                if (!TextUtils.isEmpty(smallId)) {
                    mEventPara.setTypeId(smallId);
                    mEventPara.setTypeName(smallName);
                    tvType.setText(typeName +"-"+bigName+"-"+smallName);
                }
                if (!TextUtils.isEmpty(standardId)) {
                    mEventPara.setStandardId(standardId);
                    mEventPara.setStandardName(standardName);
                    tvStandard.setText(standardName);
                }
            }catch (Exception e){
                e.printStackTrace();
            }


        }
    }

    @OnClick(R.id.btn_event_new_submit)
    public void onSubmit(View v) {
        if (!validateParas()) {
            return;
        }
        SystemUtils.hideSoftInput(this, v);
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
        if (mEventPara.getGeoX() <= 0 || mEventPara.getGeoY() <= 0) {
            ToastUtils.show(this, "请选择事发位置");
            return false;
        }
        if (TextUtils.isEmpty(mEventPara.getDesc())) {
            ToastUtils.show(this, "请输入问题描述");
            return false;
        }
        if (gvAttaches.isEmpty() || !gvAttaches.CanUpload()) {
            ToastUtils.show(this, "请添加附件");
            return false;
        }

        return true;
    }

    private void loadAttaches() {
        ArrayList<Attach> attaches = new ArrayList<>();
        gvAttaches.getAttach(gvAttaches.getCanUploadAttach(),attaches,0);
        mEventPara.setAttaches(attaches);

    }

    private void reportEvent() {
        QuestionService questionService = ServiceGenerator.create(QuestionService.class);
        Observable<Response<List<EventProc>>> observable = questionService.getEventProc("1");
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
                            if (eventProc.getKey().equals("special_census")) {
                                procId = eventProc.getId();
                                break;
                            }
                        }
                        if (procId == null) {
                            throw new IllegalAccessException("获取案件流程失败");
                        }
                        mEventPara.setProcId(procId);
                        mEventPara.setUserName(UserSession.getUserName(CensusEventNewActivity.this));
                        mEventPara.setMobile(UserSession.getMobile(CensusEventNewActivity.this));
                        if (Environments.userBase != null) {
                            mEventPara.setAreaCode(Environments.userBase.getDefaultDepartment().getAreaCode());
                        }
                        loadAttaches();
                        QuestionService questionService = ServiceGenerator.create(QuestionService.class);
                        return questionService.addCensusEvent(new JsonBody(mEventPara));
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .compose(new ProgressTransformer<Response<String>>(this, ProgressText.submit))
                .subscribe(new ResponseObserver<String>(this) {
                    @Override
                    public void onSuccess(String title) {
                        String message = "案件\"" + title + "\"上报成功";
                        DialogUtils.show(CensusEventNewActivity.this, message, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                setResult(Activity.RESULT_OK);
                                finish();
                            }
                        });
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
            DialogUtils.askYesNo(CensusEventNewActivity.this, "提示", message, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    CensusEventNewActivity.super.onBackPressed();
                }
            });
        }
    }
}
