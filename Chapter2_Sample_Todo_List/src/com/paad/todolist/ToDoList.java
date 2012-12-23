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

  @Override
  public void onCreate(Bundle icicle) {
    super.onCreate(icicle);

    // Inflate your view
 	  setContentView(R.layout.main);

 	  // Get references to UI widgets
 	  ListView myListView = (ListView)findViewById(R.id.myListView);
 	  final EditText myEditText = (EditText)findViewById(R.id.myEditText);  

 	  // Create the ArrayList and the ArrayAdapter
 	  final ArrayList<String> todoItems = new ArrayList<String>();
 	  final ArrayAdapter<String> aa = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,todoItems);

 	  // Bind the listview to the array adapter
 	  myListView.setAdapter(aa);

    // Add key listener to add the new todo item
 	  // when the middle D-pad button is pressed.
 	  myEditText.setOnKeyListener(new OnKeyListener() {
 	    public boolean onKey(View v, int keyCode, KeyEvent event) {
        if (event.getAction() == KeyEvent.ACTION_DOWN)
          if (keyCode == KeyEvent.KEYCODE_DPAD_CENTER) {
            // Add the new todo item, and clear the input text box
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