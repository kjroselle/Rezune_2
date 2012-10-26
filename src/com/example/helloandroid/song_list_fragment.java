package com.example.helloandroid;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.ListFragment;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.app.LoaderManager;

//class Mp3Filter implements FilenameFilter 
//{
//    public boolean accept(File dir, String name)
//    {
//        return (name.endsWith(".mp3"));
//    }
//}

public class song_list_fragment extends ListFragment implements LoaderManager.LoaderCallbacks<Cursor> {
//	private List<String> songs = new ArrayList<String>();
	private static final int SONG_LIST_LOADER = 0x01;
	private SimpleCursorAdapter adapter;
	private songListListener mSongListListener;
	Cursor musicCursor;
	private MediaPlayer mp;
	private int currentPosition = 0;
	private enum repeat_enum {
		R_NONE, R_ONE, R_ALL
	};
	
	private repeat_enum repeat_opt = repeat_enum.R_ALL;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		Log.d("KJ_LOG", "song_list_fragment: onCreateView()");
		View view = inflater.inflate(R.layout.song_list_fragment, container, false);
		return view;
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		mp = new MediaPlayer();
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
	
	public interface songListListener {
//		public void onSongSelect(Uri songUri);
//		public void onSongSelect(int position);
		public void playingSong(boolean playing);
	}
	
	@Override
	public void onPause() {
		Log.d("KJ_LOG", "song_list_fragment: onPause()");
//		releasePlayer();
		mSongListListener.playingSong(false);
		if(mp.isPlaying()) {
			mp.pause();
		}
//		musicCursor.close();
		super.onPause();
	}
	
	@Override
	public void onResume() {
//		getLoaderManager().initLoader(SONG_LIST_LOADER, null, this);
		Log.d("KJ_LOG", "song_list_fragment: onResume()");
		super.onResume();
	}
	
	@Override
	public void onStop() {
		Log.d("KJ_LOG", "song_list_fragment: onStop()");
		super.onStop();
	}
	
	@Override
	public void onDestroy() {
		Log.d("KJ_LOG", "song_list_fragment: onDestroy()");
		super.onDestroy();
	}
	
	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		Log.d("KJ_LOG", "clickeditem");
		Log.d("KJ_LOG", "position: " + position);
		// possible problem if currentPosition == position but mp not in paused/started state
		if(currentPosition == position) {
    		pauseResumeSong();
    	} else {
    		// set now playing
//    		String np_title = (String)((TextView)(v.findViewById(R.id.title))).getText().toString();
//    		TextView text = (TextView) getActivity().findViewById(R.id.npitem);
//    		text.setText(np_title);
			currentPosition = position;
			playSong(id);
    	}
	}
	
	private void releasePlayer() {
		mp.release();
	}
    
    // pauses mp if a song is playing, otherwise resumes
    public void pauseResumeSong() {
	    	if(mp.isPlaying()) {
	    		// tell activity we are not playing a song now
	    		mSongListListener.playingSong(false);
	    		try {
	    			mp.pause();
		    	} catch (IllegalStateException e) {
		            Log.v(getString(R.string.app_name), e.getMessage());
		    	}
	    	} else {
	    		// tell activity we are playing a song now
	    		mSongListListener.playingSong(true);
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
    	Log.d("KJ_LOG", "songid: " + id);
    	mSongListListener.playingSong(true);
        Cursor songCursor = getActivity().getContentResolver().query(
                Uri.withAppendedPath(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                        String.valueOf(id)), proj, null, null, null);
        if (songCursor.moveToFirst()) {
            songUri = songCursor.getString(0);
            Log.d("KJ_LOG", "songUri: " + songUri);
            
	        // set now playing
            Cursor temp_c = (Cursor)adapter.getItem(currentPosition);
            String np_title = temp_c.getString(temp_c.getColumnIndex("title"));
            TextView text = (TextView) getActivity().findViewById(R.id.npitem);
            if(text != null)
            	text.setText(np_title);
            
            // get URI for song
            String tUri = temp_c.getString(1);
            
//	        View v = (View)adapter.getItem(currentPosition);
//	        String np_title = (String)((TextView)(v.findViewById(R.id.title))).getText().toString();
//			TextView text = (TextView) getActivity().findViewById(R.id.npitem);
//			text.setText(np_title);
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
    
    public void nextSong() {
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
	        // Play next song
	        playSong(adapter.getItemId(currentPosition));
        }
    }
    
    public void forceNextSong() {
    	if (++currentPosition >= adapter.getCount()) {
            // Last song, reset position to start
            currentPosition = 0;
        	playSong(adapter.getItemId(currentPosition));
        } else {
	        // Play next song
	        playSong(adapter.getItemId(currentPosition));
        }
    }
    
    public void prevSong() {
    	if (--currentPosition < 0) {
            // Last song, reset position to start
            currentPosition = adapter.getCount()-1;
        	playSong(adapter.getItemId(currentPosition));
        } else {
	        // Play prev song
	        playSong(adapter.getItemId(currentPosition));
        }
    }
    
    @Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
	     
	// This makes sure that the container activity has implemented
	// the callback interface. If not, it throws an exception
		try {
		mSongListListener = (songListListener) activity;
		} catch (ClassCastException e) {
			throw new ClassCastException(activity.toString()
				+ " must implement songListListener");
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
    	musicCursor = cursor;
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
    	Cursor temp_c = (Cursor)adapter.getItem(0);
        
        // get URI for song
        String tUri = temp_c.getString(1);
    	try {
    		String np_title = temp_c.getString(temp_c.getColumnIndex("title"));
            TextView text = (TextView) getActivity().findViewById(R.id.npitem);
            if(text != null)
            	text.setText(np_title);
    		mp.reset();
    		mp.setDataSource(tUri);
    		mp.prepare();
    		mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
				
				public void onCompletion(MediaPlayer mp) {
					nextSong();
					
				}
			});

    	} catch (IOException e) {
            Log.v(getString(R.string.app_name), e.getMessage());
    	}
    }
 
//    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
    	adapter.swapCursor(null);
    }
}