package com.example.musiclovers.Services;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.musiclovers.Models.Album;
import com.example.musiclovers.Models.artistItem;
import com.example.musiclovers.Models.genreItem;
import com.example.musiclovers.Models.playlistItem;

/**
 * DONE
 */
public class ViewModel extends androidx.lifecycle.ViewModel {
    // TODO: Implement the ViewModel -> communicate between fragments
    //ALBUM______________
    private final MutableLiveData<Album> selectedAlbum = new MutableLiveData<Album>();
    public void select(Album album) {
        selectedAlbum.setValue(album);
    }
    public LiveData<Album> getSelectedAlbum() {
        return selectedAlbum;
    }

    //PLAYLIST______________
    private final MutableLiveData<playlistItem> selectedPlaylist = new MutableLiveData<playlistItem>();
    public void select(playlistItem playlistItem) {
        selectedPlaylist.setValue(playlistItem);
    }
    public LiveData<playlistItem> getSelectedPlaylist() {
        return selectedPlaylist;
    }

    //ARTIST______________
    private final MutableLiveData<artistItem> selectedArtist = new MutableLiveData<artistItem>();
    public void select(artistItem artistItem) {
        selectedArtist.setValue(artistItem);
    }
    public LiveData<artistItem> getSelectedArtist() {
        return selectedArtist;
    }

    //GENRE______________
    private final MutableLiveData<genreItem> selectedGenre = new MutableLiveData<genreItem>();
    public void select(genreItem genreItem) {
        selectedGenre.setValue(genreItem);
    }
    public LiveData<genreItem> getSelectedGenre() {
        return selectedGenre;
    }
}