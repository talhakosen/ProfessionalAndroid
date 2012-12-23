package com.paad.nfc;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentFilter;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.tech.NfcF;
import android.os.Bundle;
import android.os.Parcelable;

public class BeamerActivity extends Activity {
  
  /**
   * Listing 16-30: Configuring foreground dispatching parameters
   */
  PendingIntent nfcPendingIntent;
  IntentFilter[] intentFiltersArray;
  String[][] techListsArray;
  NfcAdapter nfcAdapter;

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.main);
    
    nfcAdapter = NfcAdapter.getDefaultAdapter(this);
    
    // Create the Pending Intent.  
    int requestCode = 0;
    int flags = 0;

    Intent nfcIntent = new Intent(this, getClass());
    nfcIntent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
     
    nfcPendingIntent = 
      PendingIntent.getActivity(this, requestCode, nfcIntent, flags);
    
    // Create an Intent Filter limited to the URI or MIME type to
    // intercept TAG scans from.
    IntentFilter tagIntentFilter = 
      new IntentFilter(NfcAdapter.ACTION_NDEF_DISCOVERED);
    tagIntentFilter.addDataScheme("http");
    tagIntentFilter.addDataAuthority("blog.radioactiveyak.com", null);
    intentFiltersArray = new IntentFilter[] { tagIntentFilter };

    // Create an array of technologies to handle.
    techListsArray = new String[][] {
      new String[] { 
        NfcF.class.getName() 
      } 
    };
  }
  
  private void processIntent(Intent intent) {
    /**
     * Listing 16-28: Extracting NFC tag payloads
     */
    String action = getIntent().getAction();

    if (NfcAdapter.ACTION_NDEF_DISCOVERED.equals(action)) {
      Parcelable[] messages = 
        intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);

      for (int i = 0; i < messages.length; i++) {
        NdefMessage message = (NdefMessage)messages[i];
        NdefRecord[] records = message.getRecords();

        for (int j = 0; j < records.length; j++) {
          NdefRecord record = records[j];
          // TODO Process the individual records.
        }
      }
    }
  }
  
  /**
   * Listing 16-29: Using the foreground dispatch system
   */
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

}