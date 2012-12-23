package com.paad.PA4AD_Ch14_MyWidget;

import java.util.HashMap;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.provider.LiveFolders;

public class MyContentProvider extends ContentProvider {
  
  public static final Uri CONTENT_URI = Uri.parse("content://com.paad.appwidgets/livefolder");

  
  public static final String KEY_ID = "KEY_ID";
  public static final String KEY_NAME = "KEY_NAME";
  public static final String KEY_DESCRIPTION = "KEY_DESCRIPTION";
  public static final String KEY_IMAGE = "KEY_IMAGE";

  /**
   * Listing 14-28: Creating a projection to support a Live Folder
   */
  private static final HashMap<String, String> LIVE_FOLDER_PROJECTION;
  static {
    // Create the projection map.
    LIVE_FOLDER_PROJECTION = new HashMap<String, String>();

    // Map existing column names to those required by a Live Folder.
    LIVE_FOLDER_PROJECTION.put(LiveFolders._ID,
                             KEY_ID + " AS " +
                             LiveFolders._ID);
    LIVE_FOLDER_PROJECTION.put(LiveFolders.NAME,
                             KEY_NAME + " AS " +
                             LiveFolders.NAME);
    LIVE_FOLDER_PROJECTION.put(LiveFolders.DESCRIPTION,
                             KEY_DESCRIPTION + " AS " +
                             LiveFolders.DESCRIPTION);
    LIVE_FOLDER_PROJECTION.put(LiveFolders.ICON_BITMAP,
                             KEY_IMAGE + " AS " +
                             LiveFolders.ICON_BITMAP);
  }
  
  /**
   * Listing 14-29: Applying a projection to support a Live Folder
   */
  public static Uri LIVE_FOLDER_URI 
    = Uri.parse("com.paad.provider.MyLiveFolder");

  public Cursor query(Uri uri, String[] projection, String selection,
                      String[] selectionArgs, String sortOrder) {

    SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
    
    switch (URI_MATCHER.match(uri)) {
      case LIVE_FOLDER:
        qb.setTables(MYTABLE);
        qb.setProjectionMap(LIVE_FOLDER_PROJECTION);
        break;
      default:
        throw new IllegalArgumentException("Unknown URI " + uri);
    }

    Cursor c = qb.query(null, projection, selection, selectionArgs, 
                        null, null, null);

    c.setNotificationUri(getContext().getContentResolver(), uri);

    return c;
  }

  
  @Override
  public int delete(Uri arg0, String arg1, String[] arg2) {
    // TODO Auto-generated method stub
    return 0;
  }

  @Override
  public String getType(Uri arg0) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public Uri insert(Uri arg0, ContentValues arg1) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public boolean onCreate() {
    // TODO Auto-generated method stub
    return false;
  }

  private static final String MYTABLE = "mytable";
  private static final int LIVE_FOLDER = 4;

  public static UriMatcher URI_MATCHER;
  static {
    URI_MATCHER = new UriMatcher(UriMatcher.NO_MATCH);
    URI_MATCHER.addURI("com.paad.provider.MyLiveFolder", "live_folder", LIVE_FOLDER);
   }

  @Override
  public int update(Uri uri, ContentValues contentValues, String arg2, String[] arg3) {
    // TODO Auto-generated method stub
    return 0;
  }

}
