package com.example.ayman.cherish.View.customViews;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.design.button.MaterialButton;
import android.support.v4.app.DialogFragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.ayman.cherish.Model.location.GetCityName;
import com.example.ayman.cherish.Model.sharedClasses.SharedObjects;
import com.example.ayman.cherish.R;
import com.example.ayman.cherish.View.activities.Profile.MainLancher;
import com.example.ayman.cherish.permissions.Util;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import cafe.adriel.androidaudiorecorder.AndroidAudioRecorder;
import cafe.adriel.androidaudiorecorder.model.AudioChannel;
import cafe.adriel.androidaudiorecorder.model.AudioSampleRate;
import cafe.adriel.androidaudiorecorder.model.AudioSource;
import de.hdodenhof.circleimageview.CircleImageView;
import studio.carbonylgroup.textfieldboxes.ExtendedEditText;

public class CustomDialogueVoice extends DialogFragment {
	
	ExtendedEditText title_voice;
	ImageButton add_voice;
	MaterialButton newVoice;
	
	CircleImageView avatar_voiceCherish;
	TextView VoiceUserName;
	
	//firebase objects
	FirebaseAuth mAuth;
	StorageReference storageReference;
	FirebaseFirestore firebaseFirestore;
	
	//other vars
	Uri postVoiceuri;
	String user_id;
	
	//Voice note cliper
	private static final int REQUEST_RECORD_AUDIO = 3;
	public static final String AUDIO_FILE_PATH =
			Environment.getExternalStorageDirectory().getPath() + "/recorded_audio.wav";
	
	
	@NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        
        LayoutInflater inflater = getActivity().getLayoutInflater();
    
        View view=inflater.inflate(R.layout.new_voice, null);
        
        builder.setView(view);
		
        //Vars
		title_voice=view.findViewById(R.id.title_voice);
		add_voice=view.findViewById(R.id.add_voice);
		newVoice=view.findViewById(R.id.newVoice);
		avatar_voiceCherish=view.findViewById(R.id.avatar_voiceCherish);
		VoiceUserName=view.findViewById(R.id.VoiceUserName);
		
		//get instance from firebase objects
		mAuth= FirebaseAuth.getInstance();
		storageReference= FirebaseStorage.getInstance().getReference();
		firebaseFirestore= FirebaseFirestore.getInstance();
		
		//get current user id
		user_id=mAuth.getCurrentUser().getUid();
		
		//Voice note permissions
		if (((AppCompatActivity)getActivity()).getSupportActionBar() != null) {
			((AppCompatActivity)getActivity()).getSupportActionBar().setBackgroundDrawable(
					new ColorDrawable(ContextCompat.getColor(getActivity(), R.color.colorPrimaryDark)));
		}
		
		Util.requestPermission(getActivity(), Manifest.permission.RECORD_AUDIO);
		Util.requestPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE);
		
		//Initialize dialogue
		intitializeInfo();
		
		add_voice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
	
	            recordAudio();
            }
        });

//
		newVoice.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				//get desc
				final String title=title_voice.getText().toString();

				//should not be empty fields
				if(!TextUtils.isEmpty(title)) {
					//get random value for image
					final String randomName= UUID.randomUUID().toString();

					//name of image depend on random value
					//because the same user can be have many of post images
					//In contrast of avatar is one image

					//prepare file path for image in firebase storage
					StorageReference filePath=storageReference.child("cherish_voice")
							.child(randomName+".wav");

					//upload image without compressed
					filePath.putFile(Uri.fromFile(new File(AUDIO_FILE_PATH))).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
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
								postMap.put("type",3);
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
															.collection(user_id).document("voice")
															.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
														@Override
														public void onComplete(@NonNull Task<DocumentSnapshot> task) {
															if(task.isSuccessful())
															{
																int noVoice=0;
																if(task.getResult().exists())
																{
																	noVoice=Integer.parseInt(task.getResult().get("voice").toString());
																}
																
																Map<String,Object> NoteCount=new HashMap<>();
																NoteCount.put("voice",noVoice+1);
																firebaseFirestore.collection("Counter").document(user_id)
																		.collection(user_id).document("voice")
																		.set(NoteCount).addOnCompleteListener(new OnCompleteListener<Void>() {
																	@Override
																	public void onComplete(@NonNull Task<Void> task) {
																		if(task.isSuccessful())
																		{
																			Toast.makeText(getActivity(),
																					"Cherish was added", Toast.LENGTH_SHORT).show();
																			Intent in=new Intent(getActivity(),MainLancher.class);
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
	
	public void recordAudio() {
		AndroidAudioRecorder.with(getActivity())
				// Required
				.setFilePath(AUDIO_FILE_PATH)
				.setColor(ContextCompat.getColor(getActivity(), R.color.recorder_bg))
				.setRequestCode(REQUEST_RECORD_AUDIO)
				
				// Optional
				.setSource(AudioSource.MIC)
				.setChannel(AudioChannel.STEREO)
				.setSampleRate(AudioSampleRate.HZ_48000)
				.setAutoStart(false)
				.setKeepDisplayOn(true)
				
				// Start recording
				.record();
	}
	
	private void intitializeInfo() {
		ArrayList<String> basicInfo = new ArrayList<String>();
		
		//TODO:PRIVATE MODE 0
		SharedPreferences pref = getActivity().getSharedPreferences("basicInfo", 0);
		basicInfo.add(pref.getString("fname", ""));
		basicInfo.add(pref.getString("lname", ""));
		basicInfo.add(pref.getString("image", ""));
		
		VoiceUserName.setText(basicInfo.get(0) + " " + basicInfo.get(1));
		
		//to put into imageView until download image url
		RequestOptions placeholderOption = new RequestOptions();
		placeholderOption.placeholder(R.drawable.def_avatar);
		
		Glide.with(getActivity())
				.applyDefaultRequestOptions(placeholderOption)
				.load(basicInfo.get(2))
				.into(avatar_voiceCherish);
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
