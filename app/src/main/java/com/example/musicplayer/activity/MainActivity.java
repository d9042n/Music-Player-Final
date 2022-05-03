package com.example.musicplayer.activity;

import android.Manifest;
import android.content.ContentUris;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.viewpager2.widget.ViewPager2;

import com.example.musicplayer.R;
import com.example.musicplayer.adapter.ViewPagerAdapter;
import com.example.musicplayer.model.Song;
import com.example.musicplayer.transformer.ZoomOutPageTransformer;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    public static ArrayList<Song> localSongs;

    private static final int PERMISSION_REQUEST_CODE = 10;

    private BottomNavigationView bottomNavigationView;
    private ViewPager2 viewPager2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        checkWriteExternalStoragePermission();

        initBottomNavigation();

        initViewPager2();

        localSongs = getAllLocalSongs(this);

    }


    // Check WRITE_EXTERNAL_STORAGE_PERMISSION
    public void checkWriteExternalStoragePermission() {
        if (ContextCompat.checkSelfPermission(
                getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) ==
                PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(this, "Permission Granted On Check Function!", Toast.LENGTH_SHORT).show();
        } else {
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);
        }
    }


    // Override onRequestPermissionsResult
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Permission Granted!", Toast.LENGTH_SHORT).show();
            } else {
                ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);
            }
        }
    }


    // Create Bottom Navigation
    public void initBottomNavigation() {
        bottomNavigationView = findViewById(R.id.bottom_navigation);

        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.menu_songs:
                        viewPager2.setCurrentItem(1);
                        break;
                    case R.id.menu_favorites:
                        viewPager2.setCurrentItem(2);
                        break;
                    case R.id.menu_search:
                        viewPager2.setCurrentItem(3);
                        break;
                    default:
                        viewPager2.setCurrentItem(0);
                        break;
                }
                return true;
            }
        });
    }


    // Create View Pager 2
    public void initViewPager2() {
        viewPager2 = findViewById(R.id.view_pager2);
        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(this);

        viewPager2.setPageTransformer(new ZoomOutPageTransformer());
        viewPager2.setAdapter(viewPagerAdapter);
        viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                switch (position) {
                    case 0:
                        bottomNavigationView.getMenu().findItem(R.id.menu_home).setChecked(true);
                        break;
                    case 1:
                        bottomNavigationView.getMenu().findItem(R.id.menu_songs).setChecked(true);
                        break;
                    case 2:
                        bottomNavigationView.getMenu().findItem(R.id.menu_favorites).setChecked(true);
                        break;
                    case 3:
                        bottomNavigationView.getMenu().findItem(R.id.menu_search).setChecked(true);
                        break;
                }
            }
        });
    }


    // Get All Local Songs
    public static ArrayList<Song> getAllLocalSongs(Context context) {
        ArrayList<Song> localSongsList = new ArrayList<>();

        Uri collection;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            collection = MediaStore.Audio.Media.getContentUri(MediaStore.VOLUME_EXTERNAL);
        } else {
            collection = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        }

        String[] projection = {
                MediaStore.Audio.Media.TITLE,
                MediaStore.Audio.Media.ARTIST,
                MediaStore.Audio.Media.ALBUM,
                MediaStore.Audio.Media.DURATION,
                MediaStore.Audio.Media.DATA,
        };

        String selection = MediaStore.Audio.Media.IS_MUSIC + "!= 0";

        String sortOrder = MediaStore.Audio.Media.TITLE + " ASC";

        Cursor cursor = context.getContentResolver().query(collection, projection, selection, null, sortOrder);
        if (cursor != null) {
            int titleID = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.TITLE);
            int artistID = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST);
            int albumID = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM);
            int durationID = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DURATION);
            int pathID = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA);

            while (cursor.moveToNext()) {
                String title = cursor.getString(titleID);
                String artist = cursor.getString(artistID);
                String album = cursor.getString(albumID);
                String duration = cursor.getString(durationID);
                String path = cursor.getString(pathID);

//                Uri path = ContentUris.withAppendedId(
//                        MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, pathID);

                Song song = new Song(title, artist, album, duration, path, null);
                localSongsList.add(song);
            }
            cursor.close();
        }
        return localSongsList;
    }
}