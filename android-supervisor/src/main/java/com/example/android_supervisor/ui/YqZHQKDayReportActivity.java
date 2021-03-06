package com.example.android_supervisor.ui;


import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.android_supervisor.ui.model.CaseClassTreeVO;
import com.example.android_supervisor.ui.model.EsPersonHealthVO;
import com.example.android_supervisor.ui.model.YqcsDayPara;
import com.example.android_supervisor.ui.model.YqcsDayRes;
import com.jzxiang.pickerview.TimePickerDialog;
import com.jzxiang.pickerview.data.Type;
import com.jzxiang.pickerview.listener.OnDateSetListener;
import com.example.android_supervisor.Presenter.YqTypePresenter;
import com.example.android_supervisor.R;
import com.example.android_supervisor.common.UserSession;
import com.example.android_supervisor.entities.ChooseData;
import com.example.android_supervisor.http.ServiceGenerator;
import com.example.android_supervisor.http.common.ProgressTransformer;
import com.example.android_supervisor.http.request.JsonBody;
import com.example.android_supervisor.http.response.Response;
import com.example.android_supervisor.http.response.ResponseObserver;
import com.example.android_supervisor.http.service.YqService;
import com.example.android_supervisor.ui.view.MenuItemView;
import com.example.android_supervisor.ui.view.OptionalTextView;
import com.example.android_supervisor.ui.view.ProgressText;
import com.example.android_supervisor.utils.DateUtils;
import com.example.android_supervisor.utils.ToastUtils;
import com.xuexiang.xui.widget.button.switchbutton.SwitchButton;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;


public class YqZHQKDayReportActivity extends BaseActivity {
    EsPersonHealthVO para = new EsPersonHealthVO();

//    ActivityYqDayReportBinding mBinding;
    @BindView(R.id.mv_case_type)
    MenuItemView mvCaseType;
    @BindView(R.id.tv_csName)
    TextView tv_csName;

    @BindView(R.id.et_csName)
    Spinner etCsName;
    @BindView(R.id.et_name)
    EditText etName;
    @BindView(R.id.et_phone)
    EditText etPhone;
    @BindView(R.id.et_sfz)
    EditText et_sfz;

    @BindView(R.id.et_xzdz)
    EditText et_xzdz;  //????????????

    @BindView(R.id.et_hjHome)
    EditText etHjHome;
    @BindView(R.id.et_tw)
    EditText etTw;
    @BindView(R.id.et_ks)
    EditText etKs;
    @BindView(R.id.et_wh_home)
    EditText etWhHome;
    @BindView(R.id.et_leave_whData)
    TextView etLeaveWhData;
    @BindView(R.id.tv_csname)
    TextView tvCsname;
    @BindView(R.id.et_jtgj)
    Spinner etJtgj;
    @BindView(R.id.et_gjxx)
    EditText etGjxx;
    @BindView(R.id.et_sftl)
    EditText etSftl;
    @BindView(R.id.et_txry)
    EditText etTxry;
    @BindView(R.id.et_rylb)
    Spinner etRylb;
    @BindView(R.id.ll_cs_name)
    LinearLayout llCsName;
    @BindView(R.id.et_whjc)
    EditText etWhjc;
    @BindView(R.id.et_qfry)
    TextView etQfry;
    @BindView(R.id.et_mx)
    EditText etMx;
    @BindView(R.id.tv_desc_character)
    TextView tvDescCharacter;

    @BindView(R.id.swb_whjc)
    SwitchButton swb_whjc;
    @BindView(R.id.swb_ks)
    SwitchButton swb_ks;


    @BindView(R.id.btn_report)
    Button btnReport;
//    ????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????
//    ????????????????????????????????????????????????????????????????????????????????????????????????????????????/??????/??????/?????????????????????
//            ???????????????????????????????????????????????????????????????????????????????????????
//    ??????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????
//            ?????????????????????????????????????????????????????????????????????

    String bean,areaCode,csId;
    boolean sourse;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_yq_day_report);
        ButterKnife.bind(this);

        ArrayAdapter arrayAdapter = new ArrayAdapter(this,R.layout.support_simple_spinner_dropdown_item,new String[]{
              "?????????????????????", "??????" ,"??????","??????","??????",
        });
        etJtgj.setAdapter(arrayAdapter);


        ArrayAdapter arrayAdapter1 = new ArrayAdapter(this,R.layout.support_simple_spinner_dropdown_item,new String[]{
                "?????????????????????","????????????" ,"????????????","????????????????????????","????????????",
                "????????????" ,"?????????????????????????????????"
        });
        etRylb.setAdapter(arrayAdapter1);

//        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_yq_day_report);
//        mBinding.setModel(new EsPersonHealthVO());

        try {
            setTitle("??????????????????????????????");
            bean  = (String) getIntent().getStringExtra("item");
            areaCode = (String) getIntent().getStringExtra("areaCode");
            sourse  = (Boolean) getIntent().getBooleanExtra("sourse",true);
            csId  = (String) getIntent().getStringExtra("csId");
            //??????

        }catch (Exception e){

        }

        if (!sourse){
            setTitle("????????????????????????");
            tv_csName.setText("????????????");
            mvCaseType.setVisibility(View.GONE);


            //??????????????????
            YqTypePresenter presenter = new YqTypePresenter();
            YqcsDayPara para = new YqcsDayPara();
            List<String>  list = new ArrayList<>();
            list.add("WH_0596");
            para.setType(list);
            presenter.getCsDayJk(this, para, new YqTypePresenter.CsBack() {
                @Override
                public void onSuccess(YqcsDayRes types) {
                    if (types != null) {
                        if (types != null && types.getWhDetailsList() != null) {

                             ArrayAdapter<YqcsDayRes.WhDetailsListBean> arrayAdapter = new ArrayAdapter(
                                     YqZHQKDayReportActivity.this,R.layout.support_simple_spinner_dropdown_item,types.getWhDetailsList());
                             etCsName.setAdapter(arrayAdapter);
                        }

                    }

                }
            });


        }else {


            YqcsDayPara para = new YqcsDayPara();

            YqTypePresenter presenter = new YqTypePresenter();
            presenter.getCsDayJk(this, para, new YqTypePresenter.CsBack() {
                @Override
                public void onSuccess(YqcsDayRes types) {
                    if (types != null) {
                        if (types != null && types.getWhDetailsList() != null) {

                            ArrayAdapter<YqcsDayRes.WhDetailsListBean> arrayAdapter =
                                    new ArrayAdapter(YqZHQKDayReportActivity.this,R.layout.support_simple_spinner_dropdown_item,types.getWhDetailsList());
                            etCsName.setAdapter(arrayAdapter);

                            for (int i = 0; i < types.getWhDetailsList().size(); i++) {
                                YqcsDayRes.WhDetailsListBean detailsListBean= types.getWhDetailsList().get(i);
                                if (detailsListBean.getId().equals(bean)){
                                    etCsName.setSelection(i);
                                }
                            }
                        }

                    }

                }
            });


        }


        btnReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (!check()){
                    ToastUtils.show(YqZHQKDayReportActivity.this,"???????????????????????????????????????");
                    return;
                }

//                EsPersonHealthVO para = mBinding.getModel();
                YqcsDayRes.WhDetailsListBean res = (YqcsDayRes.WhDetailsListBean) etCsName.getSelectedItem();
                para.setPlaceName(res.getObjname());
                para.setPlaceId(Long.parseLong(res.getId()));
                para.setPersonName(etName.getText().toString().trim());
                para.setPhone(etPhone.getText().toString().trim());
                para.setIdCard(et_sfz.getText().toString().trim());
                para.setCurrentAddress(etHjHome.getText().toString().trim());
                para.setDiscomfort(etKs.getText().toString().trim());//TextUtils.isEmpty(etKs.getText().toString().trim())?"0":"1"
                para.setIsTouch(etWhjc.getText().toString().trim());//TextUtils.isEmpty(etWhjc.getText().toString().trim())?"0":"1"
//                if (TextUtils.isEmpty(etTw.getText().toString().trim()) && (etTw.getText().toString().trim() instanceof Double){
//
//                }
                try {
                    if (!TextUtils.isEmpty(etTw.getText().toString().trim())){
                        para.setTemperature(Double.parseDouble(etTw.getText().toString().trim()));
                    }

                }catch (Exception e){
                    e.printStackTrace();
                    ToastUtils.show(YqZHQKDayReportActivity.this,"????????????????????????");
                    return;
                }

                para.setWhAddress(etWhHome.getText().toString().trim());
                para.setLeftWhDate(etLeaveWhData.getText().toString().trim());
                para.setVehicleType(etJtgj.getSelectedItem().toString().trim());
                para.setVehicleInfo(etGjxx.getText().toString().trim());
                para.setStopAddress(etSftl.getText().toString().trim());
                para.setColleagueNames(etTxry.getText().toString().trim());
                para.setPersonTypeName(etRylb.getSelectedItem().toString().trim());
                para.setEndTime(etQfry.getText().toString().trim());
                para.setRemark(etMx.getText().toString().trim());
                para.setAreaCode(res.getAreaCode());
                para.setReportDate(DateUtils.format(new Date(),4));

                para.setCreateName(UserSession.getUserName(YqZHQKDayReportActivity.this));
                para.setCreateId(UserSession.getUserId(YqZHQKDayReportActivity.this));

                YqService service = ServiceGenerator.create(YqService.class);
                //??????????????????
                if (sourse){

                    YqcsDayRes.WhDetailsListBean whDetailsListBean = (YqcsDayRes.WhDetailsListBean) etCsName.getSelectedItem();
                    para.setBayonetId(whDetailsListBean.getId());
                    para.setBayonetName(etCsName.getSelectedItem().toString().trim());

                    Observable<Response<Boolean>> observable =  service.esPersonHealth(new JsonBody(para));

                    observable
                            .observeOn(AndroidSchedulers.mainThread())
                            .compose(new ProgressTransformer<Response<Boolean>>(YqZHQKDayReportActivity.this, ProgressText.load))
                            .subscribe(new ResponseObserver<Boolean>(YqZHQKDayReportActivity.this){

                                @Override
                                public void onSuccess(Boolean data) {
                                    if (data){
                                        ToastUtils.show(YqZHQKDayReportActivity.this,"??????????????????");

                                    }

                                }
                            });
                }else {
                    Observable<Response<Boolean>> observable =  observable =  service.esPersonCheck(new JsonBody(para));

                    observable
                            .observeOn(AndroidSchedulers.mainThread())
                            .compose(new ProgressTransformer<Response<Boolean>>(YqZHQKDayReportActivity.this, ProgressText.load))
                            .subscribe(new ResponseObserver<Boolean>(YqZHQKDayReportActivity.this){

                                @Override
                                public void onSuccess(Boolean data) {
                                    if (data){
                                        ToastUtils.show(YqZHQKDayReportActivity.this,"??????????????????");

                                    }

                                }
                            });
                }


            }
        });

        etLeaveWhData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TimePickerDialog dialogAll = new TimePickerDialog.Builder()
                        .setType(Type.ALL)
                        .setCallBack(new OnDateSetListener() {
                            @Override
                            public void onDateSet(TimePickerDialog timePickerView, long millseconds) {
                                Date date = new Date(millseconds);
                                etLeaveWhData.setText(DateUtils.format(date,0));
                            }
                        })
                        .setTitleStringId("???????????????")
                        .build();
                dialogAll.show(getSupportFragmentManager(), "All");
            }
        });

        etQfry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TimePickerDialog dialogAll = new TimePickerDialog.Builder()
                        .setType(Type.ALL)
                        .setCallBack(new OnDateSetListener() {
                            @Override
                            public void onDateSet(TimePickerDialog timePickerView, long millseconds) {
                                Date date = new Date(millseconds);
                                etQfry.setText(DateUtils.format(date,0));
                            }
                        })
                        .setTitleStringId("???????????????")
                        .build();
                dialogAll.show(getSupportFragmentManager(), "All");
            }
        });


        //??????????????????
        YqTypePresenter presenter = new YqTypePresenter();
        presenter.getType(YqZHQKDayReportActivity.this, new YqTypePresenter.CallBack() {
            @Override
            public void onSuccess(List<CaseClassTreeVO> types) {
                if (types!=null){
                    ArrayList<ChooseData> chooseDatas = new ArrayList<>();

                    int index = 0;

                    for (int i = 0; i < types.size(); i++) {

                        CaseClassTreeVO type = types.get(i);
                        //??????
                        if (type.getQuestionClassId().equals("0101")){
                            ChooseData chooseData = new ChooseData(String.valueOf(type.getId()),type.getNodeName(),type.getWhType());
                            chooseDatas.add(chooseData);
                            if (chooseData.id.equals(csId)){
                                index = i;
                            }
                        }
                    }


                    mvCaseType.setSelectListener(new OptionalTextView.SelectListener() {
                        @Override
                        public void selected(int which, ChooseData chooseData) {

                            mvCaseType.setValue("??????/??????/"+chooseData.getName());
                            para.setPlaceTypeId(Long.valueOf(chooseData.id));
                            para.setPlaceTypeName(chooseData.getName());

                        }
                    });
                    mvCaseType.setChooseData(chooseDatas);

                   // mvCaseType.setDefault();//????????????
                    mvCaseType.setSelectedItem(index);//??????????????????

                }
            }
        });

        swb_whjc.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                para.setIsTouch(b?"1":"0");

            }
        });


        swb_ks.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                para.setDiscomfort(b?"1":"0");//TextUtils.isEmpty(etKs.getText().toString().trim())?"0":"1"
            }
        });


    }


    public boolean check() {

//        if (TextUtils.isEmpty(mBinding.etCsName.getText().toString().trim()) ||
//                TextUtils.isEmpty(mBinding.etName.getText().toString().trim()) ||
//                TextUtils.isEmpty(mBinding.etPhone.getText().toString().trim()) ||
//                TextUtils.isEmpty(mBinding.etHjHome.getText().toString().trim()) ||
//                TextUtils.isEmpty(mBinding.etJtgj.getText().toString().trim()) ||
//                TextUtils.isEmpty(mBinding.etGjxx.getText().toString().trim()) ||
//                TextUtils.isEmpty(mBinding.etRylb.getText().toString().trim())
//        ){
//            return false;
//        }
        if (TextUtils.isEmpty(etCsName.getSelectedItem().toString().trim()) ||
                TextUtils.isEmpty(etName.getText().toString().trim()) ||
                TextUtils.isEmpty(etPhone.getText().toString().trim()) ||
                TextUtils.isEmpty(etHjHome.getText().toString().trim()) ||
                etJtgj.getSelectedItem().toString().trim().equals("?????????????????????")||
                etRylb.getSelectedItem().toString().trim().equals("?????????????????????")||
                TextUtils.isEmpty(etGjxx.getText().toString().trim()))
        {
            return false;
        }
        return true;

    }
}
