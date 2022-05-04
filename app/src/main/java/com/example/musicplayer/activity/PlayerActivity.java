package com.example.musicplayer.activity;

import static com.example.musicplayer.activity.MainActivity.localSongs;

import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.musicplayer.R;
import com.example.musicplayer.model.Song;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class PlayerActivity extends AppCompatActivity {

    TextView songName, artistName, durationPlayed, durationTotal;
    ImageView backButton, coverArt, shuffleButton, forwardButton, previousButton, repeatButton;
    FloatingActionButton playPauseButton;
    SeekBar seekBar;

    ArrayList<Song> listSongs = new ArrayList<>();

    int position;

    static Uri uri;
    static MediaPlayer mediaPlayer;

    private Handler handler = new Handler();
    private Thread playThread, previousThread, forwardThread;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);

        initViews();
        getIntentMethod();

        songName.setText(listSongs.get(position).getTitle());
        artistName.setText(listSongs.get(position).getArtist());
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
                handler.postDelayed(this, 100);
            }
        });
    }

    @Override
    protected void onResume() {
        playThreadButton();
        previousThreadButton();
        forwardThreadButton();
        super.onResume();
    }

    private void forwardThreadButton() {
        forwardThread = new Thread() {
            @Override
            public void run() {
                super.run();
                forwardButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        forwardButtonClicked();
                    }
                });
            }
        };
        forwardThread.start();
    }

    private void forwardButtonClicked() {
        if (mediaPlayer.isPlaying()) {
            mediaPlayer.stop();
            mediaPlayer.release();
            position = ((position + 1) % listSongs.size());
            uri = Uri.parse(listSongs.get(position).getPath());
            mediaPlayer = MediaPlayer.create(getApplicationContext(), uri);
            metaData(uri);
            songName.setText(listSongs.get(position).getTitle());
            artistName.setText(listSongs.get(position).getArtist());
            seekBar.setMax(mediaPlayer.getDuration() / 1000);
            PlayerActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (mediaPlayer != null) {
                        int mCurrentPosition = mediaPlayer.getCurrentPosition() / 1000;
                        seekBar.setProgress(mCurrentPosition);
                    }
                    handler.postDelayed(this, 100);
                }
            });
            playPauseButton.setImageResource(R.drawable.player_pause_icon);
            mediaPlayer.start();
        } else {
            mediaPlayer.stop();
            mediaPlayer.release();
            position = ((position + 1) % listSongs.size());
            uri = Uri.parse(listSongs.get(position).getPath());
            mediaPlayer = MediaPlayer.create(getApplicationContext(), uri);
            metaData(uri);
            songName.setText(listSongs.get(position).getTitle());
            artistName.setText(listSongs.get(position).getArtist());
            seekBar.setMax(mediaPlayer.getDuration() / 1000);
            PlayerActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (mediaPlayer != null) {
                        int mCurrentPosition = mediaPlayer.getCurrentPosition() / 1000;
                        seekBar.setProgress(mCurrentPosition);
                    }
                    handler.postDelayed(this, 100);
                }
            });
            playPauseButton.setImageResource(R.drawable.player_play_icon);
        }
    }


    private void previousThreadButton() {
        previousThread = new Thread() {
            @Override
            public void run() {
                super.run();
                previousButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        previousButtonClicked();
                    }
                });
            }
        };
        previousThread.start();
    }

    private void previousButtonClicked() {
        if (mediaPlayer.isPlaying()) {
            mediaPlayer.stop();
            mediaPlayer.release();
            position = ((position - 1) < 0 ? (listSongs.size() - 1) : (position - 1));
            uri = Uri.parse(listSongs.get(position).getPath());
            mediaPlayer = MediaPlayer.create(getApplicationContext(), uri);
            metaData(uri);
            songName.setText(listSongs.get(position).getTitle());
            artistName.setText(listSongs.get(position).getArtist());
            seekBar.setMax(mediaPlayer.getDuration() / 1000);
            PlayerActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (mediaPlayer != null) {
                        int mCurrentPosition = mediaPlayer.getCurrentPosition() / 1000;
                        seekBar.setProgress(mCurrentPosition);
                    }
                    handler.postDelayed(this, 100);
                }
            });
            playPauseButton.setImageResource(R.drawable.player_pause_icon);
            mediaPlayer.start();
        } else {
            mediaPlayer.stop();
            mediaPlayer.release();
            position = ((position - 1) < 0 ? (listSongs.size() - 1) : (position - 1));
            uri = Uri.parse(listSongs.get(position).getPath());
            mediaPlayer = MediaPlayer.create(getApplicationContext(), uri);
            metaData(uri);
            songName.setText(listSongs.get(position).getTitle());
            artistName.setText(listSongs.get(position).getArtist());
            seekBar.setMax(mediaPlayer.getDuration() / 1000);
            PlayerActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (mediaPlayer != null) {
                        int mCurrentPosition = mediaPlayer.getCurrentPosition() / 1000;
                        seekBar.setProgress(mCurrentPosition);
                    }
                    handler.postDelayed(this, 100);
                }
            });
            playPauseButton.setImageResource(R.drawable.player_play_icon);
        }
    }


    private void playThreadButton() {
        playThread = new Thread() {
            @Override
            public void run() {
                super.run();
                playPauseButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        playPauseButtonClicked();
                    }
                });
            }
        };
        playThread.start();
    }

    private void playPauseButtonClicked() {
        if (mediaPlayer.isPlaying()) {
            playPauseButton.setImageResource(R.drawable.player_play_icon);
            mediaPlayer.pause();
            seekBar.setMax(mediaPlayer.getDuration() / 1000);
            PlayerActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (mediaPlayer != null) {
                        int mCurrentPosition = mediaPlayer.getCurrentPosition() / 1000;
                        seekBar.setProgress(mCurrentPosition);
                    }
                    handler.postDelayed(this, 100);
                }
            });
        } else {
            playPauseButton.setImageResource(R.drawable.player_pause_icon);
            mediaPlayer.start();
            seekBar.setMax(mediaPlayer.getDuration() / 1000);
            PlayerActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (mediaPlayer != null) {
                        int mCurrentPosition = mediaPlayer.getCurrentPosition() / 1000;
                        seekBar.setProgress(mCurrentPosition);
                    }
                    handler.postDelayed(this, 100);
                }
            });
        }
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

        metaData(uri);
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

    private void metaData(Uri uri) {
        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        retriever.setDataSource(uri.toString());
        int durationTotalData = Integer.parseInt(listSongs.get(position).getDuration()) / 1000;
        durationTotal.setText(formattedTime(durationTotalData));

        byte[] art = retriever.getEmbeddedPicture();
        if (art != null) {
            Glide.with(this)
                    .asBitmap()
                    .load(art)
                    .into(coverArt);
        } else {
            Glide.with(this)
                    .asBitmap()
                    .load(R.drawable.ic_launcher_background)
                    .into(coverArt);
        }
    }

}