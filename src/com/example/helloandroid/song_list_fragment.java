package com.example.helloandroid;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import android.app.ListFragment;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.app.LoaderManager;

//class Mp3Filter implements FilenameFilter 
//{
//    public boolean accept(File dir, String name)
//    {
//        return (name.endsWith(".mp3"));
//    }
//}

public class song_list_fragment extends ListFragment implements LoaderManager.LoaderCallbacks<Cursor> {
	private static final String MEDIA_PATH = new String("/mnt/sdcard/Music/");
	private List<String> songs = new ArrayList<String>();
	private static final int SONG_LIST_LOADER = 0x01;
	private SimpleCursorAdapter adapter;
//	***
//	Some junk
	Cursor music_cursor;
	private int music_count;
	private int music_column_index;
//	***
	private MediaPlayer mp = new MediaPlayer();
	private int currentPosition = -1;
	private long cur_song_id;
	private enum repeat_enum {
		R_NONE, R_ONE, R_ALL
	};
	
	private repeat_enum repeat_opt = repeat_enum.R_ALL;
	
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		String[] uiBindFrom = { MediaStore.Audio.Media.TITLE, MediaStore.Audio.Media.ARTIST, MediaStore.Audio.Media.ALBUM};
        int[] uiBindTo = { R.id.title, R.id.artist, R.id.album};

        getLoaderManager().initLoader(SONG_LIST_LOADER, null, this);

        adapter = new SimpleCursorAdapter(
                getActivity().getApplicationContext(), R.layout.song_item,
                null, uiBindFrom, uiBindTo,
                CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);

        setListAdapter(adapter);
//		updateSongList();
	}
	
	public interface onSongSelectListener {
		public void onSongSelect(Uri songUri);
	}
	
	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		Log.d("KJ_LOG", "clickeditem");
		Log.d("KJ_LOG", "position: " + position);
		// possible problem if currentPosition == position but mp not in paused/started state
		if(currentPosition == position) {
    		pauseResumeSong();
    	} else {
			currentPosition = position;
			playSong(id);
    	}
//    	if(currentPosition == position) {
//    		pauseResumeSong();
//    	} else {
//			currentPosition = position;
//			playSong(songs.get(position));
//    	}
	}
	
	public void updateSongList() {
        File music_dir = new File(MEDIA_PATH);
        if (music_dir.exists()) {
	        if (music_dir.listFiles(new Mp3Filter()).length > 0) {
                for (File file : music_dir.listFiles(new Mp3Filter())) {
                    songs.add(file.getName());
                    songs.add(file.getName());
                    songs.add(file.getName());
                    songs.add(file.getName());
                    songs.add(file.getName());
                    songs.add(file.getName());
                    songs.add(file.getName());
                    songs.add(file.getName());
                    songs.add(file.getName());
                    songs.add(file.getName());
                    songs.add(file.getName());
                    songs.add(file.getName());
                }
//                setListAdapter(ArrayAdapter.createFromResource(getActivity()
//                        .getApplicationContext(), songs,
//                        R.layout.song_item));
                ArrayAdapter<String> songList = new ArrayAdapter<String>(getActivity().getApplicationContext(),
                      R.layout.song_item, 
                      songs);
                setListAdapter(songList);
	        }
        }
    }
    
    // pauses mp if a song is playing, otherwise resumes
    private void pauseResumeSong() {
	    	if(mp.isPlaying()) {
	    		try {
	    			mp.pause();
		    	} catch (IllegalStateException e) {
		            Log.v(getString(R.string.app_name), e.getMessage());
		    	}
	    	} else {
	    		try {
	    			mp.start();
		    	} catch (IllegalStateException e) {
		            Log.v(getString(R.string.app_name), e.getMessage());
		    	}
	    	}
    	
    }
    private void playSong(long id) {
    	String songUri;
    	String proj[] = { MediaStore.Audio.Media.DATA };
        Cursor songCursor = getActivity().getContentResolver().query(
                Uri.withAppendedPath(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                        String.valueOf(id)), proj, null, null, null);
        if (songCursor.moveToFirst()) {
            songUri = songCursor.getString(0);
            Log.d("KJ_LOG", "songUri: " + songUri);
//            tutSelectedListener.onTutSelected(tutorialUrl);
            try {
        		mp.reset();
        		mp.setDataSource(songUri);
        		mp.prepare();
        		mp.start();
        		mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
    				
    				public void onCompletion(MediaPlayer mp) {
    					nextSong();
    					
    				}
    			});
        	} catch (IOException e) {
                Log.v(getString(R.string.app_name), e.getMessage());
        	}
        }
        songCursor.close();
    }
    private void playSong(String song_path) {
    	try {
    		mp.reset();
    		mp.setDataSource(song_path);
    		mp.prepare();
    		mp.start();
    		mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
				
				public void onCompletion(MediaPlayer mp) {
					nextSong();
					
				}
			});
    	} catch (IOException e) {
            Log.v(getString(R.string.app_name), e.getMessage());
    	}
    }
    private void nextSong() {
    	if (repeat_opt == repeat_enum.R_ONE) {
    		// keep playing the same song...
    		playSong(adapter.getItemId(currentPosition));
    	} else if (++currentPosition >= adapter.getCount()) {
            // Last song, reset position to start
            currentPosition = 0;
            
            // if repeat all is enabled keep playing
            if (repeat_opt == repeat_enum.R_ALL) {
        		playSong(adapter.getItemId(currentPosition));
            }
        } else {
	        // Play next song*
	        playSong(adapter.getItemId(currentPosition));
        }
    }
    
// LoaderManager.LoaderCallbacks<Cursor> methods:
    
//    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
    	String[] proj = { MediaStore.Audio.Media._ID,
    			MediaStore.Audio.Media.DATA,
    			MediaStore.Audio.Media.DISPLAY_NAME,
    			MediaStore.Audio.Media.TITLE,
    			MediaStore.Audio.Media.ARTIST,
    			MediaStore.Audio.Media.ALBUM};
    	 
        CursorLoader cursorLoader = new CursorLoader(getActivity(),
        		MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, proj, null, null, null);
        
        return cursorLoader;
    }
 
//    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
    	if(cursor != null)
        {
        	while(cursor.moveToNext())
        	{
        		Log.d("KJ_LOG", "ID: " + cursor.getString(0));
	            Log.d("KJ_LOG", "DATA: " + cursor.getString(1));
	            Log.d("KJ_LOG", "DISPLAY_NAME: " + cursor.getString(2)); 
	            Log.d("KJ_LOG", "TITLE: " + cursor.getString(3));
	            Log.d("KJ_LOG", "ARTIST: " + cursor.getString(4));
	            Log.d("KJ_LOG", "ALBUM: " + cursor.getString(5));
        	}
        }
    	adapter.swapCursor(cursor);
    }
 
//    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
    	adapter.swapCursor(null);
    }
}