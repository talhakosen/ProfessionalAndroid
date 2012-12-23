package com.paad.intents;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class MyReceiver extends BroadcastReceiver {

  private static final String TAG = "My Receiver";
  public static final String ACTION = "com.paad.intents.MyReceiverAction";

  @Override
  public void onReceive(Context context, Intent intent) {
    Log.d(TAG, "My Receiver Received Broadcast");
  }

}
