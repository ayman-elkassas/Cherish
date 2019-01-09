package com.example.ayman.cherish.Presenter;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;

import com.example.ayman.cherish.MainMVP.MainMVPInterfaceComponent;
import com.example.ayman.cherish.Model.MainModel;
import com.example.ayman.cherish.Model.models.SetupDataAccount;
import com.example.ayman.cherish.View.activities.Profile.Profile;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class MainPresenter implements MainMVPInterfaceComponent.IPresenter {
	
	Context con;
	
	MainModel model;
	Object view;
	
	public static SetupDataAccount setupDataAccount=new SetupDataAccount();
	
	public MainPresenter(Object newView,Context con) {
		//TODO:3-INITIALIZE TWO OBJECTS: MODEL AND VIEW FROM THEIR CLASSES
		
		view=newView;
		model=new MainModel(con);
		this.con=con;
	}
	
	@Override
	public void getMoreInfoData(String fname, String lname, String phone) {
		setupDataAccount.setFname(fname);
		setupDataAccount.setLname(lname);
		setupDataAccount.setPhone(phone);
	}
	
	@Override
	public Boolean ifUserGoogleAlreadySaved(String user_id) {
		return model.checkGooglesaved(user_id);
	}
	
	@Override
	public void verifyMobileNum(String phoneNo,Activity activity) {
		model.verifyMobileNumWithFirebase(phoneNo,activity);
	}
	
	@Override
	public void resendCode(String phoneNo, Activity activity) {
		model.resendCode(phoneNo,activity);
	}
	
	@Override
	public void setImageUri(Uri uri) {
		setupDataAccount.setImage_url(uri);
	}
	
	@Override
	public void getBasicInfoAccount(String id, FirebaseFirestore firebaseFirestore) {
		model.getBasicInfoFirebase(id,firebaseFirestore);
		
		//start fetch data from shared preferences
		
		ArrayList<String> basicInfo=new ArrayList<String>();
		
		//get from sharedP..
		
		//TODO:PRIVATE MODE 0
		SharedPreferences pref=con.getSharedPreferences("basicInfo",0);
		basicInfo.add(pref.getString("fname",""));
		basicInfo.add(pref.getString("lname",""));
		basicInfo.add(pref.getString("image",""));
		
		((Profile)view).onBasicDataReceive(basicInfo);
		
	}
}
