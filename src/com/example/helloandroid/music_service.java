package com.example.helloandroid;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class music_service extends Service {
	@Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // TBD
        return Service.START_FLAG_REDELIVERY;
    }
 
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
 
//    private class DownloaderTask extends AsyncTask<URL, Void, Boolean> {
// 
//        private static final String DEBUG_TAG = "TutListDownloaderService$DownloaderTask";
// 
//        @Override
//        protected Boolean doInBackground(URL... params) {
//            // TBD
//        }
// 
//        private boolean xmlParse(URL downloadPath) {
//            // TBD
//        }
// 
//    }
}