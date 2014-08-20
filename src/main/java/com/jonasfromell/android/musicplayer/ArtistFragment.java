package com.jonasfromell.android.musicplayer;

import android.os.Bundle;
import android.support.v4.app.Fragment;
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
public class ArtistFragment extends Fragment {
    private static final String TAG = "ArtistFragment";

    private static final String ARG_ARTIST_ID = "ARTIST_ID";

    private Artist mArtist;

    public static ArtistFragment newInstance (long ID) {
        // Create a new bundle with the ID argument
        Bundle args = new Bundle();
        args.putLong(ARG_ARTIST_ID, ID);

        // Create a new instance with the created bundle
        ArtistFragment artistFragment = new ArtistFragment();
        artistFragment.setArguments(args);

        return artistFragment;
    }

    @Override
    public void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle args = getArguments();

        if (args != null) {
            long ID = args.getLong(ARG_ARTIST_ID, -1);

            if (ID != -1) {
                mArtist = MusicRetriever.getArtist(getActivity(), ID);
            }
        }
    }

    @Override
    public View onCreateView (LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_artist, container, false);

        TextView name = (TextView) v.findViewById(R.id.artist_name_text);
        name.setText(mArtist.getName());

        TextView numAlbums = (TextView) v.findViewById(R.id.artist_num_albums);
        numAlbums.setText(mArtist.getNumAlbums());

        TextView numSongs = (TextView) v.findViewById(R.id.artist_num_songs);
        numSongs.setText(mArtist.getNumSongs());

        ListView artistAlbums = (ListView) v.findViewById(R.id.artist_album_list);
        artistAlbums.setAdapter(new ArtistAlbumsAdapter(MusicRetriever.getArtistAlbums(getActivity(), mArtist.getName())));

        ListView artistSongs = (ListView) v.findViewById(R.id.artist_song_list);
        artistSongs.setAdapter(new ArtistSongsAdapter(MusicRetriever.getArtistSongs(getActivity(), mArtist.getName())));

        return v;
    }

    private class ArtistAlbumsAdapter extends ArrayAdapter<Album> {

        public ArtistAlbumsAdapter (ArrayList<Album> albums) {
            super(getActivity(), 0, albums);
        }

        @Override
        public View getView (int position, View convertView, ViewGroup parent) {
            // Inflate the view with our custom list item
            if (convertView == null) {
                convertView = getActivity().getLayoutInflater().inflate(R.layout.list_item_artist_album, null);
            }

            // Get a reference to the item
            Album a = getItem(position);

            // Set the views
            ImageView cover = (ImageView) convertView.findViewById(R.id.list_artist_album_cover_image);
            cover.setImageBitmap(a.getCover());

            TextView title = (TextView) convertView.findViewById(R.id.list_artist_album_name_text);
            title.setText(a.getName());

            // Return the inflated view
            return convertView;
        }
    }

    private class ArtistSongsAdapter extends ArrayAdapter<Song> {

        public ArtistSongsAdapter (ArrayList<Song> songs) {
            super(getActivity(), 0, songs);
        }

        @Override
        public View getView (int position, View convertView, ViewGroup parent) {
            // Inflate the view with our custom list item
            if (convertView == null) {
                convertView = getActivity().getLayoutInflater().inflate(R.layout.list_item_artist_song, null);
            }

            // Get a reference to the item
            Song s = getItem(position);

            // Set the views
            TextView title = (TextView) convertView.findViewById(R.id.list_artist_song_title_text);
            title.setText(s.getTitle());

            TextView album = (TextView) convertView.findViewById(R.id.list_artist_song_album_text);
            title.setText(s.getAlbum());

            // Return the inflated view
            return convertView;
        }
    }
}
