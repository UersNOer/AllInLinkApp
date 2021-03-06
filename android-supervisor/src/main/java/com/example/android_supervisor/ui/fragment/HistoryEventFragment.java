package com.example.android_supervisor.ui.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.LinearLayout;

import com.bigkoo.pickerview.TimePickerView;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.example.android_supervisor.R;
import com.example.android_supervisor.common.UserSession;
import com.example.android_supervisor.entities.Attach;
import com.example.android_supervisor.entities.EventPara;
import com.example.android_supervisor.entities.EventRes;
import com.example.android_supervisor.http.ServiceGenerator;
import com.example.android_supervisor.http.common.ProgressTransformer;
import com.example.android_supervisor.http.common.RefreshLayoutTransformer;
import com.example.android_supervisor.http.request.JsonBody;
import com.example.android_supervisor.http.request.QueryBody;
import com.example.android_supervisor.http.response.Response;
import com.example.android_supervisor.http.response.ResponseObserver;
import com.example.android_supervisor.http.service.QuestionService;
import com.example.android_supervisor.ui.EventDetailActivity;
import com.example.android_supervisor.ui.EventNewActivity;
import com.example.android_supervisor.ui.adapter.EventItemAdapter;
import com.example.android_supervisor.ui.view.ProgressText;
import com.example.android_supervisor.utils.DateUtils;
import com.example.android_supervisor.utils.DialogUtils;
import com.example.android_supervisor.utils.ToastUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;

/**
 * @author yj  ?????????
 */
public class HistoryEventFragment extends SearchListFragment {
    private EventItemAdapter adapter;

    private int pageIndex = 1;
    private int pageSize = 30;
    private String searchKey;
    Date startDate,endData;

    boolean isTimeSerach = false;
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        etSearch.setHint("???????????????????????????????????????????????????");
        etSearch.setInputType(EditorInfo.TYPE_TEXT_VARIATION_LONG_MESSAGE);
        setSearchInfo("???%d?????????", 0);
        initTime();
        adapter = new EventItemAdapter(getContext(),0);
        setAdapter(adapter);
        adapter.setShowCaseType(true);
        fetchData(false, false);

        iv_search_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (btn_stime.getText().toString().trim().equals("????????????")){
                    ToastUtils.show(getContext(),"?????????????????????");
                    return;
                }
                if (btn_etime.getText().toString().trim().equals("????????????")){
                    ToastUtils.show(getContext(),"?????????????????????");
                    return;
                }

                if (!startDate.before(endData)){
                    ToastUtils.show(getContext(),"??????????????????????????????");
                    return;
                }
                isTimeSerach = true;
                onSearchTime();



            }
        });

        ll_serach_time.setVisibility(View.VISIBLE);
    }

    @Override
    public RecyclerView.ItemDecoration getItemDecoration(Context context) {
        DividerItemDecoration divider = new DividerItemDecoration(context, LinearLayout.VERTICAL);
        divider.setDrawable(getResources().getDrawable(R.drawable.divider_space));
        return divider;
    }

    @Override
    public void onRefresh(RefreshLayout refreshLayout) {
        fetchData(true, false);
    }

    @Override
    public void onLoadMore(RefreshLayout refreshLayout) {
        fetchData(false, true);
    }

    @Override
    public void onSearch(String key) {
        searchKey = key;
        fetchData(false, false);
    }

    public void onSearchTime() {
        fetchData(false, false);
    }

    private void fetchData(boolean refresh, boolean loadMore) {
        if (!loadMore) {
            pageIndex = 1;
            adapter.clear();
        }
        QueryBody queryBody;
        if (!isTimeSerach){
            if (TextUtils.isEmpty(searchKey)) {
                queryBody = new QueryBody.Builder()
                        .pageIndex(pageIndex)
                        .pageSize(pageSize)
                        .eq("createId", UserSession.getUserId(getContext()))
//                    .desc("caseCode")
//                    .desc("caseLevel")
                        .create();
            } else {
                queryBody = new QueryBody.Builder()
                        .pageIndex(pageIndex)
                        .pageSize(pageSize)
                        .eq("createId", UserSession.getUserId(getContext()))
                        .beginGroup()
                        .like("caseCode", searchKey)
                        .or()
                        .like("position", searchKey)
                        .like("questionDesc", searchKey)
                        .endGroup()
//                    .desc("caseCode")
                        .create();
            }
        }else {
            queryBody = new QueryBody.Builder()
                    .pageIndex(pageIndex)
                    .pageSize(pageSize)
                    .startReportTime(btn_stime.getText().toString().trim())
                    .endReportTime(btn_etime.getText().toString().trim())
                    .eq("createId", UserSession.getUserId(getContext()))
                    .beginGroup()
                    .like("caseCode", searchKey)
                    .or()
                    .like("position", searchKey)
                    .like("questionDesc", searchKey)
                    .endGroup()
//                    .desc("caseCode")
                    .create();
        }

        ObservableTransformer<Response<List<EventRes>>, Response<List<EventRes>>> transformer;
        if (refresh || loadMore) {
            transformer = new RefreshLayoutTransformer<>(mRefreshLayout);
        } else {
            transformer = new ProgressTransformer<>(getActivity(), ProgressText.load);
        }
        QuestionService questionService = ServiceGenerator.create(QuestionService.class);
        Observable<Response<List<EventRes>>> observable = questionService.searchEvent(queryBody);
        observable.compose(this.<Response<List<EventRes>>>bindToLifecycle())
                .observeOn(AndroidSchedulers.mainThread())
                .compose(transformer)
                .subscribe(new ResponseObserver<List<EventRes>>(getContext()) {
                    @Override
                    public void onSuccess(List<EventRes> data) {
                        if (data.size() > 0) {
                            adapter.addAll(data);
                            adapter.setSearchKey(searchKey);
                            pageIndex++;
                        } else {
                            setNoMoreData(true);
                        }
                        setNoData(adapter.size() == 0);
                        setSearchInfo("???%d?????????", adapter.size());
                    }
                });
    }

    @Override
    public void onItemClick(RecyclerView parent, View view, int position, long id) {
        EventRes data = adapter.get(position);
        Intent intent = new Intent(getContext(), EventDetailActivity.class);
        intent.putExtra("id", data.getId());
        intent.putExtra("status", 1);
        intent.putExtra("edit", 2);
        intent.putExtra("handled", false);
        ArrayList arrayList = (ArrayList) data.getAttaches();
        intent.putExtra("attachs",arrayList);
        startActivity(intent);
        getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }

    @Override
    public boolean onItemLongClick(final RecyclerView parent, final View view, final int position, final long id) {
        final EventRes eventRes = adapter.get(position);
        DialogUtils.askYesNo(getContext(), "??????????????????", eventRes.getTitle() + "??????", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                EventRes data = adapter.get(position);
//                Intent intent = new Intent(getContext(), EventEditActivity.class);
//                intent.putExtra("id", data.getId());
//                intent.putExtra("status", 1);
//                startActivityForResult(intent, 1);

                EventPara eventPara = new EventPara();
                String[] areaCode = eventRes.getAreaCode().split("-");
                String[] typeId = eventRes.getTypeId().split("-");
                eventPara.setAddress(eventRes.getAddress());
                eventPara.setAreaCode(areaCode[areaCode.length - 1]);
                eventPara.setGeoX(eventRes.getGeoX());
                eventPara.setGeoY(eventRes.getGeoY());
                eventPara.setDesc(eventRes.getDesc());
                eventPara.setStandardId(eventRes.getStandardId());
                eventPara.setStandardName(eventRes.getStandardName());
                eventPara.setTypeName(eventRes.getTypeName());
                eventPara.setTypeId(typeId[typeId.length - 1]);
                eventPara.setLevel(eventRes.getLevel());
                eventPara.setProcId(eventRes.getProcDefId());
                eventPara.setUserName(UserSession.getUserName(getContext()));
                eventPara.setMobile(UserSession.getMobile(getContext()));
                ArrayList<Attach> attaches=new ArrayList<>();
                ArrayList<Attach> attachesLater=new ArrayList<>();
                for (Attach attach : eventRes.getAttaches()) {
                    if (attach.getUsage()==3){
                        attachesLater.add(attach);
                    }else
                        attaches.add(attach);
                }
                eventPara.setLaterAttaches(attachesLater);
                eventPara.setAttaches(attaches);
                eventPara.setSource(data.getSource());
                Intent intent = null;
//                if (eventPara.getLaterAttaches() != null && eventPara.getLaterAttaches().size() > 0) {
//                    intent = new Intent(getContext(), FastReportActivity.class);
//                } else
//                    intent = new Intent(getContext(), EventNewActivity.class);
                intent = new Intent(getContext(), EventNewActivity.class);
                intent.putExtra("eventPara",eventPara);

                startActivityForResult(intent, 1);

//                fetchReport(eventPara);
                dialog.dismiss();
            }
        }, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        return super.onItemLongClick(parent, view, position, id);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == Activity.RESULT_OK) {
            fetchData(true,false);
        }
    }

    private void fetchReport(EventPara eventPara) {
        QuestionService questionService = ServiceGenerator.create(QuestionService.class);
        questionService.addEvent(new JsonBody(eventPara))
                .observeOn(AndroidSchedulers.mainThread())
                .compose(new ProgressTransformer<Response<String>>(getContext(), ProgressText.submit))
                .subscribe(new ResponseObserver<String>(getContext()) {
                    @Override
                    public void onSuccess(String title) {
                        String message = "????????????\"" + title + "\"????????????";
                        DialogUtils.show(getContext(), message, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                fetchData(true, false);
                            }
                        });
                        //RxBus.getDefault().post(new EventFreshCase());
                    }

                    @Override
                    public void onFailure(int code, String errMsg) {
                        super.onFailure(code, errMsg);
                        ToastUtils.show(getContext(), "????????????????????????");
                    }
                });
    }

    private void initTime() {


        btn_stime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                TimePickerDialog dialogAll = new TimePickerDialog.Builder()
//                        .setType(Type.HOURS_MINS)
//                        .setThemeColor(R.color.select_color)
//                        .setCallBack(new OnDateSetListener() {
//                            @Override
//                            public void onDateSet(TimePickerDialog timePickerView, long millseconds) {
//                                startDate = new Date(millseconds);
//                                btn_qj_start.setText(DateUtils.format(startDate,0));
//                                btn_qj_start.setTextColor(getResources().getColor(R.color.select_color));
//                            }
//                        })
//                        .setTitleStringId("???????????????")
//                        .build();
//                dialogAll.show(getSupportFragmentManager(), "All");


                TimePickerView pvTime = new TimePickerView.Builder(getContext(), new TimePickerView.OnTimeSelectListener() {
                    @Override
                    public void onTimeSelect(Date date, View v) {
//                        btn_date.setText(getTime(date));
                        startDate = date;
                        btn_stime.setText(DateUtils.format(startDate,1));
                        btn_stime.setTextColor(getResources().getColor(R.color.select_color));
                    }
                })
                        .setType(new boolean[]{true, true, true, false, false, false})// ??????????????????
                        .setCancelText("??????")//??????????????????
                        .setSubmitText("??????")//??????????????????
//                .setContentSize(18)//??????????????????
//                .setTitleSize(20)//??????????????????
//                //.setTitleText("Title")//????????????
//                .setOutSideCancelable(true)//???????????????????????????????????????????????????????????????
//                .isCyclic(true)//??????????????????
//                //.setTitleColor(Color.BLACK)//??????????????????
//                .setSubmitColor(Color.BLUE)//????????????????????????
//                .setCancelColor(Color.BLUE)//????????????????????????
//                //.setTitleBgColor(0xFF666666)//?????????????????? Night mode
//                .setBgColor(0xFF333333)//?????????????????? Night mode
////                .setDate(selectedDate)// ?????????????????????????????????????????????*/
////                .setRangDate(startDate,endDate)//???????????????????????????
//                //.setLabel("???","???","???","???","???","???")//?????????????????????????????????
                        .isCenterLabel(false) //?????????????????????????????????label?????????false?????????item???????????????label???
                        //.isDialog(true)//??????????????????????????????
                        .build();

                pvTime.show();


            }
        });

        btn_etime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TimePickerView pvTime = new TimePickerView.Builder(getContext(), new TimePickerView.OnTimeSelectListener() {
                    @Override
                    public void onTimeSelect(Date date, View v) {
                        endData = date;
                        btn_etime.setText(DateUtils.format(endData,1));
                        btn_etime.setTextColor(getResources().getColor(R.color.select_color));
//                            }
                    }
                })
                        .setType(new boolean[]{true, true, true, false, false, false})// ??????????????????
                        .setCancelText("??????")//??????????????????
                        .setSubmitText("??????")//??????????????????
                        .isCenterLabel(false) //?????????????????????????????????label?????????false?????????item???????????????label???
                        .build();

                pvTime.show();

            }
        });
    }

    public void setShowTime() {

        if (ll_serach_time!=null){
            ll_serach_time.setVisibility(View.VISIBLE);
        }

    }
}
