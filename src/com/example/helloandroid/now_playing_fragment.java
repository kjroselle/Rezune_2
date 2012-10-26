package com.example.helloandroid;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ToggleButton;

public class now_playing_fragment extends Fragment {
	
	npBarFragListener mCallback;
	ImageButton btnPrev;
	ToggleButton btnPlay;
	ImageButton btnNext;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.now_playing_fragment,
				container, false);
//		btnPrev = (Button)getActivity().findViewById(R.id.npprev);
//		btnPrev.setOnClickListener(new View.OnClickListener() {
//            public void onClick(View v) {
//                // Perform action on click
//            }
//        });
//		btnNext = (Button)getActivity().findViewById(R.id.npnext);
//		btnNext.setOnClickListener(new View.OnClickListener() {
//            public void onClick(View v) {
//                // Perform action on click
//            }
//        });
	    return view;
	}
	  
	@Override
	public void onStart() {
		btnPlay = (ToggleButton)getActivity().findViewById(R.id.npplay);
		btnNext = (ImageButton)getActivity().findViewById(R.id.npnext);
		btnPrev = (ImageButton)getActivity().findViewById(R.id.npprev);
		btnNext.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Perform action on click
            	mCallback.onNext();
            }
        });
		btnPrev.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Perform action on click
            	mCallback.onPrev();
            }
        });
		btnPlay.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Perform action on click
            	mCallback.onPlayPause();
            }
        });
//		btnPlay.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//		    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//		    	mCallback.onPlayPause();
//		        if (isChecked) {
//		            // The toggle is enabled
//		        } else {
//		            // The toggle is disabled
//		        }
//		    }
//		});
		super.onStart();
	}
	
	// Container Activity must implement this interface
	public interface npBarFragListener {
		public void onPlayPause();
		public void onNext();
	    public void onPrev();
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
	     
	// This makes sure that the container activity has implemented
	// the callback interface. If not, it throws an exception
		try {
		mCallback = (npBarFragListener) activity;
		} catch (ClassCastException e) {
			throw new ClassCastException(activity.toString()
				+ " must implement OnHeadlineSelectedListener");
		}
	}
	
	public void setPlaying(boolean playing) {
		btnPlay.setChecked(playing);
	}
}