package com.example.ayman.cherish.View.customViews;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.button.MaterialButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.DialogFragment;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.ayman.cherish.Model.location.GetCityName;
import com.example.ayman.cherish.Model.sharedClasses.SharedObjects;
import com.example.ayman.cherish.R;
import com.example.ayman.cherish.View.activities.Profile.Profile;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import de.hdodenhof.circleimageview.CircleImageView;
import id.zelory.compressor.Compressor;
import studio.carbonylgroup.textfieldboxes.ExtendedEditText;

import static android.app.Activity.RESULT_OK;

public class CustomDialogueVideo extends DialogFragment {
	
	ExtendedEditText title_video;
	ImageButton add_video;
	ImageView video_post,add_location_photo;
	MaterialButton newVideo;
	
	CircleImageView avatar_videoCherish;
	TextView VideoUserName;
	
	//firebase objects
	FirebaseAuth mAuth;
	StorageReference storageReference;
	FirebaseFirestore firebaseFirestore;
	
	//other vars
	Uri postVideouri;
	String user_id;
	
	@NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        
        LayoutInflater inflater = getActivity().getLayoutInflater();
    
        View view=inflater.inflate(R.layout.new_video, null);
        
        builder.setView(view);
		
        //Vars
		title_video=view.findViewById(R.id.title_video);
		add_video=view.findViewById(R.id.add_video);
		newVideo=view.findViewById(R.id.newVideo);
		avatar_videoCherish=view.findViewById(R.id.avatar_videoCherish);
		VideoUserName=view.findViewById(R.id.VideoUserName);
		
		//get instance from firebase objects
		mAuth= FirebaseAuth.getInstance();
		storageReference= FirebaseStorage.getInstance().getReference();
		firebaseFirestore= FirebaseFirestore.getInstance();
		
		//get current user id
		user_id=mAuth.getCurrentUser().getUid();
		
		//Initialize dialogue
		intitializeInfo();
		
		add_video.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            	
	            Profile.code=3;

                //for get image from gallery
                if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.M)
                {
                    if(ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE)
                            != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(getActivity(),new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},1);
                    }
                    else
                    {
                        videoPicker();
                    }
                }
                else
                {
                    videoPicker();
                }
            }
        });

//
		newVideo.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				//get desc
				final String title=title_video.getText().toString();

				//should not be empty fields
				if(!TextUtils.isEmpty(title) && postVideouri !=null) {
					//get random value for image
					final String randomName= UUID.randomUUID().toString();

					//name of image depend on random value
					//because the same user can be have many of post images
					//In contrast of avatar is one image

					//prepare file path for image in firebase storage
					StorageReference filePath=storageReference.child("cherish_videos")
							.child(randomName+".mp4");

					//upload image without compressed
					filePath.putFile(postVideouri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
						@Override
						public void onComplete(@NonNull final Task<UploadTask.TaskSnapshot> task) {

							if(task.isSuccessful())
							{
								//get download url from uploaded post image
								final String download_url=task.getResult().getDownloadUrl().toString();
								
								//now create new collection posts (one-to-many) with users
								//but if embedded relation used document size with maximize
								//prepare new document to save inside post collection
								Map<String,Object> postMap=new HashMap<>();
								//save two image (original and compressed)
								postMap.put("video_url",download_url);
								postMap.put("title",title);
								postMap.put("user_id",user_id);
								postMap.put("type",2);
								postMap.put("location",LocationName());
								postMap.put("timestamp", FieldValue.serverTimestamp());
								
								//create new collection posts
								//to add document with random name use add() without document()
								firebaseFirestore.collection(SharedObjects.MainCollection).document(user_id)
										.collection("Timeline").add(postMap)
										.addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
											@Override
											public void onComplete(@NonNull Task<DocumentReference> task) {
												if(task.isSuccessful())
												{
													firebaseFirestore.collection("Counter").document(user_id)
															.collection(user_id).document("video")
															.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
														@Override
														public void onComplete(@NonNull Task<DocumentSnapshot> task) {
															if(task.isSuccessful())
															{
																int noVideo=0;
																if(task.getResult().exists())
																{
																	noVideo=Integer.parseInt(task.getResult().get("video").toString());
																}
																
																Map<String,Object> NoteCount=new HashMap<>();
																NoteCount.put("video",noVideo+1);
																firebaseFirestore.collection("Counter").document(user_id)
																		.collection(user_id).document("video")
																		.set(NoteCount).addOnCompleteListener(new OnCompleteListener<Void>() {
																	@Override
																	public void onComplete(@NonNull Task<Void> task) {
																		if(task.isSuccessful())
																		{
																			Toast.makeText(getActivity(),
																					"Cherish was added", Toast.LENGTH_SHORT).show();
																			Intent in=new Intent(getActivity(),Profile.class);
																			startActivity(in);
																			getActivity().finish();
																		}
																	}
																});
															}
														}
													});
												}
											}
										});
							}
							else
							{
								String error=task.getException().getMessage();
								Toast.makeText(getActivity(), ""+error,
										Toast.LENGTH_LONG).show();
							}
						}
					});

				}
			}
		});
//
        
        return builder.create();
    }
	
	private void videoPicker() {
		Intent in=new Intent(Intent.ACTION_PICK);
		in.setType("video/*");
		in.setAction(Intent.ACTION_GET_CONTENT);
		startActivityForResult(Intent.createChooser(in,"Select Video"),2);
		
		Profile.code=3;
	}
	
	private void intitializeInfo() {
		ArrayList<String> basicInfo = new ArrayList<String>();
		
		//TODO:PRIVATE MODE 0
		SharedPreferences pref = getActivity().getSharedPreferences("basicInfo", 0);
		basicInfo.add(pref.getString("fname", ""));
		basicInfo.add(pref.getString("lname", ""));
		basicInfo.add(pref.getString("image", ""));
		
		VideoUserName.setText(basicInfo.get(0) + " " + basicInfo.get(1));
		
		//to put into imageView until download image url
		RequestOptions placeholderOption = new RequestOptions();
		placeholderOption.placeholder(R.drawable.def_avatar);
		
		Glide.with(getActivity())
				.applyDefaultRequestOptions(placeholderOption)
				.load(basicInfo.get(2))
				.into(avatar_videoCherish);
	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		
		if (resultCode == RESULT_OK) {
			
			//get Uri path image
			postVideouri =data.getData();
		}
	}
	
	public String LocationName()
	{
		String city="";
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
				&& getActivity().checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION)
				!= PackageManager.PERMISSION_GRANTED) {
			requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, 100);
		} else {
			LocationManager locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
			Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
			
			try {
				city= GetCityName.hereLocation(location.getLatitude(), location.getLongitude(),getActivity());
				Toast.makeText(getActivity(), "" + city, Toast.LENGTH_SHORT).show();
			} catch (Exception e) {
				Toast.makeText(getActivity(), "Not found!", Toast.LENGTH_SHORT).show();
			}
		}
		
		return city;
	}
	
	//check if permission is granted
	@Override
	public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
		super.onRequestPermissionsResult(requestCode, permissions, grantResults);
		
		GetCityName.onRequestReturn(requestCode,grantResults,getActivity());
	}
}
