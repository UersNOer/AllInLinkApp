package com.example.android_supervisor.ui;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.amap.api.location.AMapLocation;
import com.example.android_supervisor.R;
import com.example.android_supervisor.common.UserSession;
import com.example.android_supervisor.entities.Attach;
import com.example.android_supervisor.entities.CheckUpPara;
import com.example.android_supervisor.entities.CheckUpRes;
import com.example.android_supervisor.http.ServiceGenerator;
import com.example.android_supervisor.http.common.ProgressTransformer;
import com.example.android_supervisor.http.request.JsonBody;
import com.example.android_supervisor.http.response.Response;
import com.example.android_supervisor.http.response.ResponseObserver;
import com.example.android_supervisor.http.service.PublicService;
import com.example.android_supervisor.map.MapLocationAPI;
import com.example.android_supervisor.ui.media.MediaUploadGridView;
import com.example.android_supervisor.ui.view.ProgressText;
import com.example.android_supervisor.utils.CoordinateUtils;
import com.example.android_supervisor.utils.SystemUtils;
import com.example.android_supervisor.utils.ToastUtils;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;

public class CheckUpActivity extends BaseActivity {
	@BindView(R.id.tv_check_up_content)
    TextView tvContent;

//	@BindView(R.id.tv_pos_character)
//	TextView tv_pos_character;


	@BindView(R.id.et_check_up_reply)
    EditText etReply;

	@BindView(R.id.gv_check_up_attaches)
    MediaUploadGridView gvAttaches;

	private String id;
	private CheckUpPara para;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_check_up);
		ButterKnife.bind(this);

		this.id = getIntent().getStringExtra("id");
		this.para = new CheckUpPara();

		fetchData();
		//TextChangeUtils textChangeUtils = new TextChangeUtils(etReply,tv_pos_character,this);
	}

	private void fetchData() {
		PublicService publicService = ServiceGenerator.create(PublicService.class);
		Observable<Response<CheckUpRes>> observable = publicService.getCheckUpById(id);
		observable.compose(this.<Response<CheckUpRes>>bindToLifecycle())
				.observeOn(AndroidSchedulers.mainThread())
				.compose(new ProgressTransformer<Response<CheckUpRes>>(this, ProgressText.load))
				.subscribe(new ResponseObserver<CheckUpRes>(this) {
					@Override
					public void onSuccess(CheckUpRes data) {
						tvContent.setText(data.getContent());
					}
				});
	}

	@OnClick(R.id.btn_check_up_submit)
	public void onSub(View v) {
		SystemUtils.hideSoftInput(this, v);
		if (validateParams()) {
			loadAttaches();
			submit();
		}
	}

	private boolean validateParams() {
		String content = etReply.getText().toString();
		if (TextUtils.isEmpty(content)) {
			ToastUtils.show(this, "【回复】不能为空");
			return false;
		}
		if (gvAttaches.isEmpty()) {
			ToastUtils.show(this, "请添加附件");
			return false;
		}
		return true;
	}

	private void loadAttaches() {

		ArrayList<Attach> attaches = new ArrayList<>();
		gvAttaches.getAttach(gvAttaches.getCanUploadAttach(),attaches,0);
		para.setAttaches(attaches);

	}

	@SuppressWarnings("unchecked")
	public void submit() {

		AMapLocation location = MapLocationAPI.getLocation();
		ToastUtils.show(this,"请开启gps");
		if (location == null){
			return;
		}
		para.setUserId(UserSession.getUserId(CheckUpActivity.this));
		para.setCheckId(id);
		para.setContent(etReply.getText().toString());

		if (location!=null){

			double[] latLng84  =  CoordinateUtils.gcj02ToWGS84(location.getLongitude(), location.getLatitude());
			if (latLng84 ==null){
				ToastUtils.show(this,"坐标转换失败");
				return;
			}
			para.setAbsX(latLng84[0]);
			para.setAbsY(latLng84[1]);
			para.setReplyLocation(location.getAddress());
		}


		PublicService publicService = ServiceGenerator.create(PublicService.class);
		Observable<Response> observable = publicService.replyCheckUp(new JsonBody(para));
		observable.compose(this.<Response>bindToLifecycle())
				.observeOn(AndroidSchedulers.mainThread())
				.compose(new ProgressTransformer<Response>(this, ProgressText.submit))
				.subscribe(new ResponseObserver(this) {
					@Override
					public void onSuccess(Object data) {
						setResult(RESULT_OK);
						finish();
						ToastUtils.show(CheckUpActivity.this, "提交成功");
					}

					@Override
					public void onFailure(int code, String errMsg) {
						super.onFailure(code, errMsg);
						ToastUtils.show(CheckUpActivity.this,errMsg);
					}

					@Override
					public void onError(Throwable e) {
						super.onError(e);
						ToastUtils.show(CheckUpActivity.this,e.getMessage());
					}
				});
	}

	@Override
	public void finish() {
		super.finish();
		overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
	}
}
