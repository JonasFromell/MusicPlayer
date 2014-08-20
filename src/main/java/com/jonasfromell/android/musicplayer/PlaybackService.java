package com.jonasfromell.android.musicplayer;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.Messenger;
import android.util.Log;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by Jonas on 8/19/14.
 */
public class PlaybackService extends Service {
    private static final String TAG = "PlaybackService";

    // AudioManager to request audio focus for playback
    private AudioManager mAudioManager;

    // The MediaPlayer
    private MediaPlayer mMediaPlayer;

    // The queue
    private Queue mQueue;
    private int mCurrentQueuePosition;

    // Track the current song
    private Song mCurrentSong;

    // Keep track of the registered clients
    private ArrayList<Messenger> mClients = new ArrayList<Messenger>();

    // Message codes
    static final int MSG_REGISTER_CLIENT = 1;
    static final int MSG_UNREGISTER_CLIENT = 2;

    static final int MSG_PLAY_PAUSE = 3;
    static final int MSG_PLAY_NEXT = 4;
    static final int MSG_PLAY_PREVIOUS = 5;

    static final int MSG_ADD_TO_QUEUE = 6;

    // This is what we publish to the client
    final Messenger mMessenger = new Messenger(new IncomingHandler());

    // Handle incoming messages here
    class IncomingHandler extends Handler {
        @Override
        public void handleMessage (Message msg) {
            Log.i(TAG, "Recieved message");

            switch (msg.what) {
                // Client registration
                case MSG_REGISTER_CLIENT:
                    mClients.add(msg.replyTo);
                    break;
                case MSG_UNREGISTER_CLIENT:
                    mClients.remove(msg.replyTo);
                    break;
                // Music control
                case MSG_PLAY_PAUSE:
                    Log.i(TAG, "Handling PLAY_PAUSE");
                    toggle();
                    break;
                case MSG_PLAY_NEXT:
                    Log.i(TAG, "Handling PLAY_NEXT");
                    next();
                    break;
                case MSG_PLAY_PREVIOUS:
                    Log.i(TAG, "Handling PLAY_PREVIOUS");
                    previous();
                    break;
                case MSG_ADD_TO_QUEUE:
                    Log.i(TAG, "Handling ADD_TO_QUEUE");
                    // Get the song out of the message
                    Song song = (Song) msg.getData().getParcelable("Song");

                    // Add the song to the queue
                    addToQueue(song);

                    break;
                // Default
                default:
                    super.handleMessage(msg);
            }
        }
    }

    @Override
    public IBinder onBind (Intent intent) {
        return mMessenger.getBinder();
    }

    @Override
    public void onCreate () {
        super.onCreate();

        Log.i(TAG, "Service created");

        // Initialize the audio manager
        mAudioManager = (AudioManager) getBaseContext().getSystemService(Context.AUDIO_SERVICE);

        // Initialize the media player
        mMediaPlayer = new MediaPlayer();
        mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);

        // Set media player event listeners
        mMediaPlayer.setOnPreparedListener(mOnPreparedListener);
        mMediaPlayer.setOnCompletionListener(mOnCompletionListener);
        mMediaPlayer.setOnErrorListener(mOnErrorListener);

        // Initialize a new queue
        mQueue = new Queue();
    }

    @Override
    public int onStartCommand (Intent intent, int flags, int startId) {
        Log.i(TAG, "Received start id: " + startId);

        // We want to run this service until it is explicitly stopped
        return START_STICKY;
    }

    // Music control

    /**
     * Toggles the play or pause state
     */
    private void toggle () {
        if (mMediaPlayer.isPlaying()) {
            pause();
        }
        else {
            play();
        }
    }

    /**
     * Resumes playback
     */
    private void play () {
        // We don't wanna call start() if currently playing
        if (!mMediaPlayer.isPlaying()) {
            mMediaPlayer.start();
        }
    }

    /**
     * Starts playback
     *
     * @param song
     */
    private void play (Song song) {
        // Reset the media player
        mMediaPlayer.reset();

        // Try to set the data source
        try {
            mMediaPlayer.setDataSource(song.getFilepath());
        }
        catch (IOException e) {
            Log.e(TAG, "Couldn't read file");
        }

        // Store the current song
        mCurrentSong = song;

        // Store the current position in the queue
        mCurrentQueuePosition = mQueue.indexOf(song);

        // Prepare the media player
        mMediaPlayer.prepareAsync();
    }

    /**
     * Pauses playback
     */
    private void pause () {
        mMediaPlayer.pause();
    }

    /**
     * Stops playback
     */
    private void stop () {
        mMediaPlayer.stop();
    }

    /**
     * Plays the next track
     */
    private void next () {
        Song nextSong = mQueue.getNext(mCurrentQueuePosition);

        // If no next song is found, stop playback
        if (nextSong != null) {
            play(nextSong);
        }
        else {
            stop();
        }
    }

    /**
     * Plays the previous track
     */
    private void previous () {
        Song previousSong = mQueue.getPrevious(mCurrentQueuePosition);

        // If no previous song is found, just replay the current song
        if (previousSong != null) {
            play(previousSong);
        }
        else {
            replay();
        }
    }

    /**
     * Re-plays the current track
     */
    private void replay () {
        play(mCurrentSong);
    }

    // Manage queue

    /**
     * Adds a track to the queue
     *
     * @param song
     */
    private void addToQueue (Song song) {
        mQueue.add(song);
    }

    /**
     * Adds several tracks to the queue
     *
     * @param songs
     */
    private void addToQueue (ArrayList<Song> songs) {
        mQueue.add(songs);
    }

    /**
     * Remove track from the queue
     *
     * @param song
     */
    private void removeFromQueue (Song song) {
        mQueue.remove(song);
    }

    /**
     * MediaPlayer.OnComplete listener
     */
    private MediaPlayer.OnCompletionListener mOnCompletionListener = new MediaPlayer.OnCompletionListener() {
        @Override
        public void onCompletion (MediaPlayer mediaPlayer) {
            Log.i(TAG, "MediaPlayer completed playback");

            // Play the next song in the queue
            next();
        }
    };

    /**
     * MediaPlayer.OnError listener
     */
    private MediaPlayer.OnErrorListener mOnErrorListener = new MediaPlayer.OnErrorListener() {
        @Override
        public boolean onError (MediaPlayer mediaPlayer, int i, int i2) {
            Log.i(TAG, "MediaPlayer threw an error");
            return false;
        }
    };

    private MediaPlayer.OnPreparedListener mOnPreparedListener = (new MediaPlayer.OnPreparedListener() {
        @Override
        public void onPrepared (MediaPlayer mediaPlayer) {
            Log.i(TAG, "MediaPlayer is prepared");

            mediaPlayer.start();
        }
    });

    /**
     * Listen for changes in audio focus
     */
    private AudioManager.OnAudioFocusChangeListener mAudioFocusChangeListener = new AudioManager.OnAudioFocusChangeListener() {
        @Override
        public void onAudioFocusChange (int i) {
            if (i == AudioManager.AUDIOFOCUS_LOSS_TRANSIENT) {
                pause();
            }
            else if (i == AudioManager.AUDIOFOCUS_GAIN) {
                play();
            }
            else if (i == AudioManager.AUDIOFOCUS_LOSS) {
                mAudioManager.abandonAudioFocus(mAudioFocusChangeListener);

                stop();
            }
        }
    };

    /**
     * Intent filter for the AUDIO_BECOMING_NOISY broadcast
     */
    private IntentFilter mNoisyAudioIntentFilter = new IntentFilter(AudioManager.ACTION_AUDIO_BECOMING_NOISY);

    /**
     * Listen for the AUDIO_BECOMING_NOISY broadcast and pause playback if it is.
     */
    private class NoisyAudioStreamReceiver extends BroadcastReceiver {
        @Override
        public void onReceive (Context context, Intent intent) {
            if (AudioManager.ACTION_AUDIO_BECOMING_NOISY.equals(intent.getAction())) {
                pause();
            }
        }
    }
}
