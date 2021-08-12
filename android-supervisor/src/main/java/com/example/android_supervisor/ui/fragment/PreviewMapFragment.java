package com.example.android_supervisor.ui.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.view.View;

import com.tencent.tencentmap.mapsdk.maps.model.BitmapDescriptorFactory;
import com.tencent.tencentmap.mapsdk.maps.model.LatLng;
import com.tencent.tencentmap.mapsdk.maps.model.Marker;
import com.tencent.tencentmap.mapsdk.maps.model.MarkerOptions;
import com.example.android_supervisor.R;
import com.example.android_supervisor.entities.EventRes;
import com.example.android_supervisor.http.ServiceGenerator;
import com.example.android_supervisor.http.request.QueryBody;
import com.example.android_supervisor.http.response.Response;
import com.example.android_supervisor.http.service.QuestionService;
import com.example.android_supervisor.map.MapFragment;
import com.example.android_supervisor.ui.TaskDetailActivity;
import com.example.android_supervisor.utils.ToastUtils;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * @author wujie
 */
public class PreviewMapFragment extends MapFragment {
    private List<Marker> todoEventMarkers = new ArrayList<>();
    private Marker selectedEventMarker;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        loadTodoEvents();
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        Object tag = marker.getTag();
        if (tag != null) {
            if (tag instanceof EventTag) {
                selectedEventMarker = marker;
                EventTag evtTag = (EventTag) tag;
                Intent intent = new Intent(getContext(), TaskDetailActivity.class);
                intent.putExtra("id", evtTag.id);
                intent.putExtra("status", evtTag.type);
                startActivityForResult(intent, 1);
                return true;
            }
        }
        return false;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK && requestCode == 1) {
            if (selectedEventMarker != null) {
                todoEventMarkers.remove(selectedEventMarker);
                selectedEventMarker.remove();
            }
        }
        selectedEventMarker = null;
    }

    class EventTag {
        String id;
        int type;

        public EventTag(String id, int type) {
            this.id = id;
            this.type = type;
        }
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        if (!hidden) {
            loadTodoEvents();
        }
    }

    // 加载待办案件
    private void loadTodoEvents() {
        for (Marker evtMarker : todoEventMarkers) {
            evtMarker.remove();
        }
        todoEventMarkers.clear();

        QueryBody queryBody = new QueryBody.Builder()
                .pageIndex(1)
                .pageSize(100)
                .put("isHistory", 1)
                .create();

        QuestionService questionService = ServiceGenerator.create(QuestionService.class);
        Observable<Response<List<EventRes>>> observable = questionService.getHcTaskList(queryBody);
        observable.compose(this.<Response<List<EventRes>>>bindToLifecycle())
                .observeOn(AndroidSchedulers.mainThread())
                .flatMap(new Function<Response<List<EventRes>>, ObservableSource<Response<List<EventRes>>>>() {
                    @Override
                    public ObservableSource<Response<List<EventRes>>> apply(Response<List<EventRes>> response) throws Exception {
                        if (response.isSuccess()) {
                            List<EventRes> data = response.getData();
                            for (EventRes evtRes : data) {
                                LatLng position = new LatLng(evtRes.getGeoY(), evtRes.getGeoX());
                                todoEventMarkers.add(
                                        tencentMap.addMarker(new MarkerOptions(position)
                                                .anchor(.5f, .5f)
                                                .draggable(false)
                                                .tag(new EventTag(evtRes.getId(), 1))
                                                .icon(BitmapDescriptorFactory.fromResource(R.drawable.tmap_event))));
                            }
                        }

                        QueryBody queryBody = new QueryBody.Builder()
                                .pageIndex(1)
                                .pageSize(100)
                                .put("isHistory", 1)
                                .create();

                        QuestionService questionService = ServiceGenerator.create(QuestionService.class);
                        return questionService.getHsTaskList(queryBody);
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Response<List<EventRes>>>() {
                    @Override
                    public void accept(Response<List<EventRes>> response) throws Exception {
                        if (response.isSuccess()) {
                            List<EventRes> data = response.getData();
                            for (EventRes evtRes : data) {
                                LatLng position = new LatLng(evtRes.getGeoY(), evtRes.getGeoX());
                                todoEventMarkers.add(
                                        tencentMap.addMarker(new MarkerOptions()
                                                .position(position)
                                                .anchor(.5f, .5f)
                                                .draggable(false)
                                                .tag(new EventTag(evtRes.getId(), 2))
                                                .icon(BitmapDescriptorFactory.fromResource(R.drawable.tmap_event))));
                            }
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        ToastUtils.show(getContext(), "待办案件加载失败");
                    }
                });
    }
}
