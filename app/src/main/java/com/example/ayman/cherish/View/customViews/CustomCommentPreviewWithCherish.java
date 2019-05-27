package com.example.ayman.cherish.View.customViews;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.ayman.cherish.Model.adapters.LikersAdapter;
import com.example.ayman.cherish.Model.models.Likers;
import com.example.ayman.cherish.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.like.LikeButton;
import com.stfalcon.chatkit.messages.MessageInput;
import com.universalvideoview.UniversalMediaController;
import com.universalvideoview.UniversalVideoView;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class CustomCommentPreviewWithCherish extends DialogFragment {
	
	CircleImageView iconPost;
	TextView titlePost, postType, locationPost, desc;
	ImageView postImage;
	FrameLayout postVideo;
	UniversalVideoView universalVideoView;
	UniversalMediaController universalMediaController;
	public LinearLayout voicePlayer;
	CircleImageView childAvatar;
	TextView postOwner;
	TextView contentPost;
	//Voice
	public ImageButton playVoice;
	public SeekBar seekbarVoice;
	public TextView duration;
	
	//like
	LikeButton cherishLike;
	TextView cherishLikeCount, cherishCommentCount;
	
	//message cherish
	MessageInput messageComment;
	
	//firebase
	//firebase objects
	FirebaseAuth mAuth;
	StorageReference storageReference;
	FirebaseFirestore firebaseFirestore;
	String user_id;
	
	public CustomCommentPreviewWithCherish() {
		// Required empty public constructor
	}
	
	@NonNull
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		
		LayoutInflater inflater = getActivity().getLayoutInflater();
		
		View view=inflater.inflate(R.layout.comment_show, null);
		
		//get instance from firebase objects
		mAuth= FirebaseAuth.getInstance();
		storageReference= FirebaseStorage.getInstance().getReference();
		firebaseFirestore= FirebaseFirestore.getInstance();
		
		//get current user id
		user_id=mAuth.getCurrentUser().getUid();
		
		iconPost = view.findViewById(R.id.iconPost);
		titlePost = view.findViewById(R.id.titlePost);
		postType = view.findViewById(R.id.postType);
		locationPost = view.findViewById(R.id.locationPost);
		desc = view.findViewById(R.id.contentPost);
		postImage = view.findViewById(R.id.postImage);
		postVideo = view.findViewById(R.id.postVideo);
		universalVideoView = view.findViewById(R.id.videoView);
		universalMediaController = view.findViewById(R.id.media_controller);
		voicePlayer = view.findViewById(R.id.voicePlayer);
		childAvatar = view.findViewById(R.id.childavatar);
		postOwner = view.findViewById(R.id.postOwner);
		
		playVoice = view.findViewById(R.id.playVoice);
		seekbarVoice = view.findViewById(R.id.seekbarVoice);
		duration = view.findViewById(R.id.timeDuration);
		
		//like
		cherishLike = view.findViewById(R.id.star_button);
		cherishLikeCount = view.findViewById(R.id.cherishLikeCount);
		
		//message cherish
		messageComment = view.findViewById(R.id.messageComment);
		cherishCommentCount = view.findViewById(R.id.cherishCommentCount);
		
		Bundle bundle = getArguments();
		String cherishCommentId = bundle.getString("cherishCommentId","");
		
		Toast.makeText(getActivity(), ""+cherishCommentId, Toast.LENGTH_SHORT).show();
		
		intializeView(cherishCommentId);
		
		builder.setView(view);
		
		return builder.create();
	}
	
	private void intializeView(String cherishCommentId) {
		
		//to put into imageView until download image url
		final RequestOptions placeholderOption = new RequestOptions();
		
		firebaseFirestore.collection("Cherish")
				.document(user_id)
				.collection("Timeline")
				.document(cherishCommentId)
				.get()
				.addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
					@Override
					public void onComplete(@NonNull Task<DocumentSnapshot> task) {
						if(task.getResult().exists())
						{
							String type=String.valueOf(task.getResult().get("type"));
							
							if(type.equals("0"))
							{
								
								desc.setVisibility(View.VISIBLE);
								
								postType.setText("Note");
								iconPost.setImageResource(R.drawable.timelinesmscard);
								iconPost.setCircleBackgroundColor(getActivity().getResources().getColor(R.color.sms_color));
								iconPost.setImageResource(R.drawable.timelinesmscard);
								iconPost.setCircleBackgroundColor(getActivity().getResources().getColor(R.color.sms_color));
								
								titlePost.setText(task.getResult().getString("title"));
								desc.setText(task.getResult().getString("desc"));
								locationPost.setText(task.getResult().getString("location"));
								
								firebaseFirestore.collection("Users")
										.document(task.getResult().getString("user_id"))
										.get()
										.addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
											@Override
											public void onComplete(@NonNull Task<DocumentSnapshot> task) {
												if(task.getResult().exists())
												{
													placeholderOption.placeholder(R.drawable.def_avatar);
													
													Glide.with(getActivity())
															.applyDefaultRequestOptions(placeholderOption)
															.load(task.getResult().getString("image"))
															.into(childAvatar);
													
													postOwner.setText(task.getResult().getString("fname")
													+" "+task.getResult().getString("lname"));
												}
											}
										});
							}
							else if(type.equals("1"))
							{
							
							}
							
						}
					}
				});
	
	}
}
