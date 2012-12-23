package com.paad.chapter8;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

public class MyService extends Service {
	  
  @Override
  public void onCreate() {
    // TODO Actions to perform when service is created.
  }
  
  @Override
  public void onStart(Intent intent, int startId) {
    // TODO Actions to perform when service is started.
  }
  
  @Override
  public IBinder onBind(Intent intent) {
    return binder;
  }

  private final IBinder binder = new MyBinder();
  
  public class MyBinder extends Binder {        
    MyService getService() {
      return MyService.this;        
    }
  }
}