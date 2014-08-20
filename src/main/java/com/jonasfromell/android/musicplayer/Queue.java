package com.jonasfromell.android.musicplayer;

import java.util.ArrayList;

/**
 * Created by Jonas on 8/20/14.
 */
public class Queue {

    private ArrayList<Song> mSongs;

    public Queue () {
        mSongs = new ArrayList<Song>();
    }

    public void add (Song song) {
        mSongs.add(song);
    }

    public void add (ArrayList<Song> songs) {
        mSongs.addAll(songs);
    }

    public void remove (Song song) {
        mSongs.remove(song);
    }

    public void clear () {
        mSongs.clear();
    }

    public ArrayList<Song> getSongs () {
        return mSongs;
    }

    public Song getNext (int curPosition) {
        if (mSongs.get(++curPosition) != null) {
            return mSongs.get(++curPosition);
        }

        return null;
    }

    public Song getPrevious (int curPosition) {
        if (mSongs.get(--curPosition) != null) {
            return mSongs.get(--curPosition);
        }

        return null;
    }

    public int indexOf (Song song) {
        return mSongs.indexOf(song);
    }

}
