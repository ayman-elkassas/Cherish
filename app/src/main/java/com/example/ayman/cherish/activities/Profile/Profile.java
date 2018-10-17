package com.example.ayman.cherish.activities.Profile;

import android.graphics.Color;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.ayman.cherish.R;

import java.util.ArrayList;
import java.util.Random;

import de.hdodenhof.circleimageview.CircleImageView;
import devlight.io.library.ntb.NavigationTabBar;

public class Profile extends AppCompatActivity
		implements NavigationView.OnNavigationItemSelectedListener {
	
	CircleImageView avatar;
	CircleImageView toolbarAvatar;
	DrawerLayout drawer;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_profile);
//		Toolbar toolbar = (Toolbar) findViewById(R.id.main_toolbar);
//		setSupportActionBar(toolbar);
//		getSupportActionBar().setTitle("Home");
//		toolbar.setTitleTextColor(Color.WHITE);
		
		avatar=findViewById(R.id.avatar);
		
		drawer= (DrawerLayout) findViewById(R.id.drawer_layout);
//		ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
//				this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
//		drawer.addDrawerListener(toggle);
//		toggle.syncState();
		
		avatar.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				drawer.openDrawer(Gravity.LEFT);
			}
		});

		NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
		navigationView.setNavigationItemSelectedListener(this);
		
		initUI();
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
			// Handle the camera action
		} else if (id == R.id.nav_gallery) {
		
		} else if (id == R.id.nav_slideshow) {
		
		} else if (id == R.id.nav_manage) {
		
		} else if (id == R.id.nav_share) {
		
		} else if (id == R.id.nav_send) {
		
		}
		
		DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
		drawer.closeDrawer(GravityCompat.START);
		return true;
	}
	
	
	private void initUI() {
		final ViewPager viewPager = (ViewPager) findViewById(R.id.vp_horizontal_ntb);
		viewPager.setAdapter(new PagerAdapter() {
			@Override
			public int getCount() {
				return 5;
			}
			
			@Override
			public boolean isViewFromObject(final View view, final Object object) {
				return view.equals(object);
			}
			
			@Override
			public void destroyItem(final View container, final int position, final Object object) {
				((ViewPager) container).removeView((View) object);
			}
			
			@Override
			public Object instantiateItem(final ViewGroup container, final int position) {
				final View view = LayoutInflater.from(
						getBaseContext()).inflate(R.layout.item_vp_list, null, false);
				
				final RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.rv);
				recyclerView.setHasFixedSize(true);
				recyclerView.setLayoutManager(new LinearLayoutManager(
								getBaseContext(), LinearLayoutManager.VERTICAL, false
						)
				);
				recyclerView.setAdapter(new RecycleAdapter());
				
				container.addView(view);
				return view;
			}
		});
		
		final String[] colors = getResources().getStringArray(R.array.default_preview);
		
		final NavigationTabBar navigationTabBar = (NavigationTabBar) findViewById(R.id.ntb_horizontal);
		final ArrayList<NavigationTabBar.Model> models = new ArrayList<>();
		models.add(
				new NavigationTabBar.Model.Builder(
						getResources().getDrawable(R.drawable.timeline3),
						Color.parseColor(colors[0]))
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
						Color.parseColor(colors[2]))
//						.selectedIcon(getResources().getDrawable(R.drawable.ic_seventh))
						.title("Family")
//						.badgeTitle("state")
						.build()
		);
		models.add(
				new NavigationTabBar.Model.Builder(
						getResources().getDrawable(R.drawable.messages),
						Color.parseColor(colors[3]))
//                        .selectedIcon(getResources().getDrawable(R.drawable.ic_eighth))
						.title("Messages")
//						.badgeTitle("icon")
						.build()
		);
		models.add(
				new NavigationTabBar.Model.Builder(
						getResources().getDrawable(R.drawable.bell),
						Color.parseColor(colors[4]))
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
		findViewById(R.id.fab).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(final View v) {
				for (int i = 0; i < navigationTabBar.getModels().size(); i++) {
					final NavigationTabBar.Model model = navigationTabBar.getModels().get(i);
					navigationTabBar.postDelayed(new Runnable() {
						@Override
						public void run() {
							final String title = String.valueOf(new Random().nextInt(15));
							if (!model.isBadgeShowed()) {
								model.setBadgeTitle(title);
								model.showBadge();
							} else model.updateBadgeTitle(title);
						}
					}, i * 100);
				}
				
				coordinatorLayout.postDelayed(new Runnable() {
					@Override
					public void run() {
						final Snackbar snackbar = Snackbar.make(navigationTabBar, "Coordinator NTB", Snackbar.LENGTH_SHORT);
						snackbar.getView().setBackgroundColor(Color.parseColor("#9b92b3"));
						((TextView) snackbar.getView().findViewById(R.id.snackbar_text))
								.setTextColor(Color.parseColor("#423752"));
						snackbar.show();
					}
				}, 1000);
			}
		});
		
		final CollapsingToolbarLayout collapsingToolbarLayout =
				(CollapsingToolbarLayout) findViewById(R.id.toolbar);
		collapsingToolbarLayout.setExpandedTitleColor(Color.parseColor("#ffffff"));
//		collapsingToolbarLayout.setScrimAnimationDuration(1000);
		
//		collapsingToolbarLayout.addView(toolbarAvatar);
		collapsingToolbarLayout.setCollapsedTitleTextColor(Color.parseColor("#ffffff"));
	}
	
	public class RecycleAdapter extends RecyclerView.Adapter<RecycleAdapter.ViewHolder> {
		
		@Override
		public ViewHolder onCreateViewHolder(final ViewGroup parent, final int viewType) {
			final View view = LayoutInflater.from(getBaseContext()).inflate(R.layout.item_list, parent, false);
			return new ViewHolder(view);
		}
		
		@Override
		public void onBindViewHolder(final ViewHolder holder, final int position) {
			holder.txt.setText(String.format("Navigation Item #%d", position));
		}
		
		@Override
		public int getItemCount() {
			return 20;
		}
		
		public class ViewHolder extends RecyclerView.ViewHolder {
			
			public TextView txt;
			
			public ViewHolder(final View itemView) {
				super(itemView);
				txt = (TextView) itemView.findViewById(R.id.txt_vp_item_list);
			}
		}
	}
}

