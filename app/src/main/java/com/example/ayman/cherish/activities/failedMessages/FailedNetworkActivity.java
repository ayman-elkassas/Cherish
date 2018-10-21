package com.example.ayman.cherish.activities.failedMessages;

import android.os.Bundle;
import android.support.design.button.MaterialButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ayman.cherish.R;
import com.example.ayman.cherish.networkConnectionTest.TestConnection;

public class FailedNetworkActivity extends AppCompatActivity {
	
	MaterialButton retry;
	
	@Override
	protected void onCreate(Bundle savedInstaceState) {
		super.onCreate(savedInstaceState);
		setContentView(R.layout.activity_failed_network);
		this.retry = (MaterialButton) findViewById(R.id.retry);
		
		retry.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if(TestConnection.isConnected(FailedNetworkActivity.this))
				{
					finish();
				}
				else
				{
					Toast.makeText(FailedNetworkActivity.this,
							"Make sure from wifi connection!",
							Toast.LENGTH_LONG).show();
				}
			}
		});
		
		
	}
}
