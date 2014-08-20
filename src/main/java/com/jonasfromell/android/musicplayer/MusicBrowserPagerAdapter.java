package com.jonasfromell.android.musicplayer;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * Created by Jonas on 8/13/14.
 */
public class MusicBrowserPagerAdapter extends FragmentPagerAdapter {

    private static final int PAGE_COUNT = 4;

    public MusicBrowserPagerAdapter (FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem (int position) {
        switch (position) {

            // Playlists
            case 0:
                return new PlaylistsFragment();
            // Songs
            case 1:
                return new SongsFragment();
            // Artists
            case 2:
                return new ArtistsFragment();
            // Albums
            case 3:
                return new AlbumsFragment();

        }

        return null;
    }

    @Override
    public int getCount () {
        return PAGE_COUNT;
    }
}
