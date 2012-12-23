package com.paad.contactpicker;

import android.app.Activity;
import android.content.ContentUris;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.ContactsContract.Contacts;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

public class ContactPicker extends Activity {
  @Override
  public void onCreate(Bundle icicle) {
    super.onCreate(icicle);
    setContentView(R.layout.main);
    
    final Cursor c = getContentResolver().query(
        ContactsContract.Contacts.CONTENT_URI, null, null, null, null);

      String[] from = new String[] { Contacts.DISPLAY_NAME_PRIMARY };
      int[] to = new int[] { R.id.itemTextView };

      SimpleCursorAdapter adapter = new SimpleCursorAdapter(this,
                                                            R.layout.listitemlayout,
                                                            c,
                                                            from,
                                                            to);
      ListView lv = (ListView)findViewById(R.id.contactListView);
      lv.setAdapter(adapter);

      lv.setOnItemClickListener(new ListView.OnItemClickListener() {
        public void onItemClick(AdapterView<?> parent, View view, int pos,
                                long id) {
          // Move the cursor to the selected item
          c.moveToPosition(pos);
          // Extract the row id.
          int rowId = c.getInt(c.getColumnIndexOrThrow("_id"));
          // Construct the result URI.
          Uri outURI = ContentUris.withAppendedId(ContactsContract.Contacts.CONTENT_URI, rowId);
          Intent outData = new Intent();
          outData.setData(outURI);
          setResult(Activity.RESULT_OK, outData);
          finish();
        }
      });

  }
}
