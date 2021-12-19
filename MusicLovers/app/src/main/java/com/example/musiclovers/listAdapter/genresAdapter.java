package com.example.musiclovers.listAdapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.example.musiclovers.DownloadImageTask;
import com.example.musiclovers.MainActivity;
import com.example.musiclovers.R;
import com.example.musiclovers.ViewModel;
import com.example.musiclovers.fragments.artistDetailFragment;
import com.example.musiclovers.fragments.genreDetailFragment;
import com.example.musiclovers.models.artistItem;
import com.example.musiclovers.models.genreItem;
import com.example.musiclovers.models.playlistItem;

import java.util.ArrayList;

public class genresAdapter extends RecyclerView.Adapter<genresAdapter.ViewHolder>{
    private Context context;
    int layoutHolder, name;
    ArrayList<genreItem> genreItems;

    private ViewModel viewModel;

    public genresAdapter(int layoutHolder, int name, ArrayList<genreItem> genreItems, Context context) {
        this.context = context;
        this.name = name;
        this.genreItems = genreItems;
        this.layoutHolder = layoutHolder;
    }

    @NonNull
    @Override
    public genresAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(layoutHolder, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull genresAdapter.ViewHolder holder, int position) {
        genreItem currentGenre = genreItems.get(position);
        holder.tvName.setText(currentGenre.getGenreType());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewModel = new ViewModelProvider((MainActivity) context).get(ViewModel.class);
                viewModel.select(currentGenre);
                ((FragmentActivity) context).getSupportFragmentManager().beginTransaction()
                        .add(R.id.fragment_container, new genreDetailFragment())
                        .addToBackStack(null)
                        .setReorderingAllowed(true)
                        .commit();
            }
        });
    }

    @Override
    public int getItemCount() {
        return genreItems.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvName;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(name);
        }
    }
}
