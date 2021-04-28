package course.examples.Services.KeyService;

import android.app.Service;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.IBinder;
import android.os.RemoteException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import course.examples.Services.KeyCommon.KeyGenerator;
/**
 * Author: John Mistica
 * Date: 4/28/2021
 * Assignment: Project 5
 * Reference: Built on top of ServiceWithIPCExampleService app from CS 478 Spring 2021 Blackboard
 * Description: This app acts as MusicCentral and stores all data relating to songs which can
 * be retrieved when a service is bound to it
 */
public class KeyGeneratorImpl extends Service {

	public List<String> songList;
	public List<String> artistList;
	public List<String> urlList;
	public List<Bitmap> imageList;

	@Override
	public void onCreate() {
		super.onCreate();

		songList = new ArrayList<String>(Arrays.asList("Sunny", "Energy", "Better Days", "Dubstep", "Once Again"));
		artistList = new ArrayList<String>(Arrays.asList("Bensound", "Bensound", "Bensound", "Bensound", "Bensound"));
		imageList= new ArrayList<Bitmap>(Arrays.asList(
				BitmapFactory.decodeResource(this.getResources(),R.drawable.image1),
				BitmapFactory.decodeResource(this.getResources(),R.drawable.image2),
				BitmapFactory.decodeResource(this.getResources(),R.drawable.image3),
				BitmapFactory.decodeResource(this.getResources(),R.drawable.image4),
				BitmapFactory.decodeResource(this.getResources(),R.drawable.image5)));
		urlList = new ArrayList<String>(Arrays.asList(
				"https://audio.jukehost.co.uk/46bUd8nHATCRQqREtF2VUELsoDDGq1xF",
				"https://audio.jukehost.co.uk/FVYFWYGYTTuPzdUrp9cbWYQhA9fRSlqf",
				"https://audio.jukehost.co.uk/5ElAmFk4tFaUTewRIIEgh1axjYocHBS0",
				"https://audio.jukehost.co.uk/ZEJXs7HO0MN0OOGXEi7i4lJrhkpVP4XU",
				"https://audio.jukehost.co.uk/fF6LXIWjdPjbiPn7U4BBL6EaOqQTlEIG"));


	}

	// Implement the Stub for this Object
	private final KeyGenerator.Stub mBinder = new KeyGenerator.Stub() {

		public List<String> getSong(){
			return songList;
		}
		public List<String> getArtist(){
			return artistList;
		}
		public List<String> getURL(){
			return urlList;
		}
		public Bitmap getImage(int i){
			return imageList.get(i);
		}

	};

	// Return the Stub defined above
	@Override
	public IBinder onBind(Intent intent) {
		return mBinder;
	}
}
