package com.paad.DatabaseSkeleton;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class MyHoardDatabase {
  /**
   * Listing 8-1: Skeleton code for contract class constants
   */
  //The index (key) column name for use in where clauses.
  public static final String KEY_ID = "_id";
  
  //The name and column index of each column in your database.
  //These should be descriptive.
  public static final String KEY_GOLD_HOARD_NAME_COLUMN =  
   "GOLD_HOARD_NAME_COLUMN";
  public static final String KEY_GOLD_HOARD_ACCESSIBLE_COLUMN =
   "OLD_HOARD_ACCESSIBLE_COLUMN";
  public static final String KEY_GOLD_HOARDED_COLUMN =
   "GOLD_HOARDED_COLUMN";
  //TODO: Create public field for each column in your table.
  /***/


  // Database open/upgrade helper
  private HoardDBOpenHelper hoardDBOpenHelper;

  public MyHoardDatabase(Context context) {
    hoardDBOpenHelper = new HoardDBOpenHelper(context, HoardDBOpenHelper.DATABASE_NAME, null, 
                                              HoardDBOpenHelper.DATABASE_VERSION);
  }
  
  // Called when you no longer need access to the database.
  public void closeDatabase() {
    hoardDBOpenHelper.close();
  }

  private Cursor getAccessibleHoard() {
    /**
     * Listing 8-3: Querying a database
     */
    // Specify the result column projection. Return the minimum set
    // of columns required to satisfy your requirements.
    String[] result_columns = new String[] { 
      KEY_ID, KEY_GOLD_HOARD_ACCESSIBLE_COLUMN, KEY_GOLD_HOARDED_COLUMN }; 
    
    // Specify the where clause that will limit our results.
    String where = KEY_GOLD_HOARD_ACCESSIBLE_COLUMN + "=" + 1;
    
    // Replace these with valid SQL statements as necessary.
    String whereArgs[] = null;
    String groupBy = null;
    String having = null;
    String order = null;
    
    SQLiteDatabase db = hoardDBOpenHelper.getWritableDatabase();
    Cursor cursor = db.query(HoardDBOpenHelper.DATABASE_TABLE, 
                             result_columns, where,
                             whereArgs, groupBy, having, order);
    //
    return cursor;
  }
  
  public float getAverageAccessibleHoardValue() {
    Cursor cursor = getAccessibleHoard();
    
    /**
     * Listing 8-4: Extracting values from a Cursor
     */
    float totalHoard = 0f;
    float averageHoard = 0f;

    // Find the index to the column(s) being used.
    int GOLD_HOARDED_COLUMN_INDEX =
      cursor.getColumnIndexOrThrow(KEY_GOLD_HOARDED_COLUMN);

    // Iterate over the cursors rows. 
    // The Cursor is initialized at before first, so we can 
    // check only if there is a "next" row available. If the
    // result Cursor is empty this will return false.
    while (cursor.moveToNext()) {
      float hoard = cursor.getFloat(GOLD_HOARDED_COLUMN_INDEX);
      totalHoard += hoard;
    }

    // Calculate an average -- checking for divide by zero errors.
    float cursorCount = cursor.getCount();
    averageHoard = cursorCount > 0 ? 
                     (totalHoard / cursorCount) : Float.NaN;

    // Close the Cursor when you've finished with it.
    cursor.close();
    
    return averageHoard;
  }
  
  public void addNewHoard(String hoardName, float hoardValue, boolean hoardAccessible) {
    /**
     * Listing 8-5: Inserting new rows into a database
     */
    // Create a new row of values to insert.
    ContentValues newValues = new ContentValues();
  
    // Assign values for each row.
    newValues.put(KEY_GOLD_HOARD_NAME_COLUMN, hoardName);
    newValues.put(KEY_GOLD_HOARDED_COLUMN, hoardValue);
    newValues.put(KEY_GOLD_HOARD_ACCESSIBLE_COLUMN, hoardAccessible);
    // [ ... Repeat for each column / value pair ... ]
  
    // Insert the row into your table
    SQLiteDatabase db = hoardDBOpenHelper.getWritableDatabase();
    db.insert(HoardDBOpenHelper.DATABASE_TABLE, null, newValues); 
  }
  
  public void updateHoardValue(int hoardId, float newHoardValue) {
    /**
     * Listing 8-6: Updating a database row
     */
    // Create the updated row Content Values.
    ContentValues updatedValues = new ContentValues();
  
    // Assign values for each row.
    updatedValues.put(KEY_GOLD_HOARDED_COLUMN, newHoardValue);
    // [ ... Repeat for each column to update ... ]
  
    // Specify a where clause the defines which rows should be
    // updated. Specify where arguments as necessary.
    String where = KEY_ID + "=" + hoardId;
    String whereArgs[] = null;
  
    // Update the row with the specified index with the new values.
    SQLiteDatabase db = hoardDBOpenHelper.getWritableDatabase();
    db.update(HoardDBOpenHelper.DATABASE_TABLE, updatedValues, 
              where, whereArgs);
  }
  
  public void deleteEmptyHoards() {
    /**
     * Listing 8-7: Deleting a database row
     */
    // Specify a where clause that determines which row(s) to delete.
    // Specify where arguments as necessary.
    String where = KEY_GOLD_HOARDED_COLUMN + "=" + 0;
    String whereArgs[] = null;
  
    // Delete the rows that match the where clause.
    SQLiteDatabase db = hoardDBOpenHelper.getWritableDatabase();
    db.delete(HoardDBOpenHelper.DATABASE_TABLE, where, whereArgs);
  }

  /**
   * Listing 8-2: Implementing an SQLite Open Helper
   */
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
      KEY_GOLD_HOARD_ACCESSIBLE_COLUMN + " integer);";

    public HoardDBOpenHelper(Context context, String name,
                      CursorFactory factory, int version) {
      super(context, name, factory, version);
    }

    // Called when no database exists in disk and the helper class needs
    // to create a new one.
    @Override
    public void onCreate(SQLiteDatabase db) {
      db.execSQL(DATABASE_CREATE);
    }

    // Called when there is a database version mismatch meaning that
    // the version of the database on disk needs to be upgraded to
    // the current version.
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, 
                          int newVersion) {
      // Log the version upgrade.
      Log.w("TaskDBAdapter", "Upgrading from version " +
        oldVersion + " to " +
        newVersion + ", which will destroy all old data");

      // Upgrade the existing database to conform to the new 
      // version. Multiple previous versions can be handled by 
      // comparing oldVersion and newVersion values.

      // The simplest case is to drop the old table and create a new one.
      db.execSQL("DROP TABLE IF IT EXISTS " + DATABASE_TABLE);
      // Create a new one.
      onCreate(db);
    }
  }
}