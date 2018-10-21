package com.example.ayman.cherish.activities.register;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.example.ayman.cherish.R;
import com.example.ayman.cherish.activities.Profile.Profile;
import com.example.ayman.cherish.networkConnectionTest.TestConnection;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import studio.carbonylgroup.textfieldboxes.ExtendedEditText;

public class SignInActivity extends AppCompatActivity implements View.OnClickListener {
	
	//vars views
	private TextView newAccount;
	ExtendedEditText signin_email,signin_pass;
	Button signin_submit;
	
	//Firebase
	private FirebaseAuth mAuth;
	private android.widget.ProgressBar proLoading;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sign_in);
		
//		//if you want to remove status bar
//		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
//        WindowManager.LayoutParams.FLAG_FULLSCREEN);
		
		newAccount=findViewById(R.id.newAccount);
		signin_email=findViewById(R.id.signin_email);
		signin_pass=findViewById(R.id.signin_pass);
		signin_submit=findViewById(R.id.signin_submit);
		
		newAccount.setOnClickListener(this);
		signin_submit.setOnClickListener(this);
		
		//firebase snippet
		mAuth=FirebaseAuth.getInstance();
		
	}
	
	//1-first should check again
	@Override
	protected void onStart() {
		super.onStart();
		
		if(TestConnection.isConnected(getBaseContext()))
		{
			FirebaseUser currentUser=mAuth.getCurrentUser();
			
			//if current user not null forward to main profile
			if(currentUser!=null)
			{
				sendToMain();
			}
		}
		else
		{
		
		}
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId())
		{
			case R.id.newAccount:
				Intent in=new Intent(this,SignUpActivity.class);
				startActivity(in);
				break;
			case R.id.signin_submit:
			
			
			
//				Intent intent=new Intent(this,SignUpActivity.class);
//				startActivity(intent);
				break;
			default:
				break;
		}
	}
	
	private void sendToMain()
	{
		Intent in = new Intent(this, Profile.class);
		startActivity(in);
		finish();
	}
}
