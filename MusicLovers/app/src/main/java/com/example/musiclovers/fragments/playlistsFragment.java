package com.example.musiclovers.fragments;

import android.os.Bundle;
import android.transition.TransitionInflater;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.musiclovers.R;
import com.example.musiclovers.listAdapter.playlistAdapter;
import com.example.musiclovers.models.playlistItem;

import java.util.ArrayList;

public class playlistsFragment extends Fragment {

    RecyclerView.LayoutManager playlistRecyclerViewLayoutManager;
    ArrayList<playlistItem> playlistItems = new ArrayList<>();
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TransitionInflater inflater = TransitionInflater.from(requireContext());
        setExitTransition(inflater.inflateTransition(R.transition.fade));
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_playlists, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        RecyclerView playlistRecyclerView = view.findViewById(R.id.fragment_playlists_RecyclerView);
        playlistRecyclerView.setHasFixedSize(true);
        playlistRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        playlistAdapter playlistAdapter = new playlistAdapter(
                R.layout.fragment_playlists,
                R.id.playlist_format_Name,
                R.id.playlist_format_NumSongs,
                R.id.playlist_format_Image,
                playlistItems,
                getContext()
        );
        playlistRecyclerView.setAdapter(playlistAdapter);

    }
}
