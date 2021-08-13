package com.example.android_supervisor.ui.fragment;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.android_supervisor.R;
import com.example.android_supervisor.entities.CensusTaskRes;
import com.example.android_supervisor.http.ServiceGenerator;
import com.example.android_supervisor.http.common.ProgressTransformer;
import com.example.android_supervisor.http.response.Response;
import com.example.android_supervisor.http.response.ResponseObserver;
import com.example.android_supervisor.http.service.QuestionService;
import com.example.android_supervisor.ui.view.ProgressText;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;

/**
 * @author wujie
 */
public class CensusTaskInfoFragment extends BaseFragment {
    @BindView(R.id.tv_census_info_code)
    TextView tvCode;

    @BindView(R.id.tv_census_info_name)
    TextView tvName;

    @BindView(R.id.tv_census_info_type)
    TextView tvType;

    @BindView(R.id.tv_census_info_begin_time)
    TextView tvBeginTime;

    @BindView(R.id.tv_census_info_end_time)
    TextView tvEndTime;

    @BindView(R.id.tv_census_info_area)
    TextView tvArea;

    @BindView(R.id.tv_census_info_desc)
    TextView tvDesc;

    @BindView(R.id.tv_census_info_people)
    TextView tvPeople;

    @BindView(R.id.tv_census_info_tasktype)
    TextView tvTaskType;

    private String taskClass = "";
    private String id;

    public static CensusTaskInfoFragment newInstance(String id) {
        CensusTaskInfoFragment fragment = new CensusTaskInfoFragment();
        Bundle bundle = new Bundle();
        bundle.putString("id", id);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_census_info, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);

        Bundle bundle = getArguments();
        if (bundle != null) {
            this.id = bundle.getString("id");
        }

        fetchData();
    }

    private void fetchData() {
        QuestionService questionService = ServiceGenerator.create(QuestionService.class);
        Observable<Response<CensusTaskRes>> observable = questionService.getCensusTaskById(id);
        observable.compose(this.<Response<CensusTaskRes>>bindToLifecycle())
                .observeOn(AndroidSchedulers.mainThread())
                .compose(new ProgressTransformer<Response<CensusTaskRes>>(getContext(), ProgressText.load))
                .subscribe(new ResponseObserver<CensusTaskRes>(getContext()) {
                    @Override
                    public void onSuccess(CensusTaskRes data) {
                        tvCode.setText(String.valueOf(data.getCode()));
                        tvName.setText(data.getName());
                        tvType.setText(data.getTypeName());
//                        tvBeginTime.setText(DateUtils.format(data.getBeginTime()));
//                        tvEndTime.setText(DateUtils.format(data.getEndTime()));

                        tvBeginTime.setText(data.getBeginTime());
                        tvEndTime.setText(data.getEndTime());

                        tvArea.setText(data.getAreaName());
                        tvDesc.setText(data.getDesc());
                        tvPeople.setText(data.getUserName());

                        //任务类型
                        tvTaskType.setText(data.getType());

                        taskClass =data.getTypeName();
                    }
                });
    }

    public String getTaskClass() {
        return taskClass;
    }
}
