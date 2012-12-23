package com.paad.c2dm;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

public class MyC2DMReceiver extends BroadcastReceiver {

  private static final String TAG = "C2DM_RECEIVER";

  /**
   * Listing 18-5: Extracting the C2DM registration ID
   */
  public void onReceive(Context context, Intent intent) {
    if (intent.getAction().equals(
      "com.google.android.c2dm.intent.REGISTRATION")) {

      String registrationId = intent.getStringExtra("registration_id"); 
      String error = intent.getStringExtra("error");
      String unregistered = intent.getStringExtra("unregistered");

      if (error != null) { 
        // Registration failed.
        if (error.equals("SERVICE_NOT_AVAILABLE")) {
          Log.e(TAG, "Service not available.");
          // Retry using exponential back off.
        }
        else if (error.equals("ACCOUNT_MISSING")) {
          Log.e(TAG, "No Google account on device.");
          // Ask the user to create / add a Google account
        }
        else if (error.equals("AUTHENTICATION_FAILED")) {
          Log.e(TAG, "Incorrect password.");
          // Ask the user to re-enter their Google account password.
        }
        else if (error.equals("TOO_MANY_REGISTRATIONS")) {
          Log.e(TAG, "Too many applications registered.");
          // Ask the user to unregister / uninstall some applications.
        }
        else if (error.equals("INVALID_SENDER")) {
          Log.e(TAG, "Invalid sender account.");
          // The sender account specified has not been registered
          // with the C2DM server.
        }
        else if (error.equals("PHONE_REGISTRATION_ERROR")) {
          Log.e(TAG, "Phone registration failed.");
          // The phone doesn't currently support C2DM.
        }
      } else if (unregistered != null) {
        // Unregistration complete. The application should stop
        // processing any further received messages.
        Log.d(TAG, "Phone deregistration completed successfully.");
      } else if (registrationId != null) {
        Log.d(TAG, "C2DM egistration ID received.");
        // Send the registration ID to your server. 
      }
    }
    /**
     * Listing 18-7: Extracting C2DM message details
     */
    if (intent.getAction().equals(
        "com.google.android.c2dm.intent.RECEIVE")) {
      Bundle extras = intent.getExtras();
      
      // Extract any extras included in the server messages.
      int newVoicemailCount = extras.getInt("VOICEMAIL_COUNT", 0);
    }

  }
}