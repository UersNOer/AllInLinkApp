package com.example.android_supervisor.ui.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.tencent.liteav.demo.MainActivity;
import com.example.android_supervisor.Presenter.NoticeNewsPresenter;
import com.example.android_supervisor.R;
import com.example.android_supervisor.common.UserSession;
import com.example.android_supervisor.entities.ActualNews;
import com.example.android_supervisor.entities.MainListItems;
import com.example.android_supervisor.entities.MapBundle;
import com.example.android_supervisor.http.ServiceGenerator;
import com.example.android_supervisor.http.response.Response;
import com.example.android_supervisor.http.response.ResponseObserver;
import com.example.android_supervisor.http.service.QuestionService;
import com.example.android_supervisor.map.AMapActivity;
import com.example.android_supervisor.notify.Notifies;
import com.example.android_supervisor.notify.NotifyManager;
import com.example.android_supervisor.notify.NotifyReceiver;
import com.example.android_supervisor.ui.ActualNewsDetailActivity;
import com.example.android_supervisor.ui.ActualNewsListActivity;
import com.example.android_supervisor.ui.CensusTaskListActivity;
import com.example.android_supervisor.ui.CommutePlanActivity;
import com.example.android_supervisor.ui.ContactsActivity;
import com.example.android_supervisor.ui.HistoryActivity;
import com.example.android_supervisor.ui.HomeItemView;
import com.example.android_supervisor.ui.MyTaskListActivity;
import com.example.android_supervisor.ui.SimpleDividerItemDecoration;
import com.example.android_supervisor.ui.StatisticsActivity;
import com.example.android_supervisor.ui.adapter.ActualNewsAdapter;
import com.example.android_supervisor.ui.adapter.GridAdapter;
import com.example.android_supervisor.ui.view.CircleImageView;
import com.example.android_supervisor.utils.CalendarUtils;
import com.example.android_supervisor.utils.DateUtils;
import com.example.android_supervisor.utils.Environments;
import com.example.android_supervisor.utils.FileServerUtils;
import com.example.android_supervisor.utils.MainIcon;
import com.example.android_supervisor.utils.ToastUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.Formatter;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;

/**
 * @author yj
 */
public class HomeFragment extends BaseFragment implements GridAdapter.OnItemClickListener{
    @BindView(R.id.tv_home_user_role)
    TextView tvRole;

    @BindView(R.id.tv_home_user_name)
    TextView tvName;

    @BindView(R.id.iv_home_gps)
    ImageView ivGps;

    @BindView(R.id.iv_home_gprs)
    ImageView ivGprs;

    @BindView(R.id.iv_home_gps_collect)
    ImageView iv_home_gps_collect;

    @BindView(R.id.tv_home_report_count)
    TextView tvSbCount;


    @BindView(R.id.tv_home_hc_count)
    TextView tvHcCount;

    @BindView(R.id.tv_home_hs_count)
    TextView tvHsCount;

    @BindView(R.id.recycler_view)
    RecyclerView recycler;

    @BindView(R.id.recycler_menu_view)
    RecyclerView recycler_menu_view;

    @BindView(R.id.gv_menu_view)
    GridView gv_menu_view;



    @BindView(R.id.head_icon)
    CircleImageView head_icon;



    private MainListItems listItems = new MainListItems();

    private long current = 0;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view,
                              @Nullable Bundle savedInstanceState) {
        ButterKnife.bind(this, view);

        if ( Environments.userBase!=null){
            tvName.setText("（" + Environments.userBase.getUsername() + "）");
            tvRole.setText(Environments.userBase!=null?
                    Environments.userBase.getDefaultRole()!=null?Environments.userBase.getDefaultRole().getName() :"监督员":"");
        }
        if (Environments.userBase!=null&&Environments.userBase.getProfile()!=null){
            setIcon(Environments.userBase.getProfile());
        }

        addMenu(view);/////////////////////

        NotifyManager.registerReceiver(getActivity(), notifyReceiver);
        fetchData();

    }



    private void addMenu(View view) {

        listItems = new MainIcon().getMeun(getActivity());
//        recycler_menu_view.setLayoutManager(new LinearLayoutManager(getActivity()));
        listItems.setAdapter(gv_menu_view, HomeFragment.this);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        if (!RxBus.getDefault().isRegistered(this)) {
//            RxBus.getDefault().register(this);
//        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
//        if (RxBus.getDefault().isRegistered(this)) {
//            RxBus.getDefault().unregister(this);
//        }

    }
//    @Subscribe(threadMode = ThreadMode.MAIN)
//    public void eventBus(EventFreshCase obj) {
//        fetchData();
//    }


    private void fetchData() {
        Date now = new Date();
        //本月
//        Date begin = CalendarUtils.getFirstDayOfMonth(now);
//        Date end = CalendarUtils.getLastDayOfMonth(now);
        //当天
        Date begin = CalendarUtils.getStartTime(now);
        Date end = new Date();

        String userId = UserSession.getUserId(getContext());
//        Log.d("fetchData: ", firstDayOfMonth);

        QuestionService questionService = ServiceGenerator.create(QuestionService.class);

        Observable<Response<Map<String, Integer>>> observable = questionService.getSelfInfo(DateUtils.format(begin),
                DateUtils.format(end), userId);
        observable.compose(this.<Response<Map<String, Integer>>>bindToLifecycle())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new ResponseObserver<Map<String, Integer>>(getContext()) {
                    @Override
                    public void onSuccess(Map<String, Integer> data) {
                        try{

                            Integer reportCount = data.get("reportCount");
                            Integer handleCount = data.get("completedCount");
                            Integer validCount = data.get("validReportCount");
                            Integer invalidCount = data.get("cancelCount");
                            Integer hcCount = data.get("checkedCount");
                            Integer hsCount = data.get("verifiedCount");

                            tvSbCount.setText(reportCount == null?"":String.valueOf(reportCount));
                            tvHcCount.setText(hcCount == null?"":String.valueOf(hcCount));
                            tvHsCount.setText(hsCount == null?"":String.valueOf(hsCount));
                        }catch (Exception e){
                            ToastUtils.show(getContext(),"工作统计："+e.getMessage());
                            e.printStackTrace();
                        }


                    }
                });

        NoticeNewsPresenter noticeNewsPresenter = new NoticeNewsPresenter();
        noticeNewsPresenter.getNoticeNews(getContext(), new NoticeNewsPresenter.NoticeNewsCallBack() {
            @Override
            public void onSuccess(List<ActualNews> actualNewsList) {
                if (actualNewsList!=null){
                    setRecyclerVeiw(actualNewsList);
                }
            }
        });

    }

    private void setRecyclerVeiw(List<ActualNews> actualNewsList) {
        List<ActualNews> actualNews = new ArrayList<>();
        if (actualNewsList != null && actualNewsList.size() > 2) {
            actualNews.add(actualNewsList.get(0));
            actualNews.add(actualNewsList.get(1));
            actualNews.add(actualNewsList.get(2));
        } else actualNews = actualNewsList;
        recycler.setLayoutManager(new LinearLayoutManager(getContext()));
        recycler.addItemDecoration(new SimpleDividerItemDecoration(getContext(), 1));
        ActualNewsAdapter actualNewsAdapter = new ActualNewsAdapter(R.layout.item_actual_news, actualNews);
        final List<ActualNews> finalActualNews = actualNews;
        actualNewsAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                Intent intent=new Intent(getContext(), ActualNewsDetailActivity.class);
                intent.putExtra("id", finalActualNews.get(position).getId());
                startActivity(intent);
            }
        });
        recycler.setAdapter(actualNewsAdapter);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        NotifyManager.unregisterReceiver(getActivity(), notifyReceiver);
    }

    @OnClick(R.id.tv_home_statistics_enter)
    public void onStatistics(View v) {
        Intent intent = new Intent(getContext(), StatisticsActivity.class);
        startActivityForResult(intent,4);
        getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }

    @OnClick(R.id.ll_home_report_count)
    public void onHistoryEvent(View v) {
        Intent intent = new Intent(getContext(), HistoryActivity.class);
        intent.putExtra("index", 2);
        startActivity(intent);
    }

    @OnClick(R.id.ll_home_hc_count)
    public void onHistoryHcTask(View v) {
        Intent intent = new Intent(getContext(), HistoryActivity.class);
        intent.putExtra("index", 3);
        startActivity(intent);
    }

    @OnClick(R.id.ll_home_hs_count)
    public void onHistoryHsTask(View v) {
        Intent intent = new Intent(getContext(), HistoryActivity.class);
        intent.putExtra("index", 4);
        startActivity(intent);
    }



    /**
     * 新闻详情
     * 实时新闻
     * @param v
     */
    @OnClick(R.id.go2NewsDetailList)
    public void go2NewsDetailList(View v) {
        Intent intent=new Intent(getContext(),ActualNewsListActivity.class);
        getActivity().startActivity(intent);
    }

    //个人任务
    @OnClick(R.id.grid_item_my_task)
    public void onMyTask(View v) {
        Intent intent = new Intent(getContext(), MyTaskListActivity.class);
        startActivityForResult(intent, 2);
    }

    //历史记录
    @OnClick(R.id.grid_item_history)
    public void onHistory(View v) {
        Intent intent = new Intent(getContext(), HistoryActivity.class);
        startActivity(intent);
    }

    //通讯录
    @OnClick(R.id.grid_item_contacts)
    public void onContacts(View v) {
        //腾讯云
//        Intent intent = new Intent(getContext(), ContactListActivity.class);
//        startActivity(intent);
        //
        Intent intent = new Intent(getContext(), ContactsActivity.class);
        startActivity(intent);
    }

    //日常考勤
    @OnClick(R.id.grid_item_commute_plan)
    public void onCommutePlan(View v) {
        Intent intent = new Intent(getContext(), CommutePlanActivity.class);
        startActivity(intent);
    }

    //专项任务
    @OnClick(R.id.grid_item_census_task)
    public void onCensusTask(View v) {
        Intent intent = new Intent(getContext(), CensusTaskListActivity.class);
        startActivity(intent);
    }


    @OnClick(R.id.tv_OfflineMap)
    public void offlineMap(View v) {
        //离线地图
//        startActivity(new Intent(getActivity(),
//                com.amap.api.maps.offlinemap.OfflineMapActivity.class));
        //视频会议
       startActivity(new Intent(getActivity(), MainActivity.class));
    }

    //卡点功能
    @OnClick(R.id.tv_clock)
    public void check_out(View v) {
        //卡点
        Bundle bundle = new Bundle();
        MapBundle mapBundle = new MapBundle();

        mapBundle.isShowGird = true;
        mapBundle.isShowCase = false;
        bundle.putSerializable("mapBundle", mapBundle);
        Intent intent = new Intent(getActivity(), AMapActivity.class);
        intent.putExtras(bundle);
        startActivity(intent);
    }




    @Override
    public void onHiddenChanged(boolean hidden) {
        if (!hidden) {
            fetchData();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                fetchData();
            }
        },1000);

//        if (resultCode == Activity.RESULT_OK) {
//            switch (requestCode) {
//                case 1: // 案件上报成功后
//                    fetchData();
//                    break;
//                case 2: // 案件核实核查成功后
//                    fetchData();
//                    break;
//                case 3: // 案件快速上報成功后
//                    fetchData();
//                    break;
//                case 4:
//                    fetchData();
//                    break;
//            }
//        }
    }

    final NotifyReceiver notifyReceiver = new NotifyReceiver() {

        @Override
        public void onReceive(Context context, int type, int status, int count) {
            switch (type) {
                case Notifies.NOTIFY_TYPE_GPS:
                    if (status == Notifies.NOTIFY_STATUS_ONLINE) {
                        ivGps.setBackgroundColor(0xfff39800);
                    } else {
                        ivGps.setBackgroundColor(0xffbababa);
                    }
                    break;
                case Notifies.NOTIFY_TYPE_GPRS:
                    if (status == Notifies.NOTIFY_STATUS_ONLINE) {
                        ivGprs.setBackgroundColor(0xff8fc31f);
                    } else {
                        ivGprs.setBackgroundColor(0xffbababa);
                    }
                    break;
                case Notifies.NOTIFY_TYPE_GPS_COLLECT:
                    if (status == Notifies.NOTIFY_STATUS_ONLINE) {
                        iv_home_gps_collect.setBackgroundColor(0xff8fc31f);
                        iv_home_gps_collect.setImageResource(R.drawable.gps_nomal);
                    } else {
                        iv_home_gps_collect.setBackgroundColor(0xffbababa);
                        iv_home_gps_collect.setImageResource(R.drawable.gps_none);
                    }
                    break;
                case Notifies.NOTIFY_TYPE_MSG:
                    fetchData();
                    break;
            }
        }
    };

    @Override
    public void onItemClick(int position, View v) {
        if (v instanceof HomeItemView){
            HomeItemView view = (HomeItemView) v;
            Intent intent = new Intent(getActivity(), view.getLauncher());
            Bundle bundle = new Bundle();
            switch (view.getTags()){

                case tagCR:
                    bundle.putInt("source", 4);
                    intent.putExtras(bundle);
                    startActivityForResult(intent, 1);
                    break;
                case tagCRKS:
                    bundle.putInt("source", 5);
                    intent.putExtras(bundle);
                    startActivityForResult(intent, 3);
                    break;
                case tagTask:
                    startActivityForResult(intent, 2);
                    break;
                case tagPK:
                    MapBundle mapBundle = new MapBundle();
                    mapBundle.isShowGird = true;
                    mapBundle.isShowCase = false;
                    bundle.putSerializable("mapBundle", mapBundle);
                    intent.putExtras(bundle);
                    startActivity(intent);
                    break;
                case tagYqc:
                    bundle.putInt("source",82 );
                    intent.putExtras(bundle);
                    startActivity(intent);
                    break;

                case tagYqs:
                    bundle.putInt("source", 83);
                    intent.putExtras(bundle);
                    startActivity(intent);
                    break;

                default:
                    startActivity(intent);
                    break;
            }

        }

    }


    private StringBuilder mFormatBuilder;
    private Formatter mFormatter;

    public String stringForTime(long timeMs) {

        long hour = timeMs / 3600;
        long min = timeMs % 3600 / 60;
        long second = timeMs % 60;
        return String.format(Locale.CHINESE, "%02d:%02d:%02d", hour, min, second);
    }


    public void setIcon(String imageUrl) {

        if (TextUtils.isEmpty(imageUrl)){
            return;
        }

        Glide.with(HomeFragment.this).load(FileServerUtils.getPath(getContext(),imageUrl)).into(head_icon);

    }
}
