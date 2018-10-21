package com.example.ayman.cherish.activities.Profile;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.TransitionDrawable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
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

import com.example.ayman.cherish.R;
import com.example.ayman.cherish.activities.failedMessages.ConnectionForward;
import com.example.ayman.cherish.activities.onBoarding.OnBoardingActivity;
import com.example.ayman.cherish.activities.profileAdapters.TitleApdapter;
import com.example.ayman.cherish.customViews.CustomDialogueNote;
import com.example.ayman.cherish.customViews.CustomDialoguePhoto;
import com.example.ayman.cherish.networkConnectionTest.TestConnection;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.sql.Connection;
import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;
import devlight.io.library.ntb.NavigationTabBar;

public class Profile extends AppCompatActivity
		implements NavigationView.OnNavigationItemSelectedListener {
	
	//firebase objects
	private FirebaseAuth mAuth;
	
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
	
	static public int code=0;
	
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
		
		//firebase snippet
		mAuth = FirebaseAuth.getInstance();
		
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
			
			}
		});
		
		initUI();
	}
	
	//First should check if there is now a user sign in or not
	@Override
	public void onStart() {
		super.onStart();
		
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
				//get current profile data..
			}
		}
		else
		{
			ConnectionForward.forwardFailed(this);
		}
	}
	
//	public void show(Activity activity, float x, float y)
//	{
//		final ViewGroup root = (ViewGroup) activity.getWindow().getDecorView().findViewById(android.R.id.content);
//
//		final View view = new View(getBaseContext());
//		view.setLayoutParams(new ViewGroup.LayoutParams(1, 1));
//		view.setBackgroundColor(Color.WHITE);
//
//		root.addView(view);
//
//		view.setX(x);
//		view.setY(y);
//
//		PopupMenu popupMenu = new PopupMenu(getBaseContext(), view, Gravity.RIGHT);
//
//		final MenuInflater menuInflater=popupMenu.getMenuInflater();
//		menuInflater.inflate(R.menu.profile,popupMenu.getMenu());
//
//		popupMenu.setOnDismissListener(new PopupMenu.OnDismissListener()
//		{
//			@Override
//			public void onDismiss(PopupMenu menu)
//			{
//				root.removeView(view);
//			}
//		});
//
//		popupMenu.show();
//	}
//
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
		
		} else if (id == R.id.nav_manage) {
			viewPager.setCurrentItem(3);
		
		} else if (id == R.id.nav_share) {
			viewPager.setCurrentItem(4);
		
		} else if (id == R.id.nav_send) {
		
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
						Color.parseColor(colors[1]))
//						.selectedIcon(getResources().getDrawable(R.drawable.timeline2))
						.title("Timeline")
//						.badgeTitle("NTB")
						.build()
		);
		models.add(
				new NavigationTabBar.Model.Builder(
						getResources().getDrawable(R.drawable.baby_boy),
						Color.parseColor(colors[1]))
//                        .selectedIcon(getResources().getDrawable(R.drawable.ic_eighth))
						.title("My Children")
//						.badgeTitle("with")
						.build()
		);
		models.add(
				new NavigationTabBar.Model.Builder(
						getResources().getDrawable(R.drawable.family),
						Color.parseColor(colors[1]))
//						.selectedIcon(getResources().getDrawable(R.drawable.ic_seventh))
						.title("Family")
//						.badgeTitle("state")
						.build()
		);
		models.add(
				new NavigationTabBar.Model.Builder(
						getResources().getDrawable(R.drawable.messages),
						Color.parseColor(colors[1]))
//                        .selectedIcon(getResources().getDrawable(R.drawable.ic_eighth))
						.title("Messages")
//						.badgeTitle("icon")
						.build()
		);
		models.add(
				new NavigationTabBar.Model.Builder(
						getResources().getDrawable(R.drawable.bell),
						Color.parseColor(colors[1]))
//                        .selectedIcon(getResources().getDrawable(R.drawable.ic_eighth))
						.title("Notifications")
//						.badgeTitle("icon")
						.build()
		);
		
		navigationTabBar.setModels(models);
		navigationTabBar.setViewPager(viewPager, 0);
		
		//IMPORTANT: ENABLE SCROLL BEHAVIOUR IN COORDINATOR LAYOUT
		navigationTabBar.setBehaviorEnabled(true);
		
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
		
//		findViewById(R.id.fab).setOnClickListener(new View.OnClickListener() {
//			@Override
//			public void onClick(final View v) {
//				for (int i = 0; i < navigationTabBar.getModels().size(); i++) {
//					final NavigationTabBar.Model model = navigationTabBar.getModels().get(i);
//					navigationTabBar.postDelayed(new Runnable() {
//						@Override
//						public void run() {
//							final String title = String.valueOf(new Random().nextInt(15));
//							if (!model.isBadgeShowed()) {
//								model.setBadgeTitle(title);
//								model.showBadge();
//							} else model.updateBadgeTitle(title);
//						}
//					}, i * 100);
//				}
//
//				coordinatorLayout.postDelayed(new Runnable() {
//					@Override
//					public void run() {
//						final Snackbar snackbar = Snackbar.make(navigationTabBar, "Coordinator NTB", Snackbar.LENGTH_SHORT);
//						snackbar.getView().setBackgroundColor(Color.parseColor("#9b92b3"));
//						((TextView) snackbar.getView().findViewById(R.id.snackbar_text))
//								.setTextColor(Color.parseColor("#423752"));
//						snackbar.show();
//					}
//				}, 1000);
//			}
//		});
		
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
	}
	
	private void sendToLogin() {
		Intent in = new Intent(this, OnBoardingActivity.class);
		startActivity(in);
		finish();
	}
}

