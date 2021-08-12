package com.example.android_supervisor.ui.fragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AppCompatImageView;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.qmuiteam.qmui.widget.dialog.QMUIBottomSheet;
import com.example.android_supervisor.Presenter.EvtFreqWordsPresenter;
import com.example.android_supervisor.R;
import com.example.android_supervisor.entities.Attach;
import com.example.android_supervisor.entities.EventHandlePara;
import com.example.android_supervisor.entities.EventRes;
import com.example.android_supervisor.entities.EventRollbackPara;
import com.example.android_supervisor.entities.WordRes;
import com.example.android_supervisor.http.ServiceGenerator;
import com.example.android_supervisor.http.common.ProgressTransformer;
import com.example.android_supervisor.http.request.JsonBody;
import com.example.android_supervisor.http.response.Response;
import com.example.android_supervisor.http.response.ResponseObserver;
import com.example.android_supervisor.http.service.QuestionService;
import com.example.android_supervisor.ui.WordHandleTask;
import com.example.android_supervisor.ui.media.MediaUploadGridView;
import com.example.android_supervisor.ui.view.ProgressText;
import com.example.android_supervisor.utils.DialogUtils;
import com.example.android_supervisor.utils.SystemUtils;
import com.example.android_supervisor.utils.ToastUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;

/**
 * @author 个人任务处理  抽查
 */
public class TaskHandleFragment extends BaseFragment {
    @BindView(R.id.tv_task_handle_type)
    TextView tvType;

    @BindView(R.id.tv_commonReply)
    AppCompatImageView tv_commonReply;


    @BindView(R.id.ll_task_handle_opinion)
    LinearLayout llOpinion;

    @BindView(R.id.et_task_handle_opinion)
    EditText etOpinion;

    @BindView(R.id.ll_task_handle_reason)
    LinearLayout llReason;

    @BindView(R.id.et_task_handle_reason)
    EditText etReason;

    @BindView(R.id.gv_task_handle_attaches)
    MediaUploadGridView gvAttaches;

    @BindView(R.id.fl_radio)
    LinearLayout fl_radio;

    @BindView(R.id.ib_addword)
    AppCompatImageView ib_addword;


    private int status;
    private String id;
    private int handleType;
    private EventRes taskRes;

    private EventHandlePara handlePara = new EventHandlePara();


    CharSequence[] items;

    public static TaskHandleFragment newInstance(int status, String id) {
        TaskHandleFragment fragment = new TaskHandleFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("status", status);
        bundle.putString("id", id);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_task_handle, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);

        Bundle bundle = getArguments();
        if (bundle != null) {
            this.status = bundle.getInt("status");
            this.id = bundle.getString("id");
        }
        initHandleUi();
        fetchData();

        tv_commonReply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                QMUIBottomSheet.BottomListSheetBuilder builder = new QMUIBottomSheet.BottomListSheetBuilder(getContext());
                EvtFreqWordsPresenter presenter = new EvtFreqWordsPresenter();
                presenter.getWords(getContext(), new EvtFreqWordsPresenter.EvtFreqWordsCallBack() {
                    @Override
                    public void onSuccess(List<WordRes> data) {
                        for (WordRes res : data) {
                            builder.addItem(res.getContent(), res.getContent());
                        }

                        //builder.setIsCenter(true);
                        builder.setOnSheetItemClickListener(new QMUIBottomSheet.BottomListSheetBuilder.OnSheetItemClickListener() {
                            @Override
                            public void onClick(QMUIBottomSheet dialog, View itemView, int position, String tag) {
                                etOpinion.setText(tag);
                                dialog.dismiss();
                            }
                        });
                        builder.build().show();
                    }

                    @Override
                    public void onError() {

                    }
                });


            }
        });
    }

    private void initHandleUi() {
        RadioGroup radioGroup = new RadioGroup(getContext());
        radioGroup.setOrientation(LinearLayout.HORIZONTAL);
        radioGroup.setWeightSum(3);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        radioGroup.setLayoutParams(layoutParams);
        if (status == 1) {
            items = new String[]{"通过", "不通过", "退件"};
            addRadioButton(radioGroup, items);
        } else {
            items = new String[]{"属实", "不属实", "退件"};
            addRadioButton(radioGroup, items);
        }
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                for (int i = 0; i < group.getChildCount(); i++) {
                    RadioButton rb = (RadioButton) radioGroup.getChildAt(i);
                    if (rb.isChecked()) {
                        setHandleType(i, items[i]);
                        break;
                    }
                }
            }
        });

    }


    public void addRadioButton(RadioGroup radioGroup, CharSequence[] items) {
        for (int i = 0; i < items.length; i++) {
            RadioButton radioButton = new RadioButton(getContext());
            radioButton.setText(items[i]);
            RadioGroup.LayoutParams layoutParams = new RadioGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            layoutParams.weight = 1;
            layoutParams.gravity = Gravity.CENTER;
            radioButton.setLayoutParams(layoutParams);
            radioButton.setGravity(Gravity.CENTER);
            radioGroup.addView(radioButton);

            radioButton.setChecked((i == 0 ? true : false));
        }

        fl_radio.addView(radioGroup);
    }

    private void fetchData() {
        QuestionService questionService = ServiceGenerator.create(QuestionService.class);
        Observable<Response<EventRes>> observable;
        switch (status) {
            case 1:
                observable = questionService.getHcTaskById(id);
                break;
            case 2:
                observable = questionService.getHsTaskById(id);
                break;
            default:
                return;
        }
        observable.compose(this.<Response<EventRes>>bindToLifecycle())
                .observeOn(AndroidSchedulers.mainThread())
                .compose(new ProgressTransformer<Response<EventRes>>(getContext(), ProgressText.load))
                .subscribe(new ResponseObserver<EventRes>(getContext()) {
                    @Override
                    public void onSuccess(EventRes data) {
                        taskRes = data;
                    }
                });
    }

    @OnClick(R.id.ll_task_handle_type)
    public void onTypeClick(View v) {
        final CharSequence[] items;
        if (status == 1) {
            items = new String[]{"通过", "不通过", "退件"};
        } else {
            items = new String[]{"属实", "不属实", "退件"};
        }
        new AlertDialog.Builder(getContext())
                .setTitle("办理类型")
                .setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        setHandleType(which, items[which]);
                    }
                }).show();
    }

    public void setHandleType(int handleType, CharSequence text) {
        this.handleType = handleType;
        switch (handleType) {
            case 0:
                tvType.setText(text);
                llReason.setVisibility(View.GONE);
                llOpinion.setVisibility(View.VISIBLE);
                gvAttaches.setVisibility(View.VISIBLE);
                break;
            case 1:
                tvType.setText(text);
                llReason.setVisibility(View.GONE);
                llOpinion.setVisibility(View.VISIBLE);
                gvAttaches.setVisibility(View.VISIBLE);
                break;
            case 2:
                tvType.setText(text);
                llReason.setVisibility(View.VISIBLE);
                llOpinion.setVisibility(View.GONE);
                gvAttaches.setVisibility(View.GONE);
                break;
        }
    }


    @OnClick(R.id.ib_addword)
    public void onAddword(View view) {
        startActivityForResult(new Intent(getContext(), WordHandleTask.class),0);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK){
            if (requestCode == 0 ){
                WordRes wordRes = (WordRes) data.getSerializableExtra("WordRes");
                if (wordRes!=null){
                    etOpinion.setText(wordRes.getContent());
                }
            }
        }
    }

    @OnClick(R.id.btn_task_handle_submit)
    public void onSubmit(View v) {
        SystemUtils.hideSoftInput(getContext(), v);

        switch (handleType) {
            case 0:
                commitReply();
                break;
            case 1:
                commitReply();
                break;
            case 2:
                rollback();
                break;
            default:
                ToastUtils.show(getContext(), "请选择办理类型");
                break;
        }
    }

    @SuppressLint("StaticFieldLeak")
    private void commitReply() {
        if (taskRes == null) {
            return;
        }
        final String opinion = etOpinion.getText().toString();
        if (TextUtils.isEmpty(opinion)) {
            ToastUtils.show(getContext(), "【办理意见】不能为空");
            return;
        }
        if (gvAttaches.isEmpty()) {
            ToastUtils.show(getContext(), "请添加附件");
            return;
        }
        DialogUtils.askYesNo(getContext(), "办理案件", "您确定办理此案件？", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                handleMyTask();
            }
        }, null);
    }

    @SuppressWarnings("unchecked")
    private void handleMyTask() {
        handlePara = new EventHandlePara();
        handlePara.setEventId(taskRes.getId());//evtid
        handlePara.setAcceptId(taskRes.getId());
        handlePara.setActInstId(taskRes.getActInstId());
        handlePara.setProcDefId(taskRes.getProcDefId());
        handlePara.setProcInsId(taskRes.getProcInstId());
        handlePara.setComment(etOpinion.getText().toString());
        handlePara.setStatus(getHandleStatus());

        handlePara.setCaseTitle(status == 1 ? taskRes.getEventCode() : taskRes.getAcceptCode());

        Map<String, Object> params = new HashMap<>();
        params.put("actDefId", taskRes.getActDefId());
        handlePara.setParams(params);

        ArrayList<Attach> attaches = new ArrayList<>();
        gvAttaches.getAttach(gvAttaches.getCanUploadAttach(), attaches, 0);
        handlePara.setUmEvtAttchList(attaches);


        QuestionService questionService = ServiceGenerator.create(QuestionService.class);
        Observable<Response> observable;
        switch (status) {
            case 1:
                handlePara.setEvtid(taskRes.getId());
                observable = questionService.handleHcTask(new JsonBody(handlePara));
                break;
            case 2:
                observable = questionService.handleHsTask(new JsonBody(handlePara));
                break;
            default:
                throw new IllegalArgumentException("Unknown.");
        }

        observable.compose(this.<Response>bindToLifecycle())
                .observeOn(AndroidSchedulers.mainThread())
                .compose(new ProgressTransformer<Response>(getContext(), ProgressText.submit))
                .subscribe(new ResponseObserver(getContext()) {
                    @Override
                    public void onSuccess(Object data) {
                        DialogUtils.show(getContext(), "案件已成功办理", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Activity activity = getActivity();
                                if (activity != null) {
                                    activity.setResult(Activity.RESULT_OK);
                                    activity.finish();
                                }
                            }
                        });
                    }
                });
    }

    private int getHandleStatus() {
        int handleStatus = -1;
        if (status == 1) { // 核查
            if (handleType == 0) {
                handleStatus = 3;
            } else if (handleType == 1) {
                handleStatus = 4;
            }
        } else if (status == 2) { // 核实
            if (handleType == 0) {
                handleStatus = 0;
            } else if (handleType == 1) {
                handleStatus = 1;
            }
        }
        return handleStatus;
    }

    @SuppressLint("StaticFieldLeak")
    private void rollback() {
        if (taskRes == null) {
            return;
        }
        final String reason = etReason.getText().toString();
        if (TextUtils.isEmpty(reason)) {
            ToastUtils.show(getContext(), "【退件原因】不能为空");
            return;
        }
        DialogUtils.askYesNo(getContext(), "回退案件", "您确定回退此案件？", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                rollbackMyTask();
            }
        }, null);
    }

    @SuppressWarnings("unchecked")
    private void rollbackMyTask() {
        EventRollbackPara para = new EventRollbackPara();
        para.setId(taskRes.getId());
        para.setAcceptId(taskRes.getId());
        para.setActInstId(taskRes.getActInstId());
        para.setProcInstId(taskRes.getProcInstId());
        para.setCurrentLink(taskRes.getCurrentLink());
        para.setComment(etReason.getText().toString());

        para.setCaseTitle(status == 1 ? taskRes.getEventCode() : taskRes.getAcceptCode());

        QuestionService questionService = ServiceGenerator.create(QuestionService.class);
        Observable<Response> observable = questionService.rollback(new JsonBody(para));
        observable.compose(this.<Response>bindToLifecycle())
                .observeOn(AndroidSchedulers.mainThread())
                .compose(new ProgressTransformer<Response>(getContext(), ProgressText.submit))
                .subscribe(new ResponseObserver(getContext()) {
                    @Override
                    public void onSuccess(Object data) {
                        DialogUtils.show(getContext(), "案件已回退", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Activity activity = getActivity();
                                if (activity != null) {
                                    activity.setResult(Activity.RESULT_OK);
                                    activity.finish();
                                }
                            }
                        });
                    }
                });
    }
}
