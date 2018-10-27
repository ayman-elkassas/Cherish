package com.example.ayman.cherish.View.activities.register;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ayman.cherish.Model.helpers.StartActivityCheckSession;
import com.example.ayman.cherish.R;
import com.example.ayman.cherish.View.activities.Profile.Profile;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.shobhitpuri.custombuttons.GoogleSignInButton;

import studio.carbonylgroup.textfieldboxes.ExtendedEditText;
import studio.carbonylgroup.textfieldboxes.TextFieldBoxes;

public class SignInActivity extends AppCompatActivity implements View.OnClickListener {
	
	//vars views
	private TextView newAccount;
	ExtendedEditText signin_email,signin_pass;
	Button signin_submit;
	
	ConstraintLayout root_view;
	private android.widget.ImageView imageView;
	private TextView textView3;
	private ExtendedEditText signinemail;
	private studio.carbonylgroup.textfieldboxes.TextFieldBoxes signinemailtextBox;
	private ExtendedEditText signinpass;
	private studio.carbonylgroup.textfieldboxes.TextFieldBoxes signinpasstextBox;
	private Button signinsubmit;
	private TextView textView2;
	private com.shobhitpuri.custombuttons.GoogleSignInButton googleSignInButton;
	private ConstraintLayout rootview;
	
	//Firebase
	private FirebaseAuth mAuth;
	
	//loading
	ProgressDialog mProgress;
	
	//public vars
	private static final int RC_SIGN_IN=1;
	private GoogleApiClient googleApiClient;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sign_in);
		
//		//if you want to remove status bar
//		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
//        WindowManager.LayoutParams.FLAG_FULLSCREEN);
		
		//Define get By id
		this.googleSignInButton = (GoogleSignInButton) findViewById(R.id.googleSignInButton);
		this.signinpasstextBox = (TextFieldBoxes) findViewById(R.id.signin_pass_textBox);
		this.signinemailtextBox = (TextFieldBoxes) findViewById(R.id.signin_email_textBox);
		newAccount=findViewById(R.id.newAccount);
		signin_email=findViewById(R.id.signin_email);
		signin_pass=findViewById(R.id.signin_pass);
		signin_submit=findViewById(R.id.signin_submit);
		root_view=findViewById(R.id.root_view);
		
		//Loading
		mProgress = new ProgressDialog(this);
		mProgress.setIndeterminateDrawable(getResources().getDrawable(R.drawable.custom_progress_dialog));
		mProgress.setIndeterminate(true);
		
		newAccount.setOnClickListener(this);
		signin_submit.setOnClickListener(this);
		googleSignInButton.setOnClickListener(this);
		
		//firebase snippet
		mAuth=FirebaseAuth.getInstance();
		
		// Configure Google Sign In
		GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
				.requestIdToken(getString(R.string.default_web_client_id))
				.requestEmail()
				.build();
		
		googleApiClient=new GoogleApiClient.Builder(getApplicationContext())
				.enableAutoManage(this, new GoogleApiClient.OnConnectionFailedListener() {
					@Override
					public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
						Toast.makeText(SignInActivity.this,
								"You Got an Error", Toast.LENGTH_LONG).show();
					}
				}).addApi(Auth.GOOGLE_SIGN_IN_API,gso)
				.build();
		
	}
	
	//1-first should check again
	@Override
	protected void onStart() {
		super.onStart();
		
		if (StartActivityCheckSession.OnStartCheckSession(getBaseContext(), mAuth))
		{
			sendToMain();
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
				signIn();
				break;
			case R.id.googleSignInButton:
				signInWithGoogle();
				break;
			default:
				break;
		}
	}
	
	private void signInWithGoogle() {
		Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(googleApiClient);
		startActivityForResult(signInIntent, RC_SIGN_IN);
	}
	
	private void signIn()
	{
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
//										Snackbar.make(root_view, errorMessage, Snackbar.LENGTH_LONG)
//												.show();
								
								signinemailtextBox.setError("Email @.com",true);
								signinpasstextBox.setError("min 6 char",false);
								
							}
							
						}
					});
		}
		else
		{
//					Snackbar.make(root_view, "Fields may be empty", Snackbar.LENGTH_LONG)
//							.show();
			signinemailtextBox.setError("Field may be empty",true);
			signinpasstextBox.setError("Field may be empty",false);
		}
	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		
		// Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
		if (requestCode == RC_SIGN_IN) {
			Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
			try {
				// Google Sign In was successful, authenticate with Firebase
				GoogleSignInAccount account = task.getResult(ApiException.class);
				
				firebaseAuthWithGoogle(account);
				
			} catch (ApiException e) {
				// Google Sign In failed, update UI appropriately
				Toast.makeText(this, "Google sign in failed", Toast.LENGTH_LONG).show();
				// ...
			}
		}
	}
	
	private void firebaseAuthWithGoogle(GoogleSignInAccount account) {
		
		AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
		mAuth.signInWithCredential(credential)
				.addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
					@Override
					public void onComplete(@NonNull Task<AuthResult> task) {
						if (task.isSuccessful()) {
							
							
							
							sendToMain();
						} else {
							// If sign in fails, display a message to the user.
							Log.w("Status", "signInWithCredential:failure", task.getException());
							Snackbar.make(root_view,
									"Authentication Failed.", Snackbar.LENGTH_LONG).show();
						}
					}
				});
		
	}
	
	private void sendToMain()
	{
		Intent in = new Intent(this, Profile.class);
		startActivity(in);
		finish();
	}
}
