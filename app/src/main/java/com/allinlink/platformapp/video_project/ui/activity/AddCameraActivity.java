package com.allinlink.platformapp.video_project.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.unistrong.utils.StringsUtils;
import com.allinlink.platformapp.R;
import com.allinlink.platformapp.video_project.contract.activity.AddCameraContract;
import com.allinlink.platformapp.video_project.presenter.activity.AddCameraPresenter;
import com.allinlink.platformapp.video_project.utils.LogUtil;
import com.allinlink.platformapp.video_project.widget.EditLinearLayout;
import com.unistrong.view.base.BaseTitleActivity;
import com.unistrong.view.navigationbar.INavigationBarBackListener;
import com.unistrong.view.navigationbar.INavigationBarRightListener;
import com.unistrong.view.navigationbar.NavigationBar;
import com.unistrong.view.navigationbar.NavigationBuilder;
import com.unistrong.view.utils.ToastUtil;

import java.util.ArrayList;

public class AddCameraActivity extends BaseTitleActivity<AddCameraPresenter> implements AddCameraContract.View {
    EditLinearLayout linDeviceCode, linDeviceName, limDeviceIP, linDevicePort, linDeviceWay, linDeviceUserName, linDeviceUserPassword, linDeviceManufacturer, linDevicecChannel, linDeviceOrganization, linDeviceDesc;
    ArrayList<EditLinearLayout> editLinearLayouts = new ArrayList<>();
    private String standardType = "rtsp";

    @Override
    protected NavigationBuilder onCreateNavigationBarByBuilder(NavigationBuilder builder) {
        NavigationBuilder navigationBuilder = builder.setTitle(StringsUtils.getResourceString(R.string.activity_addcamera))
                .setType(NavigationBar.BACK_TITLE_RIGHT).setRight("下一步").setLeftOfRightEnable(true).setNavigationBarListener(new INavigationBarRightListener() {
                    @Override
                    public void onBackClick() {
                        finish();

                    }

                    @Override
                    public void onTitleClick(String title) {

                    }

                    @Override
                    public void onRightClick() {
                        for (EditLinearLayout editLinearLayout : editLinearLayouts) {
                            if (editLinearLayout.checkEmpty()) {
                                ToastUtil.showVerboseToast("请输入" + editLinearLayout.getMsg());
                                return;
                            }
                        }

                        mPresenter.addCamera(linDeviceCode.getText(), linDeviceName.getText(), linDeviceOrganization.getText(), limDeviceIP.getText(), linDevicePort.getText(),
                                linDeviceUserName.getText(), linDeviceUserPassword.getText(), standardType, linDeviceManufacturer.getText());
//                        startActivity(new Intent(AddCameraActivity.this, AddCameraInfoActivity.class));
                    }
                });
        return navigationBuilder;
    }


    @Override
    public View onCreateContentView() {
        View view = LayoutInflater.from(this).inflate(R.layout.activity_add_camera, null);
        initView(view);
        return view;
    }

    private void initView(View view) {
        mPresenter = new AddCameraPresenter(this);
        //查询所有厂商
        mPresenter.findChannelInfoChecked();
        //获取所有机构
        mPresenter.findAllTenanInfoList();
        //摄像机分组管理
        mPresenter.findCreamManage();


        linDeviceCode = view.findViewById(R.id.lin_device_code);
        linDeviceCode.initEdit("设备编号");
        linDeviceName = view.findViewById(R.id.lin_device_name);
        linDeviceName.initEdit("设备名称");
        limDeviceIP = view.findViewById(R.id.lin_device_ip);
        limDeviceIP.initEdit("设备IP");
        linDevicePort = view.findViewById(R.id.lin_device_port);
        linDevicePort.initEdit("设备端口");
        linDeviceUserName = view.findViewById(R.id.lin_device_username);
        linDeviceUserName.initEdit("用户名");
        linDeviceUserPassword = view.findViewById(R.id.lin_device_userpassword);
        linDeviceUserPassword.initEdit("密码");
        linDeviceManufacturer = view.findViewById(R.id.lin_device_manufacturer);
        linDeviceManufacturer.initEdit("厂家");
        linDeviceOrganization = view.findViewById(R.id.lin_device_organization);
                linDeviceOrganization.initEdit("所属组织");
                linDeviceDesc = view.findViewById(R.id.lin_device_desc);
                linDeviceDesc.initEdit("描述");
                editLinearLayouts.add(linDeviceCode);
                editLinearLayouts.add(linDeviceName);
                editLinearLayouts.add(limDeviceIP);
                editLinearLayouts.add(linDevicePort);
                editLinearLayouts.add(linDeviceUserName);
                editLinearLayouts.add(linDeviceUserPassword);
                editLinearLayouts.add(linDeviceManufacturer);
                editLinearLayouts.add(linDeviceOrganization);
                editLinearLayouts.add(linDeviceDesc);

                RadioGroup radioGroup = (RadioGroup) view.findViewById(R.id.standardType);//获取单选按钮组
                //为单选按钮组添加事件监听
                radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(RadioGroup radioGroup, int i) {
                RadioButton RB = (RadioButton) findViewById(i);//获取被选择的单选按钮
                standardType = RB.getText().toString();
                LogUtil.i("TAG", standardType);
            }
        });
    }


    @Override
    public void onError(String msg) {

    }

    @Override
    public Context getContext() {
        return null;
    }

    @Override
    public void addSuccess(String gid) {
        Intent intent = new Intent(AddCameraActivity.this, AddCameraInfoActivity.class);
        intent.putExtra("gid", gid);
        startActivity(intent);
    }
}