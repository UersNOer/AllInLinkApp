package com.example.android_supervisor.entities;

import android.content.Context;
import android.widget.GridView;


import com.example.android_supervisor.ui.HomeItemView;
import com.example.android_supervisor.ui.adapter.GridAdapter;
import com.example.android_supervisor.utils.MainIcon;

import java.util.ArrayList;

@SuppressWarnings("serial")
public class MainListItems extends ArrayList<HomeItemView>
{

	public boolean hasValue()
	{
		return size() > 0;
	}

	public HomeItemView add(Context context, String name, MainIcon.GridTag tag, int resId, String url)
	{
		HomeItemView item = new HomeItemView(context);
		item.setGridTitle(name);
		item.setGridImg(resId);
		item.setTags(tag);
		item.setTag(item);
		this.add(item);
		return item;
	}

	public HomeItemView add(Context context, String name, MainIcon.GridTag tag, int resId)
	{
		HomeItemView item = new HomeItemView(context);
		item.setGridTitle(name);
		item.setGridImg(resId);
		item.setTags(tag);
		item.setTag(item);
		this.add(item);
		return item;
	}

	public HomeItemView add(Context context, String name, MainIcon.GridTag tag, int resId,int parseColor,Class activityLauncher)
	{
		HomeItemView item = new HomeItemView(context);
		item.setGridTitle(name);
		item.setGridImg(resId);
		item.setTags(tag);
		item.setTag(item);
		item.setLauncher(activityLauncher);
        item.setBgTint(parseColor);
		this.add(item);
		return item;
	}


	public void setAdapter(GridView listView, GridAdapter.OnItemClickListener listener)
	{
		if(listView != null && hasValue())
		{

			GridAdapter adapter = new GridAdapter(listView.getContext(), this);
			if(listener != null)
			{
				adapter.setOnItemClickListener(listener);
			}
			listView.setAdapter(adapter);

		}
	}

//	public void setCount(Object tag, int count)
//	{
//		for (HomeItemView item : this)
//		{
//			if(item.getTags().equals(tag))
//			{
//				item.setGridCount(count);
//				break;
//			}
//		}
//	}
}
