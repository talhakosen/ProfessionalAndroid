package com.paad.mediaplayer;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Listing 15-8: A media button press Manifest Broadcast Receiver implementation
 **/
public class MediaControlReceiver extends BroadcastReceiver {
  
  public static final String ACTION_MEDIA_BUTTON =
    "com.paad.ACTION_MEDIA_BUTTON";
  
  @Override
  public void onReceive(Context context, Intent intent) {
    if (Intent.ACTION_MEDIA_BUTTON.equals(intent.getAction())) {
      Intent internalIntent = new Intent(ACTION_MEDIA_BUTTON);
      internalIntent.putExtras(intent.getExtras());
      context.sendBroadcast(internalIntent);
    }
  }
}