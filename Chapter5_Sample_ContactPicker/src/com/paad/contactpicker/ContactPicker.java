package com.paad.contactpicker;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Contacts.People;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.AdapterView.OnItemClickListener;

public class ContactPicker extends Activity {
 
	@Override
	public void onCreate(Bundle icicle) {
	  super.onCreate(icicle);
	  setContentView(R.layout.main);

	  // Get data and create an adapter for it
	  String dataPath = getIntent().getData().toString();
	  
	  final Uri data = Uri.parse(dataPath + "people/");
	  final Cursor c = managedQuery(data, null, null, null, null);
	      
	  String[] from = new String[] {People.NAME};
	  int[]  to = new int[] { R.id.itemTextView };
	      
	  SimpleCursorAdapter adapter = new SimpleCursorAdapter(getApplicationContext(),
	                                                        R.layout.listitemlayout,
	                                                        c, from, to);
	  
	  // Assign the adapter to the List View
	  ListView lv = (ListView)findViewById(R.id.contactListView);
	  lv.setAdapter(adapter);

	  // Return the selected contact when it is chosen from the list.
	  lv.setOnItemClickListener(new OnItemClickListener() {

		    public void onItemClick(AdapterView<?> parent, View view, int pos, long id) {
		      // Move the cursor to the selected item
		      c.moveToPosition(pos);
		      // Extract the row id.
		      int rowId = c.getInt(c.getColumnIndexOrThrow("_id"));
		      // Construct the result URI.
		      Uri outURI = Uri.parse(data.toString() + rowId);
		      Intent outData = new Intent();
		      outData.setData(outURI);
		      setResult(Activity.RESULT_OK, outData);
		      finish();
		    }
		    
		  });	 
	}

}