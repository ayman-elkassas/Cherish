package com.example.ayman.cherish.activities.register;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.example.ayman.cherish.R;

public class SignInActivity extends AppCompatActivity implements View.OnClickListener {
	
	private TextView newAccount;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sign_in);
		
//		//if you want to remove status bar
//		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
//        WindowManager.LayoutParams.FLAG_FULLSCREEN);
		
		newAccount=findViewById(R.id.newAccount);
		newAccount.setOnClickListener(this);
		
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId())
		{
			case R.id.newAccount:
				Intent in=new Intent(this,SignUpActivity.class);
				startActivity(in);
				break;
			default:
				break;
		}
	}
}
