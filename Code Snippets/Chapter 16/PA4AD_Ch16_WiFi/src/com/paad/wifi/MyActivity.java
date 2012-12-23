package com.paad.wifi;

import java.util.List;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

public class MyActivity extends Activity {
  
  private static final String TAG = "TAG";

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.main);
    
    /**
     * Listing 16-14: Accessing the Wi-Fi Manager
     */
    String service = Context.WIFI_SERVICE;
    final WifiManager wifi = (WifiManager)getSystemService(service);
    
    /**
     * Listing 16-15: Monitoring and changing Wi-Fi state
     */
    if (!wifi.isWifiEnabled())
      if (wifi.getWifiState() != WifiManager.WIFI_STATE_ENABLING)
        wifi.setWifiEnabled(true);

    /**
     * Listing 16-16: Querying the active network connection
     */
    WifiInfo info = wifi.getConnectionInfo();
    if (info.getBSSID() != null) {
      int strength = WifiManager.calculateSignalLevel(info.getRssi(), 5);
      int speed = info.getLinkSpeed();
      String units = WifiInfo.LINK_SPEED_UNITS;
      String ssid = info.getSSID();

      String cSummary = String.format("Connected to %s at %s%s. " +
                                      "Strength %s/5",
                                      ssid, speed, units, strength);
      Log.d(TAG, cSummary);
    }

    /**
     * Listing 16-17: Conducting a scan for Wi-Fi access points
     */
    // Register a broadcast receiver that listens for scan results.
    registerReceiver(new BroadcastReceiver() {
      @Override
      public void onReceive(Context context, Intent intent) {
        List<ScanResult> results = wifi.getScanResults();
        ScanResult bestSignal = null;
        for (ScanResult result : results) {
          if (bestSignal == null ||
              WifiManager.compareSignalLevel(
                bestSignal.level,result.level) < 0)
            bestSignal = result;
        }

        String connSummary = String.format("%s networks found. %s is" +
                                           "the strongest.",
                                           results.size(),
                                           bestSignal.SSID);

        Toast.makeText(MyActivity.this, 
                       connSummary, Toast.LENGTH_LONG).show();
      }
    }, new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));

    // Initiate a scan.
    wifi.startScan();
  }
}