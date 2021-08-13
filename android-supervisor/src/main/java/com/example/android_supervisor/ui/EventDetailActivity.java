package com.example.android_supervisor.ui;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.TableRow;
import android.widget.TextView;

import com.example.android_supervisor.R;
import com.example.android_supervisor.common.ResultCallback;
import com.example.android_supervisor.common.UserSession;
import com.example.android_supervisor.entities.Attach;
import com.example.android_supervisor.entities.ConstantEntity;
import com.example.android_supervisor.entities.EventFlowRes;
import com.example.android_supervisor.entities.EventRes;
import com.example.android_supervisor.entities.MapBundle;
import com.example.android_supervisor.http.ServiceGenerator;
import com.example.android_supervisor.http.common.ProgressTransformer;
import com.example.android_supervisor.http.response.Response;
import com.example.android_supervisor.http.response.ResponseObserver;
import com.example.android_supervisor.http.service.QuestionService;
import com.example.android_supervisor.map.AMapManager;
import com.example.android_supervisor.ui.adapter.FlowAdapter;
import com.example.android_supervisor.ui.media.MediaGridView;
import com.example.android_supervisor.ui.media.MediaItem;
import com.example.android_supervisor.ui.view.ProgressText;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;

/**
 * 历史记录 已核实 已核查 已退件 已上报跳转
 */
public class EventDetailActivity extends BaseActivity {
//    @BindView(R.id.tab_layout)
//    TabLayout mTabLayout;

    @BindView(R.id.tr_event_info_title)
    TableRow trTitle;

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

    @BindView(R.id.gv_event_info_attaches)
    MediaGridView gvAttaches;

    @BindView(R.id.gv_event_info_later_attaches)
    MediaGridView gvLaterAttaches;

    @BindView(R.id.btn_event_info_edit)
    FloatingActionButton btnEdit;

    @BindView(R.id.recycler_flow)
    RecyclerView recyclerView;



    private int status;
    private int editType;
    private String id;
    private boolean handled;
    private EventRes eventRes;

    ArrayList<Attach> arrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_detail);
        ButterKnife.bind(this);
        initData();

        gvLaterAttaches.setDrawableHide();
        gvAttaches.setDrawableHide();
        initView();

    }

    private void initView() {

        tvAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();

                MapBundle mapBundle = new MapBundle();
                if (eventRes.getGeoX() != 0.0 && eventRes.getGeoY() != 0.0) {
                    mapBundle.longitude = eventRes.getGeoX();
                    mapBundle.latitude = eventRes.getGeoY();
                }
                mapBundle.isShowGird = false;
                mapBundle.isShowCase = false;
                bundle.putSerializable("mapBundle", mapBundle);

                AMapManager.show(EventDetailActivity.this, bundle, new ResultCallback<Intent>() {
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

    private void initData() {
        eventRes=new EventRes();
        status = getIntent().getIntExtra("status", 1);
        id = getIntent().getStringExtra("id");
        editType = getIntent().getIntExtra("edit", 0);
        handled = getIntent().getBooleanExtra("handled", false);

        arrayList = (ArrayList<Attach>) getIntent().getSerializableExtra("attachs");
        switch (status) {
            case 1:
                ((TextView) trTitle.getChildAt(0)).setText("案件号");
                btnEdit.hide();
                break;
            case 2:
                ((TextView) trTitle.getChildAt(0)).setText("受理号");//多次核实
                btnEdit.show();
                break;
        }
        if (editType == 1)
            btnEdit.show();
        if (editType == 2)
            btnEdit.hide();
        fetchData();
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
                .compose(new ProgressTransformer<Response<EventRes>>(EventDetailActivity.this, ProgressText.load))
                .subscribe(new ResponseObserver<EventRes>(EventDetailActivity.this) {
                    @Override
                    public void onSuccess(EventRes data) {
                        eventRes=data;
                        setEventInfo(data);
                    }
                });
        questionService.getEventFlowList(id).compose(this.<Response<List<EventFlowRes>>>bindToLifecycle())
                .observeOn(AndroidSchedulers.mainThread())
                .compose(new ProgressTransformer<Response<List<EventFlowRes>>>(EventDetailActivity.this, ProgressText.load))
                .subscribe(new ResponseObserver<List<EventFlowRes>>(EventDetailActivity.this) {
                    @Override
                    public void onSuccess(List<EventFlowRes> data) {
                        if (data != null) {
                            FlowAdapter flowAdapter = new FlowAdapter(R.layout.item_my_task_flow, data);
                            recyclerView.setLayoutManager(new LinearLayoutManager(EventDetailActivity.this));
                            recyclerView.setAdapter(flowAdapter);
                        }
                    }
                });

    }

    private void setEventInfo(EventRes data) {
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
        Log.d("data.getSource()", data.getSource() + "");
        switch (data.getSource()) {

            case ConstantEntity.CASE_WEB:
                tvSource.setText(this.getResources().getString(R.string.case_web));
                break;
            case ConstantEntity.CASE_PHONE:
                tvSource.setText(this.getResources().getString(R.string.case_phone));
                break;
            case ConstantEntity.CASE_WECHAT:
                tvSource.setText(this.getResources().getString(R.string.case_wechat));
                break;
            case ConstantEntity.CASE_NORMAL:
                tvSource.setText(this.getResources().getString(R.string.case_normal));
                break;
            case ConstantEntity.CASE_FAST:
                tvSource.setText(this.getResources().getString(R.string.case_fast));
                break;
            case ConstantEntity.CASE_LEADER:
                tvSource.setText(this.getResources().getString(R.string.case_leader));
                break;
            case ConstantEntity.CASE_CZT:
                tvSource.setText(this.getResources().getString(R.string.case_czt));
                break;
            case ConstantEntity.CASE_MASTER:
                tvSource.setText(this.getResources().getString(R.string.case_master));
                break;
            case ConstantEntity.CASE_PUBLIC:
                tvSource.setText(this.getResources().getString(R.string.case_public));
                break;
            default:
                tvSource.setText(this.getResources().getString(R.string.case_normal));
                break;
        }
        tvType.setText(data.getTypeName());
        tvStandard.setText(data.getStandardName());
//        tvTime.setText(DateUtils.format(data.getReportTime()));
        tvTime.setText(data.getReportTime());
        tvArea.setText(data.getAreaName());
        tvAddress.setText(data.getAddress());
        tvDesc.setText(data.getDesc());

        ArrayList<Attach> attaches=new ArrayList<>();
        ArrayList<Attach> attachesLater=new ArrayList<>();//data.getAttaches()
        if (arrayList!=null && arrayList.size()>0){
            for (Attach attach : arrayList) {
                if (attach.getUsage()==3){
                    attachesLater.add(attach);
                }else
                    attaches.add(attach);
            }
        }else {
            if (data.getAttaches()!=null){
                for (Attach attach : data.getAttaches()) {
                    if (attach.getUsage()==3){
                        attachesLater.add(attach);
                    }else
                        attaches.add(attach);
                }
            }
        }


        List<MediaItem> mediaItem = setAttachesView(attaches);
        List<MediaItem> mediaItemLater = setAttachesView(attachesLater);

        setAttachVisibility(mediaItem,gvAttaches);
        setAttachVisibility(mediaItemLater,gvLaterAttaches);
    }

    private void setAttachVisibility(List<MediaItem> mediaItem,MediaGridView view) {

        if (mediaItem!=null){
            view.setVisibility(View.VISIBLE);
            view.clearMediaItems();
            view.addMediaItems(mediaItem);
        } else {
            view.setVisibility(View.GONE);
        }
    }

    private List<MediaItem> setAttachesView(List<Attach> attaches) {
        List<MediaItem> mediaItems = new ArrayList<>();
        if (attaches != null && attaches.size() > 0) {
            for (Attach attach : attaches) {
                MediaItem mediaItem = new MediaItem();
                mediaItem.setType(attach.getType());
                if (!TextUtils.isEmpty(attach.getUrl())) {
                    Uri coverUri = Uri.parse(attach.getUrl());
                    coverUri = getUri(attach.getUrl(), coverUri);
                    mediaItem.setUri(coverUri);
                }
                if (!TextUtils.isEmpty(attach.getThumbUrl())) {
                    Uri thumbUri = Uri.parse(attach.getThumbUrl());
                    thumbUri = getUri(attach.getThumbUrl(), thumbUri);
                    mediaItem.setThumbnailUri(thumbUri);
                }
                mediaItems.add(mediaItem);
            }
        }
        return mediaItems;
    }

    private Uri getUri(String attachUrl, Uri coverUri) {
        if (!attachUrl.contains("http")) {
//            String fileServer = SaveObjectUtils.getInstance(this).getObject("fileServer", null);
            String fileServer =  UserSession.getFileServer(this);
            if (!TextUtils.isEmpty(fileServer)){
                String path = new StringBuilder(fileServer).append(attachUrl).toString();
                coverUri = Uri.parse(path);
            }
        }
        return coverUri;
    }

    @OnClick(R.id.btn_event_info_edit)
    public void onEventEdit(View v) {
        Intent intent = new Intent(this, EventEditActivity.class);
        intent.putExtra("id", id);
        intent.putExtra("status", status);
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

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }
}
