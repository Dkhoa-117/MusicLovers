package com.example.musiclovers;

import com.example.musiclovers.models.albumItem;
import com.example.musiclovers.models.playlistItem;
import com.example.musiclovers.models.songItem;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.PATCH;
import retrofit2.http.Path;

public interface PlaceHolder {
    // 👇 GET 👇
    @GET("songs")
    Call<List<songItem>> getSongs();

    @GET("songs/{songId}")
    Call<songItem> getSong (@Path("songId") String songId);

    @GET("albums")
    Call<List<albumItem>> getAlbums();

    @GET("albums/{albumId}")
    Call<albumItem> getAlbum (@Path("albumId") String albumId);

    @GET("songs/album/{albumId}")
    Call<List<songItem>> getSongsByAlbum (@Path("albumId") String albumId);

    @GET("playlists")
    Call<List<playlistItem>> getPlaylists();

    @GET("playlists/{playlistId}")
    Call<playlistItem> getPlaylist (@Path("playlistId") String playlistId);

    @GET("songs/playlist/{playlistId}")
    Call<List<songItem>> getSongsByPlaylist (@Path("playlistId") String playlistId);

    @GET("playlists/user/{userId}")
    Call<List<playlistItem>> getPlaylistsByUser (@Path("userId") String userId);

    // 👇 PATCH 👇
    @PATCH("songs/{songId}/likes")
    Call<songItem> patchSong (@Path("songId") String songId, @Body songItem songItem);

}
