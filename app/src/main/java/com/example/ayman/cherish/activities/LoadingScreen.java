package com.example.ayman.cherish.activities;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.WindowManager;

import com.example.ayman.cherish.R;

public class LoadingScreen extends AppCompatActivity {
	
	private static int splashScreen=3000;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.loading_screen);
		
		//to full screen without status bar
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		
		//TODO:Delay for loading screen
		new Handler().postDelayed(new Runnable() {
			@Override
			public void run() {
				startAct();
				this.finish();
			}

			private void finish()
			{

			}
		},splashScreen);
		
	}
	
	void startAct()
	{
		Intent in=new Intent(this,SplashScreen.class);
//		//TODO:TO CLEAR ACTIVITY STACK
		in.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
		startActivity(in);
//		finish();
	}
}
