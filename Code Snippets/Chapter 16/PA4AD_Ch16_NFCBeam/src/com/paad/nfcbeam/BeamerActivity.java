package com.paad.nfcbeam;

import java.nio.charset.Charset;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentFilter;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.NfcAdapter.CreateNdefMessageCallback;
import android.nfc.NfcEvent;
import android.os.Bundle;
import android.os.Parcelable;
import android.widget.TextView;

public class BeamerActivity extends Activity {
  /** Called when the activity is first created. */
  
  PendingIntent nfcPendingIntent;
  IntentFilter[] intentFiltersArray;
  String[][] techListsArray;
  
  NfcAdapter nfcAdapter;
  
  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.main);
    
    nfcAdapter = NfcAdapter.getDefaultAdapter(this);
    
    /**
     * Listing 16-31: Creating an Android Beam NDEF message
     */
    String payload = "Two to beam across";

    String mimeType = "application/com.paad.nfcbeam";
    byte[] mimeBytes = mimeType.getBytes(Charset.forName("US-ASCII"));

    NdefMessage nfcMessage = new NdefMessage(new NdefRecord[] { 
      // Create the NFC payload.
      new NdefRecord(
        NdefRecord.TNF_MIME_MEDIA, 
        mimeBytes, 
        new byte[0],    
        payload.getBytes()),

      // Add the AAR (Android Application Record)
      NdefRecord.createApplicationRecord("com.paad.nfcbeam")
    });
    
    //
    nfcAdapter.setNdefPushMessage(nfcMessage, this);
        
    /**
     * Listing 16-32: Setting your Android Beam message dynamically
     */
    nfcAdapter.setNdefPushMessageCallback(new CreateNdefMessageCallback() {
      public NdefMessage createNdefMessage(NfcEvent event) {
        String payload = "Beam me up, Android!\n\n" +
            "Beam Time: " + System.currentTimeMillis();
        
        NdefMessage message = createMessage(payload);
        
        return message;
      }
    }, this);
  }
  
  public NdefMessage createMessage(String payload) {
    String mimeType = "application/com.paad.nfcbeam";
    byte[] mimeBytes = mimeType.getBytes(Charset.forName("US-ASCII"));
    
    NdefMessage msg = new NdefMessage(new NdefRecord[] { 
      new NdefRecord(NdefRecord.TNF_MIME_MEDIA, mimeBytes, new byte[0], payload.getBytes()),
      NdefRecord.createApplicationRecord("com.paad.nfcbeam")
    });
        
    return msg;
  }
  
  public void onPause() {
    super.onPause();

    nfcAdapter.disableForegroundDispatch(this);
  }

  @Override
  public void onResume() {
    super.onResume();
    nfcAdapter.enableForegroundDispatch(
      this, 
      // Intent that will be used to package the Tag Intent.
      nfcPendingIntent, 
      // Array of Intent Filters used to declare the Intents you
      // wish to intercept.
      intentFiltersArray, 
      // Array of Tag technologies you wish to handle.
      techListsArray);

    String action = getIntent().getAction();
    if (NfcAdapter.ACTION_NDEF_DISCOVERED.equals(action)) {
      processIntent(getIntent());
    }
  }

  @Override
  public void onNewIntent(Intent intent) {
    setIntent(intent);
  }
  
  /**
   * Parses the NDEF Message from the intent and prints to the TextView
   */
  void processIntent(Intent intent) {      
      /**
       * Listing 16-34: Extracting the Android Beam payload
       */
      Parcelable[] messages = intent.getParcelableArrayExtra(
        NfcAdapter.EXTRA_NDEF_MESSAGES);

      NdefMessage message = (NdefMessage)messages[0];
      NdefRecord record = message.getRecords()[0];

      String payload = new String(record.getPayload());

      //
      TextView textView = (TextView) findViewById(R.id.textView);
      textView.setText(payload);
  }
  
}