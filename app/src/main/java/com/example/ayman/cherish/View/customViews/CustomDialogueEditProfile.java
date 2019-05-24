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
import com.example.ayman.cherish.Presenter.MainPresenter;
import com.example.ayman.cherish.R;
import com.example.ayman.cherish.View.activities.Profile.MainLancher;
import com.example.ayman.cherish.View.activities.Profile.MyPofile;
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

import org.apache.commons.lang3.StringUtils;

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

public class CustomDialogueEditProfile extends DialogFragment {
	
	ExtendedEditText firstName,lastName,bio;
	MaterialButton edit;
	
	static CircleImageView avatar_photoCherish,avatarProEdit;
	TextView PhotoUserName;
	
	//Flags
	static public int code=3;
	
	//firebase objects
	FirebaseAuth mAuth;
	StorageReference storageReference;
	FirebaseFirestore firebaseFirestore;
	
	String user_id;
	
	//MVP
	static private MainPresenter presenter;
	
	//imageUri
	static Uri imageUri=null;
	
	@NonNull
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		
		LayoutInflater inflater = getActivity().getLayoutInflater();
		
		View view=inflater.inflate(R.layout.editprofile_layout, null);
		
		builder.setView(view);
		
		//Vars
		firstName=view.findViewById(R.id.firstName);
		lastName=view.findViewById(R.id.lastName);
		bio=view.findViewById(R.id.bio);
		avatarProEdit=view.findViewById(R.id.avatarProEdit);
		
		edit=view.findViewById(R.id.edit);
		
		PhotoUserName=view.findViewById(R.id.PhotoUserName);
		avatar_photoCherish=view.findViewById(R.id.avatar_photoCherish);
		
		//get instance from firebase objects
		mAuth= FirebaseAuth.getInstance();
		storageReference= FirebaseStorage.getInstance().getReference();
		firebaseFirestore= FirebaseFirestore.getInstance();
		
		//get current user id
		user_id=mAuth.getCurrentUser().getUid();
		
		//Initialize dialogue
		intitializeInfo();
		
		//TODO:1-CREATE INSTANCE FROM PRESENTER CLASS
		presenter = new MainPresenter(this, getContext());
		
		avatarProEdit.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				
				MyPofile.code=3;
				
				CropImage.activity()
						.setGuidelines(CropImageView.Guidelines.ON)
						.setMinCropResultSize(512, 512)
						.setAspectRatio(1, 1)
						.start(getActivity());
			}
		});
		
		edit.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				final String fname= StringUtils.capitalize(StringUtils.lowerCase(
						firstName.getText().toString()));
				
				final String lname=StringUtils.capitalize(StringUtils.lowerCase(
						lastName.getText().toString()));
				
				final String bioProfile= StringUtils.capitalize(StringUtils.lowerCase(
						bio.getText().toString()));
				
				if(!TextUtils.isEmpty(fname) && !TextUtils.isEmpty(lname)
						&& !TextUtils.isEmpty(bioProfile)) {
					
					presenter.getMoreInfoDataEditProfile(fname,lname,bioProfile);
					
					//create profile image folder and image name defined with each user id
					//unique avatar image with userId as name
					StorageReference image_path = storageReference.child("profile_images")
							.child(user_id + ".jpg");
					
					image_path.putFile(MainPresenter.setupDataAccount.getImage_url()).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
						@Override
						public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
							
							if (task.isSuccessful()) {
								//firestore document
								storeFirestore(task);
							} else {
								String error = task.getException().getMessage();
								Toast.makeText(getActivity(),
										"Error : " + error, Toast.LENGTH_LONG).show();
							}
						}
					});
				}
				
			}
		});
		
//		newPhoto.setOnClickListener(new View.OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				//get desc
//				final String title=title_photo.getText().toString();
//
//				//should not be empty fields
//				if(!TextUtils.isEmpty(title) && postImageuri !=null) {
//					//get random value for image
//					final String randomName= UUID.randomUUID().toString();
//
//					//name of image depend on random value
//					//because the same user can be have many of post images
//					//In contrast of avatar is one image
//
//					//prepare file path for image in firebase storage
//					StorageReference filePath=storageReference.child("cherish_images")
//							.child(randomName+".jpg");
//
//					//upload image without compressed
//					filePath.putFile(postImageuri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
//						@Override
//						public void onComplete(@NonNull final Task<UploadTask.TaskSnapshot> task) {
//
//							if(task.isSuccessful())
//							{
//								//get download url from uploaded post image
//								final String download_url=task.getResult().getDownloadUrl().toString();
//
//								//prepare to compress image as thumb to save it too using compressor lib
//								//because preview it before restoring original size of image...
//								//************************************************
//								File newImageFile=new File(postImageuri.getPath());
//								try {
//									compressedImageFile = new Compressor(getActivity())
//											.setMaxHeight(100)
//											.setMaxWidth(100)
//											.setQuality(2)
//											.compressToBitmap(newImageFile);
//								} catch (IOException e) {
//									Toast.makeText(getActivity(), ""+e.getMessage().toString(), Toast.LENGTH_LONG).show();
//								}
//								ByteArrayOutputStream baos=new ByteArrayOutputStream();
//								compressedImageFile.compress(Bitmap.CompressFormat.JPEG,100,baos);
//								byte [] thumbData=baos.toByteArray();
//								//finish compress part
//								//*********************************************
//								//create new path in firebase storage thumbs inside post_images dir
//								//return type of putBytes is UploadTask not StorageReference
//								UploadTask uploadTask=storageReference.child("cherish_images/thumbs")
//										.child(randomName+".jpg").putBytes(thumbData);
//
//								uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
//									@Override
//									public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//
//										//get thubm download url
//										String downloadThumUri=taskSnapshot.getDownloadUrl().toString();
//
//										//now create new collection posts (one-to-many) with users
//										//but if embedded relation used document size with maximize
//										//prepare new document to save inside post collection
//										Map<String,Object> postMap=new HashMap<>();
//										//save two image (original and compressed)
//										postMap.put("image_url",download_url);
//										postMap.put("thumb",downloadThumUri);
//										postMap.put("title",title);
//										postMap.put("user_id",user_id);
//										postMap.put("type",1);
//										postMap.put("location",LocationName());
//										postMap.put("timestamp", FieldValue.serverTimestamp());
//
//										//create new collection posts
//										//to add document with random name use add() without document()
//										firebaseFirestore.collection(SharedObjects.MainCollection).document(user_id)
//												.collection("Timeline").add(postMap)
//												.addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
//													@Override
//													public void onComplete(@NonNull Task<DocumentReference> task) {
//														if(task.isSuccessful())
//														{
//															firebaseFirestore.collection("Counter").document(user_id)
//																	.collection(user_id).document("photo")
//																	.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
//																@Override
//																public void onComplete(@NonNull Task<DocumentSnapshot> task) {
//																	if(task.isSuccessful())
//																	{
//																		int noPhoto=0;
//																		if(task.getResult().exists())
//																		{
//																			noPhoto=Integer.parseInt(task.getResult().get("photo").toString());
//																		}
//
//																		Map<String,Object> NoteCount=new HashMap<>();
//																		NoteCount.put("photo",noPhoto+1);
//																		firebaseFirestore.collection("Counter").document(user_id)
//																				.collection(user_id).document("photo")
//																				.set(NoteCount).addOnCompleteListener(new OnCompleteListener<Void>() {
//																			@Override
//																			public void onComplete(@NonNull Task<Void> task) {
//																				if(task.isSuccessful())
//																				{
//																					Toast.makeText(getActivity(),
//																							"Cherish was added", Toast.LENGTH_SHORT).show();
//																					Intent in=new Intent(getActivity(),MainLancher.class);
//																					startActivity(in);
//																					getActivity().finish();
//																				}
//																			}
//																		});
//																	}
//																}
//															});
//														}
//													}
//												});
//									}
//								}).addOnFailureListener(new OnFailureListener() {
//									@Override
//									public void onFailure(@NonNull Exception e) {
//
//									}
//								});
//							}
//							else
//							{
//								String error=task.getException().getMessage();
//								Toast.makeText(getActivity(), ""+error,
//										Toast.LENGTH_LONG).show();
//							}
//						}
//					});
//
//				}
//			}
//		});
		
		
		return builder.create();
	}
	
	private void storeFirestore(Task<UploadTask.TaskSnapshot> task) {
		Uri download_uri;
		if(task!=null)
		{
			//get download url of image uploaded in firebase storage
			download_uri=task.getResult().getDownloadUrl();
			
			//prepare HashMap object
			Map<String,String> userMap=new HashMap<>();
			userMap.put("fname",MainPresenter.setupDataAccount.getFname());
			userMap.put("lname",MainPresenter.setupDataAccount.getLname());
			userMap.put("bio",MainPresenter.setupDataAccount.getBio());
			userMap.put("image",download_uri.toString());
			
			firebaseFirestore= FirebaseFirestore.getInstance();
			
			firebaseFirestore.collection("Users").document(user_id)
					.set(userMap).addOnCompleteListener(new OnCompleteListener<Void>() {
				@Override
				public void onComplete(@NonNull Task<Void> task) {
					if(task.isSuccessful())
					{

//                        Intent mainIntent=new Intent(getContext(), MainLancher.class);
//                        startActivity(mainIntent);
						firebaseFirestore.collection("Users").document(user_id).get()
								.addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
									@Override
									public void onComplete(@NonNull Task<DocumentSnapshot> task) {
										if(task.getResult().exists())
										{
											Toast.makeText(getActivity(), "Edit Successfully...", Toast.LENGTH_SHORT).show();
											Intent in=new Intent(getActivity(), MyPofile.class);
											startActivity(in);
											getActivity().finish();
										}
									}
								});
					}
					else {
						String error=task.getException().getMessage();
						Toast.makeText(getActivity(),
								"FireStore Error : "+error, Toast.LENGTH_LONG).show();
					}
				}
			});
		}
	}
	
	private void intitializeInfo() {
		ArrayList<String> basicInfo=new ArrayList<String>();
		
		//TODO:PRIVATE MODE 0
		SharedPreferences pref=getActivity().getSharedPreferences("basicInfo",0);
		basicInfo.add(pref.getString("fname",""));
		basicInfo.add(pref.getString("lname",""));
		basicInfo.add(pref.getString("image",""));
		
		PhotoUserName.setText(basicInfo.get(0)+" "+basicInfo.get(1));
		
		//to put into imageView until download image url
		RequestOptions placeholderOption=new RequestOptions();
		placeholderOption.placeholder(R.drawable.def_avatar);
		
		Glide.with(getActivity())
				.applyDefaultRequestOptions(placeholderOption)
				.load(basicInfo.get(2))
				.into(avatar_photoCherish);
	}
	
	public static void imageset(Uri uri)
	{
		presenter.setImageUri(uri);
	}
	
	//check if permission is granted
	@Override
	public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
		super.onRequestPermissionsResult(requestCode, permissions, grantResults);
		
		if(grantResults.length>0)
		{
			for (int i=0;i<grantResults.length;i++)
			{
				if(grantResults[i]==PackageManager.PERMISSION_GRANTED)
				{
					Toast.makeText(getActivity(), "Permission is granted", Toast.LENGTH_LONG).show();
				}
			}
		}
		else
		{
			Toast.makeText(getActivity(), "Denied permission"
					, Toast.LENGTH_LONG).show();
		}
	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
			CropImage.ActivityResult result = CropImage.getActivityResult(data);
			if (resultCode == RESULT_OK) {
				Uri resultUri = result.getUri();
				
				imageset(resultUri);
				imageUri=resultUri;
				
				avatarProEdit.setImageURI(resultUri);
			} else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
				Exception error = result.getError();
			}
		}
	}
}
