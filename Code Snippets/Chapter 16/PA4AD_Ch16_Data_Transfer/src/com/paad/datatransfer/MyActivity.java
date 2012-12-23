package com.paad.datatransfer;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;

public class MyActivity extends Activity {
  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.main);
    
    /**
     * Listing 16-9: Accessing the Connectivity Manager
     */
    String service = Context.CONNECTIVITY_SERVICE;

    final ConnectivityManager connectivity = 
      (ConnectivityManager)getSystemService(service);
    
    /**
     * Listing 16-10: Monitoring the background data setting
     */
    registerReceiver(
      new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
          boolean backgroundEnabled = 
            connectivity.getBackgroundDataSetting();
          setBackgroundData(backgroundEnabled);
        }
      },
      new IntentFilter(
            ConnectivityManager.ACTION_BACKGROUND_DATA_SETTING_CHANGED)
    );
    
    /**
     * Listing 16-12: Determining connectivity
     */
    NetworkInfo activeNetwork = connectivity.getActiveNetworkInfo();

    boolean isConnected = ((activeNetwork  != null) &&   
                           (activeNetwork.isConnectedOrConnecting()));

    boolean isWiFi = activeNetwork.getType() ==
                     ConnectivityManager.TYPE_WIFI;


  }
  
  private void setBackgroundData(boolean backgroundEnabled) {
    // TODO Enable or disable background data transfers.
  }
}