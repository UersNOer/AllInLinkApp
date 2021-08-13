package com.example.android_supervisor.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.android_supervisor.Presenter.EditDayNoticePresenter;
import com.example.android_supervisor.Presenter.callback.IEditDayNoticeCallback;
import com.example.android_supervisor.R;
import com.example.android_supervisor.entities.DayNoticeModel;
import com.example.android_supervisor.utils.ToastUtils;

import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class EditDayNoticeActivity extends BaseActivity implements IEditDayNoticeCallback {

    @BindView(R.id.tvTime)
    TextView tvTime;
    @BindView(R.id.tvStatus)
    TextView tvStatus;
    @BindView(R.id.btnSave)
    Button btnSave;
    @BindView(R.id.etContent)
    EditText etContent;
    @BindView(R.id.btnSubmit)
    Button btnSubmit;

    private EditDayNoticePresenter mPresenter;
    private DayNoticeModel mModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_day_notice);
        initView();
        setListener();
    }

    private void setListener() {

    }


    private void initView() {
        ButterKnife.bind(this);

        initUI();
        mPresenter = new EditDayNoticePresenter(this);

    }

    private void initUI(){
        mModel = (DayNoticeModel) getIntent().getSerializableExtra("bean");
        if (mModel == null){
            setTitle("新增日志");
        }else if (mModel.getFillState() == 0){
            setTitle("日志详情");
            tvStatus.setVisibility(View.VISIBLE);
            tvStatus.setText("(已保存)");
            etContent.setText(mModel.getFillContent());
        }else{
            setTitle("日志详情");
            tvStatus.setText("(已提交)");
            etContent.setText(mModel.getFillContent());
            tvStatus.setVisibility(View.VISIBLE);
            etContent.setEnabled(false);
            btnSubmit.setVisibility(View.GONE);
            btnSave.setVisibility(View.GONE);
        }
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String today = dateFormat.format(new Date(System.currentTimeMillis()));
        tvTime.setText(today);


    }

    @OnClick(R.id.btnSave)
    void onSaveClick(View view){
        if (mPresenter.isCanSubmit(etContent)){
            mPresenter.saveData(etContent.getText().toString());
        }
    }
    @OnClick(R.id.btnSubmit)
    void onSubmitClick(View view){
        if (mPresenter.isCanSubmit(etContent)){
            String id = null;
            if (mModel != null){
                id = mModel.getId();
            }
            mPresenter.showSureDialog(etContent.getText().toString(),id);
        }
    }
    public static void start(Context context) {
        Intent starter = new Intent(context, EditDayNoticeActivity.class);
        ((BaseActivity)context).startActivityForResult(starter,123);
    }
    public static void start(Context context,DayNoticeModel model) {
        Intent starter = new Intent(context, EditDayNoticeActivity.class);
        starter.putExtra("bean",model);
        ((BaseActivity)context).startActivityForResult(starter,123);
    }

    @Override
    public void saveDataSuccessCallback() {
        if (isFinishing()){
            return;
        }
        ToastUtils.show(mContext,"保存成功");
        setResult();
    }

    @Override
    public void submitSuccessCallback() {
        if (isFinishing()){
            return;
        }
        ToastUtils.show(mContext,"提交成功");
        setResult();
    }

    public void setResult(){
        Intent intent = new Intent();
        setResult(RESULT_OK,intent);
        finish();
    }
}
