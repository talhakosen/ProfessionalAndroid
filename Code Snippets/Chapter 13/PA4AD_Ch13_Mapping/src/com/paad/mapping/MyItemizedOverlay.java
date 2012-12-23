package com.paad.mapping;

/**
 * Listing 13-18: Creating a new Itemized Overlay
 */
import android.graphics.drawable.Drawable;
import com.google.android.maps.GeoPoint;
import com.google.android.maps.ItemizedOverlay;
import com.google.android.maps.OverlayItem;

public class MyItemizedOverlay extends ItemizedOverlay<OverlayItem> {

  public MyItemizedOverlay(Drawable defaultMarker) {
    super(boundCenterBottom(defaultMarker));
    populate();
  }

  @Override
  protected OverlayItem createItem(int index) {
    switch (index) {
      case 0:
        Double lat = 37.422006*1E6;
        Double lng = -122.084095*1E6;
        GeoPoint point = new GeoPoint(lat.intValue(), lng.intValue());

        OverlayItem oi;
        oi = new OverlayItem(point, "Marker", "Marker Text");
        return oi;
    }
    return null;
  }

  @Override
  public int size() {
    // Return the number of markers in the collection
    return 1;
  }
}