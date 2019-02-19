package com.example.ayman.cherish.View.customViews;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
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
import com.example.ayman.cherish.Model.sharedClasses.SharedObjects;
import com.example.ayman.cherish.R;
import com.example.ayman.cherish.View.activities.Profile.Profile;
import com.example.ayman.cherish.Model.location.GetCityName;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;
import studio.carbonylgroup.textfieldboxes.ExtendedEditText;

import static android.app.Activity.RESULT_OK;

public class CustomDialogueNote extends DialogFragment {
	
	private static final int GALARY_INTENT = 2;
	
	//Vars
	
	ImageView add_location_note;
	CircleImageView NoteAvatar;
	TextView NoteUserName;
	ExtendedEditText NoteTitle,NoteDesc;
	MaterialButton NotePost;
	
	//firebase objects
	private FirebaseAuth mAuth;
	private FirebaseFirestore firebaseFirestore;
	
	//get current user
	private String currentUserId;
	
	@NonNull
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		LayoutInflater inflater = getActivity().getLayoutInflater();
		
		View view = inflater.inflate(R.layout.new_note, null);
		builder.setView(view);
		
		//firebase snippet
		mAuth = FirebaseAuth.getInstance();
		currentUserId =mAuth.getUid();
		firebaseFirestore= FirebaseFirestore.getInstance();
		
		//Vars
		NoteAvatar=view.findViewById(R.id.NoteAvatar);
		NoteUserName=view.findViewById(R.id.NoteUserName);
		NoteTitle=view.findViewById(R.id.NoteTitle);
		NoteDesc=view.findViewById(R.id.NoteDesc);
		NotePost=view.findViewById(R.id.NotePost);
		
		//Vars bar
		ImageButton add_photo_note = view.findViewById(R.id.add_photo_note);
		add_location_note = view.findViewById(R.id.add_location_note);
		
		add_photo_note.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				//for get image from gallery
				if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
					if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE)
							!= PackageManager.PERMISSION_GRANTED) {
						ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
					} else {
						imagePicker();
					}
				} else {
					imagePicker();
				}
			}
		});
		
		add_location_note.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				
				if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
						&& getActivity().checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION)
						!= PackageManager.PERMISSION_GRANTED) {
					requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, 100);
				} else {
					LocationManager locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
					Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
					
					try {
						String city = GetCityName.hereLocation(location.getLatitude(), location.getLongitude(),getActivity());
						Toast.makeText(getActivity(), "" + city, Toast.LENGTH_SHORT).show();
					} catch (Exception e) {
						Toast.makeText(getActivity(), "Not found!", Toast.LENGTH_SHORT).show();
					}
				}
			}
		});
		
		//Initialize dialogue
		intitializeInfo();
		
		NotePost.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				//get desc
				final String title=NoteTitle.getText().toString();
				final String desc=NoteDesc.getText().toString();
				
				//should not be empty fields
				if(!TextUtils.isEmpty(title) && !TextUtils.isEmpty(desc)) {
					
					//now create new collection posts (one-to-many) with users
					//but if embedded relation used document size with maximize
					//prepare new document to save inside post collection
					Map<String,Object> postMap=new HashMap<>();
					//save two image (original and compressed)
					postMap.put("user_id",currentUserId);
					postMap.put("title",title);
					postMap.put("desc",desc);
					postMap.put("location",LocationName());
					postMap.put("timestamp",FieldValue.serverTimestamp());
					postMap.put("type",0);
					
					//create new collection posts
					//to add document with random name use add() without document()
					firebaseFirestore.collection(SharedObjects.MainCollection).document(currentUserId)
							.collection("Timeline").add(postMap)
							.addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
								@Override
								public void onComplete(@NonNull Task<DocumentReference> task) {
									if(task.isSuccessful())
									{
										firebaseFirestore.collection("Counter").document(currentUserId)
												.collection(currentUserId).document("note")
												.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
											@Override
											public void onComplete(@NonNull Task<DocumentSnapshot> task) {
												if(task.isSuccessful())
												{
													int noNote=0;
													if(task.getResult().exists())
													{
														noNote=Integer.parseInt(task.getResult().get("note").toString());
													}
													
													Map<String,Object> NoteCount=new HashMap<>();
													NoteCount.put("note",noNote+1);
													firebaseFirestore.collection("Counter").document(currentUserId)
															.collection(currentUserId).document("note")
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
			}
		});
		
		return builder.create();
	}
	
	private void intitializeInfo() {
		ArrayList<String> basicInfo=new ArrayList<String>();
		
		//TODO:PRIVATE MODE 0
		SharedPreferences pref=getActivity().getSharedPreferences("basicInfo",0);
		basicInfo.add(pref.getString("fname",""));
		basicInfo.add(pref.getString("lname",""));
		basicInfo.add(pref.getString("image",""));
		
		NoteUserName.setText(basicInfo.get(0)+" "+basicInfo.get(1));
		
		//to put into imageView until download image url
		RequestOptions placeholderOption=new RequestOptions();
		placeholderOption.placeholder(R.drawable.def_avatar);
		
		Glide.with(getActivity())
				.applyDefaultRequestOptions(placeholderOption)
				.load(basicInfo.get(2))
				.into(NoteAvatar);
	}
	
	private void imagePicker() {
		Intent in = new Intent(Intent.ACTION_PICK);
		in.setType("image/*");
		startActivityForResult(in, GALARY_INTENT);
		
		Profile.code = 2;
	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		
		if(requestCode==GALARY_INTENT && resultCode==RESULT_OK)
		{
			//TODO: get image from galary
			Uri uri=data.getData();
			
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
