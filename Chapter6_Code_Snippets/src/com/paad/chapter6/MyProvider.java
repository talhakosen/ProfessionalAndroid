package com.paad.chapter6;

import android.content.*;
import android.database.Cursor;
import android.database.SQLException;
import android.net.Uri;

public class MyProvider extends ContentProvider {

  private static final String myURI = "content://com.paad.provider.myApp/items";
  public static final Uri CONTENT_URI = Uri.parse(myURI);

  @Override
  public boolean onCreate() {
    // TODO Construct the underlying database.
   return true;
  }

  // Create the constants used to differentiate between the different URI 
  // requests.
  private static final int ALLROWS = 1;
  private static final int SINGLE_ROW = 2;
	  
  private static final UriMatcher uriMatcher;
  // Populate the UriMatcher object, where a URI ending in 'items' will
  // correspond to a request for all items, and 'items/[rowID]' 
  // represents a single row.
  static {
    uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
    uriMatcher.addURI("com.paad.provider.myApp", "items", ALLROWS);
    uriMatcher.addURI("com.paad.provider.myApp", "items/#", SINGLE_ROW);
  }
	  
  @Override
  public Cursor query(Uri uri, 
                      String[] projection, 
                      String selection, 
                      String[] selectionArgs, 
                      String sort) {
	        
    // If this is a row query, limit the result set to the passed in row.        
    switch (uriMatcher.match(uri)) {
      case SINGLE_ROW : 
        // TODO: Modify selection based on row id, where:
        // rowNumber = uri.getPathSegments().get(1));
    }
    return null;
  }

  @Override
  public Uri insert(Uri _uri, ContentValues _initialValues) {
    long rowID = 1;//[ ... Add a new item ... ]
	          
    // Return a URI to the newly added item.
    if (rowID > 0) {
      return ContentUris.withAppendedId(CONTENT_URI, rowID);
    }
    throw new SQLException("Failed to add new item into " + _uri);
  }

  @Override
  public int delete(Uri uri, String where, String[] whereArgs) {
    switch (uriMatcher.match(uri)) {
      case ALLROWS: 
      case SINGLE_ROW:
      default: throw new IllegalArgumentException("Unsupported URI:" + uri);
    }
  }

  @Override
  public int update(Uri uri, ContentValues values, String where, String[] whereArgs) {
    switch (uriMatcher.match(uri)) {
      case ALLROWS: 
      case SINGLE_ROW:
      default: throw new IllegalArgumentException("Unsupported URI:" + uri);
    }
  }

  @Override
  public String getType(Uri _uri) {
    switch (uriMatcher.match(_uri)) {
      case ALLROWS: return "vnd.paad.cursor.dir/myprovidercontent";
      case SINGLE_ROW: return "vnd.paad.cursor.item/myprovidercontent";
      default: throw new IllegalArgumentException("Unsupported URI: " + _uri);
    }
  }
  
}