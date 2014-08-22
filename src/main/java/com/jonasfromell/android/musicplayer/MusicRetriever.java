package com.jonasfromell.android.musicplayer;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;

import java.util.ArrayList;

/**
 * Created by Jonas on 8/13/14.
 */
public class MusicRetriever {

    // Get all albums
    public static ArrayList<Album> getAlbums (Context c) {

        // Store the column indexes
        String[] cols = new String[] {
                MediaStore.Audio.Albums._ID,
                MediaStore.Audio.Albums.ARTIST,
                MediaStore.Audio.Albums.ALBUM,
                MediaStore.Audio.Albums.ALBUM_ART
        };

        // Sort by artist and date released
        String sort = MediaStore.Audio.Albums.ARTIST + ", " + MediaStore.Audio.Albums.FIRST_YEAR + " DESC";

        // Create an empty array of albums
        ArrayList<Album> albums = new ArrayList<Album>();

        // Initialize a cursor object for the albums
        ContentResolver resolver = c.getContentResolver();
        Uri URI = MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI;
        Cursor cursor = resolver.query(URI, cols, null, null, sort);

        // Check if there are any records
        if (cursor != null && cursor.moveToFirst()) {
            // Create a new 'Album' for every record
            do {
                albums.add(new Album(
                        cursor.getLong(0),
                        cursor.getString(1),
                        cursor.getString(2),
                        cursor.getString(3)
                ));
            } while (cursor.moveToNext());

            // Close the cursor
            cursor.close();
        }

        // Return the albums
        return albums;

    }

    // Get 'the' album
    public static Album getAlbum (Context c, long ID) {
        String lID = String.valueOf(ID);

        Album album = null;

        // Store the column indexes
        String[] cols = new String[] {
                MediaStore.Audio.Albums._ID,
                MediaStore.Audio.Albums.ARTIST,
                MediaStore.Audio.Albums.ALBUM,
                MediaStore.Audio.Albums.ALBUM_ART
        };

        // Build the arguments
        String[] args = new String[] {
                lID
        };

        ContentResolver resolver = c.getContentResolver();
        Uri URI = MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI;
        Cursor cursor = resolver.query(URI, cols, "_id=?", args, MediaStore.Audio.Albums.DEFAULT_SORT_ORDER);

        if (cursor != null && cursor.moveToFirst()) {
            album = new Album(
                    cursor.getLong(0),
                    cursor.getString(1),
                    cursor.getString(2),
                    cursor.getString(3)
            );

            // Close the cursor
            cursor.close();
        }

        return album;
    }

    public static ArrayList<Song> getSongsForAlbum (Context c, String album) {

        // Store the column indexes
        String[] cols = new String[] {
                MediaStore.Audio.Media._ID,
                MediaStore.Audio.Media.TITLE,
                MediaStore.Audio.Media.ARTIST,
                MediaStore.Audio.Media.ALBUM,
                MediaStore.Audio.Media.DATA
        };

        // Build arguments
        String[] args = new String[] {
                album
        };

        // Create an empty array of songs
        ArrayList<Song> songs = new ArrayList<Song>();

        // Initialize a cursor object for the songs
        ContentResolver resolver = c.getContentResolver();
        Uri URI = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        Cursor cursor = resolver.query(URI, cols, MediaStore.Audio.Media.ALBUM + "=?", args, MediaStore.Audio.Media.DEFAULT_SORT_ORDER);

        // Check if there are any records
        if (cursor != null && cursor.moveToFirst()) {
            // Create a new 'Song' for every record
            do {
                songs.add(new Song(
                        cursor.getLong(0),
                        cursor.getString(1),
                        cursor.getString(2),
                        cursor.getString(3),
                        cursor.getString(4)
                ));
            } while (cursor.moveToNext());

            // Close the cursor
            cursor.close();
        }

        // Return the songs
        return songs;
    }

    // Get all songs
    public static ArrayList<Song> getSongs (Context c) {

        // Store the column indexes
        String[] cols = new String[] {
                MediaStore.Audio.Media._ID,
                MediaStore.Audio.Media.TITLE,
                MediaStore.Audio.Media.ARTIST,
                MediaStore.Audio.Media.ALBUM,
                MediaStore.Audio.Media.DATA
        };

        // Create an empty array of songs
        ArrayList<Song> songs = new ArrayList<Song>();

        // Only grab what is considered music
        String selection = MediaStore.Audio.Media.IS_MUSIC + "!=0";

        // Sort by artist and album
        String sort = MediaStore.Audio.Media.ARTIST + ", " + MediaStore.Audio.Media.ALBUM + " ASC";

        // Initialize a cursor object for the songs
        ContentResolver resolver = c.getContentResolver();
        Uri URI = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        Cursor cursor = resolver.query(URI, cols, selection, null, sort);

        // Check if there are any records
        if (cursor != null && cursor.moveToFirst()) {
            // Create a new 'Song' for every record
            do {
                songs.add(new Song(
                        cursor.getLong(0),
                        cursor.getString(1),
                        cursor.getString(2),
                        cursor.getString(3),
                        cursor.getString(4)
                ));
            } while (cursor.moveToNext());

            // Close the cursor
            cursor.close();
        }

        // Return the songs
        return songs;

    }

    // Get all artists
    public static ArrayList<Artist> getArtists (Context c) {

        // Store the column indexes
        String[] cols = new String[] {
                MediaStore.Audio.Artists._ID,
                MediaStore.Audio.Artists.ARTIST,
                MediaStore.Audio.Artists.NUMBER_OF_TRACKS,
                MediaStore.Audio.Artists.NUMBER_OF_ALBUMS
        };

        // Create an empty array of artists
        ArrayList<Artist> artists = new ArrayList<Artist>();

        // Initialize a cursor object for the songs
        ContentResolver resolver = c.getContentResolver();
        Uri URI = MediaStore.Audio.Artists.EXTERNAL_CONTENT_URI;
        Cursor cursor = resolver.query(URI, cols, null, null, MediaStore.Audio.Artists.DEFAULT_SORT_ORDER);

        // Check if there are any records
        if (cursor != null && cursor.moveToFirst()) {
            // Create a new 'Artist' for every record
            do {
                artists.add(new Artist(
                        cursor.getLong(0),
                        cursor.getString(1),
                        cursor.getString(2),
                        cursor.getString(3)
                ));
            } while (cursor.moveToNext());

            // Close the cursor
            cursor.close();
        }

        // Return the artists
        return artists;
    }

    // Get 'the' artist
    public static Artist getArtist (Context c, long ID) {
        String lID = String.valueOf(ID);

        Artist artist = null;

        // Store the column indexes
        String[] cols = new String[] {
                MediaStore.Audio.Artists._ID,
                MediaStore.Audio.Artists.ARTIST,
                MediaStore.Audio.Artists.NUMBER_OF_TRACKS,
                MediaStore.Audio.Artists.NUMBER_OF_ALBUMS
        };

        // Build arguments
        String[] args = new String[] {
                lID
        };

        ContentResolver resolver = c.getContentResolver();
        Uri URI = MediaStore.Audio.Artists.EXTERNAL_CONTENT_URI;
        Cursor cursor = resolver.query(URI, cols, "_id=?", args, MediaStore.Audio.Artists.DEFAULT_SORT_ORDER);

        if (cursor != null && cursor.moveToFirst()) {
            artist = new Artist(
                    cursor.getLong(0),
                    cursor.getString(1),
                    cursor.getString(2),
                    cursor.getString(3)
            );

            // Close the cursor
            cursor.close();
        }

        return artist;
    }

    public static ArrayList<Song> getArtistSongs (Context c, String artist) {

        // Store the column indexes
        String[] cols = new String[] {
                MediaStore.Audio.Media._ID,
                MediaStore.Audio.Media.TITLE,
                MediaStore.Audio.Media.ARTIST,
                MediaStore.Audio.Media.ALBUM,
                MediaStore.Audio.Media.DATA
        };

        // Build arguments
        String[] args = new String[] {
                artist
        };

        // Sort by album
        String sort = MediaStore.Audio.Media.ALBUM + " ASC";

        // Create an empty array of songs
        ArrayList<Song> songs = new ArrayList<Song>();

        // Initialize a cursor object for the songs
        ContentResolver resolver = c.getContentResolver();
        Uri URI = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        Cursor cursor = resolver.query(URI, cols, "album=?", args, sort);

        // Check if there are any records
        if (cursor != null && cursor.moveToFirst()) {
            // Create a new 'Song' for every record
            do {
                songs.add(new Song(
                        cursor.getLong(0),
                        cursor.getString(1),
                        cursor.getString(2),
                        cursor.getString(3),
                        cursor.getString(4)
                ));
            } while (cursor.moveToNext());

            // Close the cursor
            cursor.close();
        }

        // Return the songs
        return songs;

    }

    public static ArrayList<Album> getArtistAlbums (Context c, String artist) {

        // Store the column indexes
        String[] cols = new String[] {
                MediaStore.Audio.Albums._ID,
                MediaStore.Audio.Albums.ARTIST,
                MediaStore.Audio.Albums.ALBUM,
                MediaStore.Audio.Albums.ALBUM_ART
        };

        // Build arguments
        String[] args = new String[] {
                artist
        };

        // Create an empty array of albums
        ArrayList<Album> albums = new ArrayList<Album>();

        // Initialize a cursor object for the albums
        ContentResolver resolver = c.getContentResolver();
        Uri URI = MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI;
        Cursor cursor = resolver.query(URI, cols, "artist=?", args, MediaStore.Audio.Albums.DEFAULT_SORT_ORDER);

        // Check if there are any records
        if (cursor != null && cursor.moveToFirst()) {
            // Create a new 'Album' for every record
            do {
                albums.add(new Album(
                        cursor.getLong(0),
                        cursor.getString(1),
                        cursor.getString(2),
                        cursor.getString(3)
                ));
            } while (cursor.moveToNext());

            // Close the cursor
            cursor.close();
        }

        // Return the albums
        return albums;

    }
}
