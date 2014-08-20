package com.jonasfromell.android.musicplayer;

import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

/**
 * Created by Jonas on 8/13/14.
 */
public class PlaylistsFragment extends ListFragment {

    private static final String mPlaylists[] = new String[] {
        "Summer Hits 1999",
        "Best of Rock",
        "Winter is comming"
    };

    @Override
    public View onCreateView (LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, mPlaylists);
        setListAdapter(adapter);

        return super.onCreateView(inflater, container, savedInstanceState);
    }
}
