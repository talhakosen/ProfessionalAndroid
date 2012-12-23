package com.paad.mapping;

/**
 * Listing 13-14: Creating a new Overlay
 */
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.RectF;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;
import com.google.android.maps.Projection;

public class MyOverlay extends Overlay {
//  @Override
//  public void draw(Canvas canvas, MapView mapView, boolean shadow) {
//    if (shadow == false) {
//      // TODO [ ... Draw annotations on main map layer ... ]
//    }
//    else {
//      // TODO [ ... Draw annotations on the shadow layer ... ]
//    }
//  }
//
//  @Override
//  public boolean onTap(GeoPoint point, MapView mapView) {
//   // Return true if screen tap is handled by this overlay
//   return false;
//  }
  
  /**
   * Listing 13-16: A simple map Overlay
   */
  @Override
  public void draw(Canvas canvas, MapView mapView, boolean shadow) {
    Projection projection = mapView.getProjection();

    Double lat = -31.960906*1E6;
    Double lng = 115.844822*1E6;
    GeoPoint geoPoint = new GeoPoint(lat.intValue(), lng.intValue());

    if (shadow == false) {
      /**
       * Listing 13-15: Using map Projections
       */
      Point myPoint = new Point();
      // To screen coordinates
      projection.toPixels(geoPoint, myPoint);
      // To GeoPoint location coordinates
      GeoPoint gPoint = projection.fromPixels(myPoint.x, myPoint.y);

      // Create and setup your paint brush
      Paint paint = new Paint();
      paint.setARGB(250, 255, 0, 0);
      paint.setAntiAlias(true);
      paint.setFakeBoldText(true);

      // Create the circle
      int rad = 5;
      RectF oval = new RectF(myPoint.x-rad, myPoint.y-rad,
                             myPoint.x+rad, myPoint.y+rad);

      // Draw on the canvas
      canvas.drawOval(oval, paint);
      canvas.drawText("Red Circle", myPoint.x+rad, myPoint.y, paint);
    }
  }
  
  /**
   * Listing 13-17: Handling map-tap events
   */
  @Override
  public boolean onTap(GeoPoint point, MapView mapView) {
    // Perform hit test to see if this overlay is handling the click
//    if ([ ... perform hit test ... ]) {
//      // TODO [ ... execute on tap functionality ... ]
//      return true;
//    }

    // If not handled return false
    return false;
  }

}
