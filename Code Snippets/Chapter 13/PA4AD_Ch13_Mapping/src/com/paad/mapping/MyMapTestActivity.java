package com.paad.mapping;

import java.util.List;

import android.graphics.drawable.Drawable;
import android.os.Bundle;

import com.google.android.maps.MapActivity;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;

public class MyMapTestActivity extends MapActivity {
  private MapView mapView;

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.map_layout);
    mapView = (MapView)findViewById(R.id.map_view);
    
    List<Overlay> overlays = mapView.getOverlays();
    Drawable drawable = getResources().getDrawable(R.drawable.ic_launcher);
    MyOverlay overlay = new MyOverlay();
    MyItemizedOverlay markers = new MyItemizedOverlay(drawable);
    MyDynamicItemizedOverlay dynamicMarkers = new MyDynamicItemizedOverlay(drawable);
    overlays.add(overlay);
    overlays.add(markers);
    overlays.add(dynamicMarkers);
  }

  @Override
  protected boolean isRouteDisplayed() {
    // IMPORTANT: This method must return true if your Activity
    // is displaying driving directions. Otherwise return false.
    return false;
  }
}