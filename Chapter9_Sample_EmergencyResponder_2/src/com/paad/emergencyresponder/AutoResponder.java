package com.paad.emergencyresponder;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.BroadcastReceiver;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;

public class AutoResponder extends Activity {

  public static final String alarmAction = "com.paad.emergencyresponder.AUTO_RESPONSE_EXPIRED";
  PendingIntent intentToFire;

	Spinner respondForSpinner;
	CheckBox locationCheckbox;
	EditText responseTextBox;

	@Override
	public void onCreate(Bundle icicle) {
	  super.onCreate(icicle);
	  setContentView(R.layout.autoresponder);

	  // Get references to the user controls
	  respondForSpinner = (Spinner)findViewById(R.id.spinnerRespondFor);
	  locationCheckbox = (CheckBox)findViewById(R.id.checkboxLocation);
	  responseTextBox = (EditText)findViewById(R.id.responseText);

	  // Populate the respond for spinner
	  ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.respondForDisplayItems, android.R.layout.simple_spinner_item);

	  adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
	  respondForSpinner.setAdapter(adapter);

	  // Load the saved preferences and update the UI
	  updateUIFromPreferences();

	  Button okButton = (Button) findViewById(R.id.okButton);
	  okButton.setOnClickListener(new View.OnClickListener() {
	    public void onClick(View view) {
	      savePreferences();
	      setResult(RESULT_OK, null);
	      finish();
	    }
	  });

	  Button cancelButton = (Button) findViewById(R.id.cancelButton);
	  cancelButton.setOnClickListener(new View.OnClickListener() {
	    public void onClick(View view) {
	      respondForSpinner.setSelection(-1);
	      savePreferences();
	      setResult(RESULT_CANCELED, null);
	      finish();
	    }
	  });
	}

	private void updateUIFromPreferences() {
	  // Get the saves settings
	  String preferenceName = getString(R.string.user_preferences);
	  SharedPreferences sp = getSharedPreferences(preferenceName, 0);

	  boolean autoRespond = sp.getBoolean(getString(R.string.autoRespondPref), false);
	  String respondText = sp.getString(getString(R.string.responseTextPref), "");
    boolean includeLoc = sp.getBoolean(getString(R.string.includeLocationPref), false);
    int respondForIndex = sp.getInt(getString(R.string.respondForPref), 0);

	  // Apply the saved settings to the UI
	  if (autoRespond)
	    respondForSpinner.setSelection(respondForIndex);
	  else
	    respondForSpinner.setSelection(0);
	  locationCheckbox.setChecked(includeLoc);
	  responseTextBox.setText(respondText);
  }

	private void savePreferences() {
	  // Get the current settings from the UI
	  boolean autoRespond = respondForSpinner.getSelectedItemPosition() > 0; 
	  int respondForIndex = respondForSpinner.getSelectedItemPosition();
	  boolean includeLoc = locationCheckbox.isChecked();
	  String respondText = responseTextBox.getText().toString();

	  // Save them to the Shared Preference file
	  String preferenceName = getString(R.string.user_preferences);
	  SharedPreferences sp = getSharedPreferences(preferenceName, 0);

	  Editor editor = sp.edit();
	  editor.putBoolean(getString(R.string.autoRespondPref), autoRespond);
	  editor.putString(getString(R.string.responseTextPref), respondText);
	  editor.putBoolean(getString(R.string.includeLocationPref), includeLoc );
	  editor.putInt(getString(R.string.respondForPref), respondForIndex );
	  editor.commit();

	  // Set the alarm to turn off the auto-responder    
	  setAlarm(respondForIndex);
	}
	
  private BroadcastReceiver stopAutoResponderReceiver = new BroadcastReceiver() {
    @Override
    public void onReceive(Context context, Intent intent) {
      if (intent.getAction().equals(alarmAction)) {
        String preferenceName = getString(R.string.user_preferences);
        SharedPreferences sp = getSharedPreferences(preferenceName, 0);
        
        Editor editor = sp.edit();
        editor.putBoolean(getString(R.string.autoRespondPref), false);
        editor.commit();
      }
    }
  };

  private void setAlarm(int respondForIndex) {
    // Create the alarm and register the alarm intent receiver.
    AlarmManager alarms = (AlarmManager)getSystemService(ALARM_SERVICE);
  
    if (intentToFire == null) {
      Intent intent = new Intent(alarmAction);
      intentToFire = PendingIntent.getBroadcast(getApplicationContext(),0,intent,0);
      IntentFilter filter = new IntentFilter(alarmAction);
      registerReceiver(stopAutoResponderReceiver, filter);
    }
  
    if (respondForIndex < 1)
      // If "disabled" is selected, cancel the alarm.
      alarms.cancel(intentToFire);
    else {
      // Otherwise find the length of time represented by the selection and
      // and set the alarm to trigger after that time has passed.
      Resources r = getResources();
      int[] respondForValues = r.getIntArray(R.array.respondForValues);
      int respondFor = respondForValues [respondForIndex];
  
      long t = System.currentTimeMillis();
      t = t + respondFor*1000*60;
  
      // Set the alarm.
      alarms.set(AlarmManager.RTC_WAKEUP, t, intentToFire);
    }
  }
}