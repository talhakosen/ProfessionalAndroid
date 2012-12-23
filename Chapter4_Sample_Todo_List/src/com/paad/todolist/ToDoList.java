package com.paad.todolist;

import java.util.ArrayList;
import android.app.Activity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnKeyListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

public class ToDoList extends Activity {
  
  /** Called when the activity is first created. */
  @Override
  public void onCreate(Bundle icicle) {
    super.onCreate(icicle);
   	  
    // Inflate your view
 	  setContentView(R.layout.main);

   	// Get references to UI widgets
   	ListView myListView = (ListView)findViewById(R.id.myListView);
   	final EditText myEditText = (EditText)findViewById(R.id.myEditText);
   	
   	// Create the Todo List ArrayList and Adapter and assign is to the ListView
   	final ArrayList<String> todoItems = new ArrayList<String>();
   	int resID = R.layout.todolist_item;
   	final ArrayAdapter<String> aa = new ArrayAdapter<String>(this, resID, todoItems);
   	myListView.setAdapter(aa);

   	// Assign the KeyListener to the DPad button to add new items
   	myEditText.setOnKeyListener(new OnKeyListener() {
   	  public boolean onKey(View v, int keyCode, KeyEvent event) {
   	    if (event.getAction() == KeyEvent.ACTION_DOWN)
          if (keyCode == KeyEvent.KEYCODE_DPAD_CENTER)
   	      {    	 
            // Add a new todo item and clear the input box.
            todoItems.add(0, myEditText.getText().toString());
   	        myEditText.setText("");
   	        aa.notifyDataSetChanged();
	          return true;   	        
   	      }
   	    return false;
   	  }
    });
  }   
}