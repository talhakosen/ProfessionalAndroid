/**
 * Code snippets for Chapter 9: Communication, looking at  
 * SMS messages
 */

package com.paad.chapter9;

import java.util.ArrayList;
import android.app.Activity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.telephony.gsm.SmsManager;

public class MyActivity extends Activity {

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.main);

    // -- Sending an SMS message
    
    SmsManager smsManager = SmsManager.getDefault();
    
    String sendTo = "5554";
    String myMessage = "Android supports programmatic SMS messaging!";

    smsManager.sendTextMessage(sendTo, null, myMessage, null, null);      

    // -- Sending a multi-part message
    
    PendingIntent sentIntent = PendingIntent.getBroadcast(getApplicationContext(), 0, new Intent("null"), 0);
    ArrayList<String> messageArray = smsManager.divideMessage(myMessage);
    ArrayList<PendingIntent> sentIntents = new ArrayList<PendingIntent>();
    for (int i = 0; i < messageArray.size(); i++)
      sentIntents.add(sentIntent);
    
    smsManager.sendMultipartTextMessage(sendTo, null, messageArray, sentIntents, null);

    // -- Sending Data SMS messages
    
    short destinationPort = 80;
    byte[] data = null;//[ ... your data ... ];
    smsManager.sendDataMessage(sendTo, null, destinationPort, data, null, null);
    
    // -- Intercepting SMS Messages 
    
    final String SMS_RECEIVED = "android.provider.Telephony.SMS_RECEIVED";  
  
    IntentFilter filter = new IntentFilter(SMS_RECEIVED);
    BroadcastReceiver receiver = new IncomingSMSReceiver();
    registerReceiver(receiver, filter);
    
    // -- Tracking Delivery Success
    
    String SENT_SMS_ACTION = "SENT_SMS_ACTION";
    String DELIVERED_SMS_ACTION = "DELIVERED_SMS_ACTION";

    // Create the sentIntent parameter
    Intent sentIntent1 = new Intent(SENT_SMS_ACTION);
    PendingIntent sentPI = PendingIntent.getBroadcast(getApplicationContext(),
                                                      0,
                                                      sentIntent1,
                                                      0);

    // Create the deliveryIntent parameter
    Intent deliveryIntent = new Intent(DELIVERED_SMS_ACTION);
    PendingIntent deliverPI = PendingIntent.getBroadcast(getApplicationContext(),
                                                          0,
                                                          deliveryIntent,
                                                          0);
    // Register the Broadcast Receivers
    registerReceiver(new BroadcastReceiver() {
                       @Override
                       public void onReceive(Context _context, Intent _intent) {
                         switch (getResultCode()) {
                           case Activity.RESULT_OK: 
                             //[... send success actions ... ]; break;
                           case SmsManager.RESULT_ERROR_GENERIC_FAILURE: 
                             //[... generic failure actions ... ]; break;
                           case SmsManager.RESULT_ERROR_RADIO_OFF: 
                             //[... radio off failure actions ... ]; break;
                           case SmsManager.RESULT_ERROR_NULL_PDU: 
                             //[... null PDU failure actions ... ]; break;
                         }
                       }
                     }, 
                     new IntentFilter(SENT_SMS_ACTION));

    registerReceiver(new BroadcastReceiver() {
                       @Override
                       public void onReceive(Context _context, Intent _intent) {
                         //[... SMS delivered actions ... ]
                       }
                     }, 
                     new IntentFilter(DELIVERED_SMS_ACTION));


    // Send the message
    smsManager.sendTextMessage(sendTo, null, myMessage, sentPI, deliverPI);        
  }  
}