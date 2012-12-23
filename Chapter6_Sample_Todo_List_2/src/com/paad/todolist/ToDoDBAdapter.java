package com.paad.todolist;

import java.util.Date;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.util.Log;

/** Database Adapter for persisting Todo Items */
public class ToDoDBAdapter {
  private static final String DATABASE_NAME = "todoList.db";
  private static final String DATABASE_TABLE = "todoItems";
  private static final int DATABASE_VERSION = 1;
  
  public static final String KEY_ID = "_id";
  public static final String KEY_TASK = "task";
  public static final String KEY_CREATION_DATE = "creation_date";
  
  public static final int TASK_COLUMN = 1;
  public static final int CREATION_DATE_COLUMN = 2;
    
  private SQLiteDatabase db;
  private final Context context;
  private toDoDBOpenHelper dbHelper;

  public ToDoDBAdapter(Context _context) {
    context = _context;
    dbHelper = new toDoDBOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION);
  }

  /** Close the database */
  public void close() {
	  db.close();
	}

  /** Open the database */
  public void open() throws SQLiteException {  
	  try {
		  db = dbHelper.getWritableDatabase();
	  }
	  catch (SQLiteException ex) {
		  db = dbHelper.getReadableDatabase();
	  }	  
	}
  
  /** Insert a new task */
  public long insertTask(ToDoItem _task) {
    // Create a new row of values to insert.
    ContentValues newTaskValues = new ContentValues();
  
    // Assign values for each row.
    newTaskValues.put(KEY_TASK, _task.getTask());
    newTaskValues.put(KEY_CREATION_DATE, _task.getCreated().getTime());
    
    // Insert the row.
    return db.insert(DATABASE_TABLE, null, newTaskValues);
  }

  /** Remove a task based on its index */
  public boolean removeTask(long _rowIndex) {
    return db.delete(DATABASE_TABLE, KEY_ID + "=" + _rowIndex, null) > 0;
  }

  /** Update a task */
  public boolean updateTask(long _rowIndex, String _task) {
    ContentValues newValue = new ContentValues();
    newValue.put(KEY_TASK, _task);
    return db.update(DATABASE_TABLE, newValue, KEY_ID + "=" + _rowIndex, null) > 0;
  }
  
  /** Return a Cursor to all the Todo items */
  public Cursor getAllToDoItemsCursor() {
	  return db.query(DATABASE_TABLE, new String[] { KEY_ID, KEY_TASK, KEY_CREATION_DATE}, null, null, null, null, null);
	}

  /** Return a Cursor to a specific row */
	public Cursor setCursorToToDoItem(long _rowIndex) throws SQLException {
	  Cursor result = db.query(true, DATABASE_TABLE, new String[] {KEY_ID, KEY_TASK},
	                           KEY_ID + "=" + _rowIndex, null, null, null, null, null);
	  
	  if ((result.getCount() == 0) || !result.moveToFirst()) {
	    throw new SQLException("No Todo items found for row: " + _rowIndex);
	  }
	  return result;
	}

	/** Return a Todo Item based on its row index */
	public ToDoItem getToDoItem(long _rowIndex) throws SQLException {
	  Cursor cursor = db.query(true, DATABASE_TABLE, new String[] {KEY_ID, KEY_TASK},
	                           KEY_ID + "=" + _rowIndex, null, null, null, null, null);
	  if ((cursor.getCount() == 0) || !cursor.moveToFirst()) {
	    throw new SQLException("No to do item found for row: " + _rowIndex);
	  }

	  String task = cursor.getString(TASK_COLUMN);
	  long created = cursor.getLong(CREATION_DATE_COLUMN);
		  
	  ToDoItem result = new ToDoItem(task, new Date(created));
	  return result;  
	}

	/** Static Helper class for creating, upgrading, and opening
	 * the database.
	 */
  private static class toDoDBOpenHelper extends SQLiteOpenHelper {

	  public toDoDBOpenHelper(Context context, String name, CursorFactory factory, int version) {
	    super(context, name, factory, version);
	  }

	  /** SQL Statement to create a new database */
	  private static final String DATABASE_CREATE = "create table " + 
	    DATABASE_TABLE + " (" + KEY_ID + " integer primary key autoincrement, " +
	    KEY_TASK + " text not null, " + KEY_CREATION_DATE + " integer);";

	  @Override
	  public void onCreate(SQLiteDatabase _db) {
	    _db.execSQL(DATABASE_CREATE);
	  }

	  @Override
	  public void onUpgrade(SQLiteDatabase _db, int _oldVersion, int _newVersion) {
	    Log.w("TaskDBAdapter", "Upgrading from version " + 
	                           _oldVersion + " to " +
	                           _newVersion + ", which will destroy all old data");

	    // Drop the old table.
	    _db.execSQL("DROP TABLE IF EXISTS " + DATABASE_TABLE);
	    // Create a new one.
	    onCreate(_db);
	  }
	}
}