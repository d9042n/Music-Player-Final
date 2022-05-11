package com.example.musicplayer.activity;

import android.Manifest;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.viewpager2.widget.ViewPager2;

import com.example.musicplayer.R;
import com.example.musicplayer.adapter.ViewPagerAdapter;
import com.example.musicplayer.fragment.SongsFragment;
import com.example.musicplayer.model.Song;
import com.example.musicplayer.transformer.ZoomOutPageTransformer;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements SearchView.OnQueryTextListener {
    public static ArrayList<Song> listAllSongs;
    public static ArrayList<Song> albums = new ArrayList<>();

    private static final int PERMISSION_REQUEST_CODE = 10;
    private String SORT_PREF = "SortOrder";

    private BottomNavigationView bottomNavigationView;
    private ViewPager2 viewPager2;

    static boolean shuffleBoolean = false, repeatBoolean = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        checkWriteExternalStoragePermission();

        initBottomNavigation();

        initViewPager2();

        listAllSongs = getAllLocalSongs(this);
        Song song = new Song(null, "HandClap", "Fitz and the Tantrums", "Unknown", "311000", null, "https://drive.google.com/u/6/uc?id=1_50m6RMn-KGK4X8CJsSt7hCVM-W8oJCw");
        listAllSongs.add(song);
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
                    case R.id.menu_album:
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
                        bottomNavigationView.getMenu().findItem(R.id.menu_album).setChecked(true);
                        break;
                    case 3:
                        bottomNavigationView.getMenu().findItem(R.id.menu_search).setChecked(true);
                        break;
                }
            }
        });
    }


    // Get All Local Songs
    public ArrayList<Song> getAllLocalSongs(Context context) {
        SharedPreferences preferences = getSharedPreferences(SORT_PREF, MODE_PRIVATE);
        String sortOrderOption = preferences.getString("sorting", "sortByName");
        ArrayList<String> duplicate = new ArrayList<>();
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
                MediaStore.Audio.Media._ID,
        };

        String selection = MediaStore.Audio.Media.IS_MUSIC + "!= 0";

//        String sortOrder = MediaStore.Audio.Media.TITLE + " ASC";
        String sortOrder = null;
        switch (sortOrderOption) {
            case "sortByName":
                sortOrder = MediaStore.Audio.Media.TITLE + " ASC";
                break;
            case "sortByDate":
                sortOrder = MediaStore.Audio.Media.DATE_ADDED + " ASC";
                break;
            case "sortBySize":
                sortOrder = MediaStore.Audio.Media.SIZE + " DESC";
                break;
        }

        albums.clear();

        Cursor cursor = context.getContentResolver().query(collection, projection, selection, null, sortOrder);
        if (cursor != null) {
            int titleID = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.TITLE);
            int artistID = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST);
            int albumID = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM);
            int durationID = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DURATION);
            int pathID = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA);
            int _id = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media._ID);

            while (cursor.moveToNext()) {
                String title = cursor.getString(titleID);
                String artist = cursor.getString(artistID);
                String album = cursor.getString(albumID);
                String duration = cursor.getString(durationID);
                String path = cursor.getString(pathID);
                String id = cursor.getString(_id);

//                Uri path = ContentUris.withAppendedId(
//                        MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, pathID);

                Song song = new Song(id, title, artist, album, duration, path, null);
                localSongsList.add(song);

                if (!duplicate.contains(album)) {
                    albums.add(song);
                    duplicate.add(album);
                }
            }
            cursor.close();
        }
        return localSongsList;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.action_bar_options, menu);
        MenuItem menuItem = menu.findItem(R.id.search_option);
        SearchView searchView = (SearchView) menuItem.getActionView();
        searchView.setOnQueryTextListener(this);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        String userInput = newText.toLowerCase();
        ArrayList<Song> searchSongs = new ArrayList<>();

        for (Song song : listAllSongs) {
            if (song.getTitle().toLowerCase().contains(userInput)) {
                searchSongs.add(song);
            }
        }

        SongsFragment.songAdapter.updateListSongs(searchSongs);

        return true;
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        SharedPreferences.Editor editor = getSharedPreferences(SORT_PREF, MODE_PRIVATE).edit();
        switch (item.getItemId()) {
            case R.id.by_name:
                editor.putString("sorting", "sortByName");
                editor.apply();
                this.recreate();
                break;
            case R.id.by_date:
                editor.putString("sorting", "sortByDate");
                editor.apply();
                this.recreate();
                break;
            case R.id.by_size:
                editor.putString("sorting", "sortBySize");
                editor.apply();
                this.recreate();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}