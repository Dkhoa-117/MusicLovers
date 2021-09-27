package com.example.musiclovers;

public class songItem {
    private String SongName;
    private String ArtistName;
    private int Image;

    public songItem(String songName, String artistName, int image) {
        this.SongName = songName;
        this.ArtistName = artistName;
        this.Image = image;
    }

    public String getSongName() {
        return SongName;
    }

    public String getArtistName() {
        return ArtistName;
    }

    public int getImage() {
        return Image;
    }
}