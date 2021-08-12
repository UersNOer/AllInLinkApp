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
import com.example.android_supervisor.common.UserSession;
import com.example.android_supervisor.entities.CensusEventRes;
import com.example.android_supervisor.http.ServiceGenerator;
import com.example.android_supervisor.http.request.QueryBody;
import com.example.android_supervisor.http.response.Response;
import com.example.android_supervisor.http.service.QuestionService;
import com.example.android_supervisor.map.MapFragment;
import com.example.android_supervisor.ui.CensusEventDetailActivity;
import com.example.android_supervisor.utils.ToastUtils;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;

/**
 * @author wujie
 */
public class CensusMapFragment extends MapFragment {
    private List<Marker> todoEventMarkers = new ArrayList<>();
    private Marker selectedEventMarker;

    private String taskId;

    public static CensusMapFragment newInstance(String taskId) {
        CensusMapFragment fragment = new CensusMapFragment();
        Bundle bundle = new Bundle();
        bundle.putString("taskId", taskId);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Bundle bundle = getArguments();
        if (bundle != null) {
            this.taskId = bundle.getString("taskId");
        }
        loadTodoEvents();
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        Object tag = marker.getTag();
        if (tag != null) {
            if (tag instanceof EventTag) {
                selectedEventMarker = marker;
                EventTag evtTag = (EventTag) tag;
                Intent intent = new Intent(getContext(), CensusEventDetailActivity.class);
                intent.putExtra("id", evtTag.id);
                startActivity(intent);
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
                    .eq("censusTaskId", taskId)
                    .eq("createId", UserSession.getUserId(getContext()))
                    .desc("reportTime")
                    .create();
        QuestionService questionService = ServiceGenerator.create(QuestionService.class);
        Observable<Response<List<CensusEventRes>>> observable = questionService.getCensusEventList(queryBody);

        observable.compose(this.<Response<List<CensusEventRes>>>bindToLifecycle())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Response<List<CensusEventRes>>>() {
                    @Override
                    public void accept(Response<List<CensusEventRes>> response) throws Exception {
                        if (response.isSuccess()) {
                            List<CensusEventRes> data = response.getData();
                            for (CensusEventRes evtRes : data) {
                                LatLng position = new LatLng(evtRes.getGeoY(), evtRes.getGeoX());
                                todoEventMarkers.add(
                                        tencentMap.addMarker(new MarkerOptions(position)
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
                        ToastUtils.show(getContext(), "案件加载失败");
                    }
                });
    }
}
