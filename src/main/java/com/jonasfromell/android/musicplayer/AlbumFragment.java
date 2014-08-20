package com.jonasfromell.android.musicplayer;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Jonas on 8/14/14.
 */
public class AlbumFragment extends Fragment {
    private static final String TAG = "AlbumFragment";

    private static final String ARG_ALBUM_ID = "ALBUM_ID";

    private Album mAlbum;

    public static AlbumFragment newInstance (long ID) {
        // Create a new bundle with the ID argument
        Bundle args = new Bundle();
        args.putLong(ARG_ALBUM_ID, ID);

        // Create a new instance with the created bundle
        AlbumFragment albumFragment = new AlbumFragment();
        albumFragment.setArguments(args);

        return albumFragment;
    }

    @Override
    public void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle args = getArguments();

        if (args != null) {
            long ID = args.getLong(ARG_ALBUM_ID, -1);

            if (ID != -1) {
                mAlbum = MusicRetriever.getAlbum(getActivity(), ID);
            }
        }
    }

    @Override
    public View onCreateView (LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_album, container, false);

        ImageView cover = (ImageView) v.findViewById(R.id.album_cover_image);
        cover.setImageBitmap(mAlbum.getCover());

        TextView name = (TextView) v.findViewById(R.id.album_name_text);
        name.setText(mAlbum.getName());

        TextView artist = (TextView) v.findViewById(R.id.album_artist_text);
        artist.setText(mAlbum.getArtist());

        // Populate the list view with the songs for this album
        ListView songs = (ListView) v.findViewById(R.id.album_songs_list);
        songs.setAdapter(new AlbumSongsAdapter(MusicRetriever.getSongsForAlbum(getActivity(), mAlbum.getName())));

        return v;
    }

    private class AlbumSongsAdapter extends ArrayAdapter<Song> {

        public AlbumSongsAdapter (ArrayList<Song> songs) {
            super(getActivity(), 0, songs);
        }

        @Override
        public View getView (int position, View convertView, ViewGroup parent) {
            // Inflate the view with our custom list item
            if (convertView == null) {
                convertView = getActivity().getLayoutInflater().inflate(R.layout.list_item_album_song, null);
            }

            // Get a reference to the item
            Song s = getItem(position);

            // Set the views
            TextView title = (TextView) convertView.findViewById(R.id.list_album_song_title_text);
            title.setText(s.getTitle());

            TextView artist = (TextView) convertView.findViewById(R.id.list_album_song_artist_text);
            artist.setText(s.getArtist());

            // Return the inflated view
            return convertView;
        }
    }
}
