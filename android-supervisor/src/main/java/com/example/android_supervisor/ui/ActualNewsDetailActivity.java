package com.example.android_supervisor.ui;

import android.net.Uri;
import android.os.Bundle;
//import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.android_supervisor.R;
import com.example.android_supervisor.entities.ActualNewsDetail;
import com.example.android_supervisor.http.ServiceGenerator;
import com.example.android_supervisor.http.response.Response;
import com.example.android_supervisor.http.response.ResponseObserver;
import com.example.android_supervisor.http.service.PublicService;
import com.example.android_supervisor.ui.media.MediaGridView;
import com.example.android_supervisor.ui.media.MediaItem;
import com.zzhoujay.richtext.ImageHolder;
import com.zzhoujay.richtext.RichText;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;

/**
 * 新闻 通知公告详情类
 */
public class ActualNewsDetailActivity extends BaseActivity {

    @BindView(R.id.tv_headline_content)
    TextView textView;
    @BindView(R.id.recycView)
    RecyclerView recyclerView;


    @BindView(R.id.media_v)
    MediaGridView media_v;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_actual_news_detail);
        ButterKnife.bind(this);
        media_v.setDrawableHide();
        try{
            String id = getIntent().getStringExtra("id");
            Log.d("news", "onCreate: " + id);
            initData(id);
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    private void setNews(String content) {
        RichText.from(content).bind(this)
                .showBorder(false)
                .size(ImageHolder.MATCH_PARENT, ImageHolder.WRAP_CONTENT)
                .into(textView);
    }

    private void initData(String id) {
        RichText.initCacheDir(this);
        PublicService publicService = ServiceGenerator.create(PublicService.class);
        Observable<Response<ActualNewsDetail>> observable1 = publicService.getNoticeNewsDetail(id);
        observable1.compose(this.<Response<ActualNewsDetail>>bindToLifecycle())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new ResponseObserver<ActualNewsDetail>(ActualNewsDetailActivity.this) {
                    @Override
                    public void onSuccess(ActualNewsDetail actualNewsDetail) {
                        setNews(actualNewsDetail.getContent());
                        setView(actualNewsDetail);
                    }

                    @Override
                    public void onFailure() {
                        super.onFailure();
                        finish();
                    }
                });

    }

    private void setView(ActualNewsDetail actualNewsDetail) {
        if (actualNewsDetail.getFiles() != null && actualNewsDetail.getFiles().size() > 0) {
//            List<NewsMultItem> newsMultItemList = new ArrayList<>();

            List<MediaItem> mediaItems = new ArrayList<>();
            String reg = ".+(.JPEG|.jpeg|.JPG|.jpg|.png|.PNG)$";

//            String reg = "\\w+\\.(jpg|gif|bmp|png)";
            for (ActualNewsDetail.FilesBean filesBean : actualNewsDetail.getFiles()) {
//                Log.d("filesBean", "setView: " + filesBean.getName().matches(reg));
//                if (filesBean.getName().matches(reg)) {
//                    newsMultItemList.add(new NewsMultItem(filesBean.getUrl(), NewsMultItem.IMG));
//                }
                Matcher matcher = Pattern.compile(reg).matcher(filesBean.getName());
                if (matcher.find()){
                    MediaItem  mediaItem = new MediaItem();
                    mediaItem.setUri(Uri.parse(media_v.adapter.getPath(filesBean.getUrl())));
                    mediaItem.setThumbnailUri(Uri.parse(media_v.adapter.getPath(filesBean.getUrl())));
                    mediaItem.setFileName(filesBean.getName());
                    mediaItem.setType(0);
                    mediaItems.add(mediaItem);
                }

//                if (filesBean.getName().endsWith(".png") ||filesBean.getName().endsWith(".jpg") ||filesBean.getName().endsWith(".bmp") ||filesBean.getName().endsWith(".jpg")){
//
//                }
//                if (filesBean.getName().matches(reg)) {
//                    newsMultItemList.add(new NewsMultItem(filesBean.getUrl(), NewsMultItem.IMG));
//                }
            }

            media_v.adapter.addAll(mediaItems);

//            recyclerView.setLayoutManager(new LinearLayoutManager(this));
//            NewsMultItemAdapter newsMultItemAdapter = new NewsMultItemAdapter(newsMultItemList);
//            recyclerView.setAdapter(newsMultItemAdapter);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //结束时清空内容
        RichText.clear(this);
    }
}
