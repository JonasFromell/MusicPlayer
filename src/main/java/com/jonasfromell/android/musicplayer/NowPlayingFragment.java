package com.jonasfromell.android.musicplayer;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

/**
 * Created by Jonas on 8/19/14.
 */
public class NowPlayingFragment extends Fragment {
    private OnPlayerControlClickedListener mCallback;

    public interface OnPlayerControlClickedListener {
        public void onPlayPauseButtonClicked ();
        public void onNextButtonClicked();
        public void onPreviousButtonClicked();
    }

    @Override
    public View onCreateView (LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_now_playing, container, false);

        // Setup on click listeners for the buttons
        ImageButton playPause = (ImageButton) v.findViewById(R.id.now_playing_play_pause_button);
        playPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View view) {
                mCallback.onPlayPauseButtonClicked();
            }
        });

        ImageButton playNext = (ImageButton) v.findViewById(R.id.now_playing_next_button);
        playNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View view) {
                mCallback.onNextButtonClicked();
            }
        });

        ImageButton playPrevious = (ImageButton) v.findViewById(R.id.now_playing_previous_button);
        playPrevious.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View view) {
                mCallback.onPreviousButtonClicked();
            }
        });

        return v;
    }

    @Override
    public void onAttach (Activity activity) {
        super.onAttach(activity);

        // Make sure the container activity has implemented
        // the callback interface.
        try {
            mCallback = (OnPlayerControlClickedListener) activity;
        }
        catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement OnPlayerControlClickedListener");
        }
    }
}
