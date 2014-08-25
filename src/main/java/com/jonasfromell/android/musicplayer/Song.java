package com.jonasfromell.android.musicplayer;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Jonas on 8/13/14.
 */
public class Song implements Parcelable {

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

    /**
     * Parcelable implementation
     */
    @Override
    public int describeContents () {
        return 0;
    }

    @Override
    public void writeToParcel (Parcel parcel, int flags) {
        parcel.writeLong(mID);
        parcel.writeString(mTitle);
        parcel.writeString(mArtist);
        parcel.writeString(mAlbum);
        parcel.writeString(mFilepath);
    }

    public static final Creator<Song> CREATOR = new Creator<Song>() {
        @Override
        public Song createFromParcel (Parcel parcel) {
            return new Song(parcel);
        }

        @Override
        public Song[] newArray (int size) {
            return new Song[size];
        }
    };

    private Song (Parcel parcel) {
        mID = parcel.readLong();
        mTitle = parcel.readString();
        mArtist = parcel.readString();
        mAlbum = parcel.readString();
        mFilepath = parcel.readString();
    }
}
