package com.example.musiclovers.fragments;

import android.os.Bundle;
import android.transition.TransitionInflater;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.musiclovers.PlaceHolder;
import com.example.musiclovers.R;
import com.example.musiclovers.listAdapter.playlistAdapter;
import com.example.musiclovers.models.playlistItem;
import com.example.musiclovers.signIn_signUpActivity.SaveSharedPreference;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class playlistsFragment extends Fragment {

    ArrayList<playlistItem> playlistItems = new ArrayList<>();
    PlaceHolder placeHolder;
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

        String base_Url = "http://10.0.2.2:3000/";
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(base_Url)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        placeHolder = retrofit.create(PlaceHolder.class);
        Call<List<playlistItem>> call = placeHolder.getPlaylistsByUser(SaveSharedPreference.getId(getContext()));
        call.enqueue(new Callback<List<playlistItem>>() {
            @Override
            public void onResponse(Call<List<playlistItem>> call, Response<List<playlistItem>> response) {
                if(!response.isSuccessful()){
                    Toast.makeText(getContext(), "code: "+ response.code(), Toast.LENGTH_SHORT).show();
                    return;
                }
                playlistItems = (ArrayList<playlistItem>) response.body();
                RecyclerView playlistRecyclerView = view.findViewById(R.id.fragment_playlists_RecyclerView);
                playlistRecyclerView.setHasFixedSize(true);
                playlistRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                playlistAdapter playlistAdapter = new playlistAdapter(
                        R.layout.playlist_format,
                        R.id.playlist_format_Name,
                        R.id.playlist_format_NumSongs,
                        R.id.playlist_format_Image,
                        playlistItems,
                        getContext()
                );
                playlistRecyclerView.setAdapter(playlistAdapter);
            }

            @Override
            public void onFailure(Call<List<playlistItem>> call, Throwable t) {
                Toast.makeText(getContext(), "error", Toast.LENGTH_LONG);
            }
        });
    }
}
