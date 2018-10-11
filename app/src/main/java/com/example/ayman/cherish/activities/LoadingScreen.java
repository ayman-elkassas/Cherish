package com.example.ayman.cherish.activities;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.WindowManager;

import com.example.ayman.cherish.R;

import java.util.Timer;

public class LoadingScreen extends AppCompatActivity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		startAct();
		
	}
	
	void startAct()
	{
		Intent in=new Intent(this,SplashScreen.class);
//		//TODO:TO CLEAR ACTIVITY STACK
//		in.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
		startActivity(in);
		finish();
	}
}
