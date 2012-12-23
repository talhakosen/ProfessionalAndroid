package com.paad.notifications;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.os.Bundle;
import android.widget.RemoteViews;

public class MyActivity extends Activity {

  private static final String BUTTON_CLICK_ACTION = "com.paad.notifications.action.BUTTON_CLICK";
  private static final int NOTIFICATION_REF = 1;

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.main);
    
    /**
     * Listing 10-33: Using the Notification Manager
     */
    String svcName = Context.NOTIFICATION_SERVICE;

    NotificationManager notificationManager;
    notificationManager = (NotificationManager)getSystemService(svcName);
    
  }
  
  private Notification simpleNotification() {
    /**
     * Listing 10-34: Creating a Notification
     */
    // Choose a drawable to display as the status bar icon
    int icon = R.drawable.icon;
    // Text to display in the status bar when the notification is launched
    String tickerText = "Notification";
    // The extended status bar orders notification in time order
    long when = System.currentTimeMillis();

    Notification notification = new Notification(icon, tickerText, when);
    
    //
    return notification;
  }
  
  private Notification.Builder notificationBuilder() {
    /**
     * Listing 10-35: Setting Notification options using the Notification Builder
     */
    Notification.Builder builder = 
      new Notification.Builder(MyActivity.this);

    builder.setSmallIcon(R.drawable.ic_launcher)
           .setTicker("Notification")
           .setWhen(System.currentTimeMillis())
           .setDefaults(Notification.DEFAULT_SOUND |
                        Notification.DEFAULT_VIBRATE)
           .setSound(
              RingtoneManager.getDefaultUri(
                RingtoneManager.TYPE_NOTIFICATION))
           .setVibrate(new long[] { 1000, 1000, 1000, 1000, 1000 })
           .setLights(Color.RED, 0, 1);

    Notification notification = builder.getNotification();

    //
    return builder;
  }
  
  private Notification.Builder customLayoutNotification() {
    Notification.Builder builder = 
        new Notification.Builder(MyActivity.this);
    
    Intent newIntent = new Intent(BUTTON_CLICK_ACTION);
    PendingIntent pendingIntent = 
      PendingIntent.getBroadcast(MyActivity.this, 2, newIntent, 0);
    Bitmap myIconBitmap = null; // TODO Obtain Bitmap
        
    /**
     * Listing 10-36: Applying a custom layout to the Notification status window
     */
    builder.setSmallIcon(R.drawable.ic_launcher)
           .setTicker("Notification")
           .setWhen(System.currentTimeMillis())
           .setContentTitle("Title")
           .setContentText("Subtitle")
           .setContentInfo("Info")
           .setLargeIcon(myIconBitmap)
           .setContentIntent(pendingIntent);
    
    //
    return builder;
  }
  
  private Notification.Builder customNotificationWindowNotification() {
    Notification.Builder builder = 
        new Notification.Builder(MyActivity.this);

    /**
     * Listing 10-38: Applying a custom layout to the Notification status window
     */
    RemoteViews myRemoteView = 
      new RemoteViews(this.getPackageName(),
                      R.layout.my_notification_layout);

    builder.setSmallIcon(R.drawable.ic_launcher)
           .setTicker("Notification")
           .setWhen(System.currentTimeMillis())
           .setContentTitle("Progress")
           .setProgress(100, 50, false)
           .setContent(myRemoteView);
    
    //
    Notification notification = builder.getNotification();
    
    /**
     * Listing 10-39: Customizing your extended Notification window layout
     */
    notification.contentView.setImageViewResource(R.id.status_icon,
                                                  R.drawable.icon);
    notification.contentView.setTextViewText(R.id.status_text,
                                             "Current Progress:");
    notification.contentView.setProgressBar(R.id.status_progress,
                                            100, 50, false);

    
    /**
     * Listing 10-40: Adding click handlers to your customized extended Notification window layout
     */
    Intent newIntent = new Intent(BUTTON_CLICK_ACTION);
    PendingIntent newPendingIntent = 
      PendingIntent.getBroadcast(MyActivity.this, 2, newIntent, 0);

    notification.contentView.setOnClickPendingIntent(
      R.id.status_progress, newPendingIntent);
    
    /**
     * Listing 10-41: Applying a custom layout to the Notification ticker
     */
    RemoteViews myTickerView = 
      new RemoteViews(this.getPackageName(),
                      R.layout.my_ticker_layout);

    builder.setSmallIcon(R.drawable.notification_icon)
           .setTicker("Notification", myTickerView)
           .setWhen(System.currentTimeMillis())
           .setContent(myRemoteView);

    return builder;
  }
  
  private Notification.Builder onGoingNotification() {
    Notification.Builder builder = 
        new Notification.Builder(MyActivity.this);

    RemoteViews myRemoteView = 
      new RemoteViews(this.getPackageName(),
                      R.layout.my_notification_layout);
    
    /**
     * Listing 10-42: Setting an ongoing Notification
     */
    builder.setSmallIcon(R.drawable.notification_icon)
           .setTicker("Notification")
           .setWhen(System.currentTimeMillis())
           .setContentTitle("Progress")
           .setProgress(100, 50, false)
           .setContent(myRemoteView)
           .setOngoing(true);

    return builder;
  }
  
  private void triggerNotification(Notification.Builder builder) {
    /**
     * Listing 10-43: Triggering a Notification
     */
    String svc = Context.NOTIFICATION_SERVICE;

    NotificationManager notificationManager 
      = (NotificationManager)getSystemService(svc);

    int NOTIFICATION_REF = 1;
    Notification notification = builder.getNotification();

    notificationManager.notify(NOTIFICATION_REF, notification);
  }
  
  private Notification.Builder autoCancelNotification() {
    Notification.Builder builder = 
        new Notification.Builder(MyActivity.this);
    
    Intent newIntent = new Intent(BUTTON_CLICK_ACTION);
    PendingIntent pendingIntent = 
      PendingIntent.getBroadcast(MyActivity.this, 2, newIntent, 0);
    Bitmap myIconBitmap = null; // TODO Obtain Bitmap
    
    /**
     * Listing 10-45: Setting an auto-cancel Notification
     */
    builder.setSmallIcon(R.drawable.ic_launcher)
           .setTicker("Notification")
           .setWhen(System.currentTimeMillis())
           .setContentTitle("Title")
           .setContentText("Subtitle")
           .setContentInfo("Info")
           .setLargeIcon(myIconBitmap)
           .setContentIntent(pendingIntent)
           .setAutoCancel(true);
    
    return builder;
  }
  
  private void cancelNotification(NotificationManager notificationManager) {
    /**
     * Listing 10-46: Canceling a Notification
     */
    notificationManager.cancel(NOTIFICATION_REF);

  }
}