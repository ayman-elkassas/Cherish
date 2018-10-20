package com.example.ayman.cherish.customViews;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.DialogFragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.ayman.cherish.R;
import com.example.ayman.cherish.activities.Profile.Profile;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import studio.carbonylgroup.textfieldboxes.TextFieldBoxes;

import static android.app.Activity.RESULT_OK;

public class CustomDialoguePhoto extends DialogFragment {
    
    Uri resultUri=null;
    private Boolean ischanged=false;
    
    TextFieldBoxes post_note_content;
    
    ImageView add_photo_post;
	
	private static final int GALARY_INTENT=2;
	
	
	@NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        
        
        LayoutInflater inflater = getActivity().getLayoutInflater();
    
        View view=inflater.inflate(R.layout.new_photo, null);
        
        builder.setView(view);
        
        add_photo_post=view.findViewById(R.id.add_photo_post);
    
        add_photo_post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //for get image from gallery
                if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.M)
                {
                    if(ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE)
                            != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(getActivity(),new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},1);
                    }
                    else
                    {
//                        imagePicker();
	
	                    Intent in=new Intent(Intent.ACTION_PICK);
	                    in.setType("image/*");
	                    startActivityForResult(in,GALARY_INTENT);
	
	                    Profile.code=1;
            
                    }
                }
                else
                {
//                    imagePicker();
	                Intent in=new Intent(Intent.ACTION_PICK);
	                in.setType("image/*");
	                startActivityForResult(in,GALARY_INTENT);
	
	                Profile.code=1;
                }
            }
        });
        
        return builder.create();
    }
    
//    private void imagePicker() {
//        // start picker to get image for cropping and then use the image in cropping activity
//        CropImage.activity()
//                .setGuidelines(CropImageView.Guidelines.ON)
//                .setAspectRatio(1,1)
//                .start(getActivity());
//
//        Profile.code=1;
//    }
    
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
    
//    @Override
//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//
//        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
//            CropImage.ActivityResult result = CropImage.getActivityResult(data);
//            if (resultCode == RESULT_OK) {
//                resultUri= result.getUri();
//
//                add_photo_post.setImageURI(resultUri);
//
//                ischanged=true;
//
//            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
//                Exception error = result.getError();
//                Toast.makeText(getActivity(), ""+error.getMessage(), Toast.LENGTH_LONG).show();
//            }
//        }
//    }
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		
		if(requestCode==GALARY_INTENT && resultCode==RESULT_OK)
		{
			//TODO: get image from galary
			Uri uri=data.getData();
			add_photo_post.setImageURI(uri);
			
			ischanged=true;
		}
	}
}
