package com.jonasfromell.android.musicplayer;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

/**
 * Created by Jonas on 8/21/14.
 */
public class PlayerFragment extends Fragment {

    private OnPlayerControlClickedListener mPlayerControlClickedListener;

    public interface OnPlayerControlClickedListener {
        public void onPlayPauseControlClicked ();
    }

    @Override
    public void onAttach (Activity activity) {
        super.onAttach(activity);

        // Make sure the container activity has implemented
        // the callback interface.
        try {
            mPlayerControlClickedListener = (OnPlayerControlClickedListener) activity;
        }
        catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement OnPlayerControlClickedListener");
        }
    }

    @Override
    public View onCreateView (LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_player, container, false);

        ImageButton playPauseButton = (ImageButton) v.findViewById(R.id.player_play_pause);
        playPauseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View view) {
                mPlayerControlClickedListener.onPlayPauseControlClicked();
            }
        });

        return v;
    }
}
