package com.example.musicplayer.model;

public class Song {
    private String id;
    private String title;
    private String artist;
    private String album;
    private String duration;
    private String localPath;
    private String onlinePath;

    public Song() {
    }

    public Song(String id, String title, String artist, String album, String duration, String localPath, String onlinePath) {
        this.id = id;
        this.title = title;
        this.artist = artist;
        this.album = album;
        this.duration = duration;
        this.localPath = localPath;
        this.onlinePath = onlinePath;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public String getAlbum() {
        return album;
    }

    public void setAlbum(String album) {
        this.album = album;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getLocalPath() {
        return localPath;
    }

    public void setLocalPath(String localPath) {
        this.localPath = localPath;
    }

    public String getOnlinePath() {
        return onlinePath;
    }

    public void setOnlinePath(String onlinePath) {
        this.onlinePath = onlinePath;
    }

    public String getPath() {
        if (this.localPath != null) {
            return this.localPath;
        } else {
            return this.onlinePath;
        }
    }

    @Override
    public String toString() {
        return "Song{" +
                "title='" + title + '\'' +
                ", artist='" + artist + '\'' +
                ", album='" + album + '\'' +
                ", duration='" + duration + '\'' +
                ", localPath='" + localPath + '\'' +
                ", onlinePath='" + onlinePath + '\'' +
                '}';
    }
}
