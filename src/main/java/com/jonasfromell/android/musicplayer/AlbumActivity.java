package com.jonasfromell.android.musicplayer;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;

/**
 * Created by Jonas on 8/14/14.
 */
public class AlbumActivity extends ActionBarActivity {
    private static final String TAG = "AlbumActivity";

    public static final String EXTRA_ALBUM_ID = "com.jonasfromell.android.musicplayer.album_id";

    private ActionBar mActionBar;

    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_album);

        // Create the fragment instance
        FragmentManager fm = getSupportFragmentManager();
        Fragment fragment = fm.findFragmentById(R.id.album_fragment_container);

        if (fragment == null) {
            fragment = AlbumFragment.newInstance(getIntent().getLongExtra(EXTRA_ALBUM_ID, -1));

            // Begin the transaction
            fm.beginTransaction()
                    .add(R.id.album_fragment_container, fragment)
                    .commit();
        }
    }
}
