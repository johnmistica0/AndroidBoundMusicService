/**
 * Author: John Mistica - Code template gathered from Blackboard
 * NetID: jmisti2
 * UIN: 660678902
 * Date: 2/23/21
 * Assignment: Project #2
 * Description: An application that displays the song name, album cover, and artist name.
 * There is an option menu to select to view songs in either a vertical list or a grid with two columns.
 * Each song can be short-clicked which will navigate the user to the songs music video.
 * A long-click will prompt the user with a context menu with three options which will lead
 * the user to either a music video, song titles wiki, and artist wiki respectively
 */

package course.examples.Services.KeyClient;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import java.util.List;

/**
 * Author: John Mistica
 * Date: 4/28/2021
 * Assignment: Project 5
 * Description: Second activity that displays recycle view with a list of songs
 * When a song is clicked it plays the song using the MusicService
 * Code reused from project 2
 */

public class MainActivity extends AppCompatActivity {

    //instantiating data structures that hold links, songs, and images
    List<String> songList;
    List<String> artistList;
    List<Bitmap> albumIds;
    List<String> urlList;
    RecyclerView songView;
    public static ProgressBar mProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        getSupportActionBar().setTitle("Music List");

        mProgressBar = (ProgressBar) findViewById(R.id.progressBar3);

        songView = (RecyclerView) findViewById(R.id.recycler_view);
        final Intent musicServiceIntent = new Intent(this, MusicService.class);
        songList = KeyServiceUser.songsList;
        artistList = KeyServiceUser.artistList;
        albumIds = KeyServiceUser.imageList;
        urlList = KeyServiceUser.urlList;

        //defines listener with lambda and displays song title on a toast
        RVClickListener listener = new RVClickListener() {
            @Override
            public void onClick(View view, int position) {
                //starts music service and plays song
                stopService(musicServiceIntent);
                MusicService.url = urlList.get(position);
                Log.i("target", ""+position);
                MusicService.startByList = true;
                startService(musicServiceIntent);
            }
        };

        //instantiates and sets new dynamic adapter class and passes in data
        MyAdapter adapter = new MyAdapter(songList, artistList, albumIds, listener);
        songView.setHasFixedSize(true);
        songView.setAdapter(adapter);

        songView.setLayoutManager(new LinearLayoutManager(this));
    }

}