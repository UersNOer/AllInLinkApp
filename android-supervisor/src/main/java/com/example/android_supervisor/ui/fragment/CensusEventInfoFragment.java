package com.example.android_supervisor.ui.fragment;

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
import com.example.android_supervisor.entities.Attach;
import com.example.android_supervisor.entities.CensusEventRes;
import com.example.android_supervisor.http.ServiceGenerator;
import com.example.android_supervisor.http.common.ProgressTransformer;
import com.example.android_supervisor.http.response.Response;
import com.example.android_supervisor.http.response.ResponseObserver;
import com.example.android_supervisor.http.service.QuestionService;
import com.example.android_supervisor.ui.media.MediaGridView;
import com.example.android_supervisor.ui.media.MediaItem;
import com.example.android_supervisor.ui.view.ProgressText;
import com.example.android_supervisor.utils.CaseBindUtils;
import com.example.android_supervisor.utils.FileServerUtils;
import com.example.android_supervisor.utils.LogUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;

/**
 * 专项普查 任务详情
 */
public class CensusEventInfoFragment extends BaseFragment {
    @BindView(R.id.tv_event_info_title)
    TextView tvTitle;

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

    @BindView(R.id.tv_event_info_taskName)
    TextView tv_taskName;

    @BindView(R.id.tv_event_info_tasktype)
    TextView tv_taskType;

    @BindView(R.id.tv_event_info_taskarea)
    TextView tv_taskArea;

    @BindView(R.id.tr_event_info_big)
    TableRow tr_event_info_big;

    @BindView(R.id.tr_event_info_small)
    TableRow tr_event_info_small;

    @BindView(R.id.tr_event_info_level)
    TableRow tr_event_info_level;

    @BindView(R.id.tr_event_info_standard)
    TableRow tr_event_info_standard;



    private String id;
    ArrayList<Attach> attachs;

    public static CensusEventInfoFragment newInstance(String id) {
        CensusEventInfoFragment fragment = new CensusEventInfoFragment();
        Bundle bundle = new Bundle();
        bundle.putString("id", id);
        fragment.setArguments(bundle);
        return fragment;
    }

    public static CensusEventInfoFragment newInstance(String id,ArrayList<Attach> attachs) {
        CensusEventInfoFragment fragment = new CensusEventInfoFragment();
        Bundle bundle = new Bundle();
        bundle.putString("id", id);
        bundle.putSerializable("attachs",attachs);
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
            this.id = bundle.getString("id");
            this.attachs = (ArrayList<Attach>) bundle.getSerializable("attachs");
            LogUtils.d("CensusEventInfoFragment：","id == " +id);
        }
        setUI();
        fetchData();
    }

    private void setUI() {
        tr_event_info_big.setVisibility(View.GONE);
        tr_event_info_small.setVisibility(View.GONE);
        tr_event_info_level.setVisibility(View.GONE);
        tr_event_info_standard.setVisibility(View.GONE);
    }

    private void fetchData() {
        QuestionService questionService = ServiceGenerator.create(QuestionService.class);
        Observable<Response<CensusEventRes>> observable = questionService.getCensusEventById(id);
        observable.compose(this.<Response<CensusEventRes>>bindToLifecycle())
                .observeOn(AndroidSchedulers.mainThread())
                .compose(new ProgressTransformer<Response<CensusEventRes>>(getContext(), ProgressText.load))
                .subscribe(new ResponseObserver<CensusEventRes>(getContext()) {
                    @Override
                    public void onSuccess(CensusEventRes data) {
                        setEventInfo(data);
                    }
                });
    }

    public void setEventInfo(CensusEventRes data) {
        tvTitle.setText(data.getEventCode());

        tvSource.setText(CaseBindUtils.getSourceNameByCode(getContext(),data.getSource()));
        tvType.setText(data.getTypeName());
        tvStandard.setText(data.getStandardName());
//        tvTime.setText(DateUtils.format(data.getReportTime()));
        tvTime.setText(data.getReportTime());
        tvArea.setText(data.getAreaName());
        tvAddress.setText(data.getAddress());
        tvDesc.setText(data.getDesc());

        tv_taskName.setText(data.getTaskName());
        tv_taskType.setText(data.getClassName());
        tv_taskArea.setText(data.getCensusAreaName());


//        if (attachs!=null && attachs.size()>0){
//            data.setAttaches(attachs);
//        }

        if (data.getAttaches() != null && data.getAttaches().size() > 0) {
            List<MediaItem> mediaItems = new ArrayList<>();
            for (Attach attach : data.getAttaches()) {
                MediaItem mediaItem = new MediaItem();
                mediaItem.setType(attach.getType());
                if (!TextUtils.isEmpty(attach.getUrl())) {
                    Uri coverUri = Uri.parse(attach.getUrl());
                    coverUri = FileServerUtils.getUri(getContext(),attach.getUrl(), coverUri);
                    mediaItem.setUri(coverUri);
                }
                if (!TextUtils.isEmpty(attach.getThumbUrl())) {
                    Uri coverUri = Uri.parse(attach.getThumbUrl());
                    coverUri = FileServerUtils.getUri(getContext(),attach.getThumbUrl(), coverUri);
                    mediaItem.setThumbnailUri(coverUri);
                }
                mediaItems.add(mediaItem);
            }
            gvAttaches.setVisibility(View.VISIBLE);
            gvAttaches.addMediaItems(mediaItems);
        } else {
            gvAttaches.setVisibility(View.GONE);
        }

    }
}
