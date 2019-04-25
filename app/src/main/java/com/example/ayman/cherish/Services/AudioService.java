package com.example.ayman.cherish.Services;

import android.app.Service;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Handler;
import android.os.IBinder;

import com.example.ayman.cherish.Model.adapters.TimelineChildRecyAdapter;
import com.example.ayman.cherish.R;

import java.io.IOException;

public class AudioService extends Service {
	
	MediaPlayer player;
	Handler handler;
	Runnable runnable;
	
	public AudioService() {
	}
	
	@Override
	public IBinder onBind(Intent intent) {
		// TODO: Return the communication channel to the service.
		throw new UnsupportedOperationException("Not yet implemented");
	}
	
	@Override
	public int onStartCommand(final Intent intent, int flags, int startId) {
		
		Thread th= new Thread(new Runnable() {
			@Override
			public void run() {
				playAudio(intent.getStringExtra("uri"));
//				while (isRunning)
//				{
//					try {
//						Thread.sleep(2000);
//					} catch (InterruptedException e) {
//						e.printStackTrace();
//					}
//				}
			}
		});
		
		th.start();
		
		return super.onStartCommand(intent, flags, startId);
	}
	
	private void playAudio(String uri) {
		
		
		player = new MediaPlayer();
		try {
			player.setDataSource(uri);
			player.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
				@Override
				public void onPrepared(MediaPlayer mp) {
					TimelineChildRecyAdapter.VoiceViews(mp.getDuration());
					mp.start();
				}
			});
			player.prepareAsync();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		player.pause();
		player.release();
	}
	
	private void changeSeekbar() {
		TimelineChildRecyAdapter.holder.seekbarVoice.setProgress(player.getCurrentPosition());
		
		if(player.isPlaying())
		{
			runnable=new Runnable() {
				@Override
				public void run() {
					changeSeekbar();
				}
			};
			
			handler.postDelayed(runnable,1000);
		}
	}
}
