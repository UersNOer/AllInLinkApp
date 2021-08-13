package com.example.android_supervisor.ui;


import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.example.android_supervisor.R;
import com.example.android_supervisor.http.ServiceGenerator;
import com.example.android_supervisor.http.common.ProgressTransformer;
import com.example.android_supervisor.http.response.Response;
import com.example.android_supervisor.http.response.ResponseObserver;
import com.example.android_supervisor.http.service.YqService;
import com.example.android_supervisor.ui.media.MediaGridView;
import com.example.android_supervisor.ui.media.MediaItem;
import com.example.android_supervisor.ui.model.CsDescRes;
import com.example.android_supervisor.ui.view.ProgressText;
import com.example.android_supervisor.utils.FileServerUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;

public class SJDescActivity extends BaseActivity {


    @BindView(R.id.tv_qjsj_sj)
    TextView tvsjTyoe;
    @BindView(R.id.tv_qy)
    TextView tv_qy;
    @BindView(R.id.tv_event_info_address)
    TextView tv_event_info_address;
    @BindView(R.id.tv_event_info_desc)
    TextView tvEventInfoDesc;


    @BindView(R.id.gv_event_info_attaches_b)
    MediaGridView gvEventInfoAttachesB;
    @BindView(R.id.gv_event_info_attaches_f)
    MediaGridView gvEventInfoAttachesF;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_sj_info);
        ButterKnife.bind(this);
        String id  = getIntent().getStringExtra("id");
        getNetData(id);
        setTitle("疫情事件");
    }

    private void getNetData( String id ) {
        if (TextUtils.isEmpty(id)){
            return;
        }
        YqService questionService = ServiceGenerator.create(YqService.class);
        Observable<Response<CsDescRes>> observable = questionService.esEventDesc(id);

        observable.compose(this.<Response<CsDescRes>>bindToLifecycle())
                .observeOn(AndroidSchedulers.mainThread())
                .compose(new ProgressTransformer<>(SJDescActivity.this, ProgressText.load))
                .subscribe(new ResponseObserver<CsDescRes>(SJDescActivity.this) {
                    @Override
                    public void onSuccess(CsDescRes data) {

                        tvsjTyoe.setText(data.getTypeName());
                        tv_qy.setText(data.getAreaName());
                        tv_event_info_address.setText(data.getEsPosition());
                        tvEventInfoDesc.setText(data.getEsDesc());


                        if (data.getBeforeImgUrls()!=null){

                            gvEventInfoAttachesB.setVisibility(View.VISIBLE);
                            updateAttachData(data.getBeforeImgUrls(),gvEventInfoAttachesB);
                        }

                        if (data.getBeforeImgUrls()!=null){

                            gvEventInfoAttachesF.setVisibility(View.VISIBLE);
                            updateAttachData(data.getBeforeImgUrls(),gvEventInfoAttachesF);
                        }

                    }
                });

    }


    private void updateAttachData(List<CsDescRes.BeforeImgUrlsBean> attaches, MediaGridView mediaUploadGridView) {
        if (attaches == null || attaches.size() <= 0) {
            return;
        }
        List<MediaItem> mediaItems = new ArrayList<>();

        for (CsDescRes.BeforeImgUrlsBean attach : attaches) {
            MediaItem mediaItem = new MediaItem();
            try{
                mediaItem.setType(Integer.valueOf(attach.getFileType()));
            }catch (Exception e){
                e.printStackTrace();
            }

            mediaItem.setProgress(100);
            if (!TextUtils.isEmpty(attach.getFilePath())) {
                String path = FileServerUtils.getPath(this, attach.getFilePath());
                mediaItem.setUri(Uri.parse(path));
                mediaItem.setThumbnailUri(Uri.parse(path));
            }

            mediaItems.add(mediaItem);
        }
        mediaUploadGridView.addMediaItems(mediaItems);
    }
}
