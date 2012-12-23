package com.paad.wakelocks;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;

public class MyActivity extends Activity {
  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.main);
    
    MyAsyncTask myTask = new MyAsyncTask();
    myTask.execute();
  }
  
  /**
   * Listing 18-12: Using a Wake Lock
   */
  WakeLock wakeLock;

  private class MyAsyncTask extends AsyncTask<Void,  Void, Void> {
    @Override
    protected Void doInBackground(Void... parameters) {
      PowerManager pm = 
        (PowerManager)getSystemService(Context.POWER_SERVICE);

      wakeLock = 
        pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "MyWakeLock");

      wakeLock.acquire();
      
      // TODO Do things in the background

      return null;
    }

    @Override
    protected void onPostExecute(Void parameters) {
      wakeLock.release();
    }
  }

}