package com.example.android_supervisor.ui;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android_supervisor.Presenter.LeaveManagerPresenter;
import com.example.android_supervisor.Presenter.callback.ILeaveManagerCallback;
import com.example.android_supervisor.R;
import com.example.android_supervisor.entities.LeaveRecordModel;
import com.example.android_supervisor.entities.LeaveTypeModel;
import com.example.android_supervisor.utils.MDatePickerDialog;
import com.example.android_supervisor.utils.SelectorSingleDialog;
import com.example.android_supervisor.utils.TimeUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LeaveManagerActivity extends BaseActivity implements ILeaveManagerCallback {

    @BindView(R.id.tvType)
    TextView tvType;
    @BindView(R.id.tvStartTime)
    TextView tvStartTime;
    @BindView(R.id.tvEndTime)
    TextView tvEndTime;
    @BindView(R.id.tvDayNum)
    TextView tvDayNum;
    @BindView(R.id.etReason)
    EditText etReason;
    @BindView(R.id.tvSubmit)
    TextView tvSubmit;
    @BindView(R.id.tvReset)
    TextView tvReset;
    @BindView(R.id.ivEnterType)
    ImageView ivEnterType;
    @BindView(R.id.ivEnterStart)
    ImageView ivEnterStart;
    @BindView(R.id.ivEnterEnd)
    ImageView ivEnterEnd;

    private LeaveManagerPresenter mPresenter;
    private LeaveTypeModel mSelectorType;
    private SelectorSingleDialog mTypeDialog;
    private LeaveRecordModel mModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leave_manager);
        initView();
        setListener();
    }

    private void setListener() {

    }

    @OnClick({R.id.llType,R.id.llStartTime,R.id.llEndTime,R.id.tvSubmit,R.id.tvReset})
    void onClick(View view){
        if(mModel != null){
            return;
        }
        switch (view.getId()){
            case R.id.llType:
                if (mTypeDialog != null)
                mTypeDialog.show();
                break;
            case R.id.llStartTime:
                String startTime = tvStartTime.getText().toString();
                MDatePickerDialog mStartDialog = new MDatePickerDialog(mContext, startTime, new MDatePickerDialog.CallBack() {
                    @Override
                    public void callback(String time) {
                        if (!TimeUtils.isEndOverStart(time,tvEndTime.getText().toString(),"yyyy年MM月dd日")){
//                            showToast("开始时间不能大于结束时间");
                            tvEndTime.setText("");
                            tvDayNum.setText(mPresenter.getDayNum(tvStartTime.getText().toString(),tvEndTime.getText().toString()));
                            return;
                        }
                        tvStartTime.setText(time);
                        tvDayNum.setText(mPresenter.getDayNum(tvStartTime.getText().toString(),tvEndTime.getText().toString()));
                    }
                });
                mStartDialog.show();
                break;
            case R.id.llEndTime:
                String endTime = tvEndTime.getText().toString();
                MDatePickerDialog mEndDialog = new MDatePickerDialog(mContext, endTime, new MDatePickerDialog.CallBack() {
                    @Override
                    public void callback(String time) {
                        if (!TimeUtils.isEndOverStart(tvStartTime.getText().toString(),time,"yyyy年MM月dd日")){
                            showToast("结束时间不能小于开始时间");
                            tvEndTime.setText("");
                            tvDayNum.setText(mPresenter.getDayNum(tvStartTime.getText().toString(),tvEndTime.getText().toString()));
                            return;
                        }
                        tvEndTime.setText(time);
                        tvDayNum.setText(mPresenter.getDayNum(tvStartTime.getText().toString(),tvEndTime.getText().toString()));
                    }
                });
                mEndDialog.show();
                break;
            case R.id.tvSubmit:
                if (mPresenter.isCanSubmit(tvStartTime,tvEndTime,tvType,tvDayNum,etReason)){
                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setMessage("您取得提交请假吗？");
                    builder.setTitle("温馨提示");
                    builder.setNegativeButton("取消",null);
                    builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            mPresenter.submitLeaveData(tvStartTime,tvEndTime,mSelectorType.getDictCode(),tvDayNum,etReason);
                        }
                    });
                    builder.create().show();
                }
                break;
            case R.id.tvReset:
                tvStartTime.setText("");
                tvEndTime.setText("");
                etReason.setText("");
                tvType.setText("");
                break;
        }
    }
    private void initView() {
        ButterKnife.bind(this);
        mPresenter = new LeaveManagerPresenter(this,this);
        mPresenter.getTypeData();
        updateUi();
    }

    private void updateUi() {
//        /obLeave/get/{id}  获取请假详细信息
        mModel = (LeaveRecordModel) getIntent().getSerializableExtra("bean");
        if (mModel != null){
            tvType.setEnabled(false);
            tvEndTime.setEnabled(false);
            tvStartTime.setEnabled(false);
            tvDayNum.setEnabled(false);
            etReason.setEnabled(false);
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy年MM月dd日");
            tvStartTime.setText(dateFormat.format(mModel.getLeaveStartTime()));
            tvEndTime.setText(dateFormat.format(mModel.getLeaveEndTime()));
            if ("0".equals(mModel.getLeaveType())){
                tvType.setText("事假");
            }else if ("1".equals(mModel.getLeaveType())){
                tvType.setText("病假");
            }else if ("2".equals(mModel.getLeaveType())){
                tvType.setText("年假");
            }else {
//            、、3
                tvType.setText("其他");
            }
            tvDayNum.setText(mModel.getLeaveDuration()+"");
            etReason.setText(mModel.getLeaveReason());
            tvSubmit.setVisibility(View.GONE);
            tvReset.setVisibility(View.GONE);
            ivEnterEnd.setVisibility(View.INVISIBLE);
            ivEnterStart.setVisibility(View.INVISIBLE);
            ivEnterType.setVisibility(View.INVISIBLE);
        }
    }

    public static void start(BaseActivity context) {
        Intent starter = new Intent(context, LeaveManagerActivity.class);
        context.startActivityForResult(starter,123);
    }
    public static void start(BaseActivity context,LeaveRecordModel model) {
        Intent starter = new Intent(context, LeaveManagerActivity.class);
        starter.putExtra("bean",model);
        context.startActivityForResult(starter,123);
    }


    @Override
    public void getTypeSuccessCallback(List<LeaveTypeModel> data, ArrayList<String> list) {
        if (list == null || data.size() <= 0){
            return;
        }
        mTypeDialog = new SelectorSingleDialog(mContext, new SelectorSingleDialog.Callback() {
            @Override
            public void onSelector(String param, int position) {
                tvType.setText(data.get(position).getDictName());
                mSelectorType = data.get(position);
            }
        });
        mTypeDialog.setData(list);
    }

    @Override
    public void submitDataSuccessCallback() {
        setResult(RESULT_OK,new Intent());
        finish();
    }
}
