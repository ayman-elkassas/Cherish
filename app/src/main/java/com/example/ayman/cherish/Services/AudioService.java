package com.example.ayman.cherish.Services;

import android.app.Service;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.example.ayman.cherish.Model.adapters.TimelineChildRecyAdapter;
import com.example.ayman.cherish.R;

import java.io.IOException;

public class AudioService extends Service {
	
	public static SeekBar seekBar;
	public static TextView dur;
	public static ImageButton play;
	
	MediaPlayer player;
	Handler handler;
	Runnable runnable;
	
	static int i=0;
	
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
			player.setAudioStreamType(AudioManager.STREAM_MUSIC);
			player.setDataSource(uri);
			
			player.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
				@Override
				public void onPrepared(MediaPlayer mp) {
//					TimelineChildRecyAdapter.VoiceViews(mp.getDuration());
					changeSeekbar();
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
	
//		TimelineChildRecyAdapter.seekBar(player.getDuration());
		try {
			int time = player.getDuration() / 1000;
			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
				seekBar.setMin(0);
			}
			seekBar.setMax(time);
			dur.setText((((int)player.getCurrentPosition()/1000)+1)+":" + String.valueOf(time));
			
			seekBar.setProgress(player.getCurrentPosition()/1000);
			
			handler=new Handler();
			
			runnable=new Runnable() {
				@Override
				public void run() {
					changeSeekbar();
				}
			};
			
			handler.postDelayed(runnable,1000);
			
			if(((int)player.getCurrentPosition()/1000)+1==seekBar.getMax())
			{
				play.setImageResource(R.drawable.play);
				seekBar.setProgress(0);
				stopSelf();
			}
			
			seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
				@Override
				public void onProgressChanged(SeekBar s, int progress, boolean fromUser) {
					if(fromUser)
					{
						seekBar.setProgress(progress);
						player.seekTo(progress);
					}
				}
				
				@Override
				public void onStartTrackingTouch(SeekBar s) {
				
				}
				
				@Override
				public void onStopTrackingTouch(SeekBar s) {
				
				}
			});
			
		}catch (Exception e)
		{
		}
		
	}
}
