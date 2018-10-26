package com.example.ayman.cherish.View.activities.accountSetup;

import android.graphics.Color;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import com.example.ayman.cherish.R;
import com.rakshakhegde.stepperindicator.StepperIndicator;

public class AccountSetup extends AppCompatActivity {
	
	StepperIndicator indicator;
	private android.support.v7.widget.Toolbar setupToolbar;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_account_setup);
		
		this.setupToolbar = (Toolbar) findViewById(R.id.setupToolbar);
		
		//setup toolbar
		setSupportActionBar(setupToolbar);
		getSupportActionBar().setTitle("Account setup");
		
		final ViewPager pager = findViewById(R.id.pager);
		assert pager != null;
		pager.setAdapter(new PagerAdapter(getSupportFragmentManager()));
		
		indicator = findViewById(R.id.stepper_indicator);
		// We keep last page for a "finishing" page
		indicator.setViewPager(pager, true);
		
		indicator.addOnStepClickListener(new StepperIndicator.OnStepClickListener() {
			@Override
			public void onStepClicked(int step) {
				pager.setCurrentItem(step, true);
			}
		});
	}
}
