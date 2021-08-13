package com.example.android_supervisor.ui;

import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.cncoderx.recyclerviewhelper.utils.DividerItemDecoration;
import com.haibin.calendarview.Calendar;
import com.haibin.calendarview.CalendarLayout;
import com.haibin.calendarview.CalendarView;
import com.example.android_supervisor.R;
import com.example.android_supervisor.common.UserSession;
import com.example.android_supervisor.entities.CheckInRes;
import com.example.android_supervisor.entities.WorkScheRes;
import com.example.android_supervisor.http.ServiceGenerator;
import com.example.android_supervisor.http.common.ProgressTransformer;
import com.example.android_supervisor.http.request.JsonBody;
import com.example.android_supervisor.http.request.QueryBody;
import com.example.android_supervisor.http.response.Response;
import com.example.android_supervisor.http.response.ResponseObserver;
import com.example.android_supervisor.http.service.PublicService;
import com.example.android_supervisor.ui.adapter.CheckInAdapter;
import com.example.android_supervisor.ui.adapter.WorkScheAdapter;
import com.example.android_supervisor.ui.view.ProgressText;
import com.example.android_supervisor.utils.DateUtils;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * @author wujie
 */
public class CommutePlanActivity extends BaseActivity implements CalendarView.OnCalendarSelectListener {
    @BindView(R.id.layout_commute_plan_calendar)
    CalendarLayout calendarLayout;

    @BindView(R.id.cv_commute_plan_calendar)
    CalendarView calendarView;

    @BindView(R.id.tv_commute_plan_calendar_title)
    TextView tvCalendarTitle;

    @BindView(R.id.iv_commute_plan_calendar_left)
    ImageView ivCalendarLeft;

    @BindView(R.id.iv_commute_plan_calendar_right)
    ImageView ivCalendarRight;

    @BindView(R.id.sv_commute_plan_content)
    ScrollView svContent;

    @BindView(R.id.rv_commute_plan_schedule)
    RecyclerView rvSchedule;

    @BindView(R.id.rv_commute_plan_check_in)
    RecyclerView rvCheckIn;

    private WorkScheAdapter workScheAdapter;
    private CheckInAdapter checkInAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_commute_plan);
        ButterKnife.bind(this);

        initCalendar();
        initCommuteList();
        initCheckInList();

        calendarView.setOnCalendarSelectListener(this);
        Calendar calendar = calendarView.getSelectedCalendar();
        onCalendarSelect(calendar, false);
    }

    private void initCalendar() {
        calendarView.setMonthViewScrollable(false);
        calendarView.setWeekViewScrollable(false);
        calendarView.setOnMonthChangeListener(onMonthChangeListener);
        int year = calendarView.getCurYear();
        int month = calendarView.getCurMonth();
        tvCalendarTitle.setText(String.format(Locale.CHINESE, "%04d年%02d月", year, month));
    }

    private void initCommuteList() {
        Drawable divider = getResources().getDrawable(R.drawable.input_divider);
        int divHeight = divider.getIntrinsicHeight();
        DividerItemDecoration itemDecoration = new DividerItemDecoration(divider, divHeight);

        rvSchedule.setLayoutManager(new LinearLayoutManager(this));
        rvSchedule.addItemDecoration(itemDecoration);

        workScheAdapter = new WorkScheAdapter();
        workScheAdapter.setNotifyOnChange(false);
        rvSchedule.setAdapter(workScheAdapter);

        rvSchedule.setLayoutManager(new LinearLayoutManager(this));
        rvSchedule.addItemDecoration(itemDecoration);
    }

    private void initCheckInList() {
        Drawable divider = getResources().getDrawable(R.drawable.input_divider);
        int divHeight = divider.getIntrinsicHeight();
        DividerItemDecoration itemDecoration = new DividerItemDecoration(divider, divHeight);

        rvCheckIn.setLayoutManager(new LinearLayoutManager(this));
        rvCheckIn.addItemDecoration(itemDecoration);

        checkInAdapter = new CheckInAdapter();
        checkInAdapter.setNotifyOnChange(false);
        rvCheckIn.setAdapter(checkInAdapter);

        rvCheckIn.setLayoutManager(new LinearLayoutManager(this));
        rvCheckIn.addItemDecoration(itemDecoration);
    }

    private void getWorkScheList(final Date begin, final Date end) {
        workScheAdapter.clear();
        checkInAdapter.clear();

        HashMap<String, String> map = new HashMap<>();
        map.put("userId", UserSession.getUserId(this));
        map.put("beginTime", DateUtils.format(begin));
        map.put("endTime", DateUtils.format(end));
        JsonBody jsonBody = new JsonBody(map);

        PublicService publicService = ServiceGenerator.create(PublicService.class);
        Observable<Response<List<WorkScheRes>>> observable = publicService.getWorkScheList(jsonBody);
        observable.compose(this.<Response<List<WorkScheRes>>>bindToLifecycle())
                .observeOn(AndroidSchedulers.mainThread())
                .flatMap(new Function<Response<List<WorkScheRes>>, ObservableSource<Response<List<CheckInRes>>>>() {
                    @Override
                    public ObservableSource<Response<List<CheckInRes>>> apply(Response<List<WorkScheRes>> response) throws Exception {
                        if (response.isSuccess()) {
                            workScheAdapter.addAll(response.getData());
                            workScheAdapter.notifyDataSetChanged();
                        }
                        QueryBody queryBody = new QueryBody.Builder()
                                .eq("userId", UserSession.getUserId(getApplicationContext()))
                                .eq("workDate", DateUtils.format(begin, 1))
                                .create();
                        PublicService publicService = ServiceGenerator.create(PublicService.class);
                        return publicService.getCheckInList(queryBody);
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .compose(new ProgressTransformer<Response<List<CheckInRes>>>(this, ProgressText.load))
                .subscribe(new ResponseObserver<List<CheckInRes>>(this) {
                    @Override
                    public void onSuccess(List<CheckInRes> data) {
                        checkInAdapter.addAll(data);
                        checkInAdapter.notifyDataSetChanged();
                    }
                });
    }

    @OnClick(R.id.iv_commute_plan_calendar_left)
    public void onCalendarLeft(View v) {
        calendarView.scrollToPre(true);
    }

    @OnClick(R.id.iv_commute_plan_calendar_right)
    public void onCalendarRight(View v) {
        calendarView.scrollToNext(true);
    }

    private void updateCalendarTitle() {
        int year = calendarView.getCurYear();
        int month = calendarView.getCurMonth();
        tvCalendarTitle.setText(String.format(Locale.CHINESE, "%04d年%02d月", year, month));
    }

    @OnClick(R.id.iv_commute_plan_calendar_expand)
    public void onCalendarExpand(View v) {
        if (calendarLayout.isExpand()) {
            calendarLayout.shrink();
            ((ImageView) v).setImageResource(R.drawable.calendar_expand);
        } else {
            calendarLayout.expand();
            ((ImageView) v).setImageResource(R.drawable.calendar_shrink);
        }
    }

    @Override
    public void onCalendarOutOfRange(Calendar calendar) {
//        if (workScheAdapter.size() > 0) {
//            workScheAdapter.clear();
//        }
    }

    @Override
    public void onCalendarSelect(Calendar calendar, boolean isClick) {
//        if (workScheAdapter.size() > 0) {
//            workScheAdapter.clear();
//        }

        java.util.Calendar c = java.util.Calendar.getInstance();
        c.setTimeInMillis(calendar.getTimeInMillis());

        c.set(java.util.Calendar.HOUR_OF_DAY, 0);
        c.set(java.util.Calendar.MINUTE, 0);
        c.set(java.util.Calendar.SECOND, 0);
        Date begin = c.getTime();

        c.set(java.util.Calendar.HOUR_OF_DAY, c.getActualMaximum(java.util.Calendar.HOUR_OF_DAY));
        c.set(java.util.Calendar.MINUTE, c.getActualMaximum(java.util.Calendar.MINUTE));
        c.set(java.util.Calendar.SECOND, c.getActualMaximum(java.util.Calendar.SECOND));
        Date end = c.getTime();

        getWorkScheList(begin, end);
    }

    CalendarView.OnMonthChangeListener onMonthChangeListener = new CalendarView.OnMonthChangeListener() {
        @Override
        public void onMonthChange(int year, int month) {
            Calendar minCalendar = calendarView.getMinRangeCalendar();
            if (year < minCalendar.getYear()) {
                ivCalendarLeft.setVisibility(View.GONE);
            } else if (year == minCalendar.getYear() && month <= minCalendar.getMonth()) {
                ivCalendarLeft.setVisibility(View.GONE);
            } else {
                ivCalendarLeft.setVisibility(View.VISIBLE);
            }
            Calendar maxCalendar = calendarView.getMaxRangeCalendar();
            if (year > maxCalendar.getYear()) {
                ivCalendarRight.setVisibility(View.GONE);
            } else if (year == maxCalendar.getYear() && month >= maxCalendar.getMonth()) {
                ivCalendarRight.setVisibility(View.GONE);
            } else {
                ivCalendarRight.setVisibility(View.VISIBLE);
            }
            tvCalendarTitle.setText(String.format(Locale.CHINESE, "%04d年%02d月", year, month));
        }
    };
}
