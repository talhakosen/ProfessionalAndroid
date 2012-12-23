package com.paad.todolist;

import java.util.ArrayList;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.LoaderManager;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;

public class ToDoListActivity extends Activity implements 
  NewItemFragment.OnNewItemAddedListener, 
  LoaderManager.LoaderCallbacks<Cursor> { 
  
  private ArrayList<ToDoItem> todoItems;
  private ToDoItemAdapter aa;

  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    // Inflate your view
    setContentView(R.layout.main);
      
    // Get references to the Fragments
    FragmentManager fm = getFragmentManager();
    ToDoListFragment todoListFragment = 
      (ToDoListFragment)fm.findFragmentById(R.id.TodoListFragment);
     
    // Create the array list of to do items
    todoItems = new ArrayList<ToDoItem>();
     
    // Create the array adapter to bind the array to the ListView
    int resID = R.layout.todolist_item;
    aa = new ToDoItemAdapter(this, resID, todoItems);
     
    // Bind the array adapter to the ListView.
    todoListFragment.setListAdapter(aa);
    
    getLoaderManager().initLoader(0, null, this);
  }
  
  @Override
  protected void onResume() {
    super.onResume();
    getLoaderManager().restartLoader(0, null, this);
  }
  
  public void onNewItemAdded(String newItem) {
    ContentResolver cr = getContentResolver();
    
    ContentValues values = new ContentValues();
    values.put(ToDoContentProvider.KEY_TASK, newItem);
    
    cr.insert(ToDoContentProvider.CONTENT_URI, values);
    getLoaderManager().restartLoader(0, null, this);
  }
  
  public Loader<Cursor> onCreateLoader(int id, Bundle args) {
    CursorLoader loader = new CursorLoader(this, 
      ToDoContentProvider.CONTENT_URI, null, null, null, null);
    
    return loader;
  }

  public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
    int keyTaskIndex = cursor.getColumnIndexOrThrow(ToDoContentProvider.KEY_TASK);
    
    todoItems.clear();
    while (cursor.moveToNext()) {
      ToDoItem newItem = new ToDoItem(cursor.getString(keyTaskIndex));
      todoItems.add(newItem);
    }
    aa.notifyDataSetChanged();
  }

  public void onLoaderReset(Loader<Cursor> loader) {
  }

}