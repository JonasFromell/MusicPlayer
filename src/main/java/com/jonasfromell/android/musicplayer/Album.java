package com.jonasfromell.android.musicplayer;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.util.ArrayList;

/**
 * Created by Jonas on 8/13/14.
 */
public class Album {

    private long mID;
    private String mArtist;
    private String mName;
    private String mCover;

    public Album (long ID, String artist, String name, String cover) {
        mID = ID;
        mArtist = artist;
        mName = name;
        mCover = cover;
    }

    public long getID () {
        return mID;
    }

    public String getName () {
        return mName;
    }

    public String getArtist () {
        return mArtist;
    }

    public Bitmap getCover () {
        return BitmapFactory.decodeFile(mCover);
    }

}
