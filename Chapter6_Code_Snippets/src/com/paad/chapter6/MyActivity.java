package com.paad.chapter6;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Contacts.People;
import android.widget.TextView;

public class MyActivity extends Activity {
	
  Bitmap sourceBitmap;
  String newValue = "1";
  String rowId = "1";
  String COLUMN_NAME = "C1";
  
  String KEY_ID = "Index";
  String KEY_COL1 = "C1";
  String KEY_COL3 = "C3";
  String KEY_COL5 = "C5";
  String requiredValue = "1";
  
  private static final String FILE_NAME = "tempfile.tmp";
  
  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.main);
        
    /** ---- Update TextView based on saved state ---- */
    TextView myTextView = (TextView)findViewById(R.id.myTextView);
    String text = "";
    
    if (savedInstanceState != null && savedInstanceState.containsKey(TEXTVIEW_STATE_KEY))
      text = savedInstanceState.getString(TEXTVIEW_STATE_KEY);
    
    myTextView.setText(text);
        
    /** ---- Trigger Preferences code snippets ---- */
    savePreferences();
    loadPreferences();
    saveActivityPreferences();
        
    /** ---- File handling code snippets ---- */
    try {      
      // Create a new output file stream that’s private to this application.
      FileOutputStream fos = openFileOutput(FILE_NAME, Context.MODE_PRIVATE);
	    // Create a new file input stream.
	    FileInputStream fis = openFileInput(FILE_NAME);
	    
	    // World Writable
	    String OUTPUT_FILE = "publicCopy.txt";
			fos = openFileOutput(OUTPUT_FILE, Context.MODE_WORLD_WRITEABLE);
		} catch (FileNotFoundException e) {	}

		String[] files = fileList();
    deleteFile(FILE_NAME);
    files = fileList();
		
		/** ---- External Raw Resource ---- */
		Resources myResources = getResources();
		InputStream myFile = myResources.openRawResource(R.raw.myfilename);
  }
      
  /** ---- Code snippets demonstrating the Preferences mechanism ---- */

  public static final String MY_PREFS = "mySharedPreferences";
    
  protected void savePreferences(){
    // Create or retrieve the shared preference object.
	  int mode = Activity.MODE_PRIVATE;
	  SharedPreferences mySharedPreferences = getSharedPreferences(MY_PREFS, mode);

    // Retrieve an editor to modify the shared preferences.
    SharedPreferences.Editor editor = mySharedPreferences.edit();

    // Store new primitive types in the shared preferences object.
    editor.putBoolean("isTrue", true);
    editor.putFloat("lastFloat", 1f);
    editor.putInt("wholeNumber", 2);
    editor.putLong("aNumber", 3l);
    editor.putString("textEntryValue", "Not Empty");
      
    // Commit the changes.
    editor.commit();
  }

  public void loadPreferences() {
    // Restore preferences
 	  int mode = Activity.MODE_PRIVATE;
 	  SharedPreferences mySharedPreferences = getSharedPreferences(MY_PREFS, mode);

    boolean isTrue = mySharedPreferences.getBoolean("isTrue", false);
    float lastFloat = mySharedPreferences.getFloat("lastFloat", 0f);
    int wholeNumber = mySharedPreferences.getInt("wholeNumber", 1);
    long aNumber = mySharedPreferences.getLong("aNumber", 0);
    String stringPreference = mySharedPreferences.getString("textEntryValue", "");
  }

  protected void saveActivityPreferences(){
	  // Create or retrieve the activity preference object.
	  SharedPreferences activityPreferences = getPreferences(Activity.MODE_PRIVATE);

	  // Retrieve an editor to modify the shared preferences.
	  SharedPreferences.Editor editor = activityPreferences.edit();
  	  
	  // Retrieve the View
	  TextView myTextView = (TextView)findViewById(R.id.myTextView);
  	  
	  // Store new primitive types in the shared preferences object.
	  editor.putString("currentTextValue", myTextView.getText().toString());

	  // Commit changes.
  	editor.commit();
 	}

  /** ---- Save / Restore Instance State Handlers ---- */
  
  private static final String TEXTVIEW_STATE_KEY = "TEXTVIEW_STATE_KEY";
  
  @Override
  public void onSaveInstanceState(Bundle savedInstanceState) {
    // Retrieve the View
    TextView myTextView = (TextView)findViewById(R.id.myTextView);

    // Save its state
    savedInstanceState.putString(TEXTVIEW_STATE_KEY, myTextView.getText().toString());
    super.onSaveInstanceState(savedInstanceState);
  }
  
  @Override
  public void onRestoreInstanceState(Bundle savedInstanceState) {
    super.onRestoreInstanceState(savedInstanceState);
    // Todo Restore instance state values
  }
    
  /** ---- Database Code Snippets ---- **/
  
  private static final String DATABASE_NAME = "myDatabase.db";
  private static final String DATABASE_TABLE = "mainTable";
  
  SQLiteDatabase myDatabase;
  
  private static final String DATABASE_CREATE = 
    "create table " + DATABASE_TABLE + " ( _id integer primary key autoincrement," +
    "myColumn text not null);";

  private void createDatabase() {
    myDatabase = openOrCreateDatabase(DATABASE_NAME, Context.MODE_PRIVATE, null);
    myDatabase.execSQL(DATABASE_CREATE);
  }
    
  private void databaseSnippets() {
    /** ---- Database and Content Provider Code Snippets ---- */
    
    // Return all rows for columns one and three, no duplicates
    String[] result_columns = new String[] {KEY_ID, KEY_COL1, KEY_COL3};

    Cursor allRows = myDatabase.query(true, DATABASE_TABLE, result_columns, 
                                      null, null, null, null, null, null);
             
    // Return all columns for rows where column 3 equals a set value
    // and the rows are ordered by column 5.
    String where = KEY_COL3 + "=" + requiredValue;
    String order = KEY_COL5;
    Cursor myResult = myDatabase.query(DATABASE_TABLE, null, where, 
                                       null, null, null, order);
    
    int columnIndex = 1;
    String columnValue = myResult.getString(columnIndex);
    
    //----
    
    int GOLD_HOARDED_COLUMN = 2;
    Cursor myGold = myDatabase.query("GoldHoards", null, null, null, null, null, null);
    float totalHoard = 0f;

    // Make sure there is at least one row.
    if (myGold.moveToFirst()) {
      // Iterate over each cursor.
      do { 
        float hoard = myGold.getFloat(GOLD_HOARDED_COLUMN);
        totalHoard += hoard;
      } while(myGold.moveToNext());
    }

    Float averageHoard = totalHoard / myGold.getCount();

    // --------------------------------------------------
    // Insert
    int newValue = 1;
    
    // Create a new row of values to insert.
    ContentValues newValues = new ContentValues();

    // Assign values for each row.
    newValues.put(COLUMN_NAME, newValue);
    //[ ... Repeat for each column ... ]

    // Insert the row into your table
    myDatabase.insert(DATABASE_TABLE, null, newValues);
    
    // --------------------------------------------------
    // Update
    
    // Define the updated row content.
    ContentValues updatedValues = new ContentValues();
    // Assign values for each row.
    newValues.put(COLUMN_NAME, newValue);
    //[ ... Repeat for each column ... ]

    where = KEY_ID + "=" + rowId;
    
    // Update the row with the specified index with the new values.
    myDatabase.update(DATABASE_TABLE, newValues, where, null);

    // --------------------------------------------------
    // Delete
    
    // Delete the specified row.
    myDatabase.delete(DATABASE_TABLE, KEY_ID + "=" + rowId, null); 
  }

  /** ---- Content Provider Snippets ---- **/

  private void contentProviderSnippets() {
    ContentResolver cr = getContentResolver();
  
    // Return all rows
    Cursor allRows = getContentResolver().query(MyProvider.CONTENT_URI, null, null, null, null);
  
    // Return all columns for rows where column 3 equals a set value
    // and the rows are ordered by column 5.
    String where = KEY_COL3 + "=" + requiredValue;
    String order = KEY_COL5;
    Cursor someRows = getContentResolver().query(MyProvider.CONTENT_URI, null, where, null, order);
    
    //--------------------------------------------------
    // Insert
    
    // Create a new row of values to insert.
    ContentValues newValues = new ContentValues();
  
    // Assign values for each row.
    newValues.put(COLUMN_NAME, newValue);
    //[ ... Repeat for each column ... ]
  
    Uri myRowUri = getContentResolver().insert(MyProvider.CONTENT_URI, newValues);
  
    // Create a new row of values to insert.
    ContentValues[] valueArray = new ContentValues[5];
  
    // TODO: Create an array of new rows
  
    int count = getContentResolver().bulkInsert(MyProvider.CONTENT_URI, valueArray);
  
    //--------------------------------------------------
    // Delete
    
    // Remove a specific row.
    getContentResolver().delete(myRowUri, null, null);
  
    // Remove the first five rows.
    where = "_id < 5";
    getContentResolver().delete(MyProvider.CONTENT_URI, where, null);
  
    //--------------------------------------------------
    // Updates
    
    // Create a new row of values to insert.
    newValues = new ContentValues();
  
    // Create a replacement map, specifying which columns you want to
    // update, and what values to assign to each of them.
    newValues.put(COLUMN_NAME, newValue);
  
    // Apply to the first 5 rows.
    where = "_id < 5";
  
    getContentResolver().update(MyProvider.CONTENT_URI, newValues, where, null);

    //--------------------------------------------------
    // Accessing Files in Content Providers   

    // Insert a new row into your provider, returning its unique URI.
    Uri uri = getContentResolver().insert(MyProvider.CONTENT_URI, newValues);
  
    try {
      // Open an output stream using the new row's URI.
      OutputStream outStream = getContentResolver().openOutputStream(uri);
      // Compress your bitmap and save it into your provider.
      sourceBitmap.compress(Bitmap.CompressFormat.JPEG, 50, outStream);
    }
    catch (FileNotFoundException e) { }
  
    //--------------------------------------------------
    // Using the Media Store
    android.provider.MediaStore.Images.Media.insertImage(getContentResolver(), sourceBitmap, "my_cat_pic", "Photo of my cat!");
    
    //--------------------------------------------------
    // Using the Contacts Database
    
    // Get a cursor over every contact.
    Cursor cursor = getContentResolver().query(People.CONTENT_URI, null, null, null, null); 

    // Let the activity manage the cursor lifecycle.
    startManagingCursor(cursor);

    // Use the convenience properties to get the index of the columns
    int nameIdx = cursor.getColumnIndexOrThrow(People.NAME); 
    int phoneIdx = cursor. getColumnIndexOrThrow(People.NUMBER);

    String[] result = new String[cursor.getCount()];

    if (cursor.moveToFirst())
      do { 
        // Extract the name.
        String name = cursor.getString(nameIdx);
        // Extract the phone number.
        String phone = cursor.getString(phoneIdx);

        result[cursor.getPosition()] = name + " (" + phone + ")";

      } while(cursor.moveToNext());
  }
  
}