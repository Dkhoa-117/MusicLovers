package com.example.musiclovers.fragments;

import android.os.Bundle;
import android.transition.TransitionInflater;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.musiclovers.PlaceHolder;
import com.example.musiclovers.R;
import com.example.musiclovers.listAdapter.songsListAdapter;
import com.example.musiclovers.models.songItem;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class searchFragment extends Fragment {
    SearchView searchView;
    RecyclerView recyclerView;
    ArrayList<songItem> songItems;
    songsListAdapter mAdapter;
    TextView textViewThumbnail, textViewSearchNotFound;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TransitionInflater inflater = TransitionInflater.from(requireContext());
        setEnterTransition(inflater.inflateTransition(R.transition.slide_right));

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v =  inflater.inflate(R.layout.fragment_search, container, false);
        searchView = v.findViewById(R.id.fragment_search_SearchView);
        recyclerView = v.findViewById(R.id.fragment_search_SearchRecycleView);
        textViewThumbnail = v.findViewById(R.id.search_thumbnail);
        textViewSearchNotFound = v.findViewById(R.id.tv_search_not_found);
        songItems = new ArrayList<>();
        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl("http://10.0.2.2:3000/")
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();
                PlaceHolder placeHolder = retrofit.create(PlaceHolder.class);
                Call<List<songItem>> call = placeHolder.searchSongs(query);
                call.enqueue(new Callback<List<songItem>>() {
                    @Override
                    public void onResponse(Call<List<songItem>> call, Response<List<songItem>> response) {
                        if(!response.isSuccessful()) {
                            Toast.makeText(getContext(), "code: " + response.code(), Toast.LENGTH_SHORT).show();
                            return;
                        }
                        songItems = (ArrayList<songItem>) response.body();
                        if(songItems.isEmpty()) {
                            textViewSearchNotFound.setVisibility(View.VISIBLE);
                        } else {
                            mAdapter = new songsListAdapter(
                                    R.layout.song_format,
                                    R.id.song_format_SongName,
                                    R.id.song_format_ArtistName,
                                    R.id.song_format_SongImg,
                                    songItems,
                                    getContext());
                            LinearLayoutManager manager = new LinearLayoutManager(getContext());
                            recyclerView.setHasFixedSize(true);
                            recyclerView.setLayoutManager(manager);
                            recyclerView.setAdapter(mAdapter);
                            textViewThumbnail.setVisibility(View.VISIBLE);
                        }
                    }

                    @Override
                    public void onFailure(Call<List<songItem>> call, Throwable t) {
                        Toast.makeText(getContext(), "error", Toast.LENGTH_LONG).show();
                    }
                });
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if(!songItems.isEmpty()){
                    songItems.clear();
                    textViewThumbnail.setVisibility(View.INVISIBLE);
                    mAdapter.notifyDataSetChanged();
                }else {
                    textViewSearchNotFound.setVisibility(View.GONE);
                }
                return false;
            }
        });
    }
}
