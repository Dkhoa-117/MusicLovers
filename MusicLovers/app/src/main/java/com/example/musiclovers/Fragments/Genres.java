package com.example.musiclovers.Fragments;

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

import com.example.musiclovers.Services.PlaceHolder;
import com.example.musiclovers.R;
import com.example.musiclovers.ListAdapter.genresAdapter;
import com.example.musiclovers.Models.genreItem;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Genres extends Fragment {

    ArrayList<genreItem> genres = new ArrayList<>();
    PlaceHolder placeHolder;
    genresAdapter genreAdapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TransitionInflater inflater = TransitionInflater.from(requireContext());
        setEnterTransition(inflater.inflateTransition(R.transition.fade));
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_genres, container, false);
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
        Call<List<genreItem>> call = placeHolder.getGenres();
        call.enqueue(new Callback<List<genreItem>>() {
            @Override
            public void onResponse(Call<List<genreItem>> call, Response<List<genreItem>> response) {
                if(!response.isSuccessful()){
                    Toast.makeText(getContext(), "code: "+ response.code(), Toast.LENGTH_SHORT).show();
                    return;
                }
                genres = (ArrayList<genreItem>) response.body();
                RecyclerView genresRecyclerView = view.findViewById(R.id.fragment_genres_RecyclerView);
                genresRecyclerView.setHasFixedSize(true);
                genresRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                genreAdapter = new genresAdapter(
                        R.layout.genre_format,
                        R.id.genre_name,
                        genres,
                        getContext());
                genresRecyclerView.setAdapter(genreAdapter);
            }

            @Override
            public void onFailure(Call<List<genreItem>> call, Throwable t) {
                Toast.makeText(getContext(), "message: "+ t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
