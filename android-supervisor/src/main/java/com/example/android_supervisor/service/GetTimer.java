package com.example.android_supervisor.service;

import android.content.Context;
import android.util.Log;

import com.example.android_supervisor.entities.MyTimeRes;
import com.example.android_supervisor.http.ServiceGenerator;
import com.example.android_supervisor.http.service.PublicService;
import com.example.android_supervisor.utils.SyncDateTime;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class GetTimer implements ScheduledRunnable
{
	private Context mContext;

	public GetTimer(Context context) {
		mContext = context;

	}
	@Override
	public void run() {
		try
		{

			PublicService publicService = ServiceGenerator.create(PublicService.class);
			Call<MyTimeRes> call = publicService.getSystemTime();

			call.enqueue(new Callback<MyTimeRes>() {
				@Override
				public void onResponse(Call<MyTimeRes> call, Response<MyTimeRes> response) {
					try {
						if (response.isSuccessful()){
							MyTimeRes res  = response.body();
							Log.d("getSystemTime", res.getData()+"");
							if(res != null)
							{
								SyncDateTime.getInstance().check(res.toData());
							}
							else
							{
//								SyncDateTime.getInstance().check(Environments.dateStar);
							}




						}

					} catch (Exception e) {
						e.printStackTrace();
					}

				}

				@Override
				public void onFailure(Call<MyTimeRes> call, Throwable t) {

				}


			});

		}

		catch (Exception e)
		{
			e.printStackTrace();
		}
		finally
		{

		}
	}

	@Override
	public void start(ScheduledExecutorService service) {

		service.scheduleWithFixedDelay(this, 5L, 60*60*3, TimeUnit.SECONDS);
	}

	@Override
	public void stop(ScheduledExecutorService service) {

	}
}
