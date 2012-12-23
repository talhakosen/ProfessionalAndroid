package com.paad.mapping;

import java.util.ArrayList;

import android.graphics.drawable.Drawable;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.ItemizedOverlay;
import com.google.android.maps.OverlayItem;

/**
 * Listing 13-19: Skeleton code for a dynamic Itemized Overlay
 */
public class MyDynamicItemizedOverlay extends 
  ItemizedOverlay<OverlayItem> {

  private ArrayList<OverlayItem> items;

  public MyDynamicItemizedOverlay(Drawable defaultMarker) {
    super(boundCenterBottom(defaultMarker));
    items = new ArrayList<OverlayItem>();
    populate();
  }

  public void addNewItem(GeoPoint location, String markerText,
                         String snippet) {
    items.add(new OverlayItem(location, markerText, snippet));
    populate();
  }

  public void removeItem(int index) {
    items.remove(index);
    populate();
  }

  @Override
  protected OverlayItem createItem(int index) {
    return items.get(index);
  }

  @Override
  public int size() {
    return items.size();
  }
}