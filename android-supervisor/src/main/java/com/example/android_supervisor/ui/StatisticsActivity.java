package com.example.android_supervisor.ui;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.example.android_supervisor.R;
import com.example.android_supervisor.common.UserSession;
import com.example.android_supervisor.http.ServiceGenerator;
import com.example.android_supervisor.http.common.ProgressTransformer;
import com.example.android_supervisor.http.response.Response;
import com.example.android_supervisor.http.response.ResponseObserver;
import com.example.android_supervisor.http.service.QuestionService;
import com.example.android_supervisor.ui.view.ProgressText;
import com.example.android_supervisor.utils.CalendarUtils;
import com.example.android_supervisor.utils.DateUtils;
import com.google.android.material.tabs.TabLayout;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Function;

/**
 * @author 工作统计
 */
public class StatisticsActivity extends BaseActivity implements TabLayout.OnTabSelectedListener{
    @BindView(R.id.tl_statistics_date_tabs)
    TabLayout tbDateTabs;

    @BindView(R.id.tv_statistics_duration)
    TextView tvDuration;

    @BindView(R.id.tv_statistics_report_count)
    TextView tvReportCount;

    @BindView(R.id.tv_statistics_handle_count)
    TextView tvHandleCount;

    @BindView(R.id.tv_statistics_valid_count)
    TextView tvValidCount;

    @BindView(R.id.tv_statistics_invalid_count)
    TextView tvInvalidCount;

    @BindView(R.id.tv_statistics_hc_count)
    TextView tvHcCount;

    @BindView(R.id.tv_statistics_hs_count)
    TextView tvHsCount;

    @BindView(R.id.lc_statistics_line_chart)
    LineChart mLineChart;

    private static final DateFormat DATE_FORMAT_MONTH = new SimpleDateFormat("yyyy-MM");
    private static final DateFormat DATE_FORMAT_DAY = new SimpleDateFormat("yyyy-MM-dd");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistics);
        ButterKnife.bind(this);
        tbDateTabs.addOnTabSelectedListener(this);
        setDateTabPosition(0);

        initLineChart();
    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        setDateTabPosition(tab.getPosition());
    }

    public void setDateTabPosition(int position) {
        Date begin = null;
        Date end = null;
        Date now = new Date();
        switch (position) {
            case 0: // 周
                begin = CalendarUtils.getFirstDayOfWeek(now);
                end = CalendarUtils.getLastDayOfWeek(now);
                break;
            case 1: // 月
                begin = CalendarUtils.getFirstDayOfMonth(now);
                end = CalendarUtils.getLastDayOfMonth(now);
                break;
            case 2: // 年
                begin = CalendarUtils.getFirstDayOfYear(now);
                end = CalendarUtils.getLastDayOfYear(now);
                break;
        }

        String beginDate = DateUtils.format(begin, 1);
        String endDate = DateUtils.format(end, 1);
        tvDuration.setText(beginDate + " 至 " + endDate);

        fetchData(begin, end);
        fetchChartData(position, begin, end);
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }

    private void initLineChart() {
        mLineChart.setDragEnabled(false);
        mLineChart.setScaleEnabled(false);
        mLineChart.setDoubleTapToZoomEnabled(false);
//        mLineChart.setVisibleYRange(0, 100, YAxis.AxisDependency.LEFT);

        XAxis xAxis = mLineChart.getXAxis();
        xAxis.setTextColor(0xff808080);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setGridLineWidth(.5f);
        xAxis.setGridColor(0xfff0f0f0);
        xAxis.setAxisLineWidth(1f);
        xAxis.setAxisLineColor(0xffe0e0e0);
        xAxis.setGranularity(1);

        YAxis yAxisLeft = mLineChart.getAxisLeft();
        yAxisLeft.setTextColor(0xff808080);
        yAxisLeft.setGridLineWidth(.5f);
        yAxisLeft.setGridColor(0xfff0f0f0);
        yAxisLeft.setAxisLineWidth(1f);
        yAxisLeft.setAxisLineColor(0xffe0e0e0);
        yAxisLeft.setStartAtZero(true);
        yAxisLeft.setGranularity(1);

        YAxis yAxisRight = mLineChart.getAxisRight();
        yAxisRight.setEnabled(false);

        Legend legend = mLineChart.getLegend();
        legend.setTextColor(0xff808080);
        legend.setForm(Legend.LegendForm.LINE);
        legend.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        legend.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        legend.setYOffset(10.0f);

        Description description = mLineChart.getDescription();
        description.setText("");
        mLineChart.invalidate();
    }

    private void fetchData(Date begin, Date end) {


        String beginTime = DateUtils.format(begin);
        String endTime = DateUtils.format(end);
        String userId = UserSession.getUserId(this);

        QuestionService questionService = ServiceGenerator.create(QuestionService.class);
        Observable<Response<Map<String, Integer>>> observable = questionService.getSelfInfo(beginTime, endTime, userId);
        observable.compose(this.<Response<Map<String, Integer>>>bindToLifecycle())
                .observeOn(AndroidSchedulers.mainThread())
                .compose(new ProgressTransformer<Response<Map<String, Integer>>>(this, ProgressText.load))
                .subscribe(new ResponseObserver<Map<String, Integer>>(this) {

                    @Override
                    public void onSuccess(Map<String, Integer> data) {
                        try{
                            Integer reportCount = data.get("reportCount");
                            Integer handleCount = data.get("completedCount");
                            Integer validCount = data.get("validReportCount");
                            Integer invalidCount = data.get("cancelCount");
                            Integer hcCount = data.get("checkedCount");
                            Integer hsCount = data.get("verifiedCount");
                            tvReportCount.setText(reportCount == null? "":String.valueOf(reportCount));
                            tvHandleCount.setText(handleCount ==null? "":String.valueOf(handleCount));
                            tvValidCount.setText(validCount ==null? "":String.valueOf(validCount));
                            tvInvalidCount.setText(invalidCount ==null? "":String.valueOf(invalidCount));
                            tvHcCount.setText(hcCount ==null? "":String.valueOf(hcCount));
                            tvHsCount.setText(hsCount ==null? "":String.valueOf(hsCount));


                        }catch (Exception e){
                            e.printStackTrace();
                        }

                    }
                });
    }

    private void fetchChartData(final int type, final Date begin, final Date end) {
        final String beginTime = DateUtils.format(begin);
        String endTime = DateUtils.format(end);
        String userId = UserSession.getUserId(this);

        QuestionService questionService = ServiceGenerator.create(QuestionService.class);
        Observable<Response<List<Map<String, Object>>>> observable = questionService.getSelfTotal(type == 2 ? 1 : 0, beginTime, endTime, userId);
        observable.compose(this.<Response<List<Map<String, Object>>>>bindToLifecycle())
                .observeOn(AndroidSchedulers.mainThread())
                .map(new Function<Response<List<Map<String, Object>>>, Response<List<Map<String, Object>>>>() {
                    @Override
                    public Response<List<Map<String, Object>>> apply(Response<List<Map<String, Object>>> response) throws Exception {
                        List<Map<String, Object>> fields = response.getData();
                        if (fields == null) {
                            throw new NullPointerException();
                        }
                        Map<String, Map<String, Object>> map = new HashMap<>();
                        for (Map<String, Object> field: fields) {
                            Object dateTime = field.get("dateTime");
                            if (dateTime == null) continue;

                            String _dateTime = dateTime.toString();
                            if (!TextUtils.isEmpty(_dateTime)) {
                                map.put(_dateTime.trim(), field);
                            }
                        }

                        List<Map<String, Object>> newFields = new ArrayList<>();

                        Calendar calendar = Calendar.getInstance();
                        calendar.setTime(begin);
                        Date date = new Date(begin.getTime());
                        while (date.before(end)) {
                            String dateTime;
                            if (type == 2) {
                                dateTime = DATE_FORMAT_MONTH.format(date);
                            } else {
                                dateTime = DATE_FORMAT_DAY.format(date);
                            }
                            Map<String, Object> newField = new HashMap<>();
                            newField.put("dateTime", dateTime);
                            if (map.containsKey(dateTime)) {
                                newField.put("eventCount", map.get(dateTime).get("count"));
                                newField.put("taskCount", map.get(dateTime).get("secondCount"));
                            } else {
                                newField.put("eventCount", "0");
                                newField.put("taskCount", "0");
                            }
                            newFields.add(newField);

                            if (type == 2) {
                                calendar.add(Calendar.MONTH, 1);
                            } else {
                                calendar.add(Calendar.DAY_OF_MONTH, 1);
                            }
                            date = calendar.getTime();
                        }

                        response.setData(newFields);
                        return response;
                    }
                })
                .compose(new ProgressTransformer<Response<List<Map<String, Object>>>>(this, ProgressText.load))
                .subscribe(new ResponseObserver<List<Map<String, Object>>>(this) {
                    @Override
                    public void onSuccess(List<Map<String, Object>> data) {
                        List<ILineDataSet> dataSets = new ArrayList<>();
                        List<Entry> eventEntries = new ArrayList<>();
                        List<Entry> taskEntries = new ArrayList<>();
                        for (int i = 0; i < data.size(); i++) {
                            Map<String, Object> field = data.get(i);
                            float eventCountVal = 0;
                            Object eventCount = field.get("eventCount");
                            if (eventCount != null) {
                                try {
                                    eventCountVal = Float.valueOf(eventCount.toString());
                                } catch (NumberFormatException e) {
                                }
                            }
                            float taskCountVal = 0;
                            Object taskCount = field.get("taskCount");
                            if (taskCount != null) {
                                try {
                                    taskCountVal = Float.valueOf(taskCount.toString());
                                } catch (NumberFormatException e) {
                                }
                            }
                            eventEntries.add(new Entry(i, (int) eventCountVal));
                            taskEntries.add(new Entry(i, (int) taskCountVal));
                        }
                        int blueColor = getResources().getColor(R.color.chart_blue);
                        int greenColor = getResources().getColor(R.color.chart_green);
                        dataSets.add(newLineChartData(eventEntries, "上报案件总数", blueColor));
                        dataSets.add(newLineChartData(taskEntries, "核实核查总数", greenColor));

                        switch (type) {
                            case 0:
                                setWeekQuarters();
                                break;
                            case 1:
                                setMonthQuarters();
                                break;
                            case 2:
                                setYearQuarters();
                                break;
                        }
                        mLineChart.setData(new LineData(dataSets));
                        mLineChart.invalidate();
                    }
                });
    }

    private ILineDataSet newLineChartData(List<Entry> entries, String label, int color) {
        LineDataSet dataSet = new LineDataSet(entries, label);
        dataSet.setCircleRadius(2f);
        dataSet.setCircleHoleRadius(1f);
        dataSet.setLineWidth(.5f);
        dataSet.setColor(color);
        dataSet.setCircleColor(color);
        dataSet.setValueTextColor(color);
        dataSet.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                return value > 0 ? "" + (int) value : "";
            }
        });
        return dataSet;
    }

    private void setWeekQuarters() {
        final String[] quarters = new String[] {
                "周一", "周二", "周三", "周四", "周五", "周六", "周日"};
        ValueFormatter formatter = new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                int index = (int) value;
                return (index >= 0 && index < quarters.length) ? quarters[index] : "";
            }
        };
        XAxis xAxis = mLineChart.getXAxis();
        xAxis.setGranularity(1f);
        xAxis.setValueFormatter(formatter);
    }

    private void setMonthQuarters() {
        ValueFormatter formatter = new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                return (int) (value + 1) + "号";
            }
        };
        XAxis xAxis = mLineChart.getXAxis();
        xAxis.setGranularity(1f);
        xAxis.setValueFormatter(formatter);
    }

    private void setYearQuarters() {
        final String[] quarters = new String[] {
                "一月", "二月", "三月", "四月", "五月", "六月",
                "七月", "八月", "九月", "十月", "十一月", "十二月"};
        ValueFormatter formatter = new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                int index = (int) value;
                return (index >= 0 && index < quarters.length) ? quarters[index] : "";
            }
        };
        XAxis xAxis = mLineChart.getXAxis();
        xAxis.setGranularity(1f);
        xAxis.setValueFormatter(formatter);
    }

    @Override
    public void finish() {
        super.finish();
        setResult(Activity.RESULT_OK);
        overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
    }
}
