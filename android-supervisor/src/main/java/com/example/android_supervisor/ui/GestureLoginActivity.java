package com.example.android_supervisor.ui;

import android.app.Activity;
import android.graphics.Paint;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.andrognito.patternlockview.PatternLockView;
import com.andrognito.patternlockview.listener.PatternLockViewListener;
import com.andrognito.patternlockview.utils.PatternLockUtils;
import com.example.android_supervisor.R;
import com.example.android_supervisor.common.UserSession;
import com.example.android_supervisor.entities.GestureLock;
import com.example.android_supervisor.service.AppLockThread;
import com.example.android_supervisor.sqlite.PublicSqliteHelper;
import com.example.android_supervisor.utils.SystemUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @author wujie
 */
public class GestureLoginActivity extends BaseActivity implements PatternLockViewListener {

    @BindView(R.id.iv_title_bar_nav)
    ImageView ivNav;

    @BindView(R.id.tv_gesture_login_tips)
    TextView tvTips;

    @BindView(R.id.rl_gesture_login_password)
    RelativeLayout rlPassword;

    @BindView(R.id.et_gesture_login_password)
    EditText etPassword;

    @BindView(R.id.ll_gesture_login_gesture)
    LinearLayout llGesture;

    @BindView(R.id.gesture_login_lock_indicator)
    PatternLockView patternLockIndicator;

    @BindView(R.id.gesture_login_lock_view)
    PatternLockView patternLockView;

    @BindView(R.id.tv_gesture_login_forget)
    TextView tvForget;

    private int uiState;

    private String password;
    private String gesture;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gesture_login);
        ButterKnife.bind(this);
        ivNav.setVisibility(View.GONE);

        tvForget.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
        tvForget.getPaint().setAntiAlias(true);

        String userId = UserSession.getUserId(this);

        PublicSqliteHelper sqliteHelper = PublicSqliteHelper.getInstance(this);
//        Account account = SaveObjectUtils.getInstance(this).getObject("account",null);
        String pwd = UserSession.getUserPwd(this);
        if (!TextUtils.isEmpty(pwd)) {
            password = pwd;
        } else {
            finish();
            return;
        }

        GestureLock gestureLock = sqliteHelper.getGestureLockDao().queryForId(userId);
        if (gestureLock != null) {
            gesture = gestureLock.getGesture();
        }

        if (gesture == null) {
            setUiState(0);
        } else {
            //之前设置过手势密码
            setUiState(1);
        }
        patternLockIndicator.setInputEnabled(false);
        patternLockView.addPatternLockListener(this);

        AppLockThread appLock = AppLockThread.getInstance();
        if (appLock != null) {
            appLock.pause();
        }
    }

    @Override
    protected boolean isEnableAppLock() {
        return false;
    }

    public void setUiState(int state) {
        this.uiState = state;
        switch (state) {
            case 1:
                rlPassword.setVisibility(View.GONE);
                llGesture.setVisibility(View.VISIBLE);
                patternLockIndicator.setVisibility(View.INVISIBLE);
                tvTips.setText("请绘制手势密码");
                tvTips.setTextColor(getResources().getColor(R.color.gray_light));
                tvForget.setText("忘记手势？");
                break;
            case 2:
                rlPassword.setVisibility(View.GONE);
                llGesture.setVisibility(View.VISIBLE);
                patternLockIndicator.setVisibility(View.VISIBLE);
                tvTips.setText("请设置手势密码");
                tvTips.setTextColor(getResources().getColor(R.color.gray_light));
                tvForget.setText("重置手势");
                break;
            case 3:
                rlPassword.setVisibility(View.GONE);
                llGesture.setVisibility(View.VISIBLE);
                patternLockIndicator.setVisibility(View.VISIBLE);
                tvTips.setText("请再次设置手势密码");
                tvTips.setTextColor(getResources().getColor(R.color.gray_light));
                tvForget.setText("重置手势");
                break;
            default:
                rlPassword.setVisibility(View.VISIBLE);
                llGesture.setVisibility(View.GONE);
                patternLockIndicator.setVisibility(View.INVISIBLE);
                tvTips.setText("请输入登录密码");
                tvTips.setTextColor(getResources().getColor(R.color.gray_light));
                tvForget.setText("忘记手势？");
                break;
        }
    }

    @OnClick(R.id.iv_gesture_login_show_pwd)
    public void onShowPwd(View v) {
        if (v.isSelected()) {
            v.setSelected(false);
            ((ImageView) v).setImageResource(R.drawable.ic_pwd_show);
            etPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        } else {
            v.setSelected(true);
            ((ImageView) v).setImageResource(R.drawable.ic_pwd_hide);
            etPassword.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
        }
    }

    @OnClick(R.id.iv_gesture_login_enter)
    public void onPwdEnter(View v) {
        SystemUtils.hideSoftInput(this, etPassword);
        String text = etPassword.getText().toString();
        if (TextUtils.isEmpty(text)) {
            tvTips.setText("请输入登录密码");
            tvTips.setTextColor(getResources().getColor(R.color.gray_light));
        } else if (!password.equals(text)) {
            tvTips.setText("登录密码不正确");
            tvTips.setTextColor(getResources().getColor(R.color.text_red));
        } else {
            setUiState(2);
        }
    }

    @OnClick(R.id.tv_gesture_login_forget)
    void forgetGesture(View v) {
        switch (uiState) {
            case 1:
                setUiState(0);
                patternLockView.clearPattern();
                break;
            case 2:
            case 3:
                patternLockView.clearPattern();
                break;
        }
    }

    @Override
    public void onStarted() {

    }

    @Override
    public void onProgress(List<PatternLockView.Dot> progressPattern) {

    }

    @Override
    public void onComplete(List<PatternLockView.Dot> pattern) {
        switch (uiState) {
            case 1:
                if (pattern != null && pattern.size() > 3) {
                    String gesture = PatternLockUtils.patternToMD5(patternLockView, pattern);
                    if (this.gesture.equals(gesture)) {
                        tvTips.setText("输入正确");
                        tvTips.setTextColor(getResources().getColor(R.color.text_green));
                        setResult(Activity.RESULT_OK);
                        finish();
                    } else {
                        tvTips.setText("手势密码绘制错误");
                        tvTips.setTextColor(getResources().getColor(R.color.text_red));
                        patternLockView.setViewMode(PatternLockView.PatternViewMode.WRONG);
                    }
                } else {
                    tvTips.setText("至少绘制4个点，请重新绘制");
                    tvTips.setTextColor(getResources().getColor(R.color.text_red));
                    patternLockView.setViewMode(PatternLockView.PatternViewMode.WRONG);
                }
                break;
            case 2:
                if (pattern != null && pattern.size() > 3) {
                    patternLockIndicator.setPattern(PatternLockView.PatternViewMode.CORRECT, pattern);
                    this.gesture = PatternLockUtils.patternToMD5(patternLockView, pattern);
                    patternLockView.clearPattern();
                    setUiState(3);
                } else {
                    tvTips.setText("至少绘制4个点，请重新设置手势");
                    tvTips.setTextColor(getResources().getColor(R.color.text_red));
                    patternLockView.setViewMode(PatternLockView.PatternViewMode.WRONG);
                }
                break;
            case 3:
                if (pattern != null && pattern.size() > 3) {
                    String gesture = PatternLockUtils.patternToMD5(patternLockView, pattern);
                    if (this.gesture.equals(gesture)) {
                        tvTips.setText("手势设置成功");
                        tvTips.setTextColor(getResources().getColor(R.color.text_green));

                        String userId = UserSession.getUserId(this);
                        GestureLock gestureLock = new GestureLock();
                        gestureLock.setUserId(userId);
                        gestureLock.setGesture(gesture);

                        PublicSqliteHelper sqliteHelper = PublicSqliteHelper.getInstance(this);
                        sqliteHelper.getGestureLockDao().createOrUpdate(gestureLock);

                        setResult(Activity.RESULT_OK);
                        finish();
                    } else {
                        tvTips.setText("与上一次绘制不一致，请重新绘制");
                        tvTips.setTextColor(getResources().getColor(R.color.text_red));
                        patternLockView.setViewMode(PatternLockView.PatternViewMode.WRONG);
                    }
                } else {
                    tvTips.setText("至少绘制4个点，请重新设置手势");
                    tvTips.setTextColor(getResources().getColor(R.color.text_red));
                    patternLockView.setViewMode(PatternLockView.PatternViewMode.WRONG);
                }
                break;
        }
    }

    @Override
    public void onCleared() {

    }

    @Override
    public void finish() {
        super.finish();
        AppLockThread appLock = AppLockThread.getInstance();
        if (appLock != null) {
            appLock.resume();
        }
    }

    @Override
    public void onBackPressed() {
//        super.onBackPressed();
    }
}
