package com.example.android_supervisor.ui;


import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bin.david.form.core.SmartTable;
import com.example.android_supervisor.ui.model.CountInfoRes;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.jzxiang.pickerview.TimePickerDialog;
import com.jzxiang.pickerview.data.Type;
import com.jzxiang.pickerview.listener.OnDateSetListener;
import com.example.android_supervisor.R;
import com.example.android_supervisor.http.ServiceGenerator;
import com.example.android_supervisor.http.common.ProgressTransformer;
import com.example.android_supervisor.http.request.QueryBody;
import com.example.android_supervisor.http.response.Response;
import com.example.android_supervisor.http.response.ResponseObserver;
import com.example.android_supervisor.http.service.YqService;
import com.example.android_supervisor.ui.view.ProgressText;
import com.example.android_supervisor.utils.DateUtils;
import com.example.android_supervisor.utils.ToastUtils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;

public class YqZHQKDayDetailsActivity extends BaseActivity {

    SmartTable table;
    String bean,areaCode,csId;

    @BindView(R.id.tv_time)
    public TextView tv_time;

    @BindView(R.id.iv_time)
    public ImageView iv_time;

    @BindView(R.id.ff_table)
    public LinearLayout ff_table;

    @BindView(R.id.tv_function)
    FloatingActionButton tv_function;

    View btnSave;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_yq_data_details);
        ButterKnife.bind(this);
        try {
            bean  = (String) getIntent().getSerializableExtra("item");
            areaCode  = (String) getIntent().getSerializableExtra("areaCode");
            csId  = (String) getIntent().getSerializableExtra("csId");
            getNetData(new Date());
        }catch (Exception e){

        }

        table = (SmartTable<CountInfoRes>) findViewById(R.id.table);
        table.setData(countInfoRes);


        btnSave = addMenu("上报", new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent =  new Intent(YqZHQKDayDetailsActivity.this,YqZHQKDayReportActivity.class);
                intent.putExtra("item",bean);
                intent.putExtra("areaCode",areaCode);
                intent.putExtra("csId",csId);
                startActivity(intent);
            }
        });

        tv_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TimePickerDialog dialogAll = new TimePickerDialog.Builder()
                        .setType(Type.YEAR_MONTH_DAY)
                        .setCallBack(new OnDateSetListener() {
                            @Override
                            public void onDateSet(TimePickerDialog timePickerView, long millseconds) {
                                Date date = new Date(millseconds);
                                tv_time.setText(DateUtils.format(date,1));
                                getNetData(date);
                            }
                        })
                        .setTitleStringId("请选择时间")
                        .build();
                dialogAll.show(getSupportFragmentManager(), "All");
            }
        });


        tv_function = findViewById(R.id.tv_function);
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


    List<CountInfoRes> countInfoRes = new ArrayList<>();
    private void getNetData(Date date) {

        if (bean == null){
            return;
        }


        Calendar dateStart = Calendar.getInstance();
        dateStart.setTime(date);
        dateStart.set(Calendar.HOUR_OF_DAY, 0);
        dateStart.set(Calendar.MINUTE, 0);
        dateStart.set(Calendar.SECOND, 0);

        Calendar endTime =  Calendar.getInstance();
        endTime.setTime(date);
        endTime.set(Calendar.HOUR_OF_DAY, 23);
        endTime.set(Calendar.MINUTE, 59);
        endTime.set(Calendar.SECOND, 59);


        QueryBody queryBody = new QueryBody.Builder()
                .eq("placeId",bean)
                .ge("createTime", DateUtils.format(dateStart.getTime(),0))
                .le("createTime", DateUtils.format(endTime.getTime(),0))
                .create();


        YqService service = ServiceGenerator.create(YqService.class);
        Observable<Response<List<CountInfoRes>>> observable =  observable =  service.esPersonHealthDetailList(queryBody);

        observable
                .observeOn(AndroidSchedulers.mainThread())
                .compose(new ProgressTransformer<Response<List<CountInfoRes>>>(YqZHQKDayDetailsActivity.this, ProgressText.load))
                .subscribe(new ResponseObserver<List<CountInfoRes>>(YqZHQKDayDetailsActivity.this){

                    @Override
                    public void onSuccess(List<CountInfoRes> data) {
                        ff_table.removeAllViews();
                        if (data!=null&& data.size()==0){
                            ToastUtils.show(YqZHQKDayDetailsActivity.this,"没有数据");
                            return;
                        }

                        SmartTable table = new SmartTable(YqZHQKDayDetailsActivity.this);
                        table.setData(data);
                        ff_table.addView(table);
                    }

                });
    }


}
