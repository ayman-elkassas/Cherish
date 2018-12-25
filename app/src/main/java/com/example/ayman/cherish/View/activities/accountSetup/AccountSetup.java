package com.example.ayman.cherish.View.activities.accountSetup;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import com.example.ayman.cherish.R;
import com.example.ayman.cherish.View.Setupfragments.AddAvatar;
import com.rakshakhegde.stepperindicator.StepperIndicator;
import com.theartofdev.edmodo.cropper.CropImage;

public class AccountSetup extends AppCompatActivity {
	
	StepperIndicator indicator;
	private android.support.v7.widget.Toolbar setupToolbar;
	
	Uri resultUri=null;
	private Boolean ischanged=false;
	
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
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
			CropImage.ActivityResult result = CropImage.getActivityResult(data);
			if (resultCode ==RESULT_OK) {
				resultUri= result.getUri();
				
				AddAvatar.imageset(resultUri);

				ischanged=true;

			} else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
				Exception error = result.getError();
			}
		}
		
	}
}
