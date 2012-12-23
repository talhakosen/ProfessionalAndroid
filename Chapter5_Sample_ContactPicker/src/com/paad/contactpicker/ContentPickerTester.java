package com.paad.contactpicker;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Contacts.People;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class ContentPickerTester extends Activity {

	public static final int PICK_CONTACT = 1;
	
	@Override
	public void onCreate(Bundle icicle) {
	  super.onCreate(icicle);
	  setContentView(R.layout.contentpickertester);
	
	  Button button = (Button)findViewById(R.id.pick_contact_button);
	  button.setOnClickListener(new OnClickListener() {
	    public void onClick(View _view) {
	      Intent intent = new Intent(Intent.ACTION_PICK, Uri.parse("content://contacts/"));
	      startActivityForResult(intent, PICK_CONTACT);  
	    }	   
	  });
	}
	
	@Override 
	public void onActivityResult(int reqCode, int resCode, Intent data) {
	  super.onActivityResult(reqCode, resCode, data);
	  
	  switch(reqCode) {
	    case (PICK_CONTACT) : {
	      if (resCode == Activity.RESULT_OK) {
	        Cursor c = managedQuery(data.getData(), null, null, null, null);
	        c.moveToFirst();
	        String name = c.getString(c.getColumnIndexOrThrow(People.NAME));
	        TextView tv = (TextView)findViewById(R.id.selected_contact_textview);
	        tv.setText(name);
	      }
	      break;
	    }
	  }
	  
	}

}