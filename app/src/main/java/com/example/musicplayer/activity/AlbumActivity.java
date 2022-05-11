package com.example.musicplayer.activity;

import static com.example.musicplayer.activity.MainActivity.listAllSongs;

import android.media.MediaMetadataRetriever;
import android.os.Bundle;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.musicplayer.R;
import com.example.musicplayer.adapter.AlbumDetailAdapter;
import com.example.musicplayer.model.Song;

import java.util.ArrayList;

public class AlbumActivity extends AppCompatActivity {

    RecyclerView albumRecyclerView;
    ImageView imageViewAlbumCover;
    String albumName;
    ArrayList<Song> albumSongs = new ArrayList<Song>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_album);
        initViews();
        initAlbumSongs();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (albumSongs.size() > 0) {
            AlbumDetailAdapter albumDetailAdapter = new AlbumDetailAdapter(this, albumSongs);
            albumRecyclerView.setAdapter(albumDetailAdapter);
            albumRecyclerView.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
        }
    }

    private void initAlbumSongs() {
        int albumIndex = 0;
        for (int songIndex = 0; songIndex < listAllSongs.size(); songIndex++) {
            if (albumName.equals(listAllSongs.get(songIndex).getAlbum())) {
                albumSongs.add(albumIndex, listAllSongs.get(songIndex));
                albumIndex++;
            }
        }

        byte[] image = getAlbumArt(albumSongs.get(0).getPath());
        if (image != null) {
            Glide.with(this)
                    .load(image)
                    .circleCrop()
                    .into(imageViewAlbumCover);
        } else {
            Glide.with(this)
                    .load(R.drawable.app_logo)
                    .circleCrop()
                    .into(imageViewAlbumCover);
        }
    }

    private void initViews() {
        albumRecyclerView = findViewById(R.id.albumRecycleView);
        imageViewAlbumCover = findViewById(R.id.albumImageViewAlbumCover);
        albumName = getIntent().getStringExtra("album_name");
    }

    private byte[] getAlbumArt(String path) {
        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        retriever.setDataSource(path);
        byte[] art = retriever.getEmbeddedPicture();
        retriever.release();
        return art;
    }
}