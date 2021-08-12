package com.example.android_supervisor.ui.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.example.android_supervisor.R;
import com.example.android_supervisor.common.UserSession;
import com.example.android_supervisor.entities.CacheEvent;
import com.example.android_supervisor.entities.EventPara;
import com.example.android_supervisor.entities.EventProc;
import com.example.android_supervisor.http.ServiceGenerator;
import com.example.android_supervisor.http.common.ProgressTransformer;
import com.example.android_supervisor.http.common.RefreshLayoutTransformer;
import com.example.android_supervisor.http.request.JsonBody;
import com.example.android_supervisor.http.response.Response;
import com.example.android_supervisor.http.response.ResponseObserver;
import com.example.android_supervisor.http.service.QuestionService;
import com.example.android_supervisor.notify.Notifies;
import com.example.android_supervisor.notify.NotifyManager;
import com.example.android_supervisor.notify.NotifyReceiver;
import com.example.android_supervisor.sqlite.PrimarySqliteHelper;
import com.example.android_supervisor.ui.EventNewActivity;
import com.example.android_supervisor.ui.adapter.HistoryEventAdapter;
import com.example.android_supervisor.ui.view.ProgressText;
import com.example.android_supervisor.utils.ToastUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * @author wujie
 */
public class AutoCachedEventFragment extends SearchSelectableListFragment {
    private HistoryEventAdapter adapter;
    private String cacheId;

    private int pageIndex = 1;
    private int pageSize = 30;
    private String searchKey;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setEnableRefresh(false);
        etSearch.setHint("通过事发位置、案件描述搜索");
        etSearch.setInputType(EditorInfo.TYPE_TEXT_VARIATION_LONG_MESSAGE);
        setSearchInfo("共%d条案件", 0);

        //initSelectToolBar();

        adapter = new HistoryEventAdapter(getActivity());
        setAdapter(adapter);
        fetchData(false);
        NotifyManager.registerReceiver(getActivity(), mNotifyReceiver);
        //

    }

    @Override
    public void onStart() {
        super.onStart();

    }

    final NotifyReceiver mNotifyReceiver = new NotifyReceiver() {
        @Override
        public void onReceive(Context context, int type, int status, int count) {

            if (type == Notifies.NOTIFY_TYPE_MSG ) {
                fetchData(false);
            }

        }
    };

    private void initSelectToolBar() {
        LinearLayout floatToolBar = mSelectToolBar.findViewById(R.id.ll_float_toolbar);
        View view = LayoutInflater.from(getContext()).inflate(R.layout.float_toolbar_report, floatToolBar, false);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<CacheEvent> selectedEvents = adapter.getSelectedItems();
                if (selectedEvents.isEmpty()) {
                    Toast.makeText(v.getContext(), "请选择需要上报的案件", Toast.LENGTH_SHORT).show();
                } else {
                    mSelectToolBar.hide();
                    batchReportEvent(selectedEvents);
                }
            }
        });
        floatToolBar.addView(view, 0);
    }

    private void batchReportEvent(final List<CacheEvent> events) {
        List<CacheEvent> eventAttaches = new ArrayList<>();
        List<CacheEvent> eventLaterAttaches = new ArrayList<>();
        for (CacheEvent event : events) {
            if (event.getData().getLaterAttaches() != null && event.getData().getLaterAttaches().size() > 0)
                eventLaterAttaches.add(event);
            else
                eventAttaches.add(event);
        }
        final List<CacheEvent> eventAttachesTemp = eventAttaches;
        final List<CacheEvent> eventLaterAttachesTemp = eventLaterAttaches;
        final AtomicInteger count = new AtomicInteger(0);

        QuestionService questionService = ServiceGenerator.create(QuestionService.class);
        Observable<Response<List<EventProc>>> observable;
        if (eventAttachesTemp.size() > 0)
            questionService.getEventProc("1")
                    .compose(this.<Response<List<EventProc>>>bindToLifecycle())
                    .flatMap(new Function<Response<List<EventProc>>, ObservableSource<Response<String>>>() {
                        @Override
                        public ObservableSource<Response<String>> apply(Response<List<EventProc>> response) throws Exception {
                            if (!response.isSuccess()) {
                                throw new IllegalAccessException("获取案件流程失败");
                            }
                            if (response.getData() == null) {
                                throw new IllegalAccessException("获取案件流程失败");
                            }
                            String procId = null;
                            for (EventProc eventProc : response.getData()) {
                                if (eventProc.getKey().equals("process")) {
                                    procId = eventProc.getId();
                                    break;
                                }
                            }
                            if (procId == null) {
                                throw new IllegalAccessException("获取案件流程失败");
                            }

                            List<Observable<Response<String>>> observables = new ArrayList<>();
                            QuestionService questionService = ServiceGenerator.create(QuestionService.class);
                            for (CacheEvent event : eventAttachesTemp) {
                                EventPara para = event.getData();
                                para.setProcId(procId);
                                para.setUserName(UserSession.getUserName(getContext()));
                                para.setMobile(UserSession.getMobile(getContext()));
                                observables.add(questionService.addEvent(new JsonBody(para)));
                            }

                            return Observable.concat(observables);
                        }
                    })
                    .observeOn(AndroidSchedulers.mainThread())
                    .compose(new ProgressTransformer<Response<String>>(getContext(), ProgressText.submit))
                    .subscribe(new ResponseObserver<String>(getContext()) {
                        @Override
                        public void onSuccess(String title) {
                            String message = "案件\"" + title + "\"上报成功";
                            ToastUtils.show(getContext(), message, Toast.LENGTH_SHORT);

                           // RxBus.getDefault().post(new EventFreshCase());
                            int index = count.getAndIncrement();
                            if (index < events.size()) {
                                CacheEvent event = events.get(index);
                                adapter.remove(event);

                                PrimarySqliteHelper sqliteHelper = PrimarySqliteHelper.getInstance(getContext());
                                sqliteHelper.getCacheEventDao().deleteById(event.getId());
                            }
                        }

                        @Override
                        public void onFailure(int code, String errMsg) {
                            super.onFailure(code, errMsg);
                            count.getAndIncrement();
                        }
                    });

        if (eventLaterAttachesTemp.size() > 0) {
            questionService.getEventlistProcess("1")
                    .compose(this.<Response<List<EventProc>>>bindToLifecycle())
                    .flatMap(new Function<Response<List<EventProc>>, ObservableSource<Response<String>>>() {
                        @Override
                        public ObservableSource<Response<String>> apply(Response<List<EventProc>> response) throws Exception {
                            if (!response.isSuccess()) {
                                throw new IllegalAccessException("获取快速上报案件流程失败");
                            }
                            if (response.getData() == null) {
                                throw new IllegalAccessException("获取快速上报案件流程失败");
                            }
                            String procId = null;
                            for (EventProc eventProc : response.getData()) {
                                if (eventProc.getKey().equals("fast_process")) {
                                    procId = eventProc.getId();
                                    break;
                                }
                            }
                            if (procId == null) {
                                throw new IllegalAccessException("获取快速上报案件流程失败");
                            }

                            List<Observable<Response<String>>> observables = new ArrayList<>();
                            QuestionService questionService = ServiceGenerator.create(QuestionService.class);
                            for (CacheEvent event : eventLaterAttachesTemp) {
                                EventPara para = event.getData();
                                para.setProcId(procId);
                                para.setUserName(UserSession.getUserName(getContext()));
                                para.setMobile(UserSession.getMobile(getContext()));
                                observables.add(questionService.addEvent(new JsonBody(para)));
                            }
                            return Observable.concat(observables);
                        }
                    })
                    .observeOn(AndroidSchedulers.mainThread())
                    .compose(new ProgressTransformer<Response<String>>(getContext(), ProgressText.submit))
                    .subscribe(new ResponseObserver<String>(getContext()) {
                        @Override
                        public void onSuccess(String title) {
                            String message = "快速上报案件\"" + title + "\"上报成功";
                            ToastUtils.show(getContext(), message, Toast.LENGTH_SHORT);

                            int index = count.getAndIncrement();
                            if (index < events.size()) {
                                CacheEvent event = events.get(index);
                                adapter.remove(event);

                                PrimarySqliteHelper sqliteHelper = PrimarySqliteHelper.getInstance(getContext());
                                sqliteHelper.getCacheEventDao().deleteById(event.getId());
                            }
                        }

                        @Override
                        public void onFailure(int code, String errMsg) {
                            super.onFailure(code, errMsg);
                            count.getAndIncrement();
                        }
                    });
        }
    }

    @Override
    public void onLoadMore(RefreshLayout refreshLayout) {
        fetchData(true);
    }

    @Override
    public void onSearch(String key) {
        searchKey = key;
        fetchData(false);
    }

    private void fetchData(boolean loadMore) {
        if (!loadMore) {
            pageIndex = 1;
            adapter.clear();
        }
        ObservableTransformer<List<CacheEvent>, List<CacheEvent>> transformer;
        if (loadMore) {
            transformer = new RefreshLayoutTransformer<>(mRefreshLayout);
        } else {
            transformer = new ProgressTransformer<>(getActivity(), ProgressText.load);
        }
        Observable.create(new ObservableOnSubscribe<List<CacheEvent>>() {
            @Override
            public void subscribe(ObservableEmitter<List<CacheEvent>> emitter) throws Exception {
                long offset = (pageIndex - 1) * pageSize;
                long limit = pageSize;

                PrimarySqliteHelper sqliteHelper = PrimarySqliteHelper.getInstance(getContext());
                final List<CacheEvent> caches;
                if (TextUtils.isEmpty(searchKey)) {
                    caches = sqliteHelper.getCacheEventDao()
                            .queryBuilder()
                            .offset(offset)
                            .limit(limit)
                            .where()
                            .eq("saveType",1)
                            .query();

                } else {
                    caches = sqliteHelper.getCacheEventDao()
                            .queryBuilder()
                            .offset(offset)
                            .limit(limit)
                            .where()
                            .eq("saveType",1)
                            .like("address", "%" + searchKey + "%")
                            .or()
                            .like("description", "%" + searchKey + "%")
                            .query();
                }
                emitter.onNext(caches);
                emitter.onComplete();
            }
        }).compose(this.<List<CacheEvent>>bindToLifecycle())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .compose(transformer)
                .subscribe(new Consumer<List<CacheEvent>>() {
                    @Override
                    public void accept(List<CacheEvent> caches) throws Exception {
                        Collections.reverse(caches);
                        Log.d("coverUrl", "caches: " + caches.size());
                        if (caches.size() > 0) {
                            adapter.addAll(caches);
                            pageIndex++;
                        } else {
                            setNoMoreData(true);
                        }
                        setSearchInfo("共%d条案件", caches.size());
                        setNoData(adapter.size() == 0);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        Log.d("coverUrl", "caches1: " + throwable.getMessage());
                        throwable.printStackTrace();
                    }
                });
    }

    @Override
    public RecyclerView.ItemDecoration getItemDecoration(Context context) {
        DividerItemDecoration divider = new DividerItemDecoration(context, LinearLayout.VERTICAL);
        divider.setDrawable(getResources().getDrawable(R.drawable.divider_space));
        return divider;
    }

    @Override
    public void onSelectableItemClick(RecyclerView parent, View view, int position, long id) {
        CacheEvent cacheEvent = adapter.get(position);
        this.cacheId = cacheEvent.getId();
        EventPara data = cacheEvent.getData();
        Intent intent = null;
//        if (data.getLaterAttaches() != null && data.getLaterAttaches().size() > 0) {
//            intent = new Intent(getContext(), FastReportActivity.class);
//        } else
//            intent = new Intent(getContext(), EventNewActivity.class);
        intent = new Intent(getContext(), EventNewActivity.class);
        intent.putExtra("cachedId", cacheId);
        startActivityForResult(intent, 1);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == Activity.RESULT_OK) {
            fetchData(false);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        NotifyManager.unregisterReceiver(getActivity(), mNotifyReceiver);
    }
}
