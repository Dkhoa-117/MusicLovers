package com.example.musiclovers.models;

public class playlistItem {
    private String _id;
    private String playlistName;
    private String userId;
    private String playlistImg;
    private String[] songs;
    private int numSongs;

    public String get_id() {
        return _id;
    }

    public String getPlaylistName() {
        return playlistName;
    }

    public String getUserId() {
        return userId;
    }

    public String getPlaylistImg() {
        return playlistImg;
    }

    public String[] getSongs() {
        return songs;
    }

    public int getNumSongs() {
        return numSongs;
    }

    public playlistItem(String _id, String playlistName, String userId, String playlistImg, String[] songs, int numSongs) {
        this._id = _id;
        this.playlistName = playlistName;
        this.userId = userId;
        this.playlistImg = playlistImg;
        this.songs = songs;
        this.numSongs = numSongs;
    }
}
