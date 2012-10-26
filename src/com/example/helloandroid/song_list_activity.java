package com.example.helloandroid;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Window;
import android.widget.SimpleCursorAdapter;

public class song_list_activity extends Activity implements now_playing_fragment.npBarFragListener, song_list_fragment.songListListener, LoaderManager.LoaderCallbacks<Cursor> {
	private static final int SONG_LIST_LOADER = 0x01;
	private SimpleCursorAdapter adapter;
	private MediaPlayer mp = new MediaPlayer();
	private int currentPosition = -1;
	Cursor music_cursor;
	private now_playing_fragment npBarFrag;
	private song_list_fragment songListFrag;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    
	    setContentView(R.layout.activity_main);
	    
	    Intent intent = new Intent(song_list_activity.this, music_service.class);
	    startService(intent);
	    npBarFrag = (now_playing_fragment)getFragmentManager().findFragmentById(R.id.nowPlayingFragment);
	    if(npBarFrag != null && npBarFrag.isInLayout()) {
	    	Log.d("KJ_LOG", "song_list_activity: found npBarFrag fragment");
	    }
	    
	    songListFrag = (song_list_fragment)getFragmentManager().findFragmentById(R.id.songListFragment);
	    if(songListFrag != null && songListFrag.isInLayout()) {
	    	Log.d("KJ_LOG", "song_list_activity: found songListFrag fragment");
	    }
//	    getLoaderManager().initLoader(SONG_LIST_LOADER, null, this);
	}
	
	@Override
	public void onPause() {
		Log.d("KJ_LOG", "song_list_activity: onPause()");
		super.onPause();
	}
	
	@Override
	public void onStop() {
		Log.d("KJ_LOG", "song_list_activity: onStop()");
		stopService();
		super.onStop();
	}
	
	@Override
	public void onDestroy() {
		stopService();
		super.onDestroy();
	}
	
	private void stopService() {
		Log.d("KJ_LOG", "song_list_activity: stopService()");
		if(stopService(new Intent(song_list_activity.this, music_service.class))) {
			Log.d("KJ_LOG", "song_list_activity: stopService() : success");
		} else {
			Log.d("KJ_LOG", "song_list_activity: stopService() : fail");
		}
	}
	
	// *********************************************
	// npBarFragListener
	// *********************************************
	
	public void onPlayPause() {
		// TODO Auto-generated method stub
		Log.d("KJ_LOG", "song_list_activity: onPlayPause()");
		songListFrag.pauseResumeSong();
	}
	
	public void onNext() {
		// TODO Auto-generated method stub
		Log.d("KJ_LOG", "song_list_activity: onNext()");
		songListFrag.forceNextSong();
	}

	public void onPrev() {
		// TODO Auto-generated method stub
		Log.d("KJ_LOG", "song_list_activity: onPrev()");
		songListFrag.prevSong();
	}

	// *********************************************
	// songListListener
	// *********************************************
	
	public void playingSong(boolean playing) {
		npBarFrag.setPlaying(playing);
	}
	
	// *********************************************
	// LoaderManager.LoaderCallbacks<Cursor> methods
	// *********************************************
	
	public Loader<Cursor> onCreateLoader(int id, Bundle args) {
		String[] proj = { MediaStore.Audio.Media._ID,
    			MediaStore.Audio.Media.DATA,
    			MediaStore.Audio.Media.DISPLAY_NAME,
    			MediaStore.Audio.Media.TITLE,
    			MediaStore.Audio.Media.ARTIST,
    			MediaStore.Audio.Media.ALBUM};
    	 
        CursorLoader cursorLoader = new CursorLoader(this,
        		MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, proj, null, null, null);
        
        return cursorLoader;
	}

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

	public void onLoaderReset(Loader<Cursor> arg0) {
		adapter.swapCursor(null);
	}

}