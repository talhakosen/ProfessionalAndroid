package com.paad.DatabaseSkeleton;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.os.Environment;
import android.os.ParcelFileDescriptor;
import android.text.TextUtils;
import android.util.Log;


public class MyHoardContentProvider extends ContentProvider {
  
  public static final Uri CONTENT_URI = Uri.parse("content://com.paad.hoardcontentprovider/hoards");
  static final String TAG = "MYHOARDCONTENTPROVIDER";
  
  //Create the constants used to differentiate between the different URI
  //requests.
  private static final int ALLROWS = 1;
  private static final int SINGLE_ROW = 2;
  
  private static final UriMatcher uriMatcher;
  
  //Populate the UriMatcher object, where a URI ending in Ôitems' will
  //correspond to a request for all items, and Ôitems/[rowID]Õ
  //represents a single row.
  static {
   uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
   uriMatcher.addURI("com.paad.hoardcontentprovider", "hoards", ALLROWS);
   uriMatcher.addURI("com.paad.hoardcontentprovider", "hoards/#", SINGLE_ROW);
  }
  
  // The index (key) column name for use in where clauses.
  public static final String KEY_ID = "_id";

  // The name and column index of each column in your database.
  // These should be descriptive.
  public static final String KEY_GOLD_HOARD_NAME_COLUMN = "GOLD_HOARD_NAME_COLUMN";
  public static final String KEY_GOLD_HOARD_ACCESSIBLE_COLUMN = "OLD_HOARD_ACCESSIBLE_COLUMN";
  public static final String KEY_GOLD_HOARDED_COLUMN = "GOLD_HOARDED_COLUMN";
  public static final String KEY_GOLD_HOARD_IMAGE_COLUMN = "_data";

  // Database open/upgrade helper
  private HoardDBOpenHelper hoardDBOpenHelper;

//  public MyHoardContentProvider(Context context) {
//    hoardDBOpenHelper = new HoardDBOpenHelper(context, HoardDBOpenHelper.DATABASE_NAME, null, 
//                                              HoardDBOpenHelper.DATABASE_VERSION);
//  }
  
  @Override
  public boolean onCreate() {
    // Construct the underlying database.
    // Defer opening the database until you need to perform
    // a query or transaction.
    hoardDBOpenHelper = new HoardDBOpenHelper(getContext(),
        HoardDBOpenHelper.DATABASE_NAME, null, 
        HoardDBOpenHelper.DATABASE_VERSION);
    
    return true;
  }
  
  @Override
  public Cursor query(Uri uri, String[] projection, String selection,
      String[] selectionArgs, String sortOrder) {
    // Open a read-only database.
    SQLiteDatabase db = hoardDBOpenHelper.getReadableDatabase();
  
    // Replace these with valid SQL statements if necessary.
    String groupBy = null;
    String having = null;
    
    SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
    queryBuilder.setTables(HoardDBOpenHelper.DATABASE_TABLE);
    
    // If this is a row query, limit the result set to the passed in row.
    switch (uriMatcher.match(uri)) {
      case SINGLE_ROW : 
        String rowID = uri.getPathSegments().get(1);
        queryBuilder.appendWhere(KEY_ID + "=" + rowID);
      default: break;
    }
    
    Cursor cursor = queryBuilder.query(db, projection, selection,
        selectionArgs, groupBy, having, sortOrder);
  
    return cursor;
  }

  @Override
  public int delete(Uri uri, String selection, String[] selectionArgs) {
    // Open a read / write database to support the transaction.
    SQLiteDatabase db = hoardDBOpenHelper.getWritableDatabase();
    
    // If this is a row URI, limit the deletion to the specified row.
    switch (uriMatcher.match(uri)) {
      case SINGLE_ROW : 
        String rowID = uri.getPathSegments().get(1);
        selection = KEY_ID + "=" + rowID
            + (!TextUtils.isEmpty(selection) ? 
              " AND (" + selection + ')' : "");
      default: break;
    }
    
    // To return the number of deleted items you must specify a where
    // clause. To delete all rows and return a value pass in "1".
    if (selection == null)
      selection = "1";
  
    return db.delete(HoardDBOpenHelper.DATABASE_TABLE, selection, selectionArgs);
  }
  
  
  @Override
  public Uri insert(Uri uri, ContentValues values) {
    // Open a read / write database to support the transaction.
    SQLiteDatabase db = hoardDBOpenHelper.getWritableDatabase();
    
    // To add empty rows to your database by passing in an empty Content Values object
    // you must use the null column hack parameter to specify the name of the column 
    // that can be set to null.
    String nullColumnHack = null;
    
    // Insert the values into the table
    long id = db.insert(HoardDBOpenHelper.DATABASE_TABLE, 
        nullColumnHack, values);
    
    // Construct and return the URI of the newly inserted row.
    if (id > -1)
      return ContentUris.withAppendedId(CONTENT_URI, id);
    else
      return null;
  }
  
  @Override
  public int update(Uri uri, ContentValues values, String selection,
    String[] selectionArgs) {
    
    // Open a read / write database to support the transaction.
    SQLiteDatabase db = hoardDBOpenHelper.getWritableDatabase();
    
    // If this is a row URI, limit the deletion to the specified row.
    switch (uriMatcher.match(uri)) {
      case SINGLE_ROW : 
        String rowID = uri.getPathSegments().get(1);
        selection = KEY_ID + "=" + rowID
            + (!TextUtils.isEmpty(selection) ? 
              " AND (" + selection + ')' : "");
      default: break;
    }
  
    return db.update(HoardDBOpenHelper.DATABASE_TABLE, 
      values, selection, selectionArgs);
  }
  
  @Override
  public String getType(Uri uri) {
    // Return a string that identifies the MIME type
    // for a Content Provider URI
    switch (uriMatcher.match(uri)) {
      case ALLROWS: return "vnd.android.cursor.dir/vnd.paad.hoarded";
      case SINGLE_ROW: return "vnd.android.cursor.item/vnd.paad.hoarded";
      default: throw new IllegalArgumentException("Unsupported URI: " + uri);
    }
  }
    
  /**
   * Listing 8-13: Storing files within your Content Provider
   */
  @Override
  public ParcelFileDescriptor openFile(Uri uri, String mode) 
    throws FileNotFoundException {

    // Find the row ID and use it as a filename.
    String rowID = uri.getPathSegments().get(1);
    
    // Create a file object in the application's external 
    // files directory.
    String picsDir = Environment.DIRECTORY_PICTURES;
    File file = 
      new File(getContext().getExternalFilesDir(picsDir), rowID);
    
    // If the file doesn't exist, create it now.
    if (!file.exists()) {
      try {
        file.createNewFile();
      } catch (IOException e) {
        Log.d(TAG, "File creation failed: " + e.getMessage());
      }
    }
    
    // Translate the mode parameter to the corresponding Parcel File
    // Descriptor open mode.
    int fileMode = 0;  
    if (mode.contains("w"))
      fileMode |= ParcelFileDescriptor.MODE_WRITE_ONLY;
    if (mode.contains("r")) 
      fileMode |= ParcelFileDescriptor.MODE_READ_ONLY;
    if (mode.contains("+")) 
      fileMode |= ParcelFileDescriptor.MODE_APPEND;     

    // Return a Parcel File Descriptor that represents the file.
    return ParcelFileDescriptor.open(file, fileMode);
  }

  private static class HoardDBOpenHelper extends SQLiteOpenHelper {
    
    private static final String DATABASE_NAME = "myDatabase.db";
    private static final String DATABASE_TABLE = "GoldHoards";
    private static final int DATABASE_VERSION = 1;
    
    // SQL Statement to create a new database.
    private static final String DATABASE_CREATE = "create table " +
      DATABASE_TABLE + " (" + KEY_ID +
      " integer primary key autoincrement, " +
      KEY_GOLD_HOARD_NAME_COLUMN + " text not null, " +
      KEY_GOLD_HOARDED_COLUMN + " float, " +
      KEY_GOLD_HOARD_ACCESSIBLE_COLUMN + " integer, " +
      KEY_GOLD_HOARD_IMAGE_COLUMN + " text);";

    public HoardDBOpenHelper(Context context, String name,
                      CursorFactory factory, int version) {
      super(context, name, factory, version);
    }

    // Called when no database exists in disk and the helper class needs
    // to create a new one.
    @Override
    public void onCreate(SQLiteDatabase _db) {
      _db.execSQL(DATABASE_CREATE);
    }

    // Called when there is a database version mismatch meaning that the version
    // of the database on disk needs to be upgraded to the current version.
    @Override
    public void onUpgrade(SQLiteDatabase _db, int _oldVersion, int _newVersion) {
      // Log the version upgrade.
      Log.w("TaskDBAdapter", "Upgrading from version " +
                             _oldVersion + " to " +
                             _newVersion + ", which will destroy all old data");

      // Upgrade the existing database to conform to the new version. Multiple
      // previous versions can be handled by comparing _oldVersion and _newVersion
      // values.

      // The simplest case is to drop the old table and create a new one.
      _db.execSQL("DROP TABLE IF IT EXISTS " + DATABASE_TABLE);
      // Create a new one.
      onCreate(_db);
    }
  }
}