package com.example.ayman.cherish.activities.splashScreen;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.ayman.cherish.activities.Profile.Profile;
import com.example.ayman.cherish.activities.onBoarding.OnBoardingActivity;

public class LoadingScreen extends AppCompatActivity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		
		try {
			Thread.sleep(2500);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		startAct();
		
	}
	
	void startAct()
	{
		Intent in=new Intent(this,Profile.class);
		startActivity(in);
		finish();
	}
}
