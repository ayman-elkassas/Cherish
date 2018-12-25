package com.example.ayman.cherish.Presenter;

import android.app.Activity;
import android.content.Context;

import com.example.ayman.cherish.MainMVP.MainMVPInterfaceComponent;
import com.example.ayman.cherish.Model.MainModel;
import com.example.ayman.cherish.Model.models.SetupDataAccount;

import java.util.ArrayList;

public class MainPresenter implements MainMVPInterfaceComponent.IPresenter {
	
	Context con;
	
	MainModel model;
	Object view;
	
	static SetupDataAccount setupDataAccount=new SetupDataAccount();
	
	public MainPresenter(Object newView,Context con) {
		//TODO:3-INITIALIZE TWO OBJECTS: MODEL AND VIEW FROM THEIR CLASSES
		
		view=newView;
		model=new MainModel(con);
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
	
}
