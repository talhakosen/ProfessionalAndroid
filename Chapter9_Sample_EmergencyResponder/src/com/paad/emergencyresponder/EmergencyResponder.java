package com.paad.emergencyresponder;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.locks.ReentrantLock;
import android.app.Activity;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.BroadcastReceiver;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.telephony.gsm.SmsManager;
import android.telephony.gsm.SmsMessage;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;

public class EmergencyResponder extends Activity {

  public static final String SENT_SMS = "com.paad.emergencyresponder.ER_SMS_SENT";
  public static final String SMS_RECEIVED = "android.provider.Telephony.SMS_RECEIVED";

	ReentrantLock lock;
	CheckBox locationCheckBox;
	ArrayList<String> requesters;
	ArrayAdapter<String> aa;
	
	@Override
	public void onCreate(Bundle icicle) {
	  super.onCreate(icicle);
	  setContentView(R.layout.main);

	  lock = new ReentrantLock();
	  requesters = new ArrayList<String>();
	  
	  wireUpControls();
	  
	  IntentFilter filter = new IntentFilter(SMS_RECEIVED);
	  registerReceiver(emergencyResponseRequestReceiver, filter);

	  IntentFilter attemptedDeliveryfilter = new IntentFilter(SENT_SMS);
	  registerReceiver(attemptedDeliveryReceiver, attemptedDeliveryfilter);
	}

	/** Hook up the ListView and Buttons */
	private void wireUpControls() {
	  locationCheckBox = (CheckBox)findViewById(R.id.checkboxSendLocation);
	  ListView myListView = (ListView)findViewById(R.id.myListView);

	  int layoutID = android.R.layout.simple_list_item_1;
	  aa = new ArrayAdapter<String>(this, layoutID, requesters);

	  myListView.setAdapter(aa);

	  Button okButton = (Button)findViewById(R.id.okButton);
	  okButton.setOnClickListener(new OnClickListener() {
	    public void onClick(View arg0) {
	      respond(true, locationCheckBox.isChecked()); 
	    }
	  });

	  Button notOkButton = (Button)findViewById(R.id.notOkButton);
	  notOkButton.setOnClickListener(new OnClickListener() {
	    public void onClick(View arg0) {
	      respond(false, locationCheckBox.isChecked()); 
	    }
	  });

	  Button autoResponderButton = (Button)findViewById(R.id.autoResponder);
	  autoResponderButton.setOnClickListener(new OnClickListener() {
	    public void onClick(View arg0) {
	      startAutoResponder();
	    } 
	  });
	}

  /** Respond to all requests for status */
	@SuppressWarnings("unchecked")
  public void respond(boolean _ok, boolean _includeLocation) {
	  String okString = getString(R.string.respondAllClearText);
	  String notOkString = getString(R.string.respondMaydayText);

	  String outString = _ok ? okString : notOkString; 

	  ArrayList<String> requestersCopy = (ArrayList<String>)requesters.clone();

	  for (String to : requestersCopy)
	    respond(to, outString, _includeLocation);	
	}
	
	/** Respond to a request */
	private void respond(String _to, String _response, boolean _includeLocation) {
	  // Remove the target from the list of people we need to respond to.
	  lock.lock();
	  requesters.remove(_to);
	  aa.notifyDataSetChanged();
	  lock.unlock();

	  SmsManager sms = SmsManager.getDefault(); 

	  Intent intent = new Intent(SENT_SMS);
	  intent.putExtra("recipient", _to);

	  PendingIntent failed = PendingIntent.getBroadcast(getApplicationContext(), 0, intent, 0);

	  // Send the message
	  sms.sendTextMessage(_to, null, _response, failed, null);

	  StringBuilder sb = new StringBuilder();

	  // Find the current location and send it as SMS messages if required.
	  if (_includeLocation) {
	    String ls = Context.LOCATION_SERVICE;
	    LocationManager lm = (LocationManager)getSystemService(ls);
	    Location l = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);

	    sb.append("I'm @:\n");
	    sb.append(l.toString() + "\n");

	    List<Address> addresses;
	    Geocoder g = new Geocoder(getApplicationContext(), Locale.getDefault());
	    try {
	      addresses = g.getFromLocation(l.getLatitude(), l.getLongitude(), 1);
	      if (addresses != null) {
	        Address currentAddress = addresses.get(0);
	        if (currentAddress.getMaxAddressLineIndex() > 0) {
	          for (int i = 0; i < currentAddress.getMaxAddressLineIndex(); i++) {
	            sb.append(currentAddress.getAddressLine(i));
	            sb.append("\n");
	          }
	        }
	        else {
	          if (currentAddress.getPostalCode() != null)
	            sb.append(currentAddress.getPostalCode());
	        }
	      }
	    } catch (IOException e) {}

	    ArrayList<String> locationMsgs = sms.divideMessage(sb.toString());
	    for (String locationMsg : locationMsgs)
	      sms.sendTextMessage(_to, null, locationMsg, null, null);
	  }
	}
	
	private void startAutoResponder() {}
	
	private BroadcastReceiver attemptedDeliveryReceiver = new BroadcastReceiver() { 
	  @Override
	  public void onReceive(Context _context, Intent _intent) {
	    if (_intent.getAction().equals(SENT_SMS)) {
	      if (getResultCode() != Activity.RESULT_OK) {
	        String recipient = _intent.getStringExtra("recipient");
	        requestReceived(recipient);
	      }
	    }
	  }
	};

	BroadcastReceiver emergencyResponseRequestReceiver = new BroadcastReceiver() {
    @Override 
	  public void onReceive(Context _context, Intent _intent) {
	    if (_intent.getAction().equals(SMS_RECEIVED)) {
	      String queryString = getString(R.string.querystring);

	      Bundle bundle = getIntent().getExtras();
	      if (bundle != null) {
	        Object[] pdus = (Object[]) bundle.get("pdus");
	        SmsMessage[] messages = new SmsMessage[pdus.length];
	        for (int i = 0; i < pdus.length; i++)
	          messages[i] = SmsMessage.createFromPdu((byte[]) pdus[i]);

	        for (SmsMessage message : messages) {
	          if (message.getMessageBody().toLowerCase().contains(queryString)) { 
	            requestReceived(message.getOriginatingAddress());
	          }
	        }
	      }
	    }
	  }
	};

	public void requestReceived(String _from) {
	  if (!requesters.contains(_from)) {
		    lock.lock();
		    requesters.add(_from);
		    aa.notifyDataSetChanged();
		    lock.unlock();
		  }			
	}		
}