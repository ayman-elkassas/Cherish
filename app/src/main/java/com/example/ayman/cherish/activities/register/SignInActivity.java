package com.example.ayman.cherish.activities.register;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ayman.cherish.R;
import com.example.ayman.cherish.activities.Profile.Profile;
import com.example.ayman.cherish.activities.failedMessages.ConnectionForward;
import com.example.ayman.cherish.networkConnectionTest.TestConnection;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
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
	
	//loading
	ProgressDialog mProgress;
	
	ConstraintLayout root_view;
	
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
		
		mProgress = new ProgressDialog(this);
		mProgress.setIndeterminateDrawable(getResources().getDrawable(R.drawable.custom_progress_dialog));
		mProgress.setIndeterminate(true);
		
		root_view=findViewById(R.id.root_view);
		
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
			ConnectionForward.forwardFailed(this);
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
				
				String email=signin_email.getText().toString();
				String password=signin_pass.getText().toString();
				
				if(!TextUtils.isEmpty(email)&&!TextUtils.isEmpty(password))
				{
					mProgress.setMessage("Checking credential...");
					mProgress.setCancelable(false);
					mProgress.show();
					//check if there is an account match with these fields
					mAuth.signInWithEmailAndPassword(email,password)
							.addOnCompleteListener(new OnCompleteListener<AuthResult>() {
								@Override
								public void onComplete(@NonNull Task<AuthResult> task) {
									if(task.isSuccessful())
									{
										Toast.makeText(SignInActivity.this,
												"Login Successfully...", Toast.LENGTH_LONG).show();
										sendToMain();
									}
									else
									{
										mProgress.dismiss();
										
										String errorMessage=task.getException().getMessage();
										Snackbar.make(root_view, errorMessage, Snackbar.LENGTH_LONG)
												.show();
									}
									
								}
							});
				}
				else
				{
					Snackbar.make(root_view, "Fields may be empty", Snackbar.LENGTH_LONG)
							.show();
				}
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
