package com.example.musiclovers;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
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
import com.example.musiclovers.fragments.browseFragment;
import com.example.musiclovers.fragments.libraryFragment;
import com.example.musiclovers.fragments.listenNowFragment;
import com.example.musiclovers.fragments.searchFragment;
import com.example.musiclovers.models.albumItem;
import com.example.musiclovers.models.songItem;
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


public class MainActivity extends AppCompatActivity {
    //Khai báo
    String base_Url = "http://10.0.2.2:3000/";
    ArrayList<songItem> songList = new ArrayList<>();
    MediaPlayer mediaPlayer = new MediaPlayer();
    Handler handler = new Handler();
    PlaceHolder placeHolder;
    int position = 0;
    boolean nextSong = false;
    BottomNavigationView bottomNavigationView;
    BottomSheetBehavior bottomSheetBehavior;
    LinearLayout tab_song_container;

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
        tabSong_playMusicFragment();
        if (savedInstanceState == null)
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new listenNowFragment()).commit();
        NavigationBar();
        handlerBtnOption();

        btnPause_StartHandler();
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
        bottomNavigationView = findViewById(R.id.bottomNavigationView);
    }

    private void handlerBtnOption() {
        optionButton.setOnClickListener(view -> {
            PopupMenu popup = new PopupMenu(getApplicationContext(), view);
            popup.inflate(R.menu.play_music_option_menu);
            popup.show();
            popup.setOnMenuItemClickListener(menuItem -> {
                switch (menuItem.getItemId()) {
                    case R.id.add_to_playlist:
                        //them vao playlist
                        break;
                    case R.id.go_to_artist:
                        //di toi nghe si
                        break;
                    case R.id.show_album:
                        goToAlbumDetail(songList.get(position));
                        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                        break;
                    case R.id.like:
                        //like bai hat va them vao playlist ua thich
                        loveMeOrNot(songList.get(position), true);
                        break;
                }
                return true;
            });
        });
    }

    private void tabSong_playMusicFragment() {
        //Hiện play_music
        tab_song_container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
            }
        });
        bottomSheetBehavior = BottomSheetBehavior.from(play_music_layout);
        //ẩn hiện thanh song_tab
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

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(base_Url)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        placeHolder = retrofit.create(PlaceHolder.class);
        Call<albumItem> call = placeHolder.getAlbum(songItem.getAlbumId());
        call.enqueue(new Callback<albumItem>() {
            @Override
            public void onResponse(Call<albumItem> call, Response<albumItem> response) {
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
            public void onFailure(Call<albumItem> call, Throwable t) {
                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    public void getSongs(ArrayList<songItem> songItems, int current) {

        //Đặt một thông báo hỏi user có muốn xoá hàng đợi hay không (trong trường hợp hàng đợi có sẵn)
        //if(true)
        songList.clear();
        for(int i = current; i < songItems.size(); i++){
            songList.add(songItems.get(i));
        }
        prepareSongAndPlay();
    }

    private void loveMeOrNot(songItem song, boolean loved) {

        int ic_image;
        int likeCount = song.getLikeCount();
        String message;
        if (loved) {
            likeCount++;
            ic_image = R.drawable.ic_fill_heart;
            message = "Add to Favorite List";
        } else {
            likeCount--;
            ic_image = R.drawable.ic_unfill_heart;
            message = "Remove from Favorite List";
        }
        songItem songItem = new songItem(songList.get(position).get_id(), null, null, null, null, null, likeCount, null, null);
        Call<songItem> call = placeHolder.patchSong(songList.get(position).get_id(), songItem);
        call.enqueue(new Callback<songItem>() {
            @Override
            public void onResponse(Call<songItem> call, Response<songItem> response) {
                if (response.isSuccessful()) {
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
                } else {
                    Toast.makeText(getApplicationContext(), response.message(), Toast.LENGTH_SHORT).show();
                    return;
                }
            }

            @Override
            public void onFailure(Call<songItem> call, Throwable t) {
                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void NavigationBar() {
        //load Listen Now ngay khi mở
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
        btnPause_Start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mediaPlayer.isPlaying()) {
                    handler.removeCallbacks(update);
                    mediaPlayer.pause();
                    song_tab_btnPause_Start.setImageResource(R.drawable.ic_play_music);
                    btnPause_Start.setImageResource(R.drawable.ic_play_music);
                } else {
                    mediaPlayer.start();
                    song_tab_btnPause_Start.setImageResource(R.drawable.ic_pause);
                    btnPause_Start.setImageResource(R.drawable.ic_pause);
                    updateSongSeekBar();
                }
            }
        });
        song_tab_btnPause_Start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mediaPlayer.isPlaying()) {
                    handler.removeCallbacks(update);
                    mediaPlayer.pause();
                    song_tab_btnPause_Start.setImageResource(R.drawable.ic_play_music);
                    btnPause_Start.setImageResource(R.drawable.ic_play_music);
                } else {
                    mediaPlayer.start();
                    song_tab_btnPause_Start.setImageResource(R.drawable.ic_pause);
                    btnPause_Start.setImageResource(R.drawable.ic_pause);
                    updateSongSeekBar();
                }
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
        mediaPlayer.stop();
        mediaPlayer.reset();

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
        handler.postDelayed(playingOrder, 1000);
        song_tab_btnPause_Start.setImageResource(R.drawable.ic_pause);
        btnPause_Start.setImageResource(R.drawable.ic_pause);
    }

    private void updateSongSeekBar() {
        if (mediaPlayer.isPlaying()) {
            songSeekBar.setProgress(mediaPlayer.getCurrentPosition());
            handler.postDelayed(update,300);
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
                        try { mediaPlayer.prepare(); } catch (IOException e) { e.printStackTrace(); }
                    }
                    mediaPlayer.reset();
                    prepareSongAndPlay();
                    btnPause_Start.performClick();
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
}
