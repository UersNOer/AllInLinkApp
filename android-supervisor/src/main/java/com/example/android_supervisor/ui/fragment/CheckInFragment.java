package com.example.android_supervisor.ui.fragment;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.example.android_supervisor.R;
import com.example.android_supervisor.common.UserSession;
import com.example.android_supervisor.entities.CheckInPara;
import com.example.android_supervisor.entities.CheckInRes;
import com.example.android_supervisor.entities.WorkScheRes;
import com.example.android_supervisor.http.ServiceGenerator;
import com.example.android_supervisor.http.common.ProgressTransformer;
import com.example.android_supervisor.http.request.JsonBody;
import com.example.android_supervisor.http.request.QueryBody;
import com.example.android_supervisor.http.response.Response;
import com.example.android_supervisor.http.response.ResponseObserver;
import com.example.android_supervisor.http.service.PublicService;
import com.example.android_supervisor.map.MapLocationAPI;
import com.example.android_supervisor.ui.view.ProgressText;
import com.example.android_supervisor.ui.view.TickView2;
import com.example.android_supervisor.utils.CoordinateUtils;
import com.example.android_supervisor.utils.DateUtils;
import com.example.android_supervisor.utils.LogUtils;
import com.example.android_supervisor.utils.ToastUtils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;

/**
 * @author wujie
 */
public class CheckInFragment extends BaseFragment implements AMapLocationListener {
    @BindView(R.id.btn_check_in_button)
    TextView btnCheckIn;

    @BindView(R.id.iv_check_in_tick)
    TickView2 ivTick;

    @BindView(R.id.tv_check_in_info)
    TextView tvInfo;

    @BindView(R.id.tvAddress)
    TextView tvAddress;

    private WorkScheRes workScheRes;

    public void setData(WorkScheRes workScheRes) {
        this.workScheRes = workScheRes;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_check_in, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        fetchData();

        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        String time = String.format("%02d:%02d", hour, minute);
        timeTick(time);
        setLocation();
    }

    private void fetchData() {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.HOUR_OF_DAY, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        Date begin = c.getTime();

        c.set(Calendar.HOUR_OF_DAY, c.getActualMaximum(Calendar.HOUR_OF_DAY));
        c.set(Calendar.MINUTE, c.getActualMaximum(Calendar.MINUTE));
        c.set(Calendar.SECOND, c.getActualMaximum(Calendar.SECOND));
        Date end = c.getTime();

        QueryBody queryBody = new QueryBody.Builder()
                .eq("planId", workScheRes.getId())
                .eq("userId", UserSession.getUserId(getActivity()))
                .eq("workDate", DateUtils.format(begin, 1))
                .create();

        PublicService publicService = ServiceGenerator.create(PublicService.class);
        Observable<Response<List<CheckInRes>>> observable = publicService.getCheckInList(queryBody);
        observable.compose(this.<Response<List<CheckInRes>>>bindToLifecycle())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Response<List<CheckInRes>>>() {
                    @Override
                    public void accept(Response<List<CheckInRes>> response) throws Exception {
                        if (response.isSuccess()) {
                            List<CheckInRes> data = response.getData();
                            if (data == null || data.isEmpty()) {
                                tvInfo.setText("?????????:??????" + workScheRes.getBeginTime() + "?????????");
                            } else {
                                tvInfo.setText("?????????:??????" + workScheRes.getEndTime() + "?????????");
                            }
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        throwable.printStackTrace();
                    }
                });
    }

    public void timeTick(String time) {
        if (btnCheckIn != null) {
            btnCheckIn.setText(time);
        }
    }

    @OnClick(R.id.btn_check_in_button)
    public void checkIn(View v) {

        AMapLocation location = MapLocationAPI.getLocation();
        if (location==null){
            ToastUtils.show(getContext(),"????????????????????????????????????gps");
            return;
        }
        if (TextUtils.isEmpty(location.getAddress())){
//            ToastUtils.show(getActivity(),"??????????????????????????????????????????");
            return;
        }
        ToastUtils.show(getActivity(),"????????????:"+location.getAddress());

        double[] latLng84  =  CoordinateUtils.gcj02ToWGS84(location.getLongitude(), location.getLatitude());
        if (latLng84 ==null){
            ToastUtils.show(getContext(),"??????????????????");
            return;
        }
        CheckInPara para = new CheckInPara();
        para.setAbsX(latLng84[0]);
        para.setAbsY(latLng84[1]);
        para.setAddress(location.getAddress());
        checkIn(para);

//        TXLocationApi.requestSingleLocation(getActivity(), TXLocationApi.COORDINATE_TYPE_GCJ02, new ResultCallback<TencentLocation>() {
//
//            @Override
//            public void onResult(TencentLocation result, int tag) {
//
//            }
//
//            @Override
//            public void onResult(TencentLocation location) {
//                CheckInPara para = new CheckInPara();
//                para.setAbsX(location.getLongitude());
//                para.setAbsY(location.getLatitude());
//                para.setAddress(location.getAddress());
//                checkIn(para);
//            }
//
//            @Override
//            public void onError(Throwable throwable) {
//                ToastUtils.show(getActivity(), "????????????????????????");
//            }
//        });
    }

    @SuppressWarnings("unchecked")
    private void checkIn(CheckInPara para) {
        para.setPlanId(workScheRes.getId());
        para.setScheduleId(workScheRes.getSchId());
        para.setAreaCode(workScheRes.getAreaCode());
        para.setUserId(UserSession.getUserId(getActivity()));
        para.setUserName(UserSession.getUserName(getActivity()));

        PublicService publicService = ServiceGenerator.create(PublicService.class);
        Observable<Response> observable = publicService.checkIn(new JsonBody(para));
        observable.compose(this.<Response>bindToLifecycle())
                .observeOn(AndroidSchedulers.mainThread())
                .compose(new ProgressTransformer<Response>(getContext(), ProgressText.submit))
                .subscribe(new ResponseObserver(getContext()) {
                    @Override
                    public void onSuccess(Object data) {
                        btnCheckIn.setVisibility(View.GONE);
                        ivTick.setVisibility(View.VISIBLE);
                        ivTick.setChecked(true);
                        tvInfo.setText("???????????????");
                    }
                });
    }
    //??????mlocationClient??????
    public AMapLocationClient mlocationClient;
    //??????mLocationOption??????
    public AMapLocationClientOption mLocationOption = null;
    private void setLocation(){

        mlocationClient = new AMapLocationClient(getActivity());
        //?????????????????????
        mLocationOption = new AMapLocationClientOption();
        //??????????????????
        mlocationClient.setLocationListener(this);
        //???????????????????????????????????????Battery_Saving?????????????????????Device_Sensors??????????????????
        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        //??????????????????,????????????,?????????2000ms
        mLocationOption.setInterval(2000);
        //??????????????????
        mlocationClient.setLocationOption(mLocationOption);
        // ????????????????????????????????????????????????????????????????????????????????????????????????????????????
        // ??????????????????????????????????????????????????????????????????1000ms?????????????????????????????????stopLocation()???????????????????????????
        // ???????????????????????????????????????????????????onDestroy()??????
        // ?????????????????????????????????????????????????????????????????????stopLocation()???????????????????????????sdk???????????????
        //????????????
        mlocationClient.startLocation();
    }

    @Override
    public void onLocationChanged(AMapLocation amapLocation) {
        if (amapLocation != null) {
            if (amapLocation.getErrorCode() == 0) {
                //?????????????????????????????????????????????
                amapLocation.getLocationType();//??????????????????????????????????????????????????????????????????????????????
                amapLocation.getLatitude();//????????????
                amapLocation.getLongitude();//????????????
                amapLocation.getAccuracy();//??????????????????
                String address = amapLocation.getAddress();
                tvAddress.setText(address);
                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Date date = new Date(amapLocation.getTime());
                df.format(date);//????????????
            } else {
                //??????????????????ErrCode???????????????errInfo???????????????????????????????????????
                LogUtils.e("AmapError","location Error, ErrCode:"
                        + amapLocation.getErrorCode() + ", errInfo:"
                        + amapLocation.getErrorInfo());
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mlocationClient != null){
            mlocationClient.stopLocation();
            mlocationClient = null;
            mLocationOption = null;
        }
    }
}
