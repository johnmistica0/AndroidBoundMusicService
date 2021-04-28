/**
 * Author: John Mistica - Template gathered from blackboard
 * Description: MyAdapter class defines adapter for recycle view to create a dynamic list which displays widgets and configures menu options
 */

package course.examples.Services.KeyClient;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

/**
 * Author: John Mistica
 * Date: 4/28/2021
 * Assignment: Project 5
 * Description: The adapter for the recycle view which is responsible for
 * assigning the images, titles, and song URLS
 */

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {

    private List<String> songList; //data: the name of songs
    private List<String> artistList; //data: the names of artists
    private List<Bitmap> albumIds; //data: the images of each album
    private RVClickListener RVlistener;


    //constructor that passes in data from main activity
    public MyAdapter(List<String> theList, List<String> theList2, List<Bitmap> ids, RVClickListener listener){
        songList = theList;
        artistList = theList2;
        albumIds = ids;
        this.RVlistener = listener;

    }

    //inflates viewHolders and returns it
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View listView = inflater.inflate(R.layout.rv_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(listView, RVlistener);

        return viewHolder;
    }

    //binds text and images to each ViewHolder
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.songName.setText(songList.get(position));
        holder.artistName.setText(artistList.get(position));
        holder.image.setImageBitmap(albumIds.get(position));

    }

    @Override
    public int getItemCount() {
        return songList.size();
    }


    //creates wrapper object around a view which contains layout for each item in the list
    //adds clickable functionality onto each ViewHolder
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        //widget variable instantiation
        public TextView songName;
        public TextView artistName;
        public ImageView image;
        private RVClickListener listener;
        private View itemView;


        public ViewHolder(View itemView, course.examples.Services.KeyClient.RVClickListener passedListener) {
            super(itemView);

            //sets variables to widgets
            songName = (TextView) itemView.findViewById(R.id.textView);
            artistName = (TextView) itemView.findViewById(R.id.textView2);
            image = (ImageView) itemView.findViewById(R.id.imageView);

            this.itemView = itemView;
            this.listener = passedListener;

            //short click listener
            itemView.setOnClickListener(this);
        }

        //Short click on each song leads to music video link
        @Override
        public void onClick(View v) {
            listener.onClick(v, getAdapterPosition());


        }




    }
}
