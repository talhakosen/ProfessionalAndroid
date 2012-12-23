/** Skeleton code for creating a new DB Helper */

package com.paad.chapter6;

import android.content.ContentValues;
import android.content.Context;
import android.database.*;
import android.database.sqlite.*;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.util.Log;

public class MyDBAdapter {
  private static final String DATABASE_NAME = "myDatabase.db";
  private static final String DATABASE_TABLE = "mainTable";
  private static final int DATABASE_VERSION = 1;
 
  // The index (key) column name for use in where clauses.
  public static final String KEY_ID = "_id";

  // The name and column index of each column in your database.
  public static final String KEY_NAME = "name"; 
  public static final int NAME_COLUMN = 1;
  // TODO: Create public field for each column in your table.
  
  // SQL Statement to create a new database.
  private static final String DATABASE_CREATE = "create table " + 
    DATABASE_TABLE + " (" + KEY_ID + 
    " integer primary key autoincrement, " +
    KEY_NAME + " text not null);";

  // Variable to hold the database instance
  private SQLiteDatabase db;
  // Context of the application using the database.
  private final Context context;
  // Database open/upgrade helper
  private myDbHelper dbHelper;

  public MyDBAdapter(Context _context) {
    context = _context;
    dbHelper = new myDbHelper(context, DATABASE_NAME, null, DATABASE_VERSION);
  }

  public MyDBAdapter open() throws SQLException {
    db = dbHelper.getWritableDatabase();
    return this;
  }

  public void close() {
    db.close();
  }

  public long insertEntry(MyObject _myObject) {
    ContentValues contentValues = new ContentValues();
    // TODO fill in ContentValue to represent the new row
   return db.insert(DATABASE_TABLE, null, contentValues);
  }

  public boolean removeEntry(long _rowIndex) {
    return db.delete(DATABASE_TABLE, KEY_ID + "=" + _rowIndex, null) > 0;
  }

  public Cursor getAllEntries () {
    return db.query(DATABASE_TABLE, new String[] {KEY_ID, KEY_NAME}, 
                    null, null, null, null, null);
  }

  public MyObject getEntry(long _rowIndex) {
    MyObject objectInstance = new MyObject();
    // TODO Return a cursor to a row from the database and
    // use the values to populate an instance of MyObject
    return objectInstance;
  }

  public int updateEntry(long _rowIndex, MyObject _myObject) {
    String where = KEY_ID + " = " + _rowIndex;
    ContentValues contentValues = new ContentValues();
    // TODO Fill in the ContentValue based on the new object
    return db.update(DATABASE_TABLE, contentValues, where, null);
  }

  private static class myDbHelper extends SQLiteOpenHelper {

    public myDbHelper(Context context, String name, CursorFactory factory, int version) {
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
      _db.execSQL("DROP TABLE IF EXISTS " + DATABASE_TABLE);
      // Create a new one.
      onCreate(_db);
    }
  }
 
  /** Dummy object to allow class to compile */
  static class MyObject {
  }
}
