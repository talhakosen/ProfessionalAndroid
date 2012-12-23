package com.paad.services;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

public class MyMusicService extends Service {
  
  public final static String PLAY_ALBUM = "com.paaad.action.PLAY_ALBUM";
  public final static String ALBUM_NAME_EXTRA = "ALBUM_NAME_EXTRA";
  public final static String ARTIST_NAME_EXTRA = "ARTIST_NAME_EXTRA"; 

  @Override
  public void onCreate() {
    // TODO: Actions to perform when service is created.
  }

  @Override
  public int onStartCommand(Intent intent, int flags, int startId) {
    startBackgroundTask(intent, startId);
    return Service.START_STICKY;
  }
  
  private void  startBackgroundTask(Intent intent, int startId) {
    // TODO Start a background thread and begin the processing.
  }
  
  /**
   * Listing 9-6: Implementing binding on a Service
   */
  @Override
  public IBinder onBind(Intent intent) {
    return binder;
  }

  public class MyBinder extends Binder {
    MyMusicService getService() {
      return MyMusicService.this;
    }
  }
  private final IBinder binder = new MyBinder();
  
  /**
   * Listing 9-9: Moving a Service to the foreground
   */
  private void startPlayback(String album, String artist) {
    int NOTIFICATION_ID = 1;

    // Create an Intent that will open the main Activity
    // if the notification is clicked.
    Intent intent = new Intent(this, MyActivity.class);
    PendingIntent pi = PendingIntent.getActivity(this, 1, intent, 0);

    // Set the Notification UI parameters
    Notification notification = new Notification(R.drawable.icon,
      "Starting Playback", System.currentTimeMillis());
    notification.setLatestEventInfo(this, album, artist, pi);

    // Set the Notification as ongoing
    notification.flags = notification.flags |
                         Notification.FLAG_ONGOING_EVENT;

    // Move the Service to the Foreground
    startForeground(NOTIFICATION_ID, notification);
  }
  
  /**
   * Listing 9-10: Moving a Service back to the background
   */
  public void pausePlayback() {
    // Move to the background and remove the Notification
    stopForeground(true);
  }


}