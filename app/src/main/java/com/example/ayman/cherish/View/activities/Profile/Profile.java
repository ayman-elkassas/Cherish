package com.example.ayman.cherish.View.activities.Profile;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.TransitionDrawable;
import android.os.Environment;
import android.support.annotation.ColorInt;
import android.support.annotation.ColorRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.MenuRes;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.ayman.cherish.MainMVP.MainMVPInterfaceComponent;
import com.example.ayman.cherish.Model.bootSheet.CustomBottomSheetDialogFragment;
import com.example.ayman.cherish.Presenter.MainPresenter;
import com.example.ayman.cherish.R;
import com.example.ayman.cherish.View.activities.accountSetup.AccountSetup;
import com.example.ayman.cherish.View.activities.failedMessages.ConnectionForward;
import com.example.ayman.cherish.View.activities.onBoarding.OnBoardingActivity;
import com.example.ayman.cherish.View.activities.profileAdapters.TitleApdapter;
import com.example.ayman.cherish.View.customViews.CustomDialogueNote;
import com.example.ayman.cherish.View.customViews.CustomDialoguePhoto;
import com.example.ayman.cherish.Model.networkConnectionTest.TestConnection;
import com.example.ayman.cherish.View.customViews.CustomDialogueVideo;
import com.example.ayman.cherish.View.customViews.CustomDialogueVoice;
import com.example.ayman.cherish.permissions.Util;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import org.michaelbel.bottomsheet.BottomSheet;

import java.util.ArrayList;

import cafe.adriel.androidaudiorecorder.AndroidAudioRecorder;
import cafe.adriel.androidaudiorecorder.model.AudioChannel;
import cafe.adriel.androidaudiorecorder.model.AudioSampleRate;
import cafe.adriel.androidaudiorecorder.model.AudioSource;
import de.hdodenhof.circleimageview.CircleImageView;
import devlight.io.library.ntb.NavigationTabBar;

public class Profile extends AppCompatActivity
		implements NavigationView.OnNavigationItemSelectedListener
,MainMVPInterfaceComponent.IView{
	
	//firebase objects
	private FirebaseAuth mAuth;
	private FirebaseFirestore firebaseFirestore;
	
	//get current user
	private String currentUserId;
	
	//vars
	CircleImageView avatar;
	DrawerLayout drawer;
	ViewPager viewPager;
	FloatingActionButton fab,fab_photo,fab_video,fab_note,fab_voice;
	Boolean flag_fab=false;
	ImageView vert;
	CustomDialogueNote customDialogueNote;
	CustomDialoguePhoto customDialoguePhoto;
	CustomDialogueVideo customDialogueVideo;
	CustomDialogueVoice customDialogueVoice;
	TextView fullname,currentPager;
	
	TextView noPhotoProfile,noVideoProfile,noNoteProfile,noVocieProfile;
	
	//Flags
	static public int code=0;
	
	//MVP
	MainPresenter presenter;
	
	//Outside Objects
//	public static ArrayList<String> basicInfo=new ArrayList<>();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_profile);
		
		//fetch activity views
		avatar=findViewById(R.id.avatar);
		drawer= (DrawerLayout) findViewById(R.id.drawer_layout);
		NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
		viewPager = (ViewPager) findViewById(R.id.vp_horizontal_ntb);
		fab=findViewById(R.id.fab);
		fab_photo=findViewById(R.id.fab_photo);
		fab_video=findViewById(R.id.fab_video);
		fab_note=findViewById(R.id.fab_note);
		fab_voice=findViewById(R.id.fab_voice);
		vert=findViewById(R.id.vert);
		fullname=findViewById(R.id.fullname);
		currentPager=findViewById(R.id.currentPager);
		
		noVocieProfile=findViewById(R.id.noVocieProfile);
		noNoteProfile=findViewById(R.id.noNoteProfile);
		noPhotoProfile=findViewById(R.id.noPhotoProfile);
		noVideoProfile=findViewById(R.id.noVideoProfile);
		
		//MVP
		presenter=new MainPresenter(this,getApplicationContext());
		
		//firebase snippet
		mAuth = FirebaseAuth.getInstance();
		
		if(mAuth.getUid()==null)
		{
			sendToLogin();
		}
		else
		{
			currentUserId =mAuth.getUid();
			firebaseFirestore= FirebaseFirestore.getInstance();
			
			//listeners
			avatar.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					drawer.openDrawer(Gravity.LEFT);
				}
			});
			
			findViewById(R.id.back).setOnTouchListener(new View.OnTouchListener() {
				@Override
				public boolean onTouch(View v, MotionEvent event) {
					if(flag_fab)
					{
						hideFab();
						fab.setImageDrawable(getResources().getDrawable(R.drawable.add_new));
						flag_fab=false;
						findViewById(R.id.back).setBackgroundColor(android.R.drawable.screen_background_light_transparent);
					}
					return false;
				}
			});
			
			navigationView.setNavigationItemSelectedListener(this);
			
			fab.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					if(flag_fab==false)
					{
						showFab();
						fab.setImageDrawable(getResources().getDrawable(R.drawable.close_fab));
						flag_fab=true;
						ColorDrawable[] color = {new ColorDrawable(android.R.drawable.screen_background_light_transparent),
								new ColorDrawable(Color.argb(150,0,0,0))};
						
						TransitionDrawable trans = new TransitionDrawable(color);
						findViewById(R.id.back).setBackground(trans);
						trans.startTransition(100);
						
					}
					else
					{
						hideFab();
						fab.setImageDrawable(getResources().getDrawable(R.drawable.add_new));
						flag_fab=false;
						findViewById(R.id.back).setBackgroundColor(android.R.drawable.screen_background_light_transparent);
					}
				}
			});
			
			//fab listeners
			fab_photo.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					customDialoguePhoto =new CustomDialoguePhoto();
					customDialoguePhoto.show(getSupportFragmentManager(),null);
				}
			});
			fab_video.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					customDialogueVideo =new CustomDialogueVideo();
					customDialogueVideo.show(getSupportFragmentManager(),null);
				}
			});
			fab_note.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					customDialogueNote =new CustomDialogueNote();
					customDialogueNote.show(getSupportFragmentManager(),null);
				}
			});
			
			fab_voice.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					customDialogueVoice =new CustomDialogueVoice();
					customDialogueVoice.show(getSupportFragmentManager(),null);
				}
			});
			
			vert.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					//Initializing a bottom sheet
					BottomSheetDialogFragment bottomSheetDialogFragment = new CustomBottomSheetDialogFragment(getApplication());
					
					//show it
					bottomSheetDialogFragment.show(getSupportFragmentManager(), bottomSheetDialogFragment.getTag());
				}
			});
			
			intitializeInfo();
			
			initUI();
			
			initCountOfCherish();
		}
		
	}
	
	private void initCountOfCherish() {
		
		firebaseFirestore.collection("Counter").document(currentUserId)
				.collection(currentUserId).document("note")
				.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
			@Override
			public void onComplete(@NonNull Task<DocumentSnapshot> task) {
				if(task.isSuccessful())
				{
					if(task.getResult().exists()) {
						noNoteProfile.setText(task.getResult().get("note").toString());
					}
					else
					{
						noNoteProfile.setText("0");
					}
				}
			}
		});
		
		firebaseFirestore.collection("Counter").document(currentUserId)
				.collection(currentUserId).document("photo")
				.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
			@Override
			public void onComplete(@NonNull Task<DocumentSnapshot> task) {
				if(task.isSuccessful())
				{
					if(task.getResult().exists()) {
						noPhotoProfile.setText(task.getResult().get("photo").toString());
					}
					else
					{
						noPhotoProfile.setText("0");
					}
				}
				
				
			}
		});
		
		firebaseFirestore.collection("Counter").document(currentUserId)
				.collection(currentUserId).document("video")
				.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
			@Override
			public void onComplete(@NonNull Task<DocumentSnapshot> task) {
				if(task.isSuccessful())
				{
					if(task.getResult().exists())
					{
						noVideoProfile.setText(task.getResult().get("video").toString());
					}
					else
					{
						noVideoProfile.setText("0");
					}
				}
				
			}
		});
		
		firebaseFirestore.collection("Counter").document(currentUserId)
				.collection(currentUserId).document("voice")
				.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
			@Override
			public void onComplete(@NonNull Task<DocumentSnapshot> task) {
				if(task.isSuccessful())
				{
					if(task.getResult().exists())
					{
						noVocieProfile.setText(task.getResult().get("voice").toString());
					}
					else
					{
						noVocieProfile.setText("0");
					}
				}
				
			}
		});
	}
	
	private void intitializeInfo() {
		
		presenter.getBasicInfoAccount(mAuth.getCurrentUser().getUid(),firebaseFirestore);
	}
	
	@Override
	public void onBasicDataReceive(ArrayList<String> data) {
		fullname.setText(data.get(0)+" "+data.get(1));
//
		//to put into imageView until download image url
		RequestOptions placeholderOption=new RequestOptions();
		placeholderOption.placeholder(R.drawable.def_avatar);

		Glide.with(Profile.this)
				.applyDefaultRequestOptions(placeholderOption)
				.load(data.get(2))
				.into(avatar);
	}
	
	//First should check if there is now a user sign in or not
	@Override
	public void onStart() {
		super.onStart();
		
//		mAuth.signOut();
		
		//get current user object
		if(TestConnection.isConnected(getBaseContext()))
		{
			FirebaseUser currentUser = mAuth.getCurrentUser();
			
			//if current user null forward to login
			if (currentUser == null) {
				sendToLogin();
			}
			else
			{
				currentUserId=mAuth.getCurrentUser().getUid();
				firebaseFirestore.collection("Users").document(currentUserId)
						.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
					@Override
					public void onComplete(@NonNull Task<DocumentSnapshot> task) {
						if(task.isSuccessful())
						{
							//check if document not empty
							if(!task.getResult().exists()) {
								Toast.makeText(Profile.this, "Complete Account information",
										Toast.LENGTH_SHORT).show();
								Intent in=new Intent(Profile.this, AccountSetup.class);
								startActivity(in);
								finish();
							}
						}
					}
				});
			}
		}
		else
		{
			ConnectionForward.forwardFailed(this);
		}
	}
	
	private void showFab() {
		fab_photo.show();
		fab_video.show();
		fab_note.show();
		fab_voice.show();
	}
	
	private void hideFab() {
		fab_photo.hide();
		fab_video.hide();
		fab_note.hide();
		fab_voice.hide();
	}
	
	@Override
	public void onBackPressed() {
		DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
		if (drawer.isDrawerOpen(GravityCompat.START)) {
			drawer.closeDrawer(GravityCompat.START);
		} else {
			super.onBackPressed();
		}
	}
	
	@SuppressWarnings("StatementWithEmptyBody")
	@Override
	public boolean onNavigationItemSelected(MenuItem item) {
		// Handle navigation view item clicks here.
		int id = item.getItemId();
		
		if (id == R.id.nav_camera) {
			
			viewPager.setCurrentItem(0);
		
		} else if (id == R.id.nav_gallery) {
			
			viewPager.setCurrentItem(1);
		
		} else if (id == R.id.nav_slideshow) {
			viewPager.setCurrentItem(2);
			
		} else if (id == R.id.logout) {
			mAuth.signOut();
			sendToLogin();
		}
		
		DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
		drawer.closeDrawer(GravityCompat.START);
		return true;
	}
	
	
	private void initUI() {
		
		TitleApdapter titleApdapter=new TitleApdapter(getSupportFragmentManager());
		
		viewPager.setAdapter(titleApdapter);
		viewPager.setCurrentItem(0);
		
		final String[] colors = getResources().getStringArray(R.array.default_preview);
		
		final NavigationTabBar navigationTabBar = (NavigationTabBar) findViewById(R.id.ntb_horizontal);
		final ArrayList<NavigationTabBar.Model> models = new ArrayList<>();
		models.add(
				new NavigationTabBar.Model.Builder(
						getResources().getDrawable(R.drawable.timeline3),
						Color.parseColor(colors[5]))
//						.selectedIcon(getResources().getDrawable(R.drawable.timeline2))
						.title("Timeline")
						.badgeTitle("2")
						.build()
		);
		models.add(
				new NavigationTabBar.Model.Builder(
						getResources().getDrawable(R.drawable.baby_boy),
						Color.parseColor(colors[5]))
//                        .selectedIcon(getResources().getDrawable(R.drawable.ic_eighth))
						.title("My Children")
						.badgeTitle("7")
						.build()
		);
		models.add(
				new NavigationTabBar.Model.Builder(
						getResources().getDrawable(R.drawable.family),
						Color.parseColor(colors[5]))
//						.selectedIcon(getResources().getDrawable(R.drawable.ic_seventh))
						.title("Family")
						.badgeTitle("4")
						.build()
		);
		models.add(
				new NavigationTabBar.Model.Builder(
						getResources().getDrawable(R.drawable.search),
						Color.parseColor(colors[5]))
//                        .selectedIcon(getResources().getDrawable(R.drawable.ic_eighth))
						.title("Search")
						.build()
		);
		models.add(
				new NavigationTabBar.Model.Builder(
						getResources().getDrawable(R.drawable.bell),
						Color.parseColor(colors[5]))
//                        .selectedIcon(getResources().getDrawable(R.drawable.search))
						.title("Notifications")
						.badgeTitle("5")
						.build()
		);
		
		navigationTabBar.setModels(models);
		navigationTabBar.setViewPager(viewPager, 0);
		
		//IMPORTANT: ENABLE SCROLL BEHAVIOUR IN COORDINATOR LAYOUT
		navigationTabBar.setBehaviorEnabled(false);
		
		navigationTabBar.setOnTabBarSelectedIndexListener(new NavigationTabBar.OnTabBarSelectedIndexListener() {
			@Override
			public void onStartTabSelected(final NavigationTabBar.Model model, final int index) {
				if(index==viewPager.getCurrentItem())
				{
				}
			}
			
			@Override
			public void onEndTabSelected(final NavigationTabBar.Model model, final int index) {
				model.hideBadge();
			}
		});
		
		navigationTabBar.getModels().get(0).showBadge();
		navigationTabBar.getModels().get(1).showBadge();
		navigationTabBar.getModels().get(2).showBadge();
		navigationTabBar.getModels().get(4).showBadge();
		
		navigationTabBar.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
			@Override
			public void onPageScrolled(final int position, final float positionOffset, final int positionOffsetPixels) {
			}
			
			@Override
			public void onPageSelected(final int position) {
			
			}
			
			@Override
			public void onPageScrollStateChanged(final int state) {
			
			}
		});
		
		final CoordinatorLayout coordinatorLayout = (CoordinatorLayout) findViewById(R.id.parent);
		
		final CollapsingToolbarLayout collapsingToolbarLayout =
				(CollapsingToolbarLayout) findViewById(R.id.toolbar);
		collapsingToolbarLayout.setExpandedTitleColor(Color.parseColor("#ffffff"));
		collapsingToolbarLayout.setCollapsedTitleTextColor(Color.parseColor("#ffffff"));
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		
		if(code==1)
		{
			customDialoguePhoto.onActivityResult(requestCode,resultCode,data);
		}
		else if(code==2)
		{
			customDialogueNote.onActivityResult(requestCode,resultCode,data);
		}
		else if(code==3)
		{
//			customDialogueVideo.onActivityResult(requestCode,resultCode,data);
		}
	}
	
	private void sendToLogin() {
		
		SharedPreferences pref=getSharedPreferences("basicInfo",0);
		//TODO:handler object like pen used to write on file test
		SharedPreferences.Editor handler=pref.edit();
		handler.putBoolean("flag",false);
		handler.commit();
		
		Intent in = new Intent(this, OnBoardingActivity.class);
		startActivity(in);
		finish();
	}
	
	
}

