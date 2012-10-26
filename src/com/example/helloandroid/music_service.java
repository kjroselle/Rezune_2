package com.example.helloandroid;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

public class music_service extends Service {
	
	private NotificationManager notificationMgr;
	
	@Override
	public void onCreate() {
		super.onCreate();
		Log.d("KJ_LOG", "music_service: onCreate()");
		notificationMgr = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
		displayNotificationMessage("background service onCreate");
		
	}
	
	@Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // TBD
		Log.d("KJ_LOG", "music_service: onStartCommand()");
        return Service.START_FLAG_REDELIVERY;
    }
 
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
    
    @Override
    public void onDestroy() {
    	notificationMgr.cancelAll();
    	super.onDestroy();
    }
    
    private void displayNotificationMessage(String message) {
    	Notification.Builder builder = new Notification.Builder(getApplicationContext());
    	Notification noti = builder
    		.setContentTitle("Music")
    	    .setContentText(message)
    	    .setSmallIcon(R.drawable.ic_launcher)
    	    .getNotification();
    	noti.flags |= Notification.FLAG_NO_CLEAR;
//    	PendingIntent contentIntent = PendingIntent.getActivity(this,0,new Intent(this, song_list_activity.class),0);
//    	noti.setLatestEventInfo(this, "bgTAG", message, contentIntent);
    	notificationMgr.notify(0, noti);
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