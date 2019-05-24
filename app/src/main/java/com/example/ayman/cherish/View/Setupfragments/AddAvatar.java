package com.example.ayman.cherish.View.Setupfragments;


import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.ayman.cherish.Presenter.MainPresenter;
import com.example.ayman.cherish.R;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * A simple {@link Fragment} subclass.
 */
public class AddAvatar extends Fragment implements View.OnClickListener {
	
	static CircleImageView setup_image;
	Uri resultUri=null;
	private Boolean ischanged=false;
	
	//MVP
	static private MainPresenter presenter;
	
	public AddAvatar() {
		// Required empty public constructor
	}
	
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
	                         Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		View view= inflater.inflate(R.layout.fragment_add_avatar, container, false);
		
		setup_image=view.findViewById(R.id.setup_image);
		setup_image.setOnClickListener(this);
		
		//TODO:1-CREATE INSTANCE FROM PRESENTER CLASS
		presenter = new MainPresenter(this, getContext());
		
		imageset(MainPresenter.setupDataAccount.getImage_url());
		
		return view;
	
	}
	
	@Override
	public void onClick(View v) {
		if(v==setup_image)
		{
			//for get image from gallery
			if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.M)
			{
				if(ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE)
						!= PackageManager.PERMISSION_GRANTED) {
					ActivityCompat.requestPermissions(getActivity(),new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},1);
				}
				else
				{
					imagePicker();
				}
			}
			else
			{
				imagePicker();
			}
		}
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
	
	private void imagePicker() {
		// start picker to get image for cropping and then use the image in cropping activity
		CropImage.activity()
				.setGuidelines(CropImageView.Guidelines.ON)
				.setAspectRatio(1,1)
				.start(getActivity());
		
	}
	
	public static void imageset(Uri uri)
	{
		setup_image.setImageURI(uri);
		presenter.setImageUri(uri);
	}
}
