package com.example.ayman.cherish.activities.accountSetup;

import android.graphics.Color;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.ayman.cherish.R;
import com.rakshakhegde.stepperindicator.StepperIndicator;

public class AccountSetup extends AppCompatActivity {
	
	StepperIndicator indicator;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_account_setup);
		
		
		
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
