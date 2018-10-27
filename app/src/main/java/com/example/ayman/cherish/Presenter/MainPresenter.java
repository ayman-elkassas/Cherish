package com.example.ayman.cherish.Presenter;

import android.app.Activity;

import com.example.ayman.cherish.MainMVP.MainMVPInterfaceComponent;
import com.example.ayman.cherish.Model.MainModel;
import com.example.ayman.cherish.Model.models.SetupDataAccount;

import java.util.ArrayList;

public class MainPresenter implements MainMVPInterfaceComponent.IPresenter {
	
	MainModel model;
	Object view;
	
	SetupDataAccount setupDataAccount;
	
	public MainPresenter(Object newView) {
		//TODO:3-INITIALIZE TWO OBJECTS: MODEL AND VIEW FROM THEIR CLASSES
		model=new MainModel();
		view=newView;
		
		setupDataAccount=new SetupDataAccount();
	}
	
	@Override
	public void getMoreInfoData(String fname, String lname, String phone) {
		setupDataAccount.setFname(fname);
		setupDataAccount.setLname(lname);
		setupDataAccount.setPhone(phone);
	}
}
