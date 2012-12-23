package com.paad.chapter9;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
//import android.provider.Telephony.Sms;
import android.os.Bundle;
import android.telephony.gsm.SmsManager;
import android.telephony.gsm.SmsMessage;

public class IncomingSMSReceiver extends BroadcastReceiver {

  private static final String SMS_RECEIVED = "android.provider.Telephony.SMS_RECEIVED";
  private static final String queryString = "@echo ";
  
  public void onReceive(Context _context, Intent _intent) {
	  if (_intent.getAction().equals(SMS_RECEIVED)) {
      SmsManager sms = SmsManager.getDefault();

      // Extract the SMS message bundle
      Bundle bundle = _intent.getExtras();
      if (bundle != null) {
        // Extract the PDUS that contain the messages
        Object[] pdus = (Object[]) bundle.get("pdus");
        
        // Create new messages from the PDUs
        SmsMessage[] messages = new SmsMessage[pdus.length];  
        for (int i = 0; i < pdus.length; i++)
          messages[i] = SmsMessage.createFromPdu((byte[]) pdus[i]);
        
        // Extract the message body and its envelope from each message
        for (SmsMessage message : messages) {
          String msg = message.getMessageBody();
          String to = message.getOriginatingAddress();
          
          // Send a new message the echoes the incoming one
          if (msg.toLowerCase().startsWith(queryString)) {
            String out = msg.substring(queryString.length());
            sms.sendTextMessage(to, null, out, null, null);
          }
        }
      }
	  }
  }
}