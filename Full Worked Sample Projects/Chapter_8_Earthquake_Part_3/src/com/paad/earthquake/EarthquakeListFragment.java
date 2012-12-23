package com.paad.earthquake;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import android.app.ListFragment;
import android.app.LoaderManager;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.SimpleCursorAdapter;

public class EarthquakeListFragment extends ListFragment implements LoaderManager.LoaderCallbacks<Cursor> {
  
  SimpleCursorAdapter adapter;

  @Override
  public void onActivityCreated(Bundle savedInstanceState) {
    super.onActivityCreated(savedInstanceState);

    // Create a new Adapter and bind it to the List View
    adapter = new SimpleCursorAdapter(getActivity(),
      android.R.layout.simple_list_item_1, null,
      new String[] { EarthquakeProvider.KEY_SUMMARY },
      new int[] { android.R.id.text1 }, 0);
    setListAdapter(adapter);

    getLoaderManager().initLoader(0, null, this);  

    Thread t = new Thread(new Runnable() {
      public void run() {
        refreshEarthquakes(); 
      }
    });
    t.start();
  }
  
  private static final String TAG = "EARTHQUAKE";
  
  Handler handler = new Handler();
  
  public void refreshEarthquakes() {
    handler.post(new Runnable() {
      public void run() {
        getLoaderManager().restartLoader(0, null, EarthquakeListFragment.this); 
      }
    });

    // Get the XML
    URL url;
    try {
      String quakeFeed = getString(R.string.quake_feed);
      url = new URL(quakeFeed);

      URLConnection connection;
      connection = url.openConnection();

      HttpURLConnection httpConnection = (HttpURLConnection)connection;
      int responseCode = httpConnection.getResponseCode();

      if (responseCode == HttpURLConnection.HTTP_OK) {
        InputStream in = httpConnection.getInputStream();

        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = dbf.newDocumentBuilder();

        // Parse the earthquake feed.
        Document dom = db.parse(in);
        Element docEle = dom.getDocumentElement();

        // Get a list of each earthquake entry.
        NodeList nl = docEle.getElementsByTagName("entry");
        if (nl != null && nl.getLength() > 0) {
          for (int i = 0 ; i < nl.getLength(); i++) {
            Element entry = (Element)nl.item(i);
            Element title = (Element)entry.getElementsByTagName("title").item(0);
            Element g = (Element)entry.getElementsByTagName("georss:point").item(0);
            Element when = (Element)entry.getElementsByTagName("updated").item(0);
            Element link = (Element)entry.getElementsByTagName("link").item(0);

            String details = title.getFirstChild().getNodeValue();
            String hostname = "http://earthquake.usgs.gov";
            String linkString = hostname + link.getAttribute("href");

            String point = g.getFirstChild().getNodeValue();
            String dt = when.getFirstChild().getNodeValue(); 
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss'Z'");
            Date qdate = new GregorianCalendar(0,0,0).getTime();
            try {
              qdate = sdf.parse(dt);
            } catch (ParseException e) {
              Log.d(TAG, "Date parsing exception.", e);
            }

            String[] location = point.split(" ");
            Location l = new Location("dummyGPS");
            l.setLatitude(Double.parseDouble(location[0]));
            l.setLongitude(Double.parseDouble(location[1]));

            String magnitudeString = details.split(" ")[1];
            int end =  magnitudeString.length()-1;
            double magnitude = Double.parseDouble(magnitudeString.substring(0, end));

            details = details.split(",")[1].trim();

            Quake quake = new Quake(qdate, details, l, magnitude, linkString);

            // Process a newly found earthquake
            addNewQuake(quake);
          }
        }
      }
    } catch (MalformedURLException e) {
      Log.d(TAG, "MalformedURLException", e);
    } catch (IOException e) {
      Log.d(TAG, "IOException", e);
    } catch (ParserConfigurationException e) {
      Log.d(TAG, "Parser Configuration Exception", e);
    } catch (SAXException e) {
      Log.d(TAG, "SAX Exception", e);
    }
    finally {
    }
  }

  private void addNewQuake(Quake _quake) {
    ContentResolver cr = getActivity().getContentResolver();
    // Construct a where clause to make sure we don't already have this
    // earthquake in the provider.
    String w = EarthquakeProvider.KEY_DATE + " = " + _quake.getDate().getTime();

    // If the earthquake is new, insert it into the provider.
    Cursor query = cr.query(EarthquakeProvider.CONTENT_URI, null, w, null, null);
    if (query.getCount()==0) {
      ContentValues values = new ContentValues();

      values.put(EarthquakeProvider.KEY_DATE, _quake.getDate().getTime());
      values.put(EarthquakeProvider.KEY_DETAILS, _quake.getDetails());   
      values.put(EarthquakeProvider.KEY_SUMMARY, _quake.toString());

      double lat = _quake.getLocation().getLatitude();
      double lng = _quake.getLocation().getLongitude();
      values.put(EarthquakeProvider.KEY_LOCATION_LAT, lat);
      values.put(EarthquakeProvider.KEY_LOCATION_LNG, lng);
      values.put(EarthquakeProvider.KEY_LINK, _quake.getLink());
      values.put(EarthquakeProvider.KEY_MAGNITUDE, _quake.getMagnitude());

      cr.insert(EarthquakeProvider.CONTENT_URI, values);
    }
    query.close();
  }

  public Loader<Cursor> onCreateLoader(int id, Bundle args) {
    String[] projection = new String[] {
      EarthquakeProvider.KEY_ID,
      EarthquakeProvider.KEY_SUMMARY
    }; 

    Earthquake earthquakeActivity = (Earthquake)getActivity();
    String where = EarthquakeProvider.KEY_MAGNITUDE + " > " + 
                   earthquakeActivity.minimumMagnitude;
   
    CursorLoader loader = new CursorLoader(getActivity(), 
      EarthquakeProvider.CONTENT_URI, projection, where, null, null);
    
    return loader;
  }

  public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
    adapter.swapCursor(cursor);
  }

  public void onLoaderReset(Loader<Cursor> loader) {
    adapter.swapCursor(null);
  }


}