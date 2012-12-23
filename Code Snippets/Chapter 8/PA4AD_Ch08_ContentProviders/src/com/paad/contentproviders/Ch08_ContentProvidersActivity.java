package com.paad.contentproviders;

import java.util.Calendar;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class Ch08_ContentProvidersActivity extends Activity {
  
  TextView resultTextView;
  
  /** Called when the activity is first created. */
  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.main);
    
    resultTextView = (TextView)findViewById(R.id.resultTextView);
    
    Button mediaStoreButton = (Button)findViewById(R.id.button1);
    mediaStoreButton.setOnClickListener(new OnClickListener() {
      public void onClick(View v) {
        AsyncTask<Void, Void, StringBuilder> task = new AsyncTask<Void, Void, StringBuilder>() {
          @Override
          protected StringBuilder doInBackground(Void... params) {
            String[] result = getSongListing();
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < result.length; i++)
              sb.append(result[i]);
            return sb;
          }
          
          @Override
          protected void onPostExecute(StringBuilder sb) {
            resultTextView.setText(sb.toString());
          }
        };
        task.execute();
      }
    });
    
    Button contactsButton = (Button)findViewById(R.id.button2);
    contactsButton.setOnClickListener(new OnClickListener() {
      public void onClick(View v) {
        AsyncTask<Void, Void, StringBuilder> task = new AsyncTask<Void, Void, StringBuilder>() {
          @Override
          protected StringBuilder doInBackground(Void... params) {
            String[] result = getNames();
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < result.length; i++)
              sb.append(result[i]);
            return sb;
          }
          
          @Override
          protected void onPostExecute(StringBuilder sb) {
            resultTextView.setText(sb.toString());
          }
        };
        task.execute();
      }
    });
    
    Button contactButton2 = (Button)findViewById(R.id.button3);
    contactButton2.setOnClickListener(new OnClickListener() {
      public void onClick(View v) {
        AsyncTask<Void, Void, StringBuilder> task = new AsyncTask<Void, Void, StringBuilder>() {
          @Override
          protected StringBuilder doInBackground(Void... params) {
            String[] result = getNameAndNumber();
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < result.length; i++)
              sb.append(result[i]);
            return sb;
          }
          
          @Override
          protected void onPostExecute(StringBuilder sb) {
            resultTextView.setText(sb.toString());
          }
        };
        task.execute();
      }
    });
    
    Button reverseLookupButton = (Button)findViewById(R.id.button4);
    reverseLookupButton.setOnClickListener(new OnClickListener() {
      public void onClick(View v) {
        AsyncTask<Void, Void, String> task = new AsyncTask<Void, Void, String>() {
          @Override
          protected String doInBackground(Void... params) {
            return performCallerId();
          }
          
          @Override
          protected void onPostExecute(String result) {
            resultTextView.setText(result);
          }
        };
        task.execute();
      }
    });
    
    Button pickContactButton = (Button)findViewById(R.id.button5);
    pickContactButton.setOnClickListener(new OnClickListener() {
      public void onClick(View v) {
        pickContact();
      }
    });
    
    Button insertContactButton = (Button)findViewById(R.id.button6);
    insertContactButton.setOnClickListener(new OnClickListener() {
      public void onClick(View v) {
        insertContactWithIntent();
      }
    });

    Button queryEventsButton = (Button)findViewById(R.id.button7);
    queryEventsButton.setOnClickListener(new OnClickListener() {
      public void onClick(View v) {
        AsyncTask<Void, Void, StringBuilder> task = new AsyncTask<Void, Void, StringBuilder>() {
          @Override
          protected StringBuilder doInBackground(Void... params) {
            String[] result = queryCalendar();
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < result.length; i++)
              sb.append(result[i]);
            return sb;
          }
          
          @Override
          protected void onPostExecute(StringBuilder sb) {
            resultTextView.setText(sb.toString());
          }
        };
        task.execute();
      }
    });
    
    Button addEventButton = (Button)findViewById(R.id.button8);
    addEventButton.setOnClickListener(new OnClickListener() {
      public void onClick(View v) {
        insertNewEventIntoCalendar();
      }
    });
    
    Button editEventButton = (Button)findViewById(R.id.button9);
    editEventButton.setOnClickListener(new OnClickListener() {
      public void onClick(View v) {
        editCalendarEvent();
      }
    });
    
    Button viewTimeButton = (Button)findViewById(R.id.button10);
    viewTimeButton.setOnClickListener(new OnClickListener() {
      public void onClick(View v) {
        displayCalendarEvent();
      }
    });
  }
    
  private String[] getSongListing() {
    /**
     * Listing 8-35: Accessing the Media Store Content Provider
     */
    // Get a Cursor over every piece of audio on the external volume, 
    // extracting the song title and album name.
    String[] projection = new String[] {
      MediaStore.Audio.AudioColumns.ALBUM,
      MediaStore.Audio.AudioColumns.TITLE
    };
  
    Uri contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
  
    Cursor cursor = 
      getContentResolver().query(contentUri, projection, 
                                 null, null, null); 
  
    // Get the index of the columns we need.
    int albumIdx =
      cursor.getColumnIndexOrThrow(MediaStore.Audio.AudioColumns.ALBUM);
    int titleIdx = 
      cursor.getColumnIndexOrThrow(MediaStore.Audio.AudioColumns.TITLE);
  
    // Create an array to store the result set.
    String[] result = new String[cursor.getCount()];
  
    // Iterate over the Cursor, extracting each album name and song title.
    while (cursor.moveToNext()) {
      // Extract the song title.
      String title = cursor.getString(titleIdx);
      // Extract the album name.
      String album = cursor.getString(albumIdx);
  
      result[cursor.getPosition()] = title + " (" + album + ")";
    } 
  
    // Close the Cursor.
    cursor.close();

    //
    return result;
  }

  private String[] getNames() {
    /**
     * Listing 8-36: Accessing the Contacts Contract Contact Content Provider
     */
    // Create a projection that limits the result Cursor
    // to the required columns.
    String[] projection = {
      ContactsContract.Contacts._ID,
      ContactsContract.Contacts.DISPLAY_NAME
    };
  
   // Get a Cursor over the Contacts Provider.
    Cursor cursor = 
      getContentResolver().query(ContactsContract.Contacts.CONTENT_URI,
                                 projection, null, null, null);
       
    // Get the index of the columns.
    int nameIdx = 
      cursor.getColumnIndexOrThrow(ContactsContract.Contacts.DISPLAY_NAME);
    int idIdx = 
      cursor.getColumnIndexOrThrow(ContactsContract.Contacts._ID);
  
    // Initialize the result set.
    String[] result = new String[cursor.getCount()];
  
    // Iterate over the result Cursor.
    while(cursor.moveToNext()) {
      // Extract the name.
      String name = cursor.getString(nameIdx);
      // Extract the unique ID.
      String id = cursor.getString(idIdx);
   
      result[cursor.getPosition()] = name + " (" + id + ")";
    } 
  
    // Close the Cursor.
    cursor.close();

    //
    return result;
  }
  
  private String[] getNameAndNumber() {
    /**
     * Listing 8-37: Finding contact details for a contact name
     */
    ContentResolver cr = getContentResolver();
    String[] result = null;

    // Find a contact using a partial name match
    String searchName = "andy";
    Uri lookupUri = 
      Uri.withAppendedPath(ContactsContract.Contacts.CONTENT_FILTER_URI,
                           searchName);

    // Create a projection of the required column names.
    String[] projection = new String[] {
      ContactsContract.Contacts._ID
    };

    // Get a Cursor that will return the ID(s) of the matched name.
    Cursor idCursor = cr.query(lookupUri, 
      projection, null, null, null);

    // Extract the first matching ID if it exists.
    String id = null;
    if (idCursor.moveToFirst()) {
      int idIdx = 
        idCursor.getColumnIndexOrThrow(ContactsContract.Contacts._ID);
      id = idCursor.getString(idIdx);
    }

    // Close that Cursor.
    idCursor.close();

    // Create a new Cursor searching for the data associated with the returned Contact ID.
    if (id != null) {
      // Return all the PHONE data for the contact.
      String where = ContactsContract.Data.CONTACT_ID + 
        " = " + id + " AND " +
        ContactsContract.Data.MIMETYPE + " = '" +
        ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE +
        "'";

      projection = new String[] {
        ContactsContract.Data.DISPLAY_NAME,
        ContactsContract.CommonDataKinds.Phone.NUMBER
      };
      
      Cursor dataCursor = 
        getContentResolver().query(ContactsContract.Data.CONTENT_URI,
          projection, where, null, null);

      // Get the indexes of the required columns.
      int nameIdx = 
        dataCursor.getColumnIndexOrThrow(ContactsContract.Data.DISPLAY_NAME);
      int phoneIdx = 
        dataCursor.getColumnIndexOrThrow(
          ContactsContract.CommonDataKinds.Phone.NUMBER);

      result = new String[dataCursor.getCount()];
      
      while(dataCursor.moveToNext()) {
        // Extract the name.
        String name = dataCursor.getString(nameIdx);
        // Extract the phone number.
        String number = dataCursor.getString(phoneIdx);

        result[dataCursor.getPosition()] = name + " (" + number + ")";
      }
      
      dataCursor.close();
    }
    
    return result;
  }
  
  private String performCallerId() {
    /**
     * Listing 8-38: Performing a caller-ID lookup
     */
    String incomingNumber = "(650)253-0000";
    String result = "Not Found";

    Uri lookupUri =
      Uri.withAppendedPath(ContactsContract.PhoneLookup.CONTENT_FILTER_URI,
                           incomingNumber);

    String[] projection = new String[] {
      ContactsContract.Contacts.DISPLAY_NAME
    };

    Cursor cursor = getContentResolver().query(lookupUri,
      projection, null, null, null);

    if (cursor.moveToFirst()) {
      int nameIdx = 
        cursor.getColumnIndexOrThrow(ContactsContract.Contacts.DISPLAY_NAME);
      
      result = cursor.getString(nameIdx);
    }

    cursor.close();

    
    return result;
  }
  
  /**
   * Listing 8-39: Picking a contact
   */
  private static int PICK_CONTACT = 0;

  private void pickContact() {
    Intent intent = new Intent(Intent.ACTION_PICK,
                               ContactsContract.Contacts.CONTENT_URI);  
    startActivityForResult(intent, PICK_CONTACT);  
  }

  @Override
  protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    if ((requestCode == PICK_CONTACT) && (resultCode == RESULT_OK)) {
      resultTextView.setText(data.getData().toString());
    }
  }
  
  private void insertContactWithIntent() {
    /**
     * Listing 8-40: Inserting a new contact using an Intent
     */
    Intent intent = 
      new Intent(ContactsContract.Intents.SHOW_OR_CREATE_CONTACT,
                 ContactsContract.Contacts.CONTENT_URI);
    intent.setData(Uri.parse("tel:(650)253-0000"));

    intent.putExtra(ContactsContract.Intents.Insert.COMPANY, "Google");
    intent.putExtra(ContactsContract.Intents.Insert.POSTAL, 
      "1600 Amphitheatre Parkway, Mountain View, California");

    startActivity(intent);

  }

  private String[] queryCalendar() {
    
    /**
     * Listing 8-41: Querying the Events table
     */
    //Create a projection that limits the result Cursor
    //to the required columns.
    String[] projection = {
       CalendarContract.Events._ID,
       CalendarContract.Events.TITLE
    };
    
    //Get a Cursor over the Events Provider.
    Cursor cursor = 
     getContentResolver().query(CalendarContract.Events.CONTENT_URI,
                                projection, null, null, null);
       
    //Get the index of the columns.
    int nameIdx = 
    cursor.getColumnIndexOrThrow(CalendarContract.Events.TITLE);
    int idIdx = cursor. getColumnIndexOrThrow(CalendarContract.Events._ID);
    
    //Initialize the result set.
    String[] result = new String[cursor.getCount()];
    
    //Iterate over the result Cursor.
    while(cursor.moveToNext()) {
      // Extract the name.
      String name = cursor.getString(nameIdx);
      // Extract the unique ID.
      String id = cursor.getString(idIdx);
    
      result[cursor.getPosition()] = name + " (" + id + ")";
    } 
    
    //Close the Cursor.
    cursor.close();
    
    //
    return result;
  }
    
  private void insertNewEventIntoCalendar() {
    /**
     * Listing 8-42: Inserting a new calendar event using an Intent
     */
    // Create a new insertion Intent.
    Intent intent = new Intent(Intent.ACTION_INSERT, CalendarContract.Events.CONTENT_URI);
  
    // Add the calendar event details
    intent.putExtra(CalendarContract.Events.TITLE, "Launch!");
    intent.putExtra(CalendarContract.Events.DESCRIPTION, 
                    "Professional Android 4 " +
                    "Application Development release!");
    intent.putExtra(CalendarContract.Events.EVENT_LOCATION, "Wrox.com");
  
    Calendar startTime = Calendar.getInstance();
    startTime.set(2012, 2, 13, 0, 30);
    intent.putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, startTime.getTimeInMillis());
   
    intent.putExtra(CalendarContract.EXTRA_EVENT_ALL_DAY, true);    
  
     // Use the Calendar app to add the new event.
    startActivity(intent);
  }

  private void editCalendarEvent() {
    /**
     * Listing 8-43: Editing a calendar event using an Intent
     */
    // Create a URI addressing a specific event by its row ID.
    // Use it to  create a new edit Intent.
    long rowID = 760;
    Uri uri = ContentUris.withAppendedId(
      CalendarContract.Events.CONTENT_URI, rowID);

    Intent intent = new Intent(Intent.ACTION_EDIT, uri);

    // Modify the calendar event details
    Calendar startTime = Calendar.getInstance();
    startTime.set(2012, 2, 13, 0, 30);
    intent.putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, startTime.getTimeInMillis());

    intent.putExtra(CalendarContract.EXTRA_EVENT_ALL_DAY, true);    

    // Use the Calendar app to edit the event.
    startActivity(intent);  
  }
  
  private void displayCalendarEvent() {
    /**
     * Listing 8-44: Displaying a calendar event using an Intent
     */
    // Create a URI that specifies a particular time to view.
    Calendar startTime = Calendar.getInstance();
    startTime.set(2012, 2, 13, 0, 30);

    Uri uri = Uri.parse("content://com.android.calendar/time/" +
      String.valueOf(startTime.getTimeInMillis()));
    Intent intent = new Intent(Intent.ACTION_VIEW, uri);

    // Use the Calendar app to view the time.
    startActivity(intent);
  }
}