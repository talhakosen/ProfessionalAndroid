package com.paad.earthquake;

import android.content.*;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;

public class EarthquakeProvider extends ContentProvider {

  public static final Uri CONTENT_URI = 
    Uri.parse("content://com.paad.provider.earthquake/earthquakes");

//Create the constants used to differentiate between the different URI 
//requests.
private static final int QUAKES = 1;
private static final int QUAKE_ID = 2;

private static final UriMatcher uriMatcher;

//Allocate the UriMatcher object, where a URI ending in 'earthquakes' will
//correspond to a request for all earthquakes, and 'earthquakes’ with a trailing '/[rowID]' will represent a single earthquake row.
static {
 uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
 uriMatcher.addURI("com.paad.provider.Earthquake", "earthquakes", QUAKES);
 uriMatcher.addURI("com.paad.provider.Earthquake", "earthquakes/#", QUAKE_ID);
}

  
@Override
public boolean onCreate() {
  Context context = getContext();

  earthquakeDatabaseHelper dbHelper = new earthquakeDatabaseHelper(context,  
                                                                 DATABASE_NAME, 
                                                                 null, 
                                                                 DATABASE_VERSION);
  earthquakeDB = dbHelper.getWritableDatabase();
  return (earthquakeDB == null) ? false : true;
}



  @Override
  public String getType(Uri uri) {
    switch (uriMatcher.match(uri)) {
      case QUAKES: return "vnd.android.cursor.dir/vnd.paad.earthquake";
      case QUAKE_ID: return "vnd.android.cursor.item/vnd.paad.earthquake";
      default: throw new IllegalArgumentException("Unsupported URI: " + uri);
    }
  }


  @Override
  public Cursor query(Uri uri, 
                      String[] projection, 
                      String selection, 
                      String[] selectionArgs, 
                      String sort) {
        
    SQLiteQueryBuilder qb = new SQLiteQueryBuilder();

    qb.setTables(EARTHQUAKE_TABLE);

    // If this is a row query, limit the result set to the passed in row. 
    switch (uriMatcher.match(uri)) {
      case QUAKE_ID: qb.appendWhere(KEY_ID + "=" + uri.getPathSegments().get(1));
                     break;
      default      : break;
    }

    // If no sort order is specified sort by date / time
    String orderBy;
    if (TextUtils.isEmpty(sort)) {
      orderBy = KEY_DATE;
    } else {
      orderBy = sort;
    }

    // Apply the query to the underlying database.
    Cursor c = qb.query(earthquakeDB, 
                        projection, 
                        selection, selectionArgs, 
                        null, null, 
                        orderBy);

    // Register the contexts ContentResolver to be notified if
    // the cursor result set changes. 
    c.setNotificationUri(getContext().getContentResolver(), uri);
    
    // Return a cursor to the query result.
    return c;
  }


  @Override
  public Uri insert(Uri _uri, ContentValues _initialValues) {
    // Insert the new row, will return the row number if 
    // successful.
    long rowID = earthquakeDB.insert(EARTHQUAKE_TABLE, "quake", _initialValues);
          
    // Return a URI to the newly inserted row on success.
    if (rowID > 0) {
      Uri uri = ContentUris.withAppendedId(CONTENT_URI, rowID);
      getContext().getContentResolver().notifyChange(uri, null);
      return uri;
    }
    throw new SQLException("Failed to insert row into " + _uri);
  }

  @Override
  public int delete(Uri uri, String where, String[] whereArgs) {
    int count;
    
    switch (uriMatcher.match(uri)) {
      case QUAKES:
        count = earthquakeDB.delete(EARTHQUAKE_TABLE, where, whereArgs);
        break;

      case QUAKE_ID:
        String segment = uri.getPathSegments().get(1);
        count = earthquakeDB.delete(EARTHQUAKE_TABLE, KEY_ID + "="
                                    + segment
                                    + (!TextUtils.isEmpty(where) ? " AND (" 
                                    + where + ')' : ""), whereArgs);
        break;

      default: throw new IllegalArgumentException("Unsupported URI: " + uri);
    }

    getContext().getContentResolver().notifyChange(uri, null);
    return count;
  }

  @Override
  public int update(Uri uri, ContentValues values, String where, String[] whereArgs) {
    int count;
    switch (uriMatcher.match(uri)) {
      case QUAKES: count = earthquakeDB.update(EARTHQUAKE_TABLE, values, 
                                               where, whereArgs);
                   break;

      case QUAKE_ID: String segment = uri.getPathSegments().get(1);
                     count = earthquakeDB.update(EARTHQUAKE_TABLE, values, KEY_ID 
                             + "=" + segment 
                             + (!TextUtils.isEmpty(where) ? " AND (" 
                             + where + ')' : ""), whereArgs);
                     break;

      default: throw new IllegalArgumentException("Unknown URI " + uri);
    }

    getContext().getContentResolver().notifyChange(uri, null);
    return count;
  }



  
//The underlying database
  private SQLiteDatabase earthquakeDB;

  private static final String TAG = "EarthquakeProvider";
  private static final String DATABASE_NAME = "earthquakes.db";
  private static final int DATABASE_VERSION = 2;
  private static final String EARTHQUAKE_TABLE = "earthquakes";

  // Column Names
  public static final String KEY_ID = "_id";
  public static final String KEY_DATE = "date";
  public static final String KEY_DETAILS = "details";
  public static final String KEY_LOCATION_LAT = "latitude";
  public static final String KEY_LOCATION_LNG = "longitude";
  public static final String KEY_MAGNITUDE = "magnitude";
  public static final String KEY_LINK = "link";

  // Column indexes
  public static final int DATE_COLUMN = 1;
  public static final int DETAILS_COLUMN = 2;
  public static final int LONGITUDE_COLUMN = 3;
  public static final int LATITUDE_COLUMN = 4;
  public static final int MAGNITUDE_COLUMN = 5;
  public static final int LINK_COLUMN = 6;
      
  // Helper class for opening, creating, and managing database version control
  private static class earthquakeDatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_CREATE =
      "create table " + EARTHQUAKE_TABLE + " (" 
      + KEY_ID + " integer primary key autoincrement, "
      + KEY_DATE + " LONG, "
      + KEY_DETAILS + " TEXT, "
      + KEY_LOCATION_LAT + " FLOAT, "
      + KEY_LOCATION_LNG + " FLOAT, "
      + KEY_MAGNITUDE + " FLOAT, "
      + KEY_LINK + " TEXT);";
        
    public earthquakeDatabaseHelper(Context context, String name,
                                    CursorFactory factory, int version) {
      super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
      db.execSQL(DATABASE_CREATE);           
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
      Log.w(TAG, "Upgrading database from version " + oldVersion + " to "
                  + newVersion + ", which will destroy all old data");
              
      db.execSQL("DROP TABLE IF EXISTS " + EARTHQUAKE_TABLE);
      onCreate(db);
    }
  }


}