package course.examples.Services.KeyClient;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

import course.examples.Services.KeyCommon.KeyGenerator;
/**
 * Author: John Mistica
 * Date: 4/28/2021
 * Assignment: Project 5
 * Reference: Built on top of ServiceWithIPCExampleClient app from CS 478 Spring 2021 Blackboard
 * Description: Client binds and unbinds to service and retrieves data from server class
 * Able to pick a song once client is binded and data relating to song is displayed
 * Also utilizes MusicService to play the selected song in the foreground
 */
public class KeyServiceUser extends AppCompatActivity {

	protected static final String TAG = "KeyServiceUser";
	private KeyGenerator mKeyGeneratorService;
	private boolean mIsBound = false;
	private Spinner spinner;
	public static ProgressBar mProgressBar;

	public static List<String> songsList;
	public static List<String> artistList;
	public static List<String> urlList;
	public static List<Bitmap> imageList = new ArrayList<Bitmap>(0);


	@Override
	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);
		setContentView(R.layout.main);

		getSupportActionBar().setTitle("Music Client");

		//widgets
		final TextView songName = (TextView) findViewById(R.id.songName);
		final TextView artistName = (TextView) findViewById(R.id.artistName);
		final TextView bindResult = (TextView) findViewById(R.id.textView);
		final Button goButton = (Button) findViewById(R.id.go_button);
		final Button listButton = (Button) findViewById(R.id.list_button);
		final Button dataButton = (Button) findViewById(R.id.data_button);
		final ImageView image = (ImageView) findViewById(R.id.imageView);
		mProgressBar = findViewById(R.id.progressBar);

		//Intent to start musicService
		final Intent musicServiceIntent = new Intent(this, MusicService.class);

		spinner = (Spinner) findViewById(R.id.spinner);

		//when bind button is clicked
		goButton.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				if (!mIsBound) {

					boolean b = false;
					Intent i = new Intent(KeyGenerator.class.getName());
					ResolveInfo info = getPackageManager().resolveService(i, 0);
					i.setComponent(new ComponentName(info.serviceInfo.packageName, info.serviceInfo.name));

					b = bindService(i, mConnection, Context.BIND_AUTO_CREATE);
					if (b) {
						Log.i(TAG, "bindService() succeeded!");
					} else {
						Log.i(TAG, "bindService() failed!");
					}

					//enables button to be pressed once successfully bound
					dataButton.setEnabled(true);
					listButton.setEnabled(true);

					//updates text to reflect bound status
					goButton.setText("Unbind Service");
					bindResult.setText("Status: Binded");
					bindResult.setTextColor(0xFF4CAF50);


				}
				else{
					//unbinds connection
					mIsBound = false;
					unbindService(mConnection);

					//disables buttons and spinner
					spinner.setVisibility(View.GONE);
					dataButton.setEnabled(false);
					listButton.setEnabled(false);

					//updates text and images when service is unbound
					goButton.setText("Bind Service");
					bindResult.setText("Status: Unbinded");
					bindResult.setTextColor(0xFFFF0000);
					songName.setText("");
					artistName.setText("");
					image.setImageBitmap(null);

					//stops service
					stopService(musicServiceIntent);
				}

			}
		});

		//when retrieveData button is clicked
		dataButton.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {

				try {
					if (mIsBound) {

						//sets up spinner and populates with songs retrieved from server
						spinner.setVisibility(View.VISIBLE);
						populateSpinner(mKeyGeneratorService.getSong());

						//populates song data retrieved from server data
						songsList = mKeyGeneratorService.getSong();
						artistList =  mKeyGeneratorService.getArtist();
						urlList = mKeyGeneratorService.getURL();

						//populates image list
						for(int i = 0; i < songsList.size(); i++){
							imageList.add(mKeyGeneratorService.getImage(i));
						}

						//clickable for when spinner item is selected
						spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
						{
							public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
							{
								stopService(musicServiceIntent);
								songName.setText(songsList.get(position));
								artistName.setText(artistList.get(position));
								image.setImageBitmap(imageList.get(position));
								MusicService.url = urlList.get(position);
								MusicService.startByList = false;
								startService(musicServiceIntent);

							} // to close the onItemSelected
							public void onNothingSelected(AdapterView<?> parent)
							{

							}
						});

					} else {
						Log.i(TAG, "Service was not bound!");
					}
				} catch (RemoteException e) {
					Log.e(TAG, e.toString());
				}

			}
		});

		//when listSongs button is clicked
		listButton.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				//starts new recycleView activity to show songs in list
				openActivity();
			}
		});
	}

	//opens new recycleView activity
	private void openActivity(){
		Intent myIntent = new Intent(this, MainActivity.class);
		startActivity(myIntent);
	}

	//populates spinner with array items
	private void populateSpinner(List<String> spinnerArray){

		ArrayAdapter<String> adapter = new ArrayAdapter<String>(
				this, android.R.layout.simple_spinner_item, spinnerArray);

		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner.setAdapter(adapter);
	}

	//connection to service
	private final ServiceConnection mConnection = new ServiceConnection() {
		public void onServiceConnected(ComponentName className, IBinder iservice) {
			mKeyGeneratorService = KeyGenerator.Stub.asInterface(iservice);
			mIsBound = true;
		}

		public void onServiceDisconnected(ComponentName className) {
			mKeyGeneratorService = null;
			mIsBound = false;
		}
	};


	// Unbind from KeyGenerator Service
	@Override
	protected void onStop() {

		super.onStop();

//		if (mIsBound) {
//			mIsBound = false;
//			unbindService(this.mConnection);
//		}
	}



	@Override
	public void onResume() {
		super.onResume();
	}

	@Override
	public void onPause() {
		super.onPause();
	}
}
