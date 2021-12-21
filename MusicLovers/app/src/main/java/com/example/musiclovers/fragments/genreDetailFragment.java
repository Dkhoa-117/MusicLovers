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
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.musiclovers.PlaceHolder;
import com.example.musiclovers.R;
import com.example.musiclovers.ViewModel;
import com.example.musiclovers.listAdapter.songsListAdapter;
import com.example.musiclovers.models.songItem;
import com.example.musiclovers.signIn_signUpActivity.SaveSharedPreference;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class genreDetailFragment extends Fragment {
    PlaceHolder placeHolder;
    ViewModel viewModel;
    ArrayList<songItem> songs;
    RecyclerView mRecyclerView;
    TextView genreType;
    songsListAdapter mAdapter;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TransitionInflater inflater = TransitionInflater.from(requireContext());
        setEnterTransition(inflater.inflateTransition(R.transition.fade));
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_all_songs, container, false);
        mRecyclerView = v.findViewById(R.id.fragment_all_songs_RecyclerView);
        genreType = v.findViewById(R.id.tv_AllSongs);
        viewModel = new ViewModelProvider(getActivity()).get(ViewModel.class);
        return v;
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
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        viewModel.getSelectedGenre().observe(getViewLifecycleOwner(), genreItem -> {
            genreType.setText(genreItem.getGenreType());
            Call<List<songItem>> call = placeHolder.getUserGenres(
                    genreItem.get_id(),
                    SaveSharedPreference.getId(getContext())
            );

            call.enqueue(new Callback<List<songItem>>() {
                @Override
                public void onResponse(Call<List<songItem>> call, Response<List<songItem>> response) {
                    if(response.isSuccessful()){
                        songs = (ArrayList<songItem>) response.body();
                        mAdapter = new songsListAdapter(
                                R.layout.song_format,
                                R.id.song_format_SongName,
                                R.id.song_format_ArtistName,
                                R.id.song_format_SongImg,
                                songs,
                                3,
                                getContext());
                        mRecyclerView.setAdapter(mAdapter);
                    }else{
                        Toast.makeText(getContext(), "error", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<List<songItem>> call, Throwable t) {
                    Toast.makeText(getContext(), "error", Toast.LENGTH_SHORT).show();

                }
            });
        });
    }
}
