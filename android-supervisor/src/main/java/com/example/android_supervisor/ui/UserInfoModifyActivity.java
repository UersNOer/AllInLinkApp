package com.example.android_supervisor.ui;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.LinearLayout;

import com.arcsoft.arcfacedemo.activity.RegisterAndRecognizeActivity2;
import com.arcsoft.arcfacedemo.model.UserFace;
import com.arcsoft.arcfacedemo.util.FaceUtils;
import com.example.android_supervisor.R;
import com.example.android_supervisor.common.UserSession;
import com.example.android_supervisor.entities.ConstantEntity;
import com.example.android_supervisor.http.oauth.AccessToken;
import com.example.android_supervisor.service.AppServiceManager;
import com.example.android_supervisor.ui.view.MyOneLineView;
import com.example.android_supervisor.utils.DialogUtils;
import com.example.android_supervisor.utils.Environments;
import com.example.android_supervisor.utils.LoginMethodConfig;
import com.xuexiang.xui.widget.button.switchbutton.SwitchButton;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 个人信息修改
 */
public class UserInfoModifyActivity extends BaseActivity implements MyOneLineView.OnRootClickListener, SwitchButton.OnCheckedChangeListener {

    @BindView(R.id.myItem)
    LinearLayout mMyItemView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_info_xg);
        ButterKnife.bind(this);
        initMenu();
    }

    private void initMenu() {
        setItemView1(R.drawable.fix_psw,"修改密码", ConstantEntity.PSW_TAG,"",true);
        setItemView1(R.drawable.setting_face_bing,"绑定人脸", ConstantEntity.FACE_BING_TAG,"",true);

        setItemView(R.drawable.login_phone_code,"手机绑定", ConstantEntity.PHONE_TAG,"",true,LoginMethodConfig.getLoginPhone(this));
        setItemView(R.drawable.finger_printer,"指纹绑定", ConstantEntity.FINGER_TAG,"",true,LoginMethodConfig.getLoginFigger(this));
        setItemView(R.drawable.face_printer,"人脸绑定", ConstantEntity.Face_TAG, "",true,
                LoginMethodConfig.getLoginFace(this));
    }

    private void setItemView(int iconName, String textValue, int tag,String rightValue,boolean isShowToggle,boolean isChecked) {
        MyOneLineView guideItem = new MyOneLineView(this)
                .initMine(iconName, textValue, isShowToggle,tag)
                .setOnRootClickListener(this,tag)
                .isChecked(isChecked)
                .setOnCheckListener(this,tag);

//                .setOnLongClickListener(this,tag);

        mMyItemView.addView(guideItem);
    }



    private void setItemView1(int iconName, String textValue, int tag,String rightValue,boolean isShowArrow) {
        MyOneLineView guideItem = new MyOneLineView(this)
                .initMine(iconName, textValue, rightValue, isShowArrow,tag)
                .setOnRootClickListener(this,tag);
        mMyItemView.addView(guideItem);
    }



    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }

    @Override
    public void onRootClick(View view) {
        Intent intent =null;
        switch ((int)view.getTag()){
            case ConstantEntity.PSW_TAG:
                intent = new Intent(UserInfoModifyActivity.this, PwdModifyActivity.class);
                startActivityForResult(intent, 1);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                break;
            case ConstantEntity.FACE_BING_TAG:
                intent = new Intent(UserInfoModifyActivity.this, RegisterAndRecognizeActivity2.class);
                if (Environments.userBase!=null && Environments.userBase.getUserSupervisorExt()!=null){
                    intent.putExtra("loginName",Environments.userBase.getUserSupervisorExt().getLoginName());
                    intent.putExtra("pwd",Environments.userBase.getPwd());
                    intent.putExtra("isShowRegister",true);
                }
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                break;
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        initFaceData();
    }

    private void initFaceData() {
        //检查是否绑定过
        List<UserFace> userFaces = FaceUtils.getFace(this,UserSession.getUserName(this));
        if (userFaces!=null && userFaces.size()>0){
            for (int i=0;i< mMyItemView.getChildCount();i++){
                MyOneLineView view = (MyOneLineView) mMyItemView.getChildAt(i);
                if(((MyOneLineView) view).getTag().equals(ConstantEntity.FACE_BING_TAG)){
                    ((MyOneLineView) view).setRightText("已经绑定过人脸");
                    mMyItemView.setEnabled(false);
                    mMyItemView.setClickable(false);
                }
            }
        }
    }


//    private void bindFace() {
//        Intent intent = new Intent(UserInfoModifyActivity.this, RegisterAndRecognizeActivity2.class);
//        if (Environments.userBase!=null && Environments.userBase.getUserSupervisorExt()!=null){
//            intent.putExtra("loginName",Environments.userBase.getUserSupervisorExt().getLoginName());
//            intent.putExtra("pwd",Environments.userBase.getPwd());
//            intent.putExtra("isShowRegister",true);
//        }
//        startActivity(intent);
//        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
//    }


    /**
     * @OnClick---->点击事件
     * @OnCheckedChanged ---->选中，取消选中
     * @param compoundButton
     * @param b
     */
    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
        switch ((int)compoundButton.getTag()){
            case ConstantEntity.PHONE_TAG:
                LoginMethodConfig.setLoginPhone(this,b);
                break;
            case ConstantEntity.FINGER_TAG:
                LoginMethodConfig.setLoginFinger(this,b);
                break;
            case ConstantEntity.Face_TAG:
                LoginMethodConfig.setLoginFace(this,b);
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            // 修改密码成功后
            if (requestCode == 1) {
                DialogUtils.show(this, "密码已经修改，需要重新登录", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        toLogin();
                    }
                });
            }
        }
    }

    //此处修改完密码后，跳转到登录页面，但是点击返回，还是会进入项目，参考环卫切换账号
    private void toLogin() {

        AccessToken.getInstance(this).delete();
        UserSession.setUserId(this, "");
        AppServiceManager.stop(this);

        Intent loginIntent = new Intent(this, LoginActivity.class);
        loginIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        this.startActivity(loginIntent);
    }
}
