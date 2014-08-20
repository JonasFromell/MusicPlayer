package com.jonasfromell.android.musicplayer;

/**
 * Created by Jonas on 8/13/14.
 */
public class Song {

    private long mID;
    private String mTitle;
    private String mArtist;
    private String mAlbum;
    private String mFilepath;

    public Song (long ID, String title, String artist, String album, String filepath) {
        mID = ID;
        mTitle = title;
        mArtist = artist;
        mAlbum = album;
        mFilepath = filepath;
    }

    public long getID () {
        return mID;
    }

    public String getTitle () {
        return mTitle;
    }

    public String getArtist () {
        return mArtist;
    }

    public String getAlbum () {
        return mAlbum;
    }

    public String getFilepath () {
        return mFilepath;
    }
}
