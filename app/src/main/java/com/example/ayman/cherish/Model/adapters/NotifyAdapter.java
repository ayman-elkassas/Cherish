package com.example.ayman.cherish.Model.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.andexert.library.RippleView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.ayman.cherish.Model.models.Likers;
import com.example.ayman.cherish.Model.models.NotificationModel;
import com.example.ayman.cherish.R;
import com.example.ayman.cherish.View.Profilefragments.Notifications;
import com.example.ayman.cherish.View.activities.Profile.MyPofile;
import com.example.ayman.cherish.View.customViews.CustomCommentPreviewWithCherish;
import com.example.ayman.cherish.View.customViews.CustomLikeCherishUsers;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class NotifyAdapter extends RecyclerView.Adapter<NotifyAdapter.ViewHolder> {
	
	static private ArrayList<NotificationModel> NotifysArrayList;
	Context context;
	
	int pos=0;
	
	CustomCommentPreviewWithCherish customCommentPreviewWithCherish;
	FragmentManager fragmentManager;
	
	static Boolean flagtimeClick=true;
	
	public FragmentManager getFragmentManager() {
		return fragmentManager;
	}
	
	public void setFragmentManager(FragmentManager fragmentManager) {
		this.fragmentManager = fragmentManager;
	}
	
	public NotifyAdapter() {
	}
	
	public static ArrayList<NotificationModel> getNotifysArrayList() {
		return NotifysArrayList;
	}
	
	public static void setNotifysArrayList(ArrayList<NotificationModel> notifysArrayList) {
		NotifysArrayList = notifysArrayList;
	}
	
	public Context getContext() {
		return context;
	}
	
	public void setContext(Context context) {
		this.context = context;
	}
	
	public NotifyAdapter(ArrayList<NotificationModel> NotifysArrayList, Context context) {
		this.NotifysArrayList = NotifysArrayList;
		this.context = context;
	}
	
	@NonNull
	@Override
	public NotifyAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
		View itemLayout= LayoutInflater.from(parent.getContext())
				.inflate(R.layout.item_notify,parent,false);
		
		NotifyAdapter.ViewHolder viewHolder=new NotifyAdapter.ViewHolder(itemLayout);
		
		viewHolder.setIsRecyclable(false);
		
		return viewHolder;
	}
	
	@Override
	public void onBindViewHolder(@NonNull final NotifyAdapter.ViewHolder holder, int position) {
		holder.setIsRecyclable(false);
		
		holder.senderNotfy.setText(NotifysArrayList.get(position).getFname()+" "
				+NotifysArrayList.get(position).getLname());
//
		//to put into imageView until download image url
		RequestOptions placeholderOption=new RequestOptions();
		placeholderOption.placeholder(R.drawable.def_avatar);
		
		Glide.with(context)
				.applyDefaultRequestOptions(placeholderOption)
				.load(NotifysArrayList.get(position).getImage_url())
				.into(holder.NotifyAvatar);
		
		holder.NotifyAvatar.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				
				pos= Notifications.notification_recy.getChildAdapterPosition(holder.itemView);
				
				Intent intent=new Intent(context, MyPofile.class);
				intent.putExtra("user_id",NotifysArrayList.get(pos).getUser_id());
				context.startActivity(intent);
			}
		});
		
		holder.typeNotify.setText("Recent comment from");
		
		holder.comment.setText(NotifysArrayList.get(position).getBodyNotify());
		holder.timestampComment.setText(NotifysArrayList.get(position).getTimestampComment());
		
		if(NotifysArrayList.size()==0)
		{
			Notifications.noalertimage.setVisibility(View.VISIBLE);
			Notifications.alertext.setVisibility(View.VISIBLE);
		}
		else {
			Notifications.noalertimage.setVisibility(View.GONE);
			Notifications.alertext.setVisibility(View.GONE);
		}
		
		try
		{
			
			if(flagtimeClick)
			{
				holder.notifyLayout.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						
						flagtimeClick=false;
						
						customCommentPreviewWithCherish=new CustomCommentPreviewWithCherish();
						Bundle bundle = new Bundle();
						bundle.putString("cherishCommentId",NotifysArrayList.get(pos)
								.getCherishCommentId());
						customCommentPreviewWithCherish.setArguments(bundle);
						customCommentPreviewWithCherish.show(fragmentManager,null);
						flagtimeClick=true;
					}
				});
			}
			
		}catch (Exception e)
		{
		
		}
	}
	
	@Override
	public int getItemCount() {
		return NotifysArrayList.size();
	}
	
	public static class ViewHolder extends RecyclerView.ViewHolder {
		
		ConstraintLayout notifyLayout;
		
		CircleImageView NotifyAvatar;
		TextView typeNotify,senderNotfy,comment,timestampComment;
		
		public ViewHolder(@NonNull View itemView) {
			super(itemView);
			
			NotifyAvatar=itemView.findViewById(R.id.NotifyAvatar);
			typeNotify=itemView.findViewById(R.id.typeNotify);
			senderNotfy=itemView.findViewById(R.id.senderNotfy);
			comment=itemView.findViewById(R.id.comment);
			timestampComment=itemView.findViewById(R.id.timestampComment);
			notifyLayout=itemView.findViewById(R.id.notifyLayout);
		}
	}
}
