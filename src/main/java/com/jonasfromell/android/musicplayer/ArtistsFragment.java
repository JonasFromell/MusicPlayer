package com.jonasfromell.android.musicplayer;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Jonas on 8/13/14.
 *
 * Displays a list of all artists.
 */
public class ArtistsFragment extends ListFragment {

    @Override
    public View onCreateView (LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ArtistsAdapter adapter = new ArtistsAdapter(MusicRetriever.getArtists(getActivity()));
        setListAdapter(adapter);

        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onListItemClick (ListView l, View v, int position, long id) {
        // Get the clicked artist
        Artist artist = ((ArtistsAdapter) getListAdapter()).getItem(position);

        // Send the artists ID to the artist activity
        Intent i = new Intent(getActivity(), ArtistActivity.class);
        i.putExtra(ArtistActivity.EXTRA_ARTIST_ID, artist.getID());

        startActivity(i);
    }

    private class ArtistsAdapter extends ArrayAdapter<Artist> {

        public ArtistsAdapter (ArrayList<Artist> artists) {
            super(getActivity(), 0, artists);
        }

        @Override
        public View getView (int position, View convertView, ViewGroup parent) {
            // Inflate the view with our custom list item
            if (convertView == null) {
                convertView = getActivity().getLayoutInflater().inflate(R.layout.list_item_artist, null);
            }

            // Get a reference to the item
            Artist a = getItem(position);

            // Set the views
            TextView title = (TextView) convertView.findViewById(R.id.artist_name_text);
            title.setText(a.getName());

            TextView albums = (TextView) convertView.findViewById(R.id.artist_num_albums_songs);
            albums.setText(getResources().getString(R.string.albums_songs, a.getNumAlbums(), a.getNumSongs()));


            // Return the inflated view
            return convertView;
        }
    }
}
