package com.paad.chapter7;

import java.util.List;
import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.MyLocationOverlay;
import com.google.android.maps.Overlay;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class MyMapActivity extends MapActivity {

  private MapView mapView;
  
  @Override
  public void onCreate(Bundle icicle) {
    super.onCreate(icicle);
    setContentView(R.layout.map_layout);
    mapView = (MapView)findViewById(R.id.map_view);

    // ---- Managing the Map
    
    // Map configuration options
    mapView.setSatellite(false);
    mapView.setStreetView(true);
    mapView.setTraffic(true);    

    GeoPoint center = mapView.getMapCenter();
    int latSpan = mapView.getLatitudeSpan();
    int longSpan = mapView.getLongitudeSpan();
    
    // Zoom Controls    
    int y = 0;
    int x = 10;

    MapView.LayoutParams lp;
    lp = new MapView.LayoutParams(MapView.LayoutParams.WRAP_CONTENT,
                                  MapView.LayoutParams.WRAP_CONTENT,
                                  x, y,
                                  MapView.LayoutParams.TOP_LEFT);

    View zoomControls = mapView.getZoomControls();
    mapView.addView(zoomControls, lp);
    mapView.displayZoomControls(true);

    // Map Controller
    MapController mapController = mapView.getController();
    
    Double lat = 37.422006*1E6;
    Double lng = -122.084095*1E6;
    GeoPoint point = new GeoPoint(lat.intValue(), lng.intValue());
    
    mapController.setCenter(point);
    mapController.setZoom(1);
    
    mapController.animateTo(point);
    mapController.stopPanning();
    
    // ---- Using Map Overlays
    
    List<Overlay> overlays = mapView.getOverlays();
    overlays.add(new MyOverlay());
    mapView.postInvalidate();
    
    MyLocationOverlay myLocationOverlay = new MyLocationOverlay(this, mapView);
    overlays.add(myLocationOverlay);
    myLocationOverlay.enableCompass();
    myLocationOverlay.enableMyLocation();
        
    Resources r = getResources();
    MyItemizedOverlay markers = new MyItemizedOverlay(r.getDrawable(R.drawable.icon));    
    overlays.add(markers);
    
    // ---- Pinning Controls to the Map
    MapView.LayoutParams screenLP;
    screenLP = new MapView.LayoutParams(MapView.LayoutParams.WRAP_CONTENT,
                                        MapView.LayoutParams.WRAP_CONTENT,
                                        x, y,
                                        MapView.LayoutParams.TOP_LEFT);

    // Pin to screen position.
    EditText editTextScreen = new EditText(getApplicationContext());
    editTextScreen.setText("Screen Pinned");
    mapView.addView(editTextScreen, screenLP);    

    MapView.LayoutParams geoLP;
    geoLP = new MapView.LayoutParams(MapView.LayoutParams.WRAP_CONTENT,
                                     MapView.LayoutParams.WRAP_CONTENT,
                                     point,
                                     MapView.LayoutParams.TOP_LEFT);

    // Pin to a map position.
    EditText editTextMap = new EditText(getApplicationContext());
    editTextMap.setText("Location Pinned");
    mapView.addView(editTextMap, geoLP);

    // Remove pinned View
    mapView.removeView(editTextScreen);
  }

  @Override
  protected boolean isRouteDisplayed() {
    // IMPORTANT: This method must return true if your Activity
    // is displaying driving directions. Otherwise return false.
    return false;
  }
 
}