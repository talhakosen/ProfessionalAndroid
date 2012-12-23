package com.paad.intents;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

public class SelectHorseActivity extends Activity {
  
  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.selector_layout);
    
    final ListView listView = (ListView)findViewById(R.id.listView1);
    
    /**
     * Listing 5-5: Returning a result from a sub-Activity 
     */
    Button okButton = (Button) findViewById(R.id.ok_button);
    okButton.setOnClickListener(new View.OnClickListener() {
      public void onClick(View view) {
        long selected_horse_id = listView.getSelectedItemId();
            
        Uri selectedHorse = Uri.parse("content://horses/" + 
                                       selected_horse_id);
        Intent result = new Intent(Intent.ACTION_PICK, selectedHorse);

        setResult(RESULT_OK, result);
        finish();
      }
    });

    Button cancelButton = (Button) findViewById(R.id.cancel_button);
    cancelButton.setOnClickListener(new View.OnClickListener() {
      public void onClick(View view) {
        setResult(RESULT_CANCELED);
        finish();
      }
    });
  }
}