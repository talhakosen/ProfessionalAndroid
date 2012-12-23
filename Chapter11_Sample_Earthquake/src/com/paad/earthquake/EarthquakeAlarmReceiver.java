package com.paad.earthquake;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class EarthquakeAlarmReceiver extends BroadcastReceiver {

	public static final String ACTION_REFRESH_EARTHQUAKE_ALARM = "com.paad.earthquake.ACTION_REFRESH_EARTHQUAKE_ALARM"; 
	
	@Override
	public void onReceive(Context context, Intent intent) {
       Intent startIntent = new Intent(context, EarthquakeService.class);
       context.startService(startIntent);
	}

}
