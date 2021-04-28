package course.examples.Services.KeyClient;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.media.AudioAttributes;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.util.Log;
import android.widget.ProgressBar;

import androidx.core.app.NotificationCompat;

import java.io.IOException;

/**
 * Author: John Mistica
 * Date: 4/28/2021
 * Assignment: Project 5
 * Reference: Built on top of MusicPlayingServiceExample app from CS 478 Spring 2021 Blackboard
 * Description: Music service that uses media player to download
 * a song from a url and play it in the foreground on a separate thread
 */

public class MusicService extends Service {

	@SuppressWarnings("unused")
	private final String TAG = "MusicService";

	private static final int NOTIFICATION_ID = 1;
	private MediaPlayer mediaPlayer = new MediaPlayer();
	private int mStartID;
	private Notification notification ;

	private final Handler mHandler = new Handler(Looper.getMainLooper()) ;

	public static String url = "";
	private static String CHANNEL_ID = "Music player style" ;
	public static boolean startByList = false;


	@Override
	public void onCreate() {
		super.onCreate();

		Log.i("Service:","Service started");

		this.createNotificationChannel();

		// Create a notification area notification so the user
		// can get back to the MusicServiceClient

		final Intent notificationIntent = new Intent(getApplicationContext(),
				KeyServiceUser.class);

		final PendingIntent pendingIntent = PendingIntent.getActivity(this, 0,
				notificationIntent, 0) ;

		notification = new NotificationCompat.Builder(getApplicationContext(), CHANNEL_ID)
				.setSmallIcon(android.R.drawable.ic_media_play)
				.setOngoing(true).setContentTitle("Music Playing")
				.setContentText("Click to Access Music Player")
				.setTicker("Music is playing!")
				.setContentIntent(pendingIntent)
				.addAction(R.drawable.ic_launcher, "Show service", pendingIntent)
				.build();

		Log.i("MUSIC_SERVICE", ""+url);

		//start new thread to download and play song
		Thread t1 = new Thread(new ReadPageRunnable());
		t1.start();

	}

	public class ReadPageRunnable implements Runnable  {

		public void run() {
			mHandler.post(new Runnable() {
				public void run() {
					if(startByList){
						MainActivity.mProgressBar.setVisibility(ProgressBar.VISIBLE);
						MainActivity.mProgressBar.setProgress(0) ;
					}
					else{
						KeyServiceUser.mProgressBar.setVisibility(ProgressBar.VISIBLE);
						KeyServiceUser.mProgressBar.setProgress(0) ;
					}

				}
			} ) ;

			mediaPlayer.setAudioAttributes(
					new AudioAttributes.Builder()
							.setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
							.setUsage(AudioAttributes.USAGE_MEDIA)
							.build()
			);

			//takes a while to download so put in seperate thread
			try {
				mediaPlayer.setDataSource(url);
			} catch (IOException e) {
				e.printStackTrace();
			}

			mHandler.post(new Runnable() {
				public void run() {
					if(startByList){
						MainActivity.mProgressBar.setProgress(33) ;
					}
					else{
						KeyServiceUser.mProgressBar.setProgress(33) ;
					}

				}
			} ) ;

			try {
				mediaPlayer.prepare();
			} catch (IOException e) {
				e.printStackTrace();
			}

			mHandler.post(new Runnable() {
				public void run() {
					if(startByList){
						MainActivity.mProgressBar.setProgress(70) ;
					}
					else{
						KeyServiceUser.mProgressBar.setProgress(70) ;
					}


				}
			} ) ;

			try {  Thread.sleep(1000); }
			catch (InterruptedException e) { System.out.println("Thread interrupted!") ; }

			if (null != mediaPlayer) {

				mediaPlayer.setLooping(false);
				mediaPlayer.setOnCompletionListener(new OnCompletionListener() {

					@Override
					public void onCompletion(MediaPlayer mp) {
						stopSelf(mStartID);
					}
				});

				mediaPlayer.start() ;
			}

			mHandler.post(new Runnable() {
				public void run() {
					if(startByList){
						MainActivity.mProgressBar.setProgress(100) ;
					}
					else{
						KeyServiceUser.mProgressBar.setProgress(100) ;
					}


				}
			} ) ;

			startForeground(NOTIFICATION_ID, notification);



		}
	}

	// UB 11-12-2018:  Now Oreo wants communication channels...
	private void createNotificationChannel() {
		// Create the NotificationChannel, but only on API 26+ because
		// the NotificationChannel class is new and not in the support library
		CharSequence name = "Music player notification";
		String description = "The channel for music player notifications";
		int importance = NotificationManager.IMPORTANCE_DEFAULT;
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
			NotificationChannel channel = null;
			channel = new NotificationChannel(CHANNEL_ID, name, importance);
			channel.setDescription(description);
			// Register the channel with the system; you can't change the importance
			// or other notification behaviors after this
			NotificationManager notificationManager = getSystemService(NotificationManager.class);
			notificationManager.createNotificationChannel(channel);
		}
	}
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startid) {

		if (null != mediaPlayer) {
			// ID for this start command
			mStartID = startid;

			if (mediaPlayer.isPlaying()) {
				// Rewind to beginning of song
				mediaPlayer.seekTo(0);
			} else {
				// Start playing song
				mediaPlayer.start();
			}
		}

		// Don't automatically restart this Service if it is killed
		return START_NOT_STICKY;
	}

	@Override
	public void onDestroy() {

		if (null != mediaPlayer) {

			mediaPlayer.stop();
			mediaPlayer.release();

		}
	}

	// Can't bind to this Service
	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}
}
