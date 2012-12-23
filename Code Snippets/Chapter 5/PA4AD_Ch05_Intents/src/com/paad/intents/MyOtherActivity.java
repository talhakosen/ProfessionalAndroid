package com.paad.intents;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

public class MyOtherActivity extends Activity {
  
  /**
   * Listing 5-14: Finding the launch Intent in an Activity
   */
  @Override
  public void onCreate(Bundle icicle) {
    super.onCreate(icicle);
    setContentView(R.layout.main);

    Intent intent = getIntent();
    String action = intent.getAction();
    Uri data = intent.getData();
  }

  
  @Override
  public void onStart() {
    super.onStart();

    setContentView(R.layout.selector_layout);
    final ListView listView = (ListView)findViewById(R.id.listView1);
    
    Button okButton = (Button) findViewById(R.id.ok_button);
    okButton.setOnClickListener(new View.OnClickListener() {
      public void onClick(View view) {
        long selected_id = listView.getSelectedItemId();
        
        Intent result = new Intent(Intent.ACTION_PICK, Uri.parse(Long.toString(selected_id)));
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