package com.example.musicplayer.activity;

import static com.example.musicplayer.activity.MainActivity.listAllSongs;
import static com.example.musicplayer.activity.MainActivity.repeatBoolean;
import static com.example.musicplayer.activity.MainActivity.shuffleBoolean;

import android.media.AudioAttributes;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.musicplayer.R;
import com.example.musicplayer.animation.PLayerAnimation;
import com.example.musicplayer.model.Song;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Random;

public class PlayerActivity extends AppCompatActivity {

    TextView songName, artistName, durationPlayed, durationTotal;
    ImageView backButton, coverArt, shuffleButton, forwardButton, previousButton, repeatButton;
    FloatingActionButton playPauseButton;
    SeekBar seekBar;

    PLayerAnimation pLayerAnimation = new PLayerAnimation();

    ArrayList<Song> listSongs = new ArrayList<>();

    int position;

    static String path;
    static MediaPlayer mediaPlayer;

    private final Handler handler = new Handler();
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

        shuffleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (shuffleBoolean) {
                    shuffleBoolean = false;
                    shuffleButton.setImageResource(R.drawable.ic_shuffle_off);
                } else {
                    shuffleBoolean = true;
                    shuffleButton.setImageResource(R.drawable.ic_shuffle_on);
                }
            }
        });
        repeatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (repeatBoolean) {
                    repeatBoolean = false;
                    repeatButton.setImageResource(R.drawable.ic_repeat_off);
                } else {
                    repeatBoolean = true;
                    repeatButton.setImageResource(R.drawable.ic_repeat_on);
                }
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

            if (shuffleBoolean && !repeatBoolean) {
                position = getRandomSong(listSongs.size() - 1);
            } else if (!shuffleBoolean && !repeatBoolean) {
                position = ((position + 1) % listSongs.size());
            }

            path = listSongs.get(position).getPath();
            mediaPlayer = prepareSong(path);
            metaData(path);
            songName.setText(listSongs.get(position).getTitle());
            artistName.setText(listSongs.get(position).getArtist());
            seekBar.setMax(mediaPlayer.getDuration() / 1000);
            playerActivityRunOnUiThread();
            playPauseButton.setImageResource(R.drawable.player_pause_icon);
            mediaPlayer.start();
        } else {
            mediaPlayer.stop();
            mediaPlayer.release();

            if (shuffleBoolean && !repeatBoolean) {
                position = getRandomSong(listSongs.size() - 1);
            } else if (!shuffleBoolean && !repeatBoolean) {
                position = ((position + 1) % listSongs.size());
            }

            path = listSongs.get(position).getPath();
            mediaPlayer = prepareSong(path);
            metaData(path);
            songName.setText(listSongs.get(position).getTitle());
            artistName.setText(listSongs.get(position).getArtist());
            seekBar.setMax(mediaPlayer.getDuration() / 1000);
            playerActivityRunOnUiThread();
            playPauseButton.setImageResource(R.drawable.player_play_icon);
        }
    }

    private int getRandomSong(int i) {
        Random random = new Random();
        return random.nextInt(i + 1);
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

            if (shuffleBoolean && !repeatBoolean) {
                position = getRandomSong(listSongs.size() - 1);
            } else if (!shuffleBoolean && !repeatBoolean) {
                position = ((position - 1) < 0 ? (listSongs.size() - 1) : (position - 1));
            }

            path = listSongs.get(position).getPath();
            mediaPlayer = prepareSong(path);
            metaData(path);
            songName.setText(listSongs.get(position).getTitle());
            artistName.setText(listSongs.get(position).getArtist());
            seekBar.setMax(mediaPlayer.getDuration() / 1000);
            playerActivityRunOnUiThread();
            playPauseButton.setImageResource(R.drawable.player_pause_icon);
            mediaPlayer.start();
        } else {
            mediaPlayer.stop();
            mediaPlayer.release();

            if (shuffleBoolean && !repeatBoolean) {
                position = getRandomSong(listSongs.size() - 1);
            } else if (!shuffleBoolean && !repeatBoolean) {
                position = ((position - 1) < 0 ? (listSongs.size() - 1) : (position - 1));
            }

            path = listSongs.get(position).getPath();
            mediaPlayer = prepareSong(path);
            metaData(path);
            songName.setText(listSongs.get(position).getTitle());
            artistName.setText(listSongs.get(position).getArtist());
            seekBar.setMax(mediaPlayer.getDuration() / 1000);
            playerActivityRunOnUiThread();
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
            pLayerAnimation.stopDiscTurnAnimation(coverArt);
            playerActivityRunOnUiThread();
        } else {
            playPauseButton.setImageResource(R.drawable.player_pause_icon);
            mediaPlayer.start();
            seekBar.setMax(mediaPlayer.getDuration() / 1000);
            pLayerAnimation.startDiscTurnAnimation(coverArt);
            playerActivityRunOnUiThread();
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
        listSongs = listAllSongs;

        if (listSongs != null) {
            playPauseButton.setImageResource(R.drawable.player_pause_icon);
            path = listSongs.get(position).getPath();
        }

        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = prepareSong(path);
            mediaPlayer.start();
        } else {
            mediaPlayer = prepareSong(path);
            mediaPlayer.start();
        }

        seekBar.setMax(mediaPlayer.getDuration() / 1000);

        metaData(path);
    }

    private MediaPlayer prepareSong(String path) {
        if (path.contains("http")) {
            try {
                mediaPlayer = new MediaPlayer();
                mediaPlayer.setAudioAttributes(
                        new AudioAttributes.Builder()
                                .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                                .setUsage(AudioAttributes.USAGE_MEDIA)
                                .build()
                );
                mediaPlayer.setDataSource(path);
                mediaPlayer.prepare();
            } catch (Exception exception) {
                Toast.makeText(this, exception.toString(), Toast.LENGTH_SHORT);
            }
        } else {
            Uri uri = Uri.parse(path);
            try {
                mediaPlayer = new MediaPlayer();
                mediaPlayer.setAudioAttributes(
                        new AudioAttributes.Builder()
                                .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                                .setUsage(AudioAttributes.USAGE_MEDIA)
                                .build()
                );
                mediaPlayer.setDataSource(getApplicationContext(), uri);
                mediaPlayer.prepare();
            } catch (Exception exception) {
                Toast.makeText(this, exception.toString(), Toast.LENGTH_SHORT);
            }
        }
        return mediaPlayer;
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

    private void playerActivityRunOnUiThread() {
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
        pLayerAnimation.startDiscTurnAnimation(coverArt);
    }

}