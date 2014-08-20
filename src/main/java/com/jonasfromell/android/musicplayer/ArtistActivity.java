package com.jonasfromell.android.musicplayer;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBarActivity;

/**
 * Created by Jonas on 8/14/14.
 */
public class ArtistActivity extends ActionBarActivity {
    private static final String TAG = "ArtistActivity";

    public static final String EXTRA_ARTIST_ID = "com.jonasfromell.android.musicplayer.album_id";

    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_artist);

        // Create the fragment instance
        FragmentManager fm = getSupportFragmentManager();
        Fragment fragment = fm.findFragmentById(R.id.artist_fragment_container);

        if (fragment == null) {
            fragment = ArtistFragment.newInstance(getIntent().getLongExtra(EXTRA_ARTIST_ID, -1));

            // Begin the transaction
            fm.beginTransaction()
                    .add(R.id.artist_fragment_container, fragment)
                    .commit();
        }
    }
}
