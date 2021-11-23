package com.example.musiclovers;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.musiclovers.fragments.albumDetailFragment;
import com.example.musiclovers.fragments.artistDetailFragment;
import com.example.musiclovers.fragments.browseFragment;
import com.example.musiclovers.fragments.libraryFragment;
import com.example.musiclovers.fragments.listenNowFragment;
import com.example.musiclovers.fragments.searchFragment;
import com.example.musiclovers.models.albumItem;
import com.example.musiclovers.models.artistItem;
import com.example.musiclovers.models.playlistItem;
import com.example.musiclovers.models.songItem;
import com.example.musiclovers.signIn_signUpActivity.SaveSharedPreference;
import com.example.musiclovers.signIn_signUpActivity.loginActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.bottomsheet.BottomSheetBehavior;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class MainActivity extends AppCompatActivity implements Playable{
    /* Declare */
    String base_Url = "http://10.0.2.2:3000/";
    ArrayList<songItem> songList = new ArrayList<>();
    MediaPlayer mediaPlayer;
    Handler handler = new Handler();
    PlaceHolder placeHolder;
    int position = 0;
    boolean nextSong = false;
    BottomNavigationView bottomNavigationView;
    BottomSheetBehavior bottomSheetBehavior;
    LinearLayout tab_song_container;
    NotificationManager manager;
    AudioManager audioManager;

    //fragment play_music
    ImageButton optionButton, btnPause_Start, btnNext, btnPrevious;
    LinearLayout play_music_layout;
    TextView play_music_SongName, play_music_SingerName, play_music_SongEnd, play_music_SongStart;
    SeekBar songSeekBar, volumeSeekBar;
    ImageView play_music_SongImg;

    //fragment song_tab
    TextView song_tab_SongName;
    ImageView song_tab_SongImg;
    ImageButton song_tab_btnPause_Start, song_tab_btnNext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById();
        initializeRetrofit();
        setUpVolumeSeekBar();
        tabSong_playMusicFragment();
        if (savedInstanceState == null)
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new listenNowFragment()).commit();
        NavigationBar();
        handlerBtnOption();
        createNotificationChannel();

        btnPause_StartHandler();
        btnNext_PreviousHandler();
        songSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {}

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                mediaPlayer.seekTo(seekBar.getProgress());
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        logout();
    }

    public void logout(){
        if(SaveSharedPreference.getUserName(this).isEmpty()) {
            Intent login = new Intent(this, loginActivity.class);
            startActivity(login);
            finish();
        }
    }

    @Override
    protected void onResume() {
        volumeSeekBar.setProgress(audioManager.getStreamVolume(AudioManager.STREAM_MUSIC));
        super.onResume();
    }

    private void findViewById() {
        play_music_layout = findViewById(R.id.play_music);
        play_music_SongName = findViewById(R.id.play_music_SongName);
        play_music_SingerName = findViewById(R.id.play_music_SingerName);
        play_music_SongImg = findViewById(R.id.play_music_SongImg);
        optionButton = findViewById(R.id.play_music_BtnOption);
        tab_song_container = findViewById(R.id.song_tab_container);
        song_tab_SongName = findViewById(R.id.song_tab_SongName);
        song_tab_SongImg = findViewById(R.id.song_tab_SongImg);
        song_tab_btnPause_Start = findViewById(R.id.img_btn_song_tab_Play);
        song_tab_btnNext = findViewById(R.id.img_btn_song_tab_Forward);
        play_music_SongEnd = findViewById(R.id.play_music_SongEnd);
        play_music_SongStart = findViewById(R.id.play_music_SongStart);
        songSeekBar = findViewById(R.id.play_music_SeekbarSong);
        btnPause_Start = findViewById(R.id.play_music_BtnPlay);
        btnNext = findViewById(R.id.play_music_BtnForward);
        btnPrevious = findViewById(R.id.play_music_BtnBackward);
        volumeSeekBar = findViewById(R.id.play_music_SeekbarVolume);
        bottomNavigationView = findViewById(R.id.bottomNavigationView);
    }

    private void initializeRetrofit(){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(base_Url)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        placeHolder = retrofit.create(PlaceHolder.class);
    }

    private void handlerBtnOption() {
        optionButton.setOnClickListener(view -> {
            PopupMenu popup = new PopupMenu(getApplicationContext(), view);
            popup.inflate(R.menu.play_music_option_menu);
            popup.show();
            popup.setOnMenuItemClickListener(menuItem -> {
                switch (menuItem.getItemId()) {
                    case R.id.add_to_playlist:
                        //add song to playlist
                        if(!songList.isEmpty()) {
                            addSongToPlaylist(songList.get(position));
                        }
                        break;
                    case R.id.go_to_artist:
                        if(!songList.isEmpty()){
                            goToArtistDetail(songList.get(position));
                            bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                        }
                        break;
                    case R.id.show_album:
                        if(!songList.isEmpty()){
                            goToAlbumDetail(songList.get(position));
                            bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                        }
                        break;
                    case R.id.like:
                        //like song and add song to playlist [favorite songs]
                        if(!songList.isEmpty()){
                            loveMeOrNot(songList.get(position));
                        }
                        break;
                }
                return true;
            });
        });
    }

    private void tabSong_playMusicFragment() {
        /* Show up play_music bottom sheet */
        tab_song_container.setOnClickListener(view -> bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED));
        bottomSheetBehavior = BottomSheetBehavior.from(play_music_layout);
        /* Show & Hide song_tab */
        bottomSheetBehavior.addBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {
                if (slideOffset > 0.15) {
                    tab_song_container.setVisibility(View.GONE);
                } else if (slideOffset <= 0.15) {
                    tab_song_container.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    public void goToAlbumDetail(songItem songItem) {
        Call<albumItem> call = placeHolder.getAlbum(songItem.getAlbumId());
        call.enqueue(new Callback<albumItem>() {
            @Override
            public void onResponse(@NonNull Call<albumItem> call, @NonNull Response<albumItem> response) {
                if (!response.isSuccessful()) {
                    Toast.makeText(getApplicationContext(), "code: " + response.code(), Toast.LENGTH_SHORT).show();
                    return;
                }
                albumItem album = response.body();
                ViewModel viewModel = new ViewModelProvider(MainActivity.this).get(ViewModel.class);
                viewModel.select(album);
                getSupportFragmentManager().beginTransaction()
                        .add(R.id.fragment_container, new albumDetailFragment())
                        .addToBackStack(null)
                        .setReorderingAllowed(true)
                        .commit();
            }

            @Override
            public void onFailure(@NonNull Call<albumItem> call, @NonNull Throwable t) {
                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    public void goToArtistDetail(songItem songItem){
        String[] artistId = songItem.getArtistId();
        Call<artistItem> call = placeHolder.getArtist(artistId[0]);
        call.enqueue(new Callback<artistItem>() {
            @Override
            public void onResponse(@NonNull Call<artistItem> call, @NonNull Response<artistItem> response) {
                if(response.isSuccessful()){
                    artistItem artist = response.body();
                    ViewModel viewModel = new ViewModelProvider(MainActivity.this).get(ViewModel.class);
                    viewModel.select(artist);
                    getSupportFragmentManager().beginTransaction()
                            .add(R.id.fragment_container, new artistDetailFragment())
                            .addToBackStack(null)
                            .setReorderingAllowed(true)
                            .commit();
                }
            }

            @Override
            public void onFailure(@NonNull Call<artistItem> call, @NonNull Throwable t) {
                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_LONG).show();

            }
        });
    }

    public void getSongs(ArrayList<songItem> songItems, int current) {

        //Đặt một thông báo hỏi user có muốn xoá hàng đợi hay không (trong trường hợp hàng đợi có sẵn)
        //if(true)
        songList.clear();
        position = 0;
        for(int i = current; i < songItems.size(); i++){
            songList.add(songItems.get(i));
        }
        prepareSongAndPlay();
    }

    private void loveMeOrNot(songItem song) {
        Call<Void> call = placeHolder.likeSong(SaveSharedPreference.getId(this), song.get_id());
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(@NonNull Call<Void> call, @NonNull Response<Void> response) {
                if(response.isSuccessful()){
                    boolean loved = response.code() == 201;
                    int ic_image;
                    String message;
                    if(loved){
                        ic_image = R.drawable.ic_fill_heart;
                        message = "Add to Favorite List";
                    }else{
                        ic_image = R.drawable.ic_unfill_heart;
                        message = "Remove from Favorite List";
                    }
                    LayoutInflater inflater = getLayoutInflater();
                    View layout = inflater.inflate(R.layout.toast_custom, findViewById(R.id.toast_layout_root));
                    ImageView image = (ImageView) layout.findViewById(R.id.toast_image);
                    image.setImageResource(ic_image);
                    TextView text = (TextView) layout.findViewById(R.id.toast_text);
                    text.setText(message);
                    Toast toast = new Toast(getApplicationContext());
                    toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
                    toast.setDuration(Toast.LENGTH_SHORT);
                    toast.setView(layout);
                    toast.show();
                }else{
                    Toast.makeText(getApplicationContext(), "code: " + response.code(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<Void> call, @NonNull Throwable t) {
                Toast.makeText(getApplicationContext(), "code: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void addSongToPlaylist(songItem song){
        //pop-up playlists: name, number of songs
        //change to pop-up: Are you sure? - Yes - Cancel
        //Call placeHolder addSongToPlaylist: playlistId and songId
    }

    private void NavigationBar() {
        //load Listen Now when open app
        bottomNavigationView.setOnItemSelectedListener(item -> {
            Fragment selectedFragment = null;
            switch (item.getItemId()) {
                case R.id.Listen_Now_page:
                    selectedFragment = new listenNowFragment();
                    break;
                case R.id.Browse_page:
                    selectedFragment = new browseFragment();
                    break;
                case R.id.Library_page:
                    selectedFragment = new libraryFragment();
                    break;
                case R.id.Search_page:
                    selectedFragment = new searchFragment();
                    break;
            }
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, selectedFragment).commit();
            return true;
        });
        bottomNavigationView.setOnItemReselectedListener(item -> {
            Fragment selectedFragment = null;
            switch (item.getItemId()) {
                case R.id.Listen_Now_page:
                    selectedFragment = new listenNowFragment();
                    break;
                case R.id.Browse_page:
                    selectedFragment = new browseFragment();
                    break;
                case R.id.Library_page:
                    selectedFragment = new libraryFragment();
                    break;
                case R.id.Search_page:
                    selectedFragment = new searchFragment();
                    break;
            }
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, selectedFragment).commit();
        });
    }

    private void btnPause_StartHandler() {
        btnPause_Start.setOnClickListener(v -> {
            if(mediaPlayer != null){
                if (mediaPlayer.isPlaying()) {
                    onSongPause();
                } else {
                    onSongPlay();
                }
            }
        });
        song_tab_btnPause_Start.setOnClickListener(view -> {
            if(mediaPlayer != null){
                if (mediaPlayer.isPlaying()) {
                    onSongPause();
                } else {
                    onSongPlay();
                }
            }
        });
    }

    private void btnNext_PreviousHandler() {
        btnNext.setOnClickListener(view -> {
            if(mediaPlayer != null){
                onSongNext();
            }
        });
        song_tab_btnNext.setOnClickListener(view -> {
            if(mediaPlayer!=null){
                onSongNext();
            }
        });
        btnPrevious.setOnClickListener(view -> {
            if(mediaPlayer!=null){
                onSongPrevious();
            }
        });
    }

    private void timerSetting() {
        play_music_SongEnd.setText(new SimpleDateFormat("mm:ss").format(mediaPlayer.getDuration()));
        songSeekBar.setMax(mediaPlayer.getDuration());
    }

    private void prepareSongAndPlay() {
        play_music_SingerName.setText(songList.get(position).getArtistName());
        song_tab_SongName.setText(songList.get(position).getSongName() + " - " + songList.get(position).getArtistName());
        song_tab_SongName.setSelected(true);
        play_music_SongName.setText(songList.get(position).getSongName());
        new DownloadImageTask(song_tab_SongImg).execute(base_Url + songList.get(position).getSongImg());
        new DownloadImageTask((play_music_SongImg)).execute(base_Url + songList.get(position).getSongImg());

        if(mediaPlayer == null) {
            mediaPlayer = new MediaPlayer();
        }else {
            mediaPlayer.stop();
            mediaPlayer.reset();
        }
        try {
            mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mediaPlayer) {
                    mediaPlayer.stop();
                    nextSong = true;
                }
            });
            mediaPlayer.setDataSource(base_Url + songList.get(position).getSongSrc());
            mediaPlayer.prepare();
            mediaPlayer.start();
        } catch (IOException e) {
            e.printStackTrace();
        }

        timerSetting();
        updateSongSeekBar();
        new createNotification(this,
                songList.get(position).getSongName(),
                songList.get(position).getArtistName(),
                R.drawable.ic_pause,
                position,
                songList.size())
                .execute(base_Url + songList.get(position).getSongImg());

        handler.postDelayed(playingOrder, 1000);
        song_tab_btnPause_Start.setImageResource(R.drawable.ic_pause);
        btnPause_Start.setImageResource(R.drawable.ic_pause);
    }

    private void songPlayback() {
        try { mediaPlayer.prepare(); } catch (IOException e) { e.printStackTrace(); }
        song_tab_btnPause_Start.setImageResource(R.drawable.ic_play_music);
        btnPause_Start.setImageResource(R.drawable.ic_play_music);
        handler.removeCallbacks(update);
        songSeekBar.setProgress(0);
        play_music_SongStart.setText(new SimpleDateFormat("mm:ss").format(0));
        new createNotification(this,
                songList.get(position).getSongName(),
                songList.get(position).getArtistName(),
                R.drawable.ic_play_music,
                position,
                songList.size())
                .execute(base_Url + songList.get(position).getSongImg());
    }

    private void updateSongSeekBar() {
        if (mediaPlayer.isPlaying()) {
            songSeekBar.setProgress(mediaPlayer.getCurrentPosition());
            handler.postDelayed(update,300);
        }
    }

    private void setUpVolumeSeekBar() {
        audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        int stream = AudioManager.STREAM_MUSIC;

        volumeSeekBar.setMax(audioManager.getStreamMaxVolume(stream));
        volumeSeekBar.setProgress(audioManager.getStreamVolume(stream));

        volumeSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            public void onProgressChanged(SeekBar bar, int progress, boolean fromUser) {
                audioManager.setStreamVolume(stream, progress, AudioManager.FLAG_PLAY_SOUND);
            }

            public void onStartTrackingTouch(SeekBar bar) {}

            public void onStopTrackingTouch(SeekBar bar) {}
        });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_VOLUME_UP)
        {
            int index = volumeSeekBar.getProgress();
            volumeSeekBar.setProgress(index + 1);
            return true;
        } else if (keyCode == KeyEvent.KEYCODE_VOLUME_DOWN)
        {
            int index = volumeSeekBar.getProgress();
            volumeSeekBar.setProgress(index - 1);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    private void createNotificationChannel() {
        registerReceiver(broadcastReceiver, new IntentFilter("TRACKS_TRACKS"));
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            NotificationChannel channel = new NotificationChannel(
                    createNotification.CHANNEL_ID_1,
                    createNotification.CHANNEL_NAME_1,
                    NotificationManager.IMPORTANCE_LOW
            );
            channel.setLockscreenVisibility(android.app.Notification.VISIBILITY_PRIVATE);
            channel.enableLights(true);
            manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);
        }
    }

    Runnable update = new Runnable() {
        @Override
        public void run() {
            play_music_SongStart.setText(new SimpleDateFormat("mm:ss").format(mediaPlayer.getCurrentPosition()));
            updateSongSeekBar();
        }
    };

    Runnable playingOrder = new Runnable() {
        @Override
        public void run() {
            if (nextSong) {
                position++;
                if (position == (songList.size())) {
                    position = 0;
                    //nếu mảng bài hát chỉ gồm 1 phần tử thì chỉ cần prepare không cần tải lại
                    if(songList.size() == 1){
                        songPlayback();
                    }else{
                        mediaPlayer.reset();
                        prepareSongAndPlay();
                        songSeekBar.setProgress(0);
                        onSongPause();
                    }
                } else {
                    mediaPlayer.reset();
                    prepareSongAndPlay();
                }
                nextSong = false;
            } else {
                handler.postDelayed(this, 1000);
            }
        }
    };

    BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getExtras().getString("actionname");

            switch (action){
                case createNotification.ACTION_PREVIOUS:
                    onSongPrevious();
                    break;
                case createNotification.ACTION_PLAY:
                    if (mediaPlayer.isPlaying()){
                        onSongPause();
                    } else {
                        onSongPlay();
                    }
                    break;
                case createNotification.ACTION_NEXT:
                    onSongNext();
                    break;
            }
        }
    };

    @Override
    public void onSongPrevious() {
        if(position == 0 || mediaPlayer.getCurrentPosition() > 5000) {
            mediaPlayer.stop();
            try { mediaPlayer.prepare(); } catch (IOException e) { e.printStackTrace(); }
            mediaPlayer.start();
        }
        else {
            position--;
            prepareSongAndPlay();
        }
    }

    @Override
    public void onSongPlay() {
        mediaPlayer.start();
        updateSongSeekBar();
        song_tab_btnPause_Start.setImageResource(R.drawable.ic_pause);
        btnPause_Start.setImageResource(R.drawable.ic_pause);
        new createNotification(MainActivity.this,
                songList.get(position).getSongName(),
                songList.get(position).getArtistName(),
                R.drawable.ic_pause, /* switch to pause */
                position,
                songList.size())
                .execute(base_Url + songList.get(position).getSongImg());
        updateSongSeekBar();
    }

    @Override
    public void onSongPause() {
        handler.removeCallbacks(update);
        mediaPlayer.pause();
        song_tab_btnPause_Start.setImageResource(R.drawable.ic_play_music);
        btnPause_Start.setImageResource(R.drawable.ic_play_music);
        new createNotification(MainActivity.this,
                songList.get(position).getSongName(),
                songList.get(position).getArtistName(),
                R.drawable.ic_play_music,
                position,
                songList.size())
                .execute(base_Url + songList.get(position).getSongImg());
    }

    @Override
    public void onSongNext() {
        if (position == songList.size() - 1) {
            position = 0;
            if(songList.size() == 1) {
                mediaPlayer.stop();
                songPlayback();
            }
            else {
                mediaPlayer.reset();
                prepareSongAndPlay();
                songSeekBar.setProgress(0);
                onSongPause();
            }
        }
        else {
            position++;
            prepareSongAndPlay();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            manager.cancelAll();
        }
        unregisterReceiver(broadcastReceiver);
    }
}
