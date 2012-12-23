package com.paad.earthquake;

import android.database.Cursor;
import android.os.Bundle;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapView;

public class EarthquakeMap extends MapActivity {

	Cursor earthquakeCursor; 
	
  @Override
  public void onCreate(Bundle icicle) {
    super.onCreate(icicle);
    setContentView(R.layout.earthquake_map);
                    
    earthquakeCursor = getContentResolver().query(EarthquakeProvider.CONTENT_URI, null, null, null, null); 
    
    MapView earthquakeMap = (MapView)findViewById(R.id.map_view);        
    earthquakeMap.getOverlays().add(new EarthquakeOverlay(earthquakeCursor));
  }
  
  @Override 
  public void onResume() {     
    super.onResume();
    earthquakeCursor.requery();
  }

  @Override
  public void onPause() {
    earthquakeCursor.deactivate();
    super.onPause();
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

}