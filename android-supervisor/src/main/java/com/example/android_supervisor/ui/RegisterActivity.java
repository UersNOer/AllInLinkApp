package com.example.android_supervisor.ui;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import android.text.InputType;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.baidu.ocr.sdk.model.IDCardResult;
import com.blankj.utilcode.util.RegexUtils;
import com.example.android_supervisor.R;
import com.example.android_supervisor.common.BaiduOCRApi;
import com.example.android_supervisor.common.ResultCallback;
import com.example.android_supervisor.entities.RegisterPara;
import com.example.android_supervisor.http.ServiceGenerator;
import com.example.android_supervisor.http.common.ProgressTransformer;
import com.example.android_supervisor.http.request.JsonBody;
import com.example.android_supervisor.http.response.Response;
import com.example.android_supervisor.http.response.ResponseObserver;
import com.example.android_supervisor.http.service.BasicService;
import com.example.android_supervisor.ui.view.ProgressText;
import com.example.android_supervisor.utils.SystemUtils;
import com.example.android_supervisor.utils.ToastUtils;

import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;

/**
 * 监督员注册页面
 *
 * @author wujie
 */
public class RegisterActivity extends BaseActivity {

    // 选择部门
    public static final int REQUEST_CODE_CHOICE_DEPT = 1;

    private RegisterPara mRegisterPara = new RegisterPara();

    @BindView(R.id.et_register_username)
    com.xw.repo.xedittext.XEditText etUserName;


    @BindView(R.id.et_register_password)
    EditText etUserPwd;

    @BindView(R.id.et_register_password2)
    EditText etUserPwd2;

    @BindView(R.id.et_register_nickname)
    EditText etNickName;

    @BindView(R.id.et_register_gender)
    TextView tvGender;

    @BindView(R.id.et_register_depart)
    TextView tvDepart;

    @BindView(R.id.et_register_cardid)
    EditText etCardId;

    @BindView(R.id.et_register_mobile)
    EditText etMobile;

    private String deptId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ButterKnife.bind(this);

    }



    @Override
    protected boolean isEnableAppLock() {
        return false;
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }

    @OnClick(R.id.iv_register_show_password)
    public void onShowPwd(View v) {
        if (v.isSelected()) {
            v.setSelected(false);
            ((ImageView) v).setImageResource(R.drawable.ic_pwd_show);
            etUserPwd.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        } else {
            v.setSelected(true);
            ((ImageView) v).setImageResource(R.drawable.ic_pwd_hide);
            etUserPwd.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
        }
    }

    @OnClick(R.id.iv_register_show_password2)
    public void onShowPwd2(View v) {
        if (v.isSelected()) {
            v.setSelected(false);
            ((ImageView) v).setImageResource(R.drawable.ic_pwd_show);
            etUserPwd2.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        } else {
            v.setSelected(true);
            ((ImageView) v).setImageResource(R.drawable.ic_pwd_hide);
            etUserPwd2.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
        }
    }

    @OnClick(R.id.et_register_gender)
    public void onSelectGender(View v) {
        new AlertDialog.Builder(this)
                .setTitle("性别")
                .setItems(new String[]{"男", "女"}, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:
                                tvGender.setText("男");
                                break;
                            case 1:
                                tvGender.setText("女");
                                break;
                        }
                    }
                }).show();
    }

    @OnClick(R.id.et_register_depart)
    public void onSelectDepart(View v) {
        Intent intent = new Intent(this, DeptListActivity.class);
        startActivityForResult(intent, REQUEST_CODE_CHOICE_DEPT);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }

    @OnClick(R.id.iv_register_cardid_scan)
    public void onCardIdScan(View v) {
        BaiduOCRApi.getInstance().recognizeIDCard(this,
                new ResultCallback<IDCardResult>() {
                    @Override
                    public void onResult(IDCardResult result, int tag) {

                    }

                    @Override
                    public void onResult(IDCardResult result) {
                        etNickName.setText(result.getName().toString());
                        etCardId.setText(result.getIdNumber().toString());
                        tvGender.setText(result.getGender().toString());
                    }
                });
    }

    @OnClick(R.id.btn_register)
    public void onRegister(View v) {
        SystemUtils.hideSoftInput(this, v);
        if (validateParams()) {
            register();
        }
    }

    private boolean validateParams() {
        String userName = etUserName.getText().toString().trim();
        if (TextUtils.isEmpty(userName)) {
            ToastUtils.show(this, "请输入登录名");
            etUserName.requestFocus();
            return false;
        }
        String userPwd = etUserPwd.getText().toString().trim();
        if (TextUtils.isEmpty(userPwd)) {
            ToastUtils.show(this, "请输入密码");
            etUserPwd.requestFocus();
            return false;
        }
        String userPwd2 = etUserPwd2.getText().toString().trim();
        if (TextUtils.isEmpty(userPwd2)) {
            ToastUtils.show(this, "请再次输入密码");
            etUserPwd2.requestFocus();
            return false;
        }
        if (!userPwd.equals(userPwd2)) {
            ToastUtils.show(this, "两次输入的密码不一致，请重新输入");
            etUserPwd2.requestFocus();
            return false;
        }
        String nickName = etNickName.getText().toString().trim();
        String pattern = "[\\u4E00-\\u9FA5]+";
        if (TextUtils.isEmpty(nickName)) {
            ToastUtils.show(this, "请输入姓名");
            etNickName.requestFocus();
            return false;
        }
        if (!Pattern.matches(pattern, nickName)) {
            ToastUtils.show(this, "请输入中文姓名");
            etNickName.requestFocus();
            return false;
        } else {
            if (nickName.length() > 50 || nickName.length() < 1) {
                ToastUtils.show(this, "姓名只能是1~50个中文");
                etNickName.requestFocus();
                return false;
            }
        }
        String gender = tvGender.getText().toString();
        if (TextUtils.isEmpty(gender)) {
            ToastUtils.show(this, "请选择性别");
            return false;
        }
        if (TextUtils.isEmpty(deptId)) {
            ToastUtils.show(this, "请选择部门");
            return false;
        }
        String cardId = etCardId.getText().toString();
        if (!TextUtils.isEmpty(cardId)) {
            if (!cardId.matches("(^\\d{15}$)|(^\\d{18}$)|(^\\d{17}(\\d|X|x)$)")) {
                ToastUtils.show(this, "身份证号格式错误");
                etCardId.requestFocus();
                return false;
            }
        }
        String mobile = etMobile.getText().toString();
        if (TextUtils.isEmpty(mobile)) {
            ToastUtils.show(this, "请输入手机号");
            etMobile.requestFocus();
            return false;
        }

        if (!RegexUtils.isMobileExact(mobile)){
            ToastUtils.show(this, "手机号格式不正确");
            etMobile.requestFocus();
            return false;
        }

//        if (!mobile.matches("^((13[0-9])|(15[^4,\\D])|(17[0-9])|(18[0,5-9]))\\d{8}$")) {
//            ToastUtils.show(this, "手机号格式不正确");
//            etMobile.requestFocus();
//            return false;
//        }
        int sex = 0;
        if (gender.equals("男")) {
            sex = 1;
        } else if (gender.equals("女")) {
            sex = 2;
        }
        mRegisterPara.setLoginName(userName);
        mRegisterPara.setUserName(nickName);
        mRegisterPara.setUserPwd(userPwd);
        mRegisterPara.setIdentityId(cardId);
        mRegisterPara.setUserMobile(mobile);
        mRegisterPara.setSex(sex);
        mRegisterPara.setDefaultDeptId(Long.parseLong(deptId)/*"1111464899894059009"*/);
//        mRegisterPara.setDeptIds(new String[]{deptId/*"1111464899894059009"*/});

        RegisterPara.ObExt obExt = new RegisterPara.ObExt();
        obExt.setPdaId(SystemUtils.getIMEI(this));
        obExt.setPdaBrand(Build.BRAND);
        obExt.setPdaType(Build.MODEL);
        obExt.setPdaVersion(Build.VERSION.RELEASE);
        mRegisterPara.setObExt(obExt);
        return true;
    }

    @SuppressWarnings("unchecked")
    private void register() {
        BasicService basicService = ServiceGenerator.create(BasicService.class);
        Observable<Response> observable = basicService.register(new JsonBody(mRegisterPara));
        observable.compose(this.<Response>bindToLifecycle())
                .observeOn(AndroidSchedulers.mainThread())
                .compose(new ProgressTransformer<Response>(this, ProgressText.submit))
                .subscribe(new ResponseObserver(this) {
                    @Override
                    public void onSuccess(Object data) {
                        setResult(Activity.RESULT_OK);
                        finish();
                        ToastUtils.show(RegisterActivity.this, "注册申请提交成功，请等待后台管理员进行审核");
                    }
                });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == REQUEST_CODE_CHOICE_DEPT) {
                String id = data.getStringExtra("id");
                String name = data.getStringExtra("name");

                this.deptId = id;
                tvDepart.setText(name);
            }
        }
    }



}
