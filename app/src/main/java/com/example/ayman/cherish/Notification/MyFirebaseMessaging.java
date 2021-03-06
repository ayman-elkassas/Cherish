package com.example.ayman.cherish.Notification;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.NotificationCompat;
import android.view.View;
import android.widget.Toast;

import com.example.ayman.cherish.Model.models.NotificationModel;
import com.example.ayman.cherish.View.Profilefragments.Notifications;
import com.example.ayman.cherish.View.activities.Profile.MainLancher;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Queue;

public class MyFirebaseMessaging extends FirebaseMessagingService {
	
	//Firebase
	FirebaseAuth mAuth;
	FirebaseFirestore firebaseFirestore;
	
	static int counter=0;
	
	@Override
	public void onMessageReceived(RemoteMessage remoteMessage) {
		super.onMessageReceived(remoteMessage);
		
		//firebase
		mAuth = FirebaseAuth.getInstance();
		firebaseFirestore = FirebaseFirestore.getInstance();
		
		String sented=remoteMessage.getData().get("sented");
		
		FirebaseUser firebaseUser= FirebaseAuth.getInstance().getCurrentUser();
		
		if(firebaseUser!=null &&sented.equals(firebaseUser.getUid()))
		{
			NotificationFireNow(remoteMessage);
		}
	}
	
	private void NotificationFireNow(RemoteMessage remoteMessage) {
		
		String user = remoteMessage.getData().get("user");
		String icon = remoteMessage.getData().get("icon");
		String title = remoteMessage.getData().get("title");
		String body = remoteMessage.getData().get("body");
		
		String cherishCommentId = remoteMessage.getData().get("cherishCommentId");
		String timestampComment = remoteMessage.getData().get("timestampComment");
		
		updateNotificationList(user,body,cherishCommentId,timestampComment);
		
		RemoteMessage.Notification notification = remoteMessage.getNotification();
		int j = Integer.parseInt(user.replaceAll("[\\D]", ""));
		Intent intent = new Intent(this, MainLancher.class);
		Bundle bundle = new Bundle();
		bundle.putString("userid", user);
		intent.putExtras(bundle);
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		PendingIntent pendingIntent = PendingIntent.getActivity(this,counter, intent, PendingIntent.FLAG_UPDATE_CURRENT);
		
		long[] v = {500,1000};
		
		Uri defaultSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
		NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
				.setSmallIcon(Integer.parseInt(icon))
				.setContentTitle(title)
				.setContentText(body)
				.setAutoCancel(true)
				.setSound(defaultSound)
				.setVibrate(v)
				.setContentIntent(pendingIntent);
		NotificationManager noti = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
		
		noti.notify(counter++, builder.build());
	
	}
	
	private void updateNotificationList(final String user, final String body,final String cherishCommentId, final String timestampComment) {
		
		firebaseFirestore.collection("Users")
				.document(user)
				.get()
				.addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
					@Override
					public void onComplete(@NonNull Task<DocumentSnapshot> task) {
						if(task.getResult().exists())
						{
							NotificationModel notificationModel=new NotificationModel();
							
							notificationModel.setUser_id(user);
							notificationModel.setBodyNotify(body);
							
							notificationModel.setFname(task.getResult().getString("fname"));
							notificationModel.setLname(task.getResult().getString("lname"));
							notificationModel.setImage_url(task.getResult().getString("image"));
							
							notificationModel.setCherishCommentId(cherishCommentId);
							notificationModel.setTimestampComment(timestampComment);
							
//							ArrayList<NotificationModel> notificationModelArrayList=new ArrayList<>();
//							notificationModelArrayList.add(notificationModel);
							
							Notifications.notificationModelArrayList.add(0,notificationModel);
							
							Notifications.notifyAdapter.notifyDataSetChanged();
						}
					}
				});
	}
	
}
