package com.paad.earthquake;

import java.util.ArrayList;
import android.database.Cursor;
import android.database.DataSetObserver;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.RectF;
import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;
import com.google.android.maps.Projection;

public class EarthquakeOverlay extends Overlay {

	Cursor earthquakes; 
	ArrayList<GeoPoint> quakeLocations;
	
  int rad = 5;
	
	public EarthquakeOverlay(Cursor cursor) {
		super();
		
		earthquakes = cursor;
		quakeLocations = new ArrayList<GeoPoint>();
		
		refreshQuakeLocations();
		
		earthquakes.registerDataSetObserver(new DataSetObserver() {
			@Override
			public void onChanged() {
				refreshQuakeLocations();
			}
		});
	}
	
	/** Update Earthquake location list from the provider */
	private void refreshQuakeLocations() {
		if (earthquakes.moveToFirst())
		  do { 
        Double lat = earthquakes.getFloat(EarthquakeProvider.LATITUDE_COLUMN)*1E6;
        Double lng = earthquakes.getFloat(EarthquakeProvider.LONGITUDE_COLUMN)*1E6;
              
		    GeoPoint geoPoint = new GeoPoint(lng.intValue(), lat.intValue());

		    quakeLocations.add(geoPoint);
		    
		  } while(earthquakes.moveToNext());
	}
	
	@Override
	public void draw(Canvas canvas, MapView mapView, boolean shadow) {
	  Projection projection = mapView.getProjection();

	  // Create and setup your paint brush
	  Paint paint = new Paint();
	  paint.setARGB(250, 255, 0, 0);
	  paint.setAntiAlias(true);
	  paint.setFakeBoldText(true);

	  if (shadow == false) {
	    for (GeoPoint point : quakeLocations) {
	      Point myPoint = new Point();
	      projection.toPixels(point, myPoint);

	      RectF oval = new RectF(myPoint.x-rad, myPoint.y-rad, 
	                             myPoint.x+rad, myPoint.y+rad);

	      canvas.drawOval(oval, paint);
	    }
	  }
	}

}