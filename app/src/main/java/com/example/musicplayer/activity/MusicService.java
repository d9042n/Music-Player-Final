package com.example.musicplayer.activity;

import static com.example.musicplayer.activity.MainActivity.listAllSongs;

import android.app.Service;
import android.content.Intent;
import android.media.AudioAttributes;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Binder;
import android.os.IBinder;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.example.musicplayer.animation.PLayerAnimation;
import com.example.musicplayer.model.Song;

import java.util.ArrayList;

public class MusicService extends Service implements MediaPlayer.OnCompletionListener {
    String path = null;

    PLayerAnimation pLayerAnimation;
    IBinder mBinder = new PlayerBinder();
    MediaPlayer mediaPlayer;
    ActionPlaying actionPlaying;
    ArrayList<Song> songs = new ArrayList<Song>();

    @Override
    public void onCreate() {
        super.onCreate();

    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    @Override
    public void onCompletion(MediaPlayer mediaPlayer) {
        if (pLayerAnimation != null) {
            pLayerAnimation.stopDiscTurnAnimation();
        }
        if (actionPlaying != null) {
            actionPlaying.forwardButtonClicked();
        }
        createMediaPlayer(path);
        start();
        onCompleted();

    }

    public class PlayerBinder extends Binder {
        MusicService getService() {
            return MusicService.this;
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        String path = intent.getStringExtra("servicePath");
        if (path != null) {
            playMedia(path);
        }
        return START_STICKY;
    }

    private void playMedia(String path) {
        songs = listAllSongs;

        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
            if (songs != null) {
                createMediaPlayer(path);
                mediaPlayer.start();
            }
        } else {
            createMediaPlayer(path);
            mediaPlayer.start();
        }
    }


    void start() {
        mediaPlayer.start();
    }

    void pause() {
        mediaPlayer.pause();
    }

    boolean isPlaying() {
        return mediaPlayer.isPlaying();
    }

    void stop() {
        mediaPlayer.stop();
    }

    void release() {
        mediaPlayer.release();
    }

    int getDuration() {
        return mediaPlayer.getDuration();
    }

    int getCurrentPosition() {
        return mediaPlayer.getCurrentPosition();
    }

    void seekTo(int position) {
        mediaPlayer.seekTo(position);
    }

    void onCompleted() {
        mediaPlayer.setOnCompletionListener(this);
    }

    void createMediaPlayer(String path) {
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
    }
}
