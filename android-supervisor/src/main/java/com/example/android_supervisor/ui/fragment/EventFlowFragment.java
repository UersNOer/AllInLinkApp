package com.example.android_supervisor.ui.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.view.View;

import com.example.android_supervisor.R;
import com.example.android_supervisor.entities.EventFlowRes;
import com.example.android_supervisor.http.ServiceGenerator;
import com.example.android_supervisor.http.common.ProgressTransformer;
import com.example.android_supervisor.http.response.Response;
import com.example.android_supervisor.http.response.ResponseObserver;
import com.example.android_supervisor.http.service.QuestionService;
import com.example.android_supervisor.ui.adapter.MyTaskFlowAdapter;
import com.example.android_supervisor.ui.view.ProgressText;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;

/**
 * @author 案件流程
 */
public class EventFlowFragment extends ListFragment {
    private MyTaskFlowAdapter adapter;
    List<EventFlowRes> eventFlowRes = new ArrayList<>();
    private int status;
    private String id;

    @BindView(R.id.tv_function)
    FloatingActionButton tv_function;


    public static EventFlowFragment newInstance(int status, String id) {
        EventFlowFragment fragment = new EventFlowFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("status", status);
        bundle.putString("id", id);
        fragment.setArguments(bundle);
        return fragment;
    }

    @SuppressLint("RestrictedApi")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setEnableRefresh(false);
        setEnableLoadMore(false);
//        ButterKnife.bind(this, view);
        adapter = new MyTaskFlowAdapter();
        setAdapter(adapter, false);

        Bundle bundle = getArguments();
        if (bundle != null) {
            this.status = bundle.getInt("status");
            this.id = bundle.getString("id");
        }

        tv_function = view.findViewById(R.id.tv_function);
        tv_function.show(); ;
        tv_function.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (eventFlowRes==null || eventFlowRes.size()==0){
                    return;
                }
                Collections.reverse(eventFlowRes);
                List<EventFlowRes> temp = eventFlowRes;
                adapter = new MyTaskFlowAdapter();
                setAdapter(adapter, false);
                adapter.addAll(temp);
            }
        });
        fetchData();
    }


    private void fetchData() {
        if (adapter.size() > 0) {
            adapter.clear();
        }
        QuestionService questionService = ServiceGenerator.create(QuestionService.class);
        Observable<Response<List<EventFlowRes>>> observable = questionService.getEventFlowList(id);
        observable.compose(this.<Response<List<EventFlowRes>>>bindToLifecycle())
                .observeOn(AndroidSchedulers.mainThread())
                .compose(new ProgressTransformer<Response<List<EventFlowRes>>>(getContext(), ProgressText.load))
                .subscribe(new ResponseObserver<List<EventFlowRes>>(getContext()) {
                    @Override
                    public void onSuccess(List<EventFlowRes> data) {
                        eventFlowRes.clear();
                        if (data.size() > 0) {
                            eventFlowRes.addAll(data);
                            adapter.addAll(eventFlowRes);
                        }
                        setNoData(adapter.size() == 0);
                    }
                });
    }
}
