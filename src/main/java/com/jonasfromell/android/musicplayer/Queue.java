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

    public ArrayList<Song> getAll () {
        return mSongs;
    }

    public Song getFirst () {
        return mSongs.get(0);
    }

    public Song getNext (int curPosition) {
        if (!(++curPosition > mSongs.size())) {
            return mSongs.get(++curPosition);
        }

        return null;
    }

    public Song getPrevious (int curPosition) {
        if (!(--curPosition < 0)) {
            return mSongs.get(--curPosition);
        }

        return null;
    }

    public int indexOf (Song song) {
        return mSongs.indexOf(song);
    }

    public int length () {
        return mSongs.size();
    }

}
