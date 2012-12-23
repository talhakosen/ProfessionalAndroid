package com.paad.services;

/**
 * Listing 9-1: A skeleton Service class
 */
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class MyService extends Service {

  @Override
  public void onCreate() {
    // TODO: Actions to perform when service is created.
  }

  @Override
  public IBinder onBind(Intent intent) {
    // TODO: Replace with service binding implementation.
    return null;
  }
  
  /**
   * Listing 9-3: Overriding Service restart behavior
   */
  @Override
  public int onStartCommand(Intent intent, int flags, int startId) {
    startBackgroundTask(intent, startId);
    return Service.START_STICKY;
  }
  
  private void  startBackgroundTask(Intent intent, int startId) {
    // Start a background thread and begin the processing.
    backgroundExecution();
  }
  
  /**
   * Listing 9-14: Moving processing to a background Thread
   */
  //This method is called on the main GUI thread.
  private void backgroundExecution() {
   // This moves the time consuming operation to a child thread.
   Thread thread = new Thread(null, doBackgroundThreadProcessing,
                              "Background");
   thread.start();
  }
  
  //Runnable that executes the background processing method.
  private Runnable doBackgroundThreadProcessing = new Runnable() {
   public void run() {
     backgroundThreadProcessing();
   }
  };
  
  //Method which does some processing in the background.
  private void backgroundThreadProcessing() {
   // [ ... Time consuming operations ... ]
  }

}