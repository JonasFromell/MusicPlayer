package com.jonasfromell.android.musicplayer;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;


public class MusicBrowserActivity extends ActionBarActivity implements SongsFragment.OnContextMenuItemClicked, SongsFragment.OnListItemClicked, PlayerFragment.OnPlayerControlClickedListener {
    private static final String TAG = "MusicBrowserActivity";

    private ActionBar mActionBar;
    private ViewPager mViewPager;

    private DrawerLayout mDrawerLayout;
    private ListView mLeftDrawer;
    private ActionBarDrawerToggle mDrawerToggle;

    private Messenger mPlaybackService;
    private boolean mIsBound;

    private boolean mIsPlayerHidden;

    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music_browser);

        doBindService();

        // TODO: Clean up this mess
        // Get reference to the action bar
        // and set the navigation mode
        mActionBar = getSupportActionBar();
        mActionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
        mActionBar.setDisplayHomeAsUpEnabled(true);
        mActionBar.setHomeButtonEnabled(true);

        // Get reference to the drawer layout
        mDrawerLayout = (DrawerLayout) findViewById(R.id.music_browser_drawer_layout);
        mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow, Gravity.START);
        mDrawerLayout.setScrimColor(0x4C000000);

        // Get reference to the list view (drawer)
        mLeftDrawer = (ListView) findViewById(R.id.music_browser_left_drawer);

        // Setup the list for the drawer
        mLeftDrawer.setAdapter(new ArrayAdapter<String>(this,
                R.layout.list_item_drawer, R.id.list_drawer_title, getResources().getStringArray(R.array.drawer_menu_items)));

        mLeftDrawer.setOnItemClickListener(new DrawerItemCickListener());

        mDrawerLayout = (DrawerLayout) findViewById(R.id.music_browser_drawer_layout);
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.drawable.ic_drawer, R.string.open_drawer, R.string.close_drawer) {
            @Override
            public void onDrawerClosed (View drawerView) {
                super.onDrawerClosed(drawerView);

                mActionBar.setTitle(R.string.activity_music_browser);
            }

            @Override
            public void onDrawerOpened (View drawerView) {
                super.onDrawerOpened(drawerView);

                mActionBar.setTitle(R.string.app_name);
            }
        };
        mDrawerLayout.setDrawerListener(mDrawerToggle);

        // Get reference to the view pager
        // and listeners
        mViewPager = (ViewPager) findViewById(R.id.music_browser_view_pager);
        mViewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected (int position) {
                super.onPageSelected(position);

                // Select the correct tab in the action bar
                mActionBar.setSelectedNavigationItem(position);
            }
        });

        // Get reference to the fragment manager
        FragmentManager fm = getSupportFragmentManager();

        // Create an instance of fragment pager adapter
        MusicBrowserPagerAdapter fragmentPagerAdapter = new MusicBrowserPagerAdapter(fm);

        // Set the adapter for the view pager
        mViewPager.setAdapter(fragmentPagerAdapter);

        // Instantiate the player fragment
        Fragment fragment = fm.findFragmentById(R.id.music_browser_player_fragment);

        if (fragment == null) {
            fragment = new PlayerFragment();

            fm.beginTransaction()
                    .add(R.id.music_browser_player_fragment, fragment)
                    .hide(fragment)
                    .commit();
        }

        // Player fragment is hidden by default
        mIsPlayerHidden = true;

        // Define the listener for the tab selection
        ActionBar.TabListener tabListener = new ActionBar.TabListener() {
            @Override
            public void onTabSelected (ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
                mViewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected (ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
                // Required, but not implemented
            }

            @Override
            public void onTabReselected (ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
                // Required, but not implemented
            }
        };

        // Define the tabs
        ActionBar.Tab playlistsTab = mActionBar.newTab().setText("Playlists").setTabListener(tabListener);
        mActionBar.addTab(playlistsTab);

        ActionBar.Tab songsTab = mActionBar.newTab().setText("Songs").setTabListener(tabListener);
        mActionBar.addTab(songsTab);

        ActionBar.Tab artistsTab = mActionBar.newTab().setText("Artists").setTabListener(tabListener);
        mActionBar.addTab(artistsTab);

        ActionBar.Tab albumsTab = mActionBar.newTab().setText("Albums").setTabListener(tabListener);
        mActionBar.addTab(albumsTab);
    }

    @Override
    protected void onPostCreate (Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        mDrawerToggle.syncState();
    }

    @Override
    public boolean onOptionsItemSelected (MenuItem item) {
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * SongsFragment.OnContextMenuItemClicked implementation
     */
    @Override
    public void onQueueSongItemClicked (Song song) {
        // Build queue message
        Message msg = Message.obtain(null, PlaybackService.MSG_ADD_TO_QUEUE);
        msg.replyTo = mMessenger;

        // Build the data
        Bundle data = new Bundle();
        data.putParcelable("Song", song);

        // Attach the data to the message
        msg.setData(data);

        // Send message
        try {
            mPlaybackService.send(msg);
        }
        catch (RemoteException e) {
            // Service has crashed
        }
    }

    /**
     * SongsFragment.OnListItemClicked implementation
     */
    @Override
    public void onListItemClicked (Song song) {
        // Build play message
        Message msg = Message.obtain(null, PlaybackService.MSG_PLAY);
        msg.replyTo = mMessenger;

        // Build the data
        Bundle data = new Bundle();
        data.putParcelable("Song", song);

        // Attach the data to the message
        msg.setData(data);

        // Send message
        try {
            mPlaybackService.send(msg);
        }
        catch (RemoteException e) {
            // Service has crashed
        }
    }

    /**
     * PlayerFragment.OnPlayerControlClickedListener
     */
    @Override
    public void onPlayPauseControlClicked () {
        // Build play / pause message
        Message msg = Message.obtain(null, PlaybackService.MSG_PLAY_PAUSE);
        msg.replyTo = mMessenger;

        // Send message
        try {
            mPlaybackService.send(msg);
        }
        catch (RemoteException e) {
            // Service has crashed
        }
    }

    /**
     * Bind to the playback service
     */
    private void doBindService () {
        bindService(new Intent(this, PlaybackService.class), mPlaybackServiceConnection, Context.BIND_AUTO_CREATE);
        mIsBound = true;
    }

    private void doUnbindService () {
        if (mIsBound) {

            if (mPlaybackService != null) {
                try {
                    // Build unregister message
                    Message msg = Message.obtain(null, PlaybackService.MSG_UNREGISTER_CLIENT);
                    msg.replyTo = mMessenger;

                    // Send unregister message
                    mPlaybackService.send(msg);
                }
                catch (RemoteException e) {
                    Log.i(TAG, "PlaybackService has crashed, nothing to do");
                }
            }

            // Detach our connection
            unbindService(mPlaybackServiceConnection);
            mIsBound = false;
        }
    }

    private ServiceConnection mPlaybackServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected (ComponentName componentName, IBinder iBinder) {
            mPlaybackService = new Messenger(iBinder);

            try {
                // Build register message
                Message msg = Message.obtain(null, PlaybackService.MSG_REGISTER_CLIENT);
                msg.replyTo = mMessenger;

                // Send register message
                mPlaybackService.send(msg);
            }
            catch (RemoteException e) {
                Log.i(TAG, "PlaybackService has crashed, nothing to do");
            }
        }

        @Override
        public void onServiceDisconnected (ComponentName componentName) {
            Log.i(TAG, "PlaybackService unexpectedly disconnected, it probably crashed");
            mPlaybackService = null;
        }
    };

    private final Messenger mMessenger = new Messenger(new IncomingHandler());

    /**
     * Handle messages from the PlaybackService
     */
    private class IncomingHandler extends Handler {
        @Override
        public void handleMessage (Message msg) {
            switch (msg.what) {
                case PlaybackService.MSG_IS_PLAYING:
                    if (msg.getData() != null) {
                        Song song = msg.getData().getParcelable("Song");

                        doUpdatePlayer(song);
                    }
                    break;
                case PlaybackService.MSG_IS_PAUSED:
                    Log.i(TAG, "Playback is paused");
                    doUpdatePlayerButton(true);
                    break;
                case PlaybackService.MSG_IS_RESUMED:
                    Log.i(TAG, "Playback is resumed");
                    doUpdatePlayerButton(false);
                    break;
                default:
                    super.handleMessage(msg);
                    break;
            }
        }
    }

    private void doUpdatePlayer (Song song) {
        FragmentManager fm = getSupportFragmentManager();
        Fragment fragment = fm.findFragmentById(R.id.music_browser_player_fragment);

        TextView songTitle = (TextView) fragment.getView().findViewById(R.id.player_song_title);
        songTitle.setText(song.getTitle());

        TextView artist = (TextView) fragment.getView().findViewById(R.id.player_artist);
        artist.setText(song.getArtist());

        // Show the player if it is currently hidden
        if (mIsPlayerHidden) {
            fm.beginTransaction()
                    .show(fragment)
                    .commit();
        }

        // Update the state of the play/pause button
        doUpdatePlayerButton(false);
    }

    private void doUpdatePlayerButton (boolean paused) {
        FragmentManager fm = getSupportFragmentManager();
        Fragment fragment = fm.findFragmentById(R.id.music_browser_player_fragment);

        ImageButton playPauseButton = (ImageButton) fragment.getView().findViewById(R.id.player_play_pause);

        if (paused) {
            playPauseButton.setImageResource(android.R.drawable.ic_media_play);
        }
        else {
            playPauseButton.setImageResource(android.R.drawable.ic_media_pause);
        }
    }

    private class DrawerItemCickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick (AdapterView<?> adapterView, View view, int position, long id) {
            switch (position) {
                case 0:
                    Log.i(TAG, "Now playing was clicked");
                    break;
                case 1:
                    Log.i(TAG, "Equalizer was clicked");
                    break;
                default:
                    break;
            }
        }
    }
}
