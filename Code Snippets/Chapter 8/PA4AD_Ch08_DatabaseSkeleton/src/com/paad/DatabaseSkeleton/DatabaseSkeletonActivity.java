package com.paad.DatabaseSkeleton;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.OutputStream;

import android.app.Activity;
import android.app.LoaderManager;
import android.app.SearchManager;
import android.app.SearchableInfo;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.SearchView;
import android.widget.SimpleCursorAdapter;

public class DatabaseSkeletonActivity extends Activity {
  
  static final String TAG = "DATABASESKELETONACTIVITY";
  private SimpleCursorAdapter adapter;
  
  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.main);
    
    // Use the Search Manager to find the SearchableInfo related 
    // to this Activity.
    SearchManager searchManager =
      (SearchManager)getSystemService(Context.SEARCH_SERVICE);
    SearchableInfo searchableInfo = 
      searchManager.getSearchableInfo(getComponentName());
   
    // Bind the Activity's SearchableInfo to the Search View
    SearchView searchView = (SearchView)findViewById(R.id.searchView);
    searchView.setSearchableInfo(searchableInfo);
  }
  
  private String getLargestHoardName() {
    /**
     * Listing 8-15: Querying a Content Provider with a Content Resolver
     */
    // Get the Content Resolver.
    ContentResolver cr = getContentResolver();
  
    // Specify the result column projection. Return the minimum set
    // of columns required to satisfy your requirements.
    String[] result_columns = new String[] { 
      MyHoardContentProvider.KEY_ID, 
      MyHoardContentProvider.KEY_GOLD_HOARD_ACCESSIBLE_COLUMN,
      MyHoardContentProvider.KEY_GOLD_HOARDED_COLUMN }; 
  
    // Specify the where clause that will limit your results.
    String where = MyHoardContentProvider.KEY_GOLD_HOARD_ACCESSIBLE_COLUMN 
                   + "=" + 1;
  
    // Replace these with valid SQL statements as necessary.
    String whereArgs[] = null;
    String order = null;
  
    // Return the specified rows.
    Cursor resultCursor = cr.query(MyHoardContentProvider.CONTENT_URI, 
      result_columns, where, whereArgs, order);

    /**
     * Listing 8-17: Extracting values from a Content Provider result Cursor
     */
    float largestHoard = 0f;
    String hoardName = "No Hoards";

    // Find the index to the column(s) being used.
    int GOLD_HOARDED_COLUMN_INDEX = resultCursor.getColumnIndexOrThrow(
      MyHoardContentProvider.KEY_GOLD_HOARDED_COLUMN);
    int HOARD_NAME_COLUMN_INDEX = resultCursor.getColumnIndexOrThrow(
      MyHoardContentProvider.KEY_GOLD_HOARD_NAME_COLUMN);


    // Iterate over the cursors rows. 
    // The Cursor is initialized at before first, so we can 
    // check only if there is a "next" row available. If the
    // result Cursor is empty, this will return false.
    while (resultCursor.moveToNext()) {
      float hoard = resultCursor.getFloat(GOLD_HOARDED_COLUMN_INDEX);
      if (hoard > largestHoard) {
        largestHoard = hoard;
        hoardName = resultCursor.getString(HOARD_NAME_COLUMN_INDEX);
      }
    }

    // Close the Cursor when you've finished with it.
    resultCursor.close();
    
    //
    return hoardName;
  }
  
  private Cursor getRow(long rowId) {
    /**
     * Listing 8-16: Querying a Content Provider for a particular row
     */
    // Get the Content Resolver.
    ContentResolver cr = getContentResolver();
  
    // Specify the result column projection. Return the minimum set
    // of columns required to satisfy your requirements.
    String[] result_columns = new String[] { 
      MyHoardContentProvider.KEY_ID, 
      MyHoardContentProvider.KEY_GOLD_HOARD_NAME_COLUMN,
      MyHoardContentProvider.KEY_GOLD_HOARDED_COLUMN }; 
  
    // Append a row ID to the URI to address a specific row.
    Uri rowAddress =
      ContentUris.withAppendedId(MyHoardContentProvider.CONTENT_URI, 
      rowId); 
  
    // These are null as we are requesting a single row.
    String where = null;
    String whereArgs[] = null;
    String order = null;
  
    // Return the specified rows.
    Cursor resultCursor = cr.query(rowAddress, 
      result_columns, where, whereArgs, order);
    
    return resultCursor;
  }
  
  LoaderManager.LoaderCallbacks<Cursor> loaderCallback = new LoaderManager.LoaderCallbacks<Cursor>() {
    /**
     * Listing 8-18: Implementing Loader Callbacks
     */
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
      // Construct the new query in the form of a Cursor Loader. Use the id
      // parameter to contruct and return different loaders.
      String[] projection = null;
      String where = null;
      String[] whereArgs = null;
      String sortOrder = null;
        
      // Query URI
      Uri queryUri = MyContentProvider.CONTENT_URI;
        
      // Create the new Cursor loader.
      return new CursorLoader(DatabaseSkeletonActivity.this, queryUri,
        projection, where, whereArgs, sortOrder);
    }

    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
      // Replace the result Cursor displayed by the Cursor Adapter with  
      // the new result set.
      adapter.swapCursor(cursor);

      // This handler is not synchonrized with the UI thread, so you 
      // will need to synchronize it before modiyfing any UI elements
      // directly.
    }

    public void onLoaderReset(Loader<Cursor> loader) {
      // Remove the existing result Cursor from the List Adapter.
      adapter.swapCursor(null);

      // This handler is not synchonrized with the UI thread, so you 
      // will need to synchronize it before modiyfing any UI elements
      // directly.
    }
  };
  
  private Uri addNewHoard(String hoardName, float hoardValue, boolean hoardAccessible) {
    /**
     * Listing 8-19: Inserting new rows into a Content Provider
     */
    // Create a new row of values to insert.
    ContentValues newValues = new ContentValues();
  
    // Assign values for each row.
    newValues.put(MyHoardContentProvider.KEY_GOLD_HOARD_NAME_COLUMN,
                  hoardName);
    newValues.put(MyHoardContentProvider.KEY_GOLD_HOARDED_COLUMN,
                  hoardValue);
    newValues.put(MyHoardContentProvider.KEY_GOLD_HOARD_ACCESSIBLE_COLUMN,
                  hoardAccessible);
    // [ ... Repeat for each column / value pair ... ]
  
    // Get the Content Resolver
    ContentResolver cr = getContentResolver();
  
    // Insert the row into your table
    Uri myRowUri = cr.insert(MyHoardContentProvider.CONTENT_URI,
                             newValues);
    
    //
    return myRowUri;
  }
  
  private int deleteEmptyHoards() {
    /**
     * Listing 8-20: Deleting rows from a Content Provider
     */
    // Specify a where clause that determines which row(s) to delete.
    // Specify where arguments as necessary.
    String where = MyHoardContentProvider.KEY_GOLD_HOARDED_COLUMN + 
                   "=" + 0;
    String whereArgs[] = null;
  
    // Get the Content Resolver.
    ContentResolver cr = getContentResolver();
  
    // Delete the matching rows
    int deletedRowCount = 
      cr.delete(MyHoardContentProvider.CONTENT_URI, where, whereArgs);

    //
    return deletedRowCount;
  }
  
  private int updateHoardValue(int hoardId, float newHoardValue) {
   /**
    * Listing 8-21: Updating a record in a Content Provider
    */
    // Create the updated row content, assigning values for each row.
    ContentValues updatedValues = new ContentValues();
    updatedValues.put(MyHoardContentProvider.KEY_GOLD_HOARDED_COLUMN, 
                      newHoardValue);
    // [ ... Repeat for each column to update ... ]
  
    // Create a URI addressing a specific row.
    Uri rowURI = 
      ContentUris.withAppendedId(MyHoardContentProvider.CONTENT_URI,
      hoardId);
  
    // Specify a specific row so no selection clause is required.
    String where = null;
    String whereArgs[] = null;
  
    // Get the Content Resolver.
    ContentResolver cr = getContentResolver();
  
    // Update the specified row.
    int updatedRowCount = 
      cr.update(rowURI, updatedValues, where, whereArgs);
 
    return updatedRowCount;
  }

  /**
   * Listing 8-22: Reading and writing files from and to a Content Provider
   */
  private void addNewHoardWithImage(String hoardName, float hoardValue, 
    boolean hoardAccessible, Bitmap bitmap) { 
    
    // Create a new row of values to insert.
    ContentValues newValues = new ContentValues();
    
    // Assign values for each row.
    newValues.put(MyHoardContentProvider.KEY_GOLD_HOARD_NAME_COLUMN,
                  hoardName);
    newValues.put(MyHoardContentProvider.KEY_GOLD_HOARDED_COLUMN, 
                  hoardValue);
    newValues.put(
      MyHoardContentProvider.KEY_GOLD_HOARD_ACCESSIBLE_COLUMN,
      hoardAccessible);
    
    // Get the Content Resolver
    ContentResolver cr = getContentResolver();
    
    // Insert the row into your table
    Uri myRowUri = 
      cr.insert(MyHoardContentProvider.CONTENT_URI, newValues);
    
    try {
      // Open an output stream using the new row's URI.
      OutputStream outStream = cr.openOutputStream(myRowUri);
      // Compress your bitmap and save it into your provider.
      bitmap.compress(Bitmap.CompressFormat.JPEG, 80, outStream);
    }
    catch (FileNotFoundException e) { 
      Log.d(TAG, "No file found for this record.");
    }
  }
   
  private Bitmap getHoardImage(long rowId) {
    Uri myRowUri = 
      ContentUris.withAppendedId(MyHoardContentProvider.CONTENT_URI, 
                                 rowId);
      
    try {
      // Open an input stream using the new row's URI.
      InputStream inStream =
        getContentResolver().openInputStream(myRowUri);

      // Make a copy of the Bitmap.
      Bitmap bitmap = BitmapFactory.decodeStream(inStream);
      return bitmap;
    }
      catch (FileNotFoundException e) { 
      Log.d(TAG, "No file found for this record.");
    } 
    
    return null;
  }
}