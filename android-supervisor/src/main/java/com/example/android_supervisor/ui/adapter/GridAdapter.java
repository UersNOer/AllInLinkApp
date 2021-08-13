package com.example.android_supervisor.ui.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.android_supervisor.ui.HomeItemView;

import java.util.List;

public class GridAdapter extends ArrayAdapter<HomeItemView>
{
	private static final int textViewResourceId = 0;

	List<HomeItemView> objects;
	Context context;


	public GridAdapter(Context context, List<HomeItemView> objects)
	{
		super(context, textViewResourceId, objects);
		this.objects = objects;
		this.context = context;
	}


	@NonNull
	@Override
	public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

		View view = getItem(position);
        view.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				if (mItemClickListener!=null){
					mItemClickListener.onItemClick(position, view);
				}
			}
		});

		return view;
	}

	protected OnItemClickListener mItemClickListener;

	public interface OnItemClickListener {
		void onItemClick(int position, View v);
	}

	public void setOnItemClickListener(OnItemClickListener listener) {
		this.mItemClickListener = listener;
	}
}
