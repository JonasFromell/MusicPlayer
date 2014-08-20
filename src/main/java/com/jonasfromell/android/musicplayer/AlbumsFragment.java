package com.jonasfromell.android.musicplayer;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Jonas on 8/13/14.
 */
public class AlbumsFragment extends ListFragment {

    @Override
    public View onCreateView (LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        AlbumsAdapter adapter = new AlbumsAdapter(MusicRetriever.getAlbums(getActivity()));
        setListAdapter(adapter);

        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onListItemClick (ListView l, View v, int position, long id) {
        // Get the clicked album
        Album album = ((AlbumsAdapter) getListAdapter()).getItem(position);

        // Send the albums ID to the album activity
        Intent i = new Intent(getActivity(), AlbumActivity.class);
        i.putExtra(AlbumActivity.EXTRA_ALBUM_ID, album.getID());

        startActivity(i);
    }

    private class AlbumsAdapter extends ArrayAdapter<Album> {

        public AlbumsAdapter (ArrayList<Album> albums) {
            super(getActivity(), 0, albums);
        }

        @Override
        public View getView (int position, View convertView, ViewGroup parent) {
            // Inflate the view with our custom list item
            if (convertView == null) {
                convertView = getActivity().getLayoutInflater().inflate(R.layout.list_item_album, null);
            }

            // Get a reference to the item
            Album a = getItem(position);

            // Set the views
            ImageView cover = (ImageView) convertView.findViewById(R.id.list_album_cover_image);
            cover.setImageBitmap(a.getCover());


            TextView name = (TextView) convertView.findViewById(R.id.list_album_name_text);
            name.setText(a.getName());

            TextView artist = (TextView) convertView.findViewById(R.id.list_album_artist_text);
            artist.setText(a.getArtist());

            // Return the inflated view
            return convertView;
        }
    }

}
