package com.example.musicplayer.activity;

import static com.example.musicplayer.activity.MainActivity.localSongs;

import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.musicplayer.R;
import com.example.musicplayer.model.Song;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class PlayerActivity extends AppCompatActivity {

    TextView songName, artistName, durationPlayed, durationTotal;
    ImageView backButton, coverArt, shuffleButton, forwardButton, previousButton, repeatButton;
    FloatingActionButton playPauseButton;
    SeekBar seekBar;

    int position;
    ArrayList<Song> listSongs = new ArrayList<>();
    static Uri uri;

    static MediaPlayer mediaPlayer;
    private Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);

        initViews();
        getIntentMethod();

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (mediaPlayer != null && fromUser) {
                    mediaPlayer.seekTo(progress * 1000);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        PlayerActivity.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (mediaPlayer != null) {
                    int mCurrentPosition = mediaPlayer.getCurrentPosition() / 1000;
                    seekBar.setProgress(mCurrentPosition);
                    durationPlayed.setText(formattedTime(mCurrentPosition));
                }
                handler.postDelayed(this, 1000);
            }
        });
    }

    private String formattedTime(int mCurrentPosition) {
        String totalOut = "";
        String totalNew = "";
        String seconds = String.valueOf(mCurrentPosition % 60);
        String minutes = String.valueOf(mCurrentPosition / 60);

        totalOut = minutes + ":" + seconds;
        totalNew = minutes + ":" + "0" + seconds;

        if (seconds.length() == 1) {
            return totalNew;
        } else {
            return totalOut;
        }
    }

    private void getIntentMethod() {
        position = getIntent().getIntExtra("position", -1);
        listSongs = localSongs;

        if (listSongs != null) {
            playPauseButton.setImageResource(R.drawable.player_pause_icon);
            uri = Uri.parse(listSongs.get(position).getPath());
        }

        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = MediaPlayer.create(getApplicationContext(), uri);
            mediaPlayer.start();
        } else {
            mediaPlayer = MediaPlayer.create(getApplicationContext(), uri);
            mediaPlayer.start();
        }

        seekBar.setMax(mediaPlayer.getDuration() / 1000);
    }

    private void initViews() {
        songName = findViewById(R.id.playerTextViewSongName);
        artistName = findViewById(R.id.playerTextViewSongArtist);
        durationPlayed = findViewById(R.id.playerTextViewDurationPlayed);
        durationTotal = findViewById(R.id.playerTextViewDurationTotal);
        backButton = findViewById(R.id.playerImageViewBackButton);
        coverArt = findViewById(R.id.playerImageViewCoverArt);
        shuffleButton = findViewById(R.id.playerImageViewShuffleButton);
        forwardButton = findViewById(R.id.playerImageViewForwardButton);
        previousButton = findViewById(R.id.playerImageViewPreviousButton);
        repeatButton = findViewById(R.id.playerImageViewRepeatButton);
        playPauseButton = findViewById(R.id.playerImageViewPlayPauseButton);
        seekBar = findViewById(R.id.playerSeekBar);
    }


}