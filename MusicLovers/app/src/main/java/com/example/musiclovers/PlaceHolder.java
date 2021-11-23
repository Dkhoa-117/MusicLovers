package com.example.musiclovers;

import android.os.Build;

import com.example.musiclovers.models.albumItem;
import com.example.musiclovers.models.artistItem;
import com.example.musiclovers.models.playlistItem;
import com.example.musiclovers.models.songItem;
import com.example.musiclovers.models.userItem;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

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

    @GET("albums/artist/{artistId}")
    Call<List<albumItem>> getAlbumsByArtist (@Path("artistId") String artistId);

    @GET("songs/album/{albumId}")
    Call<List<songItem>> getSongsByAlbum (@Path("albumId") String albumId);

    @GET("songs/artist/{artistId}")
    Call<List<songItem>> getSongsByArtist (@Path("artistId") String artistId);

    @GET("playlists/{playlistId}")
    Call<playlistItem> getPlaylist (@Path("playlistId") String playlistId);

    @GET("playlists/songs/{playlistId}")
    Call<List<songItem>> getSongsByPlaylist (@Path("playlistId") String playlistId);

    @GET("playlists/user/{userId}")
    Call<List<playlistItem>> getPlaylistsByUser (@Path("userId") String userId);

    @GET("playlists/{userId}/{playlist_number}")
    Call<playlistItem> getSpecificPlaylistByUser (@Path("userId") String userId, @Path("playlist_number") int playlist_number);

    @GET("artists")
    Call<List<artistItem>> getArtists();

    @GET("artists/{artistId}")
    Call<artistItem> getArtist(@Path("artistId") String artistId);

    @GET("songs/search")
    Call<List<songItem>> searchSongs (@Query("q") String q);

    // 👇 PATCH 👇

    // 👇 POST 👇
    @FormUrlEncoded
    @POST("users/")
    Call<userItem> register(
            @Field("userName") String userName,
            @Field("email") String email,
            @Field("password") String password
    );

    @FormUrlEncoded
    @POST("users/login")
    Call<userItem> login(
            @Field("email") String email,
            @Field("password") String password
    );

    @FormUrlEncoded
    @POST("playlists/{playlistId}/songs")
    Call<Void> addSongInPlaylist(
            @Path("playlistId") String playlistId,
            @Field("songId") String songId
    );

    @FormUrlEncoded
    @POST("songs/likes")
    Call<Void> likeSong(
            @Field("userId") String userId,
            @Field("songId") String songId
    );

    // 👇 DELETE 👇
    @DELETE("playlist/{playlistId}/songs/{songId}")
    Call<Void> removeSongInPlaylist(
            @Path("playlistId") String playlistId,
            @Path("songId") String songId
    );
}
