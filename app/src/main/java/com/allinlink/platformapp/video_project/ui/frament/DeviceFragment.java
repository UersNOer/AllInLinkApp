package com.allinlink.platformapp.video_project.ui.frament;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.unistrong.utils.RxBus;
import com.allinlink.platformapp.R;
import com.allinlink.platformapp.video_project.bean.CameraBean;
import com.allinlink.platformapp.video_project.bean.ChannelBean;
import com.allinlink.platformapp.video_project.bean.CurrentBean;
import com.allinlink.platformapp.video_project.config.Configs;
import com.allinlink.platformapp.video_project.contract.fragment.DeviceFragmentContract;
import com.allinlink.platformapp.video_project.presenter.fragment.DeviceFragmentPresenter;
import com.allinlink.platformapp.video_project.ui.activity.CollectActivity;
import com.allinlink.platformapp.video_project.ui.activity.SearchCameraActivity;
import com.allinlink.platformapp.video_project.ui.adapter.CheckVideoAdapter;
import com.allinlink.platformapp.video_project.widget.MultiSampleVideo;
import com.unistrong.view.base.BaseFragment;

import java.util.ArrayList;
import java.util.HashMap;

import rx.Subscription;

public class DeviceFragment extends BaseFragment<DeviceFragmentPresenter> implements DeviceFragmentContract.View, View.OnClickListener, View.OnTouchListener, MultiSampleVideo.VideoControl {
    private RecyclerView xRecyclerView;//播放器视图列表
    private CheckVideoAdapter adapter;//播放器适配器
    private ArrayList<ChannelBean> cameraList = new ArrayList<>();//总的通道数据列表
    private HashMap<String, ChannelBean> selectedCameraMap = new HashMap<>();//选中的通道数据列表
    private CheckBox check_all, check_one, check_two, check_three, check_four;//复选框
    private ImageView ivControlUp, ivControlDown, ivControlLeft, ivControlRight, ivTitleRight, ivZoomOut, ivZoomIn, ivControlStop;//云台控制按钮
    private TextView tvControlPreset;//云台控制按钮
    private boolean isCheckFlag = true;//全部选中情况下，单独取消某一个时将全部按钮状态变为未选中，但是不改变其他已选中按钮状态
    //    ArrayList<CheckBox> cbList = new ArrayList<>();
    private boolean videoFlag = false;

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    protected View onCreateContentView() {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_device, null);

        view.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });
        initView(view);
        setViewListener(view);
        return view;
    }

    private void initView(View view) {
        mPresenter = new DeviceFragmentPresenter(this);
        xRecyclerView = view.findViewById(R.id.rv_current);
        check_all = view.findViewById(R.id.check_all);
        check_one = view.findViewById(R.id.check_one);
        check_two = view.findViewById(R.id.check_two);
        check_three = view.findViewById(R.id.check_three);
        check_four = view.findViewById(R.id.check_four);

        ivControlUp = view.findViewById(R.id.iv_control_up);
        ivControlDown = view.findViewById(R.id.iv_control_down);
        ivControlLeft = view.findViewById(R.id.iv_control_left);
        ivControlRight = view.findViewById(R.id.iv_control_right);

        ivZoomOut = view.findViewById(R.id.iv_zoom_out);
        ivZoomIn = view.findViewById(R.id.iv_zoom_in);
        tvControlPreset = view.findViewById(R.id.tv_control_preset);
        ivControlStop = view.findViewById(R.id.iv_control_stop);


        ivTitleRight = view.findViewById(R.id.iv_title_right);
        xRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
        adapter = new CheckVideoAdapter(getActivity());
        adapter.setVideoControl(this);
        xRecyclerView.setAdapter(adapter);

    }

    private void setViewListener(View view) {
        view.findViewById(R.id.iv_collect).setOnClickListener(this);
        ivZoomOut.setOnClickListener(this);
        ivZoomIn.setOnClickListener(this);
        ivTitleRight.setOnClickListener(this);
        ivControlUp.setOnClickListener(this);
        ivControlDown.setOnClickListener(this);
        ivControlLeft.setOnClickListener(this);
        ivControlRight.setOnClickListener(this);
        ivZoomOut.setOnTouchListener(this);
        ivZoomIn.setOnTouchListener(this);
        ivControlUp.setOnTouchListener(this);
        ivControlDown.setOnTouchListener(this);
        ivControlLeft.setOnTouchListener(this);
        ivControlRight.setOnTouchListener(this);

        ivControlStop.setOnClickListener(this);
        tvControlPreset.setOnClickListener(this);
        check_all.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    isCheckFlag = true;
                    if (cameraList.size() == 4) {
                        check_one.setChecked(true);
                        check_two.setChecked(true);
                        check_three.setChecked(true);
                        check_four.setChecked(true);
                        selectedCameraMap.put("one", cameraList.get(0));
                        selectedCameraMap.put("two", cameraList.get(1));
                        selectedCameraMap.put("three", cameraList.get(2));
                        selectedCameraMap.put("four", cameraList.get(3));
                    } else if (cameraList.size() == 3) {
                        check_one.setChecked(true);
                        check_two.setChecked(true);
                        check_three.setChecked(true);
                        selectedCameraMap.put("one", cameraList.get(0));
                        selectedCameraMap.put("two", cameraList.get(1));
                        selectedCameraMap.put("three", cameraList.get(2));
                    } else if (cameraList.size() == 2) {
                        check_one.setChecked(true);
                        check_two.setChecked(true);
                        selectedCameraMap.put("one", cameraList.get(0));
                        selectedCameraMap.put("two", cameraList.get(1));
                    } else if (cameraList.size() == 1) {
                        check_one.setChecked(true);
                        selectedCameraMap.put("one", cameraList.get(0));
                    }
                } else {
                    if (isCheckFlag) {
                        check_one.setChecked(false);
                        check_two.setChecked(false);
                        check_three.setChecked(false);
                        check_four.setChecked(false);
                        selectedCameraMap.remove("one");
                        selectedCameraMap.remove("two");
                        selectedCameraMap.remove("three");
                        selectedCameraMap.remove("four");
                    }
                }
            }
        });
        check_one.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    if (cameraList.size() > 0) {
                        selectedCameraMap.put("one", cameraList.get(0));
                        if (selectedCameraMap.size() == cameraList.size()) {
                            check_all.setChecked(true);
                        }
                    } else {
                        check_one.setChecked(false);
                    }
                } else {
                    selectedCameraMap.remove("one");
                    isCheckFlag = false;
                    check_all.setChecked(false);
                }
            }
        });
        check_two.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    if (cameraList.size() >= 2) {
                        selectedCameraMap.put("two", cameraList.get(1));
                        if (selectedCameraMap.size() == cameraList.size()) {
                            check_all.setChecked(true);
                        }
                    } else {
                        check_two.setChecked(false);
                    }
                } else {
                    selectedCameraMap.remove("two");
                    isCheckFlag = false;
                    check_all.setChecked(false);
                }
            }
        });
        check_three.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    if (cameraList.size() >= 3) {
                        selectedCameraMap.put("three", cameraList.get(2));
                        if (selectedCameraMap.size() == cameraList.size()) {
                            check_all.setChecked(true);
                        }
                    } else {
                        check_three.setChecked(false);
                    }
                } else {
                    selectedCameraMap.remove("three");
                    isCheckFlag = false;
                    check_all.setChecked(false);
                }
            }
        });
        check_four.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    if (cameraList.size() >= 4) {
                        selectedCameraMap.put("four", cameraList.get(3));
                        if (selectedCameraMap.size() == cameraList.size()) {
                            check_all.setChecked(true);
                        }
                    } else {
                        check_four.setChecked(false);
                    }
                } else {
                    selectedCameraMap.remove("four");
                    isCheckFlag = false;
                    check_all.setChecked(false);
                }
            }
        });

    }

    @Override
    public void onError(String msg) {

    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        if (hidden) {
            //相当于Fragment的onPause
            adapter.onPause();
            videoFlag = false;
        } else {
            // 相当于Fragment的onResume


        }
//        case R.id.iv_control_up:
//        mPresenter.cloundControl(selectedCameraMap, Configs.BST_PTZ_UP);
//        break;
//        case R.id.iv_control_right:
//        mPresenter.cloundControl(selectedCameraMap, Configs.BST_PTZ_RIGHT);
//        break;
//        case R.id.iv_control_down:
//        mPresenter.cloundControl(selectedCameraMap, Configs.BST_PTZ_DOWN);
//        break;
//        case R.id.iv_control_left:
//        mPresenter.cloundControl(selectedCameraMap, Configs.BST_PTZ_LEFT);
//        break;
    }

    @Override
    public void onPause() {
        adapter.onPause();
        videoFlag = false;
        super.onPause();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_collect:
                Intent intent1 = new Intent(getContext(), CollectActivity.class);
                intent1.putExtra("max", 4 - adapter.getSize());
                startActivity(intent1);
                break;
            case R.id.iv_title_right:
                Intent intent = new Intent(getContext(), SearchCameraActivity.class);
                intent.putExtra("mode", 1);
                intent.putExtra("max", 4 - adapter.getSize());
                startActivity(intent);
                break;

//            case R.id.iv_zoom_out:
//                mPresenter.cloundControl(selectedCameraMap, Configs.BST_PTZ_ZOOM_OUT);
//                break;
//            case R.id.iv_zoom_in:
//                mPresenter.cloundControl(selectedCameraMap, Configs.BST_PTZ_ZOOM_IN);
//                break;
            case R.id.iv_control_stop:
                if (cameraList.size() == 0) {
                    return;
                }
                adapter.startPlay();

                break;
            case R.id.tv_control_preset:
                mPresenter.cloundControl(selectedCameraMap, Configs.BST_PTZ_GOTO_PRESET);
                break;
        }

    }

    @Override
    public void selctCameraData(ChannelBean datasBean) {
        cameraList.add(0,datasBean);
        adapter.setCameraData(cameraList);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            switch (v.getId()) {
                case R.id.iv_control_up:
                    mPresenter.cloundControl(selectedCameraMap, Configs.BST_PTZ_UP);
                    break;
                case R.id.iv_control_right:
                    mPresenter.cloundControl(selectedCameraMap, Configs.BST_PTZ_RIGHT);
                    break;
                case R.id.iv_control_down:
                    mPresenter.cloundControl(selectedCameraMap, Configs.BST_PTZ_DOWN);
                    break;
                case R.id.iv_control_left:
                    mPresenter.cloundControl(selectedCameraMap, Configs.BST_PTZ_LEFT);
                    break;
                case R.id.iv_zoom_out:
                    mPresenter.cloundControl(selectedCameraMap, Configs.BST_PTZ_ZOOM_OUT);
                    break;
                case R.id.iv_zoom_in:
                    mPresenter.cloundControl(selectedCameraMap, Configs.BST_PTZ_ZOOM_IN);
                    break;
            }
        } else if (event.getAction() == MotionEvent.ACTION_UP) {
            mPresenter.stop(selectedCameraMap);
        }
        return false;
    }

    @Override
    public void sendControl(String gid, String cmd) {
        mPresenter.sendControl(gid, cmd);
    }
}
