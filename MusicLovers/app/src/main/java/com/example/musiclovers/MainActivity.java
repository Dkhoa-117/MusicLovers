package com.example.musiclovers;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.menu.MenuBuilder;
import androidx.appcompat.view.menu.MenuPopupHelper;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.PopupMenu;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.bottomsheet.BottomSheetBehavior;

public class MainActivity extends AppCompatActivity {
    BottomNavigationView bottomNavigationView;
    private BottomSheetBehavior bottomSheetBehavior;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        LinearLayout play_music_layout = findViewById(R.id.play_music);
        //load Listen Now ngay khi mở
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new listenNowFragment()).commit();
        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomSheetBehavior = BottomSheetBehavior.from(play_music_layout);
        ImageButton optionButton = findViewById(R.id.img_btn_option);
        LinearLayout tab_song_container = findViewById(R.id.song_tab_container);
        //Hiện play_music
        tab_song_container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
            }
        });
        //ẩn/hiện thanh song_tab
        bottomSheetBehavior.addBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {}
            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {
                if(slideOffset > 0.15){
                    tab_song_container.setVisibility(View.GONE);
                }
                else if(slideOffset <= 0.15){
                    tab_song_container.setVisibility(View.VISIBLE);
                }
            }
        });

        //Chuyển thanh bottom navigation
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
            switch (item.getItemId()) {
                case R.id.Listen_Now_page:
                    //chuyển cảnh
                case R.id.Browse_page:
                    //chuyển cảnh
                case R.id.Library_page:
                    //chuyển cảnh
                case R.id.Search_page:
                    //chuyển cảnh
            }
        });
        //option button
        optionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu popup = new PopupMenu(MainActivity.this, view);
                popup.inflate(R.menu.play_music_option_menu);
                popup.show();
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        switch (menuItem.getItemId()) {
                            case R.id.add_to_playlist:
                                //them vao playlist
                                break;
                            case R.id.go_to_artist:
                                //di toi nghe si
                                break;
                            case R.id.show_album:
                                //mo album
                                break;
                            case R.id.like:
                                //like bai hat va them vao playlist ua thich
                                break;
                        }
                        return true;
                    }
                });
            }
        });
    }

}