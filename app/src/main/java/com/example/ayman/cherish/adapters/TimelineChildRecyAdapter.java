package com.example.ayman.cherish.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ayman.cherish.R;
import com.example.ayman.cherish.models.TimelineChildCardData;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class TimelineChildRecyAdapter extends RecyclerView.Adapter<TimelineChildRecyAdapter.ViewHolder> {
	
	private ArrayList<TimelineChildCardData> timelineChildCardData;
	Context context;
	
	public TimelineChildRecyAdapter(ArrayList<TimelineChildCardData> timelineChildCardData, Context context) {
		this.timelineChildCardData = timelineChildCardData;
		this.context = context;
	}
	
	@NonNull
	@Override
	public TimelineChildRecyAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
		View itemLayout= LayoutInflater.from(parent.getContext())
				.inflate(R.layout.child_recy_timelinefragment,parent,false);
		
		TimelineChildRecyAdapter.ViewHolder viewHolder=new TimelineChildRecyAdapter.ViewHolder(itemLayout);
		
		viewHolder.setIsRecyclable(false);
		
		return viewHolder;
	}
	
	@Override
	public void onBindViewHolder(@NonNull TimelineChildRecyAdapter.ViewHolder holder, int position) {
		holder.iconPost.setImageResource(timelineChildCardData.get(position).getIconTypePost());
		holder.iconPost.setCircleBackgroundColor(timelineChildCardData.get(position).getColorIconTypePost());
		holder.titlePost.setText(timelineChildCardData.get(position).getTitle());
		holder.postType.setText(timelineChildCardData.get(position).getTypePost());
		holder.locationPost.setText(timelineChildCardData.get(position).getLocation());
		holder.contentPost.setText(timelineChildCardData.get(position).getContent());
		
		holder.setIsRecyclable(false);
	}
	
	@Override
	public int getItemCount() {
		return timelineChildCardData.size();
	}
	
	public  class ViewHolder extends RecyclerView.ViewHolder
	{
		CircleImageView iconPost;
		TextView titlePost,postType,locationPost,contentPost;
		
		public ViewHolder(@NonNull View itemView) {
			super(itemView);
			
			iconPost=itemView.findViewById(R.id.iconPost);
			titlePost=itemView.findViewById(R.id.titlePost);
			postType=itemView.findViewById(R.id.postType);
			locationPost=itemView.findViewById(R.id.locationPost);
			contentPost=itemView.findViewById(R.id.contentPost);
		}
	}
}
