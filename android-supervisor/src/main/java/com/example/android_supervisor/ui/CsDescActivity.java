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
import com.example.android_supervisor.http.service.YqytsService;
import com.example.android_supervisor.ui.media.MediaGridView;
import com.example.android_supervisor.ui.media.MediaItem;
import com.example.android_supervisor.ui.model.CsDescRess;
import com.example.android_supervisor.ui.view.ProgressText;
import com.example.android_supervisor.utils.FileServerUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;

public class CsDescActivity extends BaseActivity {


    @BindView(R.id.tv_qjsj_sj)
    TextView tvsjTyoe;
    @BindView(R.id.tv_qy)
    TextView tv_qy;
    @BindView(R.id.tv_event_info_address)
    TextView tv_event_info_address;
    @BindView(R.id.tv_event_info_desc)
    TextView tvEventInfoDesc;

    @BindView(R.id.tv_lxr)
    TextView tv_lxr;
    @BindView(R.id.tv_lxfs)
    TextView tv_lxfs;
    @BindView(R.id.tv_sbsj)
    TextView tv_sbsj;
    @BindView(R.id.tv_csName)
    TextView tv_csName;


    @BindView(R.id.gv_event_info_attaches_b)
    MediaGridView gvEventInfoAttachesB;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_cs_info);
        ButterKnife.bind(this);
        String id  = getIntent().getStringExtra("id");
        getNetData(id);
        setTitle("疫情场所");
    }

    private void getNetData( String id ) {
        if (TextUtils.isEmpty(id)){
            return;
        }
        YqytsService questionService = ServiceGenerator.create(YqytsService.class);
        Observable<Response<CsDescRess>> observable = questionService.getByIdForYQ(id);

        observable.compose(this.<Response<CsDescRess>>bindToLifecycle())
                .observeOn(AndroidSchedulers.mainThread())
                .compose(new ProgressTransformer<>(CsDescActivity.this, ProgressText.load))
                .subscribe(new ResponseObserver<CsDescRess>(CsDescActivity.this) {
                    @Override
                    public void onSuccess(CsDescRess data) {

                        tvsjTyoe.setText(data.getTypeName());
                        tv_qy.setText(data.getAreaName());
                        tv_event_info_address.setText(data.getObjpos());
                        tvEventInfoDesc.setText(data.getRemake());

                        tv_lxr.setText(data.getDirName());
                        tv_lxfs.setText(data.getDirPhone());
                        tv_sbsj.setText(data.getOrdate());
                        tv_csName.setText(data.getObjname());

                        if (data.getImgPathList()!=null){

                            gvEventInfoAttachesB.setVisibility(View.VISIBLE);
                            updateAttachData(data.getImgPathList(),gvEventInfoAttachesB);
                        }

                    }
                });

    }


    private void updateAttachData(List<CsDescRess.ImgPathListBean> attaches, MediaGridView mediaUploadGridView) {
        if (attaches == null || attaches.size() <= 0) {
            return;
        }
        List<MediaItem> mediaItems = new ArrayList<>();

        for (CsDescRess.ImgPathListBean attach : attaches) {
            MediaItem mediaItem = new MediaItem();
            try{
                mediaItem.setType(attach.getFilePath().endsWith(".jpg")?0:1);
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
