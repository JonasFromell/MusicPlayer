package com.jonasfromell.android.musicplayer;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Jonas on 8/13/14.
 */
public class SongsFragment extends ListFragment {
    private static final String TAG = "SongsFragment";

    private static final int CONTEXTMENU_ADD_TO_QUEUE = 1;

    private OnContextMenuItemClicked mCallback;

    public interface OnContextMenuItemClicked {
        public void onQueueSongItemClicked (Song song);
    }

    @Override
    public void onAttach (Activity activity) {
        super.onAttach(activity);

        // Make sure the container activity has implemented
        // the callback interface.
        try {
            mCallback = (OnContextMenuItemClicked) activity;
        }
        catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement OnContextMenuItemClicked listener");
        }
    }

    @Override
    public void onActivityCreated (Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // Register the list view for context menus
        registerForContextMenu(getListView());
    }

    @Override
    public View onCreateView (LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Set the adapter
        SongsAdapter adapter = new SongsAdapter(MusicRetriever.getSongs(getActivity()));
        setListAdapter(adapter);

        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onListItemClick (ListView l, View v, int position, long id) {
        Log.i(TAG, "Begin playing song");
    }

    @Override
    public void onCreateContextMenu (ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);

        menu.add(Menu.NONE, CONTEXTMENU_ADD_TO_QUEUE, 0, "Add to queue");
    }

    @Override
    public boolean onContextItemSelected (MenuItem item) {
        // Get the list item info
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();

        // Get the song that was clicked
        Song song = ((SongsAdapter) getListAdapter()).getItem(info.position);

        switch (item.getItemId()) {
            case CONTEXTMENU_ADD_TO_QUEUE:
                mCallback.onQueueSongItemClicked(song);
                break;
            default:
                break;
        }

        return true;
    }

    private class SongsAdapter extends ArrayAdapter<Song> {

        public SongsAdapter (ArrayList<Song> songs) {
            super(getActivity(), 0, songs);
        }

        @Override
        public View getView (int position, View convertView, ViewGroup parent) {
            // Inflate the view with our custom list item
            if (convertView == null) {
                convertView = getActivity().getLayoutInflater().inflate(R.layout.list_item_song, null);
            }

            // Get a reference to the item
            Song s = getItem(position);

            // Set the views
            TextView title = (TextView) convertView.findViewById(R.id.song_title_text);
            title.setText(s.getTitle());

            TextView artistAlbum = (TextView) convertView.findViewById(R.id.song_artist_album_text);
            artistAlbum.setText(getResources().getString(R.string.artist_album, s.getArtist(), s.getAlbum()));

            // Return the inflated view
            return convertView;
        }
    }
}
