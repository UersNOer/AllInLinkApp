package com.example.android_supervisor.utils;

import android.annotation.SuppressLint;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@SuppressLint("SimpleDateFormat")
public class SyncDateTime implements Runnable
{
	private static final SimpleDateFormat sdfHour = new SimpleDateFormat("HH:mm:ss");

	private long milliseconds;
	private OnChanged onChanged;

	private ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);

	public void start(Date dateTime)
	{
		Date date = null;
		if(dateTime == null)
		{
			date = Calendar.getInstance().getTime();
		}
		else
		{
			date = dateTime;
		}

		milliseconds = date.getTime();
	}

	public Date toDate(Long dateTime)
	{
		return new Date(dateTime);
	}



	public void check(Date dateTime)
	{
		if(dateTime != null)
		{
			Date date = dateTime;
			long milliseconds2 = date.getTime();

			long reduceValue = Math.abs(milliseconds2 - milliseconds);
			int oneMinute = 60 * 1000;// 大于等于1分钟服务器时间，则同步时服务器时间
			if(reduceValue >= oneMinute)
			{
				milliseconds = milliseconds2;
			}
		}
	}

	public Date getDate()
	{
		return new Date(milliseconds);
	}

	public Date getDateTime()
	{
		return getDate();
	}

	public String getDateText()
	{
		Date date = getDate();
		String text = DateUtils.format(date, 0);
		return text;
	}

	public String getHourText()
	{
		Date date = getDate();
		String text = sdfHour.format(date);
		return text;
	}

	public void setOnChanged(OnChanged onChanged)
	{
		this.onChanged = onChanged;
	}

	public static interface OnChanged
	{
		void changed(Date date);
	}

	private static SyncDateTime instance = null;

	private SyncDateTime()
	{
		executor.scheduleAtFixedRate(this, 0, 1, TimeUnit.SECONDS);
	}

	public static SyncDateTime getInstance()
	{
		if(instance == null)
		{
			synchronized (SyncDateTime.class)
			{
				if(instance == null)
				{
					instance = new SyncDateTime();
				}
			}
		}
		return instance;
	}

	@Override
	public void run()
	{
		if(milliseconds > 0)
		{
			milliseconds += 1000;

			if(onChanged != null)
			{
				onChanged.changed(new Date(milliseconds));
			}
		}
	}
}