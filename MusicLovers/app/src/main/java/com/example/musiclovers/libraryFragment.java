package com.example.musiclovers;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import java.util.ArrayList;

public class libraryFragment extends Fragment {
    private RecyclerView mRecyclerView;
    private songsListAdapter mAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_library, container, false);
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mRecyclerView = (RecyclerView)view.findViewById(R.id.library_recycle_view);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        ArrayList<songItem> songItems = new ArrayList<>();

        //ADD DATA FOR TESTING
        songItems.add(new songItem("I'll Keep You Safe", "sagun", R.drawable.background));
        songItems.add(new songItem("Puple Beginning", "Lo-Fi Beats", R.drawable.background));
        songItems.add(new songItem("Cozy Lofi", "Lo-Fi Beats", R.drawable.background));
        songItems.add(new songItem("Ngả Nghiêng", "Minh Đinh", R.drawable.background));
        songItems.add(new songItem("White Ferrari", "Frank Ocean", R.drawable.background));
        songItems.add(new songItem("Trốn Tìm (feat. MTV band)", "Đen", R.drawable.background));
        songItems.add(new songItem("City of Stars", "Ryan Gosling", R.drawable.background));
        songItems.add(new songItem("Seasons In The Sun", "Westlife", R.drawable.background));

        mAdapter = new songsListAdapter(songItems, getContext());
        mRecyclerView.setAdapter(mAdapter);
    }
}
