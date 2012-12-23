package com.paad.chapter3manifest;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

//Dummy Service for demonstrating the Manifest
public class MyService extends Service {

	@Override
	public IBinder onBind(Intent arg0) {
		return null;
  }
	
}