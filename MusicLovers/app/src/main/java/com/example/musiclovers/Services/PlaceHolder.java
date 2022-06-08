package com.example.musiclovers.Services;

import com.example.musiclovers.Fragments.LyricsFragment;
import com.example.musiclovers.Models.Album;
import com.example.musiclovers.Models.artistItem;
import com.example.musiclovers.Models.genreItem;
import com.example.musiclovers.Models.playlistItem;
import com.example.musiclovers.Models.songItem;
import com.example.musiclovers.Models.userItem;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;
/**
 * DONE
 */
public interface PlaceHolder {
    // 👇 GET 👇
    @GET("songs")
    Call<List<songItem>> getSongs();

    @GET("songs/{songId}")
    Call<songItem> getSong (@Path("songId") String songId);

    @GET("albums")
    Call<List<Album>> getAlbums();

    @GET("albums/{albumId}")
    Call<Album> getAlbum (@Path("albumId") String albumId);

    @GET("albums/artist/{artistId}")
    Call<List<Album>> getAlbumsByArtist (@Path("artistId") String artistId);

    @GET("songs/album/{albumId}")
    Call<List<songItem>> getSongsByAlbum (@Path("albumId") String albumId);

    @GET("songs/category/{category}")
    Call<List<songItem>> getSongsByCategory (@Path("category") String category); //new-music * best-new-songs *

    @GET("albums/category/{category}")
    Call<List<Album>> getAlbumsByCategory (@Path("category") String category); //new-albums * hot-albums *

    @GET("songs/artist/{artistId}")
    Call<List<songItem>> getSongsByArtist (@Path("artistId") String artistId);

    @GET("playlists/{playlistId}")
    Call<playlistItem> getPlaylist (@Path("playlistId") String playlistId);

    @GET("playlists/songs/{playlistId}")
    Call<List<songItem>> getSongsByPlaylist (@Path("playlistId") String playlistId);

    @GET("playlists/user/{userId}")
    Call<List<playlistItem>> getPlaylistsByUser (@Path("userId") String userId);

    @GET("playlists/{userId}/{playlist_number}")
    Call<List<playlistItem>> getPlaylistByUser_PlaylistNum(@Path("userId") String userId, @Path("playlist_number") int playlist_number);

    @GET("artists/user/{userId}")
    Call<List<artistItem>> getArtistsByUser(
            @Path("userId") String userId
    );

    @GET("artists/{userId}/{artistId}")
    Call<artistItem> getArtist(
            @Path("artistId") String artistId,
            @Path("userId") String userId
    );

    @GET("genres/{genreId}/{userId}")
    Call<List<songItem>> getUserGenres(
            @Path("genreId") String genreId,
            @Path("userId") String userId

    );

    @GET("genres")
    Call<List<genreItem>> getGenres();

    @GET("lyrics/{songId}")
    Call<LyricsFragment.LyricsResponse> getLyrics(@Path("songId") String songId);

    @GET("songs/search")
    Call<List<songItem>> searchSongs (@Query("q") String q);

    @GET("albums/search")
    Call<List<Album>> searchAlbums (@Query("q") String q);

    @GET("artists/search")
    Call<List<artistItem>> searchArtists (@Query("q") String q);

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
    @POST("playlists/")
    Call<playlistItem> createPlaylist(
            @Field("playlistName") String playlistName,
            @Field("userId") String userId
    );

    @FormUrlEncoded
    @POST("playlists/{playlistId}/songs")
    Call<Void> addSongToPlaylist(
            @Path("playlistId") String playlistId,
            @Field("songId") String songId
    );

    @FormUrlEncoded
    @POST("songs/likes")
    Call<Void> likeSong(
            @Field("userId") String userId,
            @Field("songId") String songId
    );

    @FormUrlEncoded
    @POST("artists/likes")
    Call<Void> likeArtist(
            @Field("userId") String userId,
            @Field("artistId") String artistId
    );

    @FormUrlEncoded
    @POST("songs/recent")
    Call<Void> addSong2RecentList(
            @Field("userId") String userId,
            @Field("songId") String songId
    );

    // 👇 DELETE 👇
    @DELETE("playlists/{playlistId}/songs/{songId}")
    Call<Void> removeSongInPlaylist(
            @Path("playlistId") String playlistId,
            @Path("songId") String songId
    );

    @DELETE("playlists/{playlistId}")
    Call<Void> deletePlaylist(
            @Path("playlistId") String playlistId
    );
}
