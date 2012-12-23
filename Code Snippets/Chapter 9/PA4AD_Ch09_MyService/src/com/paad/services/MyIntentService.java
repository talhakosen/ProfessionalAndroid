package com.paad.services;

/**
 * Listing 9-13: Implementing an Intent Service
 */
import android.app.IntentService;
import android.content.Intent;

public class MyIntentService extends IntentService {

  public MyIntentService(String name) {
    super(name);
    // TODO Complete any required constructor tasks.
  }

  @Override
  public void onCreate() {
    // TODO: Actions to perform when service is created.
  }
  
  @Override
  protected void onHandleIntent(Intent intent) {
    // This handler occurs on a background thread.
    // TODO The time consuming task should be implemented here.
    // Each Intent supplied to this IntentService will be 
    // processed consecutively here. When all incoming Intents
    // have been processed the Service will terminate itself.
  }
}