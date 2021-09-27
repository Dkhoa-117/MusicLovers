package com.example.musiclovers;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class songsListAdapter extends RecyclerView.Adapter<songsListAdapter.ViewHolder>{

    private ArrayList<songItem> songItems;
    private Context context;
    public songsListAdapter(ArrayList<songItem> songItems, Context context) {
        this.songItems = songItems;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.song_format, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        songItem currentSong = songItems.get(position);

        holder.image.setImageResource(currentSong.getImage());
        holder.artistName.setText(currentSong.getArtistName());
        holder.songName.setText(currentSong.getSongName());
    }

    @Override
    public int getItemCount() {
        return songItems.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        ImageView image;
        TextView songName;
        TextView artistName;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            image = (ImageView)itemView.findViewById(R.id.iv_song_format_SongImg);
            songName = (TextView)itemView.findViewById(R.id.tv_song_format_SongName);
            artistName = (TextView)itemView.findViewById(R.id.tv_song_format_ArtistName);
        }
    }
}
