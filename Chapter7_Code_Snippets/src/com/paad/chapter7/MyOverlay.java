/**
 * The skeleton code demonstrates how to implement
 * your own Overlay class by overriding the draw
 * and onTap handlers.
 */

package com.paad.chapter7;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.RectF;
import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;
import com.google.android.maps.Projection;

public class MyOverlay extends Overlay {
    
	@Override
	public void draw(Canvas canvas, MapView mapView, boolean shadow) {
	  Projection projection = mapView.getProjection();
	  GeoPoint geoPoint = new GeoPoint(-31960906, 115844822);

	  if (shadow == false) {
	    Point myPoint = new Point();
	    projection.toPixels(geoPoint, myPoint);

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
	    canvas.drawText("Red Circle", myPoint.x, myPoint.y, paint);
	  }
	}
	  
	@Override
	public boolean onTap(GeoPoint point, MapView mapView) {
	  // Perform hit test to see if this overlay is handling the click
	  if (false){//[... perform hit test ... ]) {
	    //[ ... execute on tap functionality ... ]
	    return true;
	  }
	  return false;
	}
	
}