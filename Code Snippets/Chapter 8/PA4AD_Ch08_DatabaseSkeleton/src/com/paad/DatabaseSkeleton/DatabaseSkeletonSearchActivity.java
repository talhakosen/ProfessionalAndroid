package com.paad.DatabaseSkeleton;

/**
 * Listing 8-27: Performing a search and displaying the results 
 */
import android.app.ListActivity;
import android.app.LoaderManager;
import android.app.SearchManager;
import android.app.SearchableInfo;
import android.content.ContentUris;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.SimpleCursorAdapter;

public class DatabaseSkeletonSearchActivity extends ListActivity 
  implements LoaderManager.LoaderCallbacks<Cursor> {
  
  private static String QUERY_EXTRA_KEY = "QUERY_EXTRA_KEY";
  
  private SimpleCursorAdapter adapter;
  
  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
  
    // Create a new adapter and bind it to the List View
    adapter = new SimpleCursorAdapter(this,
            android.R.layout.simple_list_item_1, null,
            new String[] { MyContentProvider.KEY_COLUMN_1_NAME },
            new int[] { android.R.id.text1 }, 0);
    setListAdapter(adapter);

    // Initiate the Cursor Loader
    getLoaderManager().initLoader(0, null, this);
    
    // Get the launch Intent
    parseIntent(getIntent());
    
    /**
     * Listing 8-29: Binding a Search View to your searchable Activity 
     */
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
  
  @Override
  protected void onNewIntent(Intent intent) {
    super.onNewIntent(intent);
    parseIntent(getIntent());
  }
  
  private void parseIntent(Intent intent) {
    // If the Activity was started to service a Search request,
    // extract the search query.
    if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
      String searchQuery = intent.getStringExtra(SearchManager.QUERY);
      // Perform the search
      performSearch(searchQuery);
    }
  }
  
  // Execute the search.
  private void performSearch(String query) {
    // Pass the search query as an argument to the Cursor Loader
    Bundle args = new Bundle();
    args.putString(QUERY_EXTRA_KEY, query);
    
    // Restart the Cursor Loader to execute the new query.
    getLoaderManager().restartLoader(0, args, this);
  }

  public Loader<Cursor> onCreateLoader(int id, Bundle args) {
    String query = "0";

    // Extract the search query from the arguments.
    if (args != null)
      query = args.getString(QUERY_EXTRA_KEY);

    // Construct the new query in the form of a Cursor Loader.
    String[] projection = { 
        MyContentProvider.KEY_ID, 
        MyContentProvider.KEY_COLUMN_1_NAME 
    };
    String where = MyContentProvider.KEY_COLUMN_1_NAME 
                   + " LIKE \"%" + query + "%\"";
    String[] whereArgs = null;
    String sortOrder = MyContentProvider.KEY_COLUMN_1_NAME + 
                       " COLLATE LOCALIZED ASC";
    
    // Create the new Cursor loader.
    return new CursorLoader(this, MyContentProvider.CONTENT_URI,
      projection, where, whereArgs, sortOrder);
  }

  public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
    // Replace the result Cursor displayed by the Cursor Adapter with
    // the new result set.
    adapter.swapCursor(cursor);
  }

  public void onLoaderReset(Loader<Cursor> loader) {
    // Remove the existing result Cursor from the List Adapter.
    adapter.swapCursor(null);
  }
  
  /**
   * Listing 8-28: Providing actions for search result selection 
   */
  @Override
  protected void onListItemClick(ListView listView, View view, int position, long id) {
    super.onListItemClick(listView, view, position, id);
    
    // Create a URI to the selected item.
    Uri selectedUri = 
      ContentUris.withAppendedId(MyContentProvider.CONTENT_URI, id);
    
    // Create an Intent to view the selected item.
    Intent intent = new Intent(Intent.ACTION_VIEW);
    intent.setData(selectedUri);
    
    // Start an Activity to view the selected item.
    startActivity(intent);
  }
  
//  /**
//   * Listing 8-25: Extracting the search query
//   */
//  @Override
//  public void onCreate(Bundle savedInstanceState) {
//    super.onCreate(savedInstanceState);
//
//    // Get the launch Intent
//    parseIntent(getIntent());
//  }
//
//  @Override
//  protected void onNewIntent(Intent intent) {
//    super.onNewIntent(intent);
//    parseIntent(getIntent());
//  }
//
//  private void parseIntent(Intent intent) {
//    // If the Activity was started to service a Search request,
//    // extract the search query.
//    if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
//      String searchQuery = intent.getStringExtra(SearchManager.QUERY);
//      // Perform the search
//      performSearch(searchQuery);
//    }    
//  }
}