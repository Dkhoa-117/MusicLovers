package com.example.musiclovers.Fragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.musiclovers.MainActivity;
import com.example.musiclovers.R;

import com.example.musiclovers.Services.PlaceHolder;
import com.gauravk.audiovisualizer.visualizer.BarVisualizer;

import java.util.ArrayList;
import java.util.Collections;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
public class LyricsFragment extends Fragment implements MainActivity.UpdateFragmentLyrics{
    RecyclerView lyricRecyclerView;
    public LyricsAdapter lyricsAdapter;
    ArrayList<String> lines = new ArrayList<>();
    BarVisualizer visualizer;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_lyrics, container, false);
        lyricRecyclerView = v.findViewById(R.id.lyricRecyclerView);
        visualizer = v.findViewById(R.id.bar);
        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        lyricsAdapter = new LyricsAdapter(lines, getContext());
        lyricRecyclerView.setHasFixedSize(true);
        lyricRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        lyricRecyclerView.setAdapter(lyricsAdapter);
    }

    @Override
    public void getData(String songId, PlaceHolder placeHolder, MediaPlayer mediaPlayer) {
        Call<Lyrics> call = placeHolder.getLyrics(songId);
        call.enqueue(new Callback<Lyrics>() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onResponse(@NonNull Call<Lyrics> call, @NonNull Response<Lyrics> response) {
                if(response.isSuccessful()){
                    String[] temp = ((Lyrics) response.body()).lyrics.split("\n");
                    lines.clear();
                    Collections.addAll(lines, temp);
                }else{
                    lines.clear();
                    lines.add("No Lyrics Available");
                }
                lyricsAdapter.notifyDataSetChanged();
            }
            @Override
            public void onFailure(Call<Lyrics> call, Throwable t) {
                Toast.makeText(LyricsFragment.this.getContext(), "Something went wrong!", Toast.LENGTH_LONG).show();
            }
        });
        int audioSessionId = mediaPlayer.getAudioSessionId();
        if (audioSessionId != -1){
            visualizer.setAudioSessionId(audioSessionId);
        }
    }

    public static class Lyrics{
        String lyrics;
    }

    public static class LyricsAdapter extends RecyclerView.Adapter<LyricsAdapter.ViewHolder> {

        ArrayList<String> lines;
        Context context;

        public LyricsAdapter(ArrayList<String> lines, Context context) {
            this.context = context;
            this.lines = lines;
        }

        @NonNull
        @Override
        public LyricsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.lyrics_line, parent, false);
            LyricsAdapter.ViewHolder holder = new LyricsAdapter.ViewHolder(view);
            return holder;
        }

        @Override
        public void onBindViewHolder(@NonNull LyricsAdapter.ViewHolder holder, int position) {
            String line = lines.get(position);
            holder.lyrics.setText(line);
        }

        @Override
        public int getItemCount() {
            return lines.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            TextView lyrics;

            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                lyrics = itemView.findViewById(R.id.lyrics_line);
            }
        }
    }
}
