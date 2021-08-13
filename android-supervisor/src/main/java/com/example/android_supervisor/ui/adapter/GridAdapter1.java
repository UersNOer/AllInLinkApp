package com.example.android_supervisor.ui.adapter;

import android.content.Context;
//import androidx.annotation.NonNull;
//import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.android_supervisor.ui.HomeItemView;

import java.util.List;

public class GridAdapter1 extends RecyclerView.Adapter<GridAdapter1.ViewHolder>
{
	private static final int textViewResourceId = 0;

	List<HomeItemView> objects;
	Context context;


	public GridAdapter1(Context context, List<HomeItemView> objects)
	{
		this.objects = objects;
		this.context = context;
//		super(context, textViewResourceId, objects);
	}


	class ViewHolder extends RecyclerView.ViewHolder{


		public ViewHolder(@NonNull View itemView) {
			super(itemView);
		}
	}

	@NonNull
	@Override
	public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
		View view = objects.get(i);

		ViewHolder viewHolder = new ViewHolder(view);
		return viewHolder;
	}

	@Override
	public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
		if (mItemClickListener != null) {
			viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					int adapterPosition = viewHolder.getAdapterPosition();
					mItemClickListener.onItemClick(adapterPosition, v);
				}
			});
		}
	}

	@Override
	public int getItemCount() {
		return objects.size();
	}

	protected OnItemClickListener mItemClickListener;

	public interface OnItemClickListener {
		void onItemClick(int position, View v);
	}

	public void setOnItemClickListener(OnItemClickListener listener) {
		this.mItemClickListener = listener;
	}
}
