package com.example.android_supervisor.ui.fragment;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableRow;
import android.widget.TextView;

import com.example.android_supervisor.R;
import com.example.android_supervisor.common.ResultCallback;
import com.example.android_supervisor.common.UserSession;
import com.example.android_supervisor.entities.Attach;
import com.example.android_supervisor.entities.CaseLevel;
import com.example.android_supervisor.entities.EventRes;
import com.example.android_supervisor.entities.EventType;
import com.example.android_supervisor.entities.MapBundle;
import com.example.android_supervisor.http.ServiceGenerator;
import com.example.android_supervisor.http.common.ProgressTransformer;
import com.example.android_supervisor.http.response.Response;
import com.example.android_supervisor.http.response.ResponseObserver;
import com.example.android_supervisor.http.service.QuestionService;
import com.example.android_supervisor.map.AMapManager;
import com.example.android_supervisor.sqlite.PublicSqliteHelper;
import com.example.android_supervisor.ui.EventEditActivity;
import com.example.android_supervisor.ui.media.MediaGridView;
import com.example.android_supervisor.ui.media.MediaItem;
import com.example.android_supervisor.ui.view.ProgressText;
import com.example.android_supervisor.utils.CaseBindUtils;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;

/**
 * @author wujie
 */
public class EventInfoFragment extends BaseFragment {
    @BindView(R.id.tr_event_info_title)
    TableRow trTitle;

    @BindView(R.id.tv_bg)
    TextView tv_bg;


    @BindView(R.id.tv_event_info_title)
    TextView tvTitle;

    @BindView(R.id.tv_event_info_db)
    TextView tvDB;

    @BindView(R.id.tv_event_info_cb)
    TextView tvCB;

    @BindView(R.id.tv_event_info_source)
    TextView tvSource;

    @BindView(R.id.tv_event_info_type)
    TextView tvType;

    @BindView(R.id.tv_event_info_big)
    TextView tvBig;

    @BindView(R.id.tv_event_info_small)
    TextView tvSmall;

    @BindView(R.id.tv_event_info_level)
    TextView tvLevel;


    @BindView(R.id.tv_event_info_standard)
    TextView tvStandard;

    @BindView(R.id.tv_event_info_time)
    TextView tvTime;

    @BindView(R.id.tv_event_info_area)
    TextView tvArea;

    @BindView(R.id.tv_event_info_address)
    TextView tvAddress;

    @BindView(R.id.tv_event_info_desc)
    TextView tvDesc;

    @BindView(R.id.tr_name)
    TableRow tr_name;
    @BindView(R.id.tr_taskarea)
    TableRow tr_taskarea;
    @BindView(R.id.tr_tasktype)
    TableRow tr_tasktype;


    @BindView(R.id.gv_event_info_attaches)
    MediaGridView gvAttaches;

    @BindView(R.id.btn_event_info_edit)
    FloatingActionButton btnEdit;

    private int status;
    private String id;
    private boolean handled;
    int taskStatus;

    public static EventInfoFragment newInstance(int status, String id, boolean handled) {
        EventInfoFragment fragment = new EventInfoFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("status", status);
        bundle.putString("id", id);
        bundle.putBoolean("handled", handled);
        fragment.setArguments(bundle);
        return fragment;
    }

    public static EventInfoFragment newInstance(int status, String id, boolean handled,int taskStatus) {
        EventInfoFragment fragment = new EventInfoFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("status", status);
        bundle.putString("id", id);
        bundle.putBoolean("handled", handled);
        bundle.putInt("taskStatus", taskStatus);
        fragment.setArguments(bundle);
        return fragment;
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_event_info, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        Bundle bundle = getArguments();
        if (bundle != null) {
            this.status = bundle.getInt("status");
            this.id = bundle.getString("id");
            this.handled = bundle.getBoolean("handled");
            this.taskStatus = bundle.getInt("taskStatus");
            if (taskStatus == 0){
                btnEdit.setVisibility(View.GONE);
                tr_name.setVisibility(View.GONE);
                tr_taskarea.setVisibility(View.GONE);
                tr_tasktype.setVisibility(View.GONE);
                tv_bg.setVisibility(View.GONE);
            }
        }
        switch (status) {
            case 1:
                ((TextView) trTitle.getChildAt(0)).setText("案件号");
                btnEdit.hide();
                break;
            case 2:
                ((TextView) trTitle.getChildAt(0)).setText("受理号");
                btnEdit.show();
                break;
        }
        setUi();
        fetchData();
    }

    private void setUi() {

    }

    private void fetchData() {
        QuestionService questionService = ServiceGenerator.create(QuestionService.class);
        Observable<Response<EventRes>> observable;
        switch (status) {
            case 1:
                if (!handled) {
                    observable = questionService.getHcTaskById(id);
                } else {
                    observable = questionService.getHcTaskById2(id);
                }
                break;
            case 2:
                if (!handled) {
                    observable = questionService.getHsTaskById(id);
                } else {
                    observable = questionService.getHsTaskById2(id);
                }
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
                        setEventInfo(data);
                    }
                });
    }

    EventRes data;
    private void setEventInfo(EventRes data) {
        this.data  = data;
        switch (status) {
            case 1:
                tvTitle.setText(data.getTitle());
                break;
            case 2:
                tvTitle.setText(data.getAcceptCode());
                break;
        }
        if (!handled) {
            if (data.isDB()) {
                tvDB.setVisibility(View.VISIBLE);
            } else {
                tvDB.setVisibility(View.GONE);
            }
            if (data.isCB()) {
                tvCB.setVisibility(View.VISIBLE);
            } else {
                tvCB.setVisibility(View.GONE);
            }
        }

        try{
            tvSource.setText(CaseBindUtils.getSourceNameByCode(getContext(),String.valueOf(data.getSource())));
        }catch (Exception e){
            e.printStackTrace();
        }


        tvSmall.setText(data.getTypeName());

        EventType eventType_small = CaseBindUtils.CaseById(getActivity(), data.getTypeId());
        if(eventType_small!=null){
            EventType eventType_big = CaseBindUtils.CaseById(getActivity(), eventType_small.getPid());
            if (eventType_big!=null){
                tvBig.setText(eventType_big.getName());
                EventType eventType_type = CaseBindUtils.CaseById(getActivity(), eventType_big.getPid());
                if (eventType_type!=null){
                    tvType.setText(eventType_type.getName());
                }
            }
        }
        //修改案件类型
        List<CaseLevel> eventType_levels =  PublicSqliteHelper.getInstance(getContext()).getCaseLevelDao().queryForAll();
        if (eventType_levels!=null&& eventType_levels.size()>0){
            for (CaseLevel caseLevel:eventType_levels) {
                if (caseLevel.getDictCode().equals(String.valueOf(data.getLevel()))){
                    tvLevel.setText(caseLevel.getDictName());
                    break;
                }
            }
        }

        tvStandard.setText(data.getStandardName());
//        tvTime.setText(DateUtils.format(data.getReportTime()));
        tvTime.setText(data.getReportTime());
        tvArea.setText(data.getAreaName());
        tvAddress.setText(data.getAddress());
        tvDesc.setText(data.getDesc());

        if (data.getAttaches() != null && data.getAttaches().size() > 0) {
            List<MediaItem> mediaItems = new ArrayList<>();
            for (Attach attach : data.getAttaches()) {
                MediaItem mediaItem = new MediaItem();
                mediaItem.setType(attach.getType());
                if (!TextUtils.isEmpty(attach.getUrl())) {
                    Uri coverUri = Uri.parse(attach.getUrl());
                    coverUri = getUri(attach.getUrl(), coverUri);
                    mediaItem.setUri(coverUri);
                }
                if (!TextUtils.isEmpty(attach.getThumbUrl())) {
                    Uri coverUri = Uri.parse(attach.getThumbUrl());
                    coverUri = getUri(attach.getUrl(), coverUri);
                    mediaItem.setThumbnailUri(coverUri);
                }else {
                    Uri coverUri = Uri.parse(attach.getUrl());
                    coverUri = getUri(attach.getUrl(), coverUri);
                    mediaItem.setThumbnailUri(coverUri);
                }
                mediaItems.add(mediaItem);
            }
            gvAttaches.setVisibility(View.VISIBLE);
            gvAttaches.addMediaItems(mediaItems);
        } else {
            gvAttaches.setVisibility(View.GONE);
        }

        tvAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle  = new Bundle();

                MapBundle mapBundle = new MapBundle();
                if (data.getGeoX()!=0.0 && data.getGeoY()!=0.0){
                    mapBundle.longitude  = data.getGeoX();
                    mapBundle.latitude  = data.getGeoY();
                }
                mapBundle.isShowGird = true;
                mapBundle.isShowCase = false;
                mapBundle.isCurrenTask = true;
                mapBundle.eventRes = data;

                bundle.putSerializable("mapBundle",mapBundle);

                AMapManager.show(getContext(), bundle, new ResultCallback<Intent>() {
                    @Override
                    public void onResult(Intent result, int tag) {
                    }

                    @Override
                    public void onResult(Intent result) {

                    }
                });
            }
        });
    }

    private Uri getUri(String attachUrl, Uri coverUri) {
        if (!attachUrl.contains("http")) {
            String fileServer = UserSession.getFileServer(getContext());
            if (!TextUtils.isEmpty(fileServer)){
                String path = new StringBuilder(fileServer).append(attachUrl).toString();
                coverUri = Uri.parse(path);
            }
        }
        return coverUri;
    }

    @OnClick(R.id.btn_event_info_edit)
    public void onEventEdit(View v) {
        Intent intent = new Intent(getContext(), EventEditActivity.class);
        intent.putExtra("id", id);
        intent.putExtra("status", status);
        intent.putExtra("areaCode", data==null?"":data.getAreaCode());
        startActivityForResult(intent, 1);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == 1) {
                fetchData();
            }
        }
    }
}
