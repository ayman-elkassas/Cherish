package com.example.ayman.cherish.Model.helpers;

import android.content.Context;

import com.example.ayman.cherish.Model.networkConnectionTest.TestConnection;
import com.example.ayman.cherish.View.activities.failedMessages.ConnectionForward;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class StartActivityCheckSession {
	
	public static Boolean OnStartCheckSession(Context con,FirebaseAuth mAuth)
	{
		if(TestConnection.isConnected(con))
		{
			FirebaseUser currentUser=mAuth.getCurrentUser();
			
			//if current user not null forward to main profile
			if(currentUser!=null)
			{
				return true;
			}
			else
			{
				return false;
			}
		}
		else
		{
			ConnectionForward.forwardFailed(con);
		}
		
		return false;
	}
	
}
