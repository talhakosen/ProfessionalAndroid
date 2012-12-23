package com.paad.mapping;

/**
 * Listing 13-12: A skeleton map Activity
 */
import android.os.Bundle;
import android.widget.EditText;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;

public class MyMapActivity extends MapActivity {
  private MapView mapView;

  private MapController mapController;

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.map_layout);
    mapView = (MapView)findViewById(R.id.map_view);
    
    //
    pinViewToScreen();
    pinViewToGeo();
  }

  @Override
  protected boolean isRouteDisplayed() {
    // IMPORTANT: This method must return true if your Activity
    // is displaying driving directions. Otherwise return false.
    return false;
  }
  
  //
  private void pinViewToScreen() {
    /**
     * Listing 13-20: Pinning a View to a map
     */
    int y = 10;
    int x = 10;

    EditText editText1 = new EditText(getApplicationContext());
    editText1.setText("Screen Pinned");

    MapView.LayoutParams screenLP;
    screenLP = new MapView.LayoutParams(MapView.LayoutParams.WRAP_CONTENT,
                                        MapView.LayoutParams.WRAP_CONTENT,
                                        x, y,
                                        MapView.LayoutParams.TOP_LEFT);
    mapView.addView(editText1, screenLP);
  }
  
  private void pinViewToGeo() {
    /**
     * Listing 13-21: Pinning a View to a geographical location
     */
    Double lat = 37.422134*1E6;
    Double lng = -122.084069*1E6;
    GeoPoint geoPoint = new GeoPoint(lat.intValue(), lng.intValue());

    MapView.LayoutParams geoLP;
    geoLP = new MapView.LayoutParams(MapView.LayoutParams.WRAP_CONTENT,
                                     MapView.LayoutParams.WRAP_CONTENT,
                                     geoPoint,
                                     MapView.LayoutParams.TOP_LEFT);

    EditText editText2 = new EditText(getApplicationContext());
    editText2.setText("Location Pinned");

    mapView.addView(editText2, geoLP);

  }
}