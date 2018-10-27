package com.example.ayman.cherish.View.activities.register;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ayman.cherish.Model.helpers.StartActivityCheckSession;
import com.example.ayman.cherish.R;
import com.example.ayman.cherish.View.activities.Profile.Profile;
import com.example.ayman.cherish.View.activities.accountSetup.AccountSetup;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.shobhitpuri.custombuttons.GoogleSignInButton;

import studio.carbonylgroup.textfieldboxes.ExtendedEditText;
import studio.carbonylgroup.textfieldboxes.TextFieldBoxes;

public class SignUpActivity extends AppCompatActivity implements View.OnClickListener {
	
	private TextView haveAccount;
	private studio.carbonylgroup.textfieldboxes.ExtendedEditText signupemail;
	private studio.carbonylgroup.textfieldboxes.ExtendedEditText signuppass;
	private studio.carbonylgroup.textfieldboxes.ExtendedEditText signupconfirmPass;
	private Button signupsubmit;
	private com.shobhitpuri.custombuttons.GoogleSignInButton googleSignUpButton;
	private ConstraintLayout root_view;
	private studio.carbonylgroup.textfieldboxes.TextFieldBoxes signuppasstextBox;
	private studio.carbonylgroup.textfieldboxes.TextFieldBoxes signupconfirmpasstextBox;
	private studio.carbonylgroup.textfieldboxes.TextFieldBoxes signupemailtextBox;
	
	//loading
	ProgressDialog mProgress;
	
	//Firebase
	private FirebaseAuth mAuth;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sign_up);
		
		//Define get By id
		this.signuppasstextBox = (TextFieldBoxes) findViewById(R.id.signup_pass_textBox);
		this.signupconfirmpasstextBox = (TextFieldBoxes) findViewById(R.id.signup_confirmPass_textBox);
		this.signupemailtextBox = (TextFieldBoxes) findViewById(R.id.signin_email_textBox);
		this.googleSignUpButton = (GoogleSignInButton) findViewById(R.id.googleSignUpButton);
		this.signupsubmit = (Button) findViewById(R.id.signup_submit);
		this.signupconfirmPass = (ExtendedEditText) findViewById(R.id.signup_confirmPass);
		this.signuppass = (ExtendedEditText) findViewById(R.id.signup_pass);
		this.signupemail = (ExtendedEditText) findViewById(R.id.signup_email);
		haveAccount=findViewById(R.id.haveAccount);
		root_view=findViewById(R.id.root_view);
		
		//if you want to remove status bar
//		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
//		WindowManager.LayoutParams.FLAG_FULLSCREEN);
		
		//listeners
		haveAccount.setOnClickListener(this);
		signupsubmit.setOnClickListener(this);
		googleSignUpButton.setOnClickListener(this);
		
		//Loading
		mProgress = new ProgressDialog(this);
		mProgress.setIndeterminateDrawable(getResources().getDrawable(R.drawable.custom_progress_dialog));
		mProgress.setIndeterminate(true);
		
		//firebase snippet
		mAuth= FirebaseAuth.getInstance();
		
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId())
		{
			case R.id.haveAccount:
				finish();
				break;
			case R.id.signup_submit:
				signUp();
				break;
			case R.id.googleSignUpButton:
				googleSignUp();
				break;
			default:
				break;
		}
	}
	
	@Override
	protected void onStart() {
		super.onStart();
		
		if(StartActivityCheckSession.OnStartCheckSession(getBaseContext(),mAuth))
		{
			sendToMain();
		}
		
	}
	
	private void signUp() {
		
		String email=signupemail.getText().toString();
		String password=signuppass.getText().toString();
		String con_password=signupconfirmPass.getText().toString();
		
		if(!TextUtils.isEmpty(email)&&!TextUtils.isEmpty(password)&&
				!TextUtils.isEmpty(con_password)) {
			
			if(password.equals(con_password)) {
				mProgress.setMessage("Construct your profile...");
				mProgress.setCancelable(false);
				mProgress.show();
				
				//Create Account Credential
				mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
					@Override
					public void onComplete(@NonNull Task<AuthResult> task) {
						if(task.isSuccessful())
						{
							Intent in = new Intent(SignUpActivity.this,AccountSetup.class);
							startActivity(in);
							finish();
						}
						else
						{
							mProgress.dismiss();
							String errorMessage=task.getException().getMessage();
//							Toast.makeText(SignUpActivity.this,
//									""+errorMessage, Toast.LENGTH_LONG).show();
							signupemailtextBox.setError("Email @.com",true);
							signuppasstextBox.setError("min 6 char",false);
						}
					}
				});
				
			}
			else
			{
//				Snackbar.make(root_view, "Password and confirm password fields doesn't match", Snackbar.LENGTH_LONG)
//						.show();
				signupconfirmpasstextBox.setError("confirm password field doesn't match",true);
			}
		}
		else
		{
//			Snackbar.make(root_view, "Fields may be empty", Snackbar.LENGTH_LONG)
//					.show();
			signupemailtextBox.setError("Field may be empty",true);
			signuppasstextBox.setError("Field may be empty",false);
			signupconfirmpasstextBox.setError("Field may be empty",false);
		}
		
	}
	
	private void googleSignUp() {
	
	}
	
	private void sendToMain()
	{
		Intent in = new Intent(this, Profile.class);
		startActivity(in);
		finish();
	}
}
