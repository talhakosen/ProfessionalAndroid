/**
 * Listing 8-14: A skeleton Content Provider implementation
 */

package com.paad.DatabaseSkeleton;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;

public class MyContentProvider extends ContentProvider {

  public static final Uri CONTENT_URI = Uri.parse("content://com.paad.skeletondatabaseprovider/elements");
  
  /**
   * Listing 8-8: Defining a UriMatcher to determine if a request is for all elements or a single row
   */
  //Create the constants used to differentiate between the different URI
  //requests.
  private static final int ALLROWS = 1;
  private static final int SINGLE_ROW = 2;
  
  private static final UriMatcher uriMatcher;
  
  //Populate the UriMatcher object, where a URI ending in 
  //'elements' will correspond to a request for all items,
  //and 'elements/[rowID]' represents a single row.
  static {
   uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
   uriMatcher.addURI("com.paad.skeletondatabaseprovider", 
                     "elements", ALLROWS);
   uriMatcher.addURI("com.paad.skeletondatabaseprovider", 
                     "elements/#", SINGLE_ROW);
  }
  /** */
  
  // The index (key) column name for use in where clauses.
  public static final String KEY_ID = "_id";

  // The name and column index of each column in your database.
  // These should be descriptive.
  public static final String KEY_COLUMN_1_NAME = "KEY_COLUMN_1_NAME";
  // TODO: Create public field for each column in your table.
  
  /**
   * Listing 8-9: Creating the Content ProviderÕs database
   */
  private MySQLiteOpenHelper myOpenHelper;

  @Override
  public boolean onCreate() {
    // Construct the underlying database.
    // Defer opening the database until you need to perform
    // a query or transaction.
    myOpenHelper = new MySQLiteOpenHelper(getContext(),
        MySQLiteOpenHelper.DATABASE_NAME, null, 
        MySQLiteOpenHelper.DATABASE_VERSION);
    
    return true;
  }
  
  /**
   * Listing 8-10: Implementing queries and transactions within a Content Provider
   */
  @Override
  public Cursor query(Uri uri, String[] projection, String selection,
    String[] selectionArgs, String sortOrder) {

    // Open thedatabase.
    SQLiteDatabase db;
    try {
      db = myOpenHelper.getWritableDatabase();
    } catch (SQLiteException ex) {
      db = myOpenHelper.getReadableDatabase();
    }

    // Replace these with valid SQL statements if necessary.
    String groupBy = null;
    String having = null;
    
    // Use an SQLite Query Builder to simplify constructing the 
    // database query.
    SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();

    // If this is a row query, limit the result set to the passed in row.
    switch (uriMatcher.match(uri)) {
      case SINGLE_ROW : 
        String rowID = uri.getPathSegments().get(1);
        queryBuilder.appendWhere(KEY_ID + "=" + rowID);
      default: break;
    }

    // Specify the table on which to perform the query. This can 
    // be a specific table or a join as required.  
    queryBuilder.setTables(MySQLiteOpenHelper.DATABASE_TABLE);

    // Execute the query.
    Cursor cursor = queryBuilder.query(db, projection, selection,
        selectionArgs, groupBy, having, sortOrder);

    // Return the result Cursor.
    return cursor;
  }
  
  /**
   * Listing 8-11: Returning a Content Provider MIME type
   */
  @Override
  public String getType(Uri uri) {
    // Return a string that identifies the MIME type
    // for a Content Provider URI
    switch (uriMatcher.match(uri)) {
      case ALLROWS:
        return "vnd.android.cursor.dir/vnd.paad.elemental";
      case SINGLE_ROW: 
        return "vnd.android.cursor.item/vnd.paad.elemental";
      default: 
        throw new IllegalArgumentException("Unsupported URI: " +
                                             uri);
    }
  }

  /**
   * Listing 8-12: Typical Content Provider transaction implementations
   */
  @Override
  public int delete(Uri uri, String selection, String[] selectionArgs) {
    // Open a read / write database to support the transaction.
    SQLiteDatabase db = myOpenHelper.getWritableDatabase();
    
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

    // Perform the deletion.
    int deleteCount = db.delete(MySQLiteOpenHelper.DATABASE_TABLE, 
      selection, selectionArgs);
    
    // Notify any observers of the change in the data set.
    getContext().getContentResolver().notifyChange(uri, null);
    
    // Return the number of deleted items.
    return deleteCount;
  }

  @Override
  public Uri insert(Uri uri, ContentValues values) {
    // Open a read / write database to support the transaction.
    SQLiteDatabase db = myOpenHelper.getWritableDatabase();
    
    // To add empty rows to your database by passing in an empty 
    // Content Values object you must use the null column hack
    // parameter to specify the name of the column that can be 
    // set to null.
    String nullColumnHack = null;
    
    // Insert the values into the table
    long id = db.insert(MySQLiteOpenHelper.DATABASE_TABLE, 
        nullColumnHack, values);
    
    // Construct and return the URI of the newly inserted row.
    if (id > -1) {
      // Construct and return the URI of the newly inserted row.
      Uri insertedId = ContentUris.withAppendedId(CONTENT_URI, id);
        
      // Notify any observers of the change in the data set.
      getContext().getContentResolver().notifyChange(insertedId, null);
        
      return insertedId;
    }
    else
      return null;
  }

  @Override
  public int update(Uri uri, ContentValues values, String selection,
    String[] selectionArgs) {
    
    // Open a read / write database to support the transaction.
    SQLiteDatabase db = myOpenHelper.getWritableDatabase();
    
    // If this is a row URI, limit the deletion to the specified row.
    switch (uriMatcher.match(uri)) {
      case SINGLE_ROW : 
        String rowID = uri.getPathSegments().get(1);
        selection = KEY_ID + "=" + rowID
            + (!TextUtils.isEmpty(selection) ? 
              " AND (" + selection + ')' : "");
      default: break;
    }

    // Perform the update.
    int updateCount = db.update(MySQLiteOpenHelper.DATABASE_TABLE, 
      values, selection, selectionArgs);
    
    // Notify any observers of the change in the data set.
    getContext().getContentResolver().notifyChange(uri, null);
    
    return updateCount;
  }

  private static class MySQLiteOpenHelper extends SQLiteOpenHelper {

    // Database name, version, and table names.
    private static final String DATABASE_NAME = "myDatabase.db";
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_TABLE = "mainTable";

    // SQL Statement to create a new database.
    private static final String DATABASE_CREATE = "create table " +
      DATABASE_TABLE + " (" + KEY_ID +
      " integer primary key autoincrement, " +
      KEY_COLUMN_1_NAME + " text not null);";

    public MySQLiteOpenHelper(Context context, String name,
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
