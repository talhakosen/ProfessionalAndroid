package com.paad.earthquake;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.google.android.maps.GeoPoint;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.database.Cursor;
import android.location.Location;
import android.os.IBinder;
import android.os.RemoteException;
import android.os.SystemClock;

public class EarthquakeService extends Service {

	@Override
	public void onStart(Intent intent, int startId) {
	  // Retrieve the shared preferences
	  SharedPreferences prefs = getSharedPreferences(Preferences.USER_PREFERENCE,
	                                                 Activity.MODE_PRIVATE);

	  int minMagIndex = prefs.getInt(Preferences.PREF_MIN_MAG, 0);
	  if (minMagIndex < 0)
	    minMagIndex = 0;

	  int freqIndex = prefs.getInt(Preferences.PREF_UPDATE_FREQ, 0);
	  if (freqIndex < 0)
	    freqIndex = 0;

	  boolean autoUpdate = prefs.getBoolean(Preferences.PREF_AUTO_UPDATE, false);

	  Resources r = getResources();
	  int[] freqValues = r.getIntArray(R.array.update_freq_values);

	  int updateFreq = freqValues[freqIndex];

	  if (autoUpdate) {
		  int alarmType = AlarmManager.ELAPSED_REALTIME_WAKEUP;
		  long timeToRefresh = SystemClock.elapsedRealtime() + updateFreq*60*1000;
		  alarms.set(alarmType, timeToRefresh, alarmIntent);
	  }
	  else
	    alarms.cancel(alarmIntent);

      refreshEarthquakes();
	};

	private Notification newEarthquakeNotification;
	public static final int NOTIFICATION_ID = 1;

	AlarmManager alarms;
	PendingIntent alarmIntent;
	
	@Override
	public void onCreate() {
  int icon = R.drawable.icon;
  String tickerText = "New Earthquake Detected";
  long when = System.currentTimeMillis();

  newEarthquakeNotification = new Notification(icon, tickerText, when);
  
   alarms = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
  
  String ALARM_ACTION = EarthquakeAlarmReceiver.ACTION_REFRESH_EARTHQUAKE_ALARM; 
  Intent intentToFire = new Intent(ALARM_ACTION);
  alarmIntent = PendingIntent.getBroadcast(this, 0, intentToFire, 0);
	}


	@Override
	public IBinder onBind(Intent intent) {
	  return myEarthquakeServiceStub;
	}

	IBinder myEarthquakeServiceStub = new IEarthquakeService.Stub() {
	  public void refreshEarthquakes() throws RemoteException {
	    EarthquakeService.this.refreshEarthquakes();
	  }

	  public List<Quake> getEarthquakes() throws RemoteException {
	    ArrayList<Quake> result = new ArrayList<Quake>();

	    ContentResolver cr = EarthquakeService.this.getContentResolver();
	    Cursor c = cr.query(EarthquakeProvider.CONTENT_URI, null, null, null, null);
	    if (c.moveToFirst())
	      do { 

	        Double lat = c.getDouble(EarthquakeProvider.LATITUDE_COLUMN);
	        Double lng = c.getDouble(EarthquakeProvider.LONGITUDE_COLUMN);
	        Location location = new Location("dummy");
	        location.setLatitude(lat);
	        location.setLongitude(lng);

	        String details = c.getString(EarthquakeProvider.DETAILS_COLUMN);
	        String link =  c.getString(EarthquakeProvider.LINK_COLUMN);

	        double magnitude = c.getDouble(EarthquakeProvider.MAGNITUDE_COLUMN);

	        long datems =  c.getLong(EarthquakeProvider.DATE_COLUMN);
	        Date date = new Date(datems);

	        result.add(new Quake(date, details, location, magnitude, link));
	      } while(c.moveToNext());

	    return result;
	  }
	};


  
  public static final String NEW_EARTHQUAKE_FOUND = "New_Earthquake_Found";

  private void addNewQuake(Quake _quake) {
    ContentResolver cr = getContentResolver();
    // Construct a where clause to make sure we don’t already have this 
    // earthquake in the provider.
    String w = EarthquakeProvider.KEY_DATE + " = " + _quake.getDate().getTime();

    // If the earthquake is new, insert it into the provider.
    Cursor c = cr.query(EarthquakeProvider.CONTENT_URI, null, w, null, null);
    int dbCount = c.getCount();
    c.close();
    
    if (dbCount == 0){
      ContentValues values = new ContentValues();

      values.put(EarthquakeProvider.KEY_DATE, _quake.getDate().getTime());
      values.put(EarthquakeProvider.KEY_DETAILS, _quake.getDetails());

      double lat = _quake.getLocation().getLatitude();
      double lng = _quake.getLocation().getLongitude();
      values.put(EarthquakeProvider.KEY_LOCATION_LAT, lat);
      values.put(EarthquakeProvider.KEY_LOCATION_LNG, lng);
      values.put(EarthquakeProvider.KEY_LINK, _quake.getLink());
      values.put(EarthquakeProvider.KEY_MAGNITUDE, _quake.getMagnitude());

      cr.insert(EarthquakeProvider.CONTENT_URI, values);
       announceNewQuake(_quake);
    }
  }

  private void announceNewQuake(Quake quake) {
	  String svcName = Context.NOTIFICATION_SERVICE;
	  NotificationManager notificationManager;
	  notificationManager = (NotificationManager)getSystemService(svcName);

	  Context context = getApplicationContext();
	  String expandedText = quake.getDate().toString();
	  String expandedTitle = "M:" + quake.getMagnitude() + " " + quake.getDetails(); 
	  Intent startActivityIntent = new Intent(this, Earthquake.class);
	  PendingIntent launchIntent = PendingIntent.getActivity(context, 
	                                                         0, 
	                                                         startActivityIntent,
	                                                         0);

	  newEarthquakeNotification.setLatestEventInfo(context, 
	                                               expandedTitle, 
	                                               expandedText,
	                                               launchIntent);
	  newEarthquakeNotification.when = java.lang.System.currentTimeMillis();

//	    if (quake.getMagnitude() > 6) {
//		  Uri ringURI = Uri.fromFile(new File("/system/media/audio/ringtones/ringer.mp3"));
//		  newEarthquakeNotification.sound = ringURI;
//		} 
//
//	    double vibrateLength = 100*Math.exp(0.53*quake.getMagnitude());
//	    long[] vibrate = new long[] {100, 100, (long)vibrateLength };
//	    newEarthquakeNotification.vibrate = vibrate;
//	    
//	    int color;        
//	    if (quake.getMagnitude() < 5.4)
//	      color = Color.GREEN;
//	    else if (quake.getMagnitude() < 6)
//	      color = Color.YELLOW;
//	    else
//	      color = Color.RED;
//	                 
//	    newEarthquakeNotification.ledARGB = color;
//	    newEarthquakeNotification.ledOffMS = (int)vibrateLength;
//	    newEarthquakeNotification.ledOnMS = (int)vibrateLength;
//	    newEarthquakeNotification.flags = newEarthquakeNotification.flags | 
//        Notification.FLAG_SHOW_LIGHTS;
  
	  notificationManager.notify(NOTIFICATION_ID, newEarthquakeNotification);	  

		 Intent intent = new Intent(NEW_EARTHQUAKE_FOUND);
		 intent.putExtra("date", quake.getDate().getTime());
		 intent.putExtra("details", quake.getDetails());
		 intent.putExtra("longitude", quake.getLocation().getLongitude());
		 intent.putExtra("latitude", quake.getLocation().getLatitude());
		 intent.putExtra("magnitude", quake.getMagnitude());

	  sendBroadcast(intent);
	}

  private void refreshEarthquakes() {
    Thread updateThread = new Thread(null, backgroundRefresh, "refresh_earthquake");  
	updateThread.start();        
  }
	
	private Runnable backgroundRefresh = new Runnable() {
	  public void run() {
	      doRefreshEarthquakes();
	  }        
	};

  
  private void doRefreshEarthquakes() {
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
	            e.printStackTrace();
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
	    e.printStackTrace();
	  } catch (IOException e) {
	    e.printStackTrace();
	  } catch (ParserConfigurationException e) {
	    e.printStackTrace();
	  } catch (SAXException e) {
	    e.printStackTrace();
	  }
	  finally {
	  }
	}
}