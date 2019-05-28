package com.example.ayman.cherish.View.customViews;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.ayman.cherish.Model.adapters.NotifyAdapter;
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

import java.text.SimpleDateFormat;

import de.hdodenhof.circleimageview.CircleImageView;

public class CustomCommentPreviewWithCherish extends DialogFragment {
	
	CircleImageView iconPost;
	TextView titlePost, postType, locationPost, desc;
	ImageView postImage;
	FrameLayout postVideo;
	UniversalVideoView universalVideoView;
	UniversalMediaController universalMediaController;
	public LinearLayout voicePlayer;
	CircleImageView childAvatar,commenterAvatar;
	TextView postOwner, timestampCherish,commentOwner,timeCherishComment,commentBody;

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
	
	//formatter
	SimpleDateFormat formatter= new SimpleDateFormat("hh:mm aaa");
	
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
		timestampCherish = view.findViewById(R.id.timestampCherish);
		desc = view.findViewById(R.id.contentPost);
		postImage = view.findViewById(R.id.postImage);
		postVideo = view.findViewById(R.id.postVideo);
		universalVideoView = view.findViewById(R.id.videoView);
		universalMediaController = view.findViewById(R.id.media_controller);
		voicePlayer = view.findViewById(R.id.voicePlayer);
		childAvatar = view.findViewById(R.id.childavatar);
		commenterAvatar = view.findViewById(R.id.commenterAvatar);
		postOwner = view.findViewById(R.id.postOwner);
		commentOwner = view.findViewById(R.id.commentOwner);
		timeCherishComment = view.findViewById(R.id.timeCherishComment);
		commentBody = view.findViewById(R.id.commentBody);
		
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
		String image_url=bundle.getString("imageAvatar");
		String usernameCommenter=bundle.getString("usernameCommenter");
		String timestampeComment=bundle.getString("timestampeComment");
		String comment=bundle.getString("commentBody");
		
		
//		Toast.makeText(getActivity(), ""+cherishCommentId, Toast.LENGTH_SHORT).show();
		
		intializeView(cherishCommentId,image_url,usernameCommenter,timestampeComment,comment);
		
		builder.setView(view);
		
		return builder.create();
	}
	
	private void intializeView(String cherishCommentId, final String image_url, final String usernameCommenter, final String timestampeComment, final String comment) {
		
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
							
							initializeHeaderOwner(task,image_url,usernameCommenter,timestampeComment,comment);
							
							if(type.equals("0"))
							{
								desc.setVisibility(View.VISIBLE);
								
								postType.setText("Note");
								iconPost.setImageResource(R.drawable.timelinesmscard);
								iconPost.setCircleBackgroundColor(getActivity().getResources().getColor(R.color.sms_color));
								
								locationPost.setText(task.getResult().getString("location"));
								
								timestampCherish.setText(
										formatter.format(
												task.getResult().get("timestamp")
										)
								);
								
								titlePost.setText(task.getResult().getString("title"));
								desc.setText(task.getResult().getString("desc"));
								
							}
							else if(type.equals("1"))
							{
								postImage.setVisibility(View.VISIBLE);
								
								postType.setText("Photo");
								
								Glide.with(getActivity())
										.load(task.getResult().getString("image_url"))
										.thumbnail(Glide.with(getActivity()).load(task.getResult().getString("thumb")))
										.into(postImage);
								titlePost.setText(task.getResult().getString("title"));
								
								locationPost.setText(task.getResult().getString("location"));
								
								timestampCherish.setText(
										formatter.format(
												task.getResult().getString("timestamp")
										)
								);
								
								iconPost.setImageResource(R.drawable.phototimeline);
								iconPost.setCircleBackgroundColor(getActivity().getResources().getColor(R.color.photo_color));
								
							}
							else if(type.equals("2"))
							{
								postVideo.setVisibility(View.VISIBLE);
								postType.setText("Video");
								
								titlePost.setText(task.getResult().getString("title"));
								
								locationPost.setText(task.getResult().getString("location"));
								
								timestampCherish.setText(
										formatter.format(
												task.getResult().getString("timestamp")
										)
								);
								
								iconPost.setImageResource(R.drawable.videotimeline);
								iconPost.setCircleBackgroundColor(getActivity().getResources().getColor(R.color.video_color));
							}
							else if(type.equals("3"))
							{
								voicePlayer.setVisibility(View.VISIBLE);
								postType.setText("Voice");
								
								titlePost.setText(task.getResult().getString("title"));
								
								locationPost.setText(task.getResult().getString("location"));
								
								timestampCherish.setText(
										formatter.format(
												task.getResult().getString("timestamp")
										)
								);
								
								iconPost.setImageResource(R.drawable.voicetimeline);
								iconPost.setCircleBackgroundColor(getActivity().getResources().getColor(R.color.voice_color));
							}
							
						}
					}
				});
	
	}
	
	private void initializeHeaderOwner(Task<DocumentSnapshot> task, String image_url, String usernameCommenter, String timestampeComment, String comment) {
		
		//to put into imageView until download image url
		final RequestOptions plaception = new RequestOptions();
		
		plaception.placeholder(R.drawable.def_avatar);
		
		Glide.with(getActivity())
				.applyDefaultRequestOptions(plaception)
				.load(image_url)
				.into(commenterAvatar);
		
		commentOwner.setText(usernameCommenter);
		
		timeCherishComment.setText(timestampeComment);
		commentBody.setText(comment);
		
		firebaseFirestore.collection("Users")
				.document(task.getResult().getString("user_id"))
				.get()
				.addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
					@Override
					public void onComplete(@NonNull Task<DocumentSnapshot> task) {
						if(task.getResult().exists())
						{
							plaception.placeholder(R.drawable.def_avatar);
							
							Glide.with(getActivity())
									.applyDefaultRequestOptions(plaception)
									.load(task.getResult().getString("image"))
									.into(childAvatar);
							
							postOwner.setText(task.getResult().getString("fname")
									+" "+task.getResult().getString("lname"));
							
							NotifyAdapter.flagtimeClick=true;
							
						}
					}
				});
		
	}
	
}
