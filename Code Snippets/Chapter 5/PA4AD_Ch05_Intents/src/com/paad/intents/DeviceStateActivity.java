package com.paad.intents;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.BatteryManager;
import android.os.Bundle;
import android.util.Log;

public class DeviceStateActivity extends Activity {
  
  private static final String TAG = "DEVICE_STATE_ACTIVITY";

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.main);
    Context context = this;

    /**
     * Listing 5-18: Determining battery and charge state information 
     */
    IntentFilter batIntentFilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
    Intent battery = context.registerReceiver(null, batIntentFilter);
    int status = battery.getIntExtra(BatteryManager.EXTRA_STATUS, -1);
    boolean isCharging = 
      status == BatteryManager.BATTERY_STATUS_CHARGING || 
      status == BatteryManager.BATTERY_STATUS_FULL;
    
    /**
     * Listing 5-19: Determining connectivity state information 
     */
    String svcName = Context.CONNECTIVITY_SERVICE;
    ConnectivityManager cm = (ConnectivityManager)context.getSystemService(svcName);

    NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
    boolean isConnected = activeNetwork.isConnectedOrConnecting();
    boolean isMobile = activeNetwork.getType() ==
                       ConnectivityManager.TYPE_MOBILE;

    /**
     * Listing 5-20: Determining docking state information 
     */
    IntentFilter dockIntentFilter = new IntentFilter(Intent.ACTION_DOCK_EVENT);
    Intent dock = context.registerReceiver(null, dockIntentFilter);

    int dockState = dock.getIntExtra(Intent.EXTRA_DOCK_STATE,
                      Intent.EXTRA_DOCK_STATE_UNDOCKED );
    boolean isDocked = dockState != Intent.EXTRA_DOCK_STATE_UNDOCKED;

    /**
     * Listing 5-21: Dynamically toggling manifest Receivers 
     */
    ComponentName myReceiverName = new ComponentName(this, MyReceiver.class);
    PackageManager pm = getPackageManager();

    // Enable a manifest receiver
    pm.setComponentEnabledSetting(myReceiverName,
      PackageManager.COMPONENT_ENABLED_STATE_ENABLED, 
      PackageManager.DONT_KILL_APP);

    // Disable a manifest receiver
    pm.setComponentEnabledSetting(myReceiverName,
      PackageManager.COMPONENT_ENABLED_STATE_DISABLED, 
      PackageManager.DONT_KILL_APP);
    
    // View Output
    Log.d(TAG, "Is Charging? " + isCharging);
    Log.d(TAG, "Is Connected? " + isConnected);
    Log.d(TAG, "Is Mobile? " + isMobile);
    Log.d(TAG, "Is Docked? " + isDocked);
  }
}