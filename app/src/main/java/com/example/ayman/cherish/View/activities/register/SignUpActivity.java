package com.example.ayman.cherish.View.activities.register;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.example.ayman.cherish.R;
import com.example.ayman.cherish.View.activities.accountSetup.AccountSetup;

public class SignUpActivity extends AppCompatActivity implements View.OnClickListener {
	
	private TextView haveAccount;
	private Button signUp;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sign_up);
		
		//if you want to remove status bar
//		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
//		WindowManager.LayoutParams.FLAG_FULLSCREEN);
		
		haveAccount=findViewById(R.id.haveAccount);
		signUp=findViewById(R.id.signUp);
		
		//listener
		haveAccount.setOnClickListener(this);
		signUp.setOnClickListener(this);
		
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId())
		{
			case R.id.haveAccount:
				finish();
				break;
			case R.id.signUp:
				Intent in=new Intent(this,AccountSetup.class);
				startActivity(in);
				finish();
				break;
			default:
				break;
		}
	}
}
