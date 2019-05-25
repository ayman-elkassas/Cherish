package com.example.ayman.cherish.Model.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.ayman.cherish.Model.models.Likers;
import com.example.ayman.cherish.R;
import com.example.ayman.cherish.View.Profilefragments.SearchFollow;
import com.example.ayman.cherish.View.activities.Profile.MyPofile;
import com.example.ayman.cherish.View.customViews.CustomLikeCherishUsers;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class LikersAdapter extends RecyclerView.Adapter<LikersAdapter.ViewHolder> {
	
	static private ArrayList<Likers> likersArrayList;
	Context context;
	
	int pos=0;
	
	public LikersAdapter(ArrayList<Likers> likersArrayList, Context context) {
		this.likersArrayList = likersArrayList;
		this.context = context;
	}
	
	@NonNull
	@Override
	public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
		
		View itemLayout= LayoutInflater.from(parent.getContext())
				.inflate(R.layout.item,parent,false);

		LikersAdapter.ViewHolder viewHolder=new LikersAdapter.ViewHolder(itemLayout);

		viewHolder.setIsRecyclable(false);

		return viewHolder;
	}
	
	@SuppressLint("ResourceAsColor")
	@Override
	public void onBindViewHolder(@NonNull final LikersAdapter.ViewHolder holder, int position) {
		holder.setIsRecyclable(false);
		
		holder.likerName.setText(likersArrayList.get(position).getFname()+" "
				+likersArrayList.get(position).getLname());
//
		//to put into imageView until download image url
		RequestOptions placeholderOption=new RequestOptions();
		placeholderOption.placeholder(R.drawable.def_avatar);
		
		Glide.with(context)
				.applyDefaultRequestOptions(placeholderOption)
				.load(likersArrayList.get(position).getImage_url())
				.into(holder.likerAvatar);
		
		holder.likerAvatar.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				
				pos= CustomLikeCherishUsers.liker_recy.getChildAdapterPosition(holder.itemView);
				
				Intent intent=new Intent(context, MyPofile.class);
				intent.putExtra("user_id",likersArrayList.get(pos).getUser_id());
				context.startActivity(intent);
			}
		});
		
		if(likersArrayList.size()==position-1)
		{
			holder.line.setVisibility(View.GONE);
		}
		
		if(likersArrayList.get(pos).getFlag())
		{
			holder.bio.setText(likersArrayList.get(position).getBio().toString());
			holder.bio.setBackgroundResource(R.drawable.transparent);
			holder.bio.setPadding(0,0,0,0);
		}
		else
		{
			holder.bio.setText(likersArrayList.get(position).getBio().toString());
			holder.favonlike.setVisibility(View.GONE);
		}
	}
	
	@Override
	public int getItemCount() {
		return likersArrayList.size();
	}
	
	public static class ViewHolder extends RecyclerView.ViewHolder {
		
		CircleImageView likerAvatar;
		TextView likerName,bio;
		ImageView favonlike;
		View line;
		
		public ViewHolder(@NonNull View itemView) {
			super(itemView);
			
			likerAvatar=itemView.findViewById(R.id.likerAvatar);
			likerName=itemView.findViewById(R.id.likerName);
			bio=itemView.findViewById(R.id.bio);
			favonlike=itemView.findViewById(R.id.favonlike);
			line=itemView.findViewById(R.id.line);
		}
	}
}
