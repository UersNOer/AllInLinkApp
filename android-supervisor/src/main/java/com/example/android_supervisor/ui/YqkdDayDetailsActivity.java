package com.example.android_supervisor.ui;


import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Bundle;
import androidx.appcompat.widget.AppCompatImageView;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.LinearLayout;

import com.bin.david.form.core.SmartTable;
import com.example.android_supervisor.Presenter.YqTypePresenter;
import com.example.android_supervisor.R;
import com.example.android_supervisor.http.ServiceGenerator;
import com.example.android_supervisor.http.common.ProgressTransformer;
import com.example.android_supervisor.http.request.JsonBody;
import com.example.android_supervisor.http.response.Response;
import com.example.android_supervisor.http.response.ResponseObserver;
import com.example.android_supervisor.http.service.YqService;
import com.example.android_supervisor.ui.model.CountInfoRes;
import com.example.android_supervisor.ui.model.EsPersonCheckCountVO;
import com.example.android_supervisor.ui.model.KdJkPara;
import com.example.android_supervisor.ui.model.YqcsDayPara;
import com.example.android_supervisor.ui.model.YqcsDayRes;
import com.example.android_supervisor.ui.view.ProgressText;
import com.example.android_supervisor.utils.DateUtils;
import com.example.android_supervisor.utils.ToastUtils;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;

public class YqkdDayDetailsActivity extends BaseActivity {

    SmartTable table;
    String bean, areaCode;

    @BindView(R.id.tv_time)
    public AutoCompleteTextView tv_time;

    @BindView(R.id.iv_time)
    public AppCompatImageView iv_time;

    View btnSave;

    @BindView(R.id.tv_function)
    FloatingActionButton tv_function;

    @BindView(R.id.ff_table)
    public LinearLayout ff_table;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_yq_kddata_details);
        ButterKnife.bind(this);
        try {
//            bean  = (String) getIntent().getSerializableExtra("item");
            areaCode = (String) getIntent().getSerializableExtra("areaCode");
            getNetData();
        } catch (Exception e) {

        }

        table = (SmartTable<CountInfoRes>) findViewById(R.id.table);

        btnSave = addMenu("上报", new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(YqkdDayDetailsActivity.this, YqZHQKDayReportActivity.class);
                intent.putExtra("item", bean);
                intent.putExtra("areaCode", areaCode);
                intent.putExtra("sourse", false);
                startActivity(intent);
            }
        });

//        iv_time.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                tv_time.getAdapter().ge
//            }
//        });


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
//                        ArrayList<YqcsDayRes.WhDetailsListBean> chooseDatas = new ArrayList<>();

//                        for (YqcsDayRes.WhDetailsListBean whDetailsListBean : types.getWhDetailsList()) {
//
//                            ChooseData chooseData = new ChooseData(String.valueOf(whDetailsListBean.getId()), whDetailsListBean.getObjname());
//                            chooseDatas.add(chooseData);
//
//                        }
                        initSearchView(types.getWhDetailsList());
                    }

                }

            }
        });

        tv_function.show(); ;
        tv_function.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Configuration mConfiguration = getResources().getConfiguration();
                int ori = mConfiguration.orientation;
                if (ori == mConfiguration.ORIENTATION_LANDSCAPE) {
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);//强制为竖屏
                } else if (ori == mConfiguration.ORIENTATION_PORTRAIT) {
                    //竖屏
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);//强制为横屏
                }
            }
        });
    }

    private void getNetData() {


        Calendar dateStart = Calendar.getInstance();
        dateStart.setTime(new Date());
        dateStart.set(Calendar.HOUR_OF_DAY, 0);
        dateStart.set(Calendar.MINUTE, 0);
        dateStart.set(Calendar.SECOND, 0);

        Calendar endTime =  Calendar.getInstance();
        endTime.setTime(new Date());
        endTime.set(Calendar.HOUR_OF_DAY, 23);
        endTime.set(Calendar.MINUTE, 59);
        endTime.set(Calendar.SECOND, 59);


        KdJkPara para = new KdJkPara();
        try{
            para.reportStartDate = DateUtils.format(dateStart.getTime(), 0);
            para.reportEndDate = DateUtils.format(endTime.getTime(), 0);
            para.bayonetId = TextUtils.isEmpty(bean)?null:Long.parseLong(bean);
        }catch (Exception e){

        }

//        QueryBody queryBody = new QueryBody.Builder()
//                .eq("placeId","1228281231834087424")
//                .ge("createTime", DateUtils.format(dateStart.getTime(),0))
//                .le("createTime", DateUtils.format(new Date(),0))
//                .create();


        YqService service = ServiceGenerator.create(YqService.class);
        Observable<Response<List<EsPersonCheckCountVO>>> observable = observable = service.countInfo(new JsonBody(para));

        observable
                .observeOn(AndroidSchedulers.mainThread())
                .compose(new ProgressTransformer<Response<List<EsPersonCheckCountVO>>>(YqkdDayDetailsActivity.this, ProgressText.load))
                .subscribe(new ResponseObserver<List<EsPersonCheckCountVO>>(YqkdDayDetailsActivity.this) {

                    @Override
                    public void onSuccess(List<EsPersonCheckCountVO> data) {
                        ff_table.removeAllViews();
                        if (data!=null&& data.size()==0){
                            ToastUtils.show(YqkdDayDetailsActivity.this,"没有数据");
                            return;
                        }

                        SmartTable table = new SmartTable(YqkdDayDetailsActivity.this);
                        table.setData(data);
                        ff_table.addView(table);

                    }


                });
    }


    private void initSearchView(List<YqcsDayRes.WhDetailsListBean> beans) {
        SearchAdapter adapter = new SearchAdapter(YqkdDayDetailsActivity.this, beans);

        tv_time.setAdapter(adapter);
        tv_time.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                YqcsDayRes.WhDetailsListBean chooseData = (YqcsDayRes.WhDetailsListBean) adapterView.getItemAtPosition(i);
                bean = chooseData.getId();
                areaCode = chooseData.getAreaCode();
                getNetData();
            }
        });
    }


}
