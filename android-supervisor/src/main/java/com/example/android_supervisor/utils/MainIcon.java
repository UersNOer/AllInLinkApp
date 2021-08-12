package com.example.android_supervisor.utils;

import android.app.Activity;
import android.graphics.Color;

import com.example.android_supervisor.R;
import com.example.android_supervisor.entities.MainListItems;
import com.example.android_supervisor.ui.CensusTaskListActivity;
import com.example.android_supervisor.ui.CheckInActivity;
import com.example.android_supervisor.ui.CommutePlanActivity;
import com.example.android_supervisor.ui.ContactsActivity;
import com.example.android_supervisor.ui.DayNoticeActivity;
import com.example.android_supervisor.ui.EventNewActivity;
import com.example.android_supervisor.ui.HistoryActivity;
import com.example.android_supervisor.ui.LeaveRecordActivity;
import com.example.android_supervisor.ui.MyTaskListActivity;


public class MainIcon
{

	public enum GridTag
	{
		tagCR, tagCRKS, tagTask, tagHS, tagZX,tagCON,tagDK,tagkQ,tagSP, tagYqc, tagYqs, tagYqZHQK, tagQJ, tagYqsJl, tagPK,tagNotice

	}


	public MainListItems getMeun(Activity mActivity)
	{
		MainListItems result = null;
		result = check_ob(mActivity);

//		switch (Environments.CurUser.type)
//		{
//			case 0:
//				result = check_ob(mActivity);
//				break;
//			case 1:
//				result = check_gz(mActivity);
//				break;
//			// case 2:
//			// result = check_gd(menuTag);
//			// break;
//			// case 3:
//			// result = check_ld(mActivity);
//			// break;
//			case 100:
//				result = check_all(mActivity);
//
//				break;
//			default:
//				result = check_all(mActivity);
//				break;
//		}
		return result;
	}


	private MainListItems check_ob(Activity mActivity)
	{
		MainListItems listItems = new MainListItems();

		listItems.add(mActivity, "问题上报", GridTag.tagCR, R.drawable.ic_home_ajsb, Color.parseColor("#3ec6ff"), EventNewActivity.class);
		listItems.add(mActivity, "快速上报", GridTag.tagCRKS, R.drawable.icon_fast_report,Color.parseColor("#e3424e"),EventNewActivity.class);
		listItems.add(mActivity, "个人任务", GridTag.tagTask, R.drawable.ic_home_grrw,Color.parseColor("#e87e8d"), MyTaskListActivity.class);
		listItems.add(mActivity, "历史记录", GridTag.tagHS, R.drawable.ic_home_lsjl, Color.parseColor("#84a7ff"),HistoryActivity.class);
		listItems.add(mActivity, "专项任务", GridTag.tagZX, R.drawable.ic_home_zxpc, Color.parseColor("#46a2de"),CensusTaskListActivity.class);
		listItems.add(mActivity, "通讯录", GridTag.tagCON, R.drawable.ic_home_txl, Color.parseColor("#fc7a5a"),ContactsActivity.class);
		listItems.add(mActivity, "打卡", GridTag.tagDK, R.drawable.ic_home_dk,Color.parseColor("#9770cf"), CheckInActivity.class);
		listItems.add(mActivity, "日常考勤", GridTag.tagkQ, R.drawable.ic_home_rckq, Color.parseColor("#53b77b"),CommutePlanActivity.class);
//		listItems.add(mActivity, "请假申请", GridTag.tagYqs, R.drawable.ic_home_rckq, Color.parseColor("#53b77b"), CTZActivity.class);
//		listItems.add(mActivity, "请假记录", GridTag.tagYqsJl, R.drawable.ic_home_rckq, Color.parseColor("#53b77b"), LeaveNoteActivity.class);
		listItems.add(mActivity, "请假管理", GridTag.tagYqsJl, R.drawable.ic_home_rckq, Color.parseColor("#53b77b"), LeaveRecordActivity.class);


//		listItems.add(mActivity, "视频会议", GridTag.tagSP, R.drawable.sphy,Color.parseColor("#46a2de"), MainActivity.class);
//		listItems.add(mActivity, "卡点考勤", GridTag.tagPK, R.drawable.home_kaoqing, Color.parseColor("#3ec6ff"),AMapActivity.class);

//		listItems.add(mActivity, "疫情场所上报", GridTag.tagYqc, R.drawable.yqcs, Color.parseColor("#e87e8d"), YqEventReportActivity.class);
//		listItems.add(mActivity, "疫情事件上报", GridTag.tagYqs, R.drawable.yqsj, Color.parseColor("#46a2de"),YqEventReportActivity.class);
//		listItems.add(mActivity, "综合情况上报", GridTag.tagYqZHQK, R.drawable.zhqk, Color.parseColor("#3ec6ff"), YqZHQKActivity.class);
//		listItems.add(mActivity, "疫情上报记录", GridTag.tagYqs, R.drawable.jl, Color.parseColor("#84a7ff"), YqReportNoteActivity.class);
		listItems.add(mActivity, "日志", GridTag.tagNotice, R.drawable.jl, Color.parseColor("#84a7ff"), DayNoticeActivity.class);

		return listItems;
	}



}
