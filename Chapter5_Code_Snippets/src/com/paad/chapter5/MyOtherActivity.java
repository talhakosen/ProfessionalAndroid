package com.paad.chapter5;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

/** Sub-Activity that returns result and closes when a button is clicked */
public class MyOtherActivity extends Activity {
	
	private static String IS_INPUT_CORRECT = "INPUT_CORRECT";
	private static String SELECTED_PISTOL = "SELECTED_PISTOL";
	
	private static boolean inputCorrect = true;
	private static String selectedPistol = ".22";
	private static String selected_horse_id = "horse101";
	
  @Override
  public void onCreate(Bundle icicle) {
    super.onCreate(icicle);
    setContentView(R.layout.othermain);
      
    Button okButton = (Button) findViewById(R.id.ok_button);
    okButton.setOnClickListener(new View.OnClickListener() {
      public void onClick(View view) {
        Uri data = Uri.parse("content://horses/" + selected_horse_id);
    
        Intent result = new Intent(null, data);
        result.putExtra(IS_INPUT_CORRECT, inputCorrect);
        result.putExtra(SELECTED_PISTOL, selectedPistol);
        
        setResult(RESULT_OK, result);    
        finish();
      }
    });

    Button cancelButton = (Button) findViewById(R.id.cancel_button);
    cancelButton.setOnClickListener(new View.OnClickListener() {
      public void onClick(View view) {
        setResult(RESULT_CANCELED, null);
        finish();
      }
    });
    
  }
}