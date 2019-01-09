package com.example.ayman.cherish.MainMVP;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class MainMVPInterfaceComponent {
	
	//TODO:CREATE MAIN CLASS THAT CONTENT 3 MAIN INTERFACES FOR MAIN COMP..
	
	//TODO:EACH CLASS WILL INHERENT YOUR OWN INTERFACE
	
	public interface IView {
		//TODO:CREATE ALL METHODS THAT WILL NEED IN VIEW ACTIVITY CLASSES
		void onBasicDataReceive(ArrayList<String> data);
		
	}
	
	public interface IPresenter {
		//TODO:CREATE ALL Abstract METHODS THAT WILL NEED IN PRESENTER CLASS
		void getMoreInfoData(String fname,String lname,String phone);
		Boolean ifUserGoogleAlreadySaved(String user_id);
		void verifyMobileNum(String phoneNo, Activity activity);
		void resendCode(String phoneNo, Activity activity);
		void setImageUri(Uri uri);
		
		void getBasicInfoAccount(String id, FirebaseFirestore firebaseFirestore);
		
	}
	
	public interface IModel {
		//TODO:CREATE ALL METHODS THAT WILL NEED IN MODEL CLASS
		Boolean setData();
		Boolean checkGooglesaved(String user_id);
		Boolean verifyMobileNumWithFirebase(String number,Activity activity);
		Boolean resendCode(String number,Activity activity);
		
		void getBasicInfoFirebase(String id, FirebaseFirestore firebaseFirestore);
	}
	
}
