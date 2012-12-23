package com.paad.todolist;

import java.util.ArrayList;

import android.app.Activity;
import android.app.FragmentManager;
import android.os.Bundle;

public class ToDoListActivity extends Activity implements NewItemFragment.OnNewItemAddedListener {
  
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
  }
  
  public void onNewItemAdded(String newItem) {
    ToDoItem newTodoItem = new ToDoItem(newItem);
    todoItems.add(0, newTodoItem);
    aa.notifyDataSetChanged();
  }

}