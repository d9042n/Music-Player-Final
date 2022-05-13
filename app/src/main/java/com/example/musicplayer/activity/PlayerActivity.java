package com.example.musicplayer.activity;

import static com.example.musicplayer.activity.MainActivity.repeatBoolean;
import static com.example.musicplayer.activity.MainActivity.shuffleBoolean;
import static com.example.musicplayer.adapter.AlbumDetailAdapter.mAlbumSongs;
import static com.example.musicplayer.adapter.SongAdapter.mSongs;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.media.MediaMetadataRetriever;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.musicplayer.R;
import com.example.musicplayer.animation.PLayerAnimation;
import com.example.musicplayer.model.Song;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Random;

public class PlayerActivity extends AppCompatActivity
        implements ActionPlaying, ServiceConnection {

    TextView songName, artistName, durationPlayed, durationTotal;
    ImageView backButton, coverArt, shuffleButton, forwardButton, previousButton, repeatButton;
    FloatingActionButton playPauseButton;
    SeekBar seekBar;
    MusicService musicService;

    PLayerAnimation pLayerAnimation = new PLayerAnimation();

    ArrayList<Song> listSongs = new ArrayList<>();

    int position;

    static String path;

    private final Handler handler = new Handler();
    private Thread playThread, previousThread, forwardThread;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);

        initViews();
        getIntentMethod();

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (musicService != null && fromUser) {
                    musicService.seekTo(progress * 1000);
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
                if (musicService != null) {
                    int mCurrentPosition = musicService.getCurrentPosition() / 1000;
                    seekBar.setProgress(mCurrentPosition);
                    durationPlayed.setText(formattedTime(mCurrentPosition));
                }
                handler.postDelayed(this, 100);
            }
        });

        shuffleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (shuffleBoolean) {
                    shuffleBoolean = false;
                    shuffleButton.setImageResource(R.drawable.player_shuffle_off);
                } else {
                    shuffleBoolean = true;
                    shuffleButton.setImageResource(R.drawable.player_shuffle_on);
                }
            }
        });
        repeatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (repeatBoolean) {
                    repeatBoolean = false;
                    repeatButton.setImageResource(R.drawable.player_repeat_off);
                } else {
                    repeatBoolean = true;
                    repeatButton.setImageResource(R.drawable.player_repeat_on);
                }
            }
        });
    }

    @Override
    protected void onResume() {
        Intent intent = new Intent(this, MusicService.class);
        bindService(intent, this, BIND_AUTO_CREATE);
        playThreadButton();
        previousThreadButton();
        forwardThreadButton();
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        unbindService(this);
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

    public void forwardButtonClicked() {
        if (musicService.isPlaying()) {
            musicService.stop();
            musicService.release();

            if (shuffleBoolean && !repeatBoolean) {
                position = getRandomSong(listSongs.size() - 1);
            } else if (!shuffleBoolean && !repeatBoolean) {
                position = ((position + 1) % listSongs.size());
            }

            path = listSongs.get(position).getPath();
            musicService.createMediaPlayer(path);
            metaData(path);
            songName.setText(listSongs.get(position).getTitle());
            artistName.setText(listSongs.get(position).getArtist());
            seekBar.setMax(musicService.getDuration() / 1000);
            playerActivityRunOnUiThread();
            musicService.onCompleted();
            playPauseButton.setImageResource(R.drawable.player_pause_icon);
            musicService.start();
        } else {
            musicService.stop();
            musicService.release();

            if (shuffleBoolean && !repeatBoolean) {
                position = getRandomSong(listSongs.size() - 1);
            } else if (!shuffleBoolean && !repeatBoolean) {
                position = ((position + 1) % listSongs.size());
            }

            path = listSongs.get(position).getPath();
            musicService.createMediaPlayer(path);
            metaData(path);
            songName.setText(listSongs.get(position).getTitle());
            artistName.setText(listSongs.get(position).getArtist());
            seekBar.setMax(musicService.getDuration() / 1000);
            playerActivityRunOnUiThread();
            musicService.onCompleted();
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

    public void previousButtonClicked() {
        if (musicService.isPlaying()) {
            musicService.stop();
            musicService.release();

            if (shuffleBoolean && !repeatBoolean) {
                position = getRandomSong(listSongs.size() - 1);
            } else if (!shuffleBoolean && !repeatBoolean) {
                position = ((position - 1) < 0 ? (listSongs.size() - 1) : (position - 1));
            }

            path = listSongs.get(position).getPath();
            musicService.createMediaPlayer(path);
            metaData(path);
            songName.setText(listSongs.get(position).getTitle());
            artistName.setText(listSongs.get(position).getArtist());
            seekBar.setMax(musicService.getDuration() / 1000);
            playerActivityRunOnUiThread();
            musicService.onCompleted();
            playPauseButton.setImageResource(R.drawable.player_pause_icon);
            musicService.start();
        } else {
            musicService.stop();
            musicService.release();

            if (shuffleBoolean && !repeatBoolean) {
                position = getRandomSong(listSongs.size() - 1);
            } else if (!shuffleBoolean && !repeatBoolean) {
                position = ((position - 1) < 0 ? (listSongs.size() - 1) : (position - 1));
            }

            path = listSongs.get(position).getPath();
            musicService.createMediaPlayer(path);
            metaData(path);
            songName.setText(listSongs.get(position).getTitle());
            artistName.setText(listSongs.get(position).getArtist());
            seekBar.setMax(musicService.getDuration() / 1000);
            playerActivityRunOnUiThread();
            musicService.onCompleted();
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

    public void playPauseButtonClicked() {
        if (musicService.isPlaying()) {
            playPauseButton.setImageResource(R.drawable.player_play_icon);
            musicService.pause();
            seekBar.setMax(musicService.getDuration() / 1000);
            pLayerAnimation.setCoverArt(coverArt);
            pLayerAnimation.stopDiscTurnAnimation();
            playerActivityRunOnUiThread();
        } else {
            playPauseButton.setImageResource(R.drawable.player_pause_icon);
            musicService.start();
            seekBar.setMax(musicService.getDuration() / 1000);
            pLayerAnimation.setCoverArt(coverArt);
            pLayerAnimation.startDiscTurnAnimation();
            playerActivityRunOnUiThread();
        }
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

    private int getRandomSong(int i) {
        Random random = new Random();
        return random.nextInt(i + 1);
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

        String sender = getIntent().getStringExtra("sender");
        if (sender != null && sender.equals("albumDetails")) {
            listSongs = mAlbumSongs;
        } else {
            listSongs = mSongs;
        }

        if (listSongs != null) {
            playPauseButton.setImageResource(R.drawable.player_pause_icon);
            path = listSongs.get(position).getPath();
        }

        Intent intent = new Intent(this, MusicService.class);
        intent.putExtra("servicePath", path);
        startService(intent);
    }

    private void metaData(String path) {
        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        retriever.setDataSource(path);
        int durationTotalData = Integer.parseInt(listSongs.get(position).getDuration()) / 1000;
        durationTotal.setText(formattedTime(durationTotalData));

        byte[] art = retriever.getEmbeddedPicture();
        if (art != null) {
            Glide.with(this)
                    .asBitmap()
                    .load(art)
                    .circleCrop()
                    .into(coverArt);
        } else {
            Glide.with(this)
                    .asBitmap()
                    .load(R.drawable.app_logo)
                    .circleCrop()
                    .into(coverArt);
        }
        pLayerAnimation.setCoverArt(coverArt);
        pLayerAnimation.startDiscTurnAnimation();
    }

    private void playerActivityRunOnUiThread() {
        PlayerActivity.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (musicService != null) {
                    int mCurrentPosition = musicService.getCurrentPosition() / 1000;
                    seekBar.setProgress(mCurrentPosition);
                }
                handler.postDelayed(this, 100);
            }
        });
    }

    @Override
    public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
        MusicService.PlayerBinder playerBinder = (MusicService.PlayerBinder) iBinder;
        musicService = playerBinder.getService();

        seekBar.setMax(musicService.getDuration() / 1000);

        metaData(path);

        songName.setText(listSongs.get(position).getTitle());
        artistName.setText(listSongs.get(position).getArtist());
        musicService.onCompleted();
    }

    @Override
    public void onServiceDisconnected(ComponentName componentName) {
        musicService = null;
    }
}