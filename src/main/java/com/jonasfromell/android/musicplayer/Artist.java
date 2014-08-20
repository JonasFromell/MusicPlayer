package com.jonasfromell.android.musicplayer;

/**
 * Created by Jonas on 8/13/14.
 */
public class Artist {

    private long mID;
    private String mName;
    private String mNumSongs;
    private String mNumAlbums;

    public Artist (long ID, String name, String numSongs, String numAlbums) {
        mID = ID;
        mName = name;
        mNumSongs = numSongs;
        mNumAlbums = numAlbums;
    }

    public long getID () {
        return mID;
    }

    public String getName () {
        return mName;
    }

    public String getNumSongs () {
        return mNumSongs;
    }

    public String getNumAlbums () {
        return mNumAlbums;
    }
}
