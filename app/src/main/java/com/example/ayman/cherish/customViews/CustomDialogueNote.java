package com.example.ayman.cherish.customViews;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.DialogFragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.ayman.cherish.R;
import com.example.ayman.cherish.activities.Profile.Profile;
import com.example.ayman.cherish.location.GetCityName;

import studio.carbonylgroup.textfieldboxes.ExtendedEditText;

import static android.app.Activity.RESULT_OK;

public class CustomDialogueNote extends DialogFragment {
	
	ExtendedEditText post_note_content;
	
	private static final int GALARY_INTENT = 2;
	
	ImageView add_location_note;
	
	@NonNull
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		LayoutInflater inflater = getActivity().getLayoutInflater();
		
		View view = inflater.inflate(R.layout.new_note, null);
		
		builder.setView(view);
		
		ImageButton add_photo_note = view.findViewById(R.id.add_photo_note);
		post_note_content = view.findViewById(R.id.post_note_content);
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
		
		return builder.create();
	}
	
	private void imagePicker() {
		Intent in = new Intent(Intent.ACTION_PICK);
		in.setType("image/*");
		startActivityForResult(in, GALARY_INTENT);
		
		Profile.code = 2;
	}
	
	//check if permission is granted
	@Override
	public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
		super.onRequestPermissionsResult(requestCode, permissions, grantResults);
		
		GetCityName.onRequestReturn(requestCode,grantResults,getActivity());
        
    }
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		
		if(requestCode==GALARY_INTENT && resultCode==RESULT_OK)
		{
			//TODO: get image from galary
			Uri uri=data.getData();
			
			post_note_content.setText(uri.toString());
			
		}
	}
}
