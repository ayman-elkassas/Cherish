package com.example.ayman.cherish.Model.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.design.button.MaterialButton;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.ayman.cherish.Model.models.SetupDataAccount;
import com.example.ayman.cherish.R;
import com.example.ayman.cherish.View.Profilefragments.SearchFollow;
import com.example.ayman.cherish.View.Setupfragments.AddFriends;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.nex3z.togglebuttongroup.SingleSelectToggleGroup;
import com.nex3z.togglebuttongroup.button.LabelToggle;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class CommunityAdapter extends RecyclerView.Adapter<CommunityAdapter.ViewHolder>  {
	
	private ArrayList<SetupDataAccount> friendAccounts;
	Context context;
	
	//Firebase add
	//Firebase
	FirebaseAuth mAuth;
	FirebaseFirestore firebaseFirestore;
	String currentUser;
	
	int pos=0;
	
	public CommunityAdapter(ArrayList<SetupDataAccount> friendAccounts, Context context) {
		this.friendAccounts = friendAccounts;
		this.context = context;
		
		notifyDataSetChanged();
		
	}
	
	@NonNull
	@Override
	public CommunityAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
		
		View itemLayout= LayoutInflater.from(parent.getContext())
				.inflate(R.layout.community_item,parent,false);
		
		CommunityAdapter.ViewHolder viewHolder=new CommunityAdapter.ViewHolder(itemLayout);
		
		viewHolder.setIsRecyclable(false);
		
		//firebase
		mAuth=FirebaseAuth.getInstance();
		firebaseFirestore=FirebaseFirestore.getInstance();
		
		//get CurrentUser
		currentUser=mAuth.getCurrentUser().getUid();
		
		return viewHolder;
	}
	
	@Override
	public void onBindViewHolder(@NonNull final CommunityAdapter.ViewHolder holder, final int position) {
		holder.setIsRecyclable(false);
		
		holder.friendName.setText(friendAccounts.get(position).getFname()+" "
				+friendAccounts.get(position).getLname());
//
		//to put into imageView until download image url
		RequestOptions placeholderOption=new RequestOptions();
		placeholderOption.placeholder(R.drawable.def_avatar);
		
		Glide.with(context)
				.applyDefaultRequestOptions(placeholderOption)
				.load(friendAccounts.get(position).getImageFriend())
				.into(holder.itemFriendAvatar);
		
		holder.relationGroup.setOnCheckedChangeListener(new SingleSelectToggleGroup.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(final SingleSelectToggleGroup group, final int checkedId) {
						
						pos= SearchFollow.listOfCommunity.getChildAdapterPosition(holder.itemView);
						
						if(holder.child.getId()==checkedId)
						{
							Map<String,Object> child=new HashMap<>();
							child.put(friendAccounts.get(pos).getUser_id(),friendAccounts.get(pos).getUser_id());
							
							firebaseFirestore.collection("Users").document(currentUser)
									.collection("child")
									.document(friendAccounts.get(pos).getUser_id())
									.set(child);
							
							Toast.makeText(context, "Add success", Toast.LENGTH_LONG).show();
						}
						else
						{
							Map<String,Object> family=new HashMap<>();
							family.put(friendAccounts.get(pos).getUser_id(),friendAccounts.get(pos).getUser_id());
							
							firebaseFirestore.collection("Users").document(currentUser)
									.collection("family")
									.document(friendAccounts.get(pos).getUser_id())
									.set(family);
							
							Toast.makeText(context, "Add success", Toast.LENGTH_LONG).show();
						}
			}
		});
	}
	
	@Override
	public int getItemCount() {
		return friendAccounts.size();
	}
	
	public  class ViewHolder extends RecyclerView.ViewHolder {
		
		CircleImageView itemFriendAvatar;
		TextView friendName,mentionKey,friendBio;
		SingleSelectToggleGroup relationGroup;
		LabelToggle child,family;
		ConstraintLayout itemFriend;
		
		
		public ViewHolder(@NonNull View itemView) {
			super(itemView);
			
			itemFriendAvatar=itemView.findViewById(R.id.itemFriendAvatar);
			friendName=itemView.findViewById(R.id.friendName);
			mentionKey=itemView.findViewById(R.id.mentionKey);
			friendBio=itemView.findViewById(R.id.friendBio);
			relationGroup=itemView.findViewById(R.id.relationGroup);
			child=itemView.findViewById(R.id.child);
			family=itemView.findViewById(R.id.family);
			itemFriend=itemView.findViewById(R.id.itemFriend);
		}
	}
}
