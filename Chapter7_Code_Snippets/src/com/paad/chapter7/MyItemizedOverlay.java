/**
 * This skeleton code demonstrates how to implement your 
 * own ItemizedOverlay using OverlayItems.
 */

package com.paad.chapter7;

import android.graphics.drawable.Drawable;
import com.google.android.maps.GeoPoint;
import com.google.android.maps.ItemizedOverlay;
import com.google.android.maps.OverlayItem;

public class MyItemizedOverlay extends ItemizedOverlay<OverlayItem> {

  public MyItemizedOverlay(Drawable defaultMarker) {
    super(defaultMarker);
    // Call populate when you are ready to create the items.
    populate();
  }

  @Override
  protected OverlayItem createItem(int index) {
    Double lat = (index+37.422006)*1E6;
    Double lng = -122.084095*1E6;
    GeoPoint point = new GeoPoint(lat.intValue(), lng.intValue());

    OverlayItem oi = new OverlayItem(point, "Marker", "Marker Text");
    return oi;
  }

  @Override
  public int size() {
    // Return the number of markers in the collection
    return 5;
  }

}
