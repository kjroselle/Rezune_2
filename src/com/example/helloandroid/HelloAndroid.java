package com.example.helloandroid;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import android.app.ListActivity;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

//public class HelloAndroid extends Activity {
//    /** Called when the activity is first created. */
//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.main);
////        Populate view in code
////        TextView tv = new TextView(this);
////        tv.setText("Hello, Android");
////        setContentView(tv);
//    }
//}

class Mp3Filter implements FilenameFilter 
{
    public boolean accept(File dir, String name)
    {
        return (name.endsWith(".mp3"));
    }
}

public class HelloAndroid extends ListActivity
{
	private static final String MEDIA_PATH = new String("/mnt/sdcard/Music/");
	private List<String> songs = new ArrayList<String>();
//	***
//	Some junk
	Cursor music_cursor;
	private int music_count;
	private int music_column_index;
//	***
	private MediaPlayer mp = new MediaPlayer();
	private int currentPosition = 0;
	private enum repeat_enum {
		R_NONE, R_ONE, R_ALL
	};
	
	private repeat_enum repeat_opt = repeat_enum.R_ALL;
	
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.songlist);
//		updateSongList();
		
//		***
//		junk
		System.gc();
        String[] proj = { MediaStore.Audio.Media._ID,
		MediaStore.Audio.Media.DATA,
		MediaStore.Audio.Media.DISPLAY_NAME,
		MediaStore.Audio.Media.TITLE,
		MediaStore.Audio.Media.ARTIST,
		MediaStore.Audio.Media.ALBUM};
        music_cursor = getContentResolver().query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
        		proj,  // columns to return for each row
        		null,  // selectionclause
        		null,  // selectionargs
        		null); // sort order
        // get the number of rows
        music_count = music_cursor.getCount();
        Log.d("KJ_LOG", "Number of rows: " + Integer.toString(music_count));
        
        // move to the first element, print stuff
        if(music_cursor != null)
        {
//            music_cursor.moveToFirst();
        	while(music_cursor.moveToNext())
        	{
        		Log.d("KJ_LOG", "ID: " + music_cursor.getString(0));
	            Log.d("KJ_LOG", "DATA: " + music_cursor.getString(1));
	            Log.d("KJ_LOG", "DISPLAY_NAME: " + music_cursor.getString(2)); 
	            Log.d("KJ_LOG", "TITLE: " + music_cursor.getString(3));
	            Log.d("KJ_LOG", "ARTIST: " + music_cursor.getString(4));
	            Log.d("KJ_LOG", "ALBUM: " + music_cursor.getString(5));
        	}
        }
        
//        SimpleCursorAdapter mAdapter = new SimpleCursorAdapter(this,
//                android.R.layout.simple_list_item_single_choice,
//                music_cursor,
//                new String[] {MediaStore.Audio.Media.TITLE},
//                new int[] { android.R.id.text1});
        
//        ArrayAdapter<String> songList = new ArrayAdapter<String>(this,
//                R.layout.song_item, songs);
//        setListAdapter(songList);
        
//        musiclist = (ListView) findViewById(R.id.PhoneMusicList);
//        musiclist.setAdapter(new MusicAdapter(getApplicationContext()));
//
//        musiclist.setOnItemClickListener(musicgridlistener);
//		***	
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
 
                ArrayAdapter<String> songList = new ArrayAdapter<String>(this,
                                R.layout.song_item, songs);
                setListAdapter(songList);
	        }
        }
    }
    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
    	// possible problem if currentPosition == position but mp not in paused/started state
    	if(currentPosition == position) {
    		pauseResumeSong();
    	} else {
			currentPosition = position;
			playSong(MEDIA_PATH + songs.get(position));
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
    		playSong(MEDIA_PATH + songs.get(currentPosition));
    	} else if (++currentPosition >= songs.size()) {
            // Last song, reset position to start
            currentPosition = 0;
            
            // if repeat all is enabled keep playing
            if (repeat_opt == repeat_enum.R_ALL) {
        		playSong(MEDIA_PATH + songs.get(currentPosition));
            }
        } else {
	        // Play next song*
	        playSong(MEDIA_PATH + songs.get(currentPosition));
        }
    }
}
