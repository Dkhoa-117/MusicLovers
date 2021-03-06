package com.example.musiclovers.Fragments.Artist;

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
import com.example.musiclovers.ListAdapter.ArtistsAdapter;
import com.example.musiclovers.Models.Artist;
import com.example.musiclovers.SignInSignUp.SaveSharedPreference;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * DONE
 */
public class Artists extends Fragment {

    ArrayList<Artist> artistItems = new ArrayList<>();
    PlaceHolder placeHolder;
    ArtistsAdapter artistAdapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TransitionInflater inflater = TransitionInflater.from(requireContext());
        setEnterTransition(inflater.inflateTransition(R.transition.fade));
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_artists, container, false);
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
        Call<List<Artist>> call = placeHolder.getArtistsByUser(SaveSharedPreference.getId(getContext()));
        call.enqueue(new Callback<List<Artist>>() {
            @Override
            public void onResponse(Call<List<Artist>> call, Response<List<Artist>> response) {
                if(!response.isSuccessful()){
                    Toast.makeText(getContext(), "code: "+ response.code(), Toast.LENGTH_SHORT).show();
                    return;
                }
                artistItems = (ArrayList<Artist>) response.body();
                RecyclerView artistRecyclerView = view.findViewById(R.id.fragment_artists_RecyclerView);
                artistRecyclerView.setHasFixedSize(true);
                artistRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                artistAdapter = new ArtistsAdapter(
                        R.layout.artist_format,
                        R.id.artist_format_ArtistName,
                        R.id.artist_format_ArtistImg,
                        artistItems,
                        getContext()
                );
                artistRecyclerView.setAdapter(artistAdapter);
            }

            @Override
            public void onFailure(Call<List<Artist>> call, Throwable t) {
                Toast.makeText(getContext(), "error", Toast.LENGTH_LONG);
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        if(artistAdapter != null)
            artistAdapter.notifyDataSetChanged();
    }
}
