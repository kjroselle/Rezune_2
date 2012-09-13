package com.example.helloandroid;

import android.app.Activity;
import android.app.ListActivity;
import android.os.Bundle;

public class song_list_activity extends Activity {
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	 
	    setContentView(R.layout.song_list_fragment);
	}
}