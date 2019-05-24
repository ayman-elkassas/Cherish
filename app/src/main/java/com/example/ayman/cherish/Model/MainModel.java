package com.example.ayman.cherish.Model;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.widget.Toast;

import com.example.ayman.cherish.MainMVP.MainMVPInterfaceComponent;
import com.example.ayman.cherish.Model.sharedClasses.SharedObjects;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class MainModel implements MainMVPInterfaceComponent.IModel {
	
	Context con;
	private String verificationId;
	
	private PhoneAuthProvider.ForceResendingToken resendToken;
	
	static ArrayList<String> basicInfo=new ArrayList<>();
	
	public MainModel(Context con) {
		this.con = con;
	}
	
	@Override
	public Boolean setData() {
		
		return null;
	}
	
	@Override
	public Boolean checkGooglesaved(String user_id) {
		
		//check if already have this account or not
		
		return true;
	}
	
	@Override
	public Boolean verifyMobileNumWithFirebase(String number,Activity activity) {
		
		Toast.makeText(con, number, Toast.LENGTH_SHORT).show();
		
		String code ="2";
		String num=number;
		
		if(num.isEmpty() || num.length()<10)
		{
			return false;
		}
		
		String phoneNum="+"+code+num;
		PhoneAuthProvider.getInstance().verifyPhoneNumber(
				phoneNum,
				60,
				TimeUnit.SECONDS,
				activity,
				mCallBack
		);
		return true;
	}
	
	private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallBack
			=new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
		
		@Override
		public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
			verificationId=s;
			resendToken=forceResendingToken;
		}
		
		@Override
		public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
			String code=phoneAuthCredential.getSmsCode();
			//validate here
			
			SharedObjects.verifyCode=code;
		}
		
		@Override
		public void onVerificationFailed(FirebaseException e) {
			Toast.makeText(con, ""+e.getMessage(), Toast.LENGTH_LONG).show();
		}
	};
	
	@Override
	public Boolean resendCode(String number, Activity activity) {
		
		PhoneAuthProvider.getInstance().verifyPhoneNumber(
				number,
				60,
				TimeUnit.SECONDS,
				activity,
				mCallBack,
				resendToken);
		
		return true;
	}
	
	@Override
	public void getBasicInfoFirebase(String id, FirebaseFirestore firebaseFirestore) {
		
		if(con.getSharedPreferences("basicInfo",0).getBoolean("flag",false))
		{
			//Set toolbar parameter views
			firebaseFirestore.collection("Users").document(id)
					.get()
					.addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
						@Override
						public void onComplete(@NonNull Task<DocumentSnapshot> task) {
							if (task.isSuccessful()) {
								if (task.getResult().exists()) {
									String fname = task.getResult().getString("fname");
									String lname = task.getResult().getString("lname");
									String image = task.getResult().getString("image");
									String bio = task.getResult().getString("bio");
									
									SharedPreferences pref=con.getSharedPreferences("basicInfo",0);
									
//									pref.edit().clear().commit();
									
									//TODO:handler object like pen used to write on file test
									SharedPreferences.Editor handler=pref.edit();
									handler.putString("fname",fname);
									handler.putString("lname",lname);
									handler.putString("image",image);
									handler.putString("bio",bio);
									handler.putBoolean("flag",true);
									handler.commit();
									
								}
							}
						}
					});
		}
	}
}
