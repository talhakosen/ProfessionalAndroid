package com.paad.chapter8;

import java.io.File;
import java.util.Date;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.view.Gravity;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class MyActivity extends Activity {

	@Override
	public void onCreate(Bundle icicle) {
	  super.onCreate(icicle);

	  // -- Services
	  
	  // Bind to the service
	  Intent bindIntent = new Intent(MyActivity.this, MyService.class);
	  bindService(bindIntent, mConnection, Context.BIND_AUTO_CREATE);
	  
	  startMyService();
	  runBackgroundTask();
	  
	  // -- Toasts
	  
	  showToast();
	  showViewToast();

	  // ---- Notifications
	  NotificationManager notificationManager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);	 
	  
    Context context = getApplicationContext();
	  // Choose a drawable to display as the status bar icon
	  int icon = R.drawable.icon;
	  // Text to display in the status bar when the notification is launched
	  String tickerText = "Notification";
	  // The extended status bar orders notification in this order
	  long when = System.currentTimeMillis();
	     
	  Notification notification = new Notification(icon, tickerText, when);
	  
	  // Text to display in the extended status window
	  String expandedText = "Extended status text"; 
	  // Title for the expanded status
	  String expandedTitle = "Notification Title"; 
	  // Intent to launch an activity when the extended text is clicked
	  Intent intent = new Intent(this, MyActivity.class);
	  PendingIntent launchIntent = PendingIntent.getActivity(context, 0, intent, 0);

	  notification.setLatestEventInfo(context, expandedTitle, expandedText, launchIntent);	 

	  // Modify the number of notifications being represented 
	  // by the status bar icon.
	  int notificationRef = 1;
	  
	  notification.number++;
	  notificationManager.notify(notificationRef, notification);
	  
	  notification.number = 10;
	  notificationManager.notify(notificationRef, notification);

	  // Add an alert sound to the notification.
	  Uri ringURI = Uri.fromFile(new File("/system/media/audio/ringtones/ringer.mp3"));
	  notification.sound = ringURI;
	  
	  // Add vibration to the notification.
	  long[] vibrate = new long[] {1000, 1000, 1000, 1000, 1000 };
	  notification.vibrate = vibrate;

	   // Add flashing LEDS to the notification.
	  notification.ledARGB = Color.RED;
	  notification.ledOffMS = 0;
	  notification.ledOnMS = 1;
	  
	  notification.flags = notification.flags | Notification.FLAG_SHOW_LIGHTS;
	  
	   // Toggle ongoing and insistent notification flags.
	  notification.flags = notification.flags | Notification.FLAG_ONGOING_EVENT;
	  notification.flags = notification.flags | Notification.FLAG_INSISTENT;

	  // ---- Alarms
	  AlarmManager alarms = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
	  
	  String MY_RTC_ALARM = "MY_RTC_ALARM"; 
	  String ALARM_ACTION = "ALARM_ACTION"; 
    
	  int alarmType = AlarmManager.ELAPSED_REALTIME_WAKEUP;
	  long timeOrLengthofWait = 10000;
	  Intent intentToFire = new Intent(ALARM_ACTION);
	  PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, intentToFire, 0);
	  alarms.set(alarmType, timeOrLengthofWait, pendingIntent);
	  
	  PendingIntent rtcIntent = PendingIntent.getBroadcast(this, 0, new Intent(MY_RTC_ALARM), 1);
	  PendingIntent elapsedIntent = PendingIntent.getBroadcast(this, 0, new Intent(ALARM_ACTION), 1);

	  // Wakeup and fire intent in 5 hours.
	  Date t = new Date();
	  t.setTime(java.lang.System.currentTimeMillis() + 60*1000*5);
	  alarms.set(AlarmManager.RTC_WAKEUP, t.getTime(), rtcIntent);

	  // Fire intent in 30 mins if already awake.
	  alarms.set(AlarmManager.ELAPSED_REALTIME, 30*60*1000, elapsedIntent);

	  // Cancel the first alarm.
	  alarms.cancel(rtcIntent);
  }
	
	/** Demonstrate displaying a simple toast */
	private void showToast() {
		Context context = getApplicationContext();
		String msg = "Displaying a Toast!";
		int duration = Toast.LENGTH_SHORT;
		Toast toast = Toast.makeText(context, msg, duration);
		toast.setGravity(Gravity.BOTTOM, 0, 0);
		toast.show();
	}
	
	/** Display a Toast that contains a custom View */
	private void showViewToast() {
		Context context = getApplicationContext();
		
		String msg = "Displaying a Toast!";
		int duration = Toast.LENGTH_LONG;
		Toast toast = Toast.makeText(context, msg, duration);
		int offsetX = 0;
		int offsetY = 0;
		toast.setGravity(Gravity.TOP, offsetX, offsetY);
				
		LinearLayout ll = new LinearLayout(context);
		ll.setOrientation(LinearLayout.VERTICAL);
		    
		TextView myTextView = new TextView(context);
		CompassView cv = new CompassView(context);

		myTextView.setText(msg);
		
		int lHeight = LinearLayout.LayoutParams.FILL_PARENT;
		int lWidth = LinearLayout.LayoutParams.WRAP_CONTENT;	  

		ll.addView(cv, new LinearLayout.LayoutParams(lHeight, lWidth));
		ll.addView(myTextView, new LinearLayout.LayoutParams(lHeight, lWidth));
			
		ll.setPadding(40, 50, 0, 50);
		
		toast.setView(ll);
		toast.show();
	}
	
	/** ---- Services ---- */

	// Reference to the service
	private MyService serviceBinder;
	    
	// Handles the connection between the service and activity
	private ServiceConnection mConnection = new ServiceConnection() {
	  public void onServiceConnected(ComponentName className, IBinder service) {
	    // Called when the connection is made.
	    serviceBinder = ((MyService.MyBinder)service).getService();
	  }

	  public void onServiceDisconnected(ComponentName className) {
	    // Received when the service unexpectedly disconnects.
	    serviceBinder = null;
	  }  
	};
	
	private void startMyService() {
    ComponentName service = startService(new Intent(this, MyService.class));
      
    // Stop a service using the service name.
    stopService(new Intent(this, service.getClass()));
    // Stop a service explicitly.
    try {
      Class serviceClass = Class.forName(service.getClassName());
      stopService(new Intent(this, serviceClass));
    } catch (ClassNotFoundException e) {}
  }

	/** ---- Background Thread Handling ---- */
	
  //Initialize a handler on the main thread.
  private Handler handler = new Handler();

  /** Starts a process that launches a background thread */
  private void runBackgroundTask() {
    // This method is called on the main GUI thread.
    mainProcessing();
  }

  private void mainProcessing() {
    Thread thread = new Thread(null, doBackgroundThreadProcessing, "Background"); 
    thread.start();
  }
  
  private Runnable doBackgroundThreadProcessing = new Runnable() {
    public void run() {
      backgroundThreadProcessing();
    }
  };
  
  // Method which does some processing in the background.
  private void backgroundThreadProcessing() {
    //[ ... Time consuming operations ... ]
    handler.post(doUpdateGUI); 
  }

  // Runnable that executes the update GUI method.
  private Runnable doUpdateGUI = new Runnable() {
    public void run() {
    	Context context = getApplicationContext();
    	String msg = "Displaying a Toast!";
    	int duration = Toast.LENGTH_SHORT;
    	Toast.makeText(context, msg, duration).show();
    }
  };
}