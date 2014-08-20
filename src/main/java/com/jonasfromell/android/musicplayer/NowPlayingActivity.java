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
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;

/**
 * Created by Jonas on 8/19/14.
 */
public class NowPlayingActivity extends ActionBarActivity implements NowPlayingFragment.OnPlayerControlClickedListener {
    private static final String TAG = "NowPlayingActivity";

    private Messenger mPlaybackService;
    private boolean mIsBound;

    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_now_playing);

        doBindService();
    }

    @Override
    protected void onDestroy () {
        super.onDestroy();

        doUnbindService();
    }

    private void doAttachFragment () {
        FragmentManager fm = getSupportFragmentManager();
        Fragment fragment = fm.findFragmentById(R.id.now_playing_fragment_container);

        if (fragment == null) {
            fragment = new NowPlayingFragment();

            fm.beginTransaction()
                    .add(R.id.now_playing_fragment_container, fragment)
                    .commit();
        }
    }

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

            // Attach the fragment here, so that the service is available in the fragment as well.
            doAttachFragment();
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
    private static class IncomingHandler extends Handler {
        @Override
        public void handleMessage (Message msg) {
            switch (msg.what) {
                default:
                    super.handleMessage(msg);
            }
        }
    }

    /**
     * Implement the NowPlayingFragment.OnPlayerControlClicked interface
     */
    @Override
    public void onPlayPauseButtonClicked () {
        Log.i(TAG, "Play/pause button clicked");
        try {
            // TODO: Build MSG_PLAY_PAUSE
            Message msg = Message.obtain(null, PlaybackService.MSG_PLAY_PAUSE);
            msg.replyTo = mMessenger;
            // TODO: Send msg
            mPlaybackService.send(msg);
        }
        catch (RemoteException e) {
            Log.i(TAG, "PlaybackService has crashed, nothing to do");
        }
    }

    @Override
    public void onNextButtonClicked () {
        Log.i(TAG, "Next button clicked");
        try {
            // TODO: Build MSG_PLAY_NEXT
            Message msg = Message.obtain(null, PlaybackService.MSG_PLAY_NEXT);
            msg.replyTo = mMessenger;
            // TODO: Send msg
            mPlaybackService.send(msg);
        }
        catch (RemoteException e) {
            Log.i(TAG, "PlaybackService has crashed, nothing to do");
        }
    }

    @Override
    public void onPreviousButtonClicked () {
        Log.i(TAG, "Previous button clicked");
        try {
            // TODO: Build MSG_PLAY_PREVIOUS
            Message msg = Message.obtain(null, PlaybackService.MSG_PLAY_PREVIOUS);
            msg.replyTo = mMessenger;
            // TODO: Send msg
            mPlaybackService.send(msg);
        }
        catch (RemoteException e) {
            Log.i(TAG, "PlaybackService has crashed, nothing to do");
        }
    }
}
