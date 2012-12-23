package com.paad.earthquake;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.os.Bundle;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapView;

public class EarthquakeMap extends MapActivity {

  Cursor earthquakeCursor; 
  EarthquakeReceiver receiver;
  NotificationManager notificationManager;
  
  @Override
  public void onCreate(Bundle icicle) {
    super.onCreate(icicle);
    setContentView(R.layout.earthquake_map);
                    
    earthquakeCursor = getContentResolver().query(EarthquakeProvider.CONTENT_URI, null, null, null, null); 
    
    MapView earthquakeMap = (MapView)findViewById(R.id.map_view);        
    earthquakeMap.getOverlays().add(new EarthquakeOverlay(earthquakeCursor));
    
    notificationManager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
  }

	@Override
	public void onPause() {
	  earthquakeCursor.deactivate();
	  super.onPause();
	}

	@Override 
	public void onResume() {
    super.onResume();
    
	  IntentFilter filter = new IntentFilter(EarthquakeService.NEW_EARTHQUAKE_FOUND);
	  receiver = new EarthquakeReceiver();
	  registerReceiver(receiver, filter);
    notificationManager.cancel(EarthquakeService.NOTIFICATION_ID);
	  
	  earthquakeCursor.requery();
	}
	
  @Override 
  public void onDestroy() {
    earthquakeCursor.close();
    super.onDestroy();
  }
  
  @Override
  protected boolean isRouteDisplayed() {
    return false;
  }

	public class EarthquakeReceiver extends BroadcastReceiver {
	  @Override
	  public void onReceive(Context context, Intent intent) {
	    notificationManager.cancel(EarthquakeService.NOTIFICATION_ID);

	    earthquakeCursor.requery();
	    MapView earthquakeMap = (MapView)findViewById(R.id.map_view);
	    earthquakeMap.invalidate();
	  }
	}
}